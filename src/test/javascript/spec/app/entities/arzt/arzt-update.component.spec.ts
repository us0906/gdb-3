/* tslint:disable max-line-length */
import { shallowMount, createLocalVue, Wrapper } from '@vue/test-utils';
import sinon, { SinonStubbedInstance } from 'sinon';
import Router from 'vue-router';

import AlertService from '@/shared/alert/alert.service';
import * as config from '@/shared/config/config';
import ArztUpdateComponent from '@/entities/arzt/arzt-update.vue';
import ArztClass from '@/entities/arzt/arzt-update.component';
import ArztService from '@/entities/arzt/arzt.service';

import SystemnutzungService from '@/entities/systemnutzung/systemnutzung.service';

const localVue = createLocalVue();

config.initVueApp(localVue);
const i18n = config.initI18N(localVue);
const store = config.initVueXStore(localVue);
const router = new Router();
localVue.use(Router);
localVue.component('font-awesome-icon', {});

describe('Component Tests', () => {
  describe('Arzt Management Update Component', () => {
    let wrapper: Wrapper<ArztClass>;
    let comp: ArztClass;
    let arztServiceStub: SinonStubbedInstance<ArztService>;

    beforeEach(() => {
      arztServiceStub = sinon.createStubInstance<ArztService>(ArztService);

      wrapper = shallowMount<ArztClass>(ArztUpdateComponent, {
        store,
        i18n,
        localVue,
        router,
        provide: {
          alertService: () => new AlertService(store),
          arztService: () => arztServiceStub,

          systemnutzungService: () => new SystemnutzungService()
        }
      });
      comp = wrapper.vm;
    });

    describe('save', () => {
      it('Should call update service on save for existing entity', async () => {
        // GIVEN
        const entity = { id: 123 };
        comp.arzt = entity;
        arztServiceStub.update.resolves(entity);

        // WHEN
        comp.save();
        await comp.$nextTick();

        // THEN
        expect(arztServiceStub.update.calledWith(entity)).toBeTruthy();
        expect(comp.isSaving).toEqual(false);
      });

      it('Should call create service on save for new entity', async () => {
        // GIVEN
        const entity = {};
        comp.arzt = entity;
        arztServiceStub.create.resolves(entity);

        // WHEN
        comp.save();
        await comp.$nextTick();

        // THEN
        expect(arztServiceStub.create.calledWith(entity)).toBeTruthy();
        expect(comp.isSaving).toEqual(false);
      });
    });
  });
});
