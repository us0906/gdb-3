import { ISystemnutzung } from '@/shared/model/systemnutzung.model';

export interface ISysteminstanz {
  id?: number;
  bezeichnung?: string;
  geraetNummer?: string;
  geraetBaujahr?: string;
  gueltigBis?: Date;
  gweContentType?: string;
  gwe?: any;
  bemerkung?: any;
  systemnutzungs?: ISystemnutzung[];
  systemtypBezeichnung?: string;
  systemtypId?: number;
  betriebsstaetteId?: number;
  betreiberId?: number;
}

export class Systeminstanz implements ISysteminstanz {
  constructor(
    public id?: number,
    public bezeichnung?: string,
    public geraetNummer?: string,
    public geraetBaujahr?: string,
    public gueltigBis?: Date,
    public gweContentType?: string,
    public gwe?: any,
    public bemerkung?: any,
    public systemnutzungs?: ISystemnutzung[],
    public systemtypBezeichnung?: string,
    public systemtypId?: number,
    public betriebsstaetteId?: number,
    public betreiberId?: number
  ) {}
}
