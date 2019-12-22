/* tslint:disable max-line-length */
import { shallowMount, createLocalVue, Wrapper } from '@vue/test-utils';
import sinon, { SinonStubbedInstance } from 'sinon';
import Router from 'vue-router';

import AlertService from '@/shared/alert/alert.service';
import * as config from '@/shared/config/config';
import SystemtypUpdateComponent from '@/entities/systemtyp/systemtyp-update.vue';
import SystemtypClass from '@/entities/systemtyp/systemtyp-update.component';
import SystemtypService from '@/entities/systemtyp/systemtyp.service';

import SysteminstanzService from '@/entities/systeminstanz/systeminstanz.service';

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
  describe('Systemtyp Management Update Component', () => {
    let wrapper: Wrapper<SystemtypClass>;
    let comp: SystemtypClass;
    let systemtypServiceStub: SinonStubbedInstance<SystemtypService>;

    beforeEach(() => {
      systemtypServiceStub = sinon.createStubInstance<SystemtypService>(SystemtypService);

      wrapper = shallowMount<SystemtypClass>(SystemtypUpdateComponent, {
        store,
        i18n,
        localVue,
        router,
        provide: {
          alertService: () => new AlertService(store),
          systemtypService: () => systemtypServiceStub,

          systeminstanzService: () => new SysteminstanzService(),

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
        comp.systemtyp = entity;
        systemtypServiceStub.update.resolves(entity);

        // WHEN
        comp.save();
        await comp.$nextTick();

        // THEN
        expect(systemtypServiceStub.update.calledWith(entity)).toBeTruthy();
        expect(comp.isSaving).toEqual(false);
      });

      it('Should call create service on save for new entity', async () => {
        // GIVEN
        const entity = {};
        comp.systemtyp = entity;
        systemtypServiceStub.create.resolves(entity);

        // WHEN
        comp.save();
        await comp.$nextTick();

        // THEN
        expect(systemtypServiceStub.create.calledWith(entity)).toBeTruthy();
        expect(comp.isSaving).toEqual(false);
      });
    });
  });
});
