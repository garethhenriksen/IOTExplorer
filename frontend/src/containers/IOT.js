import React, { useState } from "react";

import Grid from "@material-ui/core/Grid";

import IOTMessage from "../components/IOT/IOTMessage";
import IOTQuerySearch from "../components/IOT/IOTQuerySearch";
import Divider from "@material-ui/core/Divider";
import CircularProgress from "@material-ui/core/CircularProgress";
import Typography from "@material-ui/core/Typography";
import { makeStyles } from "@material-ui/core/styles";
import Paper from "@material-ui/core/Paper";
import clsx from "clsx";

import axios from "../axios-iot";
import moment from "moment";
import IOTQueryResults from "../components/IOT/IOTQueryResults";
import IOTKafkaStreamQuerySearch from "../components/IOT/IOTKafkaStreamQuerySearch";
import IOTCustomMessage from "../components/IOT/IOTCustomMessage";
import BusPositionMap from "./BusPositionMap";

const useStyles = makeStyles(theme => ({
  heading: {
    marginTop: "10px",
    textAlign: "center"
  },
  paper: {
    padding: theme.spacing(2),
    marginBottom: '20px',
    display: "flex",
    overflow: "auto",
    flexDirection: "column",
    opacity: 1,
    [theme.breakpoints.down("sm")]: {
      padding: 10
    }
  },
  fixedHeight: {
    height: "100%"
  }
}));

function IOT() {
  const [queryResults, setQueryResults] = useState(null);
  const [searchClicked, setSearchClicked] = useState(false);
  const [kafkaStreamQueryResults, setKafkaStreamQueryResults] = useState(null);
  const [kafkaStreamSearchClicked, setKafkaStreamSearchClicked] = useState(
    false
  );

  const kafkaUrl = "publish/kafka";
  const classes = useStyles();
  const fixedHeightPaper = clsx(classes.paper, classes.fixedHeight);

  const onKafkaStreamSearchClickedHandler = (
    deviceTypeId,
    deviceId,
    groupId,
    queryType
  ) => {
    setKafkaStreamSearchClicked(true);

    axios
      .get("/messages/kafka/", {
        params: {
          deviceTypeId: deviceTypeId,
          deviceId: deviceId,
          groupId: groupId,
          query: queryType !== "undefined" ? queryType : "AVG"
        }
      })
      .then(res => {
        setKafkaStreamSearchClicked(false);
        if (res.data) {
          setKafkaStreamQueryResults(res.data);
        }
      })
      .catch(err => {
        setKafkaStreamSearchClicked(false);
        alert(err.message);
      });
  };

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
          startDate: moment(selectedStartDate).format("YYYY-MM-DD HH:mm"),
          endDate: moment(selectedEndDate).format("YYYY-MM-DD HH:mm")
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
      });
  };

  let queryResultsComponent = null;
  if (queryResults !== null) {
    queryResultsComponent = <IOTQueryResults queryResults={queryResults} />;
  }

  let kafkaQueryResultsComponent = null;
  if (kafkaStreamQueryResults !== null) {
    kafkaQueryResultsComponent = (
      <IOTQueryResults queryResults={kafkaStreamQueryResults} />
    );
  }

  return (
    <div>
      <Paper className={fixedHeightPaper}>
        {" "}
        <div className={classes.heading}>
          <Typography variant="h6" gutterBottom>
            Send Message
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
      </Paper>

      <Paper className={fixedHeightPaper}> 
      <div className={classes.heading}>
        <Typography variant="h6" gutterBottom>
          Search for messages
        </Typography>
      </div>
      <IOTQuerySearch onSearchClicked={onSearchClickedHandler} />
      {searchClicked ? <CircularProgress /> : queryResultsComponent}
      </Paper>

      <Paper className={fixedHeightPaper}> 
      <div className={classes.heading}>
        <Typography variant="h6" gutterBottom>
          Search using Kafka Streams
        </Typography>
      </div>
      <IOTKafkaStreamQuerySearch
        onSearchClicked={onKafkaStreamSearchClickedHandler}
      />
      {kafkaStreamSearchClicked ? (
        <CircularProgress />
      ) : (
        kafkaQueryResultsComponent
      )}
      </Paper>

      <Paper className={fixedHeightPaper}> 
      <div className={classes.heading}>
        <Typography variant="h6" gutterBottom>
          Add a custom message
        </Typography>
      </div>
      <IOTCustomMessage url={kafkaUrl} />
      </Paper>
      {/* <div><BusPositionMap /></div> */}
    </div>
  );
}

export default IOT;
