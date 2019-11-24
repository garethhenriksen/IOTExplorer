import React, { useState } from "react";

import Grid from "@material-ui/core/Grid";

import IOTMessage from "../components/IOT/IOTMessage";
import IOTQuerySearch from "../components/IOT/IOTQuerySearch";
import Divider from '@material-ui/core/Divider';
import axios from "../axios-football";
import moment from "moment";
import IOTQueryResults from "../components/IOT/IOTQueryResults";

function IOT() {
  const [queryResults, setQueryResults] = useState(null);

  const onSearchClickedHandler = (
    deviceTypeId,
    deviceId,
    groupId,
    queryType,
    selectedStartDate,
    selectedEndDate
  ) => {
    axios
      .get("/messages/", {
        params: {
          deviceTypeId: deviceTypeId,
          deviceId: deviceId,
          groupId: groupId,
          query: queryType !== 'undefined' ? queryType : null,
          startDate: moment(selectedStartDate).format("YYYY-MM-DD hh:mm"),
          endDate: moment(selectedEndDate).format("YYYY-MM-DD hh:mm")
        }
      })
      .then(res => {
        if (res.data) {
          setQueryResults(res.data);
        }
      })
      .catch(err => {
        alert(err.message);
        console.log(err.message);
      });
  };

  let query = null;
  if (queryResults !== null) {
    query = <IOTQueryResults queryResults={queryResults} />;
  }

  return (
    <div>
      <Grid container spacing={3}>
        <Grid item xs={4}>
          <IOTMessage
            device={"Heartbeat"}
            deviceTypeID={1}
            deviceID={1}
            groupID={1}
            valueMin={75}
            valueMax={90}
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
          />
        </Grid>
      </Grid>
      <Divider variant="middle" />
      <IOTQuerySearch onSearchClicked={onSearchClickedHandler} />
      <Divider variant="middle" />
      {query}
    </div>
  );
}

export default IOT;
