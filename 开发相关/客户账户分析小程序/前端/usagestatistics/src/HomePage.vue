<template>
    <div class="homePage-wrapper">

        <div class="homePage-container">

            <div class="svg-block">

            </div>

            <div class="header">
                <!-- svg logo or header content -->
                <img class="header-svg" src="./assets/visual-data.svg" alt="pic" />
                <h1 class="header-title">客户账户分析小程序用户使用情况统计看板</h1>
            </div>

            <div class="body">
                <OverllStat />
                <DeepUsageStat />
                <CoreFunctionUsageStats />
            </div>

            <div class="footer">
            </div>
        </div>
    </div>
</template>

<script setup>
import OverllStat from './components/OverllStats.vue';
import DeepUsageStat from './components/DeepUsageStats.vue';
import CoreFunctionUsageStats from './components/CoreFunctionUsageStats.vue';

</script>

<style>
@layer reset, primevue, custom;

@layer custom {
    .homePage-wrapper {
        width: 100%;
        min-width: 1200px;

        overflow-x: auto;
    }

    
    .svg-block {
        position: absolute;
        width: 100%;
        height: 30rem;

        /* 网格背景 */
        background-image:
            linear-gradient(to right, #e2e8f0 1px, transparent 1px),
            linear-gradient(to bottom, #e2e8f0 1px, transparent 1px);
        background-size: 20px 30px;

        /* 蒙版效果 - 从顶部开始渐隐 */
        -webkit-mask-image: radial-gradient(ellipse 70% 60% at 50% 0%, #000 60%, transparent 100%);
        mask-image: radial-gradient(ellipse 70% 60% at 50% 0%, #000 60%, transparent 100%);

        /* 可选：添加背景颜色 */
        background-color: #f8fafc;
    }




    .homePage-container {
        display: flex;
        flex-direction: column;
        justify-content: flex-start;
        align-items: center;

        gap: 1rem;

        height: auto;
        width: 100%;
        min-width: 1200px;

        background-color: #f4f7f6;

        box-sizing: border-box;


    }

    .homePage-container .header {
        display: flex;

        align-items: center;
        margin-top: 1rem;
        margin-bottom: 1rem;

        height: 8rem;
        width: 1128px;

        z-index: 3;

        /* background-color: rgb(19, 133, 95); */
    }

    .homePage-container .header .header-svg {

        height: 70%;
        width: auto;
        object-fit: contain;
    }

    .header-title {
        margin-left: 2rem;
        letter-spacing: 0.1rem;
        font-size: 25px;
    }

    .homePage-container .body {
        display: flex;
        flex-direction: column;
        gap: 1.5rem;

        width: 1020px;
        height: auto;
        z-index: 3;

        /* background-color: rgb(76, 196, 243); */
    }

}
</style>