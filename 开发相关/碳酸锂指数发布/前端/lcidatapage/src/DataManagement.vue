<template>
  <div class="data-management-container">
    <!-- 粒子动效容器 -->
    <div id="particles-js" class="particles-container"></div>


    <div class="content-wrapper">
      <!-- 页面标题 + 上传说明入口 -->
      <h1 class="title">
        碳酸锂指数数据上传平台
        <span class="instructions-link" @click="showInstructions">文件上传说明</span>
      </h1>
      
      <!-- 文件上传区域 -->
      <div class="upload-section">
        <el-upload
          class="upload-demo"
          drag                
          :auto-upload="false"
          accept=".xlsx"
          :on-change="handleFileChange"
          :show-file-list="true"
          :limit="1">
          <el-icon class="el-icon--upload"><upload-filled /></el-icon>
          <div class="el-upload__text">
            将文件拖到此处或 <em>点击上传</em>
          </div>
          <template #tip>
            <div class="el-upload__t ip" style="color: aliceblue;">
              请上传 xlsx 格式的 Excel 文件
            </div>
          </template>
        </el-upload>

        <el-button 
          type="primary" 
          :disabled="!selectedFile"
          @click="handleUpload"
          class="upload-button">
          开始上传
        </el-button>
      </div>

      <!-- 上传进度显示 -->
      <div v-if="uploadProgress > 0" class="progress-section">
        <el-progress 
          :percentage="uploadProgress"
          :status="uploadStatus"
          :stroke-width="10">
        </el-progress>
      </div>
    </div>
    <UploadInstructions ref="instructionsDialog" />
  </div>
</template>

<script setup>
import { ref, onMounted } from 'vue'
import { UploadFilled } from '@element-plus/icons-vue'  // 上传图标
import { ElMessage } from 'element-plus'  // 全局提示
import axios from 'axios'  // 网络请求
import 'particles.js'  // 粒子动效库
import UploadInstructions from './components/UploadInstructions.vue'  // 上传说明弹窗组件

const instructionsDialog = ref(null) // 控制说明弹窗的显示/隐藏
const selectedFile = ref(null)
const uploadProgress = ref(0)
const uploadStatus = ref('')


// 组件挂载完成后初始化粒子动效
onMounted(() => {
  // 初始化particles.js动效，绑定到id为particles-js的容器
  window.particlesJS('particles-js', {
    particles: {
      // 粒子数量/密度
      number: {
        value: 80,
        density: {
          enable: true,
          value_area: 800
        }
      },
      // 粒子颜色（Element Plus主色）
      color: {
        value: '#409EFF'
      },
      // 粒子形状
      shape: {
        type: 'circle'
      },
      // 粒子透明度
      opacity: {
        value: 0.5,
        random: false
      },
      // 粒子大小
      size: {
        value: 3,
        random: true
      },
      // 连线配置
      line_linked: {
        enable: true,
        distance: 150,
        color: '#409EFF',
        opacity: 0.4,
        width: 1
      },
      // 粒子运动配置
      move: {
        enable: true,
        speed: 2,
        direction: 'none',
        random: false,
        straight: false,
        out_mode: 'out',
        bounce: false
      }
    },
    // 交互事件
    interactivity: {
      detect_on: 'canvas',
      events: {
        onhover: {
          enable: true,
          mode: 'grab'
        },
        onclick: {
          enable: true,
          mode: 'push'
        },
        resize: true
      },
      // 交互模式
      modes: {
        grab: {
          distance: 140,
          line_linked: {
            opacity: 1
          }
        },
        push: {
          particles_nb: 4
        }
      }
    },
    retina_detect: true
  })
})

// 打开上传说明弹窗
const showInstructions = () => {
  instructionsDialog.value.dialogVisible = true
}

// 文件选择/状态变化处理
const handleFileChange = (file) => {
  // 校验文件类型：仅允许xlsx格式
  if (file.raw.type === 'application/vnd.openxmlformats-officedocument.spreadsheetml.sheet') {
    selectedFile.value = file.raw // 缓存选中的原始文件对象
  } else {
    ElMessage.error('请上传 xlsx 格式的文件！') // 格式错误提示
  }
}

// 手动触发文件上传异步函数
const handleUpload = async () => {
  // 前置校验：未选文件则提示并终止
  if (!selectedFile.value) {
    ElMessage.warning('请先选择文件！')
    return
  }

  // 构建FormData（文件上传标准格式）
  const formData = new FormData()
  // 追加文件到formData
  formData.append('file', selectedFile.value)

  try {
    // 重置上传状态
    uploadProgress.value = 0
    uploadStatus.value = ''
    // 发起POST上传请求，监听上传进度
    const response = await axios.post('http://172.17.10.223:8082/api_lcidataupload/load', formData, {
      // 实时更新上传进度
      onUploadProgress: (progressEvent) => {
        uploadProgress.value = Math.round(
          (progressEvent.loaded * 100) / progressEvent.total
        )
      }
    })
    console.log(response.data) // 调试用：打印接口返回数据
    // if (response.data.code === 400){
    //   ElMessage.success(response.data.msg)
    // }
    
    // 上传结果判断
    if (response.data === 'Data uploaded successfully') {
      uploadStatus.value = 'success' // 标记上传成功
      ElMessage.success('文件上传成功！')
    } else {
      uploadStatus.value = 'exception' // 标记上传异常
      ElMessage.error(response.data || '上传失败，请检查网络或联系管理员')
    }
  } catch (error) {
    // 捕获网络/接口异常
    uploadStatus.value = 'exception'
    ElMessage.error('上传过程中发生错误：' + error.message)
  }
}
</script>

<style scoped>
.data-management-container {
  min-height: 100vh;
  background: #1a1a1a;

  /* flex布局居中  */
   display: flex;
  justify-content: center;
  align-items: center;

  padding: px;
  position: relative;
}

.particles-container {
  position: absolute;
  top: 0;
  left: 0;
  width: 100%;
  height: 100%;
  z-index: 1;
}

.content-wrapper {
  position: relative;
  z-index: 2;
  background: rgba(30, 30, 30, 0.8);
  backdrop-filter: blur(10px);
  border-radius: 15px;
  padding: 40px;
  width: 100%;
  max-width: 800px;
  box-shadow: 0 8px 32px rgba(0, 0, 0, 0.3);
  border: 1px solid rgba(64, 158, 255, 0.1);
}

.title {
  color: #ffffff;
  text-align: center;
  margin-bottom: 40px;
  font-size: 28px;
  font-weight: 600;
  text-shadow: 0 0 10px rgba(64, 158, 255, 0.3);

  display: flex;
  align-items: center;
  justify-content: center;
  gap: 20px;
}

.instructions-link {
  font-size: 14px;
  color: #E6A23C;
  cursor: pointer;
  padding: 5px 10px;
  border-radius: 4px;
  transition: all 0.3s ease;
  text-shadow: none;
}

.instructions-link:hover {
  transform: translateY(-2px);
  box-shadow: 0 0 15px rgba(230, 162, 60, 0.5);
  background: rgba(230, 162, 60, 0.1);
}

.upload-section {
  display: flex;
  flex-direction: column;
  align-items: center;
  gap: 20px;
}

.upload-demo {
  width: 100%;
}

.upload-button {
  width: 200px;
  height: 40px;
  margin-top: 20px;
  background: #409EFF;
  border: none;
  box-shadow: 0 0 15px rgba(64, 158, 255, 0.3);
  transition: all 0.3s ease;
}

.upload-button:hover {
  transform: translateY(-2px);
  box-shadow: 0 0 20px rgba(64, 158, 255, 0.5);
}

.progress-section {
  margin-top: 30px;
  width: 100%;
}

:deep(.el-upload-dragger) {
  width: 100%;
  height: 200px;
  display: flex;
  flex-direction: column;
  justify-content: center;
  align-items: center;
  background: rgba(40, 40, 40, 0.8);
  border: 2px dashed rgba(64, 158, 255, 0.3);
  transition: all 0.3s ease;
}

:deep(.el-upload-dragger:hover) {
  border-color: #409eff;
  background: rgba(40, 40, 40, 0.9);
  transform: translateY(-2px);
}

.el-icon--upload {
  font-size: 48px;
  color: #409eff;
  margin-bottom: 10px;
  filter: drop-shadow(0 0 5px rgba(64, 158, 255, 0.3));
}

.el-upload__text {
  color: #ffffff;
}

.el-upload__text em {
  color: #409eff;
  font-style: normal;
}

.el-upload__tip {
  color: #909399;
  margin-top: 10px;
}

:deep(.el-progress-bar__outer) {
  background-color: rgba(64, 158, 255, 0.1);
}

:deep(.el-progress__text) {
  color: #ffffff;
}
</style> 