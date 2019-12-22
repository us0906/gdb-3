/* tslint:disable max-line-length */
import { shallowMount, createLocalVue, Wrapper } from '@vue/test-utils';
import sinon, { SinonStubbedInstance } from 'sinon';

import * as config from '@/shared/config/config';
import GeraetTypDetailComponent from '@/entities/geraet-typ/geraet-typ-details.vue';
import GeraetTypClass from '@/entities/geraet-typ/geraet-typ-details.component';
import GeraetTypService from '@/entities/geraet-typ/geraet-typ.service';

const localVue = createLocalVue();

config.initVueApp(localVue);
const i18n = config.initI18N(localVue);
const store = config.initVueXStore(localVue);
localVue.component('font-awesome-icon', {});
localVue.component('router-link', {});

describe('Component Tests', () => {
  describe('GeraetTyp Management Detail Component', () => {
    let wrapper: Wrapper<GeraetTypClass>;
    let comp: GeraetTypClass;
    let geraetTypServiceStub: SinonStubbedInstance<GeraetTypService>;

    beforeEach(() => {
      geraetTypServiceStub = sinon.createStubInstance<GeraetTypService>(GeraetTypService);

      wrapper = shallowMount<GeraetTypClass>(GeraetTypDetailComponent, {
        store,
        i18n,
        localVue,
        provide: { geraetTypService: () => geraetTypServiceStub }
      });
      comp = wrapper.vm;
    });

    describe('OnInit', () => {
      it('Should call load all on init', async () => {
        // GIVEN
        const foundGeraetTyp = { id: 123 };
        geraetTypServiceStub.find.resolves(foundGeraetTyp);

        // WHEN
        comp.retrieveGeraetTyp(123);
        await comp.$nextTick();

        // THEN
        expect(comp.geraetTyp).toBe(foundGeraetTyp);
      });
    });
  });
});
