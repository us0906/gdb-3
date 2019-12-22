/* tslint:disable max-line-length */
import { shallowMount, createLocalVue, Wrapper } from '@vue/test-utils';
import sinon, { SinonStubbedInstance } from 'sinon';

import AlertService from '@/shared/alert/alert.service';
import * as config from '@/shared/config/config';
import ZubehoerTypComponent from '@/entities/zubehoer-typ/zubehoer-typ.vue';
import ZubehoerTypClass from '@/entities/zubehoer-typ/zubehoer-typ.component';
import ZubehoerTypService from '@/entities/zubehoer-typ/zubehoer-typ.service';

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
  describe('ZubehoerTyp Management Component', () => {
    let wrapper: Wrapper<ZubehoerTypClass>;
    let comp: ZubehoerTypClass;
    let zubehoerTypServiceStub: SinonStubbedInstance<ZubehoerTypService>;

    beforeEach(() => {
      zubehoerTypServiceStub = sinon.createStubInstance<ZubehoerTypService>(ZubehoerTypService);
      zubehoerTypServiceStub.retrieve.resolves({ headers: {} });

      wrapper = shallowMount<ZubehoerTypClass>(ZubehoerTypComponent, {
        store,
        i18n,
        localVue,
        stubs: { bModal: bModalStub as any },
        provide: {
          alertService: () => new AlertService(store),
          zubehoerTypService: () => zubehoerTypServiceStub
        }
      });
      comp = wrapper.vm;
    });

    it('should be a Vue instance', () => {
      expect(wrapper.isVueInstance()).toBeTruthy();
    });

    it('Should call load all on init', async () => {
      // GIVEN
      zubehoerTypServiceStub.retrieve.resolves({ headers: {}, data: [{ id: 123 }] });

      // WHEN
      comp.retrieveAllZubehoerTyps();
      await comp.$nextTick();

      // THEN
      expect(zubehoerTypServiceStub.retrieve.called).toBeTruthy();
      expect(comp.zubehoerTyps[0]).toEqual(jasmine.objectContaining({ id: 123 }));
    });
    it('Should call delete service on confirmDelete', async () => {
      // GIVEN
      zubehoerTypServiceStub.delete.resolves({});

      // WHEN
      comp.prepareRemove({ id: 123 });
      comp.removeZubehoerTyp();
      await comp.$nextTick();

      // THEN
      expect(zubehoerTypServiceStub.delete.called).toBeTruthy();
      expect(zubehoerTypServiceStub.retrieve.callCount).toEqual(2);
    });
  });
});
