import { useEffect, useState } from 'react';
import { fetchOrders, fetchVeterinas, addOrderVeterina, fetchServices, updateOrderStatus } from '../src/config/api.jsx';
import { ToastContainer, toast } from 'react-toastify';
import 'react-toastify/dist/ReactToastify.css';

const OrderManagement = () => {
    const [orders, setOrders] = useState([]);
    const [veterinas, setVeterinas] = useState([]);
    const [filteredOrders, setFilteredOrders] = useState([]);
    const [services, setServices] = useState([]);

    const [currentPage, setCurrentPage] = useState(() => {
        return parseInt(sessionStorage.getItem('currentPage')) || 1;
    });

    const ordersPerPage = 10;

    useEffect(() => {

        const fetchData = async () => {
            const ordersData = await fetchOrders();
            const veterinasData = await fetchVeterinas();
            const servicesData = await fetchServices();
            setServices(servicesData);
            setOrders(ordersData);
            setVeterinas(veterinasData.filter((vet) => vet.status === true));
        };

        fetchData();
    }, []); // Added currentStatus to dependencies

    const filterOrdersByStatus = (status) => {
        const filtered = orders.filter((order) => order.status === status);
        setFilteredOrders(filtered);
        setCurrentPage(1); // Reset to page 1 on status change
        sessionStorage.setItem('currentStatus', status); // Save status in sessionStorage
    };

    const paginate = (pageNumber) => {
        setCurrentPage(pageNumber);
        sessionStorage.setItem('currentPage', pageNumber);
    };

    const getServiceNameById = (serviceID) => {
        const service = services.find((s) => s.serviceID === serviceID);
        return service ? service.name : serviceID;
    };

    const isVeterinaAvailable = (veterinaId, orderDate, orderSlot) => {
        const veterinaOrders = orders.filter(
            (order) => order.veterinaId === veterinaId && order.orderDate === orderDate && order.slot === orderSlot
        );
        return veterinaOrders.length === 0;
    };

    const handleSelectVeterina = async (orderId, veterinaId) => {
        const response = await addOrderVeterina(orderId, veterinaId);
        if (response) {
            toast.success('Veterina added successfully!');
            setTimeout(() => window.location.reload(), 2000);
        } else {
            toast.error('Failed to add veterina');
        }
    };

    const handleCancel = async (orderID) => {
        const response = await updateOrderStatus(orderID, 'cancel');
        if (response) {
            toast.success('Order canceled successfully');
            setTimeout(() => window.location.reload(), 2000);
        } else {
            toast.error('Failed to cancel order');
        }
    };

    const sortedOrders = filteredOrders
        .sort((a, b) => new Date(b.orderDate) - new Date(a.orderDate))
        .sort((a) => (a.veterinaId === null ? -1 : 1));

    const indexOfLastOrder = currentPage * ordersPerPage;
    const indexOfFirstOrder = indexOfLastOrder - ordersPerPage;
    const currentOrders = sortedOrders.slice(indexOfFirstOrder, indexOfLastOrder);

    const totalPages = Math.ceil(sortedOrders.length / ordersPerPage);

    const renderPagination = () => {
        const pages = [];

        if (currentPage > 1) {
            pages.push(
                <span key="prev" onClick={() => paginate(currentPage - 1)} className="pagination-item">
                    « Prev
                </span>
            );
        }

        pages.push(
            <span key="1" onClick={() => paginate(1)} className="pagination-item">
                1
            </span>
        );

        if (currentPage > 3) {
            pages.push(<span key="start-ellipsis" className="pagination-item">...</span>);
        }

        if (currentPage > 1 && currentPage < totalPages) {
            pages.push(
                <span key="current" className="pagination-item current-page">
                    {currentPage}
                </span>
            );
        }

        if (currentPage < totalPages - 2) {
            pages.push(<span key="end-ellipsis" className="pagination-item">...</span>);
        }

        if (totalPages > 1) {
            pages.push(
                <span
                    key={totalPages}
                    onClick={() => paginate(totalPages)}
                    className="pagination-item"
                >
                    {totalPages}
                </span>
            );
        }

        if (currentPage < totalPages) {
            pages.push(
                <span key="next" onClick={() => paginate(currentPage + 1)} className="pagination-item">
                    Next »
                </span>
            );
        }

        return <div className="pagination">{pages}</div>;
    };

    const getVeterinaName = (veterinaId) => {
        const veterina = veterinas.find((vet) => vet.veterinaID === veterinaId);
        return veterina ? veterina.name : 'Unknown Veterina';
    };

    return (
        <div className="order-management">
            <h1>Order Management</h1>
            <ToastContainer />
            <div className="filter-buttons">
                <button onClick={() => filterOrdersByStatus('pending')}>Pending</button>
                <button onClick={() => filterOrdersByStatus('accept')}>Accepted</button>
                <button onClick={() => filterOrdersByStatus('done')}>Completed</button>
                <button onClick={() => filterOrdersByStatus('cancel')}>Canceled</button>
            </div>

            <table className="order-table">
                <thead>
                    <tr>
                        <th>Mã đơn</th>
                        <th>Mã khách hàng</th>
                        <th>Bác sĩ</th>
                        <th>Ngày đặt lịch</th>
                        <th>Slot</th>
                        <th>Địa chỉ đặt lịch</th>
                        <th>Services</th>
                        <th>Ghi chú</th>
                        <th>Action</th>
                    </tr>
                </thead>
                <tbody>
                    {currentOrders.map((order) => (
                        <tr key={order.orderId}>
                            <td>{order.orderId}</td>
                            <td>{order.userId}</td>
                            <td>
                                {order.status === 'cancel' || order.status === 'done' ? (
                                    veterinas.find((vet) => vet.veterinaID === order.veterinaId)?.name || 'Unknown'
                                ) : (
                                    <select onChange={(e) => handleSelectVeterina(order.orderId, e.target.value)} className="veterina-select">
                                        <option value={order.veterinaId !== null ? order.veterinaId : ''}>
                                            {order.veterinaId ? getVeterinaName(order.veterinaId) : 'Chọn bác sĩ'}
                                        </option>
                                        {veterinas
                                            .filter((vet) =>
                                                isVeterinaAvailable(vet.veterinaID, order.orderDate, order.slot)
                                            )
                                            .map((vet) => (
                                                <option key={vet.veterinaID} value={vet.veterinaID}>
                                                    {vet.name}
                                                </option>
                                            ))}
                                    </select>
                                )}
                            </td>
                            <td>{order.orderDate}</td>
                            <td>{order.slot}</td>
                            <td>{order.address}</td>
                            <td>
                                {order.services.map((service) => (
                                    <div key={service.serviceID}>
                                        <p>{getServiceNameById(service.serviceID)} - Qty: {service.quantity}</p>
                                    </div>
                                ))}
                            </td>
                            <td>{order.description}</td>
                            {order.status === 'pending' ? (
                                <td><button onClick={() => handleCancel(order.orderId)} className="cancel-button">Hủy đơn này</button></td>
                            ) : (
                                <td></td>
                            )}
                        </tr>
                    ))}
                </tbody>
            </table>

            <div className="pagination">
                {renderPagination()}
            </div>

            <style>{`
                .order-management {
                    font-family: Arial, sans-serif;
                    padding: 20px;
                    max-width: 1200px;
                    margin: auto;
                }

                h1 {
                    text-align: center;
                }

                .filter-buttons {
                    display: flex;
                    justify-content: center;
                    margin-bottom: 20px;
                }

                button {
                    margin: 0 5px;
                    padding: 10px 15px;
                    border: none;
                    background-color: #4CAF50;
                    color: white;
                    cursor: pointer;
                    border-radius: 5px;
                }

                button:hover {
                    background-color: #45a049;
                }

                .order-table {
                    width: 100%;
                    border-collapse: collapse;
                    margin-bottom: 20px;
                }

                th, td {
                    border: 1px solid #ddd;
                    padding: 10px;
                    text-align: left;
                }

                th {
                    background-color: #f2f2f2;
                }

                .cancel-button {
                    background-color: #f44336;
                    color: white;
                    border: none;
                    padding: 5px 10px;
                    border-radius: 5px;
                    cursor: pointer;
                }

                .cancel-button:hover {
                    background-color: #d32f2f;
                }

                .pagination {
                    display: flex;
                    justify-content: center;
                    margin-top: 20px;
                }

                .pagination-item {
                    margin: 0 5px;
                    cursor: pointer;
                }

                .current-page {
                    font-weight: bold;
                    color: #4CAF50;
                }

                .veterina-select {
                    padding: 5px;
                    border-radius: 5px;
                    border: 1px solid #ddd;
                }

            `}</style>
        </div>
    );
};

export default OrderManagement;
