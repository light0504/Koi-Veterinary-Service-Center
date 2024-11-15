import React, { useState } from 'react';
import ServiceManagement from './ServiceManagement';
import QuanLyDonHang from './OrdersManagement';
import './css/StaffPage.css'; // Import the CSS file
import Schedule from './Schedule';

const StaffPage = () => {
    const [activePage, setActivePage] = useState('orders');

    // Xử lý chuyển tiếp giữa các phần
    const handlePageChange = (page) => {
        setActivePage(page);
    };

    const handleLogout = () => {
        sessionStorage.removeItem("user");
        window.location.href = "/";
    };

    return (
        <div className="container"> {/* Use the container class */}
            <div className="button-container"> {/* Use the button container class */}
                <button onClick={() => handlePageChange('services')}>
                    Quản Lý Dịch Vụ
                </button>
                <button onClick={() => handlePageChange('orders')}>
                    Quản Lý Đơn Hàng
                </button>
                <button onClick={() => handleLogout()}>Logout</button>
            </div>

            {/* Nội dung sẽ hiển thị dựa trên trang hiện tại */}
            <div className="content"> {/* Optionally wrap content in a div with a class */}
                {activePage === 'services' && <ServiceManagement />}
                {activePage === 'orders' && <QuanLyDonHang />}
            </div>

            <Schedule />
        </div>
    );
}

export default StaffPage;
