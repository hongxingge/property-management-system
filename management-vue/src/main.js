import Vue from 'vue'
import App from './App.vue'
import router from './router'
import ElementUI from 'element-ui'
import './assets/css/global.css'
import 'element-ui/lib/theme-chalk/index.css'
import md5 from 'js-md5';

Vue.use(ElementUI)
Vue.config.productionTip = false
Vue.prototype.$md5 = md5

new Vue({
  router,
  render: h => h(App),
}).$mount('#app')
