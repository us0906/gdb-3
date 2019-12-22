/* tslint:disable max-line-length */
import { shallowMount, createLocalVue, Wrapper } from '@vue/test-utils';
import sinon, { SinonStubbedInstance } from 'sinon';
import Router from 'vue-router';

import AlertService from '@/shared/alert/alert.service';
import * as config from '@/shared/config/config';
import GeraetTypUpdateComponent from '@/entities/geraet-typ/geraet-typ-update.vue';
import GeraetTypClass from '@/entities/geraet-typ/geraet-typ-update.component';
import GeraetTypService from '@/entities/geraet-typ/geraet-typ.service';

import GeraetService from '@/entities/geraet/geraet.service';

const localVue = createLocalVue();

config.initVueApp(localVue);
const i18n = config.initI18N(localVue);
const store = config.initVueXStore(localVue);
const router = new Router();
localVue.use(Router);
localVue.component('font-awesome-icon', {});

describe('Component Tests', () => {
  describe('GeraetTyp Management Update Component', () => {
    let wrapper: Wrapper<GeraetTypClass>;
    let comp: GeraetTypClass;
    let geraetTypServiceStub: SinonStubbedInstance<GeraetTypService>;

    beforeEach(() => {
      geraetTypServiceStub = sinon.createStubInstance<GeraetTypService>(GeraetTypService);

      wrapper = shallowMount<GeraetTypClass>(GeraetTypUpdateComponent, {
        store,
        i18n,
        localVue,
        router,
        provide: {
          alertService: () => new AlertService(store),
          geraetTypService: () => geraetTypServiceStub,

          geraetService: () => new GeraetService()
        }
      });
      comp = wrapper.vm;
    });

    describe('save', () => {
      it('Should call update service on save for existing entity', async () => {
        // GIVEN
        const entity = { id: 123 };
        comp.geraetTyp = entity;
        geraetTypServiceStub.update.resolves(entity);

        // WHEN
        comp.save();
        await comp.$nextTick();

        // THEN
        expect(geraetTypServiceStub.update.calledWith(entity)).toBeTruthy();
        expect(comp.isSaving).toEqual(false);
      });

      it('Should call create service on save for new entity', async () => {
        // GIVEN
        const entity = {};
        comp.geraetTyp = entity;
        geraetTypServiceStub.create.resolves(entity);

        // WHEN
        comp.save();
        await comp.$nextTick();

        // THEN
        expect(geraetTypServiceStub.create.calledWith(entity)).toBeTruthy();
        expect(comp.isSaving).toEqual(false);
      });
    });
  });
});
