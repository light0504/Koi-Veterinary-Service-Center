// PrivateRoute.jsx
import React, { useEffect, useState } from 'react';
import { Navigate } from 'react-router-dom';

const PrivateRoute = ({ Component, role}) => {
    const [isAuthenticated, setIsAuthenticated] = useState(true);

    useEffect(() => {
        const token = sessionStorage.getItem('user');
        if (token) {
            if (role === sessionStorage.getItem('role')) {
                setIsAuthenticated(true);
            } else {
                setIsAuthenticated(false);
            }
        } else {
            setIsAuthenticated(false);
        }
        setTimeout(() => console.log(isAuthenticated), 5000)
    }, []);

    return isAuthenticated ? <Component /> : <Navigate to='/login' />;
};

export default PrivateRoute;
