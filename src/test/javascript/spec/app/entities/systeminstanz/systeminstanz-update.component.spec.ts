/* tslint:disable max-line-length */
import { shallowMount, createLocalVue, Wrapper } from '@vue/test-utils';
import sinon, { SinonStubbedInstance } from 'sinon';
import Router from 'vue-router';

import AlertService from '@/shared/alert/alert.service';
import * as config from '@/shared/config/config';
import SysteminstanzUpdateComponent from '@/entities/systeminstanz/systeminstanz-update.vue';
import SysteminstanzClass from '@/entities/systeminstanz/systeminstanz-update.component';
import SysteminstanzService from '@/entities/systeminstanz/systeminstanz.service';

import SystemnutzungService from '@/entities/systemnutzung/systemnutzung.service';

import SystemtypService from '@/entities/systemtyp/systemtyp.service';

import BetriebsstaetteService from '@/entities/betriebsstaette/betriebsstaette.service';

import BetreiberService from '@/entities/betreiber/betreiber.service';

const localVue = createLocalVue();

config.initVueApp(localVue);
const i18n = config.initI18N(localVue);
const store = config.initVueXStore(localVue);
const router = new Router();
localVue.use(Router);
localVue.component('font-awesome-icon', {});

describe('Component Tests', () => {
  describe('Systeminstanz Management Update Component', () => {
    let wrapper: Wrapper<SysteminstanzClass>;
    let comp: SysteminstanzClass;
    let systeminstanzServiceStub: SinonStubbedInstance<SysteminstanzService>;

    beforeEach(() => {
      systeminstanzServiceStub = sinon.createStubInstance<SysteminstanzService>(SysteminstanzService);

      wrapper = shallowMount<SysteminstanzClass>(SysteminstanzUpdateComponent, {
        store,
        i18n,
        localVue,
        router,
        provide: {
          alertService: () => new AlertService(store),
          systeminstanzService: () => systeminstanzServiceStub,

          systemnutzungService: () => new SystemnutzungService(),

          systemtypService: () => new SystemtypService(),

          betriebsstaetteService: () => new BetriebsstaetteService(),

          betreiberService: () => new BetreiberService()
        }
      });
      comp = wrapper.vm;
    });

    describe('save', () => {
      it('Should call update service on save for existing entity', async () => {
        // GIVEN
        const entity = { id: 123 };
        comp.systeminstanz = entity;
        systeminstanzServiceStub.update.resolves(entity);

        // WHEN
        comp.save();
        await comp.$nextTick();

        // THEN
        expect(systeminstanzServiceStub.update.calledWith(entity)).toBeTruthy();
        expect(comp.isSaving).toEqual(false);
      });

      it('Should call create service on save for new entity', async () => {
        // GIVEN
        const entity = {};
        comp.systeminstanz = entity;
        systeminstanzServiceStub.create.resolves(entity);

        // WHEN
        comp.save();
        await comp.$nextTick();

        // THEN
        expect(systeminstanzServiceStub.create.calledWith(entity)).toBeTruthy();
        expect(comp.isSaving).toEqual(false);
      });
    });
  });
});
