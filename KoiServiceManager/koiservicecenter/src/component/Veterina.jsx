import { fetchVeterinas } from '../config/api.jsx';
import { useEffect, useState } from 'react';
import './css/Veterina.css';

const Veterina = () => {
    const [veterinas, setVeterinas] = useState([]);

    useEffect(() => {
        const loadData = async () => {
            try {
                const veterinasData = await fetchVeterinas();
                setVeterinas(veterinasData);
            } catch (error) {
                console.error("Error fetching veterinas:", error);
            }
        };

        loadData();
    }, []);

    return (
        <div className={'veterinaTable'}>
            <h1 className={'veterinaLabel'}>Danh Sách Bác Sĩ Thú Y</h1>

            <table border="1" className={'table'}>
                <thead>
                    <tr>
                        <th className={'th'}>Tên bác sĩ</th>
                        <th className={'th'}>Mô Tả</th>
                    </tr>
                </thead>
                <tbody>
                    {veterinas.length > 0 ? (
                        veterinas.filter(vet => vet.status).map((veterina) => (
                            <tr key={veterina.veterinaID} className={'tr'}>
                                <td className={'td'}>{veterina.name}</td>
                                <td className={'td'}>{veterina.description}</td>
                            </tr>
                        ))
                    ) : (
                        <tr>
                            <td colSpan="3" className={'noDoctor'}>Không có bác sĩ nào.</td>
                        </tr>
                    )}
                </tbody>
            </table>
        </div>
    );
};

export default Veterina;
