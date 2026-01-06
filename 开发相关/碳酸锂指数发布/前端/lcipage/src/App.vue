<template>
    <!-- 
    设备适配渲染逻辑：
    - 当检测为手机设备时，渲染移动端提示组件（MobileNotice）
    - 非手机设备（如电脑、平板）时，渲染路由匹配的页面组件（router-view）
    注：平板设备通过 UserAgent 识别排除，避免与手机共用提示组件
  -->
  <mobile-notice v-if="isMobileDevice" />
  <router-view v-else></router-view>
</template>

<script setup>
import { ref, onMounted, onUnmounted } from 'vue'
// 导入移动端提示组件
import MobileNotice from './components/MobileNotice.vue'

const isMobileDevice = ref(false) //标记当前设备是否为手机，初始值为 false（默认非手机设备）


/**
 * 设备检测核心函数：通过 UserAgent 识别设备类型
 * 逻辑说明：
 * 1. 先获取浏览器 UserAgent 并转为小写（统一匹配格式，避免大小写问题）
 * 2. 分别匹配手机、平板设备特征：
 *    - 手机设备：包含 mobile/android/iphone/ipod 关键词
 *    - 平板设备：包含 ipad/tablet 等关键词（排除带 mobile 的安卓设备，避免误判）
 * 3. 最终判定：仅当设备为手机且非平板时，才标记 isMobileDevice 为 true
 */
const checkDevice = () => {
  // 获取浏览器 UserAgent（包含设备、浏览器信息的字符串）
  const userAgent = navigator.userAgent.toLowerCase()

  // 匹配手机设备特征（包含移动设备关键词）
  const isMobile = /mobile|android|iphone|ipad|ipod/i.test(userAgent)

  // 匹配平板设备特征（排除带 mobile 的安卓设备，避免将安卓平板误判为手机）
  const isTablet = /(ipad|tablet|playbook|silk)|(android(?!.*mobile))/i.test(userAgent)

  // 仅手机设备（非平板）才触发移动端提示
  const isPhone = isMobile && !isTablet

  // 更新响应式状态
  isMobileDevice.value = isPhone
}

// 组件挂载完成钩子
onMounted(() => {
  // 首次执行设备检测
  checkDevice() 

  // 监听浏览器窗口大小变化，实时更新响应式状态
  window.addEventListener('resize', checkDevice)
})

// 组件卸载时移除事件监听
onUnmounted(() => {
  window.removeEventListener('resize', checkDevice)
})
</script>

<style>
#app {
  min-height: 100%;
  background: none;
}
</style>
