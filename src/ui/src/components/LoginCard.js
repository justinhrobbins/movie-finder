import { React, useState } from 'react';
import { GoogleLogin } from 'react-google-login';

import './LoginCard.scss';

export const LoginCard = () => {
    const [loginData, setLoginData] = useState(
        localStorage.getItem('loginData')
            ? JSON.parse(localStorage.getItem('loginData'))
            : null
    );

    const handleFailure = (failure) => {
        console.log(failure);
    };

    const handleLogin = async (googleData) => {
        const name = (googleData.profileObj.name);

        const response = await fetch('http://localhost:8080/user', {
            method: 'GET',
            headers: {
                'Authorization': `Bearer ${googleData.tokenId}`,
            }
        });

        const userData = await response.json();
        setLoginData(userData);
        localStorage.setItem("loginData", JSON.stringify(userData));
    };

    const handleLogout = () => {
        localStorage.removeItem('loginData');
        setLoginData(null);
    };

    return (
        <div className="LoginCard">
            {loginData ? (
                <div className="login-card-logged-in">
                    <div>You logged in as {loginData.email}</div>
                    <button onClick={handleLogout}>Logout</button>
                    <div>My Actor Alerts</div>
                </div>
            ) : (
                <div className="login-card-logged-in">
                    <GoogleLogin
                        clientId={process.env.REACT_APP_GOOGLE_CLIENT_ID}
                        buttonText="Log in with Google"
                        onSuccess={handleLogin}
                        onFailure={handleFailure}
                        cookiePolicy={'single_host_origin'}
                    />
                    <div>Login to access your Actor Alerts</div>
                </div>
            )}
        </div>
    );
}