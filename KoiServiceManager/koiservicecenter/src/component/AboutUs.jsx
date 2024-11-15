import './css/AboutUs.css';
function AboutUs() {
    return (
        <div className="aboutus-container">
          <header className="aboutus-header">
            <h1>Giới thiệu về Trung tâm Chăm sóc Cá Koi</h1>
            <p>Chúng tôi là một trong những đơn vị hàng đầu chuyên về chăm sóc và tư vấn nuôi dưỡng cá Koi tại Việt Nam.</p>
          </header>
          
          <section className="aboutus-mission">
            <h2>Sứ mệnh của chúng tôi</h2>
            <p>Đem đến dịch vụ chăm sóc tốt nhất cho cá Koi với tiêu chuẩn quốc tế, nhằm giúp khách hàng giữ cho hồ cá của họ luôn đẹp và khỏe mạnh.</p>
          </section>
    
          <section className="aboutus-values">
            <h2>Giá trị cốt lõi</h2>
            <ul>
              <li>Tận tâm với từng khách hàng</li>
              <li>Chất lượng dịch vụ cao cấp</li>
              <li>Đội ngũ chuyên nghiệp và nhiều kinh nghiệm</li>
              <li>Đam mê với cá Koi và thiên nhiên</li>
            </ul>
          </section>
    
          <section className="aboutus-services">
            <h2>Dịch vụ của chúng tôi</h2>
            <ul>
              <li>Tư vấn và thiết kế hồ cá Koi</li>
              <li>Chăm sóc và điều trị bệnh cho cá Koi</li>
              <li>Cung cấp thức ăn và phụ kiện chất lượng</li>
              <li>Dịch vụ bảo trì hồ cá định kỳ</li>
            </ul>
          </section>
    
          <section className="aboutus-team">
            <h2>Đội ngũ của chúng tôi</h2>
            <p>Chúng tôi tự hào với đội ngũ chuyên gia giàu kinh nghiệm và kiến thức sâu rộng về cá Koi, luôn sẵn sàng giúp đỡ bạn.</p>
          </section>
        </div>
      );
}

export default AboutUs