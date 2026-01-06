<template>
    <div class="PersonnelManagement-card">
        <Toast />
        <Toast position="top-center" group="tc" />
        <ConfirmDialog></ConfirmDialog>

        <Dialog v-model:visible="popoverVisible_edit" modal header="编辑" :style="{ width: '25rem' }">
            <div class="dialog-container">
                <div class="dialog-header">
                    <span class="dialog-title" style="margin-bottom: 7rem;">请修改或完善以下信息：</span>
                </div>
                <div class="dialog-content">
                    <div class="dialog-body">
                        <label class="body-label" for="classCode">部门名称</label>
                        <InputText class="body-input" id="classCode" v-model="selectedItem.department_name"
                            autocomplete="off" />
                    </div>
                    <div class="dialog-body">
                        <label class="body-label" for="className">部门代码</label>
                        <InputText class="body-input" id="className" v-model="selectedItem.department_code"
                            autocomplete="off" />
                    </div>
                </div>

                <div class="dialog-footer">
                    <Button class="dialog-button" type="button" label="取消" severity="secondary"
                        @click="popoverVisible_edit = false"></Button>
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
                        <label class="body-label" for="classCode">部门名称</label>
                        <InputText class="body-input" id="classCode" v-model="newItem.department_name"
                            autocomplete="off" />
                    </div>
                    <div class="dialog-body">
                        <label class="body-label" for="className">部门代码</label>
                        <InputText class="body-input" id="className" v-model="newItem.department_code"
                            autocomplete="off" />
                    </div>
                </div>

                <div class="dialog-footer">
                    <Button class="dialog-button" type="button" label="取消" severity="secondary"
                        @click="popoverVisible_add = false"></Button>
                    <Button class="dialog-button" type="button" label="添加" @click="callAddItem"></Button>
                </div>
            </div>
        </Dialog>

        <DataTable v-model:filters="filters" :value="departmentInfoList" paginator :rows="15" dataKey="id"
            :loading="loading">

            <template #header>
                <div style="display: flex; justify-content: flex-end;">
                    <Button class="dialog-button" type="button" label="新增部门信息" @click="addItem"
                        style="background-color: #202020; border: none;"></Button>
                </div>

            </template>

            <template #empty> 未找到部门信息 </template>
            <template #loading> 正在加载部门数据，请稍候... </template>

            <!-- 序号列 -->
            <Column header="序号" field="id" style="min-width: 10rem" :showFilterMenu="false">
                <template #body="{ data }">
                    <span class="font-bold">{{ data.id }}</span>
                </template>
            </Column>

            <!-- 所属部门列 -->
            <Column header="部门" filterField="department_name" style="min-width: 10rem" :showFilterMenu="false">
                <template #body="{ data }">
                    <span class="font-bold">{{ data.department_name }}</span>
                </template>
            </Column>

            <!-- 所属部门代码列 -->
            <Column header="部门代码" filterField="department_code" style="min-width: 10rem" :showFilterMenu="false">
                <template #body="{ data }">
                    <span class="font-bold">{{ data.department_code }}</span>
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
import Button from 'primevue/button';

import Toast from 'primevue/toast';
import ConfirmDialog from 'primevue/confirmdialog';

import Dialog from 'primevue/dialog';

import { useConfirm } from "primevue/useconfirm";
import { useToast } from "primevue/usetoast";

const confirm = useConfirm();
const toast = useToast();



const departmentInfoList = ref([]);


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



// 编辑项目
const popoverVisible_edit = ref(false);
const selectedItem = ref(null);
const editItem = (item) => {
    selectedItem.value = { ...item };
    popoverVisible_edit.value = true;
};
// 新增项目
const popoverVisible_add = ref(false);
const newItem = ref({
    department_name: '',
    department_code: ''
});
const addItem = () => {
    popoverVisible_add.value = true;
    resetNewItem();
};

const saveItem = async () => {
    if (selectedItem.value.department_name === '' || selectedItem.value.department_code === '') {
        if (selectedItem.value.department_name === '') {
            toast.add({ severity: 'warn', summary: '提醒', detail: '部门名称为空', group: 'tc', life: 3000 });
            return;
        }
        if (selectedItem.value.department_code === '') {
            toast.add({ severity: 'warn', summary: '提醒', detail: '部门代码为空', group: 'tc', life: 3000 });
            return;
        }

    }
    try {
        const response = await axiosInstance.post('/clientClassMaintenance/updateDeptInfo', selectedItem.value);
        if (response.data.code === 0) {
            // 保存成功后重新加载数据
            await loadData();
            toast.add({ severity: 'success', summary: '成功', detail: response.data.message, life: 3000 });
            popoverVisible_edit.value = false;

        } else if (response.data.code === 2) {
            toast.add({ severity: 'warn', summary: '提醒', detail: response.data.message, group: 'tc', life: 3000 });
        }
        else {

            toast.add({ severity: 'error', summary: '失败', detail: response.data.message, life: 3000 });

        }

    } catch (error) {

        toast.add({ severity: 'error', summary: '失败', detail: error.message, life: 3000 });

    }
};

const callAddItem = async () => {
    if (newItem.value.department_name === '' || newItem.value.department_code === '') {
        if (newItem.value.department_name === '') {
            toast.add({ severity: 'warn', summary: '提醒', detail: '请输入部门名称', group: 'tc', life: 3000 });
            return;
        }
        if (newItem.value.department_code === '') {
            toast.add({ severity: 'warn', summary: '提醒', detail: '请输入部门代码', group: 'tc', life: 3000 });
            return;
        }

    }
    try {
        const response = await axiosInstance.post('/clientClassMaintenance/insertDeptInfo', newItem.value);
        if (response.data.code === 0) {
            // 保存成功后重新加载数据
            await loadData();
            toast.add({ severity: 'success', summary: '成功', detail: response.data.message, life: 3000 });

            popoverVisible_add.value = false;
            resetNewItem();
        } else if (response.data.code === 2) {
            toast.add({ severity: 'warn', summary: '提醒', detail: response.data.message, group: 'tc', life: 3000 });
        }
        else {
            toast.add({ severity: 'error', summary: '失败', detail: response.data.message, life: 3000 });
        }

    } catch (error) {

        toast.add({ severity: 'error', summary: '失败', detail: error.message, life: 3000 });

    }
};

// newItem置空
const resetNewItem = () => {
    newItem.value = {
        department_name: '',
        department_code: ''
    };

};

// 删除项目
const deleteItem = async (item) => {
    // 调用 PrimeVue 的确认框
    confirm.require({
        message: `确定部门 "${item.department_name}" ？`,
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
                const response = await axiosInstance.post('/clientClassMaintenance/deleteDeptInfo', item);
                if (response.data.code === 0) {
                    // 删除成功后重新加载数据
                    await loadData();
                    toast.add({ severity: 'success', summary: '成功', detail: '删除成功', life: 3000 });
                } else {
                    console.error('删除失败:', response.data.message);
                    toast.add({ severity: 'error', summary: '失败', detail: response.data.message, life: 3000 });
                }

            } catch (error) {
                console.error('删除失败:', error);
                toast.add({ severity: 'error', summary: '失败', detail: error.message, life: 3000 });
            }
        },
        // 用户点击“取消”时的提示
        reject: () => {
            console.log('取消删除');
        }
    });
};

// 加载数据
const loadData = async () => {
    try {
        loading.value = true;
        const response = await axiosInstance.post('/clientClassMaintenance/getDeptInfo');
        if (response.data.code === 0) {
            departmentInfoList.value = response.data.data;

        } else {
            toast.add({ severity: 'error', summary: '失败', detail: response.data.message, life: 3000 });
        }
    } catch (error) {
        toast.add({ severity: 'error', summary: '失败', detail: error.message, life: 3000 });
    } finally {
        loading.value = false;
    }
};

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