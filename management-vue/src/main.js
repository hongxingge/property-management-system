import { createApp } from 'vue'
import App from './App.vue'
import router from './router'
import ElementPlus from 'element-plus'
import 'element-plus/dist/index.css'
import './assets/css/global.css'
import md5 from 'js-md5'
import { ElMessage } from 'element-plus'

const app = createApp(App)

app.use(ElementPlus)
app.use(router)
app.config.globalProperties.$md5 = md5
// 做法 A：全局挂载 ElMessage，让组件里的 this.$message 继续可用
app.config.globalProperties.$message = ElMessage

app.mount('#app')
