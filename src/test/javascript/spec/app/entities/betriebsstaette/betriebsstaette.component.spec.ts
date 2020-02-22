/* tslint:disable max-line-length */
import { shallowMount, createLocalVue, Wrapper } from '@vue/test-utils';
import sinon, { SinonStubbedInstance } from 'sinon';

import AlertService from '@/shared/alert/alert.service';
import * as config from '@/shared/config/config';
import BetriebsstaetteComponent from '@/entities/betriebsstaette/betriebsstaette.vue';
import BetriebsstaetteClass from '@/entities/betriebsstaette/betriebsstaette.component';
import BetriebsstaetteService from '@/entities/betriebsstaette/betriebsstaette.service';

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
  describe('Betriebsstaette Management Component', () => {
    let wrapper: Wrapper<BetriebsstaetteClass>;
    let comp: BetriebsstaetteClass;
    let betriebsstaetteServiceStub: SinonStubbedInstance<BetriebsstaetteService>;

    beforeEach(() => {
      betriebsstaetteServiceStub = sinon.createStubInstance<BetriebsstaetteService>(BetriebsstaetteService);
      betriebsstaetteServiceStub.retrieve.resolves({ headers: {} });

      wrapper = shallowMount<BetriebsstaetteClass>(BetriebsstaetteComponent, {
        store,
        i18n,
        localVue,
        stubs: { bModal: bModalStub as any },
        provide: {
          alertService: () => new AlertService(store),
          betriebsstaetteService: () => betriebsstaetteServiceStub
        }
      });
      comp = wrapper.vm;
    });

    it('should be a Vue instance', () => {
      expect(wrapper.isVueInstance()).toBeTruthy();
    });

    it('Should call load all on init', async () => {
      // GIVEN
      betriebsstaetteServiceStub.retrieve.resolves({ headers: {}, data: [{ id: 123 }] });

      // WHEN
      comp.retrieveAllBetriebsstaettes();
      await comp.$nextTick();

      // THEN
      expect(betriebsstaetteServiceStub.retrieve.called).toBeTruthy();
      expect(comp.betriebsstaettes[0]).toEqual(jasmine.objectContaining({ id: 123 }));
    });
    it('Should call delete service on confirmDelete', async () => {
      // GIVEN
      betriebsstaetteServiceStub.delete.resolves({});

      // WHEN
      comp.prepareRemove({ id: 123 });
      comp.removeBetriebsstaette();
      await comp.$nextTick();

      // THEN
      expect(betriebsstaetteServiceStub.delete.called).toBeTruthy();
      expect(betriebsstaetteServiceStub.retrieve.callCount).toEqual(2);
    });
  });
});
