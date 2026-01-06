<template>
    <div class="main-operation-area-container">
        <div class="title">
            {{ mainOperationArea_Title }}
        </div>
        <div class="operation">
            <!-- 用户信息 -->
            <div class="user-info">
                <span class="user-role">{{ sysUserRolesForRender }}： </span>
                <span class="user-name">{{ sysUserNameForRender }}</span>


            </div>
            <Button label="退出登录" severity="contrast" size="small" @click="logout()" />
        </div>

    </div>
</template>

<script setup>
import { inject, onMounted, ref } from 'vue';
import { Button } from 'primevue';
import router from '@/router';


const mainOperationArea_Title = ref("业务数据看板");
const sideBar_Function_Activated = inject('sideBar_Function_Activated');

const sysUserNameForRender = inject('sysUserNameForRender');
const sysUserRolesForRender = inject('sysUserRolesForRender');

// watch(sideBar_Function_Activated, (newValue) => {
//     console.log("主页顶部导航栏 ----------------> watch:sideBar_Function_Activated", newValue);
//     if (newValue === "home") {
//         mainOperationArea_Title.value = "业务数据看板";
//     }else if (newValue === "zero_auth") {
//         mainOperationArea_Title.value = "";
//     }
// });

onMounted(() => {
    if (sideBar_Function_Activated.value === "home") {
        mainOperationArea_Title.value = "业务数据看板";
    } else if (sideBar_Function_Activated.value === "zero_auth") {
        mainOperationArea_Title.value = "";
    }
});

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
    .main-operation-area-container {
        display: flex;
        flex-direction: row;
        height: 5rem;
        /* background-color: #f2f2f2; */
        width: 100%;

    }

    .main-operation-area-container .title {
        display: flex;
        align-items: center;

        font-size: 1.5rem;
        font-weight: bold;
        letter-spacing: 0.04rem;
        color: #334155;
        width: 40%;
    }

    .main-operation-area-container .operation {
        display: flex;
        align-items: center;
        justify-content: flex-end;
        gap: 1rem;
        /* background-color: antiquewhite; */
        width: 60%;


        padding-right: 1rem;


    }

    .user-info {
        display: flex;
        flex-direction: row;
        justify-content: center;
        align-items: flex-end;
        margin-right: 1rem;
    }

    .user-name {
        font-weight: 600;
        color: #2c3e50;
        font-size: 0.9rem;
    }

    .user-role {
        font-weight: 600;
        font-size: 0.8rem;
        color: #6c757d;
    }

    .logout-button {
        margin-left: 0.5rem;
        margin-left: 1rem;
    }


}
</style>