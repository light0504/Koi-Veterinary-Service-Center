import axios from 'axios';

const BASE_URL = 'https://localhost:8080/api';

const config = {
    baseUrl: BASE_URL,
};

const api = axios.create(config);

api.defaults.baseURL = BASE_URL;

export default api;