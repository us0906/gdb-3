/* tslint:disable max-line-length */
import { shallowMount, createLocalVue, Wrapper } from '@vue/test-utils';
import sinon, { SinonStubbedInstance } from 'sinon';

import * as config from '@/shared/config/config';
import GeraetDetailComponent from '@/entities/geraet/geraet-details.vue';
import GeraetClass from '@/entities/geraet/geraet-details.component';
import GeraetService from '@/entities/geraet/geraet.service';

const localVue = createLocalVue();

config.initVueApp(localVue);
const i18n = config.initI18N(localVue);
const store = config.initVueXStore(localVue);
localVue.component('font-awesome-icon', {});
localVue.component('router-link', {});

describe('Component Tests', () => {
  describe('Geraet Management Detail Component', () => {
    let wrapper: Wrapper<GeraetClass>;
    let comp: GeraetClass;
    let geraetServiceStub: SinonStubbedInstance<GeraetService>;

    beforeEach(() => {
      geraetServiceStub = sinon.createStubInstance<GeraetService>(GeraetService);

      wrapper = shallowMount<GeraetClass>(GeraetDetailComponent, {
        store,
        i18n,
        localVue,
        provide: { geraetService: () => geraetServiceStub }
      });
      comp = wrapper.vm;
    });

    describe('OnInit', () => {
      it('Should call load all on init', async () => {
        // GIVEN
        const foundGeraet = { id: 123 };
        geraetServiceStub.find.resolves(foundGeraet);

        // WHEN
        comp.retrieveGeraet(123);
        await comp.$nextTick();

        // THEN
        expect(comp.geraet).toBe(foundGeraet);
      });
    });
  });
});
