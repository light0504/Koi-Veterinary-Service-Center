import { Route, Routes } from 'react-router-dom';
import Home from '../src/Home.jsx';
import Header from './component/Header.jsx';
import AboutUs from './component/AboutUs.jsx';
import Faq from './component/FAQ.jsx';
import Login from './Login_SignUp.jsx';
import Service from './component/Service.jsx';
import Veterina from './component/Veterina.jsx';
import OrdersForm from './component/OrdersForm.jsx';
import Profile from './component/Profile.jsx';
import InvoicePage from './component/InvoicePage.jsx'

import StaffPage from '../staff/StaffPage.jsx';

import Manager from '../manager/ManagerPage.jsx'

import VeterinaPage from '../veterina/VeterinaPage.jsx';

import PrivateRoute from '../src/utils/PrivateRoute.jsx';

function App() {
    return (
        <Routes>
            <Route path='/' element={<Header />}>
                <Route path='/' element={<Home />}/>
                <Route path='/aboutus' element={<AboutUs />}/>
                <Route path='/faq' element={<Faq />}/>
                <Route path='/service' element={<Service />}/>
                <Route path='/veterina' element={<Veterina />}/>
                <Route path='/login' element={<Login />}/>
                <Route path='/orderform' element={<PrivateRoute Component={OrdersForm} role='C'/>}/>
                {/* <Route path='/orderform' element={<OrdersForm />}/> */}
                <Route path='/profile' element={<Profile />} />
                <Route path='/invoice' element={<InvoicePage/> }/>
            </Route>

            <Route path='/staffPage' element={<PrivateRoute Component={StaffPage} role='S' />} />
            <Route path='/managerPage' element={<PrivateRoute Component={Manager} role ='M'/>}/>
            <Route path='/veterinaPage' element={<PrivateRoute Component={VeterinaPage} role='V' />} />
        </Routes>
        
    );
}

export default App;
