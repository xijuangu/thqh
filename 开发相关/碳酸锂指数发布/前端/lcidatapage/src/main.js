import { createApp } from 'vue' // 导入Vue核心创建函数
import App from './App.vue' // 导入根组件
import ElementPlus from 'element-plus' // 导入ElementPlus UI组件库核心
import 'element-plus/dist/index.css' // 导入ElementPlus UI组件库样式
import * as ElementPlusIconsVue from '@element-plus/icons-vue' // 导入ElementPlus UI组件库图标库

// 创建Vue实例，传入根组件生成应用实例
const app = createApp(App)

// 全局注册ElementPlus UI所有图标组件，遍历图标对象，逐个注册
for (const [key, component] of Object.entries(ElementPlusIconsVue)) {
  app.component(key, component)
}

app.use(ElementPlus)
app.mount('#app')
