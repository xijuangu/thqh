<template>
  <div ref="vantaRef" class="vanta-container">
    <div class="login-container">
      <!-- 头部LOGO和标题 -->
      <div class="header">
        <img src="@/assets/魔方.png" alt="logo" />
        <span class="title">欢迎使用业务数据看板</span>
        <span class="sub-title">请输入用户名和密码登录</span>
      </div>

      <!-- 提示信息 -->
      <div class="info-container">
        <div v-if="showText" :class="['animate__animated', currentAnimation]" ref="textRef">
          <i :class="InfoIcon" style="font-size: 0.8rem;margin-right: 0.3rem; color: #FF412C;"></i>
          <span class="info-text" style="font-size: 12px; font-weight: 600; color: #FF412C;">{{ TextContent }}</span>
        </div>
      </div>

      <!-- 登录表单 -->
      <form @submit.prevent="login">
        <div class="body">
          <!-- 用户名输入 -->
          <div class="form-group">
            <label class="form-lable" for="username">用户名：</label>
            <InputText class="form-input" type="text" id="username" v-model="username" required />
          </div>

          <!-- 密码输入 + 忘记密码 -->
          <div class="form-group">
            <div class="form-operation">
              <label class="form-lable" for="password">密码：</label>
              <span
                style="text-align: right;color: #9EF1A1; font-size: 12px; letter-spacing: 1px;text-decoration: underline; cursor: pointer;"
                @click="handleforgetPassword()">忘记密码？</span>
            </div>
            <InputText class="form-input" type="password" id="password" v-model="password" required />
          </div>

          <!-- 记住用户名密码（完善部分） -->
          <div class="form-group">
            <div class="remember-wrapper">
              <!-- Checkbox绑定rememberUser状态 -->
              <Checkbox 
                class="checkbox" 
                v-model="rememberUser" 
                inputId="remember" 
                name="remember" 
                value="remember"
                :binary="true"
                size="small"
                style="margin: 0;" 
              />
              <!-- 关联Checkbox，点击文字也能选中 -->
              <label 
                for="remember"
                class="remember-label"
              >
                记住用户名、密码
              </label>
            </div>
            <!-- 登录按钮 -->
            <Button 
              label="登录" 
              raised 
              @click="login" 
              style="background-color: #202020; border: none; width: 100%; margin-top: 0.5rem;" 
            />
          </div>
        </div>
      </form>
    </div>
  </div>
</template>

<script setup>
import { ref, onMounted, onBeforeUnmount, watch } from 'vue'
import { useRouter } from 'vue-router';
import axiosInstance from '../axiosConfig.js';
// 引入PrimeVue组件
import InputText from 'primevue/inputtext';
import Button from 'primevue/button';
import Checkbox from 'primevue/checkbox';
import 'primeicons/primeicons.css'
// 引入Animate.css（确保动画生效）
import 'animate.css';


// 3D背景相关
import * as THREE from 'three';
import NET from 'vanta/src/vanta.net.js';

const router = useRouter();

// 1. 核心状态：用户名、密码、记住状态
const username = ref('');
const password = ref('');
const rememberUser = ref(false); // 控制“记住”复选框的选中状态

// 2. Vanta背景相关
const vantaRef = ref(null);
const vantaEffect = ref(null);

// 3. 提示信息相关
const showText = ref(false);
const TextContent = ref('');
const InfoIcon = ref('');
const currentAnimation = ref('animate__fadeInDown');
const textRef = ref(null);
let hideTimer = null;

// ------------------------------
// 完善：记住密码核心逻辑
// ------------------------------
// 页面挂载时：读取localStorage，自动填充已保存的账号密码
onMounted(() => {
  // 初始化Vanta背景
  if (vantaRef.value) {
    vantaEffect.value = NET({
      el: vantaRef.value,
      THREE: THREE,
      mouseControls: true,
      touchControls: true,
      gyroControls: false,
      minHeight: window.innerHeight,
      minWidth: window.innerWidth,
      scale: 0.5,
      scaleMobile: 1.00,
      color: "#ffffff",
      backgroundColor: "#c0c0c0",
      maxDistance: 21.00,
    })
  }

  // 读取localStorage中的保存信息
  const savedUser = localStorage.getItem('savedUsername');
  const savedPwd = localStorage.getItem('savedPassword');
  const isRemembered = localStorage.getItem('isRemembered') === 'true'; // 转布尔值

  // 自动填充账号密码
  if (savedUser && isRemembered) {
    username.value = savedUser;
    password.value = savedPwd || ''; // 密码可选填（防止存储异常）
    rememberUser.value = true; // 复选框自动选中
  }
});

// 页面卸载前：销毁Vanta背景
onBeforeUnmount(() => {
  if (vantaEffect.value) {
    vantaEffect.value.destroy();
  }
  // 清除提示信息定时器（避免内存泄漏）
  if (hideTimer) clearTimeout(hideTimer);
});

// ------------------------------
// 登录逻辑（新增：根据“记住”状态保存账号密码）
// ------------------------------
const login = async () => {
  // console.log("登录方法触发", username.value, " ", password.value)

  try {
    const response = await axiosInstance.post('/sysUser/login', {
      username: username.value,
      password: password.value
    });

    if (response.data.code === 0) {
      // 1. 登录成功：根据rememberUser状态决定是否保存账号密码
      if (rememberUser.value) {
        // 保存到localStorage（长期有效，除非手动清除）
        localStorage.setItem('savedUsername', username.value);
        localStorage.setItem('savedPassword', password.value); // 注意：实际项目建议加密存储密码！
        localStorage.setItem('isRemembered', 'true'); // 标记“已记住”状态
      } else {
        // 不记住：清除之前保存的信息
        localStorage.removeItem('savedUsername');
        localStorage.removeItem('savedPassword');
        localStorage.removeItem('isRemembered');
      }

      // 2. 保存登录相关信息（原有逻辑保留）
      const loginTime = new Date().getTime();
      localStorage.setItem('userid', response.data.data);
      localStorage.setItem('loginTime', loginTime);
      localStorage.setItem('currentTime', loginTime);

      console.log('登录成功！用户id',response.data.data);

      // 3. 跳转到首页
      router.push({ path: '/BDDSPage', query: { userid: response.data.data } });
    } else {
      handleIrregularInfo(response.data.code, response.data.message);
    }
  } catch (error) {
    // console.log(error);
    showTip('pi pi-exclamation-circle', '登录失败，请检查网络连接！');
  }
};

// ------------------------------
// 其他辅助方法（原有逻辑优化）
// ------------------------------
// 忘记密码提示
const handleforgetPassword = () => {
  showTip('pi pi-question-circle', '请联系管理员获取或重置您的账号信息！');
};

// 异常信息提示
const handleIrregularInfo = (code, msg) => {
  let icon = 'pi pi-info-circle';
  let text = msg || '未知错误，请联系管理员！';
  
  switch (code) {
    case 3001:
      console.log('密码不正确');
      break;
    case 3002:
      console.log('用户名不存在');
      break;
    case 3003:
      console.log('账号被禁用，请联系管理员！');
      break; // 使用默认图标和文字
    default:
      text = '未知错误，请联系管理员！';
  }
  
  showTip(icon, text);
};

// 统一提示信息方法（优化代码复用）
const showTip = (icon, text) => {
  showText.value = true;
  InfoIcon.value = icon;
  TextContent.value = text;

  // 清除之前的定时器
  if (hideTimer) clearTimeout(hideTimer);

  // 2秒后自动隐藏（带淡出动画）
  hideTimer = setTimeout(() => {
    currentAnimation.value = 'animate__fadeOutDown';
    setTimeout(() => {
      showText.value = false;
    }, 1000); // 等待动画结束
  }, 2000);
};

// 监听提示信息状态（原有逻辑保留）
watch(showText, (newVal) => {
  if (newVal) {
    currentAnimation.value = 'animate__fadeInDown';
  }
});
</script>

<style>
@layer reset, primevue, custom;

@layer custom {
  /* 背景容器 */
  .vanta-container {
    position: relative;
    width: 100vw;
    height: 100vh;
    overflow: hidden;
  }

  /* 登录框容器 */
  .login-container {
    position: absolute;
    top: 50%;
    left: 50%;
    transform: translate(-50%, -50%);
    width: 300px;
    height: auto;
    padding: 1rem 0 3rem;
    border-radius: 12px;
    display: flex;
    flex-direction: column;
    align-items: center;
  }

  /* 玻璃态样式（原有保留） */
  .login-container {
    --filter-glass3d: blur(1px) brightness(1) saturate(1);
    --color-glass3d: transparent;
    --noise-glass3d: url("https://www.transparenttextures.com/patterns/egg-shell.png");
    position: relative;
    z-index: 4;
    box-shadow:
      0 0 0.75px hsl(205 20% 10% / 0.2),
      0.7px 0.8px 1.2px -0.4px hsl(205 20% 10% / 0.1),
      1.3px 1.5px 2.2px -0.8px hsl(205 20% 10% / 0.1),
      2.3px 2.6px 3.9px -1.2px hsl(205 20% 10% / 0.1),
      3.9px 4.4px 6.6px -1.7px hsl(205 20% 10% / 0.1),
      6.5px 7.2px 10.9px -2.1px hsl(205 20% 10% / 0.1),
      8px 9px 14px -2.5px hsl(205 20% 10% / 0.2);
  }

  .login-container::before,
  .login-container::after {
    content: "";
    position: absolute;
    inset: 0;
    pointer-events: none;
    border-radius: inherit;
    overflow: hidden;
  }

  .login-container::before {
    z-index: 3;
    -webkit-backdrop-filter: var(--filter-glass3d);
    backdrop-filter: var(--filter-glass3d);
    background-color: var(--color-glass3d);
    background-image: var(--noise-glass3d);
    background-size: 100px;
    background-repeat: repeat;
  }

  .login-container::after {
    z-index: 5;
    box-shadow:
      inset 2px 2px 1px -3px hsl(205 20% 90% / 0.8),
      inset 4px 4px 2px -6px hsl(205 20% 90% / 0.3),
      inset 1.5px 1.5px 1.5px -0.75px hsl(205 20% 90% / 0.15),
      inset 1.5px 1.5px 0.25px hsl(205 20% 90% / 0.03),
      inset 0 0 0.25px 0.5px hsl(205 20% 90% / 0.03);
  }

  .login-container>* {
    position: relative;
    z-index: 6;
  }

  /* 头部样式 */
  .header {
    display: flex;
    flex-direction: column;
    align-items: center;
    gap: 0.6rem;
    color: rgb(31, 31, 32);
    width: 100%;
    height: 7rem;
    padding-top: 1.12rem;
  }

  .header img {
    width: 20px;
    height: 20px;
  }

  .header .title {
    font-size: 1.1em;
    font-weight: bold;
  }

  .header .sub-title {
    font-size: 0.8em;
  }

  /* 表单主体 */
  .body {
    display: flex;
    flex-direction: column;
    align-items: center;
    justify-content: center;
    gap: 14px;
    width: 230px;
  }

  .body .form-group {
    display: flex;
    flex-direction: column;
    align-items: flex-start;
    width: 100%;
  }

  .form-group .form-lable {
    font-size: 14px;
    margin-bottom: 6px;
    color: rgb(31, 31, 32);
  }

  .form-group .form-input {
    width: 100%;
    padding: 10px;
    border-radius: 8px;
    border: none;
    font-size: 16px;
    color: rgb(31, 31, 32);
    box-sizing: border-box; /* 防止padding撑大宽度 */
  }

  /* 密码行操作区 */
  .form-operation {
    display: flex;
    width: 100%;
    justify-content: space-between;
    align-items: center;
  }

  /* 提示信息容器 */
  .info-container {
    width: 100%;
    height: 1.5rem;
    margin-bottom: 1rem;
    display: flex;
    justify-content: center;
    align-items: center;
    gap: 0.7rem;
  }

  /* ------------------------------
     完善：记住密码样式（视觉对齐优化）
     ------------------------------ */
  .remember-wrapper {
    display: flex;
    align-items: center;
    gap: 0.3rem;
    margin-bottom: 1rem;
  }

  .remember-label {
    font-size: 12px;
    color: rgb(31, 31, 32);
    letter-spacing: 1px;
    margin: 0;
    line-height: 1;
    cursor: pointer; /* 提示可点击 */
  }
}
</style>