<template>

    <div class="content1">
        <Toast group="tc" />
        <Toast group="tl" />
        <div class="sub-header">
            <div class="left">
                <span class="title">用户增长与概况统计</span>
                <div class="color-block"></div>
            </div>
            <div class="right">
                <span>查看年份：</span>
                <DatePicker v-model="selectedYear" showIcon fluid iconDisplay="input" view="year" placeholder="选择年份"
                    date-format="yy" />
            </div>
        </div>
        <div class="sub-body">
            <div class="left">
                <div class="title">
                    <span style="font-weight: bold;">使用概况</span>
                </div>
                <div class="indicator indicator1">
                    <span class="name">累计用户数</span>
                    <span class="value">{{ totalUsers }} 人</span>
                </div>
                <div class="indicator indicator3">
                    <span class="name">上日活跃用户数</span>
                    <span class="value">{{ yesterdayActiveUsers }} 人</span>
                </div>
                <div class="indicator indicator3">
                    <span class="name">疑似流失用户数</span>
                    <span class="value">{{ suspectedLostUsers }} 人</span>
                </div>
            </div>
            <div class="right">
                <Chart type="line" :data="chartData" :options="chartOptions" width="100%" height="300" />
            </div>

        </div>
        <div class="sub-footer">
            <div class="title">
                <i class="pi pi-info-circle" style="color: gray; font-size: small; margin-right: 0.5rem;"></i>
                <span style="font-size: 1rem; font-weight: bold;">指标说明</span>
            </div>

            <ScrollPanel style="width: 100%; height: 200px" :dt="{
                bar: {
                    background: '{#ffffff}'
                }
            }">
                <p>· 累计用户数（T日实时）：统计自小程序上线起，所有成功完成授权登录的独立用户总数。</p>
                <p>· 上日活跃用户数（T-1日更新）：统计上一日有过任何访问行为的独立用户数量，如登录小程序、登录交易账号或使用报表查询功能。</p>
                <p>· 累计疑似流失用户数（T-1日更新）：统计自小程序上线日起，所有被视为“疑似流失”的独立用户总数。定义“流失”为超过30天未有任何访问行为的用户，如登录小程序、登录交易账号或使用报表查询功能。</p>
                <p>· 月新增用户数（T-1日更新，月度结束后固化）：统计在指定月份内，首次成功完成授权登录的独立用户数量。</p>
                <p>· 月活跃用户数（T-1日更新，月度结束后固化）：统计在指定月份内，有过任何访问行为的独立用户数量，如登录小程序、登录交易账号或使用报表查询功能。</p>
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
import ScrollPanel from 'primevue/scrollpanel';

import 'primeicons/primeicons.css'


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

// 图表数据
const totalUsers = ref(); // 累计用户数
const suspectedLostUsers = ref(); // 累计疑似流失用户数
const yesterdayActiveUsers = ref(); // 上日活跃用户数
const monthlyNewUsers = ref(); // 月新增用户数统计
const monthlyActiveUsers = ref(); // 月活跃用户数统计

// 调用接口获取图表数据
const updatechartData = () => {
    console.log("updatechartData called");
    axiosInstance.post('/stats/users', { year: formatYear(selectedYear.value) })
        .then(response => {
            // console.log(response.data);
            if (response.data.code === 0) {
                totalUsers.value = response.data.data.totalUsers;
                suspectedLostUsers.value = response.data.data.suspectedLostUsers;
                yesterdayActiveUsers.value = response.data.data.yesterdayActiveUsers;
                monthlyNewUsers.value = response.data.data.monthlyNewUsers;
                monthlyActiveUsers.value = response.data.data.monthlyActiveUsers;

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
            chartData.value = setChartData();
            chartOptions.value = setChartOptions();
        });
};

const setNullData = () => {
    totalUsers.value = 0;
    suspectedLostUsers.value = 0;
    yesterdayActiveUsers.value = 0;
    monthlyNewUsers.value = {};
    monthlyActiveUsers.value = {};
}

// 获取所有月份标签（合并两个数据集的月份）
const getAllLabels = () => {
    const allMonths = new Set();

    // 添加新增用户数的月份
    for (const key in monthlyNewUsers.value) {
        allMonths.add(key);
    }

    // 添加活跃用户数的月份
    for (const key in monthlyActiveUsers.value) {
        allMonths.add(key);
    }

    // 转换为数组并排序
    const sortedMonths = Array.from(allMonths).sort();

    return sortedMonths;

}
// 根据标签数组获取对应数据集的数值
const getDataSetForLabels = (data, labels) => {
    const dataset = [];
    for (const label of labels) {
        dataset.push(data[label] || 0);
    }
    return dataset;
}

const chartData = ref();
const chartOptions = ref();

const setChartData = () => {
    const documentStyle = getComputedStyle(document.documentElement);

    // 获取统一月份的标签
    const labels = getAllLabels();

    return {
        labels: labels,
        datasets: [
            {
                label: '新增用户数',
                fill: false,
                borderColor: documentStyle.getPropertyValue('--p-cyan-500'),
                yAxisID: 'y',
                tension: 0.4,
                data: getDataSetForLabels(monthlyNewUsers.value, labels)
            },
            {
                label: '活跃用户数',
                fill: false,
                borderColor: documentStyle.getPropertyValue('--p-red-500'),
                yAxisID: 'y',
                tension: 0.4,
                data: getDataSetForLabels(monthlyActiveUsers.value, labels)
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
        aspectRatio: 0.6,
        plugins: {
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
                    color: documentStyle.getPropertyValue('--p-text-color-primary'),
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

    /** ######################################### Content 1 #################################### */
    .content1 {
        display: flex;
        flex-direction: column;

        padding: 0.8rem;

        height: auto;

        background-color: rgb(255, 255, 255);




    }

    .content1 .sub-header {
        display: flex;
        align-items: center;
        /* 让子元素向两端对齐 */
        justify-content: space-between;

        border-bottom: 0.1rem solid rgb(221, 221, 221);
    }

    .content1 .sub-header .left {
        display: flex;
        flex-direction: column;
        gap: 0.1rem;
    }

    .content1 .sub-header .left .title {
        font-size: 1rem;
        font-weight: bold;

        margin-bottom: 10px;
    }

    .content1 .sub-header .left .color-block {
        height: 0.4rem;
        width: 3rem;

        background-color: #6c63ff;
    }

    .content1 .sub-header .right {
        display: flex;
        flex-direction: row;
        align-items: center;
        gap: 0.5rem;

        font-size: 0.9rem;
    }

    .content1 .sub-header .right .p-inputtext {
        border-radius: 0;

        height: 30px;
        width: 200px;

        font-size: 0.9rem;
    }

    .p-datepicker-year {
        font-size: 0.8rem !important;
        /* 自定义字体大小，!important 确保覆盖默认样式 */
        padding: 4px 8px !important;
        /* 可选：调整年份项内边距，配合字体大小 */
    }

    .p-datepicker-decade {
        font-size: 0.8rem !important;
        /* 自定义字体大小，!important 确保覆盖默认样式 */
        padding: 4px 8px !important;
        /* 可选：调整年份项内边距，配合字体大小 */
    }

    .content1 .sub-body {
        display: flex;
        flex-direction: row;
        flex: 1;


    }

    .content1 .sub-body .left {
        display: flex;
        flex-direction: column;
        justify-content: center;
        gap: 0.8rem;

        padding: 1rem 1rem;

        width: 15%;
        height: 100%;

        /* background-color: azure; */

    }

    .content1 .sub-body .left .title {
        display: flex;
        flex-direction: column;
        justify-content: center;
        align-items: center;

        font-size: 0.8rem;
    }

    .content1 .sub-body .left .indicator {
        display: flex;
        flex-direction: column;
        align-items: center;
        gap: 0.5rem;

        padding: 0.5rem;

        /* background-color: aqua; */

        font-size: 0.7rem;
    }

    .content1 .sub-body .left .indicator .name {}

    .content1 .sub-body .left .indicator .value {}

    .content1 .sub-body .right {
        display: flex;
        flex-direction: column;
        justify-content: center;

        padding: 1rem;

        width: 85%;

        /* background-color: rgb(196, 205, 255); */
    }


    .sub-footer {
        /* background-color: #f2f6f8; */
        border-radius: 0.5rem;

        padding: 0.9rem;
    }

    .sub-footer .title {
        display: flex;
        flex-direction: row;
        align-items: center;

        margin-bottom: 0.6rem;
        
    }

}
</style>