<template>

    <div class="content3">
        <Toast group="tc" />
        <Toast group="tl" />
        <div class="sub-header">
            <div class="left">
                <span class="title">核心功能使用情况统计</span>
                <div class="color-block"></div>
            </div>
            <div class="right">
                <span>查看月份：</span>
                <DatePicker v-model="selectedMonth" showIcon fluid iconDisplay="input" dateFormat="yy/mm" view="month"
                    placeholder="选择月份" date-format="yy-mm" />

                <span>查看日期：</span>
                <DatePicker v-model="selectedDate" showIcon fluid iconDisplay="input" dateFormat="yy/mm/dd" view="date"
                    placeholder="选择日期" date-format="yy-mm-dd" />
            </div>
        </div>
        <div class="dimension-bar">
            <Menubar :model="items" v-model="selectedCoreFunc" />
        </div>

        <div class="sub-body">
            <Chart type="bar" :data="chartData" :options="chartOptions" width="85%" height="300" />
        </div>
        <div class="sub-footer">
            <div class="title">
                <i class="pi pi-info-circle" style="color: gray; font-size: small; margin-right: 0.5rem;"></i>
                <span style="font-size: 1rem; font-weight: bold;">指标说明</span>
            </div>

            <ScrollPanel style="width: 100%; height: 200px" :dt="{
                bar: {
                    background: '{primary.color}'
                }
            }">
                <p>· 功能使用人数（查询时统计）：在指定的统计周期内（自然月或自然日）内，使用过某一指定核心功能的独立用户数量。在统计周期内，以用户的唯一标识为去重依据，同一用户使用该功能一次以上均只计数一次。</p>
                <p>· 功能使用总次数（查询时统计）：在指定的统计周期内（自然月或自然日）内，所有用户对某一核心功能进行访问的累计总次数。在统计周期内，同一用户对该功能的多次使用将被累计统计。</p>
                <p>· 功能平均使用次数（查询时统计）：在指定的统计周期内（自然月或自然日）内，所有用户对某一核心功能进行访问的平均次数。
                    <span class="equation">公式：功能平均使用次数 = 功能使用总次数 / 功能使用人数；</span>
                    功能使用总次数和功能使用人数需基于同一统计周期计算。
                </p>
                <p>· 功能使用率（查询时统计）：在指定的统计周期内（自然月或自然日）内，使用某一指定核心功能的用户数占同期交易登录活跃用户总数的比例。。
                    <span class="equation">公式：功能使用率 = (功能使用人数/交易登录活跃用户数) * 100%；</span>
                </p>

            </ScrollPanel>
        </div>
    </div>



</template>

<script setup>
import 'primeicons/primeicons.css'
import { ref, onMounted, watch } from "vue";
import DatePicker from 'primevue/datepicker';
import Chart from 'primevue/chart';
import Menubar from 'primevue/menubar';
import Toast from 'primevue/toast';

import axiosInstance from '@/axiosConfig'

import { useToast } from 'primevue/usetoast';
const toast = useToast();

const chartData = ref();
const chartOptions = ref();

onMounted(() => {
    // 设定默认查看月份
    const currentDate = new Date();

    selectedMonth.value = currentDate;
    selectedCoreFunc.value = '功能使用人数';
    // selectedDate.value = null;
});

const selectedDate = ref();
const selectedMonth = ref();
const selectedCoreFunc = ref();
const chartType = ref('bar');

// 日期格式化函数
const dateFormat = (date) => {
    const year = date.getFullYear();
    const month = (date.getMonth() + 1).toString().padStart(2, '0');
    const day = date.getDate().toString().padStart(2, '0');
    return `${year}-${month}-${day}`;
};

// 月份格式化函数
const monthFormat = (date) => {
    const year = date.getFullYear();
    const month = (date.getMonth() + 1).toString().padStart(2, '0');
    return `${year}-${month}`;
};

watch(selectedDate, () => {
    if (!selectedDate.value) {
        return;
    }
    selectedMonth.value = null;
    updatechartData();
});

watch(selectedMonth, () => {

    if (!selectedMonth.value) {
        return;
    }
    selectedDate.value = null;
    updatechartData();
});

watch(selectedCoreFunc, () => {
    chartData.value = setChartData();
    chartOptions.value = setChartOptions();
});


const functionUserCount = ref({}) // 功能使用人数
const functionUseCount = ref({}) // 功能使用总次数
const functionAvgUse = ref({}) // 功能平均使用次数
const functionUsageRate = ref({}) // 功能使用率
const updatechartData = () => {
    console.log("updatechartData called");
    // 日期请求参数
    let dateInfo = '';
    if (selectedMonth.value) {
        dateInfo = monthFormat(selectedMonth.value);
    } else if (selectedDate.value) {
        dateInfo = dateFormat(selectedDate.value);
    }

    axiosInstance.post('/stats/functions', {
        date: dateInfo
    }).then(response => {

        if (response.data.code === 0) {
            functionUserCount.value = response.data.data.functionUserCount;
            functionUseCount.value = response.data.data.functionUseCount;
            functionAvgUse.value = response.data.data.functionAvgUse;
            functionUsageRate.value = response.data.data.functionUsageRate;
        } else {
            toast.add({ severity: 'error', summary: '获取数据失败', detail: response.data.msg });
            setNullData();
        }
        chartData.value = setChartData();
        chartOptions.value = setChartOptions();
    }).catch(error => {
        toast.add({ severity: 'error', summary: '获取数据失败', detail: error.message });
        setNullData();
    });
};

// 若请求失败，则将数据置空
const setNullData = () => {
    functionUserCount.value = {};
    functionUseCount.value = {};
    functionAvgUse.value = {};
    functionUsageRate.value = {};
}

// 用于从monthlyNewUsers的时间：数据键值列表中获取月份标签列表
const labelForMatch = ref([]) // 为了统计量数据列表和功能标签能够准确对应，labelForMatch中存储功能代码列表，顺序与getLabel获取到的功能标签列表对应一致
const getLabels = () => {
    const labels = [];
    let target = {};
    switch (selectedCoreFunc.value) {
        case '功能使用人数':
            target = functionUserCount.value;
            break;
        case '功能使用总次数':
            target = functionUseCount.value;
            break;
        case '功能平均使用次数':
            target = functionAvgUse.value;
            break;
        case '功能使用率':
            target = functionUsageRate.value;
            break;
        default:
            break;
    }
    for (const key in target) {
        switch (key) {
            case 'REPORT_ACCOUNT_FUND':
                labels.push('账户资金分析');
                break;
            case 'REPORT_VARIETY_ANALYSIS':
                labels.push('品种交易偏好分析');
                break;
            case 'REPORT_RISK_ANALYSIS':
                labels.push('风险度分析');
                break;
            case 'REPORT_MARKETVALUE_ANALYSIS':
                labels.push('市值权益曲线');
                break;
            case 'REPORT_YIELD_ANALYSIS':
                labels.push('累计收益率曲线');
                break;
            case 'REPORT_RETRACEMENT_ANALYSIS':
                labels.push('回撤率曲线');
                break;
            default:
                break;
        }
        labelForMatch.value.push(key);

    }
    return labels;
}
// 用于从monthlyNewUsers的时间：数据键值列表中获取数据列表
const getDataset = () => {
    const dataset = [];
    let target = {};
    switch (selectedCoreFunc.value) {
        case '功能使用人数':
            target = functionUserCount.value;
            break;
        case '功能使用总次数':
            target = functionUseCount.value;
            break;
        case '功能平均使用次数':
            target = functionAvgUse.value;
            break;
        case '功能使用率':
            target = functionUsageRate.value;
            break;
        default:
            break;
    }
    for (const key of labelForMatch.value) {
        dataset.push(target[key]);
    }
    // for (const key in target) {
    //     dataset.push(target[key]);
    // }
    return dataset;
}

const items = ref([
    {
        label: '功能使用人数',
        icon: 'pi pi-home',
        command: () => {
            selectedCoreFunc.value = '功能使用人数';
            chartType.value = 'bar';
        }
    },
    {
        label: '功能使用总次数',
        icon: 'pi pi-star',
        command: () => {
            selectedCoreFunc.value = '功能使用总次数';
            chartType.value = 'bar';
        }
    },
    {
        label: '功能平均使用次数',
        icon: 'pi pi-search',
        command: () => {
            selectedCoreFunc.value = '功能平均使用次数';
            chartType.value = 'bar';
        }
    },
    {
        label: '功能使用率',
        icon: 'pi pi-envelope',
        command: () => {
            selectedCoreFunc.value = '功能使用率';
            chartType.value = 'line';
        }
    }
]);


// 图表数据设置函数
const setChartData = () => {
    const documentStyle = getComputedStyle(document.documentElement);
    let dateInfo_label = '';
    if (selectedMonth.value) {
        dateInfo_label = monthFormat(selectedMonth.value) + '月';
    } else if (selectedDate.value) {
        dateInfo_label = dateFormat(selectedDate.value) + '日';
    }

    return {
        labels: getLabels(),
        datasets: [
            {
                type: chartType.value,
                label: dateInfo_label + ' ' + selectedCoreFunc.value + '统计',
                fill: false,
                borderColors: documentStyle.getPropertyValue('--p-cyan-500'),
                yAxisID: 'y',
                tension: 0.4,
                data: getDataset()
            }
        ]

    };

};

// 图表选项设置函数
const setChartOptions = () => {
    const documentStyle = getComputedStyle(document.documentElement);
    const textColor = documentStyle.getPropertyValue('--p-text-color');
    const textColorSecondary = documentStyle.getPropertyValue('--p-text-muted-color');
    const surfaceBorderColor = documentStyle.getPropertyValue('--p-content-border-color');

    return {
        stacked: false,
        maintainAspectRatio: false,
        responsive: true,
        aspectRatio: 0.6,
        plugin: {
            legend: {
                labels: {
                    color: textColor
                }
            }
        },
        scales: {
            x: {
                ticks: {
                    color: textColorSecondary,
                },
                grid: {
                    color: surfaceBorderColor,
                }
            },
            y: {
                type: 'linear',
                display: true,
                position: 'left',
                ticks: {
                    color: textColorSecondary,
                    // 回调函数用于格式化 y 轴刻度为整数
                    callback: function (value) {
                        if (value % 1 === 0) {
                            return value;
                        }
                    },
                },
                grid: {
                    color: surfaceBorderColor,
                }

            }
        }
    }
}
</script>

<style>
@layer reset, primevue, custom;

@layer custom {

    /** ######################################### Content 3 #################################### */
    .content3 {
        display: flex;
        flex-direction: column;
        align-items: center;

        padding: 0.8rem;

        height: auto;

        background-color: rgb(255, 255, 255);
    }

    .content3 .sub-header {
        display: flex;
        align-items: center;
        /* 让子元素向两端对齐 */
        justify-content: space-between;

        width: 100%;

        border-bottom: 0.1rem solid rgb(221, 221, 221);
    }

    .content3 .sub-header .left {
        display: flex;
        flex-direction: column;
        gap: 0.1rem;
    }

    .content3 .sub-header .left .title {
        font-size: 1rem;
        font-weight: bold;

        margin-bottom: 10px;
    }

    .content3 .sub-header .left .color-block {
        height: 0.4rem;
        width: 3rem;

        background-color: #6c63ff;
    }

    .content3 .sub-header .right {
        display: flex;
        flex-direction: row;
        align-items: center;
        gap: 0.5rem;

        font-size: 0.9rem;
    }

    .content3 .sub-header .right .p-inputtext {
        border-radius: 0;

        height: 30px;
        width: 200px;

        font-size: 0.9rem;
    }

    .p-datepicker-weekday {
        font-size: 0.8rem !important;
    }

    .p-datepicker-day {
        font-size: 0.8rem !important;
    }

    .p-datepicker-month {
        font-size: 0.8rem !important;
    }

    .content3 .dimension-bar {
        width: 100%;
    }

    .dimension-bar .p-menubar {
        --p-menubar-border-radius: 0rem;
        font-size: 0.8rem;
    }

    .content3 .sub-body {
        display: flex;
        flex-direction: column;
        justify-content: center;

        padding: 1.5rem;

        width: 100%;

        /* background-color: rgb(196, 205, 255); */
    }

    .sub-footer .equation {
        font-style: italic;
        font-weight: 500;
        color: #606068;
    }
}
</style>