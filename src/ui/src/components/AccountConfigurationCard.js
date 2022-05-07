import React, { useState, useContext, useEffect } from "react";
import { UserContext } from "../UserContext";

import "./scss/AccountConfigurationCard.scss";
import checkboxesData from './FlatrateProviders.json';

export const AccountConfigurationCard = () => {
    const { loggedInUser, setLoggedInUserContext } = useContext(UserContext);
    const [modal, setModal] = useState(false);
    const [selectedCheckboxes, setSelectedCheckboxes] = useState(null);

    useEffect(
        () => {
            setSelectedCheckboxes(new Set(loggedInUser.streamingServices));
        }, []
    );

    const handleCheckboxChange = (event) => {
        const itemKey = event.target.value;
        const newelectedCheckboxes = selectedCheckboxes ? new Set(selectedCheckboxes) : new Set();

        if (!newelectedCheckboxes.has(itemKey)) {
            newelectedCheckboxes.add(itemKey)
        } else {
            newelectedCheckboxes.delete(itemKey)
        }
        setSelectedCheckboxes(newelectedCheckboxes);
    }

    const toggleModal = () => {
        setModal(!modal);
    };

    const saveAndCloseModal = () => {
        setModal(!modal);
        let user = {};
        user.streamingServices = Array.from(selectedCheckboxes);

        const persistUserSubscriptions = async () => {
            const response = await fetch(process.env.REACT_APP_BACKEND_URL + 'user', {
                method: 'POST',
                headers: {
                    "Content-Type": "application/json",
                    'Authorization': `Bearer ${loggedInUser.tokenId}`
                },
                body: JSON.stringify(user)
            });
            const updatedUser = await response.json();

            loggedInUser.streamingServices = updatedUser.streamingServices;
            localStorage.setItem("loginData", JSON.stringify(loggedInUser));
            setLoggedInUserContext({ ...loggedInUser });
        }

        persistUserSubscriptions();
    };

    if (modal) {
        document.body.classList.add('active-modal')
    } else {
        document.body.classList.remove('active-modal')
    }

    return (
        <div className="AccountConfigurationCard">
            <div className="account-configuration-card-flatrate-provider-section">
                {
                    (loggedInUser && loggedInUser.streamingServices && loggedInUser.streamingServices.length > 0)
                        ? checkboxesData.map(
                            data =>
                                loggedInUser.streamingServices.includes(data.value) ?
                                    <div key={data.key} className="account-configuration-card-flatrate-provider-image"> <img alt={data.label} title={data.label} src={data.img} /></div>
                                    : ""
                        )
                        : ""
                }
                <button onClick={toggleModal} className="btn-modal">
                    Configure subscriptions
                </button>
            </div>
            {modal && (
                <div className="modal">
                    <div onClick={toggleModal} className="overlay"></div>
                    <div className="modal-content">
                        <div>
                            <h2>Select your streaming services</h2>
                            {
                                checkboxesData.map(
                                    checkbox =>
                                        <div key={checkbox.value} className="account-configuration-card-checkbox">
                                            <input type="checkbox" name={checkbox.value} value={checkbox.value} checked={selectedCheckboxes && selectedCheckboxes.has(checkbox.value) ? true : false} onChange={handleCheckboxChange} />
                                            <img alt={checkbox.label} title={checkbox.label} src={checkbox.img} />
                                            <label>{checkbox.label}</label>
                                        </div>
                                )
                            }
                        </div>
                        <button className="close-modal" onClick={saveAndCloseModal}>
                            Save and Close
                        </button>
                    </div>
                </div>
            )}
        </div>
    );
}