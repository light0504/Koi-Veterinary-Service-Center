import { useEffect, useState } from 'react';
import { ToastContainer, toast } from 'react-toastify';
import { fetchServices, addService, deleteService, updateService } from '../src/config/api.jsx';
import './css/QuanLyDichVuCaKoi.css';

const ServiceManagement = () => {
    const [dichVu, setDichVu] = useState([]);
    const [dichVuMoi, setDichVuMoi] = useState({ name: '', type: '', price: '', maxQuantity: '', service: true });
    const [dichVuDangChinhSua, setDichVuDangChinhSua] = useState(null);

    useEffect(() => {
        const loadServices = async () => {
            const services = await fetchServices();
            setDichVu(services);
        };
        loadServices();
    }, []);

    const handleThemDichVu = async () => {
        const response = await addService(dichVuMoi);
        if (response) {
            toast.success('Thêm dịch vụ thành công');
            setDichVu([...dichVu, { ...dichVuMoi, serviceID: response.serviceID }]);
        } else {
            toast.error('Lỗi khi thêm dịch vụ');
        }
        setDichVuMoi({ name: '', type: '', price: '', maxQuantity: '', service: true });
    };

    const handleCapNhatDichVu = async () => {
        const updatedService = await updateService(dichVuDangChinhSua);
        if (updatedService) {
            toast.success('Cập nhật dịch vụ thành công');
            setDichVu(dichVu.map(dv => dv.serviceID === dichVuDangChinhSua.serviceID ? dichVuDangChinhSua : dv));
        } else {
            toast.error('Lỗi khi cập nhật dịch vụ');
        }
        setDichVuDangChinhSua(null);
    };

    const handleXoaDichVu = async (serviceID) => {
        const confirmDelete = window.confirm("Bạn có chắc chắn muốn xóa dịch vụ này?");
        if (confirmDelete) {
            const response = await deleteService(serviceID);
            if (response) {
                toast.success('Đã xóa dịch vụ thành công');
                setDichVu(dichVu.filter(dv => dv.serviceID !== serviceID));
            } else {
                toast.error('Lỗi khi xóa dịch vụ');
            }
        }
    };

    return (
        <div className="container">
            <h1>Quản Lý Dịch Vụ Cá Koi</h1>
            <ToastContainer className="toast" />

            <table className="table">
                <thead>
                    <tr>
                        <th>Mã Dịch Vụ</th>
                        <th>Tên Dịch Vụ</th>
                        <th>Loại</th>
                        <th>Giá (VND)</th>
                        <th>Số lượng đặt hàng tối đa</th>
                        <th>Cho phép đặt lịch?</th>
                        <th>Hành Động</th>
                    </tr>
                </thead>
                <tbody>
                    {dichVu.map((dv) => (
                        <tr key={dv.serviceID}>
                            <td>{dv.serviceID}</td>
                            <td>{dv.name}</td>
                            <td>{dv.type}</td>
                            <td>{dv.price.toLocaleString('vi-VN')} VND</td>
                            <td>{dv.maxQuantity}</td>
                            <td>{dv.service ? 'Có' : 'Không'}</td>
                            <td>
                                <button className="button" onClick={() => setDichVuDangChinhSua(dv)}>Sửa</button>
                                <button className="button" onClick={() => handleXoaDichVu(dv.serviceID)}>Xóa</button>
                            </td>
                        </tr>
                    ))}
                </tbody>
            </table>

            <div>
                <h2>Thêm Dịch Vụ Mới</h2>
                <div className="input-group">
                    <label>Tên Dịch Vụ</label>
                    <input
                        type="text"
                        placeholder="Tên Dịch Vụ"
                        value={dichVuMoi.name}
                        onChange={(e) => setDichVuMoi({ ...dichVuMoi, name: e.target.value })}
                    />
                </div>
                <div className="input-group">
                    <label>Loại</label>
                    <input
                        type="text"
                        placeholder="Loại"
                        value={dichVuMoi.type}
                        onChange={(e) => setDichVuMoi({ ...dichVuMoi, type: e.target.value })}
                    />
                </div>
                <div className="input-group">
                    <label>Giá (VND)</label>
                    <input
                        type="number"
                        placeholder="Giá (VND)"
                        value={dichVuMoi.price}
                        onChange={(e) => setDichVuMoi({ ...dichVuMoi, price: e.target.value })}
                    />
                </div>
                <div className="input-group">
                    <label>Số lượng đặt tối đa</label>
                    <input
                        type="number"
                        placeholder="Số lượng đặt tối đa"
                        value={dichVuMoi.maxQuantity}
                        onChange={(e) => setDichVuMoi({ ...dichVuMoi, maxQuantity: e.target.value })}
                    />
                </div>
                <div className="input-group">
                    <label>Cho phép đặt lịch?</label>
                    <input
                        type="checkbox"
                        checked={dichVuMoi.service}
                        onChange={(e) => setDichVuMoi({ ...dichVuMoi, service: e.target.checked })}
                    />
                </div>
                <button className="button" onClick={handleThemDichVu}>Thêm Dịch Vụ</button>
            </div>

            {dichVuDangChinhSua && (
                <section>
                    <h2>Sửa Dịch Vụ</h2>
                    <div className="input-group">
                        <label>Tên dịch vụ</label>
                        <input
                            type="text"
                            value={dichVuDangChinhSua.name}
                            onChange={(e) => setDichVuDangChinhSua({ ...dichVuDangChinhSua, name: e.target.value })}
                        />
                    </div>
                    <div className="input-group">
                        <label>Loại dịch vụ</label>
                        <input
                            type="text"
                            value={dichVuDangChinhSua.type}
                            onChange={(e) => setDichVuDangChinhSua({ ...dichVuDangChinhSua, type: e.target.value })}
                        />
                    </div>
                    <div className="input-group">
                        <label>Giá dịch vụ</label>
                        <input
                            type="number"
                            value={dichVuDangChinhSua.price}
                            onChange={(e) => setDichVuDangChinhSua({ ...dichVuDangChinhSua, price: parseFloat(e.target.value) })}
                        />
                    </div>
                    <div className="input-group">
                        <label>Số lượng đặt tối đa</label>
                        <input
                            type="number"
                            min="1"
                            value={dichVuDangChinhSua.maxQuantity}
                            onChange={(e) => setDichVuDangChinhSua({ ...dichVuDangChinhSua, maxQuantity: parseFloat(e.target.value) })}
                        />
                    </div>
                    <div className="input-group">
                        <label>Cho phép đặt lịch?</label>
                        <input
                            type="checkbox"
                            checked={dichVuDangChinhSua.service}
                            onChange={(e) => setDichVuDangChinhSua({ ...dichVuDangChinhSua, service: e.target.checked })}
                        />
                    </div>
                    <button className="button" onClick={handleCapNhatDichVu}>Cập Nhật Dịch Vụ</button>
                    <button className="button" onClick={() => setDichVuDangChinhSua(null)}>Hủy</button>

                </section>
            )}
        </div>
    );
};

export default ServiceManagement;
