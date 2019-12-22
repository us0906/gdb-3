/* tslint:disable max-line-length */
import { shallowMount, createLocalVue, Wrapper } from '@vue/test-utils';
import sinon, { SinonStubbedInstance } from 'sinon';
import Router from 'vue-router';

import AlertService from '@/shared/alert/alert.service';
import * as config from '@/shared/config/config';
import HerstellerUpdateComponent from '@/entities/hersteller/hersteller-update.vue';
import HerstellerClass from '@/entities/hersteller/hersteller-update.component';
import HerstellerService from '@/entities/hersteller/hersteller.service';

import GeraetService from '@/entities/geraet/geraet.service';

import ZubehoerService from '@/entities/zubehoer/zubehoer.service';

const localVue = createLocalVue();

config.initVueApp(localVue);
const i18n = config.initI18N(localVue);
const store = config.initVueXStore(localVue);
const router = new Router();
localVue.use(Router);
localVue.component('font-awesome-icon', {});

describe('Component Tests', () => {
  describe('Hersteller Management Update Component', () => {
    let wrapper: Wrapper<HerstellerClass>;
    let comp: HerstellerClass;
    let herstellerServiceStub: SinonStubbedInstance<HerstellerService>;

    beforeEach(() => {
      herstellerServiceStub = sinon.createStubInstance<HerstellerService>(HerstellerService);

      wrapper = shallowMount<HerstellerClass>(HerstellerUpdateComponent, {
        store,
        i18n,
        localVue,
        router,
        provide: {
          alertService: () => new AlertService(store),
          herstellerService: () => herstellerServiceStub,

          geraetService: () => new GeraetService(),

          zubehoerService: () => new ZubehoerService()
        }
      });
      comp = wrapper.vm;
    });

    describe('save', () => {
      it('Should call update service on save for existing entity', async () => {
        // GIVEN
        const entity = { id: 123 };
        comp.hersteller = entity;
        herstellerServiceStub.update.resolves(entity);

        // WHEN
        comp.save();
        await comp.$nextTick();

        // THEN
        expect(herstellerServiceStub.update.calledWith(entity)).toBeTruthy();
        expect(comp.isSaving).toEqual(false);
      });

      it('Should call create service on save for new entity', async () => {
        // GIVEN
        const entity = {};
        comp.hersteller = entity;
        herstellerServiceStub.create.resolves(entity);

        // WHEN
        comp.save();
        await comp.$nextTick();

        // THEN
        expect(herstellerServiceStub.create.calledWith(entity)).toBeTruthy();
        expect(comp.isSaving).toEqual(false);
      });
    });
  });
});
