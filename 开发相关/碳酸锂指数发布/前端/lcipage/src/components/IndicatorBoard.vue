<template>
    <div class="board-container">
        <!-- 综合指数指标卡片 -->
        <div class="composite-index-card">
            <div class="composite-index-content">
                <div class="composite-index-title">通惠碳酸锂综合成本指数</div>
                <div class="composite-index-value">
                    <span class="value">{{ formatValue(indicators[7].value) }}</span>
                    <span class="unit">元/吨</span>
                </div>
            </div>
        </div>

        <!-- 原有的指标卡片 -->
        <div class="board-card">
            <div class="board-header">
                <h2>碳酸锂各工艺今日理论成本价</h2>
            </div>
            <div class="indicators-mainContainer">
                <!-- 仅渲染前7个工艺指标，排除第8个综合指数 -->
                <div class="indicator-card" v-for="(item, index) in indicators.slice(0, 7)" :key="index"
                    :class="item.className">
                    <div class="indicator-top">
                        <div class="indicator-icon">
                            <img :src="item.icon" alt="Indicator Icon">
                        </div>
                        <div class="indicator-title">{{ item.title }}</div>
                    </div>

                    <div class="indicator-bottom">
                        <div class="indicator-value">{{ formatValue(item.value) }}</div>
                        <div class="indicator-unit">元/吨</div>
                    </div>
                </div>
            </div>
        </div>
    </div>
</template>

<script setup>
import { ref, onMounted, inject } from 'vue';
import axios from 'axios';

// 父组件（home_web.vue）通过provide提供的currentDate
const currentDate = inject('currentDate');

// 响应式指标数据数组：
const indicators = ref([
    { title: '电碳现货价', value: null, className: 'ec-spot', icon: new URL('@/assets/指标卡的图标.png', import.meta.url).href },
    { title: '锂辉石工艺', value: null, className: 'spodumene', icon: new URL('@/assets/指标卡的图标.png', import.meta.url).href },
    { title: '低品位锂云母工艺', value: null, className: 'lg-lepidolite', icon: new URL('@/assets/指标卡的图标.png', import.meta.url).href },
    { title: '高品位锂云母工艺', value: null, className: 'hg-lepidolite', icon: new URL('@/assets/指标卡的图标.png', import.meta.url).href },
    { title: '磷酸铁锂正极片工艺', value: null, className: 'lfp-cathode', icon: new URL('@/assets/指标卡的图标.png', import.meta.url).href },
    { title: '磷酸铁锂电池粉工艺', value: null, className: 'lfp-battpower', icon: new URL('@/assets/指标卡的图标.png', import.meta.url).href },
    { title: '三元极片粉工艺', value: null, className: 'ncm', icon: new URL('@/assets/指标卡的图标.png', import.meta.url).href },
    { title: '通惠碳酸锂综合成本指数', value: null, className: 'compoIndex', icon: new URL('@/assets/指标卡的图标.png', import.meta.url).href }
]);


//  异步数据请求函数：获取最新碳酸锂成本指标数据
const fetchData = async () => {
    try {
        const response = await axios.get('https://service.thqh.com.cn:27878/api/getNewOne');
        // const response = await axios.get('http://localhost:8081/api/getNewOne');
        const data = response.data[0];

        indicators.value[0].value = data.compoIndex;
        indicators.value[1].value = data.ec_Spot;
        indicators.value[2].value = data.spodumene;
        indicators.value[3].value = data.lg_Lepidolite;
        indicators.value[4].value = data.hg_Lepidolite;
        indicators.value[5].value = data.lfp_Cathode;
        indicators.value[6].value = data.lfp_BattPower;
        indicators.value[7].value = data.ncm;

        // 更新currentDate
        currentDate.value = data.date;
        console.log("IndicatorBoard中的日期:", data.date);
    } catch (error) {
        console.error('Error fetching data:', error);
    }
};

// 组件挂载完成钩子
//触发数据请求，初始化指标数据（页面加载时自动获取最新数据）
const formatValue = (value) => {
    if (value === null) return '-';
    return value.toFixed(0).replace(/\B(?=(\d{3})+(?!\d))/g, ',');
};

onMounted(() => {
    fetchData();
});
</script>

<style scoped>
.board-container {
    padding: 20px;
}

.board-card {
    /* background: rgba(250, 250, 250, 0.3); */
    border-radius: 16px;
    box-shadow: 0 8px 16px rgba(0, 0, 0, 0.08);
    overflow: hidden;
}

.board-header {
    padding: 20px 24px;
    background: linear-gradient(135deg, #f8f9fa 0%, #e9ecef 100%);
    position: relative;
}

.board-header h2 {
    margin: 0;
    color: #2c3e50;
    font-size: 30px;
    font-weight: 600;
    position: relative;
    display: inline-block;
    border-radius: 16px;
    width: 420px;
    padding: 15px 30px;
    background-image: url('@/assets/小标题背景.png');
    background-size: 100% 100%;
    background-repeat: no-repeat;
    background-position: center;
    z-index: 1;
    /* background-color: #33cc99; */
}

.board-divider {
    height: 2px;
    background: linear-gradient(90deg,
            #4ba6f5 0%,
            #33cc99 20%,
            #fdca30 40%,
            #f79400 60%,
            #f34235 80%,
            #a560e8 100%);
    margin-top: 16px;
}

.indicators-mainContainer {
    display: grid;
    grid-template-columns: repeat(4, 1fr);
    gap: 50px;
    padding: 20px;
    width: 100%;
    box-sizing: border-box;
}

.indicator-card {
    display: flex;
    flex-direction: column;
    align-items: center;
    padding: 20px;
    border-radius: 12px;
    width: 100%;
    min-width: 0;
    height: 150px;
    background-image: url('@/assets/指标卡背景.png');
    background-size: 100% 100%;
    background-repeat: no-repeat;
    background-position: center;
    transition: all 0.3s ease;
    position: relative;
    box-sizing: border-box;
}

.composite-index-card {
    background-image: url('@/assets/指标卡背景.png');
}

.indicator-card:hover {
    transform: translateY(-3px);
}

.indicator-top {
    display: flex;
    align-items: center;
    width: 100%;
    margin-bottom: 20px;
}

.indicator-icon {
    width: 43px;
    height: 43px;
    /* margin-right: 16px; */
    margin-left: 16px;
    flex-shrink: 0;

}

.indicator-icon img {
    width: 100%;
    height: 100%;
    object-fit: contain;
}

.indicator-title {
    font-size: 23px;
    color: #5c6b7a;
    font-weight: 500;
    margin-left: 20px;
}

.indicator-bottom {
    display: flex;
    align-items: baseline;
    justify-content: center;
    width: 100%;
}

.indicator-value {
    font-size: 36px;
    font-weight: bold;
    color: #333333;
    text-shadow:
        2px 2px 0 rgba(0, 0, 0, 0.1),
        3px 3px 6px rgba(0, 0, 0, 0.15),
        0 0 15px rgba(52, 152, 219, 0.15);
    letter-spacing: 0.5px;
}

.indicator-unit {
    font-size: 20px;
    color: #525252;
    font-weight: 500;
    margin-left: 8px;
}





/* 综合指数卡片样式 */
.composite-index-card {
    background: linear-gradient(135deg, #ffffff 0%, #f8f9fa 100%);
    border-radius: 20px;
    padding: 30px;
    margin-bottom: 30px;
    display: flex;
    justify-content: center;
    align-items: center;
    box-shadow: 0 10px 30px rgba(0, 0, 0, 0.1);
    position: relative;
    overflow: hidden;
}

.composite-index-card::before {
    content: '';
    position: absolute;
    top: 0;
    left: 0;
    right: 0;
    bottom: 0;
    background: linear-gradient(45deg, rgba(44, 62, 80, 0.05), rgba(52, 152, 219, 0.05));
    z-index: 1;
}

.composite-index-content {
    flex: 1;
    z-index: 2;
    text-align: center;
}

.composite-index-title {
    font-size: 36px;
    font-weight: 600;
    margin-bottom: 20px;
    background: linear-gradient(45deg, #2d3092, #51539b);
    -webkit-background-clip: text;
    -webkit-text-fill-color: transparent;
}

.composite-index-value {
    display: flex;
    align-items: baseline;
    justify-content: center;
}

.composite-index-value .value {
    font-size: 48px;
    font-weight: bold;
    color: #2c3e50;
    margin-right: 10px;
    text-shadow:
        2px 2px 0 rgba(0, 0, 0, 0.1),
        4px 4px 8px rgba(0, 0, 0, 0.15),
        0 0 20px rgba(52, 152, 219, 0.2);
    letter-spacing: 1px;
}

.composite-index-value .unit {
    font-size: 24px;
    color: #7f8c8d;
    font-weight: 500;
}

.composite-index-icon {
    width: 80px;
    height: 80px;
    margin-left: 30px;
    z-index: 2;
}

.composite-index-icon img {
    width: 100%;
    height: 100%;
    object-fit: contain;
    filter: drop-shadow(0 4px 6px rgba(0, 0, 0, 0.1));
}

/* 响应式调整 */
@media (max-width: 768px) {
    .composite-index-card {
        padding: 20px;
        flex-direction: column;
        text-align: center;
    }

    .composite-index-title {
        font-size: 28px;
    }

    .composite-index-value .value {
        font-size: 36px;
    }

    .composite-index-value .unit {
        font-size: 20px;
    }

    .composite-index-icon {
        margin: 20px 0 0;
        width: 60px;
        height: 60px;
    }
}
</style>