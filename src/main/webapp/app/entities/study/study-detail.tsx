import React from 'react';
import { connect } from 'react-redux';
import { Link, RouteComponentProps } from 'react-router-dom';
import { Button, Row, Col } from 'reactstrap';
// tslint:disable-next-line:no-unused-variable
import { ICrudGetAction, TextFormat } from 'react-jhipster';
import { FontAwesomeIcon } from '@fortawesome/react-fontawesome';

import { IRootState } from 'app/shared/reducers';
import { getEntity } from './study.reducer';
import { IStudy } from 'app/shared/model/study.model';
// tslint:disable-next-line:no-unused-variable
import { APP_DATE_FORMAT, APP_LOCAL_DATE_FORMAT } from 'app/config/constants';

export interface IStudyDetailProps extends StateProps, DispatchProps, RouteComponentProps<{ id: string }> {}

export class StudyDetail extends React.Component<IStudyDetailProps> {
  componentDidMount() {
    this.props.getEntity(this.props.match.params.id);
  }

  render() {
    const { studyEntity } = this.props;
    return (
      <Row>
        <Col md="8">
          <h2>
            Study [<b>{studyEntity.id}</b>]
          </h2>
          <dl className="jh-entity-details">
            <dt>
              <span id="studyInstanceUID">Study Instance UID</span>
            </dt>
            <dd>{studyEntity.studyInstanceUID}</dd>
            <dt>
              <span id="studyDate">Study Date</span>
            </dt>
            <dd>
              <TextFormat value={studyEntity.studyDate} type="date" format={APP_LOCAL_DATE_FORMAT} />
            </dd>
            <dt>
              <span id="requestedProcedureDescription">Requested Procedure Description</span>
            </dt>
            <dd>{studyEntity.requestedProcedureDescription}</dd>
            <dt>
              <span id="accessionNumber">Accession Number</span>
            </dt>
            <dd>{studyEntity.accessionNumber}</dd>
            <dt>Patient</dt>
            <dd>{studyEntity.patient ? studyEntity.patient.id : ''}</dd>
          </dl>
          <Button tag={Link} to="/entity/study" replace color="info">
            <FontAwesomeIcon icon="arrow-left" /> <span className="d-none d-md-inline">Back</span>
          </Button>
          &nbsp;
          <Button tag={Link} to={`/entity/study/${studyEntity.id}/edit`} replace color="primary">
            <FontAwesomeIcon icon="pencil-alt" /> <span className="d-none d-md-inline">Edit</span>
          </Button>
        </Col>
      </Row>
    );
  }
}

const mapStateToProps = ({ study }: IRootState) => ({
  studyEntity: study.entity
});

const mapDispatchToProps = { getEntity };

type StateProps = ReturnType<typeof mapStateToProps>;
type DispatchProps = typeof mapDispatchToProps;

export default connect(
  mapStateToProps,
  mapDispatchToProps
)(StudyDetail);
