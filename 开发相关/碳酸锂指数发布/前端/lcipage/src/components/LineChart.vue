<template>
    <div class="chart-container">
        <v-chart class="chart" :option="chartOption" autoresize />
    </div>
</template>

<script setup>
import { ref, onMounted } from 'vue'
import axios from 'axios'
import { use } from 'echarts/core'
import { CanvasRenderer } from 'echarts/renderers'
import { LineChart } from 'echarts/charts'
import {
    TitleComponent,
    TooltipComponent,
    LegendComponent,
    GridComponent,
    DataZoomComponent,
} from 'echarts/components'

// 注册必要的组件
use([
    CanvasRenderer,
    LineChart,
    TitleComponent,
    TooltipComponent,
    LegendComponent,
    GridComponent,
    DataZoomComponent,
])

// 使用 ref 定义响应式的图表配置
const chartOption = ref({
    title: {
        text: '各工艺成本历史曲线',
        left: 'center',
        top: 20,
        padding: [0, 0, 20, 0],
        textStyle: {
            fontSize: 30,
            fontWeight: 'bold'
        }
    },
    tooltip: {
        trigger: 'axis',
        formatter: function(params) {
            let result = params[0].axisValue + '<br/>'
            params.forEach(param => {
                if (param.value && param.value[1] !== null) {
                    result += `${param.marker} ${param.seriesName}: ${param.value[1].toFixed(2)}<br/>`
                }
            })
            return result
        }
    },
    legend: {
        data: ['通惠碳酸锂综合成本指数', '锂辉石', '电碳现货价', '低品位锂云母', '高品位锂云母', '磷酸铁锂正极片', '磷酸铁锂电池粉', '三元极片粉'],
        top: 70,
        padding: [20, 0, 0, 0],
        textStyle: {
            fontSize: 16
        }
    },
    grid: {
        left: '3%',
        right: '4%',
        bottom: '15%',
        top: '25%',
        containLabel: true
    },
    xAxis: {
        type: 'time',
        boundaryGap: false,
        axisLine: {
            lineStyle: {
                color: '#333'
            }
        },
        axisLabel: {
            formatter: '{yyyy}-{MM}-{dd}'
        }
    },
    yAxis: {
        type: 'value',
        axisLabel: {
            formatter: '{value}'
        },
        splitLine: {
            lineStyle: {
                type: 'dashed'
            }
        }
    },
    dataZoom: [{
        type: 'slider',
        show: true,
        xAxisIndex: [0],
        start: 0,
        end: 100,
        bottom: 10,
        height: 30,
        borderColor: '#ccc'
    }],
    series: [
        {
            name: '通惠碳酸锂综合成本指数',
            type: 'line',
            data: [],
            symbol: 'none',
            lineStyle: {
                width: 3,
                color: '#722ED1',  // 使用深紫色显示通惠碳酸锂成本综合指数
                shadowColor: 'rgba(114, 46, 209, 0.5)',  // 阴影颜色
                shadowBlur: 10,  // 阴影模糊度
                shadowOffsetY: 2  // 阴影垂直偏移
            },
            z: 3  // 确保通惠碳酸锂成本综合指数显示在最上层
        },
        {
            name: '锂辉石',
            type: 'line',
            data: [],
            symbol: 'none',
            lineStyle: {
                width: 3,
                color: '#FF4B4B'  // 突出显示锂辉石
            },
            z: 2
        },
        {
            name: '电碳现货价',
            type: 'line',
            data: [],
            symbol: 'none',
            lineStyle: {
                width: 3,
                color: '#FFB800'  // 使用更深的黄色显示电碳现货价
            },
            z: 2
        },
        {
            name: '低品位锂云母',
            type: 'line',
            data: [],
            symbol: 'none',
            lineStyle: { 
                width: 2,
                color: '#A8D8FF'  // 淡化处理
            }
        },
        {
            name: '高品位锂云母',
            type: 'line',
            data: [],
            symbol: 'none',
            lineStyle: { 
                width: 2,
                color: '#B8E2B8'  // 淡化处理
            }
        },
        {
            name: '磷酸铁锂正极片',
            type: 'line',
            data: [],
            symbol: 'none',
            lineStyle: { 
                width: 2,
                color: '#FFE4B8'  // 淡化处理
            }
        },
        {
            name: '磷酸铁锂电池粉',
            type: 'line',
            data: [],
            symbol: 'none',
            lineStyle: { 
                width: 2,
                color: '#FFD4B8'  // 淡化处理
            }
        },
        {
            name: '三元极片粉',
            type: 'line',
            data: [],
            symbol: 'none',
            lineStyle: { 
                width: 2,
                color: '#E8D4FF'  // 淡化处理
            }
        }
    ],
    color: ['#722ED1', '#FF4B4B', '#FFB800', '#A8D8FF', '#B8E2B8', '#FFE4B8', '#FFD4B8', '#E8D4FF']  // 更新配色方案顺序
})

// 获取数据的方法，异步函数
const fetchData = async () => {
    try {
        // 调用接口获取数据
        const response = await axios.get('https://service.thqh.com.cn:27878/api/getAll')

        const data = response.data

        // 处理数据
        const processedData = {
            spodumene: [],
            lg_Lepidolite: [],
            hg_Lepidolite: [],
            lfp_Cathode: [],
            lfp_BattPower: [],
            ncm: [],
            ec_Spot: [],
            compoIndex: []  // 添加通惠碳酸锂成本综合指数数据数组
        }

        data.forEach(item => {
            const date = item.date
            if (item.spodumene !== null) processedData.spodumene.push([date, item.spodumene])
            if (item.lg_Lepidolite !== null) processedData.lg_Lepidolite.push([date, item.lg_Lepidolite])
            if (item.hg_Lepidolite !== null) processedData.hg_Lepidolite.push([date, item.hg_Lepidolite])
            if (item.lfp_Cathode !== null) processedData.lfp_Cathode.push([date, item.lfp_Cathode])
            if (item.lfp_BattPower !== null) processedData.lfp_BattPower.push([date, item.lfp_BattPower])
            if (item.ncm !== null) processedData.ncm.push([date, item.ncm])
            if (item.ec_Spot !== null) processedData.ec_Spot.push([date, item.ec_Spot])
            if (item.compoIndex !== null) processedData.compoIndex.push([date, item.compoIndex])  // 处理通惠碳酸锂成本综合指数数据
        })

        // 更新图表数据
        chartOption.value.series = chartOption.value.series.map((series, index) => ({
            ...series, // 使用扩展运算符保留原系列的所有配置（比如 name、type、color 等），只替换 data
            data: [
                processedData.spodumene,
                processedData.lg_Lepidolite,
                processedData.hg_Lepidolite,
                processedData.lfp_Cathode,
                processedData.lfp_BattPower,
                processedData.ncm,
                processedData.ec_Spot,
                processedData.compoIndex
            ][index]
        }))
    } catch (error) {
        console.error('获取数据失败:', error)
    }
}

// 组件挂载时获取数据
onMounted(() => {
    fetchData()
})
</script>

<style scoped>
.chart-container {
    width: 100wh;
    /* max-width: 1800px; */
    margin: 0 auto;
    padding: 20px;
    background: rgb(250, 250, 250);
    border-radius: 16px;
    box-shadow: 0 2px 12px 0 rgba(0,0,0,0.1);

    width: 96%;
}

.chart {
    height: 500px;
    width: 100%;
}
</style>