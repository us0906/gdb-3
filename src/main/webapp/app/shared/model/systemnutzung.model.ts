export interface ISystemnutzung {
  id?: number;
  systeminstanzBezeichnung?: string;
  systeminstanzId?: number;
  arztBezeichnung?: string;
  arztId?: number;
}

export class Systemnutzung implements ISystemnutzung {
  constructor(
    public id?: number,
    public systeminstanzBezeichnung?: string,
    public systeminstanzId?: number,
    public arztBezeichnung?: string,
    public arztId?: number
  ) {}
}
