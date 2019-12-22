/* tslint:disable max-line-length */
import { shallowMount, createLocalVue, Wrapper } from '@vue/test-utils';
import sinon, { SinonStubbedInstance } from 'sinon';
import Router from 'vue-router';

import AlertService from '@/shared/alert/alert.service';
import * as config from '@/shared/config/config';
import GeraetUpdateComponent from '@/entities/geraet/geraet-update.vue';
import GeraetClass from '@/entities/geraet/geraet-update.component';
import GeraetService from '@/entities/geraet/geraet.service';

import SystemtypService from '@/entities/systemtyp/systemtyp.service';

import GeraetTypService from '@/entities/geraet-typ/geraet-typ.service';

import HerstellerService from '@/entities/hersteller/hersteller.service';

const localVue = createLocalVue();

config.initVueApp(localVue);
const i18n = config.initI18N(localVue);
const store = config.initVueXStore(localVue);
const router = new Router();
localVue.use(Router);
localVue.component('font-awesome-icon', {});

describe('Component Tests', () => {
  describe('Geraet Management Update Component', () => {
    let wrapper: Wrapper<GeraetClass>;
    let comp: GeraetClass;
    let geraetServiceStub: SinonStubbedInstance<GeraetService>;

    beforeEach(() => {
      geraetServiceStub = sinon.createStubInstance<GeraetService>(GeraetService);

      wrapper = shallowMount<GeraetClass>(GeraetUpdateComponent, {
        store,
        i18n,
        localVue,
        router,
        provide: {
          alertService: () => new AlertService(store),
          geraetService: () => geraetServiceStub,

          systemtypService: () => new SystemtypService(),

          geraetTypService: () => new GeraetTypService(),

          herstellerService: () => new HerstellerService()
        }
      });
      comp = wrapper.vm;
    });

    describe('save', () => {
      it('Should call update service on save for existing entity', async () => {
        // GIVEN
        const entity = { id: 123 };
        comp.geraet = entity;
        geraetServiceStub.update.resolves(entity);

        // WHEN
        comp.save();
        await comp.$nextTick();

        // THEN
        expect(geraetServiceStub.update.calledWith(entity)).toBeTruthy();
        expect(comp.isSaving).toEqual(false);
      });

      it('Should call create service on save for new entity', async () => {
        // GIVEN
        const entity = {};
        comp.geraet = entity;
        geraetServiceStub.create.resolves(entity);

        // WHEN
        comp.save();
        await comp.$nextTick();

        // THEN
        expect(geraetServiceStub.create.calledWith(entity)).toBeTruthy();
        expect(comp.isSaving).toEqual(false);
      });
    });
  });
});
