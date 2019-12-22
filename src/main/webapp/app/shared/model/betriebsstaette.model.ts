import { ISysteminstanz } from '@/shared/model/systeminstanz.model';

export interface IBetriebsstaette {
  id?: number;
  bsnr?: string;
  strasse?: string;
  hausnummer?: string;
  plz?: string;
  ort?: string;
  systeminstanzs?: ISysteminstanz[];
}

export class Betriebsstaette implements IBetriebsstaette {
  constructor(
    public id?: number,
    public bsnr?: string,
    public strasse?: string,
    public hausnummer?: string,
    public plz?: string,
    public ort?: string,
    public systeminstanzs?: ISysteminstanz[]
  ) {}
}
