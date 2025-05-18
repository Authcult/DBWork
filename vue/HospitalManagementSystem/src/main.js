import './assets/main.css'

import { createApp } from 'vue'
import App from './App.vue'
import ElementPlus from 'element-plus'
import 'element-plus/dist/index.css'
import router from './router' // 导入 router
import zhCn from 'element-plus/es/locale/lang/zh-cn' // 导入中文语言包
import * as ElementPlusIconsVue from '@element-plus/icons-vue' // 美化图标的包

const app = createApp(App)

for (const [key, component] of Object.entries(ElementPlusIconsVue)) {
    app.component(key, component)
  }

app.use(ElementPlus,{locale: zhCn})
app.use(router) // 使用 router
app.mount('#app')

