<template>
    <div class="upload-board-container">
        <Toast />


        <Card>

            <template #header>
                <div class="card-header">
                    <span class="card-title">文件上传</span>
                </div>
            </template>
            <template #content>
                <FileUpload name="upLoadFileList" url="/api/upload" @upload="onAdvancedUpload"
                    @progress="onUploadProgress" @before-upload="onBeforeUpload" :multiple="false" accept=".xlsx"
                    :maxFileSize="10000000" :customUpload="true" @uploader="customUploader">
                    <template #empty>
                        <div style="text-align: center; padding: 2rem;">
                            <i class="pi pi-cloud-upload" style="font-size: 3rem; color: #ccc;"></i>
                            <p>拖拽 Excel 文件(.xlsx)到此处上传</p>
                            <p style="color: #666; font-size: 0.9rem;">或点击选择文件按钮</p>
                        </div>
                    </template>
                    <template #content="{ files, removeFileCallback }">
                        <div v-if="files.length > 0">
                            <h5>待上传文件</h5>
                            <div v-for="(file, index) of files" :key="file.name + file.type + file.size"
                                class="file-item">
                                <div class="file-info">
                                    <i class="pi pi-file-excel" style="font-size: 2rem; color: #217346;"></i>
                                    <div class="file-details">
                                        <span class="file-name">{{ file.name }}</span>
                                        <span class="file-size">{{ formatSize(file.size) }}</span>
                                        <!-- 上传进度条 -->
                                        <div v-if="uploadingFiles[file.name]" class="upload-progress">
                                            <ProgressBar :value="uploadingFiles[file.name].progress" :showValue="true"
                                                style="margin-top: 0.5rem; height: 1.2rem;">
                                                <template #value="slotProps">
                                                    <span style="font-size: 0.7rem; line-height: 1;">{{ slotProps.value
                                                        }}%</span>
                                                </template>
                                            </ProgressBar>
                                            <span class="upload-status">{{ uploadingFiles[file.name].status }}</span>
                                        </div>
                                    </div>
                                </div>
                                <Button icon="pi pi-times" @click="removeFileCallback(index)"
                                    class="p-button-outlined p-button-rounded p-button-danger"
                                    :disabled="uploadingFiles[file.name]?.uploading" />
                            </div>
                        </div>
                    </template>
                </FileUpload>

                <!-- 上传记录表格 -->
                <div v-if="uploadRecords.length > 0" style="margin-top: 2rem;">
                    <h5>上传记录</h5>
                    <DataTable :value="uploadRecords" stripedRows tableStyle="min-width: 50rem">
                        <Column field="fileName" header="文件名">
                            <template #body="slotProps">
                                <div style="display: flex; align-items: center; gap: 0.5rem;">
                                    <i class="pi pi-file-excel" style="color: #217346;"></i>
                                    {{ slotProps.data.fileName }}
                                </div>
                            </template>
                        </Column>
                        <Column field="fileSize" header="文件大小">
                            <template #body="slotProps">
                                {{ formatSize(slotProps.data.fileSize) }}
                            </template>
                        </Column>
                        <Column field="uploadTime" header="上传时间" />
                        <Column field="status" header="状态">
                            <template #body="slotProps">
                                <Tag :value="slotProps.data.status"
                                    :severity="slotProps.data.status === '成功' ? 'success' : 'danger'" />
                            </template>
                        </Column>
                        <Column header="操作">
                            <template #body="slotProps">
                                <Button icon="pi pi-trash" @click="removeRecord(slotProps.index)"
                                    class="p-button-outlined p-button-rounded p-button-danger p-button-sm" />
                            </template>
                        </Column>
                    </DataTable>
                </div>
            </template>
        </Card>
    </div>
</template>

<script setup>
import Card from 'primevue/card';
import FileUpload from 'primevue/fileupload';
import Button from 'primevue/button';
import ProgressBar from 'primevue/progressbar';
import DataTable from 'primevue/datatable';
import Column from 'primevue/column';
import Tag from 'primevue/tag';
import Toast from 'primevue/toast';
import { useToast } from "primevue/usetoast";

import axiosInstance from '../axiosConfig.js';

import { ref, inject } from 'vue';

const toast = useToast();

const update_DataBoard_operationRecord = inject("update_DataBoard_operationRecord");
const update_operation_message = inject('update_operation_message')

// 响应式数据
const uploadingFiles = ref({}); // 正在上传的文件状态
const uploadRecords = ref([]); // 上传记录
const useRealUpload = ref(true); // 是否使用真实上传

// 自定义上传处理器
const customUploader = async (event) => {
    const files = event.files;
    if (!files || files.length === 0) {
        return;
    }
    if (files.length > 1) {
        const message = '只可上传一个文件，请检查待上传文件列表！';
        update_DataBoard_operationRecord('upload_failed');
        update_operation_message(message);
        return;
    }

    for (let file of files) {
        // 根据开关选择真实上传或模拟上传
        if (useRealUpload.value) {
            await realUploadFile(file); // 真实接口上传
        }

    }
};

// 接口上传
const realUploadFile = async (file) => {
    const fileName = file.name;

    // 初始化上传状态
    uploadingFiles.value[fileName] = {
        progress: 0,
        status: '准备上传...',
        uploading: true
    };

    try {
        // 创建FormData对象, 用于上传文件
        const formData = new FormData();
        formData.append('upLoadFileList', file);

        // 发送上传请求
        const response = await axiosInstance({
            method: 'POST',
            url: '/clientClassMaintenance/upload',
            data: formData,
            // 配置进度监听（核心：通过onUploadProgress监听上传进度）
            onUploadProgress: (progressEvent) => {
                const { loaded, total } = progressEvent;
                const progress = Math.round((loaded * 100) / total);
                uploadingFiles.value[fileName].progress = progress;

                if (progress < 30) {
                    uploadingFiles.value[fileName].status = '连接服务器...';
                } else if (progress < 70) {
                    uploadingFiles.value[fileName].status = '上传中...';
                } else if (progress < 100) {
                    uploadingFiles.value[fileName].status = '处理中...';
                } else {
                    uploadingFiles.value[fileName].status = '上传完成';
                }
            },
            headers: {
                // ✅ 手动指定 multipart/form-data，不写 boundary
                "Content-Type": "multipart/form-data",
                // 其他头（如 token）可正常添加
                // "Authorization": "Bearer " + token
            }
        });
        // 上传成功
        if (response.data.code === 0) {
            uploadingFiles.value[fileName].status = '上传完成';
            uploadRecords.value.unshift({
                fileName: fileName,
                fileSize: file.size,
                uploadTime: new Date().toLocaleString(),
                status: '成功',
                response: response.data
            });
            const message = '上传成功！';
            update_DataBoard_operationRecord('upload_success');
            update_operation_message(message);
        }else {
            uploadingFiles.value[fileName].status = '上传失败';
            uploadRecords.value.unshift({
                fileName: fileName,
                fileSize: file.size,
                uploadTime: new Date().toLocaleString(),
                status: '失败',
                response: response.data
            });
            
            toast.add({ severity: 'error', summary: '失败', detail: response.data.message, life: 3000 });
        }

        // 清除上传状态
        delete uploadingFiles.value[fileName];
    } catch (error) {
        // 6. 上传失败处理（Axios 错误处理需注意区分不同错误类型）
        let errorMsg = "上传失败";
        if (error.response) {
            // 后端返回错误状态码（如 400、500）
            errorMsg = `服务器错误：${error.response.status} - ${error.response.data?.message || error.response.statusText}`;
        } else if (error.request) {
            // 网络错误（无响应）
            errorMsg = "网络错误，无法连接服务器";
        } else {
            // 请求配置错误
            errorMsg = `请求错误：${error.message}`;
        }

        // 更新状态
        uploadingFiles.value[fileName].status = errorMsg;
        uploadRecords.value.unshift({
            fileName: fileName,
            fileSize: file.size,
            uploadTime: new Date().toLocaleString(),
            status: "失败",
            error: errorMsg
        });
    }
};


// 其他事件处理器
const onAdvancedUpload = (event) => {
    console.log('File uploaded:', event);
};

const onUploadProgress = (event) => {
    console.log('Upload progress:', event);
};

const onBeforeUpload = (event) => {
    console.log('Before upload:', event);
};

// 删除上传记录
const removeRecord = (index) => {
    uploadRecords.value.splice(index, 1);
};

// 格式化文件大小
const formatSize = (bytes) => {
    if (bytes === 0) return '0 Bytes';
    const k = 1024;
    const sizes = ['Bytes', 'KB', 'MB', 'GB'];
    const i = Math.floor(Math.log(bytes) / Math.log(k));
    return parseFloat((bytes / Math.pow(k, i)).toFixed(2)) + ' ' + sizes[i];
};
</script>

<style>
@layer reset, primevue, custom;

@layer custom {
    .upload-board-container {
        width: 100%;
    }

    .upload-board-container .card-header {
        display: flex;
        align-items: center;
        padding: 1rem;
    }

    .upload-board-container .card-title {
        font-size: 1.3rem;
        font-weight: bold;
        letter-spacing: 0.08rem;
    }

    /* 文件项样式 */
    .file-item {
        display: flex;
        align-items: center;
        justify-content: space-between;
        padding: 1rem;
        border: 1px solid #e9ecef;
        border-radius: 8px;
        margin-bottom: 0.5rem;
        background-color: #f8f9fa;
    }

    .file-info {
        display: flex;
        align-items: center;
        gap: 1rem;
    }

    .file-details {
        display: flex;
        flex-direction: column;
    }

    .file-name {
        font-weight: 600;
        color: #495057;
        margin-bottom: 0.25rem;
    }

    .file-size {
        font-size: 0.875rem;
        color: #6c757d;
    }

    /* Excel 文件图标颜色 */
    .pi-file-excel {
        color: #217346 !important;
    }

    /* 上传进度样式 */
    .upload-progress {
        width: 100%;
        margin-top: 0.5rem;
    }

    .upload-status {
        font-size: 0.8rem;
        color: #6c757d;
        margin-top: 0.25rem;
        display: block;
    }

    /* 进度条自定义样式 */
    .upload-progress .p-progressbar {
        height: 1.2rem;
        background-color: #e9ecef;
        border-radius: 0.375rem;
        position: relative;
        overflow: visible;
    }

    .upload-progress .p-progressbar .p-progressbar-value {
        background: linear-gradient(90deg, #28a745 0%, #20c997 100%);
        border-radius: 0.375rem;
        display: flex;
        align-items: center;
        justify-content: center;
    }

    .upload-progress .p-progressbar .p-progressbar-label {
        font-size: 0.7rem;
        font-weight: 500;
        color: white;
        text-shadow: 0 1px 2px rgba(0, 0, 0, 0.3);
        line-height: 1;
        z-index: 1;
    }
}
</style>