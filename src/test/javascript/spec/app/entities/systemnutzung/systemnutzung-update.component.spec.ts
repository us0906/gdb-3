/* tslint:disable max-line-length */
import { shallowMount, createLocalVue, Wrapper } from '@vue/test-utils';
import sinon, { SinonStubbedInstance } from 'sinon';
import Router from 'vue-router';

import AlertService from '@/shared/alert/alert.service';
import * as config from '@/shared/config/config';
import SystemnutzungUpdateComponent from '@/entities/systemnutzung/systemnutzung-update.vue';
import SystemnutzungClass from '@/entities/systemnutzung/systemnutzung-update.component';
import SystemnutzungService from '@/entities/systemnutzung/systemnutzung.service';

import SysteminstanzService from '@/entities/systeminstanz/systeminstanz.service';

import ArztService from '@/entities/arzt/arzt.service';

const localVue = createLocalVue();

config.initVueApp(localVue);
const i18n = config.initI18N(localVue);
const store = config.initVueXStore(localVue);
const router = new Router();
localVue.use(Router);
localVue.component('font-awesome-icon', {});

describe('Component Tests', () => {
  describe('Systemnutzung Management Update Component', () => {
    let wrapper: Wrapper<SystemnutzungClass>;
    let comp: SystemnutzungClass;
    let systemnutzungServiceStub: SinonStubbedInstance<SystemnutzungService>;

    beforeEach(() => {
      systemnutzungServiceStub = sinon.createStubInstance<SystemnutzungService>(SystemnutzungService);

      wrapper = shallowMount<SystemnutzungClass>(SystemnutzungUpdateComponent, {
        store,
        i18n,
        localVue,
        router,
        provide: {
          alertService: () => new AlertService(store),
          systemnutzungService: () => systemnutzungServiceStub,

          systeminstanzService: () => new SysteminstanzService(),

          arztService: () => new ArztService()
        }
      });
      comp = wrapper.vm;
    });

    describe('save', () => {
      it('Should call update service on save for existing entity', async () => {
        // GIVEN
        const entity = { id: 123 };
        comp.systemnutzung = entity;
        systemnutzungServiceStub.update.resolves(entity);

        // WHEN
        comp.save();
        await comp.$nextTick();

        // THEN
        expect(systemnutzungServiceStub.update.calledWith(entity)).toBeTruthy();
        expect(comp.isSaving).toEqual(false);
      });

      it('Should call create service on save for new entity', async () => {
        // GIVEN
        const entity = {};
        comp.systemnutzung = entity;
        systemnutzungServiceStub.create.resolves(entity);

        // WHEN
        comp.save();
        await comp.$nextTick();

        // THEN
        expect(systemnutzungServiceStub.create.calledWith(entity)).toBeTruthy();
        expect(comp.isSaving).toEqual(false);
      });
    });
  });
});
