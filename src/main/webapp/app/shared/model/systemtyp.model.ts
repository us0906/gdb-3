import { ISysteminstanz } from '@/shared/model/systeminstanz.model';

export interface ISystemtyp {
  id?: number;
  bezeichnung?: string;
  gueltigBis?: Date;
  systeminstanzs?: ISysteminstanz[];
  geraetBezeichnung?: string;
  geraetId?: number;
  zubehoerBezeichnung?: string;
  zubehoerId?: number;
}

export class Systemtyp implements ISystemtyp {
  constructor(
    public id?: number,
    public bezeichnung?: string,
    public gueltigBis?: Date,
    public systeminstanzs?: ISysteminstanz[],
    public geraetBezeichnung?: string,
    public geraetId?: number,
    public zubehoerBezeichnung?: string,
    public zubehoerId?: number
  ) {}
}
