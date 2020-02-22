/* tslint:disable max-line-length */
import { shallowMount, createLocalVue, Wrapper } from '@vue/test-utils';
import sinon, { SinonStubbedInstance } from 'sinon';

import AlertService from '@/shared/alert/alert.service';
import * as config from '@/shared/config/config';
import ZubehoerComponent from '@/entities/zubehoer/zubehoer.vue';
import ZubehoerClass from '@/entities/zubehoer/zubehoer.component';
import ZubehoerService from '@/entities/zubehoer/zubehoer.service';

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
  describe('Zubehoer Management Component', () => {
    let wrapper: Wrapper<ZubehoerClass>;
    let comp: ZubehoerClass;
    let zubehoerServiceStub: SinonStubbedInstance<ZubehoerService>;

    beforeEach(() => {
      zubehoerServiceStub = sinon.createStubInstance<ZubehoerService>(ZubehoerService);
      zubehoerServiceStub.retrieve.resolves({ headers: {} });

      wrapper = shallowMount<ZubehoerClass>(ZubehoerComponent, {
        store,
        i18n,
        localVue,
        stubs: { bModal: bModalStub as any },
        provide: {
          alertService: () => new AlertService(store),
          zubehoerService: () => zubehoerServiceStub
        }
      });
      comp = wrapper.vm;
    });

    it('should be a Vue instance', () => {
      expect(wrapper.isVueInstance()).toBeTruthy();
    });

    it('Should call load all on init', async () => {
      // GIVEN
      zubehoerServiceStub.retrieve.resolves({ headers: {}, data: [{ id: 123 }] });

      // WHEN
      comp.retrieveAllZubehoers();
      await comp.$nextTick();

      // THEN
      expect(zubehoerServiceStub.retrieve.called).toBeTruthy();
      expect(comp.zubehoers[0]).toEqual(jasmine.objectContaining({ id: 123 }));
    });
    it('Should call delete service on confirmDelete', async () => {
      // GIVEN
      zubehoerServiceStub.delete.resolves({});

      // WHEN
      comp.prepareRemove({ id: 123 });
      comp.removeZubehoer();
      await comp.$nextTick();

      // THEN
      expect(zubehoerServiceStub.delete.called).toBeTruthy();
      expect(zubehoerServiceStub.retrieve.callCount).toEqual(2);
    });
  });
});
