import { useEffect, useState } from 'react';
import { fetchFeedback, fetchOrdersByUser, updateOrderStatus, fetchInvoiceByOrderId } from '../config/api.jsx';
import { getSlots } from '../utils/utils.jsx';
import { ToastContainer, toast } from 'react-toastify';
import { useNavigate } from 'react-router-dom';
import './css/CustomerOrders.css'; // Make sure to import your CSS file here

const CustomerOrders = ({ userID }) => {
    const [orders, setOrders] = useState([]);
    const [feedback, setFeedback] = useState([]);
    const [slots, setSlots] = useState([]);
    const [feedbackOrders, setFeedbackOrders] = useState(new Set());
    const navigate = useNavigate();

    useEffect(() => {
        const getData = async () => {
            const fetchedOrders = await fetchOrdersByUser(userID);
            const filteredOrders = fetchedOrders.filter(order => order.status !== 'cancel');
            setOrders(filteredOrders);

            const fetchedFeedback = await fetchFeedback();
            setFeedback(fetchedFeedback);

            setSlots(getSlots());

            const feedbackSet = new Set();
            for (let order of filteredOrders) {
                if (fetchedFeedback.some((fb) => fb.orderId === order.orderId)) {
                    feedbackSet.add(order.orderId);
                }
            }
            setFeedbackOrders(feedbackSet);
        };

        getData();
    }, [userID]);

    const handleCancelOrder = async (orderId) => {
        const confirmCancel = window.confirm("Bạn có muốn hủy lịch hẹn này?");
        if (!confirmCancel) return;

        try {
            await updateOrderStatus(orderId, 'cancel');
            const updatedOrders = await fetchOrdersByUser(userID);
            const filteredOrders = updatedOrders.filter(order => order.status !== 'cancel');
            setOrders(filteredOrders);
            toast.success('Hủy thành công');
        } catch (error) {
            toast.error('Lỗi khi hủy lịch hẹn!');
        }
    };

    const handleGiveFeedback = (orderId) => {
        navigate(`/feedback/${orderId}`);
    };

    const orderPriority = ["accept", "pending", "done"];

    return (
        <section className="customer-orders">
            <ToastContainer />
            <h2 className="orders-title">Danh sách đặt lịch</h2>
            <table className="orders-table">
                <thead>
                    <tr>
                        <th>Mã đơn hàng</th>
                        <th>Bác sĩ</th>
                        <th>Ngày đặt lịch</th>
                        <th>Slot</th>
                        <th>Địa chỉ đặt lịch </th>
                        <th>Trạng thái</th>
                        <th>Hành động</th>
                    </tr>
                </thead>
                <tbody>
                    {orders
                        .sort((a, b) => orderPriority.indexOf(a.status) - orderPriority.indexOf(b.status))
                        .map((order) => (
                            <tr key={order.orderId}>
                                <td>{order.orderId}</td>
                                <td>{order.veterinaId}</td>
                                <td>{order.orderDate}</td>
                                <td>{order.slot}</td>
                                <td>{order.address}</td>
                                <td>{order.status}</td>
                                <td>
                                    {order.status === 'pending' ? (
                                        <button className="cancel-button" onClick={() => handleCancelOrder(order.orderId)}>Hủy Lịch</button>
                                    ) : order.status === 'accept' ? (
                                        <p className="accepted-message">Lịch hẹn đã được duyệt.</p>
                                    ) : feedbackOrders.has(order.orderId) ? (
                                        <p className="feedback-thank-you">Cảm ơn quý khách đã góp ý.</p>
                                    ) : (
                                        <button className="feedback-button" onClick={() => handleGiveFeedback(order.orderId)}>Góp ý</button>
                                    )}
                                </td>
                            </tr>
                        ))}
                </tbody>
            </table>
        </section>
    );
};

export default CustomerOrders;
