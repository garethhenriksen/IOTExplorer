import React from "react";

import { makeStyles } from "@material-ui/core/styles";
import Table from "@material-ui/core/Table";
import TableBody from "@material-ui/core/TableBody";
import TableCell from "@material-ui/core/TableCell";
import TableHead from "@material-ui/core/TableHead";
import TableRow from "@material-ui/core/TableRow";
import TablePagination from "@material-ui/core/TablePagination";
import moment from "moment";

const useStyles = makeStyles(theme => ({
  root: {
    width: "100%"
  },
  paper: {
    marginTop: theme.spacing(3),
    width: "100%",
    overflowX: "auto",
    marginBottom: theme.spacing(2)
  },
  table: {
    minWidth: 650
  }
}));

const IOTQueryResults = props => {
  const { queryResults } = props;
  const classes = useStyles();

  const [page, setPage] = React.useState(0);
  const [rowsPerPage, setRowsPerPage] = React.useState(10);

  const handleChangePage = (event, newPage) => {
    setPage(newPage);
  };

  const handleChangeRowsPerPage = event => {
    setRowsPerPage(parseInt(event.target.value, 10));
    setPage(0);
  };

  let table = null;
  if (queryResults.messages !== null) {
    let all = [10, 25, 50, queryResults.messages.length];
    table = (
      <div>
        <div>
          <Table
            className={classes.table}
            size="small"
            aria-label="a dense table"
          >
            <TableHead>
              <TableRow>
                <TableCell>deviceId</TableCell>
                <TableCell align="right">deviceTypeId</TableCell>
                <TableCell align="right">groupId</TableCell>
                <TableCell align="right">value</TableCell>
                <TableCell align="right">difference</TableCell>
                <TableCell align="right">timestamp</TableCell>
              </TableRow>
            </TableHead>
            <TableBody>
              {(rowsPerPage > 0
              ? queryResults.messages.slice(page * rowsPerPage, page * rowsPerPage + rowsPerPage)
              : queryResults.messages
            ).map((row, index) => (
                <TableRow key={index}>
                  <TableCell component="th" scope="row">
                    {row.deviceId}
                  </TableCell>
                  <TableCell align="right">{row.deviceTypeId}</TableCell>
                  <TableCell align="right">{row.groupId}</TableCell>
                  <TableCell align="right">{row.value}</TableCell>
                  <TableCell align="right">{row.difference}</TableCell>
                  <TableCell align="right">{row.timestamp !== null ? moment(row.timestamp).format("YYYY-MM-DD HH:mm") : ""}</TableCell>
                </TableRow>
              ))}
            </TableBody>
          </Table>
        </div>
        <TablePagination
          rowsPerPageOptions={[10, 25]}
          component="div"
          count={queryResults.messages.length}
          rowsPerPage={rowsPerPage}
          page={page}
          onChangePage={handleChangePage}
          onChangeRowsPerPage={handleChangeRowsPerPage}
        />
      </div>
    );
  }
  return (
    <div>
      {queryResults.value !== null ? "Value: " + queryResults.value : ""}
      {table}
    </div>
  );
};

export default IOTQueryResults;
