/* tslint:disable max-line-length */
import { shallowMount, createLocalVue, Wrapper } from '@vue/test-utils';
import sinon, { SinonStubbedInstance } from 'sinon';

import AlertService from '@/shared/alert/alert.service';
import * as config from '@/shared/config/config';
import SysteminstanzComponent from '@/entities/systeminstanz/systeminstanz.vue';
import SysteminstanzClass from '@/entities/systeminstanz/systeminstanz.component';
import SysteminstanzService from '@/entities/systeminstanz/systeminstanz.service';

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
    hide: () => {},
    show: () => {}
  }
};

describe('Component Tests', () => {
  describe('Systeminstanz Management Component', () => {
    let wrapper: Wrapper<SysteminstanzClass>;
    let comp: SysteminstanzClass;
    let systeminstanzServiceStub: SinonStubbedInstance<SysteminstanzService>;

    beforeEach(() => {
      systeminstanzServiceStub = sinon.createStubInstance<SysteminstanzService>(SysteminstanzService);
      systeminstanzServiceStub.retrieve.resolves({ headers: {} });

      wrapper = shallowMount<SysteminstanzClass>(SysteminstanzComponent, {
        store,
        i18n,
        localVue,
        stubs: { bModal: bModalStub as any },
        provide: {
          alertService: () => new AlertService(store),
          systeminstanzService: () => systeminstanzServiceStub
        }
      });
      comp = wrapper.vm;
    });

    it('should be a Vue instance', () => {
      expect(wrapper.isVueInstance()).toBeTruthy();
    });

    it('Should call load all on init', async () => {
      // GIVEN
      systeminstanzServiceStub.retrieve.resolves({ headers: {}, data: [{ id: 123 }] });

      // WHEN
      comp.retrieveAllSysteminstanzs();
      await comp.$nextTick();

      // THEN
      expect(systeminstanzServiceStub.retrieve.called).toBeTruthy();
      expect(comp.systeminstanzs[0]).toEqual(jasmine.objectContaining({ id: 123 }));
    });
    it('Should call delete service on confirmDelete', async () => {
      // GIVEN
      systeminstanzServiceStub.delete.resolves({});

      // WHEN
      comp.prepareRemove({ id: 123 });
      comp.removeSysteminstanz();
      await comp.$nextTick();

      // THEN
      expect(systeminstanzServiceStub.delete.called).toBeTruthy();
      expect(systeminstanzServiceStub.retrieve.callCount).toEqual(2);
    });
  });
});
