import React, { useContext } from 'react';
import { GoogleLogin } from '@react-oauth/google';
import { UserContext } from "../UserContext";
import { AccountConfigurationCard } from './AccountConfigurationCard';

// import './scss/NewLoginCard.scss';

export const NewLoginCard = () => {
    const { loggedInUser, setLoggedInUserContext } = useContext(UserContext);

    const handleFailure = (failure) => {
        console.log(failure);
    };

    const handleLogin = async (googleData) => {
        console.log(JSON.stringify(googleData));

        const response = await fetch(process.env.REACT_APP_BACKEND_URL + 'user', {
            method: 'GET',
            headers: {
                'Authorization': `Bearer ${googleData.credential}`,
            }
        });

        const userData = await response.json();
        userData.tokenId = googleData.credential;
        localStorage.setItem("loginData", JSON.stringify(userData));

        setLoggedInUserContext(userData);
    };

    const handleLogout = () => {
        localStorage.removeItem('loginData');
        setLoggedInUserContext(null);
    };

    return (
        <div className="NewLoginCard">
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
                        onSuccess={handleLogin}
                        onError={handleFailure}
                        theme="filled_black"
                    />
                </div>
            )}
        </div>
    );
}