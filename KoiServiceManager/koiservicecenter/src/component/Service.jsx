import { useEffect, useState } from 'react';
import { fetchServices } from '../config/api.jsx';
import './css/Service.css'

const CustomerServices = () => {
    const [dichVu, setDichVu] = useState([]);

    useEffect(() => {
        const loadServices = async () => {
            try {
                const services = await fetchServices();
                setDichVu(services);
            } catch (error) {
                console.error("Error fetching services:", error);
            }
        };
        loadServices();
    }, []);

    return (
        <div className="service-container">
            <h1 className="service-title">Dịch Vụ Cá Koi</h1>

            <table className="service-table">
                <thead>
                    <tr>
                        <th>Tên Dịch Vụ</th>
                        <th>Loại</th>
                        <th>Giá (VND)</th>
                    </tr>
                </thead>
                <tbody>
                    {dichVu.length > 0 ? (
                        dichVu.sort((dv)=>(dv.name)).filter(dv => dv.type !== 'Thuốc').map((dv) => (
                                <tr key={dv.serviceID}>
                                    <td>{dv.name}</td>
                                    <td>{dv.type}</td>
                                    <td>{dv.price.toLocaleString('vi-VN')} VND</td>
                                </tr>
                            ))
                    ) : (
                        <tr>
                            <td colSpan="3" className="service-no-data">Không có dịch vụ nào.</td>
                        </tr>
                    )}
                </tbody>
            </table>
        </div>
    );
};

export default CustomerServices;
