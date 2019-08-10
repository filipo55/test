import React from 'react';
import { connect } from 'react-redux';
import { Link, RouteComponentProps } from 'react-router-dom';
import { Button, Row, Col, Label } from 'reactstrap';
import { AvFeedback, AvForm, AvGroup, AvInput, AvField } from 'availity-reactstrap-validation';
// tslint:disable-next-line:no-unused-variable
import { ICrudGetAction, ICrudGetAllAction, ICrudPutAction } from 'react-jhipster';
import { FontAwesomeIcon } from '@fortawesome/react-fontawesome';
import { IRootState } from 'app/shared/reducers';

import { IPatient } from 'app/shared/model/patient.model';
import { getEntities as getPatients } from 'app/entities/patient/patient.reducer';
import { getEntity, updateEntity, createEntity, reset } from './study.reducer';
import { IStudy } from 'app/shared/model/study.model';
// tslint:disable-next-line:no-unused-variable
import { convertDateTimeFromServer, convertDateTimeToServer } from 'app/shared/util/date-utils';
import { mapIdList } from 'app/shared/util/entity-utils';

export interface IStudyUpdateProps extends StateProps, DispatchProps, RouteComponentProps<{ id: string }> {}

export interface IStudyUpdateState {
  isNew: boolean;
  patientId: string;
}

export class StudyUpdate extends React.Component<IStudyUpdateProps, IStudyUpdateState> {
  constructor(props) {
    super(props);
    this.state = {
      patientId: '0',
      isNew: !this.props.match.params || !this.props.match.params.id
    };
  }

  componentWillUpdate(nextProps, nextState) {
    if (nextProps.updateSuccess !== this.props.updateSuccess && nextProps.updateSuccess) {
      this.handleClose();
    }
  }

  componentDidMount() {
    if (this.state.isNew) {
      this.props.reset();
    } else {
      this.props.getEntity(this.props.match.params.id);
    }

    this.props.getPatients();
  }

  saveEntity = (event, errors, values) => {
    if (errors.length === 0) {
      const { studyEntity } = this.props;
      const entity = {
        ...studyEntity,
        ...values
      };

      if (this.state.isNew) {
        this.props.createEntity(entity);
      } else {
        this.props.updateEntity(entity);
      }
    }
  };

  handleClose = () => {
    this.props.history.push('/entity/study');
  };

  render() {
    const { studyEntity, patients, loading, updating } = this.props;
    const { isNew } = this.state;

    return (
      <div>
        <Row className="justify-content-center">
          <Col md="8">
            <h2 id="testApp.study.home.createOrEditLabel">Create or edit a Study</h2>
          </Col>
        </Row>
        <Row className="justify-content-center">
          <Col md="8">
            {loading ? (
              <p>Loading...</p>
            ) : (
              <AvForm model={isNew ? {} : studyEntity} onSubmit={this.saveEntity}>
                {!isNew ? (
                  <AvGroup>
                    <Label for="study-id">ID</Label>
                    <AvInput id="study-id" type="text" className="form-control" name="id" required readOnly />
                  </AvGroup>
                ) : null}
                <AvGroup>
                  <Label id="studyInstanceUIDLabel" for="study-studyInstanceUID">
                    Study Instance UID
                  </Label>
                  <AvField id="study-studyInstanceUID" type="text" name="studyInstanceUID" />
                </AvGroup>
                <AvGroup>
                  <Label id="studyDateLabel" for="study-studyDate">
                    Study Date
                  </Label>
                  <AvField id="study-studyDate" type="date" className="form-control" name="studyDate" />
                </AvGroup>
                <AvGroup>
                  <Label id="requestedProcedureDescriptionLabel" for="study-requestedProcedureDescription">
                    Requested Procedure Description
                  </Label>
                  <AvField id="study-requestedProcedureDescription" type="text" name="requestedProcedureDescription" />
                </AvGroup>
                <AvGroup>
                  <Label id="accessionNumberLabel" for="study-accessionNumber">
                    Accession Number
                  </Label>
                  <AvField id="study-accessionNumber" type="text" name="accessionNumber" />
                </AvGroup>
                <AvGroup>
                  <Label for="study-patient">Patient</Label>
                  <AvInput id="study-patient" type="select" className="form-control" name="patient.id">
                    <option value="" key="0" />
                    {patients
                      ? patients.map(otherEntity => (
                          <option value={otherEntity.id} key={otherEntity.id}>
                            {otherEntity.id}
                          </option>
                        ))
                      : null}
                  </AvInput>
                </AvGroup>
                <Button tag={Link} id="cancel-save" to="/entity/study" replace color="info">
                  <FontAwesomeIcon icon="arrow-left" />
                  &nbsp;
                  <span className="d-none d-md-inline">Back</span>
                </Button>
                &nbsp;
                <Button color="primary" id="save-entity" type="submit" disabled={updating}>
                  <FontAwesomeIcon icon="save" />
                  &nbsp; Save
                </Button>
              </AvForm>
            )}
          </Col>
        </Row>
      </div>
    );
  }
}

const mapStateToProps = (storeState: IRootState) => ({
  patients: storeState.patient.entities,
  studyEntity: storeState.study.entity,
  loading: storeState.study.loading,
  updating: storeState.study.updating,
  updateSuccess: storeState.study.updateSuccess
});

const mapDispatchToProps = {
  getPatients,
  getEntity,
  updateEntity,
  createEntity,
  reset
};

type StateProps = ReturnType<typeof mapStateToProps>;
type DispatchProps = typeof mapDispatchToProps;

export default connect(
  mapStateToProps,
  mapDispatchToProps
)(StudyUpdate);
