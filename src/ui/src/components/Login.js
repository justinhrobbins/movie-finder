import React, { useContext, useEffect } from 'react';
import { GoogleLogin } from '@react-oauth/google';
import { googleLogout } from '@react-oauth/google';
import { useInterval } from 'usehooks-ts'
import { UserContext } from "../UserContext";
import { AccountConfiguration } from './AccountConfiguration';

import './scss/Login.scss';

export const Login = () => {

    const { loggedInUser, setLoggedInUserContext } = useContext(UserContext);

    const now = () => {
        return Date.now();
    }

    useInterval(
        () => {
            console.log("running setInterval()");
            if (loggedInUser && loggedInUser.expiresIn) {

                const expiresIn = Date.parse(loggedInUser.expiresIn);
                if (expiresIn < now()) {
                    console.log(expiresIn + " is older than " + now());
                    logoutAndCleanupUser();
                }
            }
        },
        5000
      )

    const handleFailure = (failure) => {
        console.log(failure);
    };

    const handleLogin = async (googleData) => {
        const response = await fetch(process.env.REACT_APP_BACKEND_URL + 'user', {
            method: 'GET',
            headers: {
                'Authorization': `Bearer ${googleData.credential}`,
            }
        });

        console.log(googleData);
        const userData = await response.json();
        userData.tokenId = googleData.credential;
        userData.expiresIn = new Date(Date.now() + 30 * 60000)

        localStorage.setItem("loginData", JSON.stringify(userData));
        setLoggedInUserContext(userData);
    };

    const logoutAndCleanupUser = () => {
        localStorage.removeItem('loginData');
        setLoggedInUserContext(null);
    }

    const handleLogout = () => {
        logoutAndCleanupUser();
        googleLogout();
    };

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
                        onSuccess={handleLogin}
                        onError={handleFailure}
                        theme="filled_black"
                        useOneTap
                        auto_select
                    />
                </div>
            )}
        </div>
    );
}