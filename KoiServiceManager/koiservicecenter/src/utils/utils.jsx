import { jwtDecode } from 'jwt-decode';
import { fetchServices, fetchVeterinas, fetchSlots } from '../config/api.jsx';

export const getUserId = () => {
    try {
        const userId = jwtDecode(sessionStorage.getItem('user')).sub;
        return userId;
    } catch (e) {
        console.log(e)
    }
    
}


export const getSlots = async () => {
    return await fetchSlots();
}

export const getVeterina = async () => {
    return await fetchVeterinas();
}

export const getService = async () => {
    return await fetchServices();
}