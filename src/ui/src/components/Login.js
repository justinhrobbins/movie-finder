import React, { useContext, useEffect } from 'react';
import { GoogleLogin } from 'react-google-login';
import { UserContext } from "../UserContext";
import { AccountConfiguration } from './AccountConfiguration';

import './scss/Login.scss';

export const Login = () => {
    const { loggedInUser, setLoggedInUserContext } = useContext(UserContext);

    useEffect(
        () => {
            setTimeout(checkForValidLogin, 5000);
        }, []
      );

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
        userData.expiresAt = googleData.tokenObj.expires_at;
        localStorage.setItem("loginData", JSON.stringify(userData));
        setLoggedInUserContext(userData);

        refreshTokenSetup(googleData);
    };

    const logoutAndCleanupUser = () => {
        localStorage.removeItem('loginData');
        setLoggedInUserContext(null);        
    }

    const handleLogout = () => {
        logoutAndCleanupUser();
    };

    const refreshTokenSetup = (res) => {
        let refreshTiming = ( (res.tokenObj.expires_in || 3600) - 1800) * 1000;
        console.log("Next refreshTiming at: " + refreshTiming);

        const refreshToken = async () => {
            console.log("Entering refreshToken()");
            const loginData = JSON.parse(localStorage.getItem('loginData'));
            if (!loginData) return;

            const newAuthRes = await res.reloadAuthResponse();
            console.log("Updating expiresAt to " + newAuthRes.expires_at);
            refreshTiming = ( (newAuthRes.expires_in || 3600) - 1800) * 1000;
            console.log("Next refreshTiming in " + refreshTiming);
            
            loginData.tokenId = newAuthRes.id_token;
            loginData.expiresAt = newAuthRes.expires_at;
            localStorage.setItem('loginData', JSON.stringify(loginData));
            setLoggedInUserContext(loginData);

            setTimeout(refreshToken, refreshTiming);
        };

        setTimeout(refreshToken, refreshTiming);
    };

    const checkForValidLogin = () => {
        // console.log("Checking for valid login");
        // if ((loggedInUser) && (loggedInUser.expiresAt < Date.now())) {
        //     console.log("loggedInUser: " + loggedInUser);
        //     console.log("Login is expired, cleaning up");
        //     logoutAndCleanupUser();
        // }
        // setTimeout(checkForValidLogin, 5000);
    }

    return (
        <div className="Login">
            {loggedInUser ? (
                <div className="login-logged-in">
                    <div>
                        <span className="login-logged-in-label">Logged in as {loggedInUser.name}</span>
                        <button onClick={handleLogout}>Logout</button>
                    </div>
                    <div><AccountConfiguration /></div>
                </div>
            ) : (
                <div className="login-logged-in">
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