import { useEffect, useState } from 'react';
import { ToastContainer, toast } from 'react-toastify'; // Importing react-toastify
import 'react-toastify/dist/ReactToastify.css'; // npm install react-toastify
import { updateOrderStatus, fetchSlots, fetchVeterinas, fetchOrdersInSelectedSlot, fetchServices, addOrderVeterina } from '../src/config/api.jsx';

const Schedule = () => {
    const [slots, setSlots] = useState([]);
    const [veterinas, setVeterinas] = useState([]);
    const [services, setServices] = useState([]);

    const [availableSlots, setAvailableSlots] = useState({});
    const [orderInSlot, setOrderInSlot] = useState()

    useEffect(() => {
        const loadData = async () => {
            const slotsData = await fetchSlots();
            const veterinasData = await fetchVeterinas();
            const servicesData = await fetchServices();

            setSlots(slotsData);
            setVeterinas(veterinasData.filter(veterina => veterina.status === true));
            setServices(servicesData);
        };

        loadData();
    }, []);

    useEffect(() => {
        const checkSlotAvailability = async () => {
            const slotAvailability = {};

            for (const day of getNext7Days()) {
                const formattedDate = day.toISOString().split('T')[0];
                slotAvailability[formattedDate] = {};

                for (const slot of slots) {
                    const orders = await fetchOrdersInSelectedSlot(formattedDate, slot.slot);
                    const activeOrders = orders.filter(order => order.status !== 'cancel');

                    slotAvailability[formattedDate][slot.slot] = activeOrders;
                }
            }
            setAvailableSlots(slotAvailability);
        };

        checkSlotAvailability();
    }, [slots, veterinas]);

    const getNext7Days = () => {
        const days = [];
        const today = new Date();
        for (let i = 0; i < 7; i++) {
            const day = new Date(today);
            day.setDate(today.getDate() + i);
            days.push(day);
        }
        return days;
    };

    const handleSlotSelection = (date, slot) => {
        const formattedDate = date.toISOString().split('T')[0];
        const order = availableSlots[formattedDate]?.[slot.slot] || [];
        setOrderInSlot(order);
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

    const handleSelectVeterina = async (orderId, veterinaId) => {
        const response = await addOrderVeterina(orderId, veterinaId);
        if (response) {
            toast.success('Veterina added successfully!');
            setTimeout(() => window.location.reload(), 2000);
        } else {
            toast.error('Failed to add veterina');
        }
    };

    const getServiceNameById = (serviceID) => {
        const service = services.find((s) => s.serviceID === serviceID);
        return service ? service.name : serviceID;
    };

    const isVeterinaAvailable = (veterinaId, orderDate, orderSlot) => {
        const veterinaOrders = orderInSlot.filter(
            (order) => order.veterinaId === veterinaId && order.orderDate === orderDate && order.slot === orderSlot
        );
        return veterinaOrders.length === 0;
    };

    const getVeterinaName = (veterinaId) => {
        const veterina = veterinas.find((vet) => vet.veterinaID === veterinaId);
        return veterina ? veterina.name : 'Unknown Veterina';
    };

    return (
        <section>
            <h1>Lịch làm việc</h1>
            <ToastContainer/>
            <div className="slot-table-container">
                <table className="slot-table" border="1">
                    <thead>
                        <tr>
                            <th>Slot</th>
                            {getNext7Days().map((day) => (
                                <th key={day}>
                                    {day.toLocaleDateString("vi-VN", {
                                        weekday: "short",
                                        day: "numeric",
                                        month: "numeric",
                                    })}
                                </th>
                            ))}
                        </tr>
                    </thead>
                    <tbody>
                        {slots.map((slot) => (
                            <tr key={slot.slot}>
                                <td>
                                    {slot.startTime} - {slot.endTime}
                                </td>
                                {getNext7Days().map((day) => {
                                    const formattedDate = day.toISOString().split('T')[0];
                                    const order = availableSlots[formattedDate]?.[slot.slot] || [];
                                    const isDisabled = order.length === 0;

                                    return (
                                        <td key={day}>
                                            <button
                                                className={`select-slot-button ${isDisabled ? 'disabled-button' : ''}`}
                                                style={{ backgroundColor: isDisabled ? 'red' : '' }}
                                                onClick={() => !isDisabled && handleSlotSelection(day, slot)}
                                                disabled={isDisabled}
                                            >
                                                {isDisabled ? 'Trống lịch' : order.length}
                                            </button>
                                        </td>
                                    );
                                })}
                            </tr>
                        ))}
                    </tbody>
                </table>
            </div>
            {orderInSlot && (
                <>
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
                            {orderInSlot.map((order) => (
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
                                        <td><button onClick={() => handleCancel(order.orderId)} className="cancel-button">Hủy đơn</button></td>
                                    ) : (
                                        <td></td>
                                    )}
                                </tr>
                            ))}
                        </tbody>
                    </table>

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
                </>
            )}
        </section>
    );
}
export default Schedule