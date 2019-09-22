import { combineReducers } from 'redux';
import { loadingBarReducer as loadingBar } from 'react-redux-loading-bar';

import authentication, { AuthenticationState } from './authentication';
import applicationProfile, { ApplicationProfileState } from './application-profile';

import administration, { AdministrationState } from 'app/modules/administration/administration.reducer';
import userManagement, { UserManagementState } from './user-management';
// prettier-ignore
import measurement, {
  MeasurementState
} from 'app/entities/measurement/measurement.reducer';
// prettier-ignore
import patient, {
  PatientState
} from 'app/entities/patient/patient.reducer';
// prettier-ignore
import study, {
  StudyState
} from 'app/entities/study/study.reducer';
// prettier-ignore
import descriptor, {
  DescriptorState
} from 'app/entities/descriptor/descriptor.reducer';
// prettier-ignore
import twoDimensionSpatialCoordinate, {
  TwoDimensionSpatialCoordinateState
} from 'app/entities/two-dimension-spatial-coordinate/two-dimension-spatial-coordinate.reducer';
/* jhipster-needle-add-reducer-import - JHipster will add reducer here */

export interface IRootState {
  readonly authentication: AuthenticationState;
  readonly applicationProfile: ApplicationProfileState;
  readonly administration: AdministrationState;
  readonly userManagement: UserManagementState;
  readonly measurement: MeasurementState;
  readonly patient: PatientState;
  readonly study: StudyState;
  readonly descriptor: DescriptorState;
  readonly twoDimensionSpatialCoordinate: TwoDimensionSpatialCoordinateState;
  /* jhipster-needle-add-reducer-type - JHipster will add reducer type here */
  readonly loadingBar: any;
}

const rootReducer = combineReducers<IRootState>({
  authentication,
  applicationProfile,
  administration,
  userManagement,
  measurement,
  patient,
  study,
  descriptor,
  twoDimensionSpatialCoordinate,
  /* jhipster-needle-add-reducer-combine - JHipster will add reducer here */
  loadingBar
});

export default rootReducer;
