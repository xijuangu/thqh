<template>

    <div class="content2">
        <Toast group="tc" />
        <Toast group="tl" />
        <div class="sub-header">
            <div class="left">
                <span class="title">用户深度使用情况统计</span>
                <div class="color-block"></div>
            </div>
            <div class="right">
                <span>查看年份：</span>
                <DatePicker v-model="selectedYear" showIcon fluid iconDisplay="input" view="year" placeholder="选择年份"
                    date-format="yy" />
            </div>
        </div>
        <div class="sub-body">
            <Chart type="bar" :data="chartData" :options="chartOptions" width="100%" height="300" />
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
                <p>· 交易登录活跃用户数（T-1日更新，月度结束后固化）：在指定的统计周期内（自然月）内，成功完成交易账号登录操作的独立用户数，同一用户在同一月内多次成功登录只记一次。</p>
                <p>· 深度使用率（T-1日更新，月度结束后固化）：在指定的统计周期内（自然月）内，成功完成交易账号登录的独立用户数占该月总授权登录用户数的百分比。该指标衡量用户从“访问者”到“核心功能访问者”的转化效率。
                    <span class="equation">公式：深度使用率 = ( 当月交易登录活跃用户数 / 当月授权登录活跃用户数 ) * 100%</span>
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
import Toast from 'primevue/toast';
import { useToast } from 'primevue/usetoast';
const toast = useToast();

import axiosInstance from "@/axiosConfig";


onMounted(() => {
    // 设定默认查看年份
    const currentDate = new Date();
    selectedYear.value = currentDate;
});

const selectedYear = ref();
watch(selectedYear, () => {
    // 当 selectedYear 变化时，更新图表数据
    updatechartData();

});

// 年份格式化函数
const formatYear = (date) => {
    const year = date.getFullYear();
    return `${year}`;
}

const monthlyTradeLoginUsers = ref({}); // 交易登录活跃用户数统计
const monthlyTradeLoginRate = ref({}); // 交易登录活跃用户占比统计
const updatechartData = () => {
    console.log("updatechartData called:XXXXXXXXXXXXXXXXXXX");

    axiosInstance.post('/stats/users', { year: formatYear(selectedYear.value) })
        .then(response => {
            // console.log(response.data);
            if (response.data.code === 0) {
                monthlyTradeLoginUsers.value = response.data.data.monthlyTradeLoginUsers;
                monthlyTradeLoginRate.value = response.data.data.monthlyTradeLoginRate;
            } else {
                toast.add({ severity: 'error', summary: '获取数据失败', detail: response.data.data.msg });
                setNullData();
            }
            chartData.value = setChartData();
            chartOptions.value = setChartOptions();

        })
        .catch(error => {
            toast.add({ severity: 'error', summary: '获取数据失败', detail: error.message });
            setNullData();
        });
};

const setNullData = () => {
    monthlyTradeLoginUsers.value = {};
    monthlyTradeLoginRate.value = {};
}

// 用于从monthlyTradeLoginUsers/monthlyTradeLoginRate的时间：数据键值列表中获取月份标签列表
const getLabels = (data) => {
    const labels = [];
    for (const key in data) {
        labels.push(key);
    }
    return labels;
}
// 用于从monthlyTradeLoginUsers/monthlyTradeLoginRate的时间：数据键值列表中获取数据列表
const getDataset = (data) => {
    const dataset = [];
    for (const key in data) {
        dataset.push(data[key]);
    }
    return dataset;
}

const chartData = ref();
const chartOptions = ref();

const setChartData = () => {
    const documentStyle = getComputedStyle(document.documentElement);

    return {
        labels: getLabels(monthlyTradeLoginUsers.value),
        datasets: [
            {
                type: 'bar',
                label: '交易登录活跃人数',
                fill: false,
                borderColors: documentStyle.getPropertyValue('--p-cyan-500'),
                yAxisID: 'y',
                tension: 0.4,
                data: getDataset(monthlyTradeLoginUsers.value)
            },
            {
                type: 'line',
                label: '深度使用率',
                fill: false,
                borderColors: documentStyle.getPropertyValue('--p-cyan-500'),
                yAxisID: 'y1',
                tension: 0.4,
                data: getDataset(monthlyTradeLoginRate.value)
            }
        ]

    };

};

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

            },
            y1: {
                type: 'linear',
                display: true,
                position: 'right',
                ticks: {
                    color: documentStyle.getPropertyValue('--p-red-500'),
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

    /** ######################################### Content 2 #################################### */
    .content2 {
        display: flex;
        flex-direction: column;
        align-items: center;

        padding: 0.8rem;

        height: auto;

        background-color: rgb(255, 255, 255);
    }

    .content2 .sub-header {
        display: flex;
        align-items: center;
        /* 让子元素向两端对齐 */
        justify-content: space-between;

        width: 100%;

        border-bottom: 0.1rem solid rgb(221, 221, 221);
    }

    .content2 .sub-header .left {
        display: flex;
        flex-direction: column;
        gap: 0.1rem;
    }

    .content2 .sub-header .left .title {
        font-size: 1rem;
        font-weight: bold;

        margin-bottom: 10px;
    }

    .content2 .sub-header .left .color-block {
        height: 0.4rem;
        width: 3rem;

        background-color: #6c63ff;
    }

    .content2 .sub-header .right {
        display: flex;
        flex-direction: row;
        align-items: center;
        gap: 0.5rem;

        font-size: 0.9rem;
    }

    .content2 .sub-header .right .p-inputtext {
        border-radius: 0;

        height: 30px;
        width: 200px;

        font-size: 0.9rem;
    }



    .content2 .sub-body {
        padding: 1.5rem;

        width: 100%;

        /* background-color: rgb(196, 205, 255); */
    }

    .sub-footer {
        background-color: #eef0f3;
        border-radius: 0.5rem;

        padding: 0.9rem;
    }

    .sub-footer .title {
        display: flex;
        flex-direction: row;
        align-items: center;

        margin-bottom: 0.6rem;

    }

    .sub-footer .equation {
        font-style: italic;
        font-weight: 500;
        color: #606068;
    }
}
</style>