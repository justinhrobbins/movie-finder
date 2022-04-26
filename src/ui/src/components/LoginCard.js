import React, { useContext } from 'react';
import { GoogleLogin } from 'react-google-login';
import { UserContext } from "../UserContext";

import './LoginCard.scss';

export const LoginCard = () => {
    const { loggedInUser, setLoggedInUserContext } = useContext(UserContext);

    const handleFailure = (failure) => {
        console.log(failure);
    };

    const handleLogin = async (googleData) => {

        const response = await fetch('http://localhost:8080/user', {
            method: 'GET',
            headers: {
                'Authorization': `Bearer ${googleData.tokenId}`,
            }
        });

        const userData = await response.json();
        userData.tokenId = googleData.tokenId;
        localStorage.setItem("loginData", JSON.stringify(userData));

        setLoggedInUserContext(userData);
    };

    const handleLogout = () => {
        localStorage.removeItem('loginData');
        setLoggedInUserContext(null);
    };

    return (
        <div className="LoginCard">
            {loggedInUser ? (
                <div className="login-card-logged-in">
                    <div>You logged in as {loggedInUser.email}</div>
                    <button onClick={handleLogout}>Logout</button>
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