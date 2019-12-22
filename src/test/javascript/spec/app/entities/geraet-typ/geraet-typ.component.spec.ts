/* tslint:disable max-line-length */
import { shallowMount, createLocalVue, Wrapper } from '@vue/test-utils';
import sinon, { SinonStubbedInstance } from 'sinon';

import AlertService from '@/shared/alert/alert.service';
import * as config from '@/shared/config/config';
import GeraetTypComponent from '@/entities/geraet-typ/geraet-typ.vue';
import GeraetTypClass from '@/entities/geraet-typ/geraet-typ.component';
import GeraetTypService from '@/entities/geraet-typ/geraet-typ.service';

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
  describe('GeraetTyp Management Component', () => {
    let wrapper: Wrapper<GeraetTypClass>;
    let comp: GeraetTypClass;
    let geraetTypServiceStub: SinonStubbedInstance<GeraetTypService>;

    beforeEach(() => {
      geraetTypServiceStub = sinon.createStubInstance<GeraetTypService>(GeraetTypService);
      geraetTypServiceStub.retrieve.resolves({ headers: {} });

      wrapper = shallowMount<GeraetTypClass>(GeraetTypComponent, {
        store,
        i18n,
        localVue,
        stubs: { bModal: bModalStub as any },
        provide: {
          alertService: () => new AlertService(store),
          geraetTypService: () => geraetTypServiceStub
        }
      });
      comp = wrapper.vm;
    });

    it('should be a Vue instance', () => {
      expect(wrapper.isVueInstance()).toBeTruthy();
    });

    it('Should call load all on init', async () => {
      // GIVEN
      geraetTypServiceStub.retrieve.resolves({ headers: {}, data: [{ id: 123 }] });

      // WHEN
      comp.retrieveAllGeraetTyps();
      await comp.$nextTick();

      // THEN
      expect(geraetTypServiceStub.retrieve.called).toBeTruthy();
      expect(comp.geraetTyps[0]).toEqual(jasmine.objectContaining({ id: 123 }));
    });
    it('Should call delete service on confirmDelete', async () => {
      // GIVEN
      geraetTypServiceStub.delete.resolves({});

      // WHEN
      comp.prepareRemove({ id: 123 });
      comp.removeGeraetTyp();
      await comp.$nextTick();

      // THEN
      expect(geraetTypServiceStub.delete.called).toBeTruthy();
      expect(geraetTypServiceStub.retrieve.callCount).toEqual(2);
    });
  });
});
