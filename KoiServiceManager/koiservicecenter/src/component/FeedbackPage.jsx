import { useEffect, useState } from 'react';
import { fetchOrderById, fetchServices, createFeedBack } from '../config/api.jsx';
import { useParams, useNavigate } from 'react-router-dom';
import StarRating from './StarRating.jsx';  // Import the star rating component

const FeedbackPage = () => {
    const { orderId } = useParams();
    const [order, setOrder] = useState(null);
    const [services, setServices] = useState([]);
    const [feedback, setFeedback] = useState({ comment: '', rating: 5 });
    const navigate = useNavigate();

    useEffect(() => {
        const getOrderAndInvoice = async () => {
            const fetchedOrder = await fetchOrderById(orderId);
            setOrder(fetchedOrder);

            const servicesData = await fetchServices();
            setServices(servicesData);
        };
        getOrderAndInvoice();
    }, [orderId]);

    const getServiceNameById = (serviceID) => {
        const service = services.find(s => s.serviceID === serviceID);
        return service ? service.name : serviceID;
    };

    const handleSubmitFeedback = async () => {
        try {
            const response = await createFeedBack({
                comment: feedback.comment,
                rating: feedback.rating,
                orderId
            });
            if (response) {
                alert('Feedback đã gửi thành công!');
                navigate('/profile');
            } else {
                alert('Có lỗi khi gửi feedback!')
            }
        } catch (error) {
            console.error('Failed to submit feedback:', error);
        }
    };

    const handleInputChange = (e) => {
        const { name, value } = e.target;
        setFeedback({ ...feedback, [name]: value });
    };

    const feedbackPageStyle = {
        fontFamily: 'Arial, sans-serif',
        padding: '20px',
        marginTop: '20px',
        backgroundColor: '#f9f9f9',
        borderRadius: '8px',
        maxWidth: '800px',
        margin: 'auto',
    };

    const titleStyle = {
        fontSize: '24px',
        fontWeight: 'bold',
        marginBottom: '15px',
        color: '#333',
    };

    const tableStyle = {
        width: '100%',
        borderCollapse: 'collapse',
        marginBottom: '20px',
        borderRadius: '4px',
        boxShadow: '0 2px 4px rgba(0, 0, 0, 0.1)',
    };

    const tableHeaderStyle = {
        backgroundColor: '#3498db',
        color: '#fff',
        textAlign: 'left',
        padding: '8px',
    };

    const tableDataStyle = {
        padding: '8px',
        borderBottom: '1px solid #ddd',
    };

    const feedbackTextAreaStyle = {
        width: '100%',
        height: '100px',
        padding: '10px',
        borderRadius: '4px',
        border: '1px solid #ccc',
        fontSize: '14px',
        marginBottom: '20px',
        resize: 'vertical',
    };

    const submitButtonStyle = {
        backgroundColor: '#3498db',
        color: 'white',
        padding: '12px 20px',
        border: 'none',
        borderRadius: '5px',
        cursor: 'pointer',
        fontSize: '16px',
    };

    const submitButtonHoverStyle = {
        backgroundColor: '#2980b9',
    };

    return (
        <div style={feedbackPageStyle}>
            {order ? (
                <>
                    <h2 style={titleStyle}>Feedback for Order</h2>
                    <p><strong>Ngày đặt dịch vụ:</strong> {order.orderDate}</p>
                    <p><strong>Địa chỉ:</strong> {order.address}</p>
                    <p><strong>Dịch vụ:</strong></p>
                    <table style={tableStyle}>
                        <thead>
                            <tr>
                                <th style={tableHeaderStyle}>Tên dịch vụ</th>
                                <th style={tableHeaderStyle}>Số lượng</th>
                            </tr>
                        </thead>
                        <tbody>
                            {order.services.map(service => (
                                <tr key={service.serviceID}>
                                    <td style={tableDataStyle}>{getServiceNameById(service.serviceID)}</td>
                                    <td style={tableDataStyle}>{service.quantity}</td>
                                </tr>
                            ))}
                        </tbody>
                    </table>

                    <h3>Phản hồi</h3>
                    <textarea
                        name="comment"
                        value={feedback.comment}
                        placeholder="Viết phản hồi của bạn ở đây"
                        onChange={handleInputChange}
                        style={feedbackTextAreaStyle}
                    />
                    <br />
                    <StarRating
                        rating={feedback.rating}
                        setRating={(newRating) => setFeedback({ ...feedback, rating: newRating })}
                        label="Rating:"
                    />
                    <br />
                    <button
                        onClick={handleSubmitFeedback}
                        style={submitButtonStyle}
                        onMouseOver={(e) => e.target.style.backgroundColor = submitButtonHoverStyle.backgroundColor}
                        onMouseOut={(e) => e.target.style.backgroundColor = submitButtonStyle.backgroundColor}
                    >
                        Submit Feedback
                    </button>
                </>
            ) : (
                <p>Loading order details...</p>
            )}
        </div>
    );
};

export default FeedbackPage;
