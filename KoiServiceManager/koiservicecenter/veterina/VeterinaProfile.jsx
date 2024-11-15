import { useEffect, useState } from 'react';
import { getUserId } from '../src/utils/utils.jsx';
import { fetchUserID, fetchVeterinas, getFeedbackByVeterinaId } from '../src/config/api.jsx';
import { useParams } from 'react-router-dom';
import '../src/component/css/Profile.css';

const Profile = () => {
    const { veterinaID } = useParams();
    const [veterina, setVeterina] = useState(null);
    const [feedbacks, setFeedbacks] = useState([]);
    const [user, setUser] = useState(null);
    const { loggedRole } = sessionStorage.getItem('role');
    useEffect(() => {

        const loadUserData = async () => {
            if (loggedRole === 'V') {
                const userId = getUserId();
                const userData = await fetchUserID(userId);
                setUser(userData);
            }

            const veterinasData = await fetchVeterinas();
            const veterinaData = veterinasData.find(vet => vet.veterinaID === veterinaID)
            setVeterina(veterinaData);

            const feedBacksData = await getFeedbackByVeterinaId(veterinaID);
            setFeedbacks(feedBacksData)
        };

        loadUserData();
    }, []);

    if (!user) {
        return <div>Loading...</div>;
    }

    return (
        <div className="profile-container">
            <h2>Hồ sơ bác sĩ</h2>


            {loggedRole === 'V' ? (
                <>
                    <p><b>Id Người dùng: </b> {user.userID}</p>
                    <p><b>Tên: </b> {user.name}</p>
                    <p><b>Email: </b> {user.email}</p>
                    <p><b>Số điện thoại: </b> {user.phoneNumber}</p>
                    <p><b>Role: </b> {user.role}</p>
                    <p><b>Địa chỉ đăng kí: </b> {user.address}</p>
                </>
            ) : (
                <></>
            )}
            <p><b>Giới thiệu: </b>{veterina.description }</p>

            {feedbacks.map(f => {
                const feedbackDate = new Date(f.feedbackDateTime).toLocaleString('vi-VN');
                const stars = '⭐'.repeat(f.rating); // Generate stars based on rating
                return (
                    <div key={f.feedbackId}>
                        <p>Ngày: {feedbackDate}</p>
                        <p>Đánh giá: {stars}</p>
                        <p>Bình luận: {f.comment}</p>
                    </div>
                );
            })}
        </div>
    );
};

export default Profile;
