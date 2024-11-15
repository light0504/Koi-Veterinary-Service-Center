import { useEffect, useState } from 'react';
import { useLocation } from 'react-router-dom';
import { fetchOrderById, fetchVeterinas, fetchServices } from '../config/api.jsx'; // Fetch functions for data
import "./css/InvoicePage.css";

const InvoicePage = () => {
    const location = useLocation();
    const [orderDetails, setOrderDetails] = useState(null);
    const [veterinas, setVeterinas] = useState([]);
    const [services, setServices] = useState([]);

    // Extract query parameters from the URL
    const queryParams = new URLSearchParams(location.search);
    const invoiceId = queryParams.get('invoiceId');
    const total = queryParams.get('total');
    const invDate = queryParams.get('invDate');
    const orderId = queryParams.get('orderId');

    // Fetch veterina and service data, along with order details
    useEffect(() => {
        const fetchData = async () => {
            const order = await fetchOrderById(orderId);
            const veterinasData = await fetchVeterinas();
            const servicesData = await fetchServices();

            setOrderDetails(order);
            setVeterinas(veterinasData);
            setServices(servicesData);
        };
        fetchData();
    }, [orderId]);

    // Function to get the veterina name by ID
    const getVeterinaName = (veterinaId) => {
        const veterina = veterinas.find((vet) => vet.veterinaID === veterinaId);
        return veterina ? veterina.name : 'Unknown Veterina';
    };

    // Function to get the service name by ID
    const getServiceName = (serviceId) => {
        const service = services.find((srv) => srv.serviceID === serviceId);
        return service ? service.name : 'Unknown Service';
    };

    if (!orderDetails) {
        return <p>Loading...</p>;
    }

    return (
        <div className="invoice-container">
            <h1 className="invoice-header">Invoice Details</h1>
            <div className="invoice-details">
                <p><b>Invoice ID:</b> {invoiceId}</p>
                <p><b>Tổng hóa đơn:</b> {total} đ</p>
                <p><b>Ngày xuất hóa đơn:</b> {invDate}</p>
            </div>

            <h2 className="order-header">Order Information</h2>
            <div className="order-details">
                <p><b>Order ID:</b> {orderDetails.orderId}</p>
                <p><b>Mã khách hàng:</b> {orderDetails.userId}</p>
                <p><b>Tên bác sĩ:</b> {getVeterinaName(orderDetails.veterinaId)}</p>
                <p><b>Ngày đặt đơn:</b> {orderDetails.orderDate}</p>
                <p><b>Slot:</b> {orderDetails.slot}</p>
                <p><b>Địa chỉ khách hàng:</b> {orderDetails.address}</p>

                <h3 className="service-header">Dịch vụ đã đặt</h3>
                <ul className="service-list">
                    {orderDetails.services.map((service) => (
                        <li key={service.serviceID} className="service-item">
                            {getServiceName(service.serviceID)} - Số lượng: {service.quantity}
                        </li>
                    ))}
                </ul>
            </div>
        </div>
    );
};

export default InvoicePage;
