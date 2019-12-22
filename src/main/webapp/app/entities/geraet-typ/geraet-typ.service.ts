import axios from 'axios';

import { IGeraetTyp } from '@/shared/model/geraet-typ.model';

const baseApiUrl = 'api/geraet-typs';
const baseSearchApiUrl = 'api/_search/geraet-typs?query=';

export default class GeraetTypService {
  public search(query): Promise<any> {
    return new Promise<any>(resolve => {
      axios.get(`${baseSearchApiUrl}${query}`).then(function(res) {
        resolve(res.data);
      });
    });
  }

  public find(id: number): Promise<IGeraetTyp> {
    return new Promise<IGeraetTyp>(resolve => {
      axios.get(`${baseApiUrl}/${id}`).then(function(res) {
        resolve(res.data);
      });
    });
  }

  public retrieve(): Promise<any> {
    return new Promise<any>(resolve => {
      axios.get(baseApiUrl).then(function(res) {
        resolve(res);
      });
    });
  }

  public delete(id: number): Promise<any> {
    return new Promise<any>(resolve => {
      axios.delete(`${baseApiUrl}/${id}`).then(function(res) {
        resolve(res);
      });
    });
  }

  public create(entity: IGeraetTyp): Promise<IGeraetTyp> {
    return new Promise<IGeraetTyp>(resolve => {
      axios.post(`${baseApiUrl}`, entity).then(function(res) {
        resolve(res.data);
      });
    });
  }

  public update(entity: IGeraetTyp): Promise<IGeraetTyp> {
    return new Promise<IGeraetTyp>(resolve => {
      axios.put(`${baseApiUrl}`, entity).then(function(res) {
        resolve(res.data);
      });
    });
  }
}
