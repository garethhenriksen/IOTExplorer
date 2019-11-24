import React, { useState } from "react";

import Grid from "@material-ui/core/Grid";

import IOTMessage from "../components/IOT/IOTMessage";
import IOTQuerySearch from "../components/IOT/IOTQuerySearch";
import Divider from "@material-ui/core/Divider";
import CircularProgress from "@material-ui/core/CircularProgress";
import Typography from "@material-ui/core/Typography";
import { makeStyles } from "@material-ui/core/styles";

import axios from "../axios-football";
import moment from "moment";
import IOTQueryResults from "../components/IOT/IOTQueryResults";
import IOTCustomMessage from "../components/IOT/IOTCustomMessage";

const useStyles = makeStyles({
  heading: {
    marginTop: "10px",
    textAlign: "center"
  }
});

function IOT() {
  const [queryResults, setQueryResults] = useState(null);
  const [searchClicked, setSearchClicked] = useState(false);
  const kafkaUrl = "publish/kafka";
  const activeMQUrl = "publish/activemq";
  const classes = useStyles();

  const onSearchClickedHandler = (
    deviceTypeId,
    deviceId,
    groupId,
    queryType,
    selectedStartDate,
    selectedEndDate
  ) => {
    setSearchClicked(true);
    axios
      .get("/messages/", {
        params: {
          deviceTypeId: deviceTypeId,
          deviceId: deviceId,
          groupId: groupId,
          query: queryType !== "undefined" ? queryType : null,
          startDate: moment(selectedStartDate).format("YYYY-MM-DD hh:mm"),
          endDate: moment(selectedEndDate).format("YYYY-MM-DD hh:mm")
        }
      })
      .then(res => {
        setSearchClicked(false);
        if (res.data) {
          setQueryResults(res.data);
        }
      })
      .catch(err => {
        setSearchClicked(false);
        alert(err.message);
        console.log(err.message);
      });
  };

  let queryResultsComponent = null;
  if (queryResults !== null) {
    queryResultsComponent = <IOTQueryResults queryResults={queryResults} />;
  }

  return (
    <div>
      <div className={classes.heading}>
        <Typography variant="h6" gutterBottom>
          Send Message to be processed using kafka
        </Typography>
      </div>
      <Grid container spacing={3}>
        <Grid item xs={4}>
          <IOTMessage
            device={"Heartbeat"}
            deviceTypeID={1}
            deviceID={1}
            groupID={1}
            valueMin={75}
            valueMax={90}
            url={kafkaUrl}
          />
        </Grid>
        <Grid item xs={4}>
          <IOTMessage
            device={"Temp"}
            deviceTypeID={2}
            deviceID={1}
            groupID={1}
            valueMin={22}
            valueMax={30}
            url={kafkaUrl}
          />
        </Grid>
        <Grid item xs={4}>
          <IOTMessage
            device={"Car Fuel Reading"}
            deviceTypeID={3}
            deviceID={1}
            groupID={1}
            valueMin={50}
            valueMax={60}
            url={kafkaUrl}
          />
        </Grid>
      </Grid>
      <Divider variant="middle" />
      <div className={classes.heading}>
        <Typography variant="h6" gutterBottom>
          Send Message to be processed using activemq
        </Typography>
      </div>
      <Grid container spacing={3}>
        <Grid item xs={4}>
          <IOTMessage
            device={"Heartbeat"}
            deviceTypeID={1}
            deviceID={1}
            groupID={1}
            valueMin={75}
            valueMax={90}
            url={activeMQUrl}
          />
        </Grid>
        <Grid item xs={4}>
          <IOTMessage
            device={"Temp"}
            deviceTypeID={2}
            deviceID={1}
            groupID={1}
            valueMin={22}
            valueMax={30}
            url={activeMQUrl}
          />
        </Grid>
        <Grid item xs={4}>
          <IOTMessage
            device={"Car Fuel Reading"}
            deviceTypeID={3}
            deviceID={1}
            groupID={1}
            valueMin={50}
            valueMax={60}
            url={activeMQUrl}
          />
        </Grid>
      </Grid>
      <Divider variant="middle" />
      <div className={classes.heading}>
        <Typography variant="h6" gutterBottom>
          Search for messages
        </Typography>
      </div>
      <IOTQuerySearch onSearchClicked={onSearchClickedHandler} />
      <Divider variant="middle" />
      {searchClicked ? <CircularProgress /> : queryResultsComponent}
      <Divider variant="middle" />
      <div className={classes.heading}>
        <Typography variant="h6" gutterBottom>
          Add a custom message
        </Typography>
      </div>
      <IOTCustomMessage />
    </div>
  );
}

export default IOT;
