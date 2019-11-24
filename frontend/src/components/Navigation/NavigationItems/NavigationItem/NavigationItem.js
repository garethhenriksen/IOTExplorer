import React from "react";
import { NavLink } from "react-router-dom";

import styles from "./NavigationItem.module.css";

const navigationItem = props => {
  return (
    <li className={styles.NavigationItem}>
      <NavLink
        to={props.link}
        activeClassName={styles.active}
        exact={props.exact}
      >
        {props.children}
      </NavLink>
    </li>
  );
};

export default navigationItem;
