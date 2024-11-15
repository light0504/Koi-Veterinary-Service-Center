import { useEffect, useState } from 'react';
import { getUserId } from '../utils/utils.jsx'
import { fetchSlots, fetchVeterinas, fetchOrdersInSelectedSlot, fetchServices, createOrder, fetchUserID, fetchTravelExpense, initiatePayment, updateOrderStatus } from '../config/api.jsx';
import { ToastContainer, toast } from 'react-toastify'; // Importing react-toastify
import 'react-toastify/dist/ReactToastify.css'; // npm install react-toastify
import { useNavigate } from 'react-router-dom';
import "./css/OrderForm.css";
import VeterinaSchedule from '../../veterina/VeterinaSchedule.jsx';

const OrdersForm = () => {
    const [slots, setSlots] = useState([]);
    const [veterinas, setVeterinas] = useState([]);
    const [services, setServices] = useState([]);
    const [user, setUser] = useState(null);
    const [travelExpenses, setTravelExpenses] = useState([]);
    const navigate = useNavigate();

    const [availableSlots, setAvailableSlots] = useState({});
    const [searchVeterina, setSearchVeterina] = useState('');

    const [selectedSlot, setSelectedSlot] = useState(null);
    const [selectedDate, setSelectedDate] = useState(null);
    const [veterinasInSelectedSlot, setVeterinasInSelectedSlot] = useState([]);
    const [selectedVeterina, setSelectedVeterina] = useState(null);

    const [selectedServices, setSelectedServices] = useState([]);
    const [serviceQuantities, setServiceQuantities] = useState({});

    const [selectedAddress, setSelectedAddress] = useState('');
    const [useMyAddress, setUseMyAddress] = useState(false);
    const [addressDetails, setAddressDetails] = useState('');

    const [pay, setPay] = useState(false);

    useEffect(() => {
        const loadData = async () => {
            const userData = await fetchUserID(getUserId());
            const slotsData = await fetchSlots();
            const veterinasData = await fetchVeterinas();
            const servicesData = await fetchServices();
            const travelExpensesData = await fetchTravelExpense();

            setTravelExpenses(travelExpensesData);
            setUser(userData);
            setSlots(slotsData);
            setVeterinas(veterinasData.filter(veterina => veterina.status === true));
            setServices(servicesData);
        };

        loadData();
    }, []);

    useEffect(() => {
        const checkSlotAvailability = async () => {
            const slotAvailability = {};

            for (const day of getNext7Days()) {
                const formattedDate = day.toISOString().split('T')[0];
                slotAvailability[formattedDate] = {};

                for (const slot of slots) {
                    const orders = await fetchOrdersInSelectedSlot(formattedDate, slot.slot);
                    const activeOrders = orders.filter(order => order.status !== 'cancel');
                    const availableVeterinas = veterinas.filter(vet =>
                        !activeOrders.some(order => order.veterinaId === vet.veterinaID)
                    );

                    slotAvailability[formattedDate][slot.slot] = availableVeterinas;
                }
            }
            setAvailableSlots(slotAvailability);
        };

        checkSlotAvailability();
    }, [slots, veterinas]);

    const getNext7Days = () => {
        const days = [];
        const today = new Date();
        for (let i = 0; i < 7; i++) {
            const day = new Date(today);
            day.setDate(today.getDate() + i);
            days.push(day);
        }
        return days;
    };

    const handleSlotSelection = (date, slot) => {
        setSelectedDate(date);
        setSelectedSlot(slot);

        const formattedDate = date.toISOString().split('T')[0];
        console.log(searchVeterina)
        if (searchVeterina === '') {
            const availableVeterinas = availableSlots[formattedDate]?.[slot.slot] || [];
            setVeterinasInSelectedSlot(availableVeterinas);
            if (availableVeterinas.length > 0) {
                toast.success(`Bạn đã chọn Slot: ${slot.startTime} - ${slot.endTime} vào ngày ${date.toLocaleDateString()}`);
                if (selectedVeterina && !availableVeterinas.some(vet => vet.veterinaId === selectedVeterina.veterinaId)) {
                    toast.error('Bác sĩ đã chọn không khả dụng trong slot mới, vui lòng chọn lại.');
                    setSelectedVeterina(null);
                }
            } else {
                toast.error('Không còn Bác sĩ nào trong slot này');
                setSelectedDate(null);
                setSelectedSlot(null);
            }
        } else {
            toast.success(`Bạn đã chọn Slot: ${slot.startTime} - ${slot.endTime} vào ngày ${date.toLocaleDateString()}`);
        }
        
    };

    const handleServiceSelection = (serviceID) => {
        if (selectedServices.includes(serviceID)) {
            return;
        }

        setSelectedServices(prevServices => [...prevServices, serviceID]);
        setServiceQuantities(prevQuantities => ({
            ...prevQuantities,
            [serviceID]: 1, // Default quantity is 1
        }));
    };

    const handleQuantityChange = (serviceID, quantity) => {
        const service = services.find(s => s.serviceID === serviceID);
        const maxQuantity = service.maxQuantity;

        const validQuantity = quantity > maxQuantity ? maxQuantity : quantity < 1 ? 1 : quantity;

        setServiceQuantities(prevQuantities => ({
            ...prevQuantities,
            [serviceID]: validQuantity,
        }));
        calculateTotal();
    };

    const handleRemoveService = (serviceID) => {
        setSelectedServices(prevServices => prevServices.filter(id => id !== serviceID));
        setServiceQuantities(prevQuantities => {
            const newQuantities = { ...prevQuantities };
            delete newQuantities[serviceID];
            return newQuantities;
        });
    };

    const getDistrict = (address) => {
        const districtMatch = address.match(/(Quận|Huyện)\s+\S+(\s+\S+)?/);
        return districtMatch ? districtMatch[0] : null;
    };


    const calculateTotal = () => {
        const district = getDistrict(selectedAddress);

        const travelExpense = travelExpenses.find(expense => expense.endLocation === district);

        const travelFee = travelExpense ? travelExpense.fee : 0;

        const total = selectedServices.reduce((total, serviceID) => {
            const service = services.find(s => s.serviceID === serviceID);
            const quantity = serviceQuantities[serviceID] || 1;
            return total + (service ? service.price * quantity : 0);
        }, 0) + travelFee;

        return total;
    };



    const handleUseMyAddress = () => {
        setUseMyAddress(!useMyAddress);
        if (!useMyAddress && user && user.address) {
            setSelectedAddress(user.address);
            calculateTotal();
        } else {
            setSelectedAddress('');
        }
    };

    const handleChangePayStatus = () => {
        setPay(prevPay => !prevPay);
    };

    const handleSlotSelect = (veterinaID, date, slot) => {
        setSelectedVeterina(veterinaID);
        handleSlotSelection(date, slot)
    };

    const handleBooking = async () => {
        if (!selectedDate || !selectedSlot) {
            alert('Vui lòng chọn Ngày và Slot!');
            return;
        }

        if (selectedServices.length === 0) {
            alert('Vui lòng chọn ít nhất một dịch vụ!');
            return;
        }

        if (selectedDate < new Date().setHours(0, 0, 0, 0) ||
            (selectedDate.toDateString() === new Date().toDateString() && selectedSlot.endTime <= new Date().toLocaleTimeString('vi-VN'))) {
            alert('Không thể đặt lịch vào slot trước thời gian hiện tại!');
            return;
        }

        if (!selectedAddress) {
            alert('Vui lòng nhập hoặc chọn địa chỉ!');
            return;
        }

        const completeAddress = selectedAddress === 'Online' ? 'Online' : `${addressDetails}, ${selectedAddress}`;
        const servicesWithQuantities = selectedServices.map(serviceID => ({
            serviceID,
            quantity: serviceQuantities[serviceID] || 1
        }));

        if (selectedAddress === 'Online') {
            const allConsultation = selectedServices.every(serviceID => {
                const service = services.find(s => s.serviceID === serviceID);
                return service.type === 'Tư vấn';
            });
            if (!allConsultation) {
                alert('Tất cả dịch vụ phải là Tư vấn khi chọn "Online". Vui lòng chọn lại.');
                return;
            }
        }

        if (searchVeterina !== '') {
            setSelectedVeterina(searchVeterina);
        }
        const newOrder = {
            userID: user.userID,
            veterinaID: selectedVeterina || null,
            slot: selectedSlot.slot,
            date: selectedDate.toISOString().split('T')[0],
            services: servicesWithQuantities,
            address: completeAddress,
        };

        
        try {
            const result = await createOrder(newOrder);
            setSelectedVeterina(null);
            setSelectedDate(null);
            setSelectedSlot(null);
            if (result !== "Veterinarian not available") {
                const { orderId, orderDate, address, status } = result;

                console.log(pay);
                if (selectedAddress === 'Online') {
                    const payment = await initiatePayment(orderId, 'accept');
                    if (payment) {
                        toast.success(`Đơn hàng của bạn đã được đặt thành công!\nMã đơn: ${orderId}\nNgày đặt: ${orderDate}\nĐịa chỉ: ${address}\nTrạng thái: ${status}, đang chuyển sang trang thanh toán`);
                        setTimeout(() => window.location.href = payment, 5000);
                    } else {
                        toast.error('Đã xảy ra lỗi khi xử lý thanh toán. Đơn đã bị hủy, vui lòng tạo lại');
                        await updateOrderStatus(orderId, 'cancel');
                    }
                } else if (pay) {
                    const payment = await initiatePayment(orderId, 'accept');
                    if (payment) {
                        toast.success(`Đơn hàng của bạn đã được đặt thành công!\nMã đơn: ${orderId}\nNgày đặt: ${orderDate}\nĐịa chỉ: ${address}\nTrạng thái: ${status}, đang chuyển sang trang thanh toán`);
                        setTimeout(() => window.location.href = payment, 5000);
                    } else {
                        toast.error('Đã xảy ra lỗi khi xử lý thanh toán. Đơn đã bị hủy, vui lòng tạo lại');
                        await updateOrderStatus(orderId, 'cancel');
                    }
                } else {
                    toast.success(`Đơn hàng của bạn đã được đặt thành công!\nMã đơn: ${orderId}\nNgày đặt: ${orderDate}\nĐịa chỉ: ${address}\nTrạng thái: ${status}`);
                    toast.warning('Bạn sẽ được chuyển về trang chủ sau 5 giây');
                    setTimeout(() => {
                        navigate('/');
                    }, 5000);
                }
            } else {
                toast.error('Đã có lỗi xảy ra khi đặt đơn hàng.');
            }
        } catch (error) {
            console.log(error)
            toast.error('Lỗi kết nối! Vui lòng thử lại.');
        }
    };

    return (
        <div className="orders-form">
            <h1 className="orders-title">Đơn đặt lịch</h1>
            <ToastContainer />

            <div>
                <section className="vet-selection">
                    <label>Tìm Bác Sĩ:</label>
                    <select onChange={(e) => setSearchVeterina(e.target.value)} value={searchVeterina || ''}>
                        <option value="">Chọn bác sĩ</option>
                        {veterinas
                            .map((vet) => (
                                <option key={vet.veterinaID} value={vet.veterinaID}>
                                    {vet.name} - {vet.description}
                                </option>
                            ))}
                    </select>
                </section>
            </div>
            {searchVeterina !== '' ? (
                <VeterinaSchedule veterinaId={searchVeterina} onSlotSelect={handleSlotSelect} />
            ) : (
                <>
                    {/* Render bảng slot */}
                    <section className="section slot-selection">
                        {/* Render bảng slot */}
                        <div className="slot-table-container">
                            <table className="slot-table" border="1">
                                <thead>
                                    <tr>
                                        <th>Slot</th>
                                        {getNext7Days().map((day) => (
                                            <th key={day}>
                                                {day.toLocaleDateString("vi-VN", {
                                                    weekday: "short",
                                                    day: "numeric",
                                                    month: "numeric",
                                                })}
                                            </th>
                                        ))}
                                    </tr>
                                </thead>
                                <tbody>
                                    {slots.map((slot) => (
                                        <tr key={slot.slot}>
                                            <td>
                                                {slot.startTime} - {slot.endTime}
                                            </td>
                                            {getNext7Days().map((day) => {

                                                const formattedDate = day.toISOString().split('T')[0];
                                                const availableVeterinas = availableSlots[formattedDate]?.[slot.slot] || [];
                                                const isDisabled = availableVeterinas.length === 0;

                                                const slotDateTime = new Date(day);
                                                slotDateTime.setHours(
                                                    slot.startTime.split(":")[0],
                                                    slot.startTime.split(":")[1]
                                                );

                                                // Use the current date and time
                                                const currentDateTime = new Date();

                                                return (
                                                    <td key={day}>
                                                        {slotDateTime > currentDateTime && (
                                                            <button
                                                                className={`select-slot-button ${isDisabled ? 'disabled-button' : ''}`}
                                                                style={{ backgroundColor: isDisabled ? 'red' : '' }}
                                                                onClick={() => !isDisabled && handleSlotSelection(day, slot)}
                                                                disabled={isDisabled}
                                                            >
                                                                {isDisabled ? 'Hết bác sĩ' : 'Chọn'}
                                                            </button>
                                                        )}
                                                    </td>
                                                );
                                            })}
                                        </tr>
                                    ))}
                                </tbody>
                            </table>
                        </div>
                        {/* Hiển thị thông tin slot và ngày đã chọn */}
                        {selectedSlot && selectedDate && (
                            <div className="selected-info">
                                <p>
                                    Slot đã chọn: {selectedSlot.startTime} - {selectedSlot.endTime}
                                </p>
                                <p>Ngày đã chọn: {selectedDate.toLocaleDateString()}</p>
                            </div>
                        )}
                    </section>

                    <section className="section ">
                        {/* Chọn bác sĩ */}
                        {selectedSlot && (
                            <section className="vet-selection">
                                <label>Chọn Bác Sĩ:</label>
                                <select onChange={(e) => setSelectedVeterina(e.target.value)} value={selectedVeterina || ''}>
                                    <option value="">Chọn bác sĩ</option>
                                    {veterinasInSelectedSlot
                                        .map((vet) => (
                                            <option key={vet.veterinaID} value={vet.veterinaID}>
                                                {vet.name} - {vet.description}
                                            </option>
                                        ))}
                                </select>
                            </section>
                        )}
                    </section>
                </>
            )}


            {/* Chọn dịch vụ */}
            <section className="section ">
                {/* Chọn dịch vụ */}
                <section className="service-selection">
                    <label>Chọn Dịch Vụ:</label>
                    <select
                        onChange={(e) => handleServiceSelection(e.target.value)}
                        value=""
                    >
                        <option value="">-- Chọn Dịch Vụ --</option>
                        {services
                            .filter((service) => service.service === true)
                            .map((service) => (
                                <option key={service.serviceID} value={service.serviceID}>
                                    {service.name} - {service.price} đ
                                </option>
                            ))}
                    </select>
                </section>

                {/* Hiển thị dịch vụ đã chọn */}
                {selectedServices.length > 0 && (
                    <section className="selected-service-table" >
                        <h3>Dịch Vụ Đã Chọn:</h3>
                        <table border="1">
                            <thead>
                                <th>Dịch vụ</th>
                                <th>Giá dịch vụ</th>
                                <th>Số lượng tối đa</th>
                                <th>Chọn số lượng</th>
                                <th>Xóa</th>
                            </thead>
                            {selectedServices.map((serviceID) => {
                                const service = services.find((s) => s.serviceID === serviceID);
                                const quantity = serviceQuantities[serviceID] || 1;
                                return (
                                    <tbody key={serviceID}>
                                        <td>{service.name}</td>
                                        <td>{service.price}</td>
                                        <td>{service.maxQuantity}</td>
                                        <td>
                                            <input
                                                type="number"
                                                min="1"
                                                max={service.maxQuantity}
                                                value={quantity}
                                                onChange={(e) =>
                                                    handleQuantityChange(
                                                        serviceID,
                                                        parseInt(e.target.value)
                                                    )
                                                }
                                            />
                                        </td>
                                        <td>
                                            <button onClick={() => handleRemoveService(serviceID)}>
                                                Xóa dịch vụ
                                            </button>
                                        </td>
                                    </tbody>
                                );
                            })}
                        </table>
                    </section>
                )}
            </section>

            {/* Chọn địa chỉ */}
            <section className="section">
                {/* Chọn địa chỉ */}
                <h3>Chọn Địa Chỉ:</h3>
                <section className="use-my-address">
                    <input
                        type="checkbox"
                        checked={useMyAddress}
                        onChange={handleUseMyAddress}
                    />
                    <label>
                        Sử dụng địa chỉ của tôi: {user?.address || "Chưa có địa chỉ"}
                    </label>
                </section>

                {!useMyAddress && (
                    <section className="address-input">
                        <select
                            onChange={(e) => setSelectedAddress(e.target.value)}
                            value={selectedAddress}
                        >
                            <option value="">-- Chọn Quận/Huyện --</option>
                            {travelExpenses.map((address) => (
                                <option key={address.expenseID} value={address.endLocation}>
                                    {address.endLocation} - {address.fee}VND
                                </option>
                            ))}
                        </select>
                        {selectedAddress !== "Online" && selectedAddress !== "" && (
                            <input
                                value={addressDetails}
                                onChange={(e) => setAddressDetails(e.target.value)}
                                placeholder="Nhập thêm chi tiết địa chỉ"
                            />
                        )}
                    </section>
                )}
            </section>


            <section className="section payment-section">
                {selectedAddress === "Online" ? (
                    <b>Dịch vụ online yêu cầu thanh toán trước</b>
                ) : (
                    <div>
                        <input
                            id="payCheckbox"
                            type="checkbox"
                            checked={pay}
                            onChange={() => handleChangePayStatus()}
                        />
                        <label htmlFor="payCheckbox">Thanh toán trước</label>
                    </div>
                )}
                <p>Tổng tiền: {calculateTotal().toLocaleString("vi-VN")} đ</p>
            </section>

            {/* Booking Button */}

            <button className="booking-button" onClick={handleBooking}>
                Đặt Lịch
            </button>
        </div>
    );
};

export default OrdersForm;
