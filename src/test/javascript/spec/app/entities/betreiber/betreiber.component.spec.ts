/* tslint:disable max-line-length */
import { shallowMount, createLocalVue, Wrapper } from '@vue/test-utils';
import sinon, { SinonStubbedInstance } from 'sinon';

import AlertService from '@/shared/alert/alert.service';
import * as config from '@/shared/config/config';
import BetreiberComponent from '@/entities/betreiber/betreiber.vue';
import BetreiberClass from '@/entities/betreiber/betreiber.component';
import BetreiberService from '@/entities/betreiber/betreiber.service';

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
  describe('Betreiber Management Component', () => {
    let wrapper: Wrapper<BetreiberClass>;
    let comp: BetreiberClass;
    let betreiberServiceStub: SinonStubbedInstance<BetreiberService>;

    beforeEach(() => {
      betreiberServiceStub = sinon.createStubInstance<BetreiberService>(BetreiberService);
      betreiberServiceStub.retrieve.resolves({ headers: {} });

      wrapper = shallowMount<BetreiberClass>(BetreiberComponent, {
        store,
        i18n,
        localVue,
        stubs: { bModal: bModalStub as any },
        provide: {
          alertService: () => new AlertService(store),
          betreiberService: () => betreiberServiceStub
        }
      });
      comp = wrapper.vm;
    });

    it('should be a Vue instance', () => {
      expect(wrapper.isVueInstance()).toBeTruthy();
    });

    it('Should call load all on init', async () => {
      // GIVEN
      betreiberServiceStub.retrieve.resolves({ headers: {}, data: [{ id: 123 }] });

      // WHEN
      comp.retrieveAllBetreibers();
      await comp.$nextTick();

      // THEN
      expect(betreiberServiceStub.retrieve.called).toBeTruthy();
      expect(comp.betreibers[0]).toEqual(jasmine.objectContaining({ id: 123 }));
    });
    it('Should call delete service on confirmDelete', async () => {
      // GIVEN
      betreiberServiceStub.delete.resolves({});

      // WHEN
      comp.prepareRemove({ id: 123 });
      comp.removeBetreiber();
      await comp.$nextTick();

      // THEN
      expect(betreiberServiceStub.delete.called).toBeTruthy();
      expect(betreiberServiceStub.retrieve.callCount).toEqual(2);
    });
  });
});
