<!-- 当后端服务器无法接收网络请求时，显示此组件 -->
<template>
    <div class="error-container">
        <div class="loading" v-if="axiosState">
            <ProgressSpinner style="width: 50px; height: 50px" strokeWidth="8" fill="transparent"
                animationDuration=".5s" aria-label="Custom ProgressSpinner" />
        </div>
        <div class="error-info-container" v-if="!axiosState">
            <h1 class="error-info">{{ error_info }} </h1>
            <img class="error-img" :src="error_img" alt="network error" />

        </div>
    </div>
</template>

<script setup>
import { inject, ref, watch } from 'vue'
import ProgressSpinner from 'primevue/progressspinner'
const error_type = inject("error_type")
const axiosState = inject("axiosState")

const error_info = ref("")
const error_img = ref("")

watch(error_type, () => {
    console.log(error_type.value)
    if (error_type.value === "network_error") {
        error_info.value = "请求失败了！请检查网络，或请联系管理员。"
        error_img.value = require('../assets/404.png');
    } else if (error_type.value === "server_error") {
        error_info.value = "服务器崩溃了！请稍后再试，或请联系管理员。"
        error_img.value = require('../assets/500.png');
    } else if (error_type.value === "zero_auth") {
        error_info.value = "该账号无任何功能权限！"
        error_img.value = require('../assets/无功能权限.png');
    }
})

</script>

<style>
@layer reset, primevue, custom;

@layer custom {
    .error-container {
        height: 100vh;
        width: 100%;

        display: flex;
        align-items: center;
        justify-content: center;
    }

    .error-info-container {
        display: flex;
        align-items: center;
        justify-content: center;
    }

    .error-info {
        font-size: 1.7rem;
        margin-right: 4rem;
    }

    .error-img {
        height: 300px;
    }
}
</style>