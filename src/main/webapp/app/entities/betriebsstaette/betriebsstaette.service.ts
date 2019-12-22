import axios from 'axios';

import { IBetriebsstaette } from '@/shared/model/betriebsstaette.model';

const baseApiUrl = 'api/betriebsstaettes';
const baseSearchApiUrl = 'api/_search/betriebsstaettes?query=';

export default class BetriebsstaetteService {
  public search(query): Promise<any> {
    return new Promise<any>(resolve => {
      axios.get(`${baseSearchApiUrl}${query}`).then(function(res) {
        resolve(res.data);
      });
    });
  }

  public find(id: number): Promise<IBetriebsstaette> {
    return new Promise<IBetriebsstaette>(resolve => {
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

  public create(entity: IBetriebsstaette): Promise<IBetriebsstaette> {
    return new Promise<IBetriebsstaette>(resolve => {
      axios.post(`${baseApiUrl}`, entity).then(function(res) {
        resolve(res.data);
      });
    });
  }

  public update(entity: IBetriebsstaette): Promise<IBetriebsstaette> {
    return new Promise<IBetriebsstaette>(resolve => {
      axios.put(`${baseApiUrl}`, entity).then(function(res) {
        resolve(res.data);
      });
    });
  }
}
