<template>
    <div class="main-page-container">
        <Toast />
        <ConfirmDialog group="positioned"></ConfirmDialog>
        <div class="left">
            <SideBar />
        </div>
        <div class="right">
            <div class="main-operation-area" v-if="sideBar_Function_Activated === 'home' || sideBar_Function_Activated === 'zero_auth'">
                <MainOperationArea />
            </div>

            <!-- 只有当sideBar_Function_Activated值为home时才显示PeriodCompareDataBoard、DataBoard -->

            <div class="main-content-area" v-if="sideBar_Function_Activated === 'home'">
                <div class="data-board-container">
                    <DataBoard />
                </div>

                <div class="period-compare-data-board-container">
                    <PeriodCompareDataBoard />
                </div>
            </div>

            <!-- <TestComponent v-if="sideBar_Function_Activated === 'home'"/> -->

            <ManagerComponent v-if="sideBar_Function_Activated === 'upload'" />
            <AuthComponent v-if="sideBar_Function_Activated === 'auth'" />

            <ErrorComponent
                v-if="errorComponentShow">
            </ErrorComponent>
        </div>
    </div>

</template>


<script setup>
import SideBar from '@/components/SideBar.vue'
import MainOperationArea from '@/components/MainOperationArea.vue';
import PeriodCompareDataBoard from '@/components/PeriodCompareDataBoard.vue';
import DataBoard from '@/components/DataBoard.vue';
import ManagerComponent from '@/components/ManagerComponent.vue';
import AuthComponent from '@/components/AuthComponent.vue';
import ErrorComponent from '@/components/ErrorPage.vue';

import Toast from 'primevue/toast';
// import TestComponent from './components/TestComponent.vue';

import { useConfirm } from "primevue/useconfirm";
import { useToast } from "primevue/usetoast";

import { onMounted, provide, ref, watch } from 'vue';

import 'primeicons/primeicons.css'
import axiosInstance from '../axiosConfig.js';

import { useRoute } from 'vue-router';

const route = useRoute();

//注册通知窗口组件
const confirm = useConfirm();
const toast = useToast();

// 响应式变量：当前激活的侧边栏功能
const sideBar_Function_Activated = ref("");
const DataBoard_operationRecord = ref('');
const operation_message = ref('');

const sysuser_id = localStorage.getItem('userid');

const userInfoDetail = ref(null)
const sysUserNameForRender = ref("");
const sysUserRolesForRender = ref("");
const sysUserAuthFunction = ref([]);
provide('sysUserNameForRender', sysUserNameForRender)
provide('sysUserRolesForRender', sysUserRolesForRender)
provide('sysUserAuthFunction', sysUserAuthFunction)

const home_button_display = ref(false)
const upload_button_display = ref(false)
const setting_button_display = ref(false)
provide('home_button_display', home_button_display)
provide('upload_button_display', upload_button_display)
provide('setting_button_display', setting_button_display)

const error_type = ref('')
provide('error_type', error_type)

const axiosState = ref(true)
provide('axiosState', axiosState)

const errorComponentShow = ref(true)

// 基于当前角色的功能权限决定按钮是否显示
const computed_button_display = () => {
    if (sysUserAuthFunction.value.includes('home')) {
        home_button_display.value = true
    }
    if (sysUserAuthFunction.value.includes('maintainance')) {
        upload_button_display.value = true
    }
    if (sysUserAuthFunction.value.includes('authsetting')) {
        setting_button_display.value = true
    }
    console.log("HomePage.vue: computed_button_display", home_button_display.value, upload_button_display.value, setting_button_display.value)
}

// 获取当前登录用户信息
const getSysUserInfoDetail = () => {
    axiosInstance.post('/sysUser/getsysuserinfodetail', {
        sysuser_id: sysuser_id
    }).then(res => {

        if (res.data.code === 0) {
            userInfoDetail.value = res.data.data
            sysUserNameForRender.value = userInfoDetail.value.sysuser_name
            sysUserRolesForRender.value = getRoleText(userInfoDetail.value.roles)
            sysUserAuthFunction.value = userInfoDetail.value.authFunctions

            console.log("主页 ---------------------> userInfoDetail", sysUserNameForRender.value, sysUserRolesForRender.value, sysUserAuthFunction.value)
            computed_button_display()
            // 基于功能权限，选定侧边栏默认激活的功能
            if (sysUserAuthFunction.value.includes('home')) {
                sideBar_Function_Activated.value = 'home'
                errorComponentShow.value = false
            } else if (sysUserAuthFunction.value.includes('maintainance')) {
                sideBar_Function_Activated.value = 'upload'
                errorComponentShow.value = false
            } else if (sysUserAuthFunction.value.includes('authsetting')) {
                sideBar_Function_Activated.value = 'auth'
                errorComponentShow.value = false
            } else {
                sideBar_Function_Activated.value = 'zero_auth'
                error_type.value = 'zero_auth'
            }
        } else {
            toast.add({ severity: 'error', summary: 'error', detail: res.data.message, life: 3000 });
            sideBar_Function_Activated.value = 'server_error'
            error_type.value = 'server_error'
        }

    }).catch(err => {
        sideBar_Function_Activated.value = 'network_error'
        error_type.value = 'network_error'
        toast.add({ severity: 'error', summary: 'error', detail: err.message, life: 3000 });
    }).finally(() => {
        axiosState.value = false
    })
}

// 角色文本映射
const getRoleText = (roles) => {
    // 将currentUser.value.roles数组中的角色名称转换为文本

    let roleNames = "";
    roles.forEach((role) => {
        roleNames += role + " ";
    });
    return roleNames === "" ? "无角色" : roleNames;

};

onMounted(() => {
    const userId = localStorage.getItem('userid');

    console.log("HomePage.vue: onMounted", userId)

    // 若没有 userid，跳转到登录页
    if (!userId) {
        route.push('/login'); // 替换为你的登录页路由路径
        return; // 跳转到登录页后，后续逻辑不再执行
    }

    // 获取当前登录用户信息
    getSysUserInfoDetail();
});

provide("sideBar_Function_Activated", sideBar_Function_Activated);

provide("DataBoard_operationRecord", DataBoard_operationRecord)
provide('update_DataBoard_operationRecord', (update_DataBoard_operationRecord) => {
    DataBoard_operationRecord.value = update_DataBoard_operationRecord
});

provide('operation_message', operation_message)
provide('update_operation_message', (update_operation_message) => {
    operation_message.value = update_operation_message
});



const confirmPosition = (position, action) => {
    // console.log('弹窗')
    let message = '';
    if (action === 'search_failed' || action === 'export_failed' || action === 'filter_failed' || action === 'upload_failed') {
        message = operation_message.value
    } else if (action === 'search_success' || action === 'export_success' || action === 'upload_success') {
        toast.add({ severity: 'success', summary: 'success', detail: operation_message.value, life: 3000 });
        DataBoard_operationRecord.value = '';
        return;
    }

    confirm.require({
        group: 'positioned',
        message: message,
        header: '提示',
        icon: 'pi pi-info-circle',
        position: position,
        rejectProps: {
            label: '取消',
            severity: 'secondary',
            text: true
        },
        acceptProps: {
            label: '确定',
            text: true
        },
        accept: () => {
            DataBoard_operationRecord.value = ''
        },
        reject: () => {
            DataBoard_operationRecord.value = ''
        }
    });
};

watch(DataBoard_operationRecord, (newValue) => {
    // console.log("检测到DataBoard_operationRecord变化", newValue)
    if (newValue === 'search_failed' ||
        newValue === 'export_failed' ||
        newValue === 'search_success' ||
        newValue === 'export_success' ||
        newValue === 'filter_success' ||
        newValue === 'filter_failed' ||
        newValue === 'upload_success' ||
        newValue === 'upload_failed') {
        confirmPosition('top', newValue)
    }

});

watch(sideBar_Function_Activated, (newValue) => {
    console.log("HomePage.vue: sideBar_Function_Activated", newValue)
});

</script>

<style>
@layer reset, primevue, custom;

@layer custom {
    .main-page-container {
        display: flex;
        flex-direction: row;

        height: 100%;
        width: 100%;

        position: relative;
        top: 0;
        left: 0;

        background-color: #fff;

        overflow: hidden;
    }

    .main-page-container .left {
        display: flex;
        flex-direction: row;
        flex: none;

        width: 5.3rem;
        height: 100%;

        /* position: relative; */
    }

    .main-page-container .right {
        display: flex;
        flex-direction: column;
        flex: none;
        gap: 1rem;

        height: 100%;
        width: calc(100% - 5.3rem);
        min-width: 60rem;
        overflow: hidden;


    }

    .main-operation-area {
        flex: none;
    }

    /* ----------------------------------------------------------- */
    .main-content-area {
        display: flex;
        flex-direction: column;
    }

    .main-operation-area {
        height: 5rem;
        flex: none;
    }

    .data-board-container {
        height: auto;
        flex: none;
        margin-bottom: 2rem;
    }

    .period-compare-data-board-container {
        height: auto;
        flex: none;

    }

}
</style>