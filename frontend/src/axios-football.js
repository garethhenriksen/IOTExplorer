import axios from "axios";

const instance = axios.create({
  baseURL: "http://localhost:8080"
  
});

instance.interceptors.request.use(config => {
  return config;
}, error => {
  return Promise.reject(error);
});

instance.interceptors.response.use(response => {
  return response;
}, error => {
  return Promise.reject(error);
});

export default instance;
