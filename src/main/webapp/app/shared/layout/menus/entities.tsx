import React from 'react';
import MenuItem from 'app/shared/layout/menus/menu-item';
import { DropdownItem } from 'reactstrap';
import { FontAwesomeIcon } from '@fortawesome/react-fontawesome';

import { NavLink as Link } from 'react-router-dom';
import { NavDropdown } from './menu-components';

export const EntitiesMenu = props => (
  // tslint:disable-next-line:jsx-self-close
  <NavDropdown icon="th-list" name="Entities" id="entity-menu">
    <MenuItem icon="asterisk" to="/entity/measurement">
      Measurement
    </MenuItem>
    <MenuItem icon="asterisk" to="/entity/patient">
      Patient
    </MenuItem>
    <MenuItem icon="asterisk" to="/entity/study">
      Study
    </MenuItem>
    <MenuItem icon="asterisk" to="/entity/descriptor">
      Descriptor
    </MenuItem>
    <MenuItem icon="asterisk" to="/entity/two-dimension-spatial-coordinate">
      Two Dimension Spatial Coordinate
    </MenuItem>
    {/* jhipster-needle-add-entity-to-menu - JHipster will add entities to the menu here */}
  </NavDropdown>
);
