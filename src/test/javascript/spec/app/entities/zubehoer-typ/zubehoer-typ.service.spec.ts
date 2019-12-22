/* tslint:disable max-line-length */
import axios from 'axios';
import { format } from 'date-fns';

import * as config from '@/shared/config/config';
import { DATE_FORMAT } from '@/shared/date/filters';
import ZubehoerTypService from '@/entities/zubehoer-typ/zubehoer-typ.service';
import { ZubehoerTyp, Technologie } from '@/shared/model/zubehoer-typ.model';

const mockedAxios: any = axios;
jest.mock('axios', () => ({
  get: jest.fn(),
  post: jest.fn(),
  put: jest.fn(),
  delete: jest.fn()
}));

describe('Service Tests', () => {
  describe('ZubehoerTyp Service', () => {
    let service: ZubehoerTypService;
    let elemDefault;
    let currentDate: Date;
    beforeEach(() => {
      service = new ZubehoerTypService();
      currentDate = new Date();

      elemDefault = new ZubehoerTyp(0, 'AAAAAAA', currentDate, Technologie.SONO);
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
      it('should create a ZubehoerTyp', async () => {
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

      it('should update a ZubehoerTyp', async () => {
        const returnedFromService = Object.assign(
          {
            bezeichnung: 'BBBBBB',
            gueltigBis: format(currentDate, DATE_FORMAT),
            technologie: 'BBBBBB'
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
      it('should return a list of ZubehoerTyp', async () => {
        const returnedFromService = Object.assign(
          {
            bezeichnung: 'BBBBBB',
            gueltigBis: format(currentDate, DATE_FORMAT),
            technologie: 'BBBBBB'
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
      it('should delete a ZubehoerTyp', async () => {
        mockedAxios.delete.mockReturnValue(Promise.resolve({ ok: true }));
        return service.delete(123).then(res => {
          expect(res.ok).toBeTruthy();
        });
      });
    });
  });
});
