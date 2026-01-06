import axios from 'axios';

const instance = axios.create({
  // baseURL: 'http://localhost:8084/api', // 测试
  baseURL: 'http://172.18.26.173:8080/api', // 生产
  headers: {
    'Content-Type': 'application/json',
  },
  timeout: 1000,
});

export default instance;