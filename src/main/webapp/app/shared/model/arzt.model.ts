import { ISystemnutzung } from '@/shared/model/systemnutzung.model';

export interface IArzt {
  id?: number;
  lanr?: string;
  titel?: string;
  vorname?: string;
  nachname?: string;
  systemnutzungs?: ISystemnutzung[];
}

export class Arzt implements IArzt {
  constructor(
    public id?: number,
    public lanr?: string,
    public titel?: string,
    public vorname?: string,
    public nachname?: string,
    public systemnutzungs?: ISystemnutzung[]
  ) {}
}
