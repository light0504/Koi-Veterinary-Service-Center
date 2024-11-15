import React, { useEffect, useState } from 'react';
import { fetchFish, addFish, updateFish, deleteFish } from '../config/api.jsx';
import './css/FishTable.css'; // Ensure to use the correct CSS file

const FishTable = ({ userID, role }) => {
    const [fishList, setFishList] = useState([]);
    const [showAddForm, setShowAddForm] = useState(false);
    const [newFish, setNewFish] = useState({
        weight: '',
        length: '',
        month: '',
        describe: ''
    });

    const [updatingFish, setUpdatingFish] = useState(null);

    const loadFishData = async () => {
        try {
            const fishData = await fetchFish(userID);
            setFishList(fishData);
        } catch (error) {
            console.error("Failed to fetch fish data:", error);
        }
    };

    useEffect(() => {
        loadFishData();
    }, []);

    const handleInputChange = (e) => {
        const { name, value } = e.target;
        setNewFish((prev) => ({
            ...prev,
            [name]: value
        }));
    };

    const validateFishData = (fish) => {
        if (fish.weight <= 0 || fish.weight >= 10) {
            alert('Trọng lượng phải lớn hơn 0 và nhỏ hơn 10.');
            return false;
        }
        if (fish.length <= 0 || fish.length >= 120) {
            alert('Độ dài phải lớn hơn 0 và nhỏ hơn 120.');
            return false;
        }
        if (fish.month <= 0 || fish.month >= 1000) {
            alert('Tháng tuổi phải lớn hơn 0 và nhỏ hơn 1000.');
            return false;
        }
        return true;
    };

    // Add new fish
    const handleAddFish = async (e) => {
        e.preventDefault();
        if (!validateFishData(newFish)) {
            return;
        }
        try {
            const newFishData = { ...newFish, userID };
            const result = await addFish(newFishData);

            if (result && !result.error) {
                setShowAddForm(false);
                setNewFish({ weight: '', length: '', month: '', describe: '' });
                loadFishData(); // Reload fish list
            } else {
                console.error("Failed to add fish:", result?.error);
            }
        } catch (error) {
            console.error("Error adding fish:", error);
        }
    };

    const handleUpdate = async () => {
        if (!validateFishData(updatingFish)) {
            return;
        }
        try {
            const fishId = updatingFish.fishID;
            setUpdatingFish({
                ...updatingFish,
                userID,
                fishId: null
            });

            const response = await updateFish(fishId, updatingFish);

            if (response) {
                setUpdatingFish({ ...updatingFish, fishId: fishId });
                alert('Cập nhật thành công');
                // Update fishList in local state
                setFishList(prevFishList => prevFishList.map(f => f.fishID === fishId ? updatingFish : f));
            }
        } catch (error) {
            console.log(error);
        } finally {
            setUpdatingFish(null);
        }
    };

    const handleDelete = async (id) => {
        try {
            const response = await deleteFish(id);
            if (response) {
                alert('Xóa thành công');
                setFishList(prevFishList => prevFishList.filter(f => f.fishID !== id));
            }
        } catch (error) {
            console.log(error);
        }
    };

    return (
        <section className="fish-table-container">
            {fishList.length > 0 ? (
                <table className="fish-table">
                    <thead>
                        <tr>
                            <th>Id</th>
                            <th>Trọng lượng</th>
                            <th>Độ dài</th>
                            <th>Tháng tuổi</th>
                            <th>Mô tả</th>
                            {role === 'C' && (
                                <>
                                    <th>Update</th>
                                    <th>Delete</th>
                                </>
                            )}
                        </tr>
                    </thead>
                    <tbody>
                        {fishList.map(fish => (
                            <tr key={fish.fishID}>
                                <td>{fish.fishID}</td>
                                <td>{fish.weight}</td>
                                <td>{fish.length}</td>
                                <td>{fish.month}</td>
                                <td>{fish.describe}</td>
                                {role === 'C' && (
                                    <>
                                        <td><button className="update-button" onClick={() => setUpdatingFish(fish)}>Cập nhật</button></td>
                                        <td><button className="delete-button" onClick={() => handleDelete(fish.fishID)}>Xóa</button></td>
                                    </>
                                )}
                            </tr>
                        ))}
                    </tbody>
                </table>
            ) : (
                <div className="no-data-message">Chưa thêm con cá nào</div>
            )}

            {role === 'C' && (
                <section className="add-fish-section">
                    <button className="toggle-add-fish-button" onClick={() => setShowAddForm(!showAddForm)}>
                        {showAddForm ? 'Hủy' : 'Thêm cá mới'}
                    </button>
                    {showAddForm && (
                        <section className="add-fish-form">
                            <form onSubmit={handleAddFish}>
                                <section className="form-col">
                                    <div>
                                        <label>Trọng lượng (kg):</label>
                                        <input type="number" name="weight" value={newFish.weight} onChange={handleInputChange} required />
                                    </div>
                                    <div>
                                        <label>Độ dài (cm):</label>
                                        <input type="number" name="length" value={newFish.length} onChange={handleInputChange} required />
                                    </div>
                                    <div>
                                        <label>Tháng tuổi:</label>
                                        <input type="number" name="month" value={newFish.month} onChange={handleInputChange} required />
                                    </div>
                                    <div>
                                        <label>Mô tả:</label>
                                        <input type="text" name="describe" value={newFish.describe} onChange={handleInputChange} required />
                                    </div>
                                </section>
                                <button type="submit" className="submit-button">Submit</button>
                            </form>
                        </section>
                    )}
                </section>
            )}

            {updatingFish && (
                <section>
                    <h2>Sửa thông tin cá Koi</h2>
                    <div className="input-group">
                        <label>Trọng lượng</label>
                        <input
                            type="number"
                            value={updatingFish.weight}
                            onChange={(e) => setUpdatingFish({ ...updatingFish, weight: parseFloat(e.target.value) })}
                        />
                    </div>
                    <div className="input-group">
                        <label>Độ dài</label>
                        <input
                            type="number"
                            value={updatingFish.length}
                            onChange={(e) => setUpdatingFish({ ...updatingFish, length: parseInt(e.target.value) })}
                        />
                    </div>
                    <div className="input-group">
                        <label>Tháng tuổi</label>
                        <input
                            type="number"
                            value={updatingFish.month}
                            onChange={(e) => setUpdatingFish({ ...updatingFish, month: parseInt(e.target.value) })}
                        />
                    </div>
                    <div className="input-group">
                        <label>Mô tả</label>
                        <input
                            type="text"
                            min="1"
                            value={updatingFish.describe}
                            onChange={(e) => setUpdatingFish({ ...updatingFish, describe: e.target.value })}
                        />
                    </div>
                    <button className="button" onClick={handleUpdate}>Cập nhật thông tin</button>
                    <button className="button" onClick={() => setUpdatingFish(null)}>Hủy</button>
                </section>
            )}
        </section>
    );
};

export default FishTable;
