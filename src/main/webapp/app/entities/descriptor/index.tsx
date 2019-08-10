import React from 'react';
import { Switch } from 'react-router-dom';

import ErrorBoundaryRoute from 'app/shared/error/error-boundary-route';

import Descriptor from './descriptor';
import DescriptorDetail from './descriptor-detail';
import DescriptorUpdate from './descriptor-update';
import DescriptorDeleteDialog from './descriptor-delete-dialog';

const Routes = ({ match }) => (
  <>
    <Switch>
      <ErrorBoundaryRoute exact path={`${match.url}/new`} component={DescriptorUpdate} />
      <ErrorBoundaryRoute exact path={`${match.url}/:id/edit`} component={DescriptorUpdate} />
      <ErrorBoundaryRoute exact path={`${match.url}/:id`} component={DescriptorDetail} />
      <ErrorBoundaryRoute path={match.url} component={Descriptor} />
    </Switch>
    <ErrorBoundaryRoute path={`${match.url}/:id/delete`} component={DescriptorDeleteDialog} />
  </>
);

export default Routes;
