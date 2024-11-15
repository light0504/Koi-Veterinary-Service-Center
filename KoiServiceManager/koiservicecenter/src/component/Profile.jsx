import { useEffect, useState } from 'react';
import { getUserId } from '../utils/utils.jsx';
import { fetchUserID } from '../config/api.jsx';
import FishTable from './FishTable.jsx';
import CustomerOrders from './CustomerOrders.jsx';
import './css/Profile.css';

const Profile = () => {
    const [user, setUser] = useState(null);
    const [showFishTable, setShowFishTable] = useState(false);
    const [showCustomerOrders, setShowCustomerOrders] = useState(false);

    useEffect(() => {
        const loadUserData = async () => {
            const userId = getUserId();
            const userData = await fetchUserID(userId);
            setUser(userData);
        };

        loadUserData();
    }, []);

    if (!user) {
        return <div>Loading...</div>;
    }

    const toggleFishTable = () => {
        // Toggle fish table visibility and hide customer orders
        setShowFishTable(prev => !prev);
        if (showCustomerOrders) {
            setShowCustomerOrders(false); // Hide customer orders if they're currently shown
        }
    };

    const toggleCustomerOrders = () => {
        // Toggle customer orders visibility and hide fish table
        setShowCustomerOrders(prev => !prev);
        if (showFishTable) {
            setShowFishTable(false); // Hide fish table if it's currently shown
        }
    };

    return (
        <div className="profile-container">
            <h2>Hồ sơ</h2>
            <p><b>Id người dùng:</b> {user.userID}</p>
            <p><b>Tên:</b> {user.name}</p>
            <p><b>Email:</b> {user.email}</p>
            <p><b>Số điện thoại:</b> {user.phoneNumber}</p>
            <p><b>Role:</b> {user.role}</p>
            <p><b>Địa chỉ đăng kí:</b> {user.address}</p>

            <div>
                <button className="profile-button" onClick={toggleFishTable}>
                    {showFishTable ? 'Ẩn danh sách cá của tôi' : 'Hiển thị danh sách cá của tôi'}
                </button>
                <button className="profile-button" onClick={toggleCustomerOrders}>
                    {showCustomerOrders ? 'Ẩn bảng lịch hẹn' : 'Xem lịch hẹn của tôi'}
                </button>
            </div>

            {showFishTable && <FishTable userID={user.userID} role={'C'} />}
            {showCustomerOrders && <CustomerOrders userID={user.userID} />}
        </div>
    );
};

export default Profile;
