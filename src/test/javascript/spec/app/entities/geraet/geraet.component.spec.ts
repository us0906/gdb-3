/* tslint:disable max-line-length */
import { shallowMount, createLocalVue, Wrapper } from '@vue/test-utils';
import sinon, { SinonStubbedInstance } from 'sinon';

import AlertService from '@/shared/alert/alert.service';
import * as config from '@/shared/config/config';
import GeraetComponent from '@/entities/geraet/geraet.vue';
import GeraetClass from '@/entities/geraet/geraet.component';
import GeraetService from '@/entities/geraet/geraet.service';

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
  describe('Geraet Management Component', () => {
    let wrapper: Wrapper<GeraetClass>;
    let comp: GeraetClass;
    let geraetServiceStub: SinonStubbedInstance<GeraetService>;

    beforeEach(() => {
      geraetServiceStub = sinon.createStubInstance<GeraetService>(GeraetService);
      geraetServiceStub.retrieve.resolves({ headers: {} });

      wrapper = shallowMount<GeraetClass>(GeraetComponent, {
        store,
        i18n,
        localVue,
        stubs: { bModal: bModalStub as any },
        provide: {
          alertService: () => new AlertService(store),
          geraetService: () => geraetServiceStub
        }
      });
      comp = wrapper.vm;
    });

    it('should be a Vue instance', () => {
      expect(wrapper.isVueInstance()).toBeTruthy();
    });

    it('Should call load all on init', async () => {
      // GIVEN
      geraetServiceStub.retrieve.resolves({ headers: {}, data: [{ id: 123 }] });

      // WHEN
      comp.retrieveAllGeraets();
      await comp.$nextTick();

      // THEN
      expect(geraetServiceStub.retrieve.called).toBeTruthy();
      expect(comp.geraets[0]).toEqual(jasmine.objectContaining({ id: 123 }));
    });
    it('Should call delete service on confirmDelete', async () => {
      // GIVEN
      geraetServiceStub.delete.resolves({});

      // WHEN
      comp.prepareRemove({ id: 123 });
      comp.removeGeraet();
      await comp.$nextTick();

      // THEN
      expect(geraetServiceStub.delete.called).toBeTruthy();
      expect(geraetServiceStub.retrieve.callCount).toEqual(2);
    });
  });
});
