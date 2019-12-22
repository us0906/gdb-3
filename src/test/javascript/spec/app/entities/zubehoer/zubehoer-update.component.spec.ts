/* tslint:disable max-line-length */
import { shallowMount, createLocalVue, Wrapper } from '@vue/test-utils';
import sinon, { SinonStubbedInstance } from 'sinon';
import Router from 'vue-router';

import AlertService from '@/shared/alert/alert.service';
import * as config from '@/shared/config/config';
import ZubehoerUpdateComponent from '@/entities/zubehoer/zubehoer-update.vue';
import ZubehoerClass from '@/entities/zubehoer/zubehoer-update.component';
import ZubehoerService from '@/entities/zubehoer/zubehoer.service';

import SystemtypService from '@/entities/systemtyp/systemtyp.service';

import HerstellerService from '@/entities/hersteller/hersteller.service';

import ZubehoerTypService from '@/entities/zubehoer-typ/zubehoer-typ.service';

const localVue = createLocalVue();

config.initVueApp(localVue);
const i18n = config.initI18N(localVue);
const store = config.initVueXStore(localVue);
const router = new Router();
localVue.use(Router);
localVue.component('font-awesome-icon', {});

describe('Component Tests', () => {
  describe('Zubehoer Management Update Component', () => {
    let wrapper: Wrapper<ZubehoerClass>;
    let comp: ZubehoerClass;
    let zubehoerServiceStub: SinonStubbedInstance<ZubehoerService>;

    beforeEach(() => {
      zubehoerServiceStub = sinon.createStubInstance<ZubehoerService>(ZubehoerService);

      wrapper = shallowMount<ZubehoerClass>(ZubehoerUpdateComponent, {
        store,
        i18n,
        localVue,
        router,
        provide: {
          alertService: () => new AlertService(store),
          zubehoerService: () => zubehoerServiceStub,

          systemtypService: () => new SystemtypService(),

          herstellerService: () => new HerstellerService(),

          zubehoerTypService: () => new ZubehoerTypService()
        }
      });
      comp = wrapper.vm;
    });

    describe('save', () => {
      it('Should call update service on save for existing entity', async () => {
        // GIVEN
        const entity = { id: 123 };
        comp.zubehoer = entity;
        zubehoerServiceStub.update.resolves(entity);

        // WHEN
        comp.save();
        await comp.$nextTick();

        // THEN
        expect(zubehoerServiceStub.update.calledWith(entity)).toBeTruthy();
        expect(comp.isSaving).toEqual(false);
      });

      it('Should call create service on save for new entity', async () => {
        // GIVEN
        const entity = {};
        comp.zubehoer = entity;
        zubehoerServiceStub.create.resolves(entity);

        // WHEN
        comp.save();
        await comp.$nextTick();

        // THEN
        expect(zubehoerServiceStub.create.calledWith(entity)).toBeTruthy();
        expect(comp.isSaving).toEqual(false);
      });
    });
  });
});
