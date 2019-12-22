/* tslint:disable max-line-length */
import { shallowMount, createLocalVue, Wrapper } from '@vue/test-utils';
import sinon, { SinonStubbedInstance } from 'sinon';

import AlertService from '@/shared/alert/alert.service';
import * as config from '@/shared/config/config';
import HerstellerComponent from '@/entities/hersteller/hersteller.vue';
import HerstellerClass from '@/entities/hersteller/hersteller.component';
import HerstellerService from '@/entities/hersteller/hersteller.service';

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
  describe('Hersteller Management Component', () => {
    let wrapper: Wrapper<HerstellerClass>;
    let comp: HerstellerClass;
    let herstellerServiceStub: SinonStubbedInstance<HerstellerService>;

    beforeEach(() => {
      herstellerServiceStub = sinon.createStubInstance<HerstellerService>(HerstellerService);
      herstellerServiceStub.retrieve.resolves({ headers: {} });

      wrapper = shallowMount<HerstellerClass>(HerstellerComponent, {
        store,
        i18n,
        localVue,
        stubs: { bModal: bModalStub as any },
        provide: {
          alertService: () => new AlertService(store),
          herstellerService: () => herstellerServiceStub
        }
      });
      comp = wrapper.vm;
    });

    it('should be a Vue instance', () => {
      expect(wrapper.isVueInstance()).toBeTruthy();
    });

    it('Should call load all on init', async () => {
      // GIVEN
      herstellerServiceStub.retrieve.resolves({ headers: {}, data: [{ id: 123 }] });

      // WHEN
      comp.retrieveAllHerstellers();
      await comp.$nextTick();

      // THEN
      expect(herstellerServiceStub.retrieve.called).toBeTruthy();
      expect(comp.herstellers[0]).toEqual(jasmine.objectContaining({ id: 123 }));
    });
    it('Should call delete service on confirmDelete', async () => {
      // GIVEN
      herstellerServiceStub.delete.resolves({});

      // WHEN
      comp.prepareRemove({ id: 123 });
      comp.removeHersteller();
      await comp.$nextTick();

      // THEN
      expect(herstellerServiceStub.delete.called).toBeTruthy();
      expect(herstellerServiceStub.retrieve.callCount).toEqual(2);
    });
  });
});
