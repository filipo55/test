import React from 'react';
import { connect } from 'react-redux';
import { Link, RouteComponentProps } from 'react-router-dom';
import { Button, Row, Col, Label } from 'reactstrap';
import { AvFeedback, AvForm, AvGroup, AvInput, AvField } from 'availity-reactstrap-validation';
// tslint:disable-next-line:no-unused-variable
import { ICrudGetAction, ICrudGetAllAction, ICrudPutAction } from 'react-jhipster';
import { FontAwesomeIcon } from '@fortawesome/react-fontawesome';
import { IRootState } from 'app/shared/reducers';

import { IStudy } from 'app/shared/model/study.model';
import { getEntities as getStudies } from 'app/entities/study/study.reducer';
import { getEntities as getDescriptors } from 'app/entities/descriptor/descriptor.reducer';
import { getEntity, updateEntity, createEntity, reset } from './descriptor.reducer';
import { IDescriptor } from 'app/shared/model/descriptor.model';
// tslint:disable-next-line:no-unused-variable
import { convertDateTimeFromServer, convertDateTimeToServer } from 'app/shared/util/date-utils';
import { mapIdList } from 'app/shared/util/entity-utils';

export interface IDescriptorUpdateProps extends StateProps, DispatchProps, RouteComponentProps<{ id: string }> {}

export interface IDescriptorUpdateState {
  isNew: boolean;
  idsdescriptor: any[];
  studyId: string;
  descriptorId: string;
}

export class DescriptorUpdate extends React.Component<IDescriptorUpdateProps, IDescriptorUpdateState> {
  constructor(props) {
    super(props);
    this.state = {
      idsdescriptor: [],
      studyId: '0',
      descriptorId: '0',
      isNew: !this.props.match.params || !this.props.match.params.id
    };
  }

  componentWillUpdate(nextProps, nextState) {
    if (nextProps.updateSuccess !== this.props.updateSuccess && nextProps.updateSuccess) {
      this.handleClose();
    }
  }

  componentDidMount() {
    if (!this.state.isNew) {
      this.props.getEntity(this.props.match.params.id);
    }

    this.props.getStudies();
    this.props.getDescriptors();
  }

  saveEntity = (event, errors, values) => {
    if (errors.length === 0) {
      const { descriptorEntity } = this.props;
      const entity = {
        ...descriptorEntity,
        ...values,
        descriptors: mapIdList(values.descriptors)
      };

      if (this.state.isNew) {
        this.props.createEntity(entity);
      } else {
        this.props.updateEntity(entity);
      }
    }
  };

  handleClose = () => {
    this.props.history.push('/entity/descriptor');
  };

  render() {
    const { descriptorEntity, studies, descriptors, loading, updating } = this.props;
    const { isNew } = this.state;

    return (
      <div>
        <Row className="justify-content-center">
          <Col md="8">
            <h2 id="testApp.descriptor.home.createOrEditLabel">Create or edit a Descriptor</h2>
          </Col>
        </Row>
        <Row className="justify-content-center">
          <Col md="8">
            {loading ? (
              <p>Loading...</p>
            ) : (
              <AvForm model={isNew ? {} : descriptorEntity} onSubmit={this.saveEntity}>
                {!isNew ? (
                  <AvGroup>
                    <Label for="descriptor-id">ID</Label>
                    <AvInput id="descriptor-id" type="text" className="form-control" name="id" required readOnly />
                  </AvGroup>
                ) : null}
                <AvGroup>
                  <Label id="dateCreatedLabel" for="descriptor-dateCreated">
                    Date Created
                  </Label>
                  <AvField id="descriptor-dateCreated" type="date" className="form-control" name="dateCreated" />
                </AvGroup>
                <AvGroup>
                  <Label for="descriptor-study">Study</Label>
                  <AvInput id="descriptor-study" type="select" className="form-control" name="study.id">
                    <option value="" key="0" />
                    {studies
                      ? studies.map(otherEntity => (
                          <option value={otherEntity.id} key={otherEntity.id}>
                            {otherEntity.id}
                          </option>
                        ))
                      : null}
                  </AvInput>
                </AvGroup>
                <AvGroup>
                  <Label for="descriptor-descriptor">Descriptor</Label>
                  <AvInput id="descriptor-descriptor" type="select" className="form-control" name="descriptor.id">
                    <option value="" key="0" />
                    {studies
                      ? studies.map(otherEntity => (
                          <option value={otherEntity.id} key={otherEntity.id}>
                            {otherEntity.id}
                          </option>
                        ))
                      : null}
                  </AvInput>
                </AvGroup>
                <AvGroup>
                  <Label for="descriptor-descriptor">Descriptor</Label>
                  <AvInput
                    id="descriptor-descriptor"
                    type="select"
                    multiple
                    className="form-control"
                    name="descriptors"
                    value={descriptorEntity.descriptors && descriptorEntity.descriptors.map(e => e.id)}
                  >
                    <option value="" key="0" />
                    {descriptors
                      ? descriptors.map(otherEntity => (
                          <option value={otherEntity.id} key={otherEntity.id}>
                            {otherEntity.id}
                          </option>
                        ))
                      : null}
                  </AvInput>
                </AvGroup>
                <Button tag={Link} id="cancel-save" to="/entity/descriptor" replace color="info">
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
  studies: storeState.study.entities,
  descriptors: storeState.descriptor.entities,
  descriptorEntity: storeState.descriptor.entity,
  loading: storeState.descriptor.loading,
  updating: storeState.descriptor.updating,
  updateSuccess: storeState.descriptor.updateSuccess
});

const mapDispatchToProps = {
  getStudies,
  getDescriptors,
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
)(DescriptorUpdate);
