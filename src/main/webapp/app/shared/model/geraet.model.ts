import { ISystemtyp } from '@/shared/model/systemtyp.model';

export interface IGeraet {
  id?: number;
  bezeichnung?: string;
  gueltigBis?: Date;
  systemtyps?: ISystemtyp[];
  geraetTypBezeichnung?: string;
  geraetTypId?: number;
  herstellerBezeichnung?: string;
  herstellerId?: number;
}

export class Geraet implements IGeraet {
  constructor(
    public id?: number,
    public bezeichnung?: string,
    public gueltigBis?: Date,
    public systemtyps?: ISystemtyp[],
    public geraetTypBezeichnung?: string,
    public geraetTypId?: number,
    public herstellerBezeichnung?: string,
    public herstellerId?: number
  ) {}
}
