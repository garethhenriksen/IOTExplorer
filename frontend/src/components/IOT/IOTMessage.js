import React, { useState } from "react";

import { makeStyles } from "@material-ui/core/styles";
import Button from "@material-ui/core/Button";
import axios from "../../axios-football";

const useStyles = makeStyles(theme => ({
  button: {
    margin: theme.spacing(1)
  },
  input: {
    display: "none"
  }
}));

const IOTMessage = props => {
  const { device, deviceTypeID, deviceID, groupID, valueMin, valueMax } = props;
  const classes = useStyles();

  const [message, setMessage] = useState("");
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
              Math.floor(Math.random() * (valueMax - valueMin + 1)) + valueMin,
            groupId: groupID,
            timestamp: new Date()
          });

          axios
            .post("/publish", data)
            .then(res => {
              console.log("done");
              setMessage("request sent [" + data + "]");
            })
            .catch(err => {
              console.log(err.message);
              setMessage(err.message);
            });
            axios
            .post("/publish/kafka", data)
            .then(res => {
              console.log("done");
              setMessage("request sent [" + data + "]");
            })
            .catch(err => {
              console.log(err.message);
              setMessage(err.message);
            });
        }, 1000)
      );
    } else {
      clearInterval(timerID);
    }
  };
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
      {message}
    </div>
  );
};

export default IOTMessage;
