import { React, useContext, useEffect, useState } from 'react';
import { UserContext } from "../UserContext";
import { ActorAlertDetailCard } from '../components/ActorAlertDetailCard';

import './ActorAlertsPage.scss';

export const ActorAlertsPage = () => {
  const { user, setUserContext } = useContext(UserContext);
  const [userActorAlerts, setUserActorAlerts] = useState({});

  useEffect(
    () => {
      const fetchActorAlerts = async () => {
        const response = await fetch('http://localhost:8080/actoralerts', {
          method: 'GET',
          headers: {
            'Authorization': `Bearer ${user.tokenId}`
          }
        });
        const userActorAlerts = await response.json();
        setUserActorAlerts(userActorAlerts);
      };
      
      if (user) {
        fetchActorAlerts();
      }
    }, []
  );

  if (!user) {
    return <h3>Login to configure your Actor Alerts</h3>
  }

  if (!userActorAlerts || !userActorAlerts.actorAlerts || userActorAlerts.actorAlerts.length === 0) {

    return <h3>No Actor Alerts configured</h3>
  }

  return (
    <div className="ActorAlertsPage">
      {
        !user ? "Please login to create Actor Alerts" : ""
      }

      {userActorAlerts.actorAlerts
        .map(actorAlert => <ActorAlertDetailCard key={actorAlert.actorId} actorId={actorAlert.actorId} />)
      }
    </div>
  );
}