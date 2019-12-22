import { ISysteminstanz } from '@/shared/model/systeminstanz.model';

export interface IBetreiber {
  id?: number;
  vorname?: string;
  nachname?: string;
  strasse?: string;
  hausnummer?: string;
  plz?: string;
  ort?: string;
  systeminstanzs?: ISysteminstanz[];
}

export class Betreiber implements IBetreiber {
  constructor(
    public id?: number,
    public vorname?: string,
    public nachname?: string,
    public strasse?: string,
    public hausnummer?: string,
    public plz?: string,
    public ort?: string,
    public systeminstanzs?: ISysteminstanz[]
  ) {}
}
