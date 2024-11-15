import "./Home.css";
import { NavLink } from "react-router-dom";

function Home() {
  return (
    <div className="service-container">
      <h1 className="service-title">
        Bạn yêu thích vẻ đẹp rực rỡ của cá Koi nhưng lại lo lắng về việc chăm
        sóc chúng?
      </h1>
      <p className="intro-text">
        Hãy để <strong>Chúng Tôi</strong> giúp bạn! Với đội ngũ chuyên gia giàu
        kinh nghiệm và am hiểu về cá Koi, chúng tôi mang đến dịch vụ chăm sóc cá
        Koi tận tâm và chuyên nghiệp, giúp những "báu vật" của bạn luôn khỏe
        mạnh, rực rỡ sắc màu.
      </p>
      <section className="service-section">
        <h2 className="section-title">Dịch vụ của chúng tôi bao gồm:</h2>
        <div className="service-item">
          <h3>Chăm sóc định kỳ:</h3>
          <ul>
            <li>Vệ sinh hồ cá Koi, thay nước, kiểm tra và xử lý nước.</li>
            <li>
              Cho cá ăn với chế độ dinh dưỡng phù hợp, đảm bảo sức khỏe và sự
              phát triển tốt nhất.
            </li>
            <li>Theo dõi sức khỏe cá, phòng và điều trị bệnh kịp thời.</li>
          </ul>
        </div>
        <div className="service-item">
          <h3>Dịch vụ khác:</h3>
          <ul>
            <li>Tư vấn, giải đáp mọi thắc mắc về cá Koi.</li>
            <li>
              Cung cấp thức ăn, thuốc và các sản phẩm chăm sóc cá Koi chất
              lượng.
            </li>
          </ul>
        </div>
      </section>
      <section className="why-choose-us">
        <h2 className="section-title">Tại sao chọn Koi Service Center?</h2>
        <ul>
          <li>
            Đội ngũ chuyên nghiệp: Kinh nghiệm lâu năm, am hiểu về cá Koi và các
            kỹ thuật chăm sóc.
          </li>
          <li>
            Tận tâm, chu đáo: Luôn đặt sức khỏe và vẻ đẹp của cá Koi lên hàng
            đầu.
          </li>
          <li>
            Giá cả hợp lý: Cung cấp dịch vụ chất lượng với mức giá cạnh tranh.
          </li>
          <li>
            Linh hoạt: Đáp ứng mọi nhu cầu của khách hàng với các gói dịch vụ đa
            dạng.
          </li>
        </ul>
      </section>
      <div className="booking-section">
        <p className="booking-text">
          Bạn muốn đặt dịch vụ cho cá Koi của mình? Hãy đặt tại đây:
        </p>
        <NavLink to="/orderform" className="booking-button">
          Đặt Dịch Vụ
        </NavLink>
      </div>
      <div className="image-placeholder">
        [Hình ảnh minh họa về quy trình chăm sóc cá Koi chuyên nghiệp]
      </div>
    </div>
  );
}
export default Home;
