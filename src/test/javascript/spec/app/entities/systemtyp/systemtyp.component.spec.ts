/* tslint:disable max-line-length */
import { shallowMount, createLocalVue, Wrapper } from '@vue/test-utils';
import sinon, { SinonStubbedInstance } from 'sinon';

import AlertService from '@/shared/alert/alert.service';
import * as config from '@/shared/config/config';
import SystemtypComponent from '@/entities/systemtyp/systemtyp.vue';
import SystemtypClass from '@/entities/systemtyp/systemtyp.component';
import SystemtypService from '@/entities/systemtyp/systemtyp.service';

const localVue = createLocalVue();

config.initVueApp(localVue);
const i18n = config.initI18N(localVue);
const store = config.initVueXStore(localVue);
localVue.component('font-awesome-icon', {});
localVue.component('b-alert', {});
localVue.component('b-badge', {});
localVue.directive('b-modal', {});
localVue.component('b-button', {});
localVue.component('router-link', {});

const bModalStub = {
  render: () => {},
  methods: {
    hide: () => {}
  }
};

describe('Component Tests', () => {
  describe('Systemtyp Management Component', () => {
    let wrapper: Wrapper<SystemtypClass>;
    let comp: SystemtypClass;
    let systemtypServiceStub: SinonStubbedInstance<SystemtypService>;

    beforeEach(() => {
      systemtypServiceStub = sinon.createStubInstance<SystemtypService>(SystemtypService);
      systemtypServiceStub.retrieve.resolves({ headers: {} });

      wrapper = shallowMount<SystemtypClass>(SystemtypComponent, {
        store,
        i18n,
        localVue,
        stubs: { bModal: bModalStub as any },
        provide: {
          alertService: () => new AlertService(store),
          systemtypService: () => systemtypServiceStub
        }
      });
      comp = wrapper.vm;
    });

    it('should be a Vue instance', () => {
      expect(wrapper.isVueInstance()).toBeTruthy();
    });

    it('Should call load all on init', async () => {
      // GIVEN
      systemtypServiceStub.retrieve.resolves({ headers: {}, data: [{ id: 123 }] });

      // WHEN
      comp.retrieveAllSystemtyps();
      await comp.$nextTick();

      // THEN
      expect(systemtypServiceStub.retrieve.called).toBeTruthy();
      expect(comp.systemtyps[0]).toEqual(jasmine.objectContaining({ id: 123 }));
    });
    it('Should call delete service on confirmDelete', async () => {
      // GIVEN
      systemtypServiceStub.delete.resolves({});

      // WHEN
      comp.prepareRemove({ id: 123 });
      comp.removeSystemtyp();
      await comp.$nextTick();

      // THEN
      expect(systemtypServiceStub.delete.called).toBeTruthy();
      expect(systemtypServiceStub.retrieve.callCount).toEqual(2);
    });
  });
});
