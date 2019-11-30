import React, { useState } from "react";

import { makeStyles } from "@material-ui/core/styles";
import Button from "@material-ui/core/Button";
import TextField from "@material-ui/core/TextField";
import MenuItem from "@material-ui/core/MenuItem";
import DateFnsUtils from "@date-io/date-fns";
import {
  MuiPickersUtilsProvider,
  KeyboardTimePicker,
  KeyboardDatePicker
} from "@material-ui/pickers";

const useStyles = makeStyles(theme => ({
  button: {
    margin: theme.spacing(1)
  },
  input: {
    display: "none"
  },
  root: {
    "& > *": {
      margin: theme.spacing(1),
      width: 150
    }
  },
  textField: {
    marginLeft: theme.spacing(1),
    marginRight: theme.spacing(1),
    width: 200
  },
  menu: {
    width: 200
  }
}));

const queryTypes = [
  {
    value: "AVG",
    label: "AVG"
  },
  {
    value: "MIN",
    label: "MIN"
  },
  {
    value: "MAX",
    label: "MAX"
  }
];

const IOTQuerySearch = props => {
  const { onSearchClicked } = props;
  const classes = useStyles();

  const [state, setState] = useState({
    deviceId: null,
    deviceTypeId: null,
    groupId: null,
    queryType: null
  });

  const handleChange = event => {
    const value = event.target.value;
    setState({
      ...state,
      [event.target.name]: value
    });
  };

  const onClicked = () => {
    if (state.queryType == null) {
      alert("please enter query type");
    } else if (
      isNaN(state.deviceTypeId) ||
      isNaN(state.deviceId) ||
      isNaN(state.groupId)
    ) {
      alert(
        "please enter numeric value for deviceId, deviceTypeId and groupId"
      );
    } else {
      onSearchClicked(
        state.deviceTypeId,
        state.deviceId,
        state.groupId,
        state.queryType
      );
    }
  };

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
        <TextField
          id="standard-select-query-type"
          select
          label="Select"
          name="queryType"
          className={classes.textField}
          onChange={handleChange}
          SelectProps={{
            MenuProps: {
              className: classes.menu
            }
          }}
          helperText="Please select a query type"
          margin="normal"
        >
          {queryTypes.map(option => (
            <MenuItem key={option.value} value={option.value}>
              {option.label}
            </MenuItem>
          ))}
        </TextField>
        <br />
      </form>
      <Button
        variant="contained"
        color="primary"
        className={classes.button}
        onClick={() => onClicked()}
      >
        Search
      </Button>
    </div>
  );
};

export default IOTQuerySearch;
