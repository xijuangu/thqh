<template>
    <div class="card">
        <Toast />
        <Toast position="top-center" group="tc" />
        <ConfirmDialog></ConfirmDialog>

        <Dialog v-model:visible="popoverVisible" modal header="编辑" :style="{ width: '25rem' }">
            <div class="dialog-container">
                <div class="dialog-header">
                    <span class="dialog-title" style="margin-bottom: 7rem;">请修改或完善以下信息：</span>
                </div>

                <div class="dialog-content">
                    <div class="dialog-body">
                        <label class="body-label" for="classCode">级别代码</label>
                        <InputText class="body-input" id="classCode" v-model="selectedItem.class_code"
                            autocomplete="off" />
                    </div>
                    <div class="dialog-body">
                        <label class="body-label" for="className">级别名称</label>
                        <InputText class="body-input" id="className" v-model="selectedItem.class_name"
                            autocomplete="off" />
                    </div>
                    <div class="dialog-body">
                        <label class="body-label" for="subCode">子类代码</label>
                        <InputText class="body-input" id="subCode" v-model="selectedItem.sub_code" autocomplete="off" />
                    </div>
                    <div class="dialog-body">
                        <label class="body-label" for="subName">子类名称</label>
                        <InputText class="body-input" id="subName" v-model="selectedItem.sub_name" autocomplete="off" />
                    </div>
                    <div class="dialog-body">
                        <label class="body-label" for="remark">备注</label>
                        <Textarea class="body-input" id="remark" v-model="selectedItem.remark" rows="5" cols="30" />
                    </div>
                    <div class="dialog-body">
                        <label class="body-label" for="personnel">所属业务人员</label>
                        <Select v-model="selectedItem.personnel_code" :options="personnelList"
                            optionLabel="personnel_name" optionValue="personnel_code" placeholder="选择一位业务人员"
                            class="w-full md:w-56" />
                    </div>
                </div>

                <div class="dialog-footer">
                    <Button class="dialog-button" type="button" label="取消" severity="secondary"
                        @click="popoverVisible = false"></Button>
                    <Button class="dialog-button" type="button" label="保存" @click="saveItem"></Button>
                </div>
            </div>
        </Dialog>

        <Dialog v-model:visible="popoverVisible_add" modal header="新增" :style="{ width: '25rem' }">
            <div class="dialog-container">
                <div class="dialog-header">
                    <span class="dialog-title" style="margin-bottom: 7rem;">请完善以下信息：</span>
                </div>
                <div class="dialog-content">
                    <div class="dialog-body">
                        <label class="body-label" for="classCode">级别代码</label>
                        <InputText class="body-input" id="classCode" v-model="newItem.class_code" autocomplete="off" />
                    </div>
                    <div class="dialog-body">
                        <label class="body-label" for="className">级别名称</label>
                        <InputText class="body-input" id="className" v-model="newItem.class_name" autocomplete="off" />
                    </div>
                    <div class="dialog-body">
                        <label class="body-label" for="subCode">子类代码</label>
                        <InputText class="body-input" id="subCode" v-model="newItem.sub_code" autocomplete="off" />
                    </div>
                    <div class="dialog-body">
                        <label class="body-label" for="subName">子类名称</label>
                        <InputText class="body-input" id="subName" v-model="newItem.sub_name" autocomplete="off" />
                    </div>
                    <div class="dialog-body">
                        <label class="body-label" for="remark">备注</label>
                        <Textarea class="body-input" id="remark" v-model="newItem.remark" rows="5" cols="30" />
                    </div>
                    <div class="dialog-body">
                        <label class="body-label" for="personnel">所属业务人员</label>
                        <Select v-model="newItem.personnel_code" :options="personnelList" optionLabel="personnel_name"
                            optionValue="personnel_code" placeholder="选择一位业务人员" class="w-full md:w-56" />
                    </div>
                </div>

                <div class="dialog-footer">
                    <Button class="dialog-button" type="button" label="取消" severity="secondary"
                        @click="popoverVisible_add = false"></Button>
                    <Button class="dialog-button" type="button" label="添加" @click="callAddItem"></Button>
                </div>
            </div>
        </Dialog>

        <DataTable v-model:filters="filters" :value="clientClassInfoList" paginator :rows="15" dataKey="id"
            filterDisplay="row" :loading="loading"
            :globalFilterFields="['class_code', 'class_name', 'sub_code', 'sub_name', 'personnel_code']">

            <template #header>
                <div style="display: flex; justify-content: flex-end;">
                    <Button class="dialog-button" type="button" label="新增客户类信息" @click="addItem"
                        style="background-color: #202020; border: none;"></Button>
                </div>

            </template>

            <template #empty> 未找到客户分类信息 </template>
            <template #loading> 正在加载客户分类数据，请稍候... </template>

            <!-- 级别代码列 -->
            <Column header="级别代码" filterField="class_code" style="min-width: 10rem" :showFilterMenu="false">
                <template #body="{ data }">
                    <span class="font-bold">{{ data.class_code }}</span>
                </template>
                <template #filter>
                    <MultiSelect v-model="selected_class_codeOptions" :options="class_codeOptions" optionLabel="label"
                        placeholder="选择级别代码" style="min-width: 10rem" :maxSelectedLabels="1"
                        @change="selected_class_codeOptions_change">
                        <template #option="slotProps">
                            <span>{{ slotProps.option.label }}</span>
                        </template>
                    </MultiSelect>
                </template>
            </Column>

            <!-- 级别名称列 -->
            <Column header="级别名称" filterField="class_name" style="min-width: 10rem" :showFilterMenu="false">
                <template #body="{ data }">
                    <span class="font-bold">{{ data.class_name }}</span>
                </template>
                <template #filter>
                    <MultiSelect v-model="selected_class_nameOptions" :options="class_nameOptions" optionLabel="label"
                        placeholder="选择级别名称" style="min-width: 10rem" :maxSelectedLabels="1"
                        @change="selected_class_nameOptions_change">
                        <template #option="slotProps">
                            <span>{{ slotProps.option.label }}</span>
                        </template>
                    </MultiSelect>
                </template>
            </Column>

            <!-- 子类代码列 -->
            <Column field="sub_code" header="子类代码" style="min-width: 10rem" :showFilterMenu="false">
                <template #body="{ data }">
                    <span class="font-bold">{{ data.sub_code }}</span>
                </template>
                <template #filter>
                    <InputText v-model="input_subCode" type="text" @input="input_subCode_change" placeholder="搜索子类代码" />
                </template>
            </Column>

            <!-- 子类名称列 -->
            <Column field="sub_name" header="子类名称" style="min-width: 12rem" :showFilterMenu="false">
                <template #body="{ data }">
                    <span class="font-bold">{{ data.sub_name }}</span>
                </template>
                <template #filter>
                    <InputText v-model="input_subName" type="text" @input="input_subName_change" placeholder="搜索子类名称" />
                </template>
            </Column>

            <!-- 备注列 -->
            <Column field="remark" header="备注" style="min-width: 15rem" :showFilterMenu="false">
                <template #body="{ data }">
                    <span class="font-bold">{{ data.remark }}</span>
                </template>
            </Column>

            <!-- 所属业务人员列 -->
            <Column field="personnel_name" header="所属业务人员" style="min-width: 12rem" :showFilterMenu="false">
                <template #body="{ data }">
                    <span class="font-bold">{{ data.personnel_name || '无' }}</span>
                </template>
                <template #filter>
                    <MultiSelect v-model="selected_personnelOptions" :options="personnelOptions" optionLabel="label"
                        placeholder="选择业务人员" style="min-width: 10rem" :maxSelectedLabels="1"
                        @change="selected_personnelOptions_change">
                        <template #option="slotProps">
                            <span>{{ slotProps.option.label }}</span>
                        </template>
                    </MultiSelect>
                </template>
            </Column>

            <!-- 状态列 -->
            <Column field="status" header="状态" :showFilterMenu="false" style="min-width: 10rem">
                <template #body="{ data }">
                    <Tag :value="getStatusText(data.personnel_code)"
                        :severity="getStatusSeverity(data.personnel_code)" />
                </template>
            </Column>

            <!-- 操作列 -->
            <Column header="操作" style="min-width: 8rem">
                <template #body="{ data }">
                    <div class="flex gap-1">
                        <Button icon="pi pi-pen-to-square" class="p-button-rounded p-button-text"
                            @click="editItem(data)" />
                        <Button icon="pi pi-trash" class="p-button-rounded p-button-text p-button-danger"
                            @click="deleteItem(data)" />
                    </div>
                </template>
            </Column>
        </DataTable>
    </div>
</template>

<script setup>
import { ref, onMounted } from 'vue';
import { FilterMatchMode } from '@primevue/core/api';
import axiosInstance from '../axiosConfig.js';
import DataTable from 'primevue/datatable';
import Column from 'primevue/column';
import InputText from 'primevue/inputtext';
import MultiSelect from 'primevue/multiselect';
import Select from 'primevue/select';
import Tag from 'primevue/tag';
import Button from 'primevue/button';
import Textarea from 'primevue/textarea';


import Toast from 'primevue/toast';
import ConfirmDialog from 'primevue/confirmdialog';

import Dialog from 'primevue/dialog';

import { useConfirm } from "primevue/useconfirm";
import { useToast } from "primevue/usetoast";

const confirm = useConfirm();
const toast = useToast();




const loading = ref(true);

// 筛选条件
const filters = ref({
    global: { value: null, matchMode: FilterMatchMode.CONTAINS },
    class_code: { value: null, matchMode: FilterMatchMode.IN },
    class_name: { value: null, matchMode: FilterMatchMode.IN },
    sub_code: { value: null, matchMode: FilterMatchMode.CONTAINS },
    sub_name: { value: null, matchMode: FilterMatchMode.CONTAINS },
    personnel_code: { value: null, matchMode: FilterMatchMode.CONTAINS },
    status: { value: null, matchMode: FilterMatchMode.EQUALS }
});

// 获取状态文本
const getStatusText = (personnelCode) => {
    return personnelCode ? '已关联业务人员' : '无关联业务人员';
};

// 获取状态严重程度
const getStatusSeverity = (personnelCode) => {
    if (personnelCode === null || personnelCode === "unchecked") {
        return "warn";
    } else {
        return "info"
    }
};

// 编辑项目
const popoverVisible = ref(false);
const selectedItem = ref(null);
const editItem = (item) => {
    selectedItem.value = item;
    popoverVisible.value = true;
};

// 新增项目
const popoverVisible_add = ref(false);
const newItem = ref({
    id: 0,
    class_code: null,
    class_name: "",
    sub_code: "",
    sub_name: "",
    personnel_code: "",
    personnel_name: "",
    remark: ""
});
const addItem = () => {
    popoverVisible_add.value = true;
};

const saveItem = async () => {
    try {
        const response = await axiosInstance.post('/clientClassMaintenance/updateClientClassInfo', selectedItem.value);
        if (response.data.code === 0) {
            // 保存成功后重新加载数据
            await loadData();
            toast.add({ severity: 'success', summary: '成功', detail: response.data.message, life: 3000 });
            popoverVisible.value = false;
        } else {
            console.error('保存失败:', response.data.message);
            toast.add({ severity: 'error', summary: '失败', detail: response.data.message, life: 3000 });
        }
    } catch (error) {
        console.error('请求失败:', error);
        toast.add({ severity: 'error', summary: '失败', detail: error.data.message, life: 3000 });
    }
};

const callAddItem = async () => {
    if (newItem.value.class_code === null || newItem.value.class_code === ""){
        toast.add({ severity: 'error', summary: '失败', detail: '请填写级别代码', life: 3000,  group: 'tc' });
        return;
    }else if (newItem.value.class_name === ""){
        toast.add({ severity: 'error', summary: '失败', detail: '请填写级别名称', life: 3000,  group: 'tc' });
        return;
    }else if (newItem.value.sub_code === ""){
        toast.add({ severity: 'error', summary: '失败', detail: '请填写子类代码', life: 3000,  group: 'tc' });
        return;
    }else if (newItem.value.sub_name === ""){
        toast.add({ severity: 'error', summary: '失败', detail: '请填写子类名称', life: 3000,  group: 'tc' });
        return;
    }else if (newItem.value.personnel_code === ""){
        toast.add({ severity: 'error', summary: '失败', detail: '请选择业务人员', life: 3000,  group: 'tc' });
        return;
    }

    
    try {
        const response = await axiosInstance.post('/clientClassMaintenance/insertClientClassInfo', newItem.value);
        if (response.data.code === 0) {
            // 保存成功后重新加载数据
            await loadData();
            toast.add({ severity: 'success', summary: '成功', detail: response.data.message, life: 3000 });

        } else {
            console.error('保存失败:', response.data.message);
            toast.add({ severity: 'error', summary: '失败', detail: response.data.message, life: 3000 });
        }
        popoverVisible_add.value = false;
        newItem.value = {
            id: 0,
            class_code: null,
            class_name: "",
            sub_code: "",
            sub_name: "",
            personnel_code: "",
            personnel_name: "",
            remark: ""
        };
    } catch (error) {
        console.error('请求失败:', error);
        toast.add({ severity: 'error', summary: '失败', detail: error.message, life: 3000 });
        popoverVisible_add.value = false;
    }
};

// 删除项目
const deleteItem = async (item) => {
    // 调用 PrimeVue 的确认框
    confirm.require({
        message: `确定要删除子类名称为 "${item.sub_name}" 该条记录吗？`,
        header: '确认删除',
        icon: 'pi pi-exclamation-triangle',
        rejectProps: {
            label: '取消',
            severity: 'secondary',
            outlined: true
        },
        acceptProps: {
            label: '确定',
        },
        // 用户点击“确定”时执行删除逻辑
        accept: async () => {
            try {
                await axiosInstance.post('/clientClassMaintenance/deleteClientClassInfo', item);
                // 删除成功后重新加载数据
                await loadData();
                toast.add({ severity: 'success', summary: '成功', detail: '删除成功', life: 3000 });
            } catch (error) {
                console.error('删除失败:', error);
                toast.add({ severity: 'error', summary: '失败', detail: '删除失败，请重试', life: 3000 });
            }
        },
        // 用户点击“取消”时的提示
        reject: () => {
            console.log('取消删除');
        }
    });
};

// 加载数据
const personnelList = ref([{
    "id": 0,
    "personnel_code": "none",
    "personnel_name": "无",
    "department_code": ""
}]);

const clientClassInfoList = ref([]);
const old_clientClassInfoList = ref([...clientClassInfoList.value]);

const loadData = async () => {
    try {
        loading.value = true;
        const response = await axiosInstance.get('/clientClassMaintenance/getAllClientClassInfo');
        if (response.data.code === 0) {
            clientClassInfoList.value = response.data.data.clientClassInfoList;
            old_clientClassInfoList.value = clientClassInfoList.value
            personnelList.value = [{
                "id": 0,
                "personnel_code": "none",
                "personnel_name": "无",
                "department_code": ""
            }];
            response.data.data.personnelList.forEach(item => {
                personnelList.value.push(item);
            });
            getfilterOptions(clientClassInfoList.value);
        } else {
            toast.add({ severity: 'error', summary: '失败', detail: response.data.message, life: 3000 });
        }
    } catch (error) {
        toast.add({ severity: 'error', summary: '失败', detail: error.message, life: 3000 });
    } finally {
        loading.value = false;
    }
};

// ------------------------------------------------------------------ 自定义筛选
const class_codeOptions = ref([{ label: "none", value: "none" }]);
const selected_class_codeOptions = ref([]);

const class_nameOptions = ref([{ label: "none", value: "none" }]);
const selected_class_nameOptions = ref([]);

const personnelOptions = ref([{ label: "none", value: "none" }]);
const selected_personnelOptions = ref([]);



// 基于clientClassInfoList获取筛选选项
const getfilterOptions = (item) => {
    let class_codeOptions_set = new Set();
    let class_nameOptions_set = new Set();
    let personnelOptions_set = new Set();
    selected_class_codeOptions.value = [];
    selected_class_nameOptions.value = [];
    selected_personnelOptions.value = [];
    item.forEach(clientClassInfo => {
        class_codeOptions_set.add(clientClassInfo.class_code);
        class_nameOptions_set.add(clientClassInfo.class_name);
        personnelOptions_set.add(clientClassInfo.personnel_name);
    });
    class_codeOptions.value = Array.from(class_codeOptions_set).map(option => {
        return { label: option, value: option };
    });
    class_nameOptions.value = Array.from(class_nameOptions_set).map(option => {
        return { label: option, value: option };
    });
    personnelOptions.value = Array.from(personnelOptions_set).map(option => {
        return { label: option === null ? "无" : option, value: option };
    });
};


const selected_class_codeOptions_change = () => {
    filterGlobal();
}

const selected_class_nameOptions_change = () => {
    filterGlobal();
}

const selected_personnelOptions_change = () => {
    filterGlobal();
}

// const selected_statusOptions_change = () => {
//     console.log("选中的status值：", selected_statusOptions.value)
//     filterGlobal();
// }

const input_subCode = ref("");
const input_subCode_change = () => {
    filterGlobal();
}

const input_subName = ref("");
const input_subName_change = () => {
    filterGlobal();
}

// 统一筛选器
const filterGlobal = () => {
    const selected_class_codeOptions_array = selected_class_codeOptions.value.map(item => item.value)
    const selected_class_nameOptions_array = selected_class_nameOptions.value.map(item => item.value)
    const selected_personnelOptions_array = selected_personnelOptions.value.map(item => item.value)
    const input_subCode_value = input_subCode.value
    const input_subName_value = input_subName.value
    // const selected_statusOptions_value = selected_statusOptions.value

    console.log("------------------------------------------------------------------------------")
    console.log("选中的class_code数组：", selected_class_codeOptions_array)
    console.log("选中的class_name数组：", selected_class_nameOptions_array)
    console.log("选中的personnel数组：", selected_personnelOptions_array)
    console.log("输入的sub_code值：", input_subCode_value)
    console.log("输入的sub_name值：", input_subName_value)
    // console.log("选中的status值：", selected_statusOptions_value)

    clientClassInfoList.value = old_clientClassInfoList.value.filter(client_class_info => {
        // 构建筛选条件数组
        const conditions = [
            // class_code 筛选：如果数组为空则跳过
            selected_class_codeOptions_array.length === 0 || 
            selected_class_codeOptions_array.includes(client_class_info.class_code),
            
            // class_name 筛选：如果数组为空则跳过
            selected_class_nameOptions_array.length === 0 || 
            selected_class_nameOptions_array.includes(client_class_info.class_name),
            
            // personnel 筛选：如果数组为空则跳过
            selected_personnelOptions_array.length === 0 || 
            selected_personnelOptions_array.includes(client_class_info.personnel_name),
            
            // sub_code 筛选：如果为空字符串则跳过
            !input_subCode_value || 
            client_class_info.sub_code.includes(input_subCode_value),
            
            // sub_name 筛选：如果为空字符串则跳过
            !input_subName_value || 
            client_class_info.sub_name.includes(input_subName_value),
            
            // status 筛选：如果为空则跳过
            // !selected_statusOptions_value || 
            // client_class_info.status === selected_statusOptions_value
        ]

        // 所有条件都满足才返回 true
        return conditions.every(condition => condition)
    })
}






onMounted(() => {
    loadData();
});
</script>

<style scoped>
@layer reset, primevue, custom;

@layer custom {
    :deep(.p-column-header-content) {
        font-weight: bold;
    }

    .dialog-container {
        display: flex;
        flex-direction: column;
    }

    .dialog-header {
        margin-bottom: 2rem;
    }

    .dialog-content {
        display: flex;
        flex-direction: column;
        gap: 1rem;
        margin-bottom: 2rem;
    }

    .dialog-body {
        display: flex;
        flex-direction: row;
        gap: 0.5rem;

    }

    .dialog-body label {
        font-weight: 800;
    }

    .dialog-footer {
        display: flex;
        flex-direction: row;
        justify-content: flex-end;
        gap: 1rem;
    }
}
</style>