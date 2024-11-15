import VeterinaOrders from './VeterinaOrders.jsx'
import { getUserId } from '../src/utils/utils.jsx'
import { fetchVeterinas } from '../src/config/api.jsx'
import React, { useEffect, useState } from 'react';



const VeterinaPage = () => {

    useEffect(() => {
        const VeterinaData = async () => {
            const userID = getUserId();
            const veterinasData = await fetchVeterinas();
            sessionStorage.setItem('vetedata', JSON.stringify(veterinasData.find(vet => vet.userID === userID)));
        }
        VeterinaData();
    },[]);
    return (
        <VeterinaOrders />
    );
}

export default VeterinaPage