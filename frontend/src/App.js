import React, { Suspense } from "react";
import { Route, Switch, Redirect } from "react-router-dom";

import Layout from "./hoc/Layout/Layout";
import IOT from "./containers/IOT";

function App() {
  let routes = (
    <Switch>
      <Route path="/IOT" render={props => <IOT {...props} />} />
      <Route path="/" exact component={IOT} />
      <Redirect to="/" />
    </Switch>
  );

  return (
    <div>
      <Layout>
        <Suspense fallback={<p>Loading...</p>}>{routes}</Suspense>
      </Layout>
    </div>
  );
}

export default App;
