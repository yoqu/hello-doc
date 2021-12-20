import axios from 'axios'
import iView from 'view-design';

// import Qs from 'qs'
// 创建axios实例
const service = axios.create({
  timeout: 60000 // 请求超时时间
})

// request拦截器
service.interceptors.request.use(config => {
  iView.LoadingBar.start();
  return config
}, error => {
  // Do something with request error
  console.log(error) // for debug
  Promise.reject(error)
  iView.LoadingBar.finish();
})


// respone拦截器
service.interceptors.response.use(
  response => {
    iView.LoadingBar.finish();
    return response.data
  },
  error => {
    iView.LoadingBar.finish();
    console.log(error)
    let response = error.response;
    if (!error.response) {
      return console.log('Error', error.message);
    }
    return Promise.reject(error)
  }
)

export default service
