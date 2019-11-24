import React from "react";

import { configure, shallow } from "enzyme";
import Adapter from "enzyme-adapter-react-16";

import NavigationItems from "./NavigationItems";
import NavigationItem from "./NavigationItem/NavigationItem";

configure({ adapter: new Adapter() });
describe("<NavigationItems />", () => {
  let wrapper;
  beforeEach(() => {
    wrapper = shallow(<NavigationItems />);
  });
  it("should render 2 <Navigation /> elements if not authenticated", () => {
    expect(wrapper.find(NavigationItem)).toHaveLength(2);
  });

  it("should render 3 <Navigation /> elements if authenticated", () => {
    wrapper.setProps({ isAuthenticated: true });
    expect(wrapper.find(NavigationItem)).toHaveLength(3);
  });

  it("should render 1 <Navigation /> logout elements if authenticated", () => {
    wrapper.setProps({ isAuthenticated: true });
    expect(wrapper.contains(<NavigationItem link='/logout'>Log Out</NavigationItem>)).toEqual(true);
  });

  it("should render 0 <Navigation /> logout elements if not authenticated", () => {
    wrapper.setProps({ isAuthenticated: false });
    expect(wrapper.contains(<NavigationItem link='/logout'>Log Out</NavigationItem>)).toEqual(false);
  });
});
