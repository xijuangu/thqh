<template>
    <div class="container">

        <div class="card" v-if="true">
            <div class="top">
                <Menubar :model="items" />
                <Dialog v-model:visible="visible" modal :header="DialogInfo" :style="{ width: '25rem' }">
                    <div style="display: flex; flex-direction: row; gap: 1rem;">
                        <DatePicker v-model="date" view="year" dateFormat="yy" v-if="DialogType === 'year'" />
                        <DatePicker v-model="date" view="month" dateFormat="yy-mm" v-if="DialogType === 'month'" />
                        <Button label="确定" style="background-color: #202020; border: none;" />
                    </div>

                </Dialog>

            </div>
            <div class="bottom">
                <Chart class="chart" type="line" :data="chartData1" :options="chartOptions1"
                    style="width: 60rem; height: 20rem" />
                <div class="info" style="display: flex; flex-direction: column; justify-content: center; align-items: center;gap: 1.3rem;">
                    <span style="color: #4e4e4e;">-- 使用概况 --</span>
                    <span style="color: #909090; font-size: small; width: 70%;">（除累计用户数为实时显示外，其余指标数据统计截至2025-11-29日）</span>
                    <span>累计用户数：1000 人 </span>
                    <span>上日新增用户数：100 人 </span>
                    <span>上日活跃用户数：800 人 </span>
                    <span>疑似流失用户数：400 人 </span>
                </div>
            </div>
        </div>

        <div class="card" v-if="false">
            <div class="top">
                <Menubar :model="items" />
                <Dialog v-model:visible="visible" modal :header="DialogInfo" :style="{ width: '25rem' }">
                    <div style="display: flex; flex-direction: row; gap: 1rem;">
                        <DatePicker v-model="date" view="year" dateFormat="yy" v-if="DialogType === 'year'" />
                        <!-- <DatePicker v-model="date" view="month" dateFormat="yy-mm" v-if="DialogType === 'month'" /> -->
                        <Button label="确定" style="background-color: #202020; border: none;" />
                    </div>

                </Dialog>

            </div>
            <div class="bottom">
                <Chart class="chart" type="bar" :data="chartData2" :options="chartOptions2"
                    :style="{ width: '100%' }" />

            </div>
        </div>



        <div class="card" v-if="false">
            <Tabs value="0" style="margin-bottom: 2rem;">
                <TabList>
                    <Tab value="0">功能使用人数</Tab>
                    <Tab value="1">功能使用总次数</Tab>
                    <Tab value="2">功能平均使用次数</Tab>
                    <Tab value="3">功能使用率</Tab>
                </TabList>
            </Tabs>
            <div class="top">
                <Menubar :model="items" style="width: 20%;" />
                <Dialog v-model:visible="visible" modal :header="DialogInfo" :style="{ width: '25rem' }">
                    <div style="display: flex; flex-direction: row; gap: 1rem;">
                        <DatePicker v-model="date" view="month" dateFormat="yy" v-if="DialogType === 'year'" />
                        <DatePicker v-model="date" view="date" dateFormat="" v-if="DialogType === 'month'" />
                        <Button label="确定" style="background-color: #202020; border: none;" />
                    </div>

                </Dialog>

            </div>
            <div class="bottom">
                <Chart class="chart" type="line" :data="chartData3" :options="chartOptions3"
                    :style="{ width: '100%' }" />
            </div>
        </div>

        <div class="card" v-if="false">
            <Tabs value="0" style="margin-bottom: 2rem;">
                <TabList>
                    <Tab value="0">功能使用人数</Tab>
                    <Tab value="1">功能使用总次数</Tab>
                    <Tab value="2">功能平均使用次数</Tab>
                    <Tab value="3">功能使用率</Tab>
                </TabList>
            </Tabs>
            <div class="top">
                <Menubar :model="items" />
                <Dialog v-model:visible="visible" modal :header="DialogInfo" :style="{ width: '25rem' }">
                    <div style="display: flex; flex-direction: row; gap: 1rem;">
                        <DatePicker v-model="date" view="month" dateFormat="yy-mm" v-if="DialogType === 'year'" />
                        <DatePicker v-model="date" view="date" dateFormat="yy-mm-dd" v-if="DialogType === 'month'" />
                        <Button label="确定" style="background-color: #202020; border: none;" />
                    </div>

                </Dialog>

            </div>
            <div class="bottom">
                <Chart class="chart" type="bar" :data="chartData4" :options="chartOptions4"
                    :style="{ width: '100%' }" />
            </div>
        </div>


    </div>

</template>

<script setup>
import { ref, onMounted } from "vue";

import Chart from 'primevue/chart';
import Menubar from 'primevue/menubar';
import Dialog from 'primevue/dialog';
import DatePicker from 'primevue/datepicker';
import Button from 'primevue/button';
// import { TabList, Tab } from 'primevue/tablist';
import TabList from 'primevue/tablist';
import Tab from 'primevue/tab';
import Tabs from 'primevue/tabs';


const visible = ref(false);
const DialogInfo = ref('');
const DialogType = ref('');

const items = ref([
    {
        label: '月视图',
        icon: 'pi pi-moon',
        command: () => {
            DialogInfo.value = '请选择您要查看年份的月视图：';
            DialogType.value = 'year';
            visible.value = true;
        }
    },
    // {
    //     label: '日视图',
    //     icon: 'pi pi-sun',
    //     command: () => {
    //         DialogInfo.value = '请选择您要查看的日期：';
    //         DialogType.value = 'month';
    //         visible.value = true;
    //     }
    // },
]);



onMounted(() => {
    chartData1.value = setChartData1();
    chartOptions1.value = setChartOptions1();

    chartData2.value = setChartData2();
    chartOptions2.value = setChartOptions2();

    chartData3.value = setChartData3();
    chartOptions3.value = setChartOptions3();

    chartData4.value = setChartData4();
    chartOptions4.value = setChartOptions4();
});

const chartData1 = ref();
const chartOptions1 = ref();

const chartData2 = ref();
const chartOptions2 = ref();

const chartData3 = ref();
const chartOptions3 = ref();

const chartData4 = ref();
const chartOptions4 = ref();

const setChartData1 = () => {
    const documentStyle = getComputedStyle(document.documentElement);

    return {
        labels: ['2025-05', '2025-06', '2025-07', '2025-08', '2025-09', '2025-10', '2025-11'],
        datasets: [
            {
                label: '新增用户',
                fill: false,
                borderColor: documentStyle.getPropertyValue('--p-cyan-500'),
                yAxisID: 'y',
                tension: 0,
                data: [65, 59, 80, 81, 100, 94, 150]
            },
            {
                label: '活跃用户',
                fill: false,
                borderColor: documentStyle.getPropertyValue('--p-gray-500'),
                yAxisID: 'y1',
                tension: 0,
                data: [28, 48, 40, 55, 86, 77, 90]
            }
        ]
    };
};
const setChartOptions1 = () => {
    const documentStyle = getComputedStyle(document.documentElement);
    const textColor = documentStyle.getPropertyValue('--p-text-color');
    const textColorSecondary = documentStyle.getPropertyValue('--p-text-muted-color');
    const surfaceBorder = documentStyle.getPropertyValue('--p-content-border-color');

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
                    color: textColorSecondary
                },
                grid: {
                    color: surfaceBorder
                }
            },
            y: {
                type: 'linear',
                display: true,
                position: 'left',
                ticks: {
                    color: textColorSecondary
                },
                grid: {
                    color: surfaceBorder
                }
            },
            y1: {
                type: 'linear',
                display: true,
                position: 'right',
                ticks: {
                    color: textColorSecondary
                },
                grid: {
                    drawOnChartArea: false,
                    color: surfaceBorder
                }
            }
        }
    };
}


const setChartData2 = () => {
    const documentStyle = getComputedStyle(document.documentElement);

    return {
        labels: ['2025-05', '2025-06', '2025-07', '2025-08', '2025-09', '2025-10', '2025-11'],
        datasets: [
            {
                type: 'line',
                label: '深度使用率',
                borderColor: documentStyle.getPropertyValue('--p-orange-500'),
                borderWidth: 2,
                fill: false,
                tension: 0.4,
                yAxisID: 'y1',
                data: [50, 25, 12, 48, 56, 76, 42]
            },
            {
                type: 'bar',
                label: '交易登录活跃人数',
                backgroundColor: documentStyle.getPropertyValue('--p-gray-500'),
                data: [21, 40, 24, 32, 17, 29, 34],
                borderColor: 'white',
                yAxisID: 'y',
                borderWidth: 2
            },

        ]
    };
};
const setChartOptions2 = () => {
    const documentStyle = getComputedStyle(document.documentElement);
    const textColor = documentStyle.getPropertyValue('--p-text-color');
    const textColorSecondary = documentStyle.getPropertyValue('--p-text-muted-color');
    const surfaceBorder = documentStyle.getPropertyValue('--p-content-border-color');

    return {
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
                    color: textColorSecondary
                },
                grid: {
                    color: surfaceBorder
                }
            },
            y: {
                position: 'left',
                title: {
                    display: true,
                    text: '人数',
                    color: textColor,

                },
                ticks: {
                    color: textColorSecondary
                },
                grid: {
                    color: surfaceBorder
                }
            },
            y1: {
                position: 'right',

                title: {
                    display: true,
                    text: '深度使用率 %',
                    color: documentStyle.getPropertyValue('--p-orange-500'),
                },
                ticks: {
                    color: documentStyle.getPropertyValue('--p-orange-500'),
                },
                grid: {
                    drawOnChartArea: false,
                    color: surfaceBorder
                }
            }
        }
    };
}

const setChartData3 = () => {
    const documentStyle = getComputedStyle(document.documentElement);

    return {
        labels: ['账户资金分析', '品种交易偏好分析', '风险度分析', '市值权益曲线', '累计收益率曲线', '回撤率曲线'],
        datasets: [
            {
                type: 'line',
                label: '功能使用率',

                data: [26, 12, 39, 9, 48, 20, 7],

                backgroundColor: documentStyle.getPropertyValue('--p-blue-500'),
                borderColor: documentStyle.getPropertyValue('--p-blue-500'),
                yAxisID: 'y',
                // borderWidth: 2
                tension: 0.4,
            },

        ]
    };
};
const setChartOptions3 = () => {
    const documentStyle = getComputedStyle(document.documentElement);
    const textColor = documentStyle.getPropertyValue('--p-text-color');
    const textColorSecondary = documentStyle.getPropertyValue('--p-text-muted-color');
    const surfaceBorder = documentStyle.getPropertyValue('--p-content-border-color');

    return {
        maintainAspectRatio: false,
        aspectRatio: 0.6,
        plugins: {
            legend: {
                labels: {
                    color: textColor
                }
            },
            title: {
                display: true,
                text: '2025-11-01 日功能使用率',
                color: textColor,
                font: {
                    size: 20,
                    weight: 'bold'
                }

            },
            tooltip: {
                callbacks: {
                    // 为数值添加单位
                    label: function (context) {
                        let label = context.dataset.label || '';
                        if (label) {
                            label += ': ';
                        }
                        // 添加单位，这里以"分钟"为例
                        label += context.raw + ' %';
                        return label;
                    }
                }
            }
        },
        scales: {
            x: {
                ticks: {
                    color: textColorSecondary
                },
                grid: {
                    color: surfaceBorder
                }
            },
            y: {
                position: 'left',
                title: {
                    display: true,
                    text: '使用率（%）',
                    color: textColor,

                },
                ticks: {
                    color: textColorSecondary
                },
                grid: {
                    color: surfaceBorder
                }
            },
        }
    };
}

const setChartData4 = () => {
    const documentStyle = getComputedStyle(document.documentElement);

    return {
        labels: ['账户资金分析', '品种交易偏好分析', '风险度分析', '市值权益曲线', '累计收益率曲线', '回撤率曲线'],
        datasets: [
            {
                type: 'bar',
                label: '功能使用总次数',
                backgroundColor: documentStyle.getPropertyValue('--p-orange-300'),
                data: [89, 245, 63, 352, 121, 317],
                borderColor: 'white'
            },

        ]
    };
}

const setChartOptions4 = () => {
    const documentStyle = getComputedStyle(document.documentElement);
    const textColor = documentStyle.getPropertyValue('--p-text-color');
    const textColorSecondary = documentStyle.getPropertyValue('--p-text-muted-color');
    const surfaceBorder = documentStyle.getPropertyValue('--p-content-border-color');

    return {
        // indexAxis: 'y',
        maintainAspectRatio: false,
        aspectRatio: 0.8,
        plugins: {
            legend: {
                labels: {
                    color: textColor
                }
            },
            title: {
                display: true,
                text: '2025-11-01 日功能使用总次数',
                color: textColor,
                font: {
                    size: 20,
                    weight: 'bold'
                }

            },
            tooltip: {
                callbacks: {
                    // 为数值添加单位
                    label: function (context) {
                        let label = context.dataset.label || '';
                        if (label) {
                            label += ': ';
                        }
                        // 添加单位，这里以"分钟"为例
                        label += context.raw + ' 次';
                        return label;
                    }
                }
            }
        },
        scales: {
            x: {
                ticks: {
                    color: textColorSecondary
                },
                grid: {
                    color: surfaceBorder,
                    display: false,
                    drawBorder: false,
                },
                title: {
                    display: true,
                    text: '功能名称',
                    color: textColor,

                },
            },
            y: {
                position: 'left',
                title: {
                    display: true,
                    text: '次数',
                    color: textColor,

                },
                ticks: {
                    color: textColorSecondary
                },
                grid: {
                    color: surfaceBorder,
                    drawBorder: false,
                }
            },
        }
    };
}
</script>

<style>
@layer reset, primevue, custom;

@layer custom {
    .container {
        display: flex;
        flex-direction: column;
        align-items: center;
        gap: 2rem;
    }

    .container .card {
        display: flex;
        flex-direction: column;
        /* gap: 5rem; */
        width: 70%;
        padding: 1rem;
        box-shadow: 0 4px 8px rgba(0, 0, 0, 0.1);
        border-radius: 10px;
        background-color: white;
    }

    .container .top {}

    .container .bottom {
        display: flex;
        flex-direction: row;
        align-items: center;
        gap: 5rem;
        margin-top: 2rem;
    }

    .chart {
        /* width: 100%; */
        height: 400px;
    }

    .container .card .bottom .info {
        display: flex;
        flex-direction: column;
        gap: 2rem;
        font-size: 1.1rem;
        color: #333;
    }

    .container .info span {
        font-weight: 500;
        color: #4e4e4e;
    }


}
</style>
