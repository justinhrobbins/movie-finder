import { React, useState } from 'react';
import { GoogleLogin } from 'react-google-login';

export const LoginCard = () => {
    const [loginData, setLoginData] = useState(
        localStorage.getItem('loggedInUser')
            ? JSON.parse(localStorage.getItem('loggedInUser'))
            : null
    );

    const handleFailure = (response) => {
        console.log(response.profileObj.name);
    }

    const handleLogin = (response) => {
        const name = (response.profileObj.name);
        setLoginData(name);
        localStorage.setItem("loggedInUser", JSON.stringify(name));
    }

    const handleLogout = () => {
        localStorage.removeItem('loggedInUser');
        setLoginData(null);
    };

    return (
        <div className="LoginCard">
            {loginData ? (
                <div>
                    <h3>You logged in as {loginData}</h3>
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