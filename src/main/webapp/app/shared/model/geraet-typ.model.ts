import { IGeraet } from '@/shared/model/geraet.model';

export const enum Technologie {
  SONO = 'SONO',
  BILD = 'BILD'
}

export interface IGeraetTyp {
  id?: number;
  bezeichnung?: string;
  gueltigBis?: Date;
  technologie?: Technologie;
  geraets?: IGeraet[];
}

export class GeraetTyp implements IGeraetTyp {
  constructor(
    public id?: number,
    public bezeichnung?: string,
    public gueltigBis?: Date,
    public technologie?: Technologie,
    public geraets?: IGeraet[]
  ) {}
}
