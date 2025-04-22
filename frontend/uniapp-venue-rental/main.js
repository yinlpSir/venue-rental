import App from './App'
import '@/utils/interceptor.js'

// #ifndef VUE3
import Vue from 'vue'
import './uni.promisify.adaptor'

Vue.config.productionTip = false
App.mpType = 'app'
const app = new Vue({
  ...App
})

app.$mount()
// #endif

/**
 * Pinia 配置参考uniapp官网文档：https://zh.uniapp.dcloud.io/tutorial/vue3-pinia.html
 */

// #ifdef VUE3
import { createSSRApp } from 'vue'
import uviewPlus from 'uview-plus'
import * as Pinia from 'pinia'
// import Antd from 'ant-design-vue'
// import 'ant-design-vue/dist/antd.css';

export function createApp() {
  const app = createSSRApp(App)
  app.use(uviewPlus)
  app.use(Pinia.createPinia())

  return {
    app,
	Pinia // 此处必须将 Pinia 返回
  }
}
// #endif