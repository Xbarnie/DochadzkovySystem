import Vue from 'vue'
import App from './App.vue'
import router from './router'
import store from './store'
import axios from "axios"
import Vuex from "vuex"
import i18n from "./i18n"
import vuetify from './plugins/vuetify';
import VueSweetalert2 from 'vue-sweetalert2';
import VueJwtDecode  from 'vue-jwt-decode';
require('./assets/css/style.css');

Vue.config.productionTip = false;

const serviceUrl = process.env.VUE_APP_SERVICE_URL;
console.log("VUE_APP_SERVICE_URL = " + serviceUrl);

Vue.prototype.$serviceUrl = serviceUrl;
axios.defaults.withCredentials = false;

axios.defaults.baseURL = serviceUrl;
Vue.prototype.$axios = axios;

Vuex.Store.prototype.$axios = axios;

Vue.use(require('vue-moment'));

Vue.use(VueSweetalert2);
Vue.use(require('vue-moment'));


Vue.use(VueJwtDecode);

new Vue({
  router,
  store,
  vuetify,
  i18n,
  render: h => h(App)
}).$mount('#app')
