import { useEffect, useState } from "react";
import clock_icon from "../assets/clock_icon.png";
import logo from "../assets/fpt_university_logo.jpg";
import mail_icon from "../assets/mail_icon.png";
import { fetchUserID } from "../config/api.jsx";
import { NavLink, Outlet } from "react-router-dom";
import "./css/Header.css";
import { getUserId } from '../utils/utils.jsx'

const Header = () => {
    const [user, setUser] = useState(null);

    // Fetch user from localStorage on component mount
    useEffect(() => {
        const loadUser = async () => {
            const token = sessionStorage.getItem('user');
            if (token) {
                setUser(await fetchUserID(getUserId()));
            }
        };
        loadUser();
    }, []);

    const handleLogout = () => {
        sessionStorage.removeItem("user");
        setUser(null);
        window.location.href = "/";
    };

    return (
        <>
            <div className="header-container">
                <div className="header-info">
                    <span>📞 Hotline: 028 7300 5588</span>
                    <a href="https://maps.app.goo.gl/pHN7n6czhj67Y3UdA" target="_blank">
                        📍 Address: Lot E2a-7, Street D1, Thu Duc City...
                    </a>
                </div>
                <div className="user-dropdown-container">
                    {user ? (
                        <>
                            <span>Welcome, {user.name}</span>
                            <div className="user-dropdown">
                                <NavLink to="/profile">Hồ sơ</NavLink>
                                <span onClick={handleLogout}>Logout</span>
                            </div>
                        </>
                    ) : (
                        <NavLink className="nav-link" to="/login">ĐĂNG NHẬP</NavLink>
                    )}
                </div>
            </div>

            <div className="logo-container">
                <img src={logo} alt="Veterinarian Service" />
                <div className="info-box">
                    <img src={mail_icon} alt="Mail Icon" />
                    <p>
                        24/7 Support <br />
                        Hotline: 028 7300 5588
                    </p>
                </div>
                <div className="info-box">
                    <img src={clock_icon} alt="Clock Icon" />
                    <p>
                        Working hours <br />
                        Mon - Sat
                        <br />
                        7:30am to 8:00pm
                    </p>
                </div>
            </div>
            <div className="navbar">
                <NavLink className="nav-link" to="/">TRANG CHỦ</NavLink>
                <NavLink className="nav-link" to="/service">DỊCH VỤ</NavLink>
                <NavLink className="nav-link" to="/veterina">BÁC SĨ</NavLink>
                <NavLink className="nav-link" to="/aboutus">VỀ CHÚNG TÔI</NavLink>
                <NavLink className="nav-link" to="/faq">FAQ</NavLink>
                <NavLink className="nav-link" to="https://www.facebook.com/FPTU.HCM" target="_blank">
                    LIÊN HỆ
                </NavLink>
            </div>
            <Outlet />
        </>
    );
};

export default Header;
