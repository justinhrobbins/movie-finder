import React, { useContext } from 'react';
import { GoogleLogin } from 'react-google-login';
import { UserContext } from "../UserContext";
import { AccountConfigurationCard } from './AccountConfigurationCard';

import './scss/LoginCard.scss';

export const LoginCard = () => {
    const { loggedInUser, setLoggedInUserContext } = useContext(UserContext);

    const handleFailure = (failure) => {
        console.log(failure);
    };

    const handleLogin = async (googleData) => {

        const response = await fetch(process.env.REACT_APP_BACKEND_URL + 'user', {
            method: 'GET',
            headers: {
                'Authorization': `Bearer ${googleData.tokenId}`,
            }
        });

        const userData = await response.json();
        userData.tokenId = googleData.tokenId;
        localStorage.setItem("loginData", JSON.stringify(userData));
        setLoggedInUserContext(userData);

        refreshTokenSetup(googleData);
    };

    const handleLogout = () => {
        localStorage.removeItem('loginData');
        setLoggedInUserContext(null);
    };

    const refreshTokenSetup = (res) => {
        let refreshTiming = (res.tokenObj.expires_in || 3600 - 5 * 60) * 1000;

        const refreshToken = async () => {
            
            const loginData = JSON.parse(localStorage.getItem('loginData'));
            if (!loginData) return;

            const newAuthRes = await res.reloadAuthResponse();
            refreshTiming = (newAuthRes.expires_in || 3600 - 5 * 60) * 1000;
            
            loginData.tokenId = newAuthRes.id_token;
            localStorage.setItem('loginData', JSON.stringify(loginData));
            setLoggedInUserContext(loginData);

            setTimeout(refreshToken, refreshTiming);
        };

        setTimeout(refreshToken, refreshTiming);
    };

    return (
        <div className="LoginCard">
            {loggedInUser ? (
                <div className="login-card-logged-in">
                    <div>
                        <span className="login-card-logged-in-label">Logged in as {loggedInUser.name}</span>
                        <button onClick={handleLogout}>Logout</button>
                    </div>
                    <div><AccountConfigurationCard /></div>
                </div>
            ) : (
                <div className="login-card-logged-in">
                    <GoogleLogin
                        clientId={process.env.REACT_APP_GOOGLE_CLIENT_ID}
                        buttonText="Login with Google"
                        onSuccess={handleLogin}
                        onFailure={handleFailure}
                        cookiePolicy={'single_host_origin'}
                    />
                </div>
            )}
        </div>
    );
}