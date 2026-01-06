<template>

    <div class="main-periodCompareDataBoard-container">
        <Toast />
        <Toast group="tc" position="top-center" />
        <Card class="main-periodCompareDataBoard-card" style="min-width: 30rem;">
            <template #header>
                <div class="card-header">
                    <div class="left">
                        <span class="card-title" style="z-index: 10;">周期业务数据明细</span>
                    </div>
                    <div class="right">
                        <Select v-model="selectedPeriod" :options="Period" optionLabel="name" placeholder="快捷区间查询"
                            class="w-full md:w-56" size="small" @change="handlePerodChange(selectedPeriod)" />

                        <FloatLabel variant="on">
                            <DatePicker v-model="Range1" inputId="start_date" showIcon iconDisplay="input" size="small"
                                :disabled="isDatePickerDisabled" />
                            <label for="start_date">开始时间</label>
                        </FloatLabel>
                        <FloatLabel variant="on">
                            <DatePicker v-model="Range2" inputId="end_date" showIcon iconDisplay="input" size="small"
                                :disabled="isDatePickerDisabled" />
                            <label for="end_date">结束时间</label>
                        </FloatLabel>
                        <Button class="download-button" icon="pi pi-undo" style="font-size: 1rem;" size="small"
                            v-tooltip.bottom="'重置周期选择'" @click="resetAllSelections" :disabled="isOperable" />
                        <Button class="download-button" icon="pi pi-search" style="font-size: 1rem;" size="small"
                            @click="operation('search')" :disabled="isOperable" />
                        <Button class="download-button" icon="pi pi-external-link" label="导出" style="font-size: 1rem;"
                            size="small" @click="exportCSV()" :disabled="isOperable" />
                    </div>

                </div>
            </template>
            <template #content>
                <TreeTable :value="TreeData" tableStyle="min-width: 100rem; min-height: 26rem;" ref="dt"
                    v-if="treetableShow" removableSort scrollable scrollHeight="37rem">

                    <!-- <Column header="业务人员" expander field="Name" footer="总计:" footerStyle="text-align: left" /> -->
                    <Column header="业务人员" expander footer="总计:" footerStyle="text-align: left">
                        <template #body="slotProps">
                            <span class="bold-name">{{ slotProps.node.data.Name }}</span>
                        </template>
                    </Column>

                    <!-- <Column header="客户类型" field="Type" footerStyle="text-align: left" /> -->
                    <Column header="客户类型" footerStyle="text-align: left">
                        <template #body="slotProps">
                            <span :class="getTypeClass(slotProps.node.data.Type)">{{ slotProps.node.data.Type }}</span>
                        </template>
                    </Column>

                    <Column header="新增客户数" sortable field="AddCustomer" :footer="Sum_AddCustomer"
                        footerStyle="text-align: left；font-weight: bold;" />
                    <Column header="总客户数" sortable field="TotalCustomer" :footer="Sum_TotalCustomer"
                        footerStyle="text-align: left；font-weight: bold;" />
                    <Column header="交易日日均权益" sortable field="TradingDayAverageEquity"
                        :footer="Sum_TradingDayAverageEquity" footerStyle="text-align: left；font-weight: bold;" />
                    <Column header="期初权益" sortable field="OpeningEquity" :footer="Sum_OpeningEquity"
                        footerStyle="text-align: left；font-weight: bold;" />
                    <Column header="期末权益" sortable field="ClosingEquity" :footer="Sum_ClosingEquity"
                        footerStyle="text-align: left；font-weight: bold;" />
                    <Column header="入金" sortable field="Deposit" :footer="Sum_Deposit"
                        footerStyle="text-align: left；font-weight: bold;" />
                    <Column header="出金" sortable field="Withdrawal" :footer="Sum_Withdrawal"
                        footerStyle="text-align: left；font-weight: bold;" />
                    <Column header="净入金" sortable field="NetDeposit" :footer="Sum_NetDeposit"
                        footerStyle="text-align: left；font-weight: bold;" />
                    <Column header="总手续费" sortable field="TotalCommission" :footer="Sum_TotalCommission"
                        footerStyle="text-align: left；font-weight: bold;" />
                    <Column header="留存手续费" sortable field="RetentionFee" :footer="Sum_RetentionFee"
                        footerStyle="text-align: left；font-weight: bold;" />
                    <Column header="总盈亏" sortable field="TotalProfitLoss" :footer="Sum_TotalProfitLoss"
                        footerStyle="text-align: left；font-weight: bold;" />
                </TreeTable>



                <!-- 骨架屏表格 -->
                <TreeTable :value="Skeletons" v-if="treetableSkeleton" class="skeleton-table">
                    <!-- 业务人员列 -->
                    <Column header="业务人员" footer="总计:" expander field="Name">
                        <template #body>
                            <Skeleton width="100%" height="1.5rem"></Skeleton>
                        </template>

                    </Column>

                    <!-- 客户类型列 -->
                    <Column header="客户类型" field="Type">
                        <template #body>
                            <Skeleton width="100%" height="1.5rem"></Skeleton>
                        </template>

                    </Column>

                    <!-- 新增客户数列 -->
                    <Column header="新增客户数" field="AddCustomer">
                        <template #body>
                            <Skeleton width="100%" height="1.5rem"></Skeleton>
                        </template>
                        <template #footer>
                            <Skeleton width="80%" height="1.5rem"></Skeleton>
                        </template>
                    </Column>

                    <!-- 总客户数列 -->
                    <Column header="总客户数" field="TotalCustomer">
                        <template #body>
                            <Skeleton width="100%" height="1.5rem"></Skeleton>
                        </template>
                        <template #footer>
                            <Skeleton width="80%" height="1.5rem"></Skeleton>
                        </template>
                    </Column>

                    <!-- 交易日日均权益列 -->
                    <Column header="交易日日均权益" field="TradingDayAverageEquity">
                        <template #body>
                            <Skeleton width="100%" height="1.5rem"></Skeleton>
                        </template>
                        <template #footer>
                            <Skeleton width="80%" height="1.5rem"></Skeleton>
                        </template>
                    </Column>

                    <!-- 期初权益列 -->
                    <Column header="期初权益" field="OpeningEquity">
                        <template #body>
                            <Skeleton width="100%" height="1.5rem"></Skeleton>
                        </template>
                        <template #footer>
                            <Skeleton width="80%" height="1.5rem"></Skeleton>
                        </template>
                    </Column>

                    <!-- 期末权益列 -->
                    <Column header="期末权益" field="ClosingEquity">
                        <template #body>
                            <Skeleton width="100%" height="1.5rem"></Skeleton>
                        </template>
                        <template #footer>
                            <Skeleton width="80%" height="1.5rem"></Skeleton>
                        </template>
                    </Column>

                    <!-- 入金列 -->
                    <Column header="入金" field="Deposit">
                        <template #body>
                            <Skeleton width="100%" height="1.5rem"></Skeleton>
                        </template>
                        <template #footer>
                            <Skeleton width="80%" height="1.5rem"></Skeleton>
                        </template>
                    </Column>

                    <!-- 出金列 -->
                    <Column header="出金" field="Withdrawal">
                        <template #body>
                            <Skeleton width="100%" height="1.5rem"></Skeleton>
                        </template>
                        <template #footer>
                            <Skeleton width="80%" height="1.5rem"></Skeleton>
                        </template>
                    </Column>

                    <!-- 净入金列 -->
                    <Column header="净入金" field="NetDeposit">
                        <template #body>
                            <Skeleton width="100%" height="1.5rem"></Skeleton>
                        </template>
                        <template #footer>
                            <Skeleton width="80%" height="1.5rem"></Skeleton>
                        </template>
                    </Column>

                    <!-- 总手续费列 -->
                    <Column header="总手续费" field="TotalCommission">
                        <template #body>
                            <Skeleton width="100%" height="1.5rem"></Skeleton>
                        </template>
                        <template #footer>
                            <Skeleton width="80%" height="1.5rem"></Skeleton>
                        </template>
                    </Column>

                    <!-- 留存手续费列 -->
                    <Column header="留存手续费" field="RetentionFee">
                        <template #body>
                            <Skeleton width="100%" height="1.5rem"></Skeleton>
                        </template>
                        <template #footer>
                            <Skeleton width="80%" height="1.5rem"></Skeleton>
                        </template>
                    </Column>

                    <!-- 总盈亏列 -->
                    <Column header="总盈亏" field="TotalProfitLoss">
                        <template #body>
                            <Skeleton width="100%" height="1.5rem"></Skeleton>
                        </template>
                        <template #footer>
                            <Skeleton width="80%" height="1.5rem"></Skeleton>
                        </template>
                    </Column>
                </TreeTable>

            </template>

        </Card>
    </div>
</template>

<script setup>
import { ref, computed, inject } from 'vue';
import TreeTable from 'primevue/treetable';
import Card from 'primevue/card';
import FloatLabel from 'primevue/floatlabel';
import DatePicker from 'primevue/datepicker';
import Button from 'primevue/button';
import Select from 'primevue/select';
import Column from 'primevue/column';
import Skeleton from 'primevue/skeleton';

import { useToast } from "primevue/usetoast";
const toast = useToast();


import axiosInstance from '../axiosConfig.js';

// import * as XLSX from 'xlsx';
import ExcelJS from 'exceljs';

// 将查询操作或导出操作的结果传递给HomePage.vue —— search_success|search_failed   export_success|export_failed
const update_DataBoard_operationRecord = inject("update_DataBoard_operationRecord");
const update_operation_message = inject('update_operation_message')

// 按钮可操作状态
const isOperable = ref(false);

// 日期选择器变量
const Range1 = ref();
const Range2 = ref();

// 日期格式化函数
const formatDate = (date) => {
    if (!date) return '';

    // 确保是Date对象
    const d = new Date(date);
    const year = d.getFullYear();
    // 月份和日期需要补零
    const month = String(d.getMonth() + 1).padStart(2, '0');
    const day = String(d.getDate()).padStart(2, '0');

    return `${year}${month}${day}`;
};

const operation = (action) => {
    let actionStr = action + '_failed';
    let message = '';
    // console.log('执行操作', Range1.value, Range2.value)
    if ((Range1.value == null || Range1.value == undefined) ||
        (Range2.value == null || Range2.value == undefined)) {
        message = '开始时间和结束时间不能为空，请选择日期后再进行操作！';
        update_DataBoard_operationRecord(actionStr);
        update_operation_message(message);
    } else if (Range1.value > Range2.value) {
        message = '开始时间不能大于结束时间，请重新选择日期！';
        update_DataBoard_operationRecord(actionStr);
        update_operation_message(message);
    } else {
        if (action === 'search') {
            // console.log('执行查询')
            searchData();
            return;
        }
    }
}






//快捷区间选项
const selectedPeriod = ref();
const Period = ref([
    { name: '近1周', code: 'NW' },
    { name: '近1月', code: 'NM' },
    { name: '近3月', code: 'NTM' },
]);

// 计算属性：判断日期选择器是否应该被禁用
const isDatePickerDisabled = computed(() => {
    return selectedPeriod.value !== null && selectedPeriod.value !== undefined;
});


//处理快捷区间变化
const handlePerodChange = (Period) => {
    if (!Period) return;
    // console.log('所选时间区间：' + Period.name)
    const today = new Date();
    today.setHours(0, 0, 0, 0);

    let startDate = new Date();

    switch (Period.code) {
        case 'NW':
            startDate.setDate(startDate.getDate() - 7);
            break;
        case 'NM':
            startDate.setMonth(startDate.getMonth() - 1);
            break;
        case 'NTM':
            startDate.setMonth(startDate.getMonth() - 3);
            break;
        default:
            break;
    }

    Range1.value = startDate;
    Range2.value = today;
};


// 重置按钮：重置所有选择的函数
const resetAllSelections = () => {
    selectedPeriod.value = null;
    Range1.value = null;
    Range2.value = null;
};





// 查询按钮-----------------------------------------------------------------------------------------------------------------------------------------
// 接收接口拉取数据
const TreeData = ref([]);
const treetableShow = ref(true);
// 骨架屏数据
const Skeletons = ref([
    {
        key: 'skeleton-1',
        data: {
            Name: '', Type: '', AddCustomer: '', TotalCustomer: '',
            TradingDayAverageEquity: '', OpeningEquity: '', ClosingEquity: '',
            Deposit: '', Withdrawal: '', NetDeposit: '', TotalCommission: '', RetentionFee: '', TotalProfitLoss: ''
        }
    },
    {
        key: 'skeleton-2',
        data: {
            Name: '', Type: '', AddCustomer: '', TotalCustomer: '',
            TradingDayAverageEquity: '', OpeningEquity: '', ClosingEquity: '',
            Deposit: '', Withdrawal: '', NetDeposit: '', TotalCommission: '', RetentionFee: '', TotalProfitLoss: ''
        }
    },
    {
        key: 'skeleton-3',
        data: {
            Name: '', Type: '', AddCustomer: '', TotalCustomer: '',
            TradingDayAverageEquity: '', OpeningEquity: '', ClosingEquity: '',
            Deposit: '', Withdrawal: '', NetDeposit: '', TotalCommission: '', RetentionFee: '', TotalProfitLoss: ''
        }
    },
    {
        key: 'skeleton-4',
        data: {
            Name: '', Type: '', AddCustomer: '', TotalCustomer: '',
            TradingDayAverageEquity: '', OpeningEquity: '', ClosingEquity: '',
            Deposit: '', Withdrawal: '', NetDeposit: '', TotalCommission: '', RetentionFee: '', TotalProfitLoss: ''
        }
    }
]);


const treetableSkeleton = ref(false);

const searchData = () => {
    // 格式化日期yyyyddmm
    const startDate = formatDate(Range1.value);
    const endDate = formatDate(Range2.value);

    // 隐藏数据列表
    treetableShow.value = false;
    // 显示骨架屏
    treetableSkeleton.value = true;

    // 禁用日期重置、查询、导出按钮
    isOperable.value = true;

    // 调用数据查询接口
    axiosInstance.post('/periodData/queryPeriodDataDetail',
        {
            startDate1: startDate,
            endDate1: endDate,
            sysUserId: localStorage.getItem('userid')
        },
        {
            timeout: 100000 // 设置超时时间为 5 秒（根据需求调整）
        }
    )
        .then(response => {
            // console.log('Data fetched successfully:', response.data);
            if (!response.data || !response.data.data) {
                let message = '查询周期：' + startDate + ' 至 ' + endDate + '的详细业务数据失败,后端返回数据格式错误，请联系管理员！';
                update_DataBoard_operationRecord('search_failed');
                update_operation_message(message);

                // 错误时也要隐藏骨架屏
                treetableShow.value = true;
                treetableSkeleton.value = false;

                isOperable.value = false;

                return;
            }
            if (response.data.code == 0) {
                // 成功操作记录

                const transformedData = transformBackendData(response.data.data);
                TreeData.value = transformedData;

                let message = '成功查询周期：' + startDate + ' 至 ' + endDate + '的详细业务数据。';
                update_DataBoard_operationRecord('search_success');
                update_operation_message(message);

                treetableShow.value = true;
                treetableSkeleton.value = false;

                isOperable.value = false;

            }
        })
        .catch(error => {
            console.error('Error fetching data:', error);

            toast.add({ severity: 'error', summary: '错误', detail: error.message, life: 3000 });

            // 错误时也要隐藏骨架屏
            treetableShow.value = true;
            treetableSkeleton.value = false;

            isOperable.value = false;
        });
};

// 后端数据转换函数
const personKeyCounter = ref(0);
const transformBackendData = (backendData) => {
    // console.log('transformBackendData方法触发');
    // console.log('原始后端数据：', backendData);

    const result = []; // 最终结果集
    let keyCounter = 0;

    // 按部门分组
    const departmentMap = new Map();

    const processData = backendData.periodDataDetailList;

    // 先处理有部门的数据
    processData.forEach(item => {
        if (item.personnelInfo.department_code && item.personnelInfo.department_name) {
            // console.log('item.department_code and item.department_name : ', item.department_code, " ", item.department_name);
            const deptKey = item.personnelInfo.department_code;
            if (!departmentMap.has(deptKey)) {
                departmentMap.set(deptKey, {
                    key: String(keyCounter++),
                    data: {
                        Name: item.personnelInfo.department_name,
                        level: 3,
                        Type: '全部',
                        AddCustomer: 0,
                        TotalCustomer: 0,
                        TradingDayAverageEquity: 0,
                        OpeningEquity: 0,
                        ClosingEquity: 0,
                        Deposit: 0,
                        Withdrawal: 0,
                        NetDeposit: 0,
                        //总手续费
                        TotalCommission: 0,
                        RetentionFee: 0,
                        TotalProfitLoss: 0
                    },
                    children: []
                });
            }
            //检查当前这个部门是否已有子节点，没有的话就将personKeyCounter归0
            if (departmentMap.get(deptKey).children.length === 0) {
                personKeyCounter.value = 0;
            }
            departmentMap.get(deptKey).children.push(createPersonNode(item, departmentMap.get(deptKey).key, personKeyCounter.value++));
        }
    });

    // 处理没有部门的数据
    processData.forEach(item => {
        if (!item.personnelInfo.department_code && !item.personnelInfo.department_name) {
            result.push(createPersonNode(item, null, keyCounter++));
        }
    });

    // 将部门数据添加到结果中
    departmentMap.forEach(dept => {
        // 计算部门汇总数据
        dept.data = calculateSummary(dept.children, dept.data.Name);
        result.push(dept);
    });

    // console.log('转换后的数据：', result);

    return result;
}

// 创建人员节点

function createPersonNode(item, parentKey, keyCounter) {
    // console.log('createPersonNode方法触发');
    const subKeyCounter = keyCounter;
    const personKey = parentKey ? `${parentKey}-${subKeyCounter}` : String(subKeyCounter);

    const personNode = {
        key: personKey,
        data: {
            Name: item.personnelInfo.personnel_name,
            level: 2,
            Type: '全部',
            AddCustomer: sumArray(item.new_customer_count),
            TotalCustomer: sumArray(item.total_customer_count),
            TradingDayAverageEquity: sumArray(item.avg_equity_daily),
            OpeningEquity: sumArray(item.initial_equity),
            ClosingEquity: sumArray(item.end_equity),
            Deposit: sumArray(item.deposit),
            Withdrawal: sumArray(item.withdrawal),
            NetDeposit: sumArray(item.net_deposit),
            TotalCommission: sumArray(item.commission_total),
            RetentionFee: sumArray(item.commission_retention),
            TotalProfitLoss: sumArray(item.profit_total)
        },
        children: createCustomerTypeNodes(item, personKey)
    };

    return personNode;
}

// 创建客户类型子节点
function createCustomerTypeNodes(item, parentKey) {
    // console.log('createCustomerTypeNodes方法触发');
    const customerTypes = ['个人', '产业', '机构'];
    const children = [];

    customerTypes.forEach((type, index) => {
        children.push({
            key: `${parentKey}-${index}`,
            data: {
                name: '',
                level: 1,
                Type: type,
                AddCustomer: getValue(item.new_customer_count, index),
                TotalCustomer: getValue(item.total_customer_count, index),
                TradingDayAverageEquity: getValue(item.avg_equity_daily, index),
                OpeningEquity: getValue(item.initial_equity, index),
                ClosingEquity: getValue(item.end_equity, index),
                Deposit: getValue(item.deposit, index),
                Withdrawal: getValue(item.withdrawal, index),
                NetDeposit: getValue(item.net_deposit, index),
                TotalCommission: getValue(item.commission_total, index),
                RetentionFee: getValue(item.commission_retention, index),
                TotalProfitLoss: getValue(item.profit_total, index)
            }
        });
    });

    return children;
}

// 计算部门汇总数据
function calculateSummary(children, departmentName) {
    const summary = {
        Name: departmentName,
        level: 3,
        Type: '全部',
        AddCustomer: 0,
        TotalCustomer: 0,
        TradingDayAverageEquity: 0,
        OpeningEquity: 0,
        ClosingEquity: 0,
        Deposit: 0,
        Withdrawal: 0,
        NetDeposit: 0,
        TotalCommission: 0,
        RetentionFee: 0,
        TotalProfitLoss: 0
    };

    children.forEach(child => {
        summary.AddCustomer += child.data.AddCustomer || 0;
        summary.TotalCustomer += child.data.TotalCustomer || 0;
        summary.TradingDayAverageEquity += child.data.TradingDayAverageEquity || 0;
        summary.OpeningEquity += child.data.OpeningEquity || 0;
        summary.ClosingEquity += child.data.ClosingEquity || 0;
        summary.Deposit += child.data.Deposit || 0;
        summary.Withdrawal += child.data.Withdrawal || 0;
        summary.NetDeposit += child.data.NetDeposit || 0;
        summary.TotalCommission += child.data.TotalCommission || 0;
        summary.RetentionFee += child.data.RetentionFee || 0;
        summary.TotalProfitLoss += child.data.TotalProfitLoss || 0;
    });

    // 对所有数值字段四舍五入保留两位小数
    Object.keys(summary).forEach(key => {
        // 跳过非数值字段（Name 和 Type）
        if (key !== 'Name' && key !== 'Type') {
            summary[key] = parseFloat(summary[key].toFixed(2));
        }
    });

    return summary;
}

// 工具函数：安全获取数组值
function getValue(array, index) {
    // 2. 处理值：非数值转为 0，数值则保留两位小数（四舍五入）
    const value = parseFloat(array[index]); // 尝试转为数字（非数值会得到 NaN）
    const validValue = isNaN(value) ? 0 : value; // 非数值按 0 处理
    // 3. 保留两位小数并转换为数字类型（与 sumArray 一致）
    return parseFloat(validValue.toFixed(2));
}

// 工具函数：数组求和
function sumArray(array) {
    if (!array) return 0;
    // 计算总和
    const sum = array.reduce((sum, value) => sum + (value || 0), 0);
    // 四舍五入保留两位小数，并转换为数字类型
    return parseFloat(sum.toFixed(2));
}

// 通用的汇总计算函数
const createSumComputed = (field) => {
    return computed(() => {
        // 计算总和
        const total = TreeData.value.reduce((sum, node) => sum + (node.data[field] || 0), 0);
        // 四舍五入保留两位小数，并转换为数字类型
        return parseFloat(total.toFixed(2));
    });
};

// 使用通用函数创建所有计算属性
const Sum_AddCustomer = createSumComputed('AddCustomer');
const Sum_TotalCustomer = createSumComputed('TotalCustomer');
const Sum_TradingDayAverageEquity = createSumComputed('TradingDayAverageEquity');
const Sum_OpeningEquity = createSumComputed('OpeningEquity');
const Sum_ClosingEquity = createSumComputed('ClosingEquity');
const Sum_Deposit = createSumComputed('Deposit');
const Sum_Withdrawal = createSumComputed('Withdrawal');
const Sum_NetDeposit = createSumComputed('NetDeposit');
const Sum_TotalCommission = createSumComputed('TotalCommission');
const Sum_RetentionFee = createSumComputed('RetentionFee');
const Sum_TotalProfitLoss = createSumComputed('TotalProfitLoss');


// 样式转换相关 ------------------------------------------------------------------------------------------------------------------------------
// 客户类型样式映射函数
const getTypeClass = (type) => {
    switch (type) {
        case '全部':
            return 'type-tag type-all';
        case '个人':
            return 'type-tag type-personal';
        case '产业':
            return 'type-tag type-industrial';
        case '机构':
            return 'type-tag type-institutional';
        default:
            return 'type-tag type-all';
    }
};




// 导出按钮
const dt = ref();
const exportCSV = async () => {
    if (!TreeData.value || TreeData.value.length === 0) {
        let message = '导出失败，没有可导出的数据！';
        update_DataBoard_operationRecord('export_failed');
        update_operation_message(message);
        return;
    }

    // 创建工作簿和工作表
    const workbook = new ExcelJS.Workbook();
    const worksheet = workbook.addWorksheet('周期业务数据明细');

    // 设置列宽
    worksheet.columns = [
        { width: 12 }, // 部门
        { width: 12 }, // 业务人员
        { width: 12 }, // 客户类型
        { width: 12 }, // 新增客户数
        { width: 12 }, // 总客户数
        { width: 15 }, // 交易日日均权益
        { width: 12 }, // 期初权益
        { width: 12 }, // 期末权益
        { width: 12 }, // 入金
        { width: 12 }, // 出金
        { width: 12 }, // 净入金
        { width: 12 }, // 总手续费
        { width: 12 }, // 留存手续费
        { width: 12 }  // 总盈亏
    ];

    // 准备表头（2行：主标题 + 列名）
    let mainHeader = '周期业务数据明细（' + formatDate(Range1.value) + '至' + formatDate(Range2.value) + ")";

    // 添加主标题（第1行，exceljs行号从1开始）
    const titleRow = worksheet.getRow(1);
    titleRow.getCell(1).value = mainHeader;
    worksheet.mergeCells('A1:N1'); // 合并A1到N1
    titleRow.getCell(1).style = {
        font: { bold: true, size: 16 },
        alignment: { horizontal: 'center', vertical: 'middle' }
    };
    titleRow.height = 25;

    // 添加表头（第2行）
    const headers = ['部门', '业务人员', '客户类型', '新增客户数', '总客户数', '交易日日均权益', '期初权益', '期末权益',
        '入金', '出金', '净入金', '总手续费', '留存手续费', '总盈亏'];

    const headerRow = worksheet.getRow(2);
    headers.forEach((header, index) => {
        const cell = headerRow.getCell(index + 1);
        cell.value = header;
        cell.style = {
            font: { bold: true },
            fill: {
                type: 'pattern',
                pattern: 'solid',
                fgColor: { argb: 'FFB0B0B0' } // 灰色背景
            },
            alignment: { horizontal: 'center', vertical: 'center' },
            border: {
                top: { style: 'thin' },
                left: { style: 'thin' },
                bottom: { style: 'thin' },
                right: { style: 'thin' }
            }
        };
    });
    headerRow.height = 20;

    // 跟踪合并范围和当前行号
    const merges = [];
    let currentRow = 3; // 从第3行开始数据

    // 递归处理树形数据 - 直接操作worksheet，不再使用data数组
    const processNode = (node) => {
        // 部门级节点（level=3）
        if (node.data.level === 3) {
            // console.log('处理部门节点：', node.data.Name);

            const row = worksheet.getRow(currentRow);
            // 设置部门行数据
            row.getCell(1).value = node.data.Name; // 部门列
            row.getCell(2).value = '全部'; // 业务人员列（汇总）
            row.getCell(3).value = ''; // 客户类型
            row.getCell(4).value = node.data.AddCustomer;
            row.getCell(5).value = node.data.TotalCustomer;
            row.getCell(6).value = node.data.TradingDayAverageEquity;
            row.getCell(7).value = node.data.OpeningEquity;
            row.getCell(8).value = node.data.ClosingEquity;
            row.getCell(9).value = node.data.Deposit;
            row.getCell(10).value = node.data.Withdrawal;
            row.getCell(11).value = node.data.NetDeposit;
            row.getCell(12).value = node.data.TotalCommission;
            row.getCell(13).value = node.data.RetentionFee;
            row.getCell(14).value = node.data.TotalProfitLoss;

            // 设置行样式
            for (let i = 1; i <= 14; i++) {
                const cell = row.getCell(i);
                cell.style = {
                    border: {
                        top: { style: 'thin' },
                        left: { style: 'thin' },
                        bottom: { style: 'thin' },
                        right: { style: 'thin' }
                    }
                };
                if (i >= 4) { // 数字列右对齐
                    cell.style.alignment = { horizontal: 'right' };
                }
                if (i >= 1) { // 字体加粗 定义背景颜色
                    cell.style.font = { bold: true };
                    cell.style.fill = {
                        type: 'pattern',
                        pattern: 'solid',
                        fgColor: { argb: 'FFE6E6E6' } // 浅灰背景
                    }
                }
            }

            const dep_row_start = currentRow;
            currentRow++;

            // 处理子节点（业务人员相关数据）
            if (node.children && node.children.length > 0) {
                processPersonNode(node.children);
                const dep_row_end = currentRow - 1;

                // 记录部门合并（只有在多行时才合并）
                if (dep_row_end > dep_row_start) {
                    merges.push({
                        start: { row: dep_row_start, column: 1 },
                        end: { row: dep_row_end, column: 1 }
                    });
                }
            }
        }
        // 业务人员级节点（level=2）
        else if (node.data.level === 2) {
            // console.log('处理人员节点：', node.data.Name);

            const row = worksheet.getRow(currentRow);
            // 设置业务人员行数据
            row.getCell(1).value = ''; // 部门列（继承上级）
            row.getCell(2).value = node.data.Name; // 业务人员列
            row.getCell(3).value = '全部'; // 客户类型（汇总）
            row.getCell(4).value = node.data.AddCustomer;
            row.getCell(5).value = node.data.TotalCustomer;
            row.getCell(6).value = node.data.TradingDayAverageEquity;
            row.getCell(7).value = node.data.OpeningEquity;
            row.getCell(8).value = node.data.ClosingEquity;
            row.getCell(9).value = node.data.Deposit;
            row.getCell(10).value = node.data.Withdrawal;
            row.getCell(11).value = node.data.NetDeposit;
            row.getCell(12).value = node.data.TotalCommission;
            row.getCell(13).value = node.data.RetentionFee;
            row.getCell(14).value = node.data.TotalProfitLoss;

            // 设置行样式
            for (let i = 1; i <= 14; i++) {
                const cell = row.getCell(i);
                cell.style = {
                    border: {
                        top: { style: 'thin' },
                        left: { style: 'thin' },
                        bottom: { style: 'thin' },
                        right: { style: 'thin' }
                    }
                };
                if (i >= 4) { // 数字列右对齐
                    cell.style.alignment = { horizontal: 'right' };
                }
                if (i >= 2) { // 字体加粗 定义背景颜色
                    cell.style.font = { bold: true };
                    cell.style.fill = {
                        type: 'pattern',
                        pattern: 'solid',
                        fgColor: { argb: 'FFE6E6E6' } // 浅灰背景
                    }
                }
            }

            const per_row_start = currentRow;
            currentRow++;

            if (node.children && node.children.length > 0) {
                // console.log('处理客户类型子节点：', node.children);
                processCustomerTypeNode(node.children);
                const per_row_end = currentRow - 1;

                // 记录业务人员合并
                if (per_row_end > per_row_start) {
                    merges.push({
                        start: { row: per_row_start, column: 2 },
                        end: { row: per_row_end, column: 2 }
                    });
                }
            }
        }
    };

    // 处理业务人员的子节点
    const processPersonNode = (children) => {
        // console.log('触发处理处理人员子节点函数');
        children.forEach(child => {
            const row = worksheet.getRow(currentRow);
            // 设置业务人员子节点数据
            row.getCell(1).value = ''; // 部门列
            row.getCell(2).value = child.data.Name; // 业务人员列
            row.getCell(3).value = '全部'; // 客户类型（汇总）
            row.getCell(4).value = child.data.AddCustomer;
            row.getCell(5).value = child.data.TotalCustomer;
            row.getCell(6).value = child.data.TradingDayAverageEquity;
            row.getCell(7).value = child.data.OpeningEquity;
            row.getCell(8).value = child.data.ClosingEquity;
            row.getCell(9).value = child.data.Deposit;
            row.getCell(10).value = child.data.Withdrawal;
            row.getCell(11).value = child.data.NetDeposit;
            row.getCell(12).value = child.data.TotalCommission;
            row.getCell(13).value = child.data.RetentionFee;
            row.getCell(14).value = child.data.TotalProfitLoss;

            // 设置行样式
            for (let i = 1; i <= 14; i++) {
                const cell = row.getCell(i);
                cell.style = {
                    border: {
                        top: { style: 'thin' },
                        left: { style: 'thin' },
                        bottom: { style: 'thin' },
                        right: { style: 'thin' }
                    }
                };
                if (i >= 4) { // 数字列右对齐
                    cell.style.alignment = { horizontal: 'right' };
                }
                if (i >= 2) { // 字体加粗 定义背景颜色
                    cell.style.font = { bold: true };
                    cell.style.fill = {
                        type: 'pattern',
                        pattern: 'solid',
                        fgColor: { argb: 'FFE6E6E6' } // 浅灰背景
                    }
                }
            }

            const per_row_start = currentRow;
            currentRow++;

            if (child.children && child.children.length > 0) {
                processCustomerTypeNode(child.children);
                const per_row_end = currentRow - 1;

                // 记录业务人员合并
                if (per_row_end > per_row_start) {
                    merges.push({
                        start: { row: per_row_start, column: 2 },
                        end: { row: per_row_end, column: 2 }
                    });
                }
            }
        });
    };

    const processCustomerTypeNode = (children) => {
        // console.log('触发处理处理客户类型子节点函数');
        children.forEach(child => {
            const row = worksheet.getRow(currentRow);
            // 设置客户类型数据
            row.getCell(1).value = ''; // 部门列
            row.getCell(2).value = ''; // 业务人员列
            row.getCell(3).value = child.data.Type; // 客户类型
            row.getCell(4).value = child.data.AddCustomer;
            row.getCell(5).value = child.data.TotalCustomer;
            row.getCell(6).value = child.data.TradingDayAverageEquity;
            row.getCell(7).value = child.data.OpeningEquity;
            row.getCell(8).value = child.data.ClosingEquity;
            row.getCell(9).value = child.data.Deposit;
            row.getCell(10).value = child.data.Withdrawal;
            row.getCell(11).value = child.data.NetDeposit;
            row.getCell(12).value = child.data.TotalCommission;
            row.getCell(13).value = child.data.RetentionFee;
            row.getCell(14).value = child.data.TotalProfitLoss;

            // 设置行样式
            for (let i = 1; i <= 14; i++) {
                const cell = row.getCell(i);
                cell.style = {
                    border: {
                        top: { style: 'thin' },
                        left: { style: 'thin' },
                        bottom: { style: 'thin' },
                        right: { style: 'thin' }
                    }
                };
                if (i >= 4) { // 数字列右对齐
                    cell.style.alignment = { horizontal: 'right' };
                }
            }

            currentRow++;
        });
    };

    // 处理所有根节点生成数据
    TreeData.value.forEach(node => {
        processNode(node);
    });

    // 添加总计行
    const totalRow = worksheet.getRow(currentRow);
    const totalRowData = [
        '总计',
        '全部',
        '', // 客户类型列留空
        Sum_AddCustomer.value || 0,
        Sum_TotalCustomer.value || 0,
        Sum_TradingDayAverageEquity.value || 0,
        Sum_OpeningEquity.value || 0,
        Sum_ClosingEquity.value || 0,
        Sum_Deposit.value || 0,
        Sum_Withdrawal.value || 0,
        Sum_NetDeposit.value || 0,
        Sum_TotalCommission.value || 0,
        Sum_RetentionFee.value || 0,
        Sum_TotalProfitLoss.value || 0
    ];

    totalRowData.forEach((value, colIndex) => {
        const cell = totalRow.getCell(colIndex + 1);
        cell.value = value;
        cell.style = {
            font: { bold: true, color: { argb: 'FFFF0000' } }, // 红色粗体
            fill: {
                type: 'pattern',
                pattern: 'solid',
                fgColor: { argb: 'FFF0F0F0' } // 浅灰背景
            },
            border: {
                top: { style: 'thin' },
                left: { style: 'thin' },
                bottom: { style: 'thin' },
                right: { style: 'thin' }
            }
        };

        // 数字列右对齐
        if (colIndex >= 3) {
            cell.style.alignment = { horizontal: 'right' };
        }
    });

    // 应用所有合并（在数据设置完成后）
    merges.forEach(merge => {
        worksheet.mergeCells(merge.start.row, merge.start.column, merge.end.row, merge.end.column);
    });

    // 导出文件
    try {
        const buffer = await workbook.xlsx.writeBuffer();
        const blob = new Blob([buffer], {
            type: 'application/vnd.openxmlformats-officedocument.spreadsheetml.sheet'
        });
        const url = URL.createObjectURL(blob);
        const link = document.createElement('a');
        link.href = url;

        const fileName = `周期业务数据明细_${formatDate(Range1.value).replace(/\//g, '-')}_${formatDate(Range2.value).replace(/\//g, '-')}.xlsx`;
        link.download = fileName;
        link.click();
        URL.revokeObjectURL(url);

        // 记录导出成功
        const message = `成功导出周期：${formatDate(Range1.value)} 至 ${formatDate(Range2.value)} 的业务数据明细。`;
        update_DataBoard_operationRecord('export_success');
        update_operation_message(message);
    } catch (error) {
        // console.error('导出失败:', error);
        const message = '导出失败，请重试！';
        update_DataBoard_operationRecord('export_failed');
        update_operation_message(message);
    }
};
</script>

<style>
@layer reset, primevue, custom;

@layer custom {
    .main-periodCompareDataBoard-container {
        /* background-color: bisque; */
        /* height: 800px; */
        /* width: 100%; */

    }

    .main-periodCompareDataBoard-card {
        border: #6a6b6e;
        width: 100%;

        min-width: 100rem;

    }



    .main-periodCompareDataBoard-container .card-header {
        display: flex;
        flex-direction: row;
        align-items: center;

        position: relative;

        width: 100%;

    }

    .main-periodCompareDataBoard-container .card-header::before {
        content: '';
        position: absolute;
        top: 0;
        left: 0;
        right: 0;
        bottom: 0;
        background-image: url('@/assets/coolbackgrounds-topography-micron.svg');
        background-size: cover;
        background-position: center;
        background-repeat: no-repeat;
        transform: scaleX(-1);
        z-index: 0;
    }

    .main-periodCompareDataBoard-container .card-header .left {
        /* background-color: antiquewhite; */

        width: 40%;

        font-size: 1.3rem;
        font-weight: bold;
        letter-spacing: 0.08rem;

        padding: 0.4rem;
        padding-left: 0.7rem;
        flex: none;
    }

    .main-periodCompareDataBoard-container .card-header .right {
        /* background-color: rgb(209, 165, 106); */

        display: flex;
        flex-direction: row;
        align-items: center;
        justify-content: flex-end;
        gap: 0.8rem;

        width: 60%;

        padding: 0.7rem;
        flex: none;
    }

    .main-periodCompareDataBoard-container .card-header .right .download-button {
        background-color: #202020;
        border: none;
        flex: none;
    }

    /* TreeTable footer 加粗样式 */
    .p-treetable .p-treetable-tfoot td {
        font-weight: bold !important;
    }

    /* TreeTable 业务人员名称加粗样式 */
    .bold-name {
        font-weight: bold;
    }

    /* 客户类型标签样式 */
    .type-tag {
        display: inline-block;
        padding: 4px 12px;
        border-radius: 16px;
        font-size: 0.875rem;
        font-weight: 500;
        text-align: center;
        min-width: 60px;
    }

    .type-all {
        background-color: #2a363b;
        color: white;
    }

    .type-personal {
        background-color: #e84a5f;
        color: white;
    }

    .type-industrial {
        background-color: #ff847c;
        color: white;
    }

    .type-institutional {
        background-color: #fecea8;
        color: white;
    }

}
</style>
