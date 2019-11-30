import React, { useState } from "react";

import { makeStyles } from "@material-ui/core/styles";
import Button from "@material-ui/core/Button";
import axios from "../../axios-iot";
import Card from "@material-ui/core/Card";
import JSONPretty from "react-json-pretty";
import CardContent from "@material-ui/core/CardContent";

const useStyles = makeStyles(theme => ({
  button: {
    margin: theme.spacing(1)
  },
  input: {
    display: "none"
  },
  card: {
    minWidth: 275,
    margin: "10px"
  },
  bullet: {
    display: "inline-block",
    margin: "0 2px",
    transform: "scale(0.8)"
  },
  title: {
    fontSize: 14
  },
  pos: {
    marginBottom: 12
  }
}));

const IOTMessage = props => {
  const {
    device,
    deviceTypeID,
    deviceID,
    groupID,
    valueMin,
    valueMax,
    url
  } = props;
  const classes = useStyles();

  const [message, setMessage] = useState(null);
  const [clicked, setClicked] = useState(false);
  const [timerID, setTimerID] = useState(null);

  const onClicked = () => {
    setClicked(!clicked);

    if (!clicked) {
      setTimerID(
        setInterval(() => {
          const data = JSON.stringify({
            deviceTypeId: deviceTypeID,
            deviceId: deviceID,
            value:
              (Math.random() * (valueMax - valueMin + 1) + valueMin).toFixed(2),
            groupId: groupID,
            timestamp: new Date()
          });

          axios
            .post(url, data)
            .then(res => {
              setMessage(data);
            })
            .catch(err => {
              console.log(err.message);
              setMessage(err);
            });
        }, 1000)
      );
    } else {
      clearInterval(timerID);
    }
  };

  let messageCard = null;
  if (!clicked) {
    messageCard = null;
  } else if (message !== null) {
    console.log(message);
    messageCard = (
      <Card className={classes.card}>
        <CardContent>
          <div>
            Request Sent
            <JSONPretty id="json-pretty" data={message}></JSONPretty>
          </div>
        </CardContent>
      </Card>
    );
  }

  return (
    <div>
      <Button
        variant="contained"
        color="primary"
        className={classes.button}
        onClick={() => onClicked()}
      >
        {!clicked ? "Start sending " : "Stop sending "}
        {device}
      </Button>
      {messageCard}
    </div>
  );
};

export default IOTMessage;
