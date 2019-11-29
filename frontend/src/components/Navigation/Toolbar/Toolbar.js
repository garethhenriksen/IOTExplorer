import React from "react";

import styles from "./Toolbar.module.css";
import NavigationItems from "../NavigationItems/NavigationItems";
import DrawerToggle from '../SideDrawer/DrawerToggle/DrawerToggle'

const toolbar = props => (
  <header className={styles.Toolbar}>
    <DrawerToggle clicked={props.drawerToggleClicked}/>
    <div className={styles.Logo}>
    </div>
    <nav className={styles.DesktopOnly}>
      <NavigationItems/>
    </nav>
  </header>
);

export default toolbar;
