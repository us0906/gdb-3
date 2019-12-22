import axios from 'axios';

import { ISysteminstanz } from '@/shared/model/systeminstanz.model';

const baseApiUrl = 'api/systeminstanzs';
const baseSearchApiUrl = 'api/_search/systeminstanzs?query=';

export default class SysteminstanzService {
  public search(query): Promise<any> {
    return new Promise<any>(resolve => {
      axios.get(`${baseSearchApiUrl}${query}`).then(function(res) {
        resolve(res.data);
      });
    });
  }

  public find(id: number): Promise<ISysteminstanz> {
    return new Promise<ISysteminstanz>(resolve => {
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

  public create(entity: ISysteminstanz): Promise<ISysteminstanz> {
    return new Promise<ISysteminstanz>(resolve => {
      axios.post(`${baseApiUrl}`, entity).then(function(res) {
        resolve(res.data);
      });
    });
  }

  public update(entity: ISysteminstanz): Promise<ISysteminstanz> {
    return new Promise<ISysteminstanz>(resolve => {
      axios.put(`${baseApiUrl}`, entity).then(function(res) {
        resolve(res.data);
      });
    });
  }
}
