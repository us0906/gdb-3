/* tslint:disable max-line-length */
import { shallowMount, createLocalVue, Wrapper } from '@vue/test-utils';
import sinon, { SinonStubbedInstance } from 'sinon';

import * as config from '@/shared/config/config';
import ZubehoerTypDetailComponent from '@/entities/zubehoer-typ/zubehoer-typ-details.vue';
import ZubehoerTypClass from '@/entities/zubehoer-typ/zubehoer-typ-details.component';
import ZubehoerTypService from '@/entities/zubehoer-typ/zubehoer-typ.service';

const localVue = createLocalVue();

config.initVueApp(localVue);
const i18n = config.initI18N(localVue);
const store = config.initVueXStore(localVue);
localVue.component('font-awesome-icon', {});
localVue.component('router-link', {});

describe('Component Tests', () => {
  describe('ZubehoerTyp Management Detail Component', () => {
    let wrapper: Wrapper<ZubehoerTypClass>;
    let comp: ZubehoerTypClass;
    let zubehoerTypServiceStub: SinonStubbedInstance<ZubehoerTypService>;

    beforeEach(() => {
      zubehoerTypServiceStub = sinon.createStubInstance<ZubehoerTypService>(ZubehoerTypService);

      wrapper = shallowMount<ZubehoerTypClass>(ZubehoerTypDetailComponent, {
        store,
        i18n,
        localVue,
        provide: { zubehoerTypService: () => zubehoerTypServiceStub }
      });
      comp = wrapper.vm;
    });

    describe('OnInit', () => {
      it('Should call load all on init', async () => {
        // GIVEN
        const foundZubehoerTyp = { id: 123 };
        zubehoerTypServiceStub.find.resolves(foundZubehoerTyp);

        // WHEN
        comp.retrieveZubehoerTyp(123);
        await comp.$nextTick();

        // THEN
        expect(comp.zubehoerTyp).toBe(foundZubehoerTyp);
      });
    });
  });
});
