import axios from 'axios';

import { IZubehoerTyp } from '@/shared/model/zubehoer-typ.model';

const baseApiUrl = 'api/zubehoer-typs';
const baseSearchApiUrl = 'api/_search/zubehoer-typs?query=';

export default class ZubehoerTypService {
  public search(query): Promise<any> {
    return new Promise<any>(resolve => {
      axios.get(`${baseSearchApiUrl}${query}`).then(function(res) {
        resolve(res.data);
      });
    });
  }

  public find(id: number): Promise<IZubehoerTyp> {
    return new Promise<IZubehoerTyp>(resolve => {
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

  public create(entity: IZubehoerTyp): Promise<IZubehoerTyp> {
    return new Promise<IZubehoerTyp>(resolve => {
      axios.post(`${baseApiUrl}`, entity).then(function(res) {
        resolve(res.data);
      });
    });
  }

  public update(entity: IZubehoerTyp): Promise<IZubehoerTyp> {
    return new Promise<IZubehoerTyp>(resolve => {
      axios.put(`${baseApiUrl}`, entity).then(function(res) {
        resolve(res.data);
      });
    });
  }
}
