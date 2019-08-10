import { Moment } from 'moment';
import { IPatient } from 'app/shared/model/patient.model';
import { IDescriptor } from 'app/shared/model/descriptor.model';

export interface IStudy {
  id?: string;
  studyInstanceUID?: string;
  studyDate?: Moment;
  requestedProcedureDescription?: string;
  accessionNumber?: string;
  patient?: IPatient;
  descriptors?: IDescriptor[];
}

export const defaultValue: Readonly<IStudy> = {};
