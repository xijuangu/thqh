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
                        <label class="body-label" for="classCode">业务人员</label>
                        <InputText class="body-input" id="classCode" v-model="selectedItem.personnel_name"
                            autocomplete="off" />
                    </div>
                    <div class="dialog-body">
                        <label class="body-label" for="className">业务人员代码</label>
                        <InputText class="body-input" id="className" v-model="selectedItem.personnel_code"
                            autocomplete="off" />
                    </div>
                    <div class="dialog-body">
                        <label class="body-label" for="subCode">所属部门</label>
                        <Select v-model="selectedItem.department_code" :options="dpetList" optionLabel="department_name"
                            optionValue="department_code" placeholder="选择一个部门" class="w-full md:w-56" />
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
                        <label class="body-label" for="classCode">业务人员</label>
                        <InputText class="body-input" id="classCode" v-model="newItem.personnel_name"
                            autocomplete="off" />
                    </div>
                    <div class="dialog-body">
                        <label class="body-label" for="className">业务人员代码</label>
                        <InputText class="body-input" id="className" v-model="newItem.personnel_code"
                            autocomplete="off" />
                    </div>
                    <div class="dialog-body">
                        <label class="body-label" for="subCode">所属部门</label>
                        <Select v-model="newItem.department_code" :options="dpetList" optionLabel="department_name"
                            optionValue="department_code" placeholder="选择一个部门" class="w-full md:w-56" />
                    </div>
                </div>

                <div class="dialog-footer">
                    <Button class="dialog-button" type="button" label="取消" severity="secondary"
                        @click="popoverVisible_add = false"></Button>
                    <Button class="dialog-button" type="button" label="添加" @click="callAddItem"></Button>
                </div>
            </div>
        </Dialog>

        <DataTable v-model:filters="filters" :value="personnelInfoList" paginator :rows="15" dataKey="id"
            :loading="loading">

            <template #header>
                <div style="display: flex; justify-content: flex-end;">
                    <Button class="dialog-button" type="button" label="新增业务人员信息" @click="addItem"
                        style="background-color: #202020; border: none;"></Button>
                </div>

            </template>

            <template #empty> 未找到业务人员信息 </template>
            <template #loading> 正在加载业务人员数据，请稍候... </template>

            <!-- 序号列 -->
            <Column header="序号" field="id" style="min-width: 10rem" :showFilterMenu="false">
                <template #body="{ data }">
                    <span class="font-bold">{{ data.id }}</span>
                </template>
            </Column>

            <!-- 业务人员列 -->
            <Column header="业务人员" field="personnel_name" style="min-width: 10rem" :showFilterMenu="false">
                <template #body="{ data }">
                    <span class="font-bold">{{ data.personnel_name }}</span>
                </template>
            </Column>

            <!-- 业务人员代码列 -->
            <Column header="业务人员代码" field="personnel_code" style="min-width: 10rem" :showFilterMenu="false">
                <template #body="{ data }">
                    <span class="font-bold">{{ data.personnel_code }}</span>
                </template>
            </Column>

            <!-- 所属部门列 -->
            <Column header="所属部门" filterField="department_name" style="min-width: 10rem" :showFilterMenu="false">
                <template #body="{ data }">
                    <span class="font-bold">{{ data.department_name }}</span>
                </template>
            </Column>

            <!-- 所属部门代码列 -->
            <Column header="所属部门代码" filterField="department_code" style="min-width: 10rem" :showFilterMenu="false">
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
import Select from 'primevue/select';

import Toast from 'primevue/toast';
import ConfirmDialog from 'primevue/confirmdialog';

import Dialog from 'primevue/dialog';

import { useConfirm } from "primevue/useconfirm";
import { useToast } from "primevue/usetoast";

const confirm = useConfirm();
const toast = useToast();



const personnelInfoList = ref([]);
const dpetList = ref([{ id: 0, department_name: '无', department_code: "none" }]);

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
    personnel_name: '',
    personnel_code: '',
    department_code: ''
});
const addItem = () => {
    popoverVisible_add.value = true;
    resetNewItem();
};

const saveItem = async () => {
    if (selectedItem.value.personnel_name === '') {
        toast.add({ severity: 'warn', summary: '提示', detail: '业务人员名称不能为空', life: 3000, group: 'tc' });

        return;
    }
    if (selectedItem.value.personnel_code === '') {
        toast.add({ severity: 'warn', summary: '提示', detail: '业务人员代码不能为空', life: 3000, group: 'tc' });
        return;
    }
    try {
        const response = await axiosInstance.post('/clientClassMaintenance/updateBusinessPersonInfo', selectedItem.value);
        if (response.data.code === 0) {
            // 保存成功后重新加载数据
            await loadData();
            toast.add({ severity: 'success', summary: '成功', detail: response.data.message, life: 3000 });
            popoverVisible_edit.value = false;
        } else if (response.data.code === 2) {
            console.error('保存失败:', response.data.message);
            toast.add({ severity: 'warn', summary: '提示', detail: response.data.message, life: 3000, group: 'tc' });

        }
        else {
            console.error('保存失败:', response.data.message);
            toast.add({ severity: 'error', summary: '失败', detail: response.data.message, life: 3000 });

        }

    } catch (error) {
        console.error('请求失败:', error);
        toast.add({ severity: 'error', summary: '失败', detail: error.message, life: 3000 });
        popoverVisible_edit.value = false;
    }
};

const callAddItem = async () => {
    if (newItem.value.personnel_name === '') {
        toast.add({ severity: 'warn', summary: '提示', detail: '业务人员名称不能为空', life: 3000, group: 'tc' });
        return;
    }
    if (newItem.value.personnel_code === '') {
        toast.add({ severity: 'warn', summary: '提示', detail: '业务人员代码不能为空', life: 3000, group: 'tc' });
        return;
    }
    try {
        const response = await axiosInstance.post('/clientClassMaintenance/insertBusinessPersonInfo', newItem.value);
        if (response.data.code === 0) {
            // 保存成功后重新加载数据
            await loadData();
            toast.add({ severity: 'success', summary: '成功', detail: response.data.message, life: 3000 });
            popoverVisible_add.value = false;
            resetNewItem();

        } else if (response.data.code === 2) {
            toast.add({ severity: 'warn', summary: '提示', detail: response.data.message, life: 3000, group: 'tc' });

        }
        else {

            toast.add({ severity: 'error', summary: '失败', detail: response.data.message, life: 3000 });
        }


    } catch (error) {
        console.error('请求失败:', error);
        toast.add({ severity: 'error', summary: '失败', detail: error.message, life: 3000 });
        popoverVisible_add.value = false;
    }
};

// newItem置空
const resetNewItem = () => {
    newItem.value = {
        personnel_name: '',
        personnel_code: '',
        department_code: ''
    };

};

// 删除项目
const deleteItem = async (item) => {
    // 调用 PrimeVue 的确认框
    confirm.require({
        message: `确定要删除业务人员 "${item.personnel_name}" 该条记录吗？`,
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
                const response = await axiosInstance.post('/clientClassMaintenance/deleteBusinessPersonInfo', item);
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
        const response = await axiosInstance.post('/clientClassMaintenance/getBusinessPersonInfo');
        if (response.data.code === 0) {
            personnelInfoList.value = response.data.data.personnelInfoList;
            dpetList.value = [{ id: 0, department_name: '无', department_code: "none" }];
            response.data.data.departmentList.forEach(item => {
                dpetList.value.push(item);
            });

        } else {
            console.error('获取数据失败:', response.data.message);
        }
    } catch (error) {
        console.error('请求失败:', error);
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