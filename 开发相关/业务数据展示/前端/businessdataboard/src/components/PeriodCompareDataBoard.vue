<template>
    <div class="main-periodCompareDataBoard-container">
        <Card style="min-width: 30rem;">
            <template #header>
                <div class="card-header">
                    <div class="left">
                        <span class="card-title" style="z-index: 10;">周期业务数据对比</span>
                    </div>
                    <div class="right">
                        <FloatLabel variant="on">
                            <DatePicker v-model="Range1" inputId="range1_label" showIcon iconDisplay="input"
                                selectionMode="range" :manualInput="false" size="small" />
                            <label for="range1_label">周期1</label>
                        </FloatLabel>
                        <FloatLabel variant="on">
                            <DatePicker v-model="Range2" inputId="range2_label" showIcon iconDisplay="input"
                                selectionMode="range" :manualInput="false" size="small" />
                            <label for="range2_label">周期2</label>
                        </FloatLabel>
                        <Button class="download-button" icon="pi pi-undo" style="font-size: 1rem;" size="small"
                            v-tooltip.bottom="'重置周期选择'" @click="resetAllSelections" :disabled="isOperable" />
                        <Button class="download-button" icon="pi pi-search" style="font-size: 1rem;" size="small"
                            @click="operation('search')" :disabled="isOperable" />
                        <Button class="download-button" icon="pi pi-external-link" label="导出" style="font-size: 1rem;"
                            size="small" @click="exportCSV()" />

                    </div>

                </div>
            </template>
            <template #content>
                <Splitter style="height: 40rem;min-width: 100rem;">
                    <SplitterPanel class="options-panel" :size="25" :minSize="10">
                        <div class="info">
                            <p>请选择要查看的业务数据项</p>
                        </div>
                        <div class="options-panel-content">
                            <Listbox v-model="selectedIndicator" :options="indicators" optionLabel="name"
                                class="listbox" listStyle="max-height:500px" />
                        </div>

                    </SplitterPanel>
                    <SplitterPanel class="data-panel" :size="75">

                        <TreeTable :value="TreeData" tableStyle="min-width: 50rem" scrollable removableSort
                            scrollHeight="37rem" v-if="treetableShow">
                            <template #header>
                                <span style="font-weight: bold;">{{ selectedIndicator === null ?
                                    '请选择业务数据项' : selectedIndicator.name }}</span>
                            </template>

                            <!-- 业务人员列 -->
                            <Column expander footer="总计:" footerStyle="text-align: left; background-color: #f2f2f2;"
                                filterField="name">
                                <template #header>
                                    <div class="filter-template">
                                        <span style="font-weight: bold;">业务人员</span>
                                        <Button icon="pi pi-filter" text rounded severity="secondary" size="small"
                                            @click="handlePersonnelFilter" v-tooltip.bottom="'业务人员筛选器'" />
                                        <Popover ref="op1" class="filter-popover">
                                            <div class="popover-content">
                                                <span class="popover-title">业务人员筛选器</span>

                                                <MultiSelect :options="personnelOptionsList" optionLabel="name"
                                                    placeholder="选择业务人员" v-model="selectedPersonnel"
                                                    class="multi-select" />

                                                <MultiSelect :options="departmentOptionsList" optionLabel="name"
                                                    placeholder="选择部门" v-model="selectedDepartments"
                                                    class="multi-select" />

                                                <Divider class="custom-divider" />


                                                <Card class="selected-card">
                                                    <template #content>
                                                        <div class="selected-items-container">
                                                            <div class="selected-header">
                                                                <span class="selected-title">已选择的筛选条件：</span>
                                                            </div>


                                                            <div class="chips-wrapper">
                                                                <div class="person-chips-container">
                                                                    <Chip v-for="person in selectedPersonnel"
                                                                        :key="person.code" :label="person.name"
                                                                        removable @remove="removePersonnel(person)"
                                                                        class="custom-chip person-chip" />
                                                                </div>
                                                            </div>


                                                            <div class="chips-wrapper">
                                                                <div class="dept-chips-container">
                                                                    <Chip v-for="dept in selectedDepartments"
                                                                        :key="dept.code" :label="dept.name" removable
                                                                        @remove="removeDepartment(dept)"
                                                                        class="custom-chip dept-chip" />
                                                                </div>
                                                            </div>

                                                            <Button label="清空全部" text size="small" severity="secondary"
                                                                @click="clearAllSelections('personnel')"
                                                                class="clear-all-btn" />
                                                        </div>
                                                    </template>
                                                </Card>

                                                <Divider class="action-divider" />

                                                <div class="action-buttons">
                                                    <Button label="取消" text size="small"
                                                        @click="closePopover('personnel')" />
                                                    <Button label="应用" severity="primary" size="small"
                                                        @click="applyFilters('personnel')" />
                                                </div>
                                            </div>
                                        </Popover>

                                    </div>
                                </template>
                                <template #body="slotProps">
                                    <span class="bold-name">{{ slotProps.node.data.name
                                        }}</span>
                                </template>
                            </Column>

                            <!-- 客户类型列 -->
                            <Column footerStyle="text-align: left; background-color: #f2f2f2;" filterField="type">
                                <template #header>
                                    <div class="filter-template">
                                        <span style="font-weight: bold;">客户类型</span>
                                    </div>
                                </template>
                                <template #body="slotProps">
                                    <span :class=getTypeClass(slotProps.node.data.type)>{{ slotProps.node.data.type
                                    }}</span>
                                </template>
                            </Column>

                            <Column header="周期1" sortable field="period1" :footer="Sum_data1"
                                footerStyle="text-align: left; font-weight: bold; background-color: #f2f2f2;" />
                            <Column header="周期2" sortable field="period2" :footer="Sum_data2"
                                footerStyle="text-align: left; font-weight: bold; background-color: #f2f2f2;" />
                            <Column header="差值（周期1-周期2）" sortable field="diff" :footer="Sum_diff"
                                footerStyle="text-align: left; font-weight: bold;color: #334155; background-color: #f2f2f2;"
                                headerStyle="color: #334155;">
                                <template #body="slotProps">
                                    <span :class="getDiffColumnColor(slotProps.node.data.diff)">
                                        {{ slotProps.node.data.diff }}
                                    </span>
                                </template>
                            </Column>
                        </TreeTable>



                        <!-- 骨架屏表格 -->
                        <TreeTable :value="Skeletons" v-if="treetableSkeleton" class="skeleton-table" scrollable
                            scrollHeight="37rem" ref="dt">
                            <template #header>
                                <span style="font-weight: bold;">{{ selectedIndicator.name }}</span>
                            </template>

                            <!-- 业务人员列 -->
                            <Column expander footer="总计:" footerStyle="text-align: left; background-color: #f2f2f2;">
                                <template #header>
                                    <div class="filter-template">
                                        <span style="font-weight: bold;">业务人员</span>
                                        <Button icon="pi pi-filter" text rounded severity="secondary" size="small"
                                            disabled />
                                    </div>
                                </template>

                                <template #body>
                                    <Skeleton width="100%" height="1.5rem"></Skeleton>
                                </template>

                            </Column>

                            <!-- 客户类型列 -->
                            <Column footerStyle="text-align: left; background-color: #f2f2f2;">
                                <template #header>
                                    <div class="filter-template">
                                        <span style="font-weight: bold;">客户类型</span>
                                        <Button icon="pi pi-filter" text rounded severity="secondary" size="small"
                                            disabled />
                                    </div>
                                </template>
                                <template #body>
                                    <Skeleton width="100%" height="1.5rem"></Skeleton>
                                </template>
                            </Column>

                            <!-- 周期1列 -->
                            <Column header="周期1"
                                footerStyle="text-align: left; font-weight: bold; background-color: #f2f2f2;">
                                <template #body>
                                    <Skeleton width="100%" height="1.5rem"></Skeleton>
                                </template>
                                <template #footer>
                                    <Skeleton width="80%" height="1.5rem"></Skeleton>
                                </template>
                            </Column>

                            <!-- 周期2列 -->
                            <Column header="周期2"
                                footerStyle="text-align: left; font-weight: bold; background-color: #f2f2f2;">
                                <template #body>
                                    <Skeleton width="100%" height="1.5rem"></Skeleton>
                                </template>
                                <template #footer>
                                    <Skeleton width="80%" height="1.5rem"></Skeleton>
                                </template>
                            </Column>

                            <!-- 差值列 -->
                            <Column header="差值（周期1-周期2）"
                                footerStyle="text-align: left; font-weight: bold; background-color: #f2f2f2;">
                                <template #body>
                                    <Skeleton width="100%" height="1.5rem"></Skeleton>
                                </template>
                                <template #footer>
                                    <Skeleton width="80%" height="1.5rem"></Skeleton>
                                </template>
                            </Column>
                        </TreeTable>



                    </SplitterPanel>
                </Splitter>

            </template>
        </Card>
    </div>

</template>

<script setup>
import Card from 'primevue/card';
import FloatLabel from 'primevue/floatlabel';
import DatePicker from 'primevue/datepicker';
import Button from 'primevue/button';

import Splitter from 'primevue/splitter';
import SplitterPanel from 'primevue/splitterpanel';

import Column from 'primevue/column';
import TreeTable from 'primevue/treetable';

import Popover from 'primevue/popover';
import MultiSelect from 'primevue/multiselect';
import Divider from 'primevue/divider';
import Chip from 'primevue/chip';

import Skeleton from 'primevue/skeleton';

import Listbox from 'primevue/listbox';

import { ref, computed, inject, watch } from "vue";

import axiosInstance from '../axiosConfig.js';
import { cloneDeep } from 'lodash';

import ExcelJS from 'exceljs';


const operationRecord = ref('');
const update_DataBoard_operationRecord = inject("update_DataBoard_operationRecord");
inject("DataBoard_operationRecord", operationRecord);

const operation_message = ref('');
const update_operation_message = inject('update_operation_message')
inject('operation_message', operation_message);

// 按钮可操作状态
const isOperable = ref(false);

// 日期选择器变量
const Range1 = ref();
const Range2 = ref();

// 日期格式化函数
const formatDate = (date) => {
    if (!date) return '';

    const d = new Date(date);
    const year = d.getFullYear();
    const month = String(d.getMonth() + 1).padStart(2, '0');
    const day = String(d.getDate()).padStart(2, '0');

    return `${year}${month}${day}`; // 修改为后端需要的格式 yyyyMMdd
};


const selectedIndicator = ref({ name: '未选择指标项' });
const indicators = ref([
    { name: '新增客户数' },
    { name: '总客户数' },
    { name: '交易日日均权益' },
    { name: '期初权益' },
    { name: '期末权益' },
    { name: '入金金额' },
    { name: '出金金额' },
    { name: '净入金' },
    { name: '总手续费' },
    { name: '留存手续费' },
    { name: '总盈亏' }
]);

// 重置按钮：重置所有选择的函数
const resetAllSelections = () => {
    Range1.value = null;
    Range2.value = null;
};

const operation = (action) => {
    let actionStr = action + '_failed';
    let message = '';

    if ((Range1.value !== null && Range1.value !== undefined) &&
        (Range2.value !== null && Range2.value !== undefined)) {
        if (action === 'search') {
            console.log('执行查询')
            searchData();
            return;
        }
    }
    message = '周期1和周期2不能为空，请选择日期后再进行操作！';
    update_DataBoard_operationRecord(actionStr);
    update_operation_message(message);
}



// 骨架屏相关变量
const treetableShow = ref(true);
const treetableSkeleton = ref(false);

// 骨架屏数据
const Skeletons = ref([
    {
        key: 'skeleton-1',
        data: {
            name: '', type: '', period1: '', period2: '', diff: ''
        }
    },
    {
        key: 'skeleton-2',
        data: {
            name: '', type: '', period1: '', period2: '', diff: ''
        }
    },
    {
        key: 'skeleton-3',
        data: {
            name: '', type: '', period1: '', period2: '', diff: ''
        }
    },
    {
        key: 'skeleton-4',
        data: {
            name: '', type: '', period1: '', period2: '', diff: ''
        }
    }
]);

// 查询数据
const searchData = async () => {
    // 日期参数
    const startDate1 = formatDate(Range1.value[0]);
    const endDate1 = formatDate(Range1.value[1]);
    const startDate2 = formatDate(Range2.value[0]);
    const endDate2 = formatDate(Range2.value[1]);
    console.log('startDate1:', startDate1, 'endDate1:', endDate1, 'startDate2:', startDate2, 'endDate2:', endDate2);

    //指标参数
    const indicator = selectedIndicator.value.name;

    if (startDate1 === '' || endDate1 === '') {
        console.log('startDate1 or endDate1 is empty');
        let message = '周期1的开始日期和结束日期不能为空，请选择日期后再进行操作！';
        update_DataBoard_operationRecord('search_failed');
        update_operation_message(message);

        return;
    } else if (startDate2 === '' || endDate2 === '') {
        let message = '周期2的开始日期和结束日期不能为空，请选择日期后再进行操作！';
        update_DataBoard_operationRecord('search_failed');
        update_operation_message(message);
        return;
    }
    if (indicator === '未选择指标项') {
        let message = '请选择指标项！';
        update_DataBoard_operationRecord('search_failed');
        update_operation_message(message);
        return;
    }

    // 显示骨架屏，隐藏真实数据
    treetableShow.value = false;
    treetableSkeleton.value = true;

    // 刷新、查询操作禁用
    isOperable.value = true;

    // 这里可以使用格式化后的日期进行后续操作
    axiosInstance.post('/periodData/queryIndicatorCompareDetail',
        {
            startDate1: startDate1,
            endDate1: endDate1,
            startDate2: startDate2,
            endDate2: endDate2,
            indicator: indicator, // 使用选中的指标名称
            sysUserId: localStorage.getItem('userid')
        },

        {
            timeout: 100000
        }
    )
        .then(response => {
            // console.log('Data fetched successfully:', response.data);
            if (!response.data || !response.data.data) {
                let message = '查询周期1：' + startDate1 + ' 至 ' + endDate1 + '与' + '周期2：' + startDate2 + ' 至 ' + endDate2 + '的详细业务数据失败,后端返回数据格式错误，请联系管理员！';
                update_DataBoard_operationRecord('search_failed');
                update_operation_message(message);

                // 错误时也要隐藏骨架屏
                treetableShow.value = true;
                treetableSkeleton.value = false;

                isOperable.value = false;

                return;
            }
            if (response.data.code == 0) {
                // 处理成功响应
                TreeOldJsonData.value = cloneDeep(response.data.data);
                TreeJsonData.value = cloneDeep(response.data.data);

                TreeData.value = transformBackendData(response.data.data);
                TreeOldData.value = cloneDeep(TreeData.value);
                filterOptionsSetting(response.data.data.indicatorPeriodCompareDetailList);

                let message = `成功查询周期1: ${response.data.data.period1}, 周期2: ${response.data.data.period2}的${response.data.data.compareIndicator}数据及差值。`;
                update_DataBoard_operationRecord('search_success');
                update_operation_message(message);

                // 隐藏骨架屏，显示真实数据
                treetableShow.value = true;
                treetableSkeleton.value = false;

                isOperable.value = false;
            }

        })
        .catch(error => {
            console.error('Error fetching data:', error);
            let message = '查询周期1：' + startDate1 + ' 至 ' + endDate1 + '与' + '周期2：' + startDate2 + ' 至 ' + endDate2 + '的详细业务数据失败,请重试';
            update_DataBoard_operationRecord('search_failed');
            update_operation_message(message);

            // 错误时也要隐藏骨架屏
            treetableShow.value = true;
            treetableSkeleton.value = false;

            isOperable.value = false;
        });

};

const TreeJsonData = ref([]);
const TreeOldJsonData = ref([]);
const TreeData = ref([]);
const TreeOldData = ref([]);

// 后端数据转换函数
const personKeyCounter = ref(0);
const transformBackendData = (backendData) => {
    // console.log('transformBackendData方法触发');
    // console.log('原始后端数据：', backendData);

    const result = []; // 最终结果集
    let keyCounter = 0;

    // 按部门分组
    const departmentMap = new Map();

    const processData = backendData.indicatorPeriodCompareDetailList;

    // 先处理有部门的数据
    processData.forEach(item => {
        if (item.personnelInfo.department_code && item.personnelInfo.department_name) {
            // console.log('item.department_code and item.department_name : ', item.department_code, " ", item.department_name);
            const deptKey = item.personnelInfo.department_code;
            if (!departmentMap.has(deptKey)) {
                departmentMap.set(deptKey, {
                    key: String(keyCounter++),
                    data: {
                        name: item.personnelInfo.department_name,
                        level: 3,
                        type: '全部',
                        period1: 0,
                        period2: 0,
                        diff: 0
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
        dept.data = calculateSummary(dept.children, dept.data.name);
        result.push(dept);
    });

    console.log('转换后的数据：', result);

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
            name: item.personnelInfo.personnel_name,
            level: 2,
            type: '全部',
            period1: sumArray(item.period1),
            period2: sumArray(item.period2),
            diff: sumArray(item.diff)

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
                type: type,
                period1: getValue(item.period1, index),
                period2: getValue(item.period2, index),
                diff: getValue(item.diff, index)
            }
        });
    });

    return children;
}

// 计算部门汇总数据
function calculateSummary(children, departmentName) {
    const summary = {
        name: departmentName,
        level: 3,
        type: '全部',
        period1: 0,
        period2: 0,
        diff: 0
    };

    children.forEach(child => {
        summary.period1 += child.data.period1 || 0;
        summary.period2 += child.data.period2 || 0;
        summary.diff += child.data.diff || 0;
    });

    // 对所有数值字段四舍五入保留两位小数
    Object.keys(summary).forEach(key => {
        // 跳过非数值字段（Name 和 Type）
        if (key !== 'name' && key !== 'type') {
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
const Sum_data1 = createSumComputed('period1');
const Sum_data2 = createSumComputed('period2');
const Sum_diff = createSumComputed('diff');

// 监听 selectedIndicator 的变化
watch(selectedIndicator, (newVal, oldVal) => {
    console.log('selectedIndicator changed from', oldVal, 'to', newVal);
    // 可以在这里添加其他逻辑，比如自动触发查询等
});

// filter 相关 ------------------------------------------------------------------------------------------------------------------------------
const personnelOptionsList = ref([]);
const departmentOptionsList = ref([]);
// const customerTypeOptionsList = ref([
//     { name: '全部', code: 'ALL' },
//     { name: '机构', code: 'ORG' },
//     { name: '产业', code: 'IND' },
//     { name: '个人', code: 'PER' }
// ]);
const selectedPersonnel = ref([]);
const selectedDepartments = ref([]);
const selectedCustomerTypes = ref([
    { name: '全部', code: 'ALL' }]);

const filterOptionsSetting = (List) => {
    const personnelMap = new Map();
    const departmentMap = new Map();

    List.forEach(item => {
        if (item.personnelInfo?.personnel_code && item.personnelInfo?.personnel_name) {
            // 添加人员 - 使用更唯一的标识
            const personKey = `${item.personnelInfo.personnel_code}_${item.personnelInfo.personnel_name}`;
            if (!personnelMap.has(personKey)) {
                personnelMap.set(personKey, {
                    key: personKey,  // 使用组合key确保唯一性
                    code: item.personnelInfo.personnel_code,
                    name: item.personnelInfo.personnel_name
                });
            }

            // 添加部门
            if (item.personnelInfo.department_code && item.personnelInfo.department_name) {
                const deptKey = `${item.personnelInfo.department_code}_${item.personnelInfo.department_name}`;
                if (!departmentMap.has(deptKey)) {
                    departmentMap.set(deptKey, {
                        key: deptKey,  // 使用组合key确保唯一性
                        code: item.personnelInfo.department_code,
                        name: item.personnelInfo.department_name
                    });
                }
            }
        }
    });

    personnelOptionsList.value = Array.from(personnelMap.values());
    departmentOptionsList.value = Array.from(departmentMap.values());
    console.log('生成的业务人员选项列表：', personnelOptionsList.value);
};

const op1 = ref();
const op2 = ref();
const handlePersonnelFilter = (event) => {
    // 打开业务人员筛选器 PopOver 面板
    console.log('handlePersonnelFilter方法触发');

    op1.value.toggle(event);

};


// const handleClientTypeFilter = (event) => {
//     // 打开客户类型筛选器 PopOver 面板
//     console.log('handleClientTypeFilter方法触发');

//     op2.value.toggle(event);
// };


// 移除业务人员 - 修复后
const removePersonnel = (person) => {
    selectedPersonnel.value = selectedPersonnel.value.filter(p => p.key !== person.key);
};

// const removeCustomerType = (type) => {
//     selectedCustomerTypes.value = selectedCustomerTypes.value.filter(t => t.code !== type.code);
// };

// 移除部门 - 修复后
const removeDepartment = (dept) => {
    selectedDepartments.value = selectedDepartments.value.filter(d => d.key !== dept.key);
};



// 清空所有选择
const clearAllSelections = (param) => {
    if (param === 'personnel') {
        selectedPersonnel.value = [];
        selectedDepartments.value = [];
    } else if (param === 'client_type') {
        selectedCustomerTypes.value = [{ name: '全部', code: 'ALL' }];
    }

};

// 应用条件筛选
const applyFilters = (param) => {
    // 直接基于 selectedPersonnel 和 selectedDepartments 进行对TreeData.value进行过滤
    console.log('applyFilters方法触发');

    let actionStr = 'filter_success';
    let message = '应用筛选成功！';
    // let filterData = [];

    console.log('已选择人员：', selectedPersonnel.value);
    console.log('已选择部门：', selectedDepartments.value);
    console.log('当前数据', TreeData.value);

    if (param === 'personnel') {
        if (selectedPersonnel.value.length === 0 && selectedDepartments.value.length === 0 && TreeOldData.value.length === 0) {
            console.log('没有选择任何筛选条件，无法应用筛选XXXXXXXX');
            message = '没有选择任何筛选条件，无法应用筛选！';
            actionStr = 'filter_failed';
        } else {
            // 过滤人员
            filterData_personnel();
            return;
        }
    } else if (param === 'client_type') {
        if (selectedCustomerTypes.value.length === 1 && selectedCustomerTypes.value[0].code === 'ALL') {
            console.log('没有选择任何筛选条件，无法应用筛选');
            message = '没有选择任何筛选条件，无法应用筛选！';
            actionStr = 'filter_failed';

        } else {
            // 过滤客户类型
        }
    }




    update_DataBoard_operationRecord(actionStr);
    update_operation_message(message);
    return;
}

// 取消筛选条件
const closePopover = (param) => {
    console.log('closePopover方法触发');
    if (param === 'personnel') {
        op1.value.hide();
    } else if (param === 'client_type') {
        op2.value.hide();
    }

};

const filterData_personnel = () => {
    console.log('filterData方法触发');
    if (selectedPersonnel.value.length === 0 && selectedDepartments.value.length === 0) {
        TreeData.value = TreeOldData.value;
        return;
    }


    // 先获取原始数据（注意：如果原始数据是引用类型，建议浅拷贝避免修改源数据）
    const originalData = TreeOldJsonData.value.indicatorPeriodCompareDetailList;
    console.log('筛选前的TreeJsonData数据：', originalData);
    console.log(selectedPersonnel.value);

    let filteredData = [];

    // 只有已有人员筛选的情况
    if (selectedPersonnel.value.length !== 0 && selectedDepartments.value.length === 0) {
        // 提取选中人员的code列表，方便判断
        const selectedCodes = selectedPersonnel.value.map(p => p.code);
        // 一次性过滤：只保留personnel_code在选中列表中的项
        filteredData = originalData.filter(item =>
            selectedCodes.includes(item.personnelInfo.personnel_code)
        );
    } else if (selectedPersonnel.value.length === 0 && selectedDepartments.value.length !== 0) {
        // 提取选中部门的code列表，方便判断
        const selectedCodes = selectedDepartments.value.map(d => d.code);
        // 一次性过滤：只保留department_code在选中列表中的项
        filteredData = originalData.filter(item =>
            selectedCodes.includes(item.personnelInfo.department_code)
        );
    } else if (selectedPersonnel.value.length !== 0 && selectedDepartments.value.length !== 0) {
        // 同时选择人员和部门
        // 先获取选中人员的code列表，方便判断
        const selectedPersonnelCodes = selectedPersonnel.value.map(p => p.code);
        // 再获取选中部门的code列表，方便判断
        const selectedDepartmentCodes = selectedDepartments.value.map(d => d.code);
        // 一次性过滤：只保留personnel_code和department_code都在选中列表中的项
        filteredData = originalData.filter(item =>
            selectedPersonnelCodes.includes(item.personnelInfo.personnel_code) ||
            selectedDepartmentCodes.includes(item.personnelInfo.department_code)
        );

    }
    // 将过滤后的结果赋值给TreeData
    TreeJsonData.value.indicatorPeriodCompareDetailList = filteredData;
    console.log('筛选后的TreeJsonData数据：', filteredData);
    console.log('此时TreeOldJsonData数据：', TreeOldJsonData.value);
    TreeData.value = transformBackendData(TreeJsonData.value);
}

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

// getDiffColumnColor实现
const getDiffColumnColor = (diff) => {
    if (diff > 0) {
        return 'green-text';
    } else if (diff < 0) {
        return 'red-text';
    } else {
        return '';
    }
}


// 导出功能 -----------------------------------------------------------------------------------------------------------------
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
    let workSheetName = selectedIndicator.value.name + '周期比对明细';
    console.log('正在创建工作表：', workSheetName);
    const worksheet = workbook.addWorksheet(workSheetName);

    // 设置列宽
    worksheet.columns = [
        { width: 12 }, // 部门
        { width: 12 }, // 业务人员
        { width: 12 }, // 客户类型
        { width: 12 }, // 周期1
        { width: 12 }, // 周期2
        { width: 15 }, // 差值
    ];

    // 准备表头（2行：主标题 + 列名）
    // 日期参数
    const startDate1 = formatDate(Range1.value[0]);
    const endDate1 = formatDate(Range1.value[1]);
    const startDate2 = formatDate(Range2.value[0]);
    const endDate2 = formatDate(Range2.value[1]);

    let mainHeader = selectedIndicator.value.name + '周期比对明细（周期1:' + startDate1 + '至' + endDate1 + '，周期2:' + startDate2 + '至' + endDate2 + ")";

    // 添加主标题（第1行，exceljs行号从1开始）
    const titleRow = worksheet.getRow(1);
    titleRow.getCell(1).value = mainHeader;
    worksheet.mergeCells('A1:F1'); // 合并A1到N1
    titleRow.getCell(1).style = {
        font: { bold: true, size: 16 },
        alignment: { horizontal: 'center', vertical: 'middle' }
    };
    titleRow.height = 25;

    // 添加表头（第2行）
    const headers = ['部门', '业务人员', '客户类型', '周期1', '周期2', '差值'];

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
            console.log('处理部门节点：', node.data.name);

            const row = worksheet.getRow(currentRow);
            // 设置部门行数据
            row.getCell(1).value = node.data.name; // 部门列
            row.getCell(2).value = '全部'; // 业务人员列（汇总）
            row.getCell(3).value = ''; // 客户类型
            row.getCell(4).value = node.data.period1;
            row.getCell(5).value = node.data.period2;
            row.getCell(6).value = node.data.diff;


            // 设置行样式
            for (let i = 1; i <= 6; i++) {
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
                if (i === 6) {
                    cell.style.font = { bold: true };
                    if (node.data.diff < 0) { // 值小于0设定为绿色
                        cell.style.font = { color: { argb: 'FFFF2700' }, bold: true }; // 黑色字体
                    } else if (node.data.diff > 0) { // 值大于0设定为红色
                        cell.style.font = { color: { argb: 'FF00F91A' }, bold: true }; // 绿色字体
                    } else {
                        cell.style.font = { color: { argb: 'FF000000' }, bold: true }; // 黑色字体
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
            console.log('处理人员节点：', node.data.name);

            const row = worksheet.getRow(currentRow);
            // 设置业务人员行数据
            row.getCell(1).value = ''; // 部门列（继承上级）
            row.getCell(2).value = node.data.name; // 业务人员列
            row.getCell(3).value = '全部'; // 客户类型（汇总）
            row.getCell(4).value = node.data.period1;
            row.getCell(5).value = node.data.period2;
            row.getCell(6).value = node.data.diff;

            // 设置行样式
            for (let i = 1; i <= 6; i++) {
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
                if (i === 6) {
                    cell.style.font = { bold: true };
                    if (node.data.diff < 0) { // 值小于0设定为绿色
                        cell.style.font = { color: { argb: 'FFFF2700' }, bold: true }; // 黑色字体
                    } else if (node.data.diff > 0) { // 值大于0设定为红色
                        cell.style.font = { color: { argb: 'FF00F91A' }, bold: true }; // 绿色字体
                    } else {
                        cell.style.font = { color: { argb: 'FF000000' }, bold: true }; // 黑色字体
                    }
                }
            }

            const per_row_start = currentRow;
            currentRow++;

            if (node.children && node.children.length > 0) {
                console.log('处理客户类型子节点：', node.children);
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
        console.log('触发处理处理人员子节点函数');
        children.forEach(child => {
            const row = worksheet.getRow(currentRow);
            // 设置业务人员子节点数据
            row.getCell(1).value = ''; // 部门列
            row.getCell(2).value = child.data.name; // 业务人员列
            row.getCell(3).value = '全部'; // 客户类型（汇总）
            row.getCell(4).value = child.data.period1;
            row.getCell(5).value = child.data.period2;
            row.getCell(6).value = child.data.diff;

            // 设置行样式
            for (let i = 1; i <= 6; i++) {
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
                if (i === 6) {
                    cell.style.font = { bold: true };
                    if (child.data.diff < 0) { // 值小于0设定为绿色
                        cell.style.font = { color: { argb: 'FFFF2700' }, bold: true }; // 黑色字体
                    } else if (child.data.diff > 0) { // 值大于0设定为红色
                        cell.style.font = { color: { argb: 'FF00F91A' }, bold: true }; // 绿色字体
                    } else {
                        cell.style.font = { color: { argb: 'FF000000' }, bold: true }; // 黑色字体
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
        console.log('触发处理处理客户类型子节点函数');
        children.forEach(child => {
            const row = worksheet.getRow(currentRow);
            // 设置客户类型数据
            row.getCell(1).value = ''; // 部门列
            row.getCell(2).value = ''; // 业务人员列
            row.getCell(3).value = child.data.type; // 客户类型
            row.getCell(4).value = child.data.period1;
            row.getCell(5).value = child.data.period2;
            row.getCell(6).value = child.data.diff;

            // 设置行样式
            for (let i = 1; i <= 6; i++) {
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
                if (i === 6) {

                    if (child.data.diff < 0) { // 值小于0设定为绿色
                        cell.style.font = { color: { argb: 'FFFF2700' }, bold: true };
                    } else if (child.data.diff > 0) { // 值大于0设定为红色
                        cell.style.font = { color: { argb: 'FF00F91A' }, bold: true }; // 绿色字体
                    } else {
                        cell.style.font = { color: { argb: 'FF000000' }, bold: true }; // 黑色字体
                    }
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
        Sum_data1.value || 0,
        Sum_data2.value || 0,
        Sum_diff.value || 0,

    ];

    totalRowData.forEach((value, colIndex) => {
        const cell = totalRow.getCell(colIndex + 1);
        cell.value = value;
        cell.style = {
            font: { bold: true }, // 红色粗体
            fill: {
                type: 'pattern',
                pattern: 'solid',
                fgColor: { argb: 'FFB0B0B0' } // 浅灰背景
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

        if (colIndex === 6) {
            if (value < 0) { // 值小于0设定为绿色
                cell.style.font = { color: { argb: 'FFFF2700' } }; // 黑色字体
            } else if (value > 0) { // 值大于0设定为红色
                cell.style.font = { color: { argb: 'FF00F91A' } }; // 绿色字体
            }
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

        // const fileName = `周期业务数据明细_${formatDate(Range1.value).replace(/\//g, '-')}_${formatDate(Range2.value).replace(/\//g, '-')}.xlsx`;
        const fileName = `${selectedIndicator.value.name}周期比对明细_周期1:${startDate1}至${endDate1}_周期2:${startDate2}至${endDate2}.xlsx`
        link.download = fileName;
        link.click();
        URL.revokeObjectURL(url);

        // 记录导出成功
        const message = `成功导出周期1：${startDate1}至${endDate1}与周期2：${startDate2}至${endDate2}的${selectedIndicator.value.name}数据比对明细。`;
        update_DataBoard_operationRecord('export_success');
        update_operation_message(message);
    } catch (error) {
        console.error('导出失败:', error);
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

        position: relative;

    }

    .main-periodCompareDataBoard-container .card-header {
        display: flex;
        flex-direction: row;
        align-items: center;

        width: 100%;
    }

    .main-periodCompareDataBoard-container .card-header .left {
        /* background-color: antiquewhite; */

        width: 40%;

        font-size: 1.3rem;
        font-weight: bold;
        letter-spacing: 0.08rem;

        padding: 0.4rem;
        padding-left: 0.7rem;
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
    }

    .main-periodCompareDataBoard-container .card-header .right .download-button {
        background-color: #202020;
        border: none;
    }

    .main-periodCompareDataBoard-container .options-panel {
        display: flex;
        flex-direction: column;
        align-items: center;
        justify-content: center;
    }

    .main-periodCompareDataBoard-container .data-panel {
        /* padding:1rem */
    }

    .main-periodCompareDataBoard-container .options-panel .info {
        color: #6a6b6e;
    }

    .main-periodCompareDataBoard-container .options-panel .options-panel-content {
        /* background-color: antiquewhite; */
        width: 100%;
        height: 100%;
    }

    .main-periodCompareDataBoard-container .options-panel .options-panel-content .listbox {
        height: 100%;
        border-left: none;
        border-right: none;
        /* display: flex;
        flex-direction: column;
        align-items: center;
        justify-content: center; */
    }


    /* 差值列的样式 */
    .red-text {
        color: #e74c3c;
        font-weight: bold;
    }

    .green-text {
        color: #27ae60;
        font-weight: bold;
    }



    .data-panel .p-treetable-header {
        display: flex;
        justify-content: center;
        align-items: center;
        font-size: 1.11rem;
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

    /* filter-template 样式 */
    .filter-template {
        display: flex;
        align-items: center;
        justify-content: space-between;
    }

    /* Popover 容器样式 - 移除最大宽度限制，允许动态适应内容 */
    .filter-popover {
        width: 20rem !important;
        min-width: 20rem !important;
        max-width: none !important;

        position: relative;

    }

    .popover-content {
        display: flex;
        flex-direction: column;
        gap: 1rem;
        width: 100%;
    }

    .multi-select {
        width: 100% !important;
    }

    /* 选中的 chips 区域 */
    .selected-card {
        background-color: #f8f9fa;
        border: 1px solid #e9ecef;
        width: 100%;
        /* 卡片宽度随popover自适应 */
    }

    .selected-items-container {
        display: flex;
        flex-direction: column;
        gap: 0.3rem;
        /* 人员和部门容器上下间隔0.3rem */
        position: relative;
        min-height: 80px;
        padding-bottom: 24px;
        /* 预留清空按钮空间 */
    }

    .selected-header {
        display: flex;
        align-items: center;
        gap: 0.5rem;
        margin-bottom: 0.3rem;
    }

    .selected-title {
        font-size: 0.875rem;
        color: #6c757d;
        font-weight: 500;
    }

    .selected-count {
        font-size: 0.75rem;
        color: #495057;
        background: #e9ecef;
        padding: 0.125rem 0.5rem;
        border-radius: 10px;
    }

    /* 外层包裹容器 - 确保内容不被截断 */
    .chips-wrapper {
        width: 100%;
        overflow: visible;
        /* 允许内容自然撑开宽度 */
    }

    /* 人员chip容器 - 每行最多4个，间隔固定0.3rem */
    .person-chips-container {
        display: inline-flex;
        /* 宽度由内容决定 */
        flex-wrap: wrap;
        gap: 0.3rem;
        /* 固定间隔，不受容器宽度影响 */
        min-width: 100%;
        /* 至少占满一行 */
        margin-bottom: 0.3rem;
    }

    /* 部门chip容器 - 相同布局逻辑 */
    .dept-chips-container {
        display: inline-flex;
        /* 宽度由内容决定 */
        flex-wrap: wrap;
        gap: 0.3rem;
        /* 固定间隔 */
        min-width: 100%;
        /* 至少占满一行 */
    }

    /* chip样式优化 - 确保内容和删除按钮完整显示 */
    .custom-chip {
        white-space: nowrap;
        /* 禁止文字换行 */
        font-size: 0.75rem;
        padding: 0.25rem 0.5rem;
        /* 足够内边距 */
        flex: 0 0 auto;
        /* 不拉伸，保持自身宽度 */
        max-width: none;
        /* 取消最大宽度限制，允许内容完整显示 */
    }

    /* 控制每行最多4个的逻辑（通过计算最大宽度） */
    .person-chips-container,
    .dept-chips-container {
        --chip-max-per-row: 4;
        --chip-gap: 0.3rem;
    }

    /* 当容器宽度足够时，每行最多4个；不足时自动换行但保持间距 */
    .person-chips-container>.custom-chip,
    .dept-chips-container>.custom-chip {
        max-width: calc((100% - var(--chip-gap) * (var(--chip-max-per-row) - 1)) / var(--chip-gap));
    }

    .person-chip {
        background-color: #e3f2fd;
        color: #1976d2;
        border: 1px solid #bbdefb;
    }

    .dept-chip {
        background-color: #e8f5e8;
        color: #2e7d32;
        border: 1px solid #c8e6c9;
        margin-bottom: 2rem;
    }

    /* 清空按钮位置 */
    .clear-all-btn {
        position: absolute;
        bottom: 0;
        right: 0;
        margin: 0.25rem;
    }

    /* 分割线样式 */
    .custom-divider {
        margin-top: 0.5rem;
        margin-bottom: 0.5rem;
    }

    .action-divider {
        margin: 0.5rem 0;
    }

    .action-buttons {
        display: flex;
        justify-content: flex-end;
        gap: 0.5rem;
    }


}
</style>
