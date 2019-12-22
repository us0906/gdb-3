import { IGeraet } from '@/shared/model/geraet.model';
import { IZubehoer } from '@/shared/model/zubehoer.model';

export interface IHersteller {
  id?: number;
  bezeichnung?: string;
  gueltigBis?: Date;
  geraets?: IGeraet[];
  zubehoers?: IZubehoer[];
}

export class Hersteller implements IHersteller {
  constructor(
    public id?: number,
    public bezeichnung?: string,
    public gueltigBis?: Date,
    public geraets?: IGeraet[],
    public zubehoers?: IZubehoer[]
  ) {}
}
