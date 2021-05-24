import Vue from 'vue'
import Vuex from 'vuex'
import VueI18n  from '../i18n.js'

Vue.use(Vuex)

export default new Vuex.Store({
  state: {
    isAdministration: false,
    toolbar: null,
    user: null,
  },
  mutations: {
    set_user(store, data) {
      store.user = data
    },
    set_toolbar(store, text) {
      store.toolbar = text
    }
  },
  actions: {
    setAdministration({state}, administration) {
      state.isAdministration = administration;
    },
    setToolbar( {commit}, text) {
      commit('set_toolbar', VueI18n.t(text));
    },
    setUser({commit}, user) {
      commit('set_user', user);
      return user;
    }
  },
  modules: {
  }
})
