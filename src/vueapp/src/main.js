import Vue from 'vue'
import App from './App.vue'

import VueRouter from 'vue-router';

import VueMaterial from 'vue-material'

import 'vue-material/dist/vue-material.min.css'
import 'vue-material/dist/theme/default.css'

import AuthComp from "./components/AuthComp";
import LingoComp from "./components/LingoComp";

Vue.use(VueMaterial)
Vue.use(VueRouter)

const routes = [
  { path: '/', component: AuthComp },
  { path: '/game', component: LingoComp },
  { path: '/auth', component: AuthComp }
]
const router = new VueRouter({
  routes
})

Vue.config.productionTip = false

new Vue({
  render: h => h(App),
  router
}).$mount('#app')
