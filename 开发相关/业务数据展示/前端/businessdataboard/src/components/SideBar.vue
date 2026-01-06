<template>
    <div class="sidebar">
        <div class="logo">
            <i class="pi pi-prime" style="font-size: 1.6rem"></i>
        </div>
        <div class="operation">
            <Button :class="['button', getButtonPatern_home]" icon="pi pi-home" style="" @click="setFunction('home')" v-if="home_button_display" />

            <Button :class="['button', getButtonPatern_upload]" icon="pi pi-cloud-upload" style="" @click="setFunction('upload')" v-if="upload_button_display" />

            <Button :class="['button', getButtonPatern_setting]" icon="pi pi-sliders-h" style="" @click="setFunction('auth')" v-if="setting_button_display" />
        </div>
    </div>
</template>

<script setup>
import Button from 'primevue/button'
import { inject,onMounted,ref, watch } from 'vue';

const sideBar_Function_Activated = inject('sideBar_Function_Activated');


const getButtonPatern_home = ref("deactivated");
const getButtonPatern_upload = ref('deactivated')
const getButtonPatern_setting = ref('deactivated')

const home_button_display = inject('home_button_display')
const upload_button_display = inject('upload_button_display')
const setting_button_display = inject('setting_button_display')



// 设置侧边栏功能状态的函数
const setFunction = (functionName) => {
    console.log("侧边栏激活功能:sideBar_Function_Activated",functionName);
    sideBar_Function_Activated.value = functionName;
    if (functionName === 'home') {
        getButtonPatern_home.value = 'activated'
        getButtonPatern_upload.value = 'deactivated'
        getButtonPatern_setting.value = 'deactivated'
    } else if (functionName === 'upload') {
        getButtonPatern_home.value = 'deactivated'
        getButtonPatern_upload.value = 'activated'
        getButtonPatern_setting.value = 'deactivated'
    }else if (functionName ==='auth') {
        getButtonPatern_home.value = 'deactivated'
        getButtonPatern_upload.value = 'deactivated'
        getButtonPatern_setting.value = 'activated'
    }
};

onMounted(() => {
    setFunction(sideBar_Function_Activated.value)
});
watch(sideBar_Function_Activated, (newValue) => {
    setFunction(newValue)
});


</script>

<style>
@layer reset, primevue, custom;

@layer custom {
    .sidebar {
        display: flex;
        flex-direction: column;
        align-items: center;
        /* justify-content: center; */
        height: 100vh;
        width: 4rem;
        background-color: #f8fafc;
        padding: 1rem;
        box-sizing: border-box;
        border-radius: 20px;
        flex: none;
    }

    .sidebar .logo {
        display: flex;
        align-items: center;
        justify-content: center;

        width: 2.5rem;
        height: 2.5rem;

        margin-bottom: 2.2rem;
        margin-top: 0.5rem;

        border: 1px solid #202020;
        border-radius: 20%;
        flex: none;
    }


    .sidebar .operation {
        display: flex;
        flex-direction: column;
        align-items: center;
        justify-content: center;
        gap: 1rem;
        width: 100%;
        flex: none;
    }

    .sidebar .button {
        width: 2.6rem;
        height: 2.6rem;
        /* background-color: #202020; */
        border-radius: 50%;
        border: none;
        font-size: 1rem;
        flex: none;
    }

    .sidebar .button.activated {

        background-color: #202020;
    }


    .sidebar .button.deactivated {
        background-color: #909090;
    }
}
</style>