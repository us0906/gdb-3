/* tslint:disable max-line-length */
import { shallowMount, createLocalVue, Wrapper } from '@vue/test-utils';
import sinon, { SinonStubbedInstance } from 'sinon';

import AlertService from '@/shared/alert/alert.service';
import * as config from '@/shared/config/config';
import SystemnutzungComponent from '@/entities/systemnutzung/systemnutzung.vue';
import SystemnutzungClass from '@/entities/systemnutzung/systemnutzung.component';
import SystemnutzungService from '@/entities/systemnutzung/systemnutzung.service';

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
  describe('Systemnutzung Management Component', () => {
    let wrapper: Wrapper<SystemnutzungClass>;
    let comp: SystemnutzungClass;
    let systemnutzungServiceStub: SinonStubbedInstance<SystemnutzungService>;

    beforeEach(() => {
      systemnutzungServiceStub = sinon.createStubInstance<SystemnutzungService>(SystemnutzungService);
      systemnutzungServiceStub.retrieve.resolves({ headers: {} });

      wrapper = shallowMount<SystemnutzungClass>(SystemnutzungComponent, {
        store,
        i18n,
        localVue,
        stubs: { bModal: bModalStub as any },
        provide: {
          alertService: () => new AlertService(store),
          systemnutzungService: () => systemnutzungServiceStub
        }
      });
      comp = wrapper.vm;
    });

    it('should be a Vue instance', () => {
      expect(wrapper.isVueInstance()).toBeTruthy();
    });

    it('Should call load all on init', async () => {
      // GIVEN
      systemnutzungServiceStub.retrieve.resolves({ headers: {}, data: [{ id: 123 }] });

      // WHEN
      comp.retrieveAllSystemnutzungs();
      await comp.$nextTick();

      // THEN
      expect(systemnutzungServiceStub.retrieve.called).toBeTruthy();
      expect(comp.systemnutzungs[0]).toEqual(jasmine.objectContaining({ id: 123 }));
    });
    it('Should call delete service on confirmDelete', async () => {
      // GIVEN
      systemnutzungServiceStub.delete.resolves({});

      // WHEN
      comp.prepareRemove({ id: 123 });
      comp.removeSystemnutzung();
      await comp.$nextTick();

      // THEN
      expect(systemnutzungServiceStub.delete.called).toBeTruthy();
      expect(systemnutzungServiceStub.retrieve.callCount).toEqual(2);
    });
  });
});
