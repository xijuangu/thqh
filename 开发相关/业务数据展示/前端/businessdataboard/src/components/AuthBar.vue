<template>
    <div class="manager-bar-container">
        <Toolbar style="border-radius: 3rem; padding: 1rem 1rem 1rem 1.5rem">
            <template #start>
                <div class="operation-area">
                    <img class="manager-bar-logo" src="@/assets/权限管理.png" alt="管理员Logo">
                    <Button :class="['upload-btn', userManageMent_btn_active]" label="用户管理" text plain
                        @click="setFunction('userManagement')" />
                    <Button :class="['maintenance-btn', roleAuth_btn_active]" label="角色权限管理" text plain
                        @click="setFunction('roleAuthManagement')" />
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
const userManageMent_btn_active = ref('active')
const roleAuth_btn_active = ref('')


const setFunction = (functionName) => {
    maintainance_function.value = functionName
    if (functionName === 'userManagement') {
        userManageMent_btn_active.value = 'active'
        roleAuth_btn_active.value = ''
    }
    else if (functionName === 'roleAuthManagement') {
        roleAuth_btn_active.value = 'active'
        userManageMent_btn_active.value = ''
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