import React from 'react';
import { connect } from 'react-redux';
import { Link, RouteComponentProps } from 'react-router-dom';
import { Button, Row, Col } from 'reactstrap';
// tslint:disable-next-line:no-unused-variable
import { ICrudGetAction } from 'react-jhipster';
import { FontAwesomeIcon } from '@fortawesome/react-fontawesome';

import { IRootState } from 'app/shared/reducers';
import { getEntity } from './measurement.reducer';
import { IMeasurement } from 'app/shared/model/measurement.model';
// tslint:disable-next-line:no-unused-variable
import { APP_DATE_FORMAT, APP_LOCAL_DATE_FORMAT } from 'app/config/constants';

export interface IMeasurementDetailProps extends StateProps, DispatchProps, RouteComponentProps<{ id: string }> {}

export class MeasurementDetail extends React.Component<IMeasurementDetailProps> {
  componentDidMount() {
    this.props.getEntity(this.props.match.params.id);
  }

  render() {
    const { measurementEntity } = this.props;
    return (
      <Row>
        <Col md="8">
          <h2>
            Measurement [<b>{measurementEntity.id}</b>]
          </h2>
          <dl className="jh-entity-details">
            <dt>
              <span id="area">Area</span>
            </dt>
            <dd>{measurementEntity.area}</dd>
            <dt>
              <span id="label">Label</span>
            </dt>
            <dd>{measurementEntity.label}</dd>
            <dt>
              <span id="description">Description</span>
            </dt>
            <dd>{measurementEntity.description}</dd>
          </dl>
          <Button tag={Link} to="/entity/measurement" replace color="info">
            <FontAwesomeIcon icon="arrow-left" /> <span className="d-none d-md-inline">Back</span>
          </Button>
          &nbsp;
          <Button tag={Link} to={`/entity/measurement/${measurementEntity.id}/edit`} replace color="primary">
            <FontAwesomeIcon icon="pencil-alt" /> <span className="d-none d-md-inline">Edit</span>
          </Button>
        </Col>
      </Row>
    );
  }
}

const mapStateToProps = ({ measurement }: IRootState) => ({
  measurementEntity: measurement.entity
});

const mapDispatchToProps = { getEntity };

type StateProps = ReturnType<typeof mapStateToProps>;
type DispatchProps = typeof mapDispatchToProps;

export default connect(
  mapStateToProps,
  mapDispatchToProps
)(MeasurementDetail);
