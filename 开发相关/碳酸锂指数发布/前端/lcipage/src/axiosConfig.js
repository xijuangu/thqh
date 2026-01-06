import axios from 'axios';

// 创建一个 axios 实例
const instance = axios.create({
    // 设置全局的 URL 前缀
    baseURL: 'http://localhost:8083',
    timeout: 10000 // 设置请求超时时间
});

export default instance;