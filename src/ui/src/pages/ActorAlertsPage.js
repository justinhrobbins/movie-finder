import { React, useContext, useEffect, useState } from 'react';
import { UserContext } from "../UserContext";
import { ActorAlertDetailCard } from '../components/ActorAlertDetailCard';

import './ActorAlertsPage.scss';

export const ActorAlertsPage = () => {
  const { loggedInUser, setLoggedInUserContext } = useContext(UserContext);
  const [userActorAlerts, setUserActorAlerts] = useState({});

  useEffect(
    () => {
      const fetchActorAlerts = async () => {
        const response = await fetch('http://localhost:8080/actoralerts', {
          method: 'GET',
          headers: {
            'Authorization': `Bearer ${loggedInUser.tokenId}`
          }
        });
        const userActorAlerts = await response.json();
        setUserActorAlerts(userActorAlerts);
      };

      if (loggedInUser) {
        fetchActorAlerts();
      }
    }, []
  );

  if (!loggedInUser) {
    return <h3>Login to configure your Actor Alerts</h3>
  }

  if (!userActorAlerts || !userActorAlerts.actorAlerts || userActorAlerts.actorAlerts.length === 0) {

    return <h3>Loading your Actor Alerts...</h3>
  }

  return (
    <div className="ActorAlertsPage">
      {
        !loggedInUser ? "Please login to create Actor Alerts" : ""
      }
      <div className="ActorAlertsPage">
        {userActorAlerts.actorAlerts
          .map(actorAlert => <ActorAlertDetailCard key={actorAlert.actorId} providedActor={actorAlert.person} actorDetails={actorAlert.details} />)
        }
      </div>
    </div>
  );
}