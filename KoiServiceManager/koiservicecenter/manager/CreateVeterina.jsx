import { useState } from 'react';
import api from '../src/config/axios';
import { createVeterina } from '../src/config/api';

const userLocation = [
    "Online",
    "Quận 1",
    "Quận 2",
    "Quận 3",
    "Quận 4",
    "Quận 5",
    "Quận 6",
    "Quận 7",
    "Quận 8",
    "Quận 9",
    "Quận 10",
    "Quận 11",
    "Quận 12",
    "Quận Bình Tân",
    "Quận Bình Thạnh",
    "Quận Gò Vấp",
    "Quận Phú Nhuận",
    "Quận Tân Bình",
    "Quận Tân Phú",
    "Huyện Bình Chánh",
    "Huyện Củ Chi",
    "Huyện Cần Giờ",
    "Huyện Hóc Môn",
    "Huyện Nhà Bè"
];

function CreateVeterina() {
    const [userID, setUserID] = useState('');
    const [email, setEmail] = useState('');
    const [password, setPassword] = useState('');
    const [fullName, setFullName] = useState('');
    const [phone, setPhone] = useState('');
    const [selectedAddress, setSelectedAddress] = useState('');
    const [addressDetails, setAddressDetails] = useState('');
    const [description, setDescription] = useState('');  // Added for Description input
    const [loading, setLoading] = useState(false);

    const handleSubmitRegister = async (e) => {
        e.preventDefault();
        setLoading(true);
        const completeAddress = selectedAddress === 'Online' ? 'Online' : `${addressDetails}, ${selectedAddress}`;

        try {
            const user = {
                userID: userID,
                email,
                password,
                name: fullName,
                phoneNumber: phone,
                address: completeAddress
            };

            const response = await api.post('/register', user);

            if (response) {
                const response2 = await createVeterina({ userId: response.userId, description });
                if (response2)
                    window.alert('Tạo bác sĩ thành công');
                else
                    window.alert('Tạo bác sĩ thất bại');
            } else {
                window.alert('Tạo bác sĩ thất bại');
            }
        } catch (error) {
            window.alert(error.response ? error.response.data : 'Đăng kí thất bại!');
        } finally {
            setLoading(false);
        }
    };

    return (
        <div className="form sign-up">
            <h2>Tạo tài khoản mới</h2>
            <label>
                <span>Tài khoản</span>
                <input type="text" value={userID} onChange={(e) => setUserID(e.target.value)} required />
            </label>
            <label>
                <span>Mật khẩu</span>
                <input type="password" value={password} onChange={(e) => setPassword(e.target.value)} required />
            </label>
            <label>
                <span>Họ và tên</span>
                <input type="text" value={fullName} onChange={(e) => setFullName(e.target.value)} required />
            </label>
            <label>
                <span>Email</span>
                <input type="email" value={email} onChange={(e) => setEmail(e.target.value)} required />
            </label>
            <label>
                <span>Số điện thoại</span>
                <input type="text" value={phone} onChange={(e) => setPhone(e.target.value)} required />
            </label>
            <label>
                <span>Địa chỉ của bạn</span><br />
                <select value={selectedAddress} onChange={(e) => setSelectedAddress(e.target.value)} required>
                    <option value="">-- Chọn quận/huyện (Online nếu ngoài Tp.HCM) --</option>
                    {userLocation.map((location) => (
                        <option key={location} value={location}>
                            {location}
                        </option>
                    ))}
                </select>
            </label>
            {selectedAddress && selectedAddress !== 'Online' && (
                <label>
                    <span>Địa chỉ chi tiết</span>
                    <input
                        type="text"
                        value={addressDetails}
                        onChange={(e) => setAddressDetails(e.target.value)}
                        placeholder="Enter additional address details"
                        required
                    />
                </label>
            )}
            <label>
                <span>Description</span>
                <input type="text" value={description} onChange={(e) => setDescription(e.target.value)} required />
            </label>
            <button type="button" className="submit" onClick={handleSubmitRegister} disabled={loading}>
                {loading ? 'Vui lòng đợi...' : 'Đăng kí'}
            </button>
        </div>
    );
}

export default CreateVeterina;
