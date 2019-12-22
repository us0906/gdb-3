export interface ISystemnutzung {
  id?: number;
  systeminstanzId?: number;
  arztId?: number;
}

export class Systemnutzung implements ISystemnutzung {
  constructor(public id?: number, public systeminstanzId?: number, public arztId?: number) {}
}
