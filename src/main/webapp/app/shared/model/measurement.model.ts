export interface IMeasurement {
  id?: string;
  area?: number;
  label?: string;
  description?: string;
}

export const defaultValue: Readonly<IMeasurement> = {};
