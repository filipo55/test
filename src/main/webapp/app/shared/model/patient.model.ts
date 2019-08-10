import { IStudy } from 'app/shared/model/study.model';

export const enum Sex {
  MALE = 'MALE',
  FEMALE = 'FEMALE'
}

export interface IPatient {
  id?: string;
  name?: string;
  patientSex?: Sex;
  studies?: IStudy[];
}

export const defaultValue: Readonly<IPatient> = {};
