import axios from 'axios';

import { ISystemnutzung } from '@/shared/model/systemnutzung.model';

const baseApiUrl = 'api/systemnutzungs';
const baseSearchApiUrl = 'api/_search/systemnutzungs?query=';

export default class SystemnutzungService {
  public search(query): Promise<any> {
    return new Promise<any>(resolve => {
      axios.get(`${baseSearchApiUrl}${query}`).then(function(res) {
        resolve(res.data);
      });
    });
  }

  public find(id: number): Promise<ISystemnutzung> {
    return new Promise<ISystemnutzung>(resolve => {
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

  public create(entity: ISystemnutzung): Promise<ISystemnutzung> {
    return new Promise<ISystemnutzung>(resolve => {
      axios.post(`${baseApiUrl}`, entity).then(function(res) {
        resolve(res.data);
      });
    });
  }

  public update(entity: ISystemnutzung): Promise<ISystemnutzung> {
    return new Promise<ISystemnutzung>(resolve => {
      axios.put(`${baseApiUrl}`, entity).then(function(res) {
        resolve(res.data);
      });
    });
  }
}
