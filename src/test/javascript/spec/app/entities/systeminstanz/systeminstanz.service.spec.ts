/* tslint:disable max-line-length */
import axios from 'axios';
import { format } from 'date-fns';

import * as config from '@/shared/config/config';
import { DATE_FORMAT } from '@/shared/date/filters';
import SysteminstanzService from '@/entities/systeminstanz/systeminstanz.service';
import { Systeminstanz } from '@/shared/model/systeminstanz.model';

const mockedAxios: any = axios;
const error = {
  response: {
    status: null,
    data: {
      type: null
    }
  }
};

jest.mock('axios', () => ({
  get: jest.fn(),
  post: jest.fn(),
  put: jest.fn(),
  delete: jest.fn()
}));

describe('Service Tests', () => {
  describe('Systeminstanz Service', () => {
    let service: SysteminstanzService;
    let elemDefault;
    let currentDate: Date;
    beforeEach(() => {
      service = new SysteminstanzService();
      currentDate = new Date();

      elemDefault = new Systeminstanz(0, 'AAAAAAA', 'AAAAAAA', 'AAAAAAA', currentDate, 'image/png', 'AAAAAAA', 'AAAAAAA');
    });

    describe('Service methods', () => {
      it('should find an element', async () => {
        const returnedFromService = Object.assign(
          {
            gueltigBis: format(currentDate, DATE_FORMAT)
          },
          elemDefault
        );
        mockedAxios.get.mockReturnValue(Promise.resolve({ data: returnedFromService }));

        return service.find(123).then(res => {
          expect(res).toMatchObject(elemDefault);
        });
      });

      it('should not find an element', async () => {
        mockedAxios.get.mockReturnValue(Promise.reject(error));
        return service
          .find(123)
          .then()
          .catch(err => {
            expect(err).toMatchObject(error);
          });
      });

      it('should create a Systeminstanz', async () => {
        const returnedFromService = Object.assign(
          {
            id: 0,
            gueltigBis: format(currentDate, DATE_FORMAT)
          },
          elemDefault
        );
        const expected = Object.assign(
          {
            gueltigBis: currentDate
          },
          returnedFromService
        );

        mockedAxios.post.mockReturnValue(Promise.resolve({ data: returnedFromService }));
        return service.create({}).then(res => {
          expect(res).toMatchObject(expected);
        });
      });

      it('should not create a Systeminstanz', async () => {
        mockedAxios.post.mockReturnValue(Promise.reject(error));

        return service
          .create({})
          .then()
          .catch(err => {
            expect(err).toMatchObject(error);
          });
      });

      it('should update a Systeminstanz', async () => {
        const returnedFromService = Object.assign(
          {
            bezeichnung: 'BBBBBB',
            geraetNummer: 'BBBBBB',
            geraetBaujahr: 'BBBBBB',
            gueltigBis: format(currentDate, DATE_FORMAT),
            gwe: 'BBBBBB',
            bemerkung: 'BBBBBB'
          },
          elemDefault
        );

        const expected = Object.assign(
          {
            gueltigBis: currentDate
          },
          returnedFromService
        );
        mockedAxios.put.mockReturnValue(Promise.resolve({ data: returnedFromService }));

        return service.update(expected).then(res => {
          expect(res).toMatchObject(expected);
        });
      });

      it('should not update a Systeminstanz', async () => {
        mockedAxios.put.mockReturnValue(Promise.reject(error));

        return service
          .update({})
          .then()
          .catch(err => {
            expect(err).toMatchObject(error);
          });
      });

      it('should return a list of Systeminstanz', async () => {
        const returnedFromService = Object.assign(
          {
            bezeichnung: 'BBBBBB',
            geraetNummer: 'BBBBBB',
            geraetBaujahr: 'BBBBBB',
            gueltigBis: format(currentDate, DATE_FORMAT),
            gwe: 'BBBBBB',
            bemerkung: 'BBBBBB'
          },
          elemDefault
        );
        const expected = Object.assign(
          {
            gueltigBis: currentDate
          },
          returnedFromService
        );
        mockedAxios.get.mockReturnValue(Promise.resolve([returnedFromService]));
        return service.retrieve().then(res => {
          expect(res).toContainEqual(expected);
        });
      });

      it('should not return a list of Systeminstanz', async () => {
        mockedAxios.get.mockReturnValue(Promise.reject(error));

        return service
          .retrieve()
          .then()
          .catch(err => {
            expect(err).toMatchObject(error);
          });
      });

      it('should delete a Systeminstanz', async () => {
        mockedAxios.delete.mockReturnValue(Promise.resolve({ ok: true }));
        return service.delete(123).then(res => {
          expect(res.ok).toBeTruthy();
        });
      });

      it('should not delete a Systeminstanz', async () => {
        mockedAxios.delete.mockReturnValue(Promise.reject(error));

        return service
          .delete(123)
          .then()
          .catch(err => {
            expect(err).toMatchObject(error);
          });
      });
    });
  });
});
