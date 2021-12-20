// The Vue build version to load with the `import` command
// (runtime-only or standalone) has been set in webpack.base.conf with an alias.
import Vue from 'vue'
import VueResource from 'vue-resource'
import App from './App'
import router from './router'
import ViewUI from 'view-design';
import 'view-design/dist/styles/iview.css';    // 使用 CSS
import Vuex from 'vuex'
Vue.use(VueResource);
Vue.use(ViewUI);
Vue.config.productionTip = false
Vue.use(Vuex);
const store = new Vuex.Store({
  state: {
    basicInfo: {},
    config: {},
    dto:{},
    service:{},
    firstLoadService:true,//用于修复重复复制的问题
  },
  mutations: {
    setBasicInfo(state, basicInfo) {
      state.basicInfo = basicInfo
    },
    setConfig(state, config) {
      state.config = config
    },
    setDto(state,dto){
      state.dto = dto
    },
    firstLoadService(state,fisrtLoad){
      state.firstLoadService =   fisrtLoad
    },
    setService(state,service){
      state.service = service
    }
  }
})

// // 路由处理全局LoadingBar进度条
// router.beforeEach((to, from, next) => {
//   ViewUI.LoadingBar.start();
//   console.log("start init router.")
//   if (window.vue == null) {
//     getBasicDefinition("").then(response => {
//       window.vue.$store.commit("setBasicInfo", response);
//       document.title = response.basic.name;
//       console.log("init all configuration")
//       next()
//     }).catch(err => {
//       console.log(err)
//       next()
//     });
//   } else {
//     next();
//   }

// });

router.afterEach(route => {
  ViewUI.LoadingBar.finish();
});

/* eslint-disable no-new */
let vue = new Vue({
  el: '#app',
  router,
  store,
  template: '<App/>',
  components: { App }
})

window.vue = vue;


// vue-resource拦截器处理处理全局LoadingBar进度条
Vue.http.interceptor.before = function (request, next) {
  ViewUI.LoadingBar.start();
  next();
};

Vue.http.interceptors.push(function (request, next) {
  // continue to next interceptor
  next(function (response) {
    ViewUI.LoadingBar.finish();
  });
});

if (!Array.prototype.filter) {
  Array.prototype.filter = function (fun /*, thisp */) {
    "use strict";
    if (this === void 0 || this === null)
      throw new TypeError();
    var t = Object(this);
    var len = t.length >>> 0;
    if (typeof fun !== "function")
      throw new TypeError();
    var res = [];
    var thisp = arguments[1];
    for (var i = 0; i < len; i++) {
      if (i in t) {
        var val = t[i]; // in case fun mutates this
        if (fun.call(thisp, val, i, t))
          res.push(val);
      }
    }

    return res;
  };
}
