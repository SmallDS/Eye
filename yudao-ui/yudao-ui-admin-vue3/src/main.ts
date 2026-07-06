// 寮曞叆unocss css
import '@/plugins/unocss'

// 瀵煎叆鍏ㄥ眬鐨剆vg鍥炬爣
import '@/plugins/svgIcon'

// 鍒濆鍖栧璇█
import { setupI18n } from '@/plugins/vueI18n'

// 寮曞叆鐘舵€佺鐞?
import { setupStore } from '@/store'

// 鍏ㄥ眬缁勪欢
import { setupGlobCom } from '@/components'

// 寮曞叆 element-plus
import { setupElementPlus } from '@/plugins/elementPlus'

// 寮曞叆 form-create
import { setupFormCreate } from '@/plugins/formCreate'

// 寮曞叆鍏ㄥ眬鏍峰紡
import '@/styles/index.scss'

// 寮曞叆鍔ㄧ敾
import '@/plugins/animate.css'

// 璺敱
import router, { setupRouter } from '@/router'

// 鎸囦护
import { setupAuth, setupMountedFocus } from '@/directives'

import { createApp } from 'vue'

import App from './App.vue'

import './permission'

import '@/plugins/tongji' // 鐧惧害缁熻
import Logger from '@/utils/Logger'

import VueDOMPurifyHTML from 'vue-dompurify-html' // 瑙ｅ喅v-html 鐨勫畨鍏ㄩ殣鎮?

import print from 'vue3-print-nb' // 鎵撳嵃鎻掍欢

// 澶勭悊 Vite 棰勫姞杞芥ā鍧楀け璐ワ紙濡傞噸鏂版瀯寤哄悗 chunk 鍝堝笇鍙樺寲锛夛紝鑷姩鍒锋柊椤甸潰
window.addEventListener('vite:preloadError', (event) => {
  event.preventDefault()
  window.location.reload()
})

// 鍒涘缓瀹炰緥
const setupAll = async () => {
  const app = createApp(App)

  await setupI18n(app)

  setupStore(app)

  setupGlobCom(app)

  setupElementPlus(app)

  setupFormCreate(app)

  setupRouter(app)

  // directives 鎸囦护
  setupAuth(app)
  setupMountedFocus(app)


  await router.isReady()

  app.use(VueDOMPurifyHTML)

  // 鎵撳嵃
  app.use(print)

  app.mount('#app')
}

setupAll()

Logger.prettyPrimary(`娆㈣繋浣跨敤`, import.meta.env.VITE_APP_TITLE)

