export interface IMeasurement {
  id?: string;
  area?: number;
  label?: string;
  description?: string;
  patientId?: string;
}

export const defaultValue: Readonly<IMeasurement> = {};
