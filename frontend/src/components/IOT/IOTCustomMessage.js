import React from "react";

import { makeStyles } from "@material-ui/core/styles";
import TextField from "@material-ui/core/TextField";
import Grid from "@material-ui/core/Grid";

const useStyles = makeStyles(theme => ({
  root: {
    flexGrow: 1
  },
  textField: {
    marginLeft: theme.spacing(1),
    marginRight: theme.spacing(1),
    width: 200
  }
}));

const IOTCustomMessage = props => {
  const classes = useStyles();
  return (
    <div>
      <div>Create a Custom Request</div>
      <Grid item md={12}>
        <TextField id="deviceTypeId" name="deviceTypeId" label="deviceTypeId" />
        <TextField id="deviceId" name="deviceId" label="deviceId" />
        <TextField id="groupId" name="groupId" label="groupId" />
      </Grid>
    </div>
  );
};

export default IOTCustomMessage;
