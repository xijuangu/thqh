<template>
    <div class="manager-bar-container">
        <Toolbar style="border-radius: 3rem; padding: 1rem 1rem 1rem 1.5rem">
            <template #start>
                <div class="operation-area">
                    <img class="manager-bar-logo" src="@/assets/关系维护.png" alt="管理员Logo">
                    <Button :class="['upload-btn', upload_btn_active]" label="文件上传" text plain
                        @click="setFunction('uploadFile')" />
                    <Button :class="['maintenance-btn', maintenance_btn_active]" label="客户类维护" text plain
                        @click="setFunction('clientClassMaintenance')" />
                    <Button :class="['maintenance-btn', PersonnelManagement_btn_active]" label="业务人员管理" text plain
                        @click="setFunction('PersonnelManagement')" />
                    <Button :class="['maintenance-btn', DepartmentManagement_btn_active]" label="部门管理" text plain
                        @click="setFunction('DepartmentManagement')" />
                        
                </div>
            </template>

            <template #end>
                <div class="user-area">
                    <span class="manager-name">{{ sysUserRolesForRender }}： {{ sysUserNameForRender }}</span>
                    <Button label="退出登录" severity="contrast" size="small" @click="logout()" />
                </div>
            </template>
        </Toolbar>
    </div>
</template>

<script setup>
import Toolbar from 'primevue/toolbar';
import Button from 'primevue/button';
import { inject, ref } from 'vue';
import router from '@/router';

const maintainance_function = inject("maintainance_function")
const upload_btn_active = ref('active')
const maintenance_btn_active = ref('')
const PersonnelManagement_btn_active = ref('')
const DepartmentManagement_btn_active = ref('')


const setFunction = (functionName) => {
    maintainance_function.value = functionName
    if (functionName === 'uploadFile') {
        upload_btn_active.value = 'active'
        maintenance_btn_active.value = ''
        PersonnelManagement_btn_active.value = ''
        DepartmentManagement_btn_active.value = ''
    }
    else if (functionName === 'clientClassMaintenance') {
        upload_btn_active.value = ''
        maintenance_btn_active.value = 'active'
        PersonnelManagement_btn_active.value = ''
        DepartmentManagement_btn_active.value = ''
    }else if (functionName === 'PersonnelManagement') {
        upload_btn_active.value = ''
        maintenance_btn_active.value = ''
        PersonnelManagement_btn_active.value = 'active'
        DepartmentManagement_btn_active.value = ''
    }else if (functionName === 'DepartmentManagement') {
                upload_btn_active.value = ''
        maintenance_btn_active.value = ''
        PersonnelManagement_btn_active.value = ''
        DepartmentManagement_btn_active.value = 'active'
    }

}

// let currentUserInfoDetail = inject('userInfoDetail');
const sysUserNameForRender = inject('sysUserNameForRender');
const sysUserRolesForRender = inject('sysUserRolesForRender');


const logout = () => {
    localStorage.removeItem('token');
    localStorage.removeItem('loginTime');
    localStorage.removeItem('currentTime');
    router.push('/login');
};

</script>

<style>
@layer reset, primevue, custom;

@layer custom {
    .manager-bar-container {
        /* margin-top: 1rem; */
        /* margin-bottom: 2rem; */

        /* position: absolute; */
    }

    .manager-bar-container .operation-area {
        display: flex;
        align-items: center;
        gap: 1.5rem;
    }

    .manager-bar-container .operation-area .manager-bar-logo {
        width: 2.5rem;
        height: 2.5rem;
        /* border-radius: 50%; */
    }


    .manager-bar-container .user-area {
        display: flex;
        align-items: center;
        gap: 1rem;
    }

    .manager-bar-container .upload-btn.active {
        background-color: #202020;
        color: white;
    }

    .manager-bar-container .maintenance-btn.active {
        background-color: #202020;
        color: white;
    }
}
</style>