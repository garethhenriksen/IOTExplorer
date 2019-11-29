import React, { useState } from "react";

import { makeStyles } from "@material-ui/core/styles";
import TextField from "@material-ui/core/TextField";
import Button from "@material-ui/core/Button";
import Typography from "@material-ui/core/Typography";
import Slider from "@material-ui/core/Slider";
import axios from "../../axios-iot";
import Card from "@material-ui/core/Card";
import JSONPretty from "react-json-pretty";
import CardContent from "@material-ui/core/CardContent";

const useStyles = makeStyles(theme => ({
  root: {
    "& > *": {
      margin: theme.spacing(1),
      width: 150
    }
  },
  button: {
    margin: theme.spacing(1)
  }
}));

const IOTCustomMessage = props => {
  const { url } = props;
  const classes = useStyles();
  const [message, setMessage] = useState(null);
  const [range, setRange] = useState([0, 100]);
  const [intervalValue, setIntervalValue] = useState(500);
  const [timerID, setTimerID] = useState(null);
  const [clicked, setClicked] = useState(false);
  const [state, setState] = useState({
    deviceId: null,
    deviceTypeId: null,
    groupId: null
  });

  const handleRangeChange = (event, newValue) => {
    setRange(newValue);
  };

  const handleIntervalChange = (event, newValue) => {
    setIntervalValue(newValue);
  };

  const handleChange = event => {
    const value = event.target.value;
    setState({
      ...state,
      [event.target.name]: value
    });
  };

  function valuetext(value) {
    return `${value}`;
  }

  const onClicked = () => {
    if (
      isNaN(state.deviceTypeId) ||
      isNaN(state.deviceId) ||
      isNaN(state.groupId) ||
      state.deviceTypeId === null ||
      state.deviceId === null ||
      state.groupId === null
    ) {
      alert(
        "please enter numeric value for deviceId, deviceTypeId and groupId"
      );
    } else {
      setClicked(!clicked);

      if (!clicked) {
        setTimerID(
          setInterval(() => {
              console.log("object")
            const data = JSON.stringify({
              deviceTypeId: state.deviceTypeId,
              deviceId: state.deviceId,
              value:
                Math.floor(Math.random() * (range[1] - range[0] + 1)) +
                range[0],
              groupId: state.groupId,
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
          }, intervalValue)
        );
      } else {
        clearInterval(timerID);
      }
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
      <form className={classes.root} noValidate autoComplete="off">
        <TextField
          id="deviceTypeId"
          name="deviceTypeId"
          label="deviceTypeId"
          onChange={handleChange}
        />
        <TextField
          id="deviceId"
          name="deviceId"
          label="deviceId"
          onChange={handleChange}
        />
        <TextField
          id="groupId"
          name="groupId"
          label="groupId"
          onChange={handleChange}
        />
        <Typography id="range-slider" gutterBottom>
          Value range
        </Typography>
        <Slider
          value={range}
          onChange={handleRangeChange}
          valueLabelDisplay="auto"
          aria-labelledby="range-slider"
          getAriaValueText={valuetext}
        />
        <Typography id="discrete-slider" gutterBottom>
          Interval to send (ms)
        </Typography>
        <Slider
          defaultValue={500}
          onChange={handleIntervalChange}
          getAriaValueText={valuetext}
          aria-labelledby="discrete-slider"
          valueLabelDisplay="auto"
          step={100}
          min={100}
          max={2000}
        />
      </form>
      <Button
        variant="contained"
        color="primary"
        className={classes.button}
        onClick={() => onClicked()}
      >
        {!clicked
          ? "Start sending Custom Request"
          : "Stop sending Custom Request"}
      </Button>
      {messageCard}
    </div>
  );
};

export default IOTCustomMessage;
