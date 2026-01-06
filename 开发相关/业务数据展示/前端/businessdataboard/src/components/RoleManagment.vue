<template>
    <div class="rolesetting-container">
        <ConfirmDialog></ConfirmDialog>
        <Dialog v-model:visible="addNewRoleDialog" header="新建角色" :modal="true" :style="{ width: '400px' }">
            <div class="addnewrole-container"
                style="display: flex; flex-direction: column; width: 100%; height: 100%; justify-content: center; align-items: center;">
                <div
                    style="display: flex; flex-direction: column; width: 100%; height: 100%; justify-content: flex-start">
                    <span
                        style="font-weight: 800; color: #fc312e; letter-spacing: 0.01rem; margin-bottom: 0.4rem;">提示：</span>
                    <span
                        style="font-weight: 600; color: #fc312e; letter-spacing: 0.01rem; margin-bottom: 0.4rem;">1、新建角色名称、代码均不可与已有角色重复</span>

                    <span
                        style="font-weight: 600; color: #fc312e; letter-spacing: 0.01rem; margin-bottom: 0.4rem;">2、新建角色默认无任何权限</span>

                </div>

                <!-- 提示信息 -->
                <div class="info-container" style="margin-bottom: 1rem; height: 20px;">
                    <div v-if="showText1" :class="['animate__animated', currentAnimation1]">
                        <i class="pi pi-times-circle"
                            style="font-size: 0.8rem;margin-right: 0.3rem; color: #FF412C;"></i>
                        <span class="info-text" style="font-size: 14px; font-weight: 600; color: #FF412C;">{{
                            showTextInfo }}</span>
                    </div>
                </div>

                <div class="info-addnewuserinput">
                    <InputText v-model="newRoleName" placeholder="角色名称" />
                    <InputText v-model="newRoleCode" placeholder="角色代码" />
                </div>
            </div>
            <template #footer>
                <Button label="取消" @click="addNewRoleDialog = false" class="mr-2" />
                <Button label="确认创建" severity="success" @click="handleAddNewRole" />
            </template>
        </Dialog>


        <Splitter class="splitter-container">
            <SplitterPanel class="splitter-panel role-list" :size="15" :minSize="10">
                <!-- 角色列表容器：撑满SplitterPanel -->
                <div class="role-list-container">
                    <!-- 角色项：循环渲染 -->
                    <div v-for="role in roleListRander" :key="role.role_code" class="role-item"
                        :class="{ 'role-item-selected': selectedRole?.role_code === role.role_code }"
                        @click="handleRoleChange(role)">
                        <span class="role-name">{{ role.role_name }}</span>
                        <span class="role-code">{{ role.role_code }}</span>
                    </div>
                    <div class="auxfunction-addnewrole">
                        <!-- <Button class="btn" label="新建角色" @click="addNewRoleDialog = true" /> -->
                         <Button class="btn" label="新建角色" @click="callAddNewRoleDialog()" />
                    </div>
                </div>

            </SplitterPanel>
            <SplitterPanel class="splitter-panel auth-function" :size="85">
                <div class="warnning"
                    style="display: flex; justify-content: center; align-items: center; height: 2rem; background-color: #E4ECF3;">
                    <i class="pi pi-exclamation-circle"
                        style="font-size: 1rem; color: #FF412C; margin-right: 0.5rem;"></i>
                    <span style="color: #FF412C;">做出调整后请及时通过保存按钮保存设置，否则调整将无效！</span>
                </div>
                <div class="rolename-edit-container">
                    <!-- 原有内容：角色名称标签和输入框 -->
                    <div class="edit-lable">
                        <span>角色名称：</span>
                    </div>
                    <div class="function">
                        <InputText v-model="selectedRole.role_name" :placeholder="selectedRole?.role_name || '请选择角色'" />
                    </div>

                    <!-- 按钮外层容器：添加 ml-auto 类 -->
                    <div class="delete-btn-container">
                        <Button label="删除当前角色" style="background-color: #fc312e;border: none;"
                            @click="handleDeleteRole" />
                        <Button label="保存设置" style="background-color: #202020;border: none;margin-left: 0.5rem;"
                            @click="handleSaveRole" />
                    </div>
                </div>
                <div class="rolefunctions-edit-container">
                    <div class="edit-lable">
                        <span>功能权限：</span>
                    </div>
                    <div class="function">
                        <div class="checkbox" v-for="func in sysfunctionListForRander" :key="func.function_code">

                            <Checkbox v-model="selectedFuncKeys" :inputId="`func-${func.function_code}`"
                                :value="func.function_code" />
                            <!-- 修正3：label的for与inputId一致，确保点击label选中Checkbox -->
                            <label class="checkbox-label" :for="`func-${func.function_code}`">
                                {{ func.function_name }}
                            </label>
                        </div>
                    </div>
                </div>
                <div class="roledataauth-edit-container">
                    <div class="edit-lable">
                        <span>数据权限：</span>
                    </div>
                    <div class="function">
                        <BlockUI :blocked="blocked">
                            <span v-if="blocked"
                                style="position: absolute;top:50%;transform:translate(-50%,-50%);color: #2d2f3c;">无数据查看权限</span>
                            <Tree v-model:selectionKeys="selectedDataAuthKeys" :value="dataAuthNodes"
                                selectionMode="checkbox" v-model:expandedKeys="expandedKeys">
                            </Tree>
                        </BlockUI>

                    </div>

                </div>
                <div class="save-btn">

                </div>
            </SplitterPanel>
        </Splitter>

    </div>
</template>

<script setup>
import { onMounted, ref, watch } from 'vue';
import Splitter from 'primevue/splitter';
import SplitterPanel from 'primevue/splitterpanel';
import Button from 'primevue/button';
import Dialog from 'primevue/dialog';
import InputText from 'primevue/inputtext';
import Checkbox from 'primevue/checkbox';
import Tree from 'primevue/tree';
import BlockUI from 'primevue/blockui';

import axiosInstance from '../axiosConfig.js';

import { useConfirm } from "primevue/useconfirm";
import { useToast } from "primevue/usetoast";

const confirm = useConfirm();
const toast = useToast();


// ---------------------------------------------角色列表获取接口调用
const roleListJson = ref([]);
const roleListRander = ref([]);
const selectedRole = ref({ role_id: 0, role_code: '', role_name: '', sysfunctions_list: [] });

const sysfunctionListForRander = ref([]);

const dataAuthList = ref([]);


const getAllRoles = () => {
    axiosInstance.get('/role/getallroles')
        .then(response => {
            if (response.data.code === 0) {
                console.log("成功获取角色列表");
                roleListJson.value = response.data.data;
                roleListRander.value = createRoleListForRander(response.data.data.roleList);


                sysfunctionListForRander.value = response.data.data.sysfunctionList;
                dataAuthList.value = response.data.data.dataViewList;
                handleDataAuthTree();


                // 1. 初始化选中第一个角色（修正为对象赋值）
                const firstRole = roleListRander.value[0] || {};
                selectedRole.value = {
                    ...firstRole,
                    // 确保sysfunctions_list存在（避免无权限时报错）
                    sysfunctions_list: firstRole.sysfunctions_list || [],
                    // 新增逻辑：auth_data_list
                    auth_data_list: firstRole.auth_data_list || []
                };

                // 2. 同步第一个角色的已授权权限到Checkbox
                // console.log("第一个角色：", selectedRole.value);
                syncSelectedFuncKeys(selectedRole.value.sysfunctions_list);

                // 3. 同步数据权限（新增逻辑）
                // console.log("第一个角色的数据权限：", selectedRole.value.auth_data_list);
                syncSelectedDataAuthKeys(selectedRole.value.auth_data_list || []);



            } else if (response.data.code === 4) {
                toast.add({ severity: 'error', summary: '错误', detail: response.data.message, life: 3000 });
            } else {
                toast.add({ severity: 'error', summary: '错误', detail: response.data.message, life: 3000 });
            }
        }).catch(error => {
            // console.log(error);

            console.log("错误类型：", error.name);
            console.log("错误信息：", error.message);
            if (error.response) {
                // 有响应但状态码非2xx
                console.log("响应状态码：", error.response.status);
                console.log("响应数据：", error.response.data);
            } else if (error.request) {
                // 无响应（如网络中断、超时）
                console.log("无响应：", error.request);
            }
            // ... 其他处理
            toast.add({ severity: 'error', summary: '错误', detail: error.message, life: 3000 });
        });

}

onMounted(() => {
    getAllRoles();
});




// ---------------------------------------------新建角色弹窗
const addNewRoleDialog = ref(false);
const newRoleName = ref('');
const newRoleCode = ref('');
const showText1 = ref(false);
const showTextInfo = ref('');
const currentAnimation1 = ref('animate__fadeInDown');

const callAddNewRoleDialog = () => {
    resetAddNewRoleParams();
    addNewRoleDialog.value = true;
};

const handleAddNewRole = () => {
    if (newRoleName.value === '' || newRoleCode.value === '') {
        if (newRoleName.value === '') {
            showTextInfo.value = '角色名称不能为空！';
        } else if (newRoleCode.value === '') {
            showTextInfo.value = '角色代码不能为空！';
        }

        showText1.value = true;
        currentAnimation1.value = 'animate__fadeInDown';
        setTimeout(() => {
            currentAnimation1.value = 'animate__fadeOutDown';
            setTimeout(() => {
                showText1.value = false;
            }, 1000); // 等待动画结束
        }, 1500); // 2秒后自动隐藏
        return;
    }
    axiosInstance.post('/role/addnewrole', {
        role_name: newRoleName.value,
        role_code: newRoleCode.value
    })
        .then(response => {
            if (response.data.code === 0) {
                console.log("成功创建角色");
                getAllRoles();
                addNewRoleDialog.value = false;
                newRoleName.value = '';
                newRoleCode.value = '';
            } else if (response.data.code === 4005) {
                showTextInfo.value = '角色名称已存在，请重新输入！';
                showText1.value = true;
                currentAnimation1.value = 'animate__fadeInDown';
                setTimeout(() => {
                    currentAnimation1.value = 'animate__fadeOutDown';
                    setTimeout(() => {
                        showText1.value = false;
                    }, 1000); // 等待动画结束
                }, 1500); // 2秒后自动隐藏
                return;
            } else if (response.data.code === 4006) {
                showTextInfo.value = '角色代码已存在，请重新输入！';
                showText1.value = true;
                currentAnimation1.value = 'animate__fadeInDown';
                setTimeout(() => {
                    currentAnimation1.value = 'animate__fadeOutDown';
                    setTimeout(() => {
                        showText1.value = false;
                    }, 1000); // 等待动画结束
                }, 1500); // 2秒后自动隐藏
                return;
            }
            else {
                toast.add({ severity: 'error', summary: '错误', detail: response.data.message, life: 3000 });
            }
        }).catch(error => {
            toast.add({ severity: 'error', summary: '错误', detail: error.message, life: 3000 });
        });
};

// 重置新增角色参数
const resetAddNewRoleParams = () => {
    newRoleName.value = '';
    newRoleCode.value = '';
};

// ---------------------------------------------角色列表
// 基于接口返回数据构建角色列表
const createRoleListForRander = (data) => {
    let result = [];
    data.forEach(item => {
        result.push({
            role_id: item.role_id,
            role_code: item.role_code,
            role_name: item.role_name,
            sysfunctions_list: item.sysfunctions_list || [],
            auth_data_list: item.personnel_list || []
        });
    });
    return result;
};

/**
 * 处理角色切换：更新选中角色 + 同步已授权权限
 * @param {Object} role - 点击选中的角色对象
 */
const handleRoleChange = (role) => {
    // 更新选中角色
    selectedRole.value = {
        ...role,
        sysfunctions_list: role.sysfunctions_list || [], // 确保权限列表存在
        auth_data_list: role.auth_data_list || []
    };
    // 同步该角色的已授权权限到Checkbox
    syncSelectedFuncKeys(selectedRole.value.sysfunctions_list);
    syncSelectedDataAuthKeys(selectedRole.value.auth_data_list);
};

// ----------------------------------------------功能权限
const blocked = ref(false);
// 存储当前角色已选中的功能权限code（核心绑定变量）
const selectedFuncKeys = ref([]);
const syncSelectedFuncKeys = (authorizedFuncs) => {
    console.log("授权功能：", authorizedFuncs);
    // 提取已授权功能的function_code，组成数组（如：['home', 'authsetting']）
    const authorizedCodes = authorizedFuncs.map(func => func.function_code);
    // 更新选中状态数组，触发Checkbox同步
    console.log("已授权功能：", authorizedCodes);
    selectedFuncKeys.value = authorizedCodes;
    // 如果已授权功能中没有"home"，则将数据权限面板的blocakUI设置为true
    if (!authorizedCodes.includes('home')) {
        blocked.value = true;
    } else {
        blocked.value = false;
    }

};

// ----------------------------------------------数据权限
// 2. 存储选中节点的key
const selectedDataAuthKeys = ref({});
const dataAuthNodes = ref(null);
// 记录部门code，用于调用角色更新接口时，去掉要作为请求体的selectedDataAuthKeys中的非有效personnel_code字符串
const deptCodes = new Set();

// 默认展开所有部门
const expandedKeys = ref({}); // 用于控制展开状态的响应式变量


// 3. 处理数据权限树
const handleDataAuthTree = () => {
    let result_data = [];
    let dept_list = new Map();

    const createChildNodes = (personnel) => {
        let node = {};
        node.key = personnel.personnel_code;
        node.label = personnel.personnel_name;
        node.data = personnel.personnel_code;
        node.icon = "pi pi-user";
        return node;
    };

    console.log("数据权限列表处理：", dataAuthList.value);

    dataAuthList.value.forEach(item => {
        let node; // 先声明变量，不提前初始化
        let node_key;

        if (item.department_code !== "") {
            node_key = item.department_code;
            deptCodes.add(item.department_code);
        } else {
            node_key = "no dept";
        }

        // 核心修正：无论部门是否存在，都需要添加当前人员节点
        const personnelNode = createChildNodes(item); // 先创建当前人员节点

        if (!dept_list.has(node_key)) {
            // 1. 部门不存在：创建部门节点，并将当前人员添加到children
            node = {};
            node.key = node_key;
            node.label = item.department_code ? item.department_name : "无部门";
            node.data = node_key;
            node.icon = "pi pi-folder-open";
            node.children = [personnelNode]; // 初始化时就加入当前人员
            dept_list.set(node_key, node);
        } else {
            // 2. 部门已存在：直接从Map获取部门，并添加当前人员
            node = dept_list.get(node_key);
            node.children.push(personnelNode);
        }
    });

    dept_list.forEach(value => {
        result_data.push(value);
    });

    console.log(result_data);
    // 建议：将结果赋值给一个响应式变量供Tree组件使用，例如：
    dataAuthNodes.value = result_data;

    // 新增：设置所有节点为展开状态
    const allExpandedKeys = {};
    result_data.forEach(department => {
        // 展开部门节点
        allExpandedKeys[department.key] = true;

        // 如果有子节点，也展开所有子节点（多层嵌套的情况）
        if (department.children && department.children.length > 0) {
            department.children.forEach(child => {
                allExpandedKeys[child.key] = true;
            });
        }
    });

    expandedKeys.value = allExpandedKeys;
};

// 同步当前角色的已授权数据人员到Tree Checkbox选中状态
const syncSelectedDataAuthKeys = (authorizedPersonnel) => {
    // 提取已授权人员的personnel_code
    const authorizedCodes = authorizedPersonnel.map(person => person.personnel_code);

    // 创建符合Tree组件要求的selectionKeys格式
    const selectionKeys = {};

    // 将授权的人员code转换为Tree需要的格式
    authorizedCodes.forEach(code => {
        selectionKeys[code] = { checked: true };
    });

    // 新增：计算并设置父节点（部门）的选中状态
    if (dataAuthNodes.value) {
        dataAuthNodes.value.forEach(department => {
            if (department.children && department.children.length > 0) {
                // 检查该部门下所有人员是否都被选中
                const allChildrenSelected = department.children.every(child =>
                    authorizedCodes.includes(child.key)
                );

                // 如果所有子节点都被选中，则设置父节点为选中状态
                if (allChildrenSelected) {
                    selectionKeys[department.key] = { checked: true };
                }
            }
        });
    }

    // 更新Tree选中状态
    selectedDataAuthKeys.value = selectionKeys;
    console.log("已授权数据查看人员（Tree格式）：", selectionKeys);
};



watch(selectedFuncKeys, (newVal) => {
    console.log("选中功能权限：", newVal);
    if (newVal.includes('home')) {
        blocked.value = false;
    } else {
        blocked.value = true;
    }
});

watch(selectedDataAuthKeys, (newVal) => {
    console.log("Tree选中状态变化：", newVal);
});

// ---------------------------------------------保存角色权限设置接口调用
const handleSaveRole = () => {
    console.log("保存角色权限设置接口调用");
    // 选中的角色
    const role = selectedRole.value;

    // 选中的功能权限
    const selectedFuncCodes = selectedFuncKeys.value;

    // 选中的数据权限
    const selectedDataAuthCodes = Object.keys(selectedDataAuthKeys.value);
    let selectedDataAuthCodes_filtered = [];
    // 基于deptCodes去掉选中数据权限中的非有效personnel_code字符串
    deptCodes.add("no dept");
    selectedDataAuthCodes.forEach(code => {
        if (deptCodes.has(code)) {
            return;
        } else {
            selectedDataAuthCodes_filtered.push(code);
        }
    });

    if (blocked.value) {
        selectedDataAuthCodes_filtered = [];
    }

    console.log("选中的角色：", role);
    console.log("选中的功能权限：", selectedFuncCodes);
    console.log("选中的数据权限：", selectedDataAuthCodes_filtered);
    console.log("无效personnel_code：", deptCodes.values());

    axiosInstance.post('/role/updaterole', {
        role_id: role.role_id,
        role_code: role.role_code,
        role_name: role.role_name,
        role_func_permis_codes: selectedFuncCodes,
        role_data_permis_person_codes: selectedDataAuthCodes_filtered
    }).then(response => {
        if (response.data.code === 0) {
            console.log("成功保存角色权限设置");
            toast.add({ severity: 'info', summary: '成功', detail: '保存角色权限设置成功！' + response.data.message, life: 4000 });
            // 刷新页面
            getAllRoles();
        } else {
            toast.add({ severity: 'error', summary: '错误', detail: response.data.message, life: 3000 });
        }
    }).catch(error => {
        toast.add({ severity: 'error', summary: '错误', detail: error.message, life: 3000 });
    });
};

// ------------------------------------------------------------- 删除角色接口调用
const handleDeleteRole = () => {
    console.log("删除角色接口调用,删除角色：", selectedRole.value);
    const role = selectedRole.value;

    confirm.require({
        message: `确认删除角色：${role.role_name}？`,
        header: '确认提示',
        icon: 'pi pi-question-circle',
        rejectProps: {
            label: '取消',
            severity: 'secondary',
            outlined: true
        },
        acceptProps: {
            label: '确定',
            severity: 'danger',
        },
        accept: () => {
            axiosInstance.post('/role/deleterole', {
                id: role.role_id
            }).then(response => {
                if (response.data.code === 0) {
                    console.log("成功删除角色");
                    toast.add({ severity: 'info', summary: '成功', detail: '删除角色成功！', life: 4000 });
                    // 刷新页面
                    getAllRoles();
                } else {
                    toast.add({ severity: 'error', summary: '错误', detail: response.data.message, life: 3000 });
                }
            }).catch(error => {
                toast.add({ severity: 'error', summary: '错误', detail: error.message, life: 3000 });
            });
        },
        reject: () => {
            console.log("取消删除角色");
        }

    });
};



</script>

<style>
@layer reset, primevue, custom;

@layer custom {
    .rolename-edit-container {
        height: auto;
        width: 100%;
        display: flex;
        flex-direction: row;
        align-items: center;
        padding: 1rem;
        /* 关键：确保父容器有足够空间 */
        box-sizing: border-box;
    }

    /* 按钮容器：自动向右对齐 */
    .delete-btn-container {
        margin-left: auto;
        /* 核心属性，推到最右边 */
        /* margin-left: 1rem;  */
    }

    /* 左侧角色列表容器：撑满SplitterPanel */
    .role-list-container {
        width: 100%;
        height: 100%;
        padding: 0.5rem;
        box-sizing: border-box;
        overflow-y: auto;
        /* 内容超出时滚动 */
        background-color: #f8fafc;
        border-radius: 4px;
    }

    /* 角色项样式 */
    .role-item {
        width: 100%;
        padding: 0.8rem 1rem;
        margin-bottom: 0.3rem;
        border-radius: 4px;
        cursor: pointer;
        transition: all 0.4s ease-out;
        box-sizing: border-box;
        background-color: #ffffff;
        border: 1px solid #f1f5f9;
    }

    /* 选中状态样式 */
    .role-item-selected {
        background-color: #626c79;
        /* 选中时的背景色 */
        border-color: #2d2f3c;
        font-weight: 500;


    }

    .role-item:hover {

        /* hover效果 */
        transform: translateY(-2px);
        box-shadow: 0 4px 8px rgba(0, 0, 0, 0.05);

    }

    /* 角色名称和编码样式 */
    .role-name {
        display: block;
        font-size: 0.95rem;
        color: #1e293b;
    }

    .role-item-selected .role-name {
        color: #ffffff;
    }

    .role-code {
        display: block;
        font-size: 0.75rem;
        color: #64748b;
        margin-top: 0.2rem;
    }

    .role-item-selected .role-code {
        color: #ffffff;
    }

    .auxfunction-addnewrole {
        /* background-color: aqua; */
        /* display: flex;
        justify-content: flex-start;
        padding: 0; */
        width: 100%;

    }

    .auxfunction-addnewrole .btn {
        background-color: #698095;
        width: 100%;
    }

    .info-addnewuserinput {
        /* background-color: #1e293b; */
        margin-bottom: 1rem;

        display: flex;
        flex-direction: column;
        align-items: center;
        justify-content: space-between;
        gap: 1rem;
    }

    /* ----------------------------------------分窗2样式----------------------------------------- */
    .splitter-panel.auth-function {
        height: auto;
    }

    .auth-function .edit-lable {
        margin-right: 0.6rem;
    }

    .rolename-edit-container {
        height: auto;
        width: 100%;


        display: flex;
        flex-direction: row;
        align-items: center;
        /* justify-content: space-between; */

        padding: 1rem;

    }


    .rolefunctions-edit-container {
        /* background-color: #7788a1; */
        height: auto;
        width: 100%;

        display: flex;
        flex-direction: row;
        align-items: center;

        padding: 1rem;
    }

    .rolefunctions-edit-container .function {
        /* background-color: #6658ff; */

        display: flex;
        flex-direction: row;
    }

    .rolefunctions-edit-container .function .checkbox-label {
        margin-left: 0.6rem;
        margin-right: 2rem;

        /* font-size: 0.8rem; */
    }

    .rolefunctions-edit-container .function .checkbox {
        display: flex;
        flex-direction: row;
        align-items: center;
    }



    .roledataauth-edit-container {
        /* background-color: #eae8ff; */
        height: calc(100% - 26%);
        width: 100%;

        display: flex;
        flex-direction: row;
        /* align-items: center; */

        padding: 1rem;
    }

    .roledataauth-edit-container .function {
        /* background-color: #1e293b; */
        width: 70%;
        min-height: 400px
    }
}
</style>
