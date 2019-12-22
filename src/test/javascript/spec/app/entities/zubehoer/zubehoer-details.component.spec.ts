/* tslint:disable max-line-length */
import { shallowMount, createLocalVue, Wrapper } from '@vue/test-utils';
import sinon, { SinonStubbedInstance } from 'sinon';

import * as config from '@/shared/config/config';
import ZubehoerDetailComponent from '@/entities/zubehoer/zubehoer-details.vue';
import ZubehoerClass from '@/entities/zubehoer/zubehoer-details.component';
import ZubehoerService from '@/entities/zubehoer/zubehoer.service';

const localVue = createLocalVue();

config.initVueApp(localVue);
const i18n = config.initI18N(localVue);
const store = config.initVueXStore(localVue);
localVue.component('font-awesome-icon', {});
localVue.component('router-link', {});

describe('Component Tests', () => {
  describe('Zubehoer Management Detail Component', () => {
    let wrapper: Wrapper<ZubehoerClass>;
    let comp: ZubehoerClass;
    let zubehoerServiceStub: SinonStubbedInstance<ZubehoerService>;

    beforeEach(() => {
      zubehoerServiceStub = sinon.createStubInstance<ZubehoerService>(ZubehoerService);

      wrapper = shallowMount<ZubehoerClass>(ZubehoerDetailComponent, {
        store,
        i18n,
        localVue,
        provide: { zubehoerService: () => zubehoerServiceStub }
      });
      comp = wrapper.vm;
    });

    describe('OnInit', () => {
      it('Should call load all on init', async () => {
        // GIVEN
        const foundZubehoer = { id: 123 };
        zubehoerServiceStub.find.resolves(foundZubehoer);

        // WHEN
        comp.retrieveZubehoer(123);
        await comp.$nextTick();

        // THEN
        expect(comp.zubehoer).toBe(foundZubehoer);
      });
    });
  });
});
