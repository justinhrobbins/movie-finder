import React, { useState, useContext, useEffect } from "react";
import { UserContext } from "../UserContext";

import "./AccountConfigurationCard.scss";

export const AccountConfigurationCard = () => {
    const { loggedInUser, setLoggedInUserContext } = useContext(UserContext);
    const [modal, setModal] = useState(false);
    const [selectedCheckboxes, setSelectedCheckboxes] = useState(null);

    const checkboxesData = [
        {
            value: 'Amazon_Prime',
            label: 'Amazon Prime Video',
            img: 'https://image.tmdb.org/t/p/w45//emthp39XA2YScoYL1p0sdbAH2WA.jpg',
        },
        {
            value: 'Apple_Plus',
            label: 'Apple TV Plus',
            img: 'https://image.tmdb.org/t/p/w45//6uhKBfmtzFqOcLousHwZuzcrScK.jpg',
        },
        {
            value: 'Diney_Plus',
            label: 'Disney Plus',
            img: 'https://image.tmdb.org/t/p/w45//7rwgEs15tFwyR9NPQ5vpzxTj19Q.jpg',
        },
        {
            value: 'HBO_Max',
            label: 'HBO Max',
            img: 'https://image.tmdb.org/t/p/w45//rrta9psrx3e7F9wLUfpANdJzudx.jpg',
        },
        {
            value: 'Netflix',
            label: 'Netflix',
            img: 'https://image.tmdb.org/t/p/w45//t2yyOv40HZeVlLjYsCsPHnWLk4W.jpg',
        }
    ];

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

        console.log("selectedCheckboxes: " + JSON.stringify([...selectedCheckboxes]));

        loggedInUser.streamingServices = [...selectedCheckboxes];

        localStorage.setItem("loginData", JSON.stringify(loggedInUser));
        setLoggedInUserContext(loggedInUser);
    };

    if (modal) {
        document.body.classList.add('active-modal')
    } else {
        document.body.classList.remove('active-modal')
    }

    return (
        <div className="AccountConfigurationCard">
            <button onClick={toggleModal} className="btn-modal">
                Configure
            </button>

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