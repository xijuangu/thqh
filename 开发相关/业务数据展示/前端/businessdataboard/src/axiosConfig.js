import axios from 'axios';

const instance = axios.create({
  baseURL: 'http://localhost:8080/api', // 本地环境
  // baseURL: 'http://172.16.10.176:8080/api', // 测试环境
  // baseURL: 'http://172.18.26.59:8080/api', // 生产环境
  headers: {
    'Content-Type': 'application/json'
  },
  timeout: 10000
});

export default instance;