<template>
    <div class="mobile-notice">
        <div class="notice-content">
            <i class="notice-icon">🖥️</i>
            <h2>请使用电脑访问</h2>
            <p>为了确保最佳的浏览体验，本网页仅支持在电脑端查看。</p>
            <p>请使用电脑浏览器访问本页面。</p>
            <p>温馨提示：若已通过pc端访问，为保证最佳的浏览体验，请在合适界面宽度下查看本界面</p>
        </div>
    </div>
</template>

<script setup>
// 无需额外的逻辑
</script>

<style scoped>
.mobile-notice {
    position: fixed;
    top: 0;
    left: 0;
    width: 100%;
    height: 100vh;
    display: flex;
    justify-content: center;
    align-items: center;
    background-color: #f5f7fa;
    padding: 20px;
    box-sizing: border-box;
}

.notice-content {
    text-align: center;
    background: white;
    padding: 30px;
    border-radius: 12px;
    box-shadow: 0 2px 12px rgba(0, 0, 0, 0.1);
    max-width: 90%;
}

.notice-icon {
    font-size: 48px;
    margin-bottom: 20px;
    display: block;
}

h2 {
    color: #303133;
    margin-bottom: 16px;
    font-size: 24px;
}

p {
    color: #606266;
    margin: 8px 0;
    font-size: 16px;
    line-height: 1.5;
}
</style> 