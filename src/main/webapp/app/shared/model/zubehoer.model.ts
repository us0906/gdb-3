import { ISystemtyp } from '@/shared/model/systemtyp.model';

export interface IZubehoer {
  id?: number;
  bezeichnung?: string;
  gueltigBis?: Date;
  systemtyps?: ISystemtyp[];
  herstellerBezeichnung?: string;
  herstellerId?: number;
  zubehoerTypBezeichnung?: string;
  zubehoerTypId?: number;
}

export class Zubehoer implements IZubehoer {
  constructor(
    public id?: number,
    public bezeichnung?: string,
    public gueltigBis?: Date,
    public systemtyps?: ISystemtyp[],
    public herstellerBezeichnung?: string,
    public herstellerId?: number,
    public zubehoerTypBezeichnung?: string,
    public zubehoerTypId?: number
  ) {}
}
