<template>
    <div class="usermanagement-container">
        <Toast />
        <Toast group="tc" position="top-center" />
        <ConfirmDialog></ConfirmDialog>

        <!-- 批量删除确认弹窗 -->
        <Dialog v-model:visible="batchDeleteDialog" header="批量删除确认" :modal="true" :style="{ width: '400px' }">
            <p class="text-danger">确定要删除选中的 {{ selectedRows.length }} 条用户数据吗？此操作不可恢复！</p>
            <template #footer>
                <Button label="取消" @click="batchDeleteDialog = false" class="mr-2" />
                <Button label="确认删除" severity="danger" @click="handleBatchDelete" />
            </template>
        </Dialog>



        <Dialog v-model:visible="pwdEditDialogVisible" modal header="修改密码" :style="{ width: '25rem' }">
            <div class="edit-container"
                style="display: flex; flex-direction: column; width: 100%; height: 100%; justify-content: center; align-items: center;;">
                <div
                    style="display: flex; flex-direction: column; width: 100%; height: 100%; justify-content: flex-start">
                    <span
                        style="font-weight: 600; color: #fc312e; letter-spacing: 0.01rem; margin-bottom: 0.4rem;">您正在修改用户
                        {{
                            selectedUser.userName }} 的密码，请输入新密码并确认操作：</span>
                </div>

                <!-- 提示信息 -->
                <div class="info-container" style="margin-bottom: 1rem; height: 20px;">
                    <div v-if="showText1" :class="['animate__animated', currentAnimation1]" ref="textRef">
                        <i class="pi pi-times-circle"
                            style="font-size: 0.8rem;margin-right: 0.3rem; color: #FF412C;"></i>
                        <span class="info-text"
                            style="font-size: 14px; font-weight: 600; color: #FF412C;">新密码不能为空</span>
                    </div>
                </div>

                <div>
                    <InputText v-model="newPassword" type="password" placeholder="新密码" />
                </div>
            </div>
            <template #footer>
                <Button label="取消" @click="pwdEditDialogVisible = false" class="mr-2" />
                <Button label="确认" severity="danger" @click="handleChangePwd()" />
            </template>

        </Dialog>


        <Dialog v-model:visible="userAddDialogVisible" modal header="新增用户" :style="{ width: '25rem' }">
            <div class="adduser-container"
                style="display: flex; flex-direction: column; width: 100%; height: 100%; justify-content: center; align-items: center;;">
                <div
                    style="display: flex; flex-direction: column; width: 100%; height: 100%; justify-content: flex-start">
                    <span
                        style="font-weight: 800; color: #fc312e; letter-spacing: 0.01rem; margin-bottom: 0.4rem;">提示：</span>
                    <span
                        style="font-weight: 600; color: #fc312e; letter-spacing: 0.01rem; margin-bottom: 0.4rem;">1、新增用户默认密码为
                        <span style="color: #851edf;">123456</span></span>
                    <span
                        style="font-weight: 600; color: #fc312e; letter-spacing: 0.01rem; margin-bottom: 0.4rem;">2、新增用户名不可与已有用户名重复</span>

                    <span
                        style="font-weight: 600; color: #fc312e; letter-spacing: 0.01rem; margin-bottom: 0.4rem;">3、新增用户账号默认状态为<span
                            style="color: #851edf;">禁用</span>，禁用账号无法登录系统，请创建后手动启用</span>

                    <span
                        style="font-weight: 600; color: #fc312e; letter-spacing: 0.01rem; margin-bottom: 0.4rem;">4、所属角色以及相关权限请至
                        <span style="color: #851edf;">角色权限管理面板</span> 进行配置或新增</span>


                </div>
                <!-- 提示信息 -->
                <div class="info-container" style="margin-bottom: 1rem; height: 20px;">
                    <div v-if="showText2" :class="['animate__animated', currentAnimation2]" ref="textRef">
                        <i class="pi pi-exclamation-circle"
                            style="font-size: 0.8rem;margin-right: 0.3rem; color: #FF412C;"></i>
                        <span class="info-text" style="font-size: 14px; font-weight: 600; color: #FF412C;">{{
                            infoText_addUser
                            }}</span>
                    </div>
                </div>
                <div style="margin-bottom: 1rem;">
                    <span style="margin-right: 1rem;">用户名</span>
                    <InputText v-model="newUserName" type="text" placeholder="用户名" />
                </div>
                <div style="margin-bottom: 1rem;">
                    <span style="margin-right: 1rem;">所属人员</span>
                    <InputText v-model="newUserRealName" type="text" placeholder="所属人员" />
                </div>
                <div style="margin-bottom: 1rem;">
                    <span style="margin-right: 1rem;">所属角色</span>
                    <MultiSelect v-model="newUserRoles" :options="allRoles" optionLabel="role_name" placeholder="选择角色"
                        style="min-width: 15rem" :maxSelectedLabels="2" :emptyFilterMessage="''" />
                </div>

            </div>

            <template #footer>
                <Button label="取消" @click="userAddDialogVisible = false" class="mr-2" />
                <Button label="确认" severity="danger" @click="handleAddUser" />
            </template>

        </Dialog>



        <DataTable class="datatable-contsianer" v-model:filters="filters" :value="userlist" paginator :rows="15"
            dataKey="id" filterDisplay="row" :loading="loading"
            :globalFilterFields="['userName', 'userRealName', 'roles', 'activate', 'lastLoginTime']"
            v-model:selection="selectedRows">
            <template #header>
                <div class="auxfunction">
                    <div class="btn-container">
                        <Button class="btn" label="添加用户" icon="pi pi-user-plus" @click="addUser()" />
                        <Button class="btn" label="批量删除" icon="pi pi-trash" :disabled="selectedRows.length === 0"
                            @click="batchDeleteDialog = true" />
                    </div>
                </div>
            </template>
            <template #empty> 未找到任何系统用户信息 </template>
            <template #loading> 加载系统用户信息中，请稍等... </template>

            <Column selectionMode="multiple" headerStyle="width: 3rem"></Column>

            <!-- 序号列 -->
            <Column field="id" header="序号" style="min-width: 6rem" filter :showFilterMenu="false">
                <template #body="{ data }">
                    {{ data.id }}
                </template>
            </Column>

            <!-- 用户名列（修复数据绑定错误） -->
            <Column field="sysuser_name" header="用户名" style="min-width: 12rem" :showFilterMenu="false">
                <template #body="{ data }">
                    <span>{{ data.sysuser_name }}</span>
                </template>
                <template #filter>
                    <InputText v-model="searchSysuser_name" placeholder="用户名" />
                </template>

            </Column>

            <!-- 真实姓名列（修复筛选框占位符） -->
            <Column field="sysuser_real_name" header="所属人员" style="min-width: 12rem" :showFilterMenu="false">
                <template #body="{ data }">
                    <span>{{ data.sysuser_real_name }}</span>
                </template>
                <template #filter>
                    <InputText v-model="searchSysuser_real_name" placeholder="所属人员" />
                </template>

            </Column>

            <!-- 所属角色列（修复筛选字段匹配+多选逻辑） -->
            <Column field="roles" header="所属角色（多角色通过逗号分隔）" style="min-width: 18rem" :showFilterMenu="false">
                <template #body="{ data }">
                    <span>{{ data.roles.join(', ') }}</span>
                </template>
                <template #filter>
                    <MultiSelect v-model="selectedRoles" :options="allRoles" optionLabel="role_name" placeholder="选择角色"
                        style="min-width: 18rem" :maxSelectedLabels="2" :emptyFilterMessage="''" />
                </template>
            </Column>

            <!-- 状态列（修复筛选选项绑定+匹配逻辑） -->
            <Column field="activate_status" header="状态（启用/禁用）" style="min-width: 10rem" :showFilterMenu="false">
                <template #body="{ data }">
                    <ToggleSwitch v-model="data.activate_status" @change="handleStatusChange(data)" />
                    <span class="ml-2">{{ data.activate_status ? '启用' : '禁用' }}</span>
                </template>
            </Column>

            <!-- 最近登录时间列（修复数据绑定+排序筛选） -->
            <Column field="last_login_Time" header="最近登录时间" style="min-width: 16rem" sortable :showFilterMenu="false">
                <template #body="{ data }">
                    <span>{{ formatDate(data.last_login_time) }}</span>
                </template>
            </Column>

            <!-- 操作列（修复重复删除按钮） -->
            <Column field="operations" header="操作" style="min-width: 18rem" :showFilterMenu="false">
                <template #body="{ data }">
                    <div class="rowOperations flex gap-2">
                        <Button class="btn delete" label="删除" icon="pi pi-trash" size="small"
                            @click="confirm_delete(data)" />
                        <Button class="btn changePwd " label="修改密码" icon="pi pi-key" size="small"
                            @click="callPwdEditDialog(data)" />
                    </div>
                </template>
            </Column>
        </DataTable>
    </div>
</template>

<script setup>
import { ref, onMounted, watch } from 'vue';
import { FilterMatchMode } from '@primevue/core/api';
import 'primeicons/primeicons.css';
// 补全缺失的组件导入
import Button from 'primevue/button';
import DataTable from 'primevue/datatable';
import Column from 'primevue/column';
import InputText from 'primevue/inputtext';
import MultiSelect from 'primevue/multiselect';
import ToggleSwitch from 'primevue/toggleswitch';
// import Select from 'primevue/select';
import { useConfirm } from "primevue/useconfirm";
import { useToast } from "primevue/usetoast";
import Dialog from 'primevue/dialog';

import axiosInstance from '../axiosConfig.js';

// 引入Animate.css（确保动画生效）
import 'animate.css';
import 'primeicons/primeicons.css'


// import { cloneDeep } from 'lodash';

const confirm = useConfirm();
const toast = useToast();

// ------------------------------------------------获取系统用户列表\角色列表接口请求
const userlist = ref([]); // 系统用户列表
const allRolesList_ori = ref([]); // 系统角色列表
const allRoles = ref([]); // 系统角色列表（数据格式化后）
const originalUserList = ref([...userlist.value]);
const loading = ref(false);
const getSystemUserList = () => {
    loading.value = true;
    axiosInstance.get('/sysUser/getsysuserlist',
        {
            timeout: 100000 // 设置超时时间为 5 秒（根据需求调整）
        }
    )
        .then(response => {
            // console.log('Data fetched successfully:', response.data);
            if (!response.data || !response.data.data) {
                console.log('unexpected response data format');
            }
            if (response.data.code == 0) {
                console.log('Data fetched successfully:', response.data.data);
                userlist.value = response.data.data.sysUserList || [];
                allRolesList_ori.value = response.data.data.allRoleList || [];
                allRoles.value = formatAllRolesList(allRolesList_ori.value);
                loading.value = false;
                originalUserList.value = userlist.value;
            }
        })
        .catch(error => {
            console.log('Error fetching data:', error);
            toast.add({ severity: 'error', summary: '错误', detail: '获取用户列表失败,网络错误，请重试', life: 3000 });
            loading.value = false;
            userlist.value = [];
        });
}

// 将角色列表数据格式化为适合渲染的格式
const formatAllRolesList = (roles) => {
    const formattedRoles = [];
    roles.forEach(role => {
        formattedRoles.push({ id: role.id, role_name: role.role_name, role_code: role.role_code });
    });
    return formattedRoles;
}









// -------------------------------------------------批量删除相关参数
const batchDeleteDialog = ref(false); // 批量删除确认弹窗
const selectedRows = ref([]);

// 批量删除处理函数
const handleBatchDelete = () => {
    // 1. 确认是否删除

    const deleteIdList = selectedRows.value.map(row => row.id);
    const delect_count = deleteIdList.length;
    axiosInstance.post('/sysUser/deletesysuserbatch', deleteIdList)
        .then(response => {
            if (response.data.code === 0) {

                // 刷新用户列表
                getSystemUserList();
                toast.add({ severity: 'info', summary: '确认删除', detail: '已删除选中的 ' + delect_count + '条用户账号', life: 3000 });
            } else if (response.data.code === 4) {
                toast.add({ severity: 'error', summary: '失败', detail: response.data.message, life: 3000 });
            } else {
                toast.add({ severity: 'error', summary: '失败', detail: response.data.message, life: 3000 });
            }
            batchDeleteDialog.value = false;
            selectedRows.value = [];
        })
        .catch(error => {
            console.log('Error fetching data:', error);
            toast.add({ severity: 'error', summary: '失败', detail: '网络错误，请重试', life: 3000 });
            batchDeleteDialog.value = false;
            selectedRows.value = [];
        });


};


// -------------------------------------------------单条用户删除弹窗
const confirm_delete = (sysUser) => {
    confirm.require({
        message: '确认删除用户 ' + sysUser.sysuser_name + '？',
        header: '确认提示',
        icon: 'pi pi-exclamation-triangle',
        rejectProps: {
            label: '取消',
            severity: 'secondary',
            outlined: true
        },
        acceptProps: {
            label: '确定'
        },
        accept: () => {
            axiosInstance.post('/sysUser/deletesysuser', { sysUserId: sysUser.id })
                .then(response => {
                    if (response.data.code === 0) {
                        toast.add({ severity: 'info', summary: '确认删除', detail: '已删除用户 ' + sysUser.sysuser_name, life: 3000 });
                        // 刷新用户列表
                        getSystemUserList();
                    } else {
                        toast.add({ severity: 'error', summary: '失败', detail: response.data.message, life: 3000 });
                    }
                })
                .catch(error => {
                    console.log('Error fetching data:', error);
                    toast.add({ severity: 'error', summary: '失败', detail: '网络错误，请重试', life: 3000 });
                });

        },
        reject: () => {
            console.log('取消删除');
        }
    });
};

// -------------------------------------------------修改密码弹窗
const pwdEditDialogVisible = ref(false);
const selectedUser = ref(null);
const newPassword = ref('');
const callPwdEditDialog = (user) => {
    pwdEditDialogVisible.value = true;
    selectedUser.value = user;

};
const handleChangePwd = () => {
    if (newPassword.value === '') {
        // toast.add({ severity: 'error', summary: '错误', detail: '密码不能为空', life: 3000 });
        showTip1();
        return;
    }
    // 调用接口（预留axios位置）
    // 2. 调用接口（预留axios位置）

    axiosInstance.post('/sysUser/changepassword', { sysUserId: selectedUser.value.id, sysUser_Password: newPassword.value })
        .then(response => {
            if (response.data.code === 0) {
                getSystemUserList();
                toast.add({ severity: 'success', summary: '成功', detail: '密码修改成功', life: 3000 });
            } else {
                toast.add({ severity: 'error', summary: '失败', detail: response.data.message, life: 3000 });
            }
            pwdEditDialogVisible.value = false;

        })
        .catch(error => {
            console.log('Error fetching data:', error);
            toast.add({ severity: 'error', summary: '失败', detail: '网络错误，请重试', life: 3000 });
        });

}

// 统一提示信息方法（优化代码复用）
const currentAnimation1 = ref('animate__fadeInDown');
const showText1 = ref(false);
const showTip1 = () => {
    // 重置动画状态
    currentAnimation1.value = 'animate__fadeInDown';
    showText1.value = true;

    // 2秒后自动隐藏（带淡出动画）
    setTimeout(() => {
        currentAnimation1.value = 'animate__fadeOutDown';
        setTimeout(() => {
            showText1.value = false;
        }, 1000); // 等待动画结束
    }, 1500);
};

// -------------------------------------------------新增用户弹窗
const userAddDialogVisible = ref(false);
const infoText_addUser = ref("")
// 统一提示信息方法（优化代码复用）
const currentAnimation2 = ref('animate__fadeInDown');
const showText2 = ref(false);

const addUser = () => {
    userAddDialogVisible.value = true;
    resetNewUserParams();
};

// 新增用户相关参数
const newUserName = ref('');
const newUserRealName = ref('');
const newUserRoles = ref([]);
const handleAddUser = () => {
    const sysuser_name = new Set();
    originalUserList.value.forEach(user => {
        sysuser_name.add(user.sysuser_name);
    });
    if (newUserName.value === '') {
        showTip2("用户名不能为空");
        return;
    } else if (newUserRealName.value === '') {
        showTip2("所属人员不能为空");
        return;
    } else if (newUserRoles.value.length === 0) {
        showTip2("所属角色不能为空");
        return;
    }
    if (sysuser_name.has(newUserName.value)) {
        showTip2("用户名已存在，请重新输入");
        return;
    }
    console.log('新增用户：', newUserRoles.value);
    axiosInstance.post('/sysUser/addsysuser', { sysUser_Name: newUserName.value, sysUser_realName: newUserRealName.value, sysUser_Roles: newUserRoles.value })
        .then(response => {
            if (response.data.code === 0) {
                getSystemUserList();
                resetNewUserParams();
                userAddDialogVisible.value = false;
                toast.add({ severity: 'success', summary: '成功', detail: '新增用户成功', life: 3000 });
            } else if (response.data.code === 2) {
                toast.add({ severity: 'error', summary: '失败', detail: response.data.message, life: 3000, group: 'tc' });
            } else if (response.data.code === 4) {
                toast.add({ severity: 'error', summary: '失败', detail: response.data.message, life: 3000 });
            } else {
                toast.add({ severity: 'error', summary: '失败', detail: '新增用户失败，请重试', life: 3000 });
            }
        })

        .catch(error => {
            console.log('Error fetching data:', error);
            toast.add({ severity: 'error', summary: '失败', detail: '网络错误，请重试', life: 3000 });
        });
};

// 重置新增角色参数
const resetNewUserParams = () => {
    newUserName.value = '';
    newUserRealName.value = '';
    newUserRoles.value = [];
};


const showTip2 = (text) => {
    // 重置动画状态
    currentAnimation2.value = 'animate__fadeInDown';
    showText2.value = true;
    infoText_addUser.value = text;


    // 2秒后自动隐藏（带淡出动画）
    setTimeout(() => {
        currentAnimation2.value = 'animate__fadeOutDown';
        setTimeout(() => {
            showText2.value = false;
        }, 1000); // 等待动画结束
    }, 1500);
};




// 所有角色选项（用于角色筛选）


// ---------------------------------------------------------角色类型筛选
// 角色筛选：绑定多选器的变量（数组类型）
const selectedRoles = ref([]);

// 过滤后的用户列表（表格渲染此数据）

const filteredUserList = ref([]);
// 核心：监听角色选择变化，执行自定义筛选逻辑
watch(selectedRoles, (newRoles) => {
    // 若未选择任何角色，显示所有数据
    console.log('角色筛选变化：', newRoles);
    if (newRoles.length === 0 || newRoles.length === allRoles.value.length) {


        filteredUserList.value = [...originalUserList.value];
        console.log('显示所有用户:', filteredUserList.value);
        userlist.value = filteredUserList.value; // 同步过滤后的用户列表到原始数据中
        return;
    }


    // 自定义筛选逻辑：用户角色数组与选中角色有交集即显示
    filteredUserList.value = originalUserList.value.filter(user => {
        // 检查用户的角色是否包含至少一个选中的角色
        return newRoles.every(role => user.roles.includes(role.role_name));
    });

    console.log('显示筛选后用户:', filteredUserList.value);
    userlist.value = filteredUserList.value; // 同步过滤后的用户列表到原始数据中

}, { deep: true }); // deep: true 监听数组内部元素变化

// --------------------------------------------------------------用户名筛选
const searchSysuser_name = ref("");
watch(searchSysuser_name, (newName) => {
    console.log('用户名筛选变化：', newName);
    if (newName === "") {
        filteredUserList.value = [...originalUserList.value];
        console.log('显示所有用户:', filteredUserList.value);
        userlist.value = filteredUserList.value; // 同步过滤后的用户列表到原始数据中
        return;
    }
    // 自定义筛选逻辑：用户名包含搜索关键字
    filteredUserList.value = originalUserList.value.filter(user => {
        return user.sysuser_name.includes(newName);
    });
    console.log('显示筛选后用户:', filteredUserList.value);
    console.log('原始数据:', originalUserList.value);
    userlist.value = filteredUserList.value; // 同步过滤后的用户列表到原始数据中

});

// --------------------------------------------------------------真实姓名筛选
const searchSysuser_real_name = ref("");
watch(searchSysuser_real_name, (newRealName) => {
    console.log('真实姓名筛选变化：', newRealName);
    if (newRealName === "") {
        filteredUserList.value = [...originalUserList.value];
        console.log('显示所有用户:', filteredUserList.value);
        userlist.value = filteredUserList.value; // 同步过滤后的用户列表到原始数据中
        return;
    }
    // 自定义筛选逻辑：真实姓名包含搜索关键字
    filteredUserList.value = originalUserList.value.filter(user => {
        return user.sysuser_real_name.includes(newRealName);
    });
    console.log('显示筛选后用户:', filteredUserList.value);
    userlist.value = filteredUserList.value; // 同步过滤后的用户列表到原始数据中

});


// ------------------------------------------------------------------- 角色筛选










// --------------------------------------------------------------------- 状态筛选列
// 状态切换处理函数
const handleStatusChange = (data) => {

    let info = !data.activate_status ? "是否禁用用户" + data.sysuser_name + "的账号？禁用后该用户将无法登录系统。" : "是否启用用户" + data.sysuser_name + "的账号？启用后可使用该账号登录系统。";
    // 弹窗提示
    confirm.require({
        message: info,
        header: '确认提示',
        icon: 'pi pi-question-circle',
        rejectProps: {
            label: '取消',
            severity: 'secondary',
            outlined: true
        },
        acceptProps: {
            label: '确定'
        },
        accept: () => {
            // 调用接口（预留axios位置）
            // 1. 调用接口（预留axios位置）
            axiosInstance.post('/sysUser/activatestatuschange', { sysUserId: data.id, activate_status: data.activate_status })
                .then(response => {
                    if (response.data.code === 0) {
                        toast.add({ severity: 'success', summary: '成功', detail: '修改状态成功', life: 3000 });
                        // 刷新用户列表
                        getSystemUserList();
                    } else {
                        toast.add({ severity: 'error', summary: '失败', detail: response.data.message, life: 3000 });
                    }
                })
                .catch(error => {
                    console.log('Error fetching data:', error);
                    toast.add({ severity: 'error', summary: '失败', detail: '网络错误，请重试', life: 3000 });
                });
        },
        reject: () => {
            console.log('取消修改');
            // 刷新用户列表
            getSystemUserList();
        }
    });

};






// 其他代码保持不变，重点修复筛选器配置
const filters = ref({
    global: { value: null, matchMode: FilterMatchMode.CONTAINS },
    id: { value: null, matchMode: FilterMatchMode.EQUALS },
    userName: { value: null, matchMode: FilterMatchMode.CONTAINS },
    userRealName: { value: null, matchMode: FilterMatchMode.CONTAINS },
    // 修复1：角色筛选 - 确保matchMode为IN，且默认值为数组
    roles: { value: [], matchMode: FilterMatchMode.IN },
    // 修复2：状态筛选 - 默认值为null，匹配EQUALS模式
    activate: { value: null, matchMode: FilterMatchMode.EQUALS },
    lastLoginTime: { value: null, matchMode: FilterMatchMode.DATE_IS }
});






// 日期格式化函数
const formatDate = (dateStr) => {
    if (!dateStr) return "未登录";
    const date = new Date(dateStr);
    return date.toLocaleString('zh-CN', {
        year: 'numeric',
        month: '2-digit',
        day: '2-digit',
        hour: '2-digit',
        minute: '2-digit',
        second: '2-digit'
    });
};

onMounted(() => {
    console.log('组件已挂载完成，用户数据加载成功');

    getSystemUserList();


});
</script>

<style>
@layer reset, primevue, custom;

@layer custom {
    .usermanagement-container {
        height: 100%;
        width: calc(100% - 2rem);
        /* 优化阴影参数，增强显示效果 */
        box-shadow: 0 2px 12px rgba(0, 0, 0, 0.07),
            0 8px 16px rgba(0, 0, 0, 0.05);
        position: relative;
        border-radius: 8px;
        background-color: #ffffff;
        /* 确保背景不透明 */
        /* 关键：避免阴影被父容器截断 */
        margin: 1rem;
        /* 增加外间距，让阴影有展示空间 */
        padding: 1rem;
        z-index: 1;
        /* 确保在同级元素上方显示 */
    }

    .auxfunction {
        background-color: #ffffff;
        padding: 0.8rem 1rem;
        border-radius: 4px;
        margin-bottom: 1rem;
    }

    .btn-container {
        background-color: transparent;
        /* 移除突兀背景色 */
        height: auto;
        width: auto;
        display: flex;
        justify-content: flex-end;
        align-items: center;
        gap: 0.5rem;
        position: relative;
    }

    .btn {
        background-color: #0f172a;
        border: none;
        position: relative;
        transition: background-color 0.2s;
    }

    .btn.edit {
        background-color: #2d2f3c;
    }

    .btn.delete {
        background-color: #7058f5;
    }

    .btn.changePwd {
        background-color: #d7d8e8;
        color: #2d2f3c;
    }

    .btn:hover {
        /* 增加变暗效果 */
        filter: brightness(90%);
    }

    .btn .p-button-icon-left {
        --webkit-backface-visibility: hidden;
        backface-visibility: hidden;
    }

    .rowOperations {
        display: flex;
        gap: 0.5rem;
    }


}
</style>