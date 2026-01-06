<template>
  <el-dialog
    v-model="dialogVisible"
    title="文件上传格式说明"
    width="80%"
    :before-close="handleClose"
    class="instructions-dialog">
    <div class="instructions-content">
      <div class="scroll-content">
        <div class="section">
          <div class="section-header">
            <h2>文件上传格式说明</h2>
            <p class="intro-text">为保障数据的准确传输与处理，确保系统的稳定运行，现就文件上传格式作出如下严格规定，请各位用户仔细阅读并遵照执行。</p>
          </div>

          <div class="subsection">
            <h3>一、表头要求</h3>
            
            <div class="sub-subsection">
              <h4>（一）表头内容及顺序</h4>
              <p>上传文件必须严格包含以下表头，且需按照此顺序排列：</p>
              <div class="header-list">
                <div class="header-item" v-for="(header, index) in headers" :key="index">
                  <span class="header-number">{{ index + 1 }}.</span>
                  <span class="header-text">{{ header }}</span>
                </div>
              </div>
              <div class="example-box">
                <div class="example-title">
                  <img src="@/assets/绿色对钩图标.png" alt="正确" class="icon"/>
                  <span>正确示例</span>
                </div>
                <img src="@/assets/表头说明正确示例.png" alt="正确示例" class="example-image"/>
              </div>
              <div class="example-box">
                <div class="example-title">
                  <img src="@/assets/红色错误图标.png" alt="错误" class="icon"/>
                  <span>错误示例</span>
                </div>
                <img src="@/assets/表头说明错误示例.png" alt="错误示例" class="example-image"/>
              </div>
            </div>

            <div class="sub-subsection">
              <h4>（二）表头起始位置</h4>
              <p>表头及表格数据必须严格从 A1 单元格开始记录。</p>
              <div class="example-box">
                <div class="example-title">
                  <img src="@/assets/绿色对钩图标.png" alt="正确" class="icon"/>
                  <span>正确示例</span>
                </div>
                <img src="@/assets/表头起始位置正确示例.png" alt="正确示例" class="example-image"/>
              </div>
              <div class="example-box">
                <div class="example-title">
                  <img src="@/assets/红色错误图标.png" alt="错误" class="icon"/>
                  <span>错误示例</span>
                </div>
                <img src="@/assets/表头起始位置错误示例.png" alt="错误示例" class="example-image"/>
              </div>
            </div>
          </div>

          <div class="subsection">
            <h3>二、数据格式要求</h3>
            
            <div class="sub-subsection">
              <h4>（一）日期格式</h4>
              <p>"日期" 字段需严格按照 "yyyy/mm/dd" 格式记录，例如 "2025/01/01"。</p>
              <div class="example-box">
                <div class="example-title">
                  <img src="@/assets/绿色对钩图标.png" alt="正确" class="icon"/>
                  <span>正确示例</span>
                </div>
                <img src="@/assets/日期格式正确示例.png" alt="正确示例" class="example-image"/>
              </div>
            </div>

            <div class="sub-subsection">
              <h4>（二）其他字段格式</h4>
              <p>其他字段需严格记录为数据文本，而非计算公式。此举旨在避免因公式引用、计算环境差异导致的数据错误或不一致，系统将无法识别以公式形式录入的数据，从而造成上传失败。</p>
              <div class="example-box">
                <div class="example-title">
                  <img src="@/assets/绿色对钩图标.png" alt="正确" class="icon"/>
                  <span>正确示例</span>
                </div>
                <img src="@/assets/表格数据格式正确示例.png" alt="正确示例" class="example-image"/>
              </div>
              <div class="example-box">
                <div class="example-title">
                  <img src="@/assets/红色错误图标.png" alt="错误" class="icon"/>
                  <span>错误示例</span>
                </div>
                <img src="@/assets/表格数据格式错误示例1.png" alt="错误示例1" class="example-image"/>
                <img src="@/assets/表格数据格式错误示例2.png" alt="错误示例2" class="example-image"/>
              </div>
            </div>
          </div>

          <div class="subsection">
            <h3>三、其他重要说明</h3>
            
            <div class="sub-subsection">
              <h4>（一）数据更新机制</h4>
              <p>系统以 "日期" 作为唯一索引。若上传数据中某条记录的日期在数据库中已存在，系统将自动对已有的记录进行更新操作。</p>
            </div>

            <div class="sub-subsection">
              <h4>（二）常见错误及解决办法</h4>
              <div class="error-solutions">
                <div class="error-item">
                  <h5>上传过程中发生错误：</h5>
                  <p>请检查网络连接，确认是否使用公司有线网络。若网络连接正常，可联系系统管理员寻求帮助。</p>
                </div>
                <div class="error-item">
                  <h5>上传失败：</h5>
                  <p>该问题可能是由后端代码的未知错误或性能问题导致。请及时联系系统管理员，以便排查和解决问题。</p>
                </div>
              </div>
            </div>
          </div>

          <div class="footer-note">
            <p>请各位用户严格遵循上述文件上传格式要求，以保障数据的有效传输与系统的稳定运行。如有任何疑问，可随时联系系统管理员。</p>
          </div>
        </div>
      </div>
    </div>
  </el-dialog>
</template>

<script setup>
  // 导入Vue核心API：ref用于响应式数据，defineExpose用于暴露组件内部属性
import { ref, defineExpose } from 'vue'


// 响应式数据：控制弹窗的显示/隐藏状态（初始隐藏）
const dialogVisible = ref(false)

// 业务常量：上传文件要求的表头列表（严格规定顺序和名称）
const headers = [
  '日期',
  '锂辉石',
  '低品位锂云母',
  '高品位锂云母',
  '磷酸铁锂正极片',
  '磷酸铁锂电池粉',
  '三元极片粉',
  '电碳现货价',
  '综合指数'
]


// done：Element Plus提供的关闭确认函数，调用后完成弹窗关闭
const handleClose = (done) => {
  done() // 执行关闭逻辑（默认行为，保证弹窗正常关闭）
}

// 暴露组件内部属性：将dialogVisible暴露给父组件
// 父组件可通过ref直接修改该属性控制弹窗显隐
defineExpose({
  dialogVisible
})
</script>

<style scoped>
.instructions-dialog :deep(.el-dialog) {
  background: #ffffff;
  border-radius: 15px;
}

.instructions-dialog :deep(.el-dialog__header) {
  padding: 20px;
  border-bottom: 1px solid #eee;
}

.instructions-dialog :deep(.el-dialog__title) {
  font-size: 24px;
  color: #333;
  font-weight: 600;
}

.instructions-content {
  max-height: 80vh;
  overflow-y: auto;
  padding: 20px;
}

.scroll-content {
  padding: 20px;
}

.section {
  max-width: 1000px;
  margin: 0 auto;
}

.section-header {
  text-align: center;
  margin-bottom: 40px;
}

.section-header h2 {
  color: #333;
  font-size: 28px;
  margin-bottom: 20px;
}

.intro-text {
  color: #666;
  font-size: 16px;
  line-height: 1.6;
}

.subsection {
  margin-bottom: 40px;
}

.subsection h3 {
  color: #409EFF;
  font-size: 22px;
  margin-bottom: 20px;
  padding-bottom: 10px;
  border-bottom: 2px solid #409EFF;
}

.sub-subsection {
  margin-bottom: 30px;
}

.sub-subsection h4 {
  color: #333;
  font-size: 18px;
  margin-bottom: 15px;
}

.sub-subsection p {
  color: #666;
  line-height: 1.6;
  margin-bottom: 15px;
  font-size: 16px;
}

.header-list {
  background: #f5f7fa;
  padding: 20px;
  border-radius: 8px;
  margin: 20px 0;
}

.header-item {
  display: flex;
  align-items: center;
  margin-bottom: 10px;
  color: #333;
}

.header-number {
  width: 30px;
  color: #409EFF;
  font-weight: 600;
}

.example-box {
  margin: 20px 0;
  padding: 15px;
  background: #f5f7fa;
  border-radius: 8px;
}

.example-title {
  display: flex;
  align-items: center;
  margin-bottom: 15px;
  color: #333;
  font-weight: 600;
}

.icon {
  width: 20px;
  height: 20px;
  margin-right: 10px;
}

.example-image {
  max-width: 100%;
  height: auto;
  border-radius: 4px;
  box-shadow: 0 2px 12px rgba(0, 0, 0, 0.1);
}

.error-solutions {
  background: #fef0f0;
  padding: 20px;
  border-radius: 8px;
  margin-top: 15px;
}

.error-item {
  margin-bottom: 15px;
}

.error-item h5 {
  color: #f56c6c;
  font-size: 16px;
  margin-bottom: 8px;
}

.error-item p {
  color: #666;
  margin: 0;
}

.footer-note {
  margin-top: 40px;
  padding: 20px;
  background: #f0f9eb;
  border-radius: 8px;
}

.footer-note p {
  color: #67c23a;
  text-align: center;
  margin: 0;
  font-size: 16px;
  line-height: 1.6;
}

.instructions-content::-webkit-scrollbar {
  width: 6px;
}

.instructions-content::-webkit-scrollbar-thumb {
  background-color: #909399;
  border-radius: 3px;
}

.instructions-content::-webkit-scrollbar-track {
  background-color: #f5f7fa;
}
</style> 