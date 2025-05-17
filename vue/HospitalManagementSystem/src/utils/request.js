import axios from "axios";

// 创建axios实例
const request = axios.create({
    baseURL:'http://localhost:8080',
    headers:{
        'Content-Type':'application/json',
    }
});

// 请求拦截器：自动添加 Authorization
request.interceptors.request.use(config => {
        const token = localStorage.getItem('token'); // 从 localStorage 获取 token
        if (token) {
            config.headers.Authorization = `Bearer ${token}`; // 将 token 添加到请求头中
        }
        return config;
    },error => {return Promise.reject(error);}
);

// 响应拦截器：处理token过期
request.interceptors.response.use(
    response => {
        if (!response.data.success) {
            return Promise.reject(response.data.error || response.data.message);
          }
          return response.data;
        },
        error => {
          const msg = error.response?.data?.error?.message || error.message || '请求错误';
          return Promise.reject(new Error(msg));
        }
);

export default request;