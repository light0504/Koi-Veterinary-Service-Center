import { useState, useEffect, useRef } from 'react';
import { fetchOrders, fetchInvoices } from '../src/config/api.jsx';

function Dashboard() {
    const [orders, setOrders] = useState([]);
    const [invoices, setInvoices] = useState([]);
    const [timeFrame, setTimeFrame] = useState('all');
    const [filteredOrders, setFilteredOrders] = useState([]);
    const [hoveredData, setHoveredData] = useState(null); // Track hovered data
    const dayChartRef = useRef(null);
    const monthChartRef = useRef(null);

    useEffect(() => {
        fetchOrders().then((data) => {
            setOrders(data);
            filterOrders(data, timeFrame);
        });
        fetchInvoices().then((data) => {
            setInvoices(data);
        });
    }, []);

    const filterOrders = (orders, frame) => {
        const now = new Date();
        let filtered;
        if (frame === 'week') {
            filtered = orders.filter(order => new Date(order.orderDate) >= new Date(now.setDate(now.getDate() - 7)));
        } else if (frame === 'month') {
            filtered = orders.filter(order => new Date(order.orderDate) >= new Date(now.setMonth(now.getMonth() - 1)));
        } else {
            filtered = orders;
        }
        setFilteredOrders(filtered);
    };

    useEffect(() => {
        filterOrders(orders, timeFrame);
    }, [timeFrame, orders]);

    const totalRevenue = invoices.reduce((sum, invoice) => sum + invoice.total, 0);

    const paymentRatio = () => {
        const onlinePayments = invoices.filter(invoice => invoice.method === 'online').length;
        const cashPayments = invoices.filter(invoice => invoice.method === 'cash').length;
        const totalPayments = onlinePayments + cashPayments;
        return {
            onlinePercentage: totalPayments ? ((onlinePayments / totalPayments) * 100).toFixed(2) : 0,
            cashPercentage: totalPayments ? ((cashPayments / totalPayments) * 100).toFixed(2) : 0,
        };
    };

    const completedOrdersCount = filteredOrders.filter(order => order.status === 'done').length;

    const orderCountByStatus = (status) => filteredOrders.filter(order => order.status === status).length;
    const totalOrders = filteredOrders.length;
    const cancelRate = totalOrders ? ((orderCountByStatus('cancel') / totalOrders) * 100).toFixed(2) : 0;

    const ordersByDay = filteredOrders.reduce((acc, order) => {
        const date = order.orderDate.split('T')[0];
        acc[date] = (acc[date] || 0) + 1;
        return acc;
    }, {});

    const ordersByMonth = filteredOrders.reduce((acc, order) => {
        const month = order.orderDate.slice(0, 7);
        acc[month] = (acc[month] || 0) + 1;
        return acc;
    }, {});

    const calculateGrowthRate = () => {
        const previousPeriodOrders = orders.slice(0, Math.max(0, orders.length - filteredOrders.length));
        const previousTotal = previousPeriodOrders.length;
        const growthRate = previousTotal ? ((totalOrders - previousTotal) / previousTotal * 100).toFixed(2) : 0;
        return growthRate;
    };

    const drawChart = (canvas, labels, data, type) => {
        const ctx = canvas.getContext('2d');
        ctx.clearRect(0, 0, canvas.width, canvas.height);

        const maxData = Math.max(...data);
        const chartHeight = canvas.height - 40;
        const chartWidth = canvas.width - 40;
        const barWidth = chartWidth / data.length;
        const scaleFactor = chartHeight / maxData;

        ctx.fillStyle = type === 'bar' ? 'rgba(153, 102, 255, 0.6)' : 'rgba(75, 192, 192, 1)';
        ctx.strokeStyle = type === 'line' ? 'rgba(75, 192, 192, 1)' : '';

        data.forEach((value, index) => {
            const x = 20 + index * barWidth;
            const y = canvas.height - 20 - value * scaleFactor;

            if (type === 'bar') {
                ctx.fillRect(x, y, barWidth - 10, value * scaleFactor);
            } else if (type === 'line') {
                if (index > 0) {
                    ctx.beginPath();
                    ctx.moveTo(20 + (index - 1) * barWidth + barWidth / 2, canvas.height - 20 - data[index - 1] * scaleFactor);
                    ctx.lineTo(x + barWidth / 2, y);
                    ctx.stroke();
                }
                ctx.beginPath();
                ctx.arc(x + barWidth / 2, y, 5, 0, 2 * Math.PI);
                ctx.fill();
            }
        });

        ctx.fillStyle = '#333';
        ctx.font = '12px Arial';
        labels.forEach((label, index) => {
            const x = 20 + index * barWidth;
            ctx.fillText(label, x + barWidth / 2 - ctx.measureText(label).width / 2, canvas.height - 5);
        });
    };


    const handleMouseMove = (e, canvas, labels, data) => {
        const canvasRect = canvas.getBoundingClientRect();
        const x = e.clientX - canvasRect.left;  // Mouse X position relative to canvas
        const y = e.clientY - canvasRect.top; 

        const barWidth = (canvas.width - 40) / data.length;

        // Check if mouse is over any bar or point
        const index = Math.floor((x - 20) / barWidth);
        if (index >= 0 && index < data.length) {
            setHoveredData({ label: labels[index], value: data[index] });
        } else {
            setHoveredData(null);
        }

    };

    useEffect(() => {
        if (dayChartRef.current) {
            const canvas = dayChartRef.current;
            canvas.addEventListener('mousemove', (e) => handleMouseMove(e, canvas, Object.keys(ordersByDay), Object.values(ordersByDay)));
            drawChart(canvas, Object.keys(ordersByDay), Object.values(ordersByDay), 'line');
        }

        if (monthChartRef.current) {
            const canvas = monthChartRef.current;
            canvas.addEventListener('mousemove', (e) => handleMouseMove(e, canvas, Object.keys(ordersByMonth), Object.values(ordersByMonth)));
            drawChart(canvas, Object.keys(ordersByMonth), Object.values(ordersByMonth), 'bar');
        }
    }, [ordersByDay, ordersByMonth]);

    return (
        <div style={{ fontFamily: 'Arial, sans-serif', padding: '20px' }}>
            <h2 style={{ color: '#333' }}>Dashboard</h2>
            <div style={{ display: 'flex', justifyContent: 'space-between', marginBottom: '20px' }}>
                <div style={{ padding: '10px', border: '1px solid #ddd', borderRadius: '5px' }}>
                    <h3>Tổng doanh thu</h3>
                    <p style={{ fontSize: '1.5em' }}>{totalRevenue.toLocaleString()} VND</p>
                </div>
                <div style={{ padding: '10px', border: '1px solid #ddd', borderRadius: '5px' }}>
                    <h3>Tỉ lệ thanh toán Online/Tiền mặt</h3>
                    <p style={{ fontSize: '1.2em' }}>
                        Online: {paymentRatio().onlinePercentage}% / Cash: {paymentRatio().cashPercentage}%
                    </p>
                </div>
                <div style={{ padding: '10px', border: '1px solid #ddd', borderRadius: '5px' }}>
                    <h3>Tổng lịch hẹn đã hoàn thành</h3>
                    <p style={{ fontSize: '1.5em', color: '#28a745' }}>{completedOrdersCount}</p>
                </div>
                <div style={{ padding: '10px', border: '1px solid #ddd', borderRadius: '5px' }}>
                    <h3>Tỉ lệ hủy lịch</h3>
                    <p style={{ fontSize: '1.5em', color: '#f00' }}>{cancelRate}%</p>
                </div>
                <div style={{ padding: '10px', border: '1px solid #ddd', borderRadius: '5px' }}>
                    <h3>Tỉ lệ tăng trưởng</h3>
                    <p style={{ fontSize: '1.5em', color: '#007bff' }}>{calculateGrowthRate()}%</p>
                </div>
                <div>
                    <label htmlFor="time-frame" style={{ fontWeight: 'bold' }}>Chọn khoảng thời gian: </label>
                    <select
                        id="time-frame"
                        value={timeFrame}
                        onChange={(e) => setTimeFrame(e.target.value)}
                        style={{ padding: '5px', borderRadius: '5px', border: '1px solid #ddd' }}
                    >
                        <option value="all">Tất cả</option>
                        <option value="week">Tuần</option>
                        <option value="month">Tháng</option>
                    </select>
                </div>
            </div>

            <h3>Biểu đồ đơn hàng theo ngày</h3>
            <canvas
                ref={dayChartRef}
                width="500"
                height="300"
                style={{ border: '1px solid #ddd', marginBottom: '20px' }}
            ></canvas>
            {hoveredData && (
                <div style={{ position: 'absolute', top: '50px', left: '20px', backgroundColor: '#fff', padding: '5px', border: '1px solid #ccc', borderRadius: '5px' }}>
                    <p><strong>{hoveredData.label}</strong>: {hoveredData.value}</p>
                </div>
            )}

            <h3>Biểu đồ đơn hàng theo tháng</h3>
            <canvas
                ref={monthChartRef}
                width="500"
                height="300"
                style={{ border: '1px solid #ddd' }}
            ></canvas>
        </div>
    );
}

export default Dashboard;
