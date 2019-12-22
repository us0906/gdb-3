import { IZubehoer } from '@/shared/model/zubehoer.model';

export const enum Technologie {
  SONO = 'SONO',
  BILD = 'BILD'
}

export interface IZubehoerTyp {
  id?: number;
  bezeichnung?: string;
  gueltigBis?: Date;
  technologie?: Technologie;
  zubehoers?: IZubehoer[];
}

export class ZubehoerTyp implements IZubehoerTyp {
  constructor(
    public id?: number,
    public bezeichnung?: string,
    public gueltigBis?: Date,
    public technologie?: Technologie,
    public zubehoers?: IZubehoer[]
  ) {}
}
