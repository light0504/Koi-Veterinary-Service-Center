import { useState, useEffect } from 'react';
import api from '../src/config/axios';
import './Login_SignUp.css';
import { useNavigate } from 'react-router-dom';

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

function LoginSignUp() {
    const [userID, setUserID] = useState('');
    const [email, setEmail] = useState('');
    const [password, setPassword] = useState('');
    const [confirmPass, setConfirmPass] = useState('');
    const [fullName, setFullName] = useState('');
    const [phone, setPhone] = useState('');
    const [selectedAddress, setSelectedAddress] = useState('');
    const [addressDetails, setAddressDetails] = useState('');
    const [loading, setLoading] = useState(false);
    const navigate = useNavigate();

    useEffect(() => {
        sessionStorage.removeItem('user');
        sessionStorage.removeItem('role');
    }, []);

    const handleSubmitLogin = async (e) => {
        e.preventDefault();
        setLoading(true);

        if (!userID) {
            window.alert('Tài khoản không được để trống!');
            setLoading(false);
            return;
        }

        if (!password) {
            window.alert('Mật khẩu không được để trống!');
            setLoading(false);
            return;
        }

        try {
            const response = await api.post('/login', {
                userID,
                password,
            });
            if (response) {
                const data = response.data;
                sessionStorage.setItem('user', data.token);
                sessionStorage.setItem('role', data.role);
                navigate(data.role === 'C' ? '/' : data.role === 'V' ? '/veterinaPage' : data.role === 'S' ? '/staffPage' : '/managerPage');
                window.location.reload();
            } else {
                window.alert("Tài khoản hoặc mật khẩu không đúng");
            }
        } catch (error) {
            console.log(error);
            window.alert("Lỗi khi đăng nhập");
        } finally {
            setLoading(false);
        }
    };


    const handleSubmitRegister = async (e) => {
        e.preventDefault();
        setLoading(true);

        if (!userID) {
            window.alert('Tài khoản không được để trống!');
            setLoading(false);
            return;
        }
        if (userID.length > 100) {
            window.alert('Tài khoản không được dài quá 100 ký tự!');
            setLoading(false);
            return;
        }

        if (!fullName) {
            window.alert('Tên đầy đủ không được để trống!');
            setLoading(false);
            return;
        }

        if (!password) {
            window.alert('Mật khẩu không được để trống!');
            setLoading(false);
            return;
        }
        if (password.length < 2) {
            window.alert('Mật khẩu phải có ít nhất 2 ký tự!');
            setLoading(false);
            return;
        }
        if (password.length > 100) {
            window.alert('Mật khẩu không được dài quá 100 ký tự!');
            setLoading(false);
            return;
        }

        const emailRegex = /^[a-zA-Z0-9._-]+@[a-zA-Z0-9.-]+\.[a-zA-Z]{2,6}$/;
        if (!email || !emailRegex.test(email)) {
            window.alert('Email không hợp lệ!');
            setLoading(false);
            return;
        }

        if (selectedAddress !== 'Online') {
            if (!addressDetails || addressDetails.trim() === "") {
                window.alert('Địa chỉ chi tiết không được để trống!');
                setLoading(false);
                return;
            }

            const invalidCharsRegex = /[*&^%$#@!]/;
            if (invalidCharsRegex.test(addressDetails)) {
                window.alert('Địa chỉ chi tiết không được chứa ký tự đặc biệt!');
                setLoading(false);
                return;
            }

            if (addressDetails.length < 7) {
                window.alert('Địa chỉ chi tiết phải có ít nhất 7 ký tự!');
                setLoading(false);
                return;
            }

            if (addressDetails.length > 100) {
                window.alert('Địa chỉ chi tiết không được dài quá 100 ký tự!');
                setLoading(false);
                return;
            }

            const isOnlyNumbers = /^\d+$/.test(addressDetails);
            if (isOnlyNumbers) {
                window.alert('Địa chỉ chi tiết không thể chỉ chứa số!');
                setLoading(false);
                return;
            }

            const containsDistrictName = userLocation.some(location => addressDetails.includes(location));
            if (containsDistrictName) {
                window.alert('Địa chỉ chi tiết không được chứa tên quận/huyện!');
                setLoading(false);
                return;
            }
        }

        if (!selectedAddress || selectedAddress.trim() === "") {
            window.alert('Vui lòng chọn địa chỉ của bạn!');
            setLoading(false);
            return;
        }

        const phoneRegex = /^0\d{9}$/;
        if (!phone) {
            window.alert('Số điện thoại không được để trống!');
            setLoading(false);
            return;
        } else if (!phoneRegex.test(phone)) {
            window.alert('Số điện thoại không đúng định dạng!');
            setLoading(false);
            return;
        }

        const completeAddress = selectedAddress === 'Online' ? 'Online' : `${addressDetails}, ${selectedAddress}`;

        const user = {
            userID: userID,
            email,
            password,
            name: fullName,
            phoneNumber: phone,
            address: completeAddress
        };

        try {
            const response = await api.post('/register', user);
            if (response) {
                window.alert('Đăng kí thành công');
                navigate('/login');
            } else {
                window.alert('Đăng kí thất bại');
            }
        } catch (error) {
            window.alert(error.response ? error.response.data : 'Đăng kí thất bại!');
        } finally {
            setLoading(false);
        }
    };



    const [isSignUp, setIsSignUp] = useState(false);
    const toggleSignUp = () => setIsSignUp(!isSignUp);

    return (
        <div className={`cont ${isSignUp ? 's--signup' : ''}`}>
            <div className="form sign-in">
                <h2>Xin chào</h2>
                <label>
                    <span>Tài khoản</span>
                    <input type="text" value={userID} onChange={(e) => setUserID(e.target.value)} required />
                </label>
                <label>
                    <span>Mật khẩu</span>
                    <input type="password" value={password} onChange={(e) => setPassword(e.target.value)} required />
                </label>
                <button type="button" className="submit" onClick={handleSubmitLogin} disabled={loading}>
                    {loading ? 'Vui lòng đợi' : 'Đăng nhập'}
                </button>
            </div>

            <div className="sub-cont">
                <div className="img">
                    <div className={`img__text ${isSignUp ? '' : 'm--in'}`}>
                        <h3>Nếu đã có tài khoản, đăng nhập ngay.</h3>
                    </div>
                    <div className={`img__text ${isSignUp ? 'm--up' : ''}`}>
                        <h3>Không có tài khoản? Đăng kí!</h3>
                    </div>
                    <div className="img__btn" onClick={toggleSignUp}>
                        <span className="m--up">Đăng kí</span>
                        <span className="m--in">Đăng nhập</span>
                    </div>
                </div>

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
                    <label>A
                        <span>Nhập lại mật khẩu</span>
                        <input type="password" value={confirmPass} onChange={(e) => setConfirmPass(e.target.value)} required />
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
                        <span>Địa chỉ của bạn</span><br></br>
                        <select value={selectedAddress} onChange={(e) => setSelectedAddress(e.target.value)} className="select" required>
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
                                placeholder="Nhập địa chỉ chi tiết"
                                required
                            />
                        </label>
                    )}
                    <button type="button" className="submit" onClick={handleSubmitRegister} disabled={loading}>
                        {loading ? 'Vui lòng đợi...' : 'Đăng kí'}
                    </button>
                </div>
            </div>
        </div>
    );
}

export default LoginSignUp;
