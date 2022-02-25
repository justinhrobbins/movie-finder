import { React, useState } from 'react';
import { GoogleLogin } from 'react-google-login';

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
                <div>
                    <h3>You logged in as {loginData.email}</h3>
                    <button onClick={handleLogout}>Logout</button>
                </div>
            ) : (
                <GoogleLogin
                    clientId={process.env.REACT_APP_GOOGLE_CLIENT_ID}
                    buttonText="Log in with Google"
                    onSuccess={handleLogin}
                    onFailure={handleFailure}
                    cookiePolicy={'single_host_origin'}
                />
            )}
        </div>
    );
}