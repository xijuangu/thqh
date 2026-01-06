<template>
    <div class="mainContainer">
        <div class="webhead">
            <div class="title-container">
                <div class="head_date">{{ formattedDate }}</div> <!-- 日期显示 -->
            </div>

            <div class="data-container">
                <div class="indicators">
                    <IndicatorBoard /> <!-- 指标卡组件 -->
                </div>

                <div class="tables">
                    <TableTab />    <!-- 表格组件 -->
                </div>

                <div class="linechart">
                    <LineChart />    <!-- 折线图组件 -->
                </div>
            </div>
        </div>
    </div>
</template>

<script setup>
import { ref, onMounted, provide, watch } from 'vue';
import IndicatorBoard from '@/components/IndicatorBoard.vue';
import TableTab from '@/components/TableTab.vue';
import LineChart from '@/components/LineChart.vue';

const formattedDate = ref(''); // 响应式变量：格式化后的日期字符串
const currentDate = ref(''); // 响应式变量：原始日期字符串

// 向子组件（IndicatorBoard/TableTab/LineChart）注入当前日期
provide('currentDate', currentDate);

// 日期格式化工具函数：将原始日期字符串转为"YYYY年MM月DD日"格式
const formatDate = (dateStr) => {
    if (!dateStr) return '';
    const date = new Date(dateStr);
    const year = date.getFullYear();
    const month = String(date.getMonth() + 1).padStart(2, '0'); // 月份从0开始，需+1后补零
    const day = String(date.getDate()).padStart(2, '0'); // 日期从0开始，需补零
    return `${year}年${month}月${day}日`;
};

// 响应式监听：监听currentDate的变化，自动更新格式化日期
watch(currentDate, (newDate) => {
    console.log("home_web中监听到日期变化:", newDate);
    if (newDate) {
        formattedDate.value = formatDate(newDate);
    }
}, { immediate: true });


// 组件挂载完成钩子
onMounted(() => {
    console.log("home_web mounted, currentDate:", currentDate.value);

    // 若currentDate初始有值，初始化格式化日期
    if (currentDate.value) {
        formattedDate.value = formatDate(currentDate.value);
    }
});

</script>

<style scoped>
.mainContainer {

    /* background: #f8f9fa; */
    min-height: 100%;

    position: relative;

    background-size: cover;
    background-position: center;
    background-repeat: no-repeat;
    /* 创建线性渐变遮罩层 */
    background-image:
        linear-gradient(to top, rgba(255, 255, 255, 0), rgba(255, 255, 255, 0)),
        url('@/assets/主背景.png');



}

.webhead {
    /* margin-bottom: 30px; */
    overflow: hidden;
    width: 100%;
    /* height: 400px; */
}

.title-container {
    width: 100%;
    background-image:
        linear-gradient(to top, rgba(0, 0, 0, 0.1), rgba(0, 0, 0, 0)),
        url('@/assets/网页标题背景图.png');
    background-size: 100% auto;
    /* 让背景图片宽度自适应，高度按比例缩放 */
    background-repeat: no-repeat;
    display: block;
    padding-top: calc(420/1920*100%);
    /* padding-top: 0; */
    position: absolute;

    background-color: rgba(255, 255, 255, 0.8);
            /* 模糊半径可以根据需要调整 */
            backdrop-filter: blur(10px);
}

.title-container .head_date {
    height: 30px;
    width: 240px;
    font-size: 30px;
    position: absolute;
    background: none;
    color: #2d3092;
    font-weight: 600;
    bottom: 18%;
    right: 20px;
    text-align: right;
}

.data-container {
    position: relative;
    margin-top: 18%;
    width: 100%;
    /* height: 175vh; */
    height: auto;
    background: none;
}

.head_title {
    margin: 0;
    font-size: 28px;
    font-weight: 600;
    color: #2c3e50;
    background: linear-gradient(45deg, #2c3e50, #3498db);
    -webkit-background-clip: text;
    -webkit-text-fill-color: transparent;
    text-shadow: 2px 2px 4px rgba(0, 0, 0, 0.1);
}

.head_date {
    font-size: 20px;
    font-weight: 500;
    color: #2d3092;
    padding: 8px 16px;
    background: linear-gradient(135deg, #f8f9fa 0%, #e9ecef 100%);
    border-radius: 8px;
    box-shadow: 0 2px 4px rgba(0, 0, 0, 0.05);
}

.title-divider {
    height: 3px;
    margin-top: 20px;
    background: linear-gradient(90deg,
            #4ba6f5 0%,
            #33cc99 20%,
            #fdca30 40%,
            #f79400 60%,
            #f34235 80%,
            #a560e8 100%);
    border-radius: 3px;
    box-shadow: 0 2px 4px rgba(0, 0, 0, 0.05);
}

.indicators,
.tables,
.linechart {
    margin-bottom: 24px;
}

/* 响应式布局调整 */
@media (max-width: 768px) {
    .title-container {
        height: 150px;
    }

    .head_title {
        font-size: 24px;
    }

    .head_date {
        font-size: 16px;
    }
}

@media (max-width: 480px) {
    .mainContainer {
        padding: 12px;
    }

    .webhead {
        padding: 16px;
        margin-bottom: 20px;
    }

    .head_title {
        font-size: 20px;
    }

    .head_date {
        font-size: 14px;
        padding: 6px 12px;
    }

    .title-container {
        height: 120px;
    }
}
</style>