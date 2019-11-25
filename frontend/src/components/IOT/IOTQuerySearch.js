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
    value: "undefined",
    label: "No Selection"
  },
  {
    value: "AVG",
    label: "AVG"
  },
  {
    value: "SUM",
    label: "SUM"
  },
  {
    value: "MIN",
    label: "MIN"
  },
  {
    value: "MAX",
    label: "MAX"
  },
  {
    value: "MEDIAN",
    label: "MEDIAN"
  }
];

const IOTQuerySearch = props => {
  const { onSearchClicked } = props;
  const classes = useStyles();
  const [selectedStartDate, setSelectedStartDate] = useState(
    new Date("2019-11-23T00:00:00")
  );
  const [selectedEndDate, setSelectedEndDate] = useState(
    new Date("2019-11-23T00:01:00")
  );

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

  const handleStartDateChange = date => {
    setSelectedStartDate(date);
  };

  const handleEndDateChange = date => {
    setSelectedEndDate(date);
  };

  const onClicked = () => {
    if (isNaN(state.deviceTypeId) || isNaN(state.deviceId) || isNaN(state.groupId)) {
      alert("please enter numeric value for deviceId, deviceTypeId and groupId");
    } else {
      onSearchClicked(
        state.deviceTypeId,
        state.deviceId,
        state.groupId,
        state.queryType,
        selectedStartDate,
        selectedEndDate
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
        <MuiPickersUtilsProvider utils={DateFnsUtils}>
          <KeyboardDatePicker
            disableToolbar
            variant="inline"
            format="yyyy-MM-dd"
            margin="normal"
            id="date-picker-inline"
            label="Start Date"
            value={selectedStartDate}
            onChange={handleStartDateChange}
            KeyboardButtonProps={{
              "aria-label": "change date"
            }}
          />
          <KeyboardTimePicker
            margin="normal"
            id="time-picker"
            label="Start Time"
            value={selectedStartDate}
            onChange={handleStartDateChange}
            KeyboardButtonProps={{
              "aria-label": "change time"
            }}
          />
        </MuiPickersUtilsProvider>

        <MuiPickersUtilsProvider utils={DateFnsUtils}>
          <KeyboardDatePicker
            disableToolbar
            variant="inline"
            format="yyyy-MM-dd"
            margin="normal"
            id="date-picker-inline"
            label="End Date"
            value={selectedEndDate}
            onChange={handleEndDateChange}
            KeyboardButtonProps={{
              "aria-label": "change date"
            }}
          />
          <KeyboardTimePicker
            margin="normal"
            id="time-picker"
            label="End Time"
            value={selectedEndDate}
            onChange={handleEndDateChange}
            KeyboardButtonProps={{
              "aria-label": "change time"
            }}
          />
        </MuiPickersUtilsProvider>
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
