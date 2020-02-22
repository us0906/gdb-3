/* tslint:disable max-line-length */
import { shallowMount, createLocalVue, Wrapper } from '@vue/test-utils';
import sinon, { SinonStubbedInstance } from 'sinon';

import AlertService from '@/shared/alert/alert.service';
import * as config from '@/shared/config/config';
import ArztComponent from '@/entities/arzt/arzt.vue';
import ArztClass from '@/entities/arzt/arzt.component';
import ArztService from '@/entities/arzt/arzt.service';

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
  describe('Arzt Management Component', () => {
    let wrapper: Wrapper<ArztClass>;
    let comp: ArztClass;
    let arztServiceStub: SinonStubbedInstance<ArztService>;

    beforeEach(() => {
      arztServiceStub = sinon.createStubInstance<ArztService>(ArztService);
      arztServiceStub.retrieve.resolves({ headers: {} });

      wrapper = shallowMount<ArztClass>(ArztComponent, {
        store,
        i18n,
        localVue,
        stubs: { bModal: bModalStub as any },
        provide: {
          alertService: () => new AlertService(store),
          arztService: () => arztServiceStub
        }
      });
      comp = wrapper.vm;
    });

    it('should be a Vue instance', () => {
      expect(wrapper.isVueInstance()).toBeTruthy();
    });

    it('Should call load all on init', async () => {
      // GIVEN
      arztServiceStub.retrieve.resolves({ headers: {}, data: [{ id: 123 }] });

      // WHEN
      comp.retrieveAllArzts();
      await comp.$nextTick();

      // THEN
      expect(arztServiceStub.retrieve.called).toBeTruthy();
      expect(comp.arzts[0]).toEqual(jasmine.objectContaining({ id: 123 }));
    });
    it('Should call delete service on confirmDelete', async () => {
      // GIVEN
      arztServiceStub.delete.resolves({});

      // WHEN
      comp.prepareRemove({ id: 123 });
      comp.removeArzt();
      await comp.$nextTick();

      // THEN
      expect(arztServiceStub.delete.called).toBeTruthy();
      expect(arztServiceStub.retrieve.callCount).toEqual(2);
    });
  });
});
