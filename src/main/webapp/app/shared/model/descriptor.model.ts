import { Moment } from 'moment';
import { IStudy } from 'app/shared/model/study.model';
import { IDescriptor } from 'app/shared/model/descriptor.model';

export interface IDescriptor {
  id?: string;
  dateCreated?: Moment;
  study?: IStudy;
  descriptor?: IStudy;
  descriptors?: IDescriptor[];
}

export const defaultValue: Readonly<IDescriptor> = {};
