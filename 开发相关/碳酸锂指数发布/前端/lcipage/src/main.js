import { createApp } from 'vue'
import App from './App.vue'

//引入路由
import VueRouter from './router'

//引入echarts
import VChart from 'vue-echarts'
import * as echarts from 'echarts'

//引入reset.css
import 'reset-css'

const app = createApp(App)
app.use(VueRouter)
app.component('v-chart', VChart)
app.use('ApexChart', echarts)



app.mount('#app')