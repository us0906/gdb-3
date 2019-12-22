/* tslint:disable max-line-length */
import { shallowMount, createLocalVue, Wrapper } from '@vue/test-utils';
import sinon, { SinonStubbedInstance } from 'sinon';

import * as config from '@/shared/config/config';
import SystemnutzungDetailComponent from '@/entities/systemnutzung/systemnutzung-details.vue';
import SystemnutzungClass from '@/entities/systemnutzung/systemnutzung-details.component';
import SystemnutzungService from '@/entities/systemnutzung/systemnutzung.service';

const localVue = createLocalVue();

config.initVueApp(localVue);
const i18n = config.initI18N(localVue);
const store = config.initVueXStore(localVue);
localVue.component('font-awesome-icon', {});
localVue.component('router-link', {});

describe('Component Tests', () => {
  describe('Systemnutzung Management Detail Component', () => {
    let wrapper: Wrapper<SystemnutzungClass>;
    let comp: SystemnutzungClass;
    let systemnutzungServiceStub: SinonStubbedInstance<SystemnutzungService>;

    beforeEach(() => {
      systemnutzungServiceStub = sinon.createStubInstance<SystemnutzungService>(SystemnutzungService);

      wrapper = shallowMount<SystemnutzungClass>(SystemnutzungDetailComponent, {
        store,
        i18n,
        localVue,
        provide: { systemnutzungService: () => systemnutzungServiceStub }
      });
      comp = wrapper.vm;
    });

    describe('OnInit', () => {
      it('Should call load all on init', async () => {
        // GIVEN
        const foundSystemnutzung = { id: 123 };
        systemnutzungServiceStub.find.resolves(foundSystemnutzung);

        // WHEN
        comp.retrieveSystemnutzung(123);
        await comp.$nextTick();

        // THEN
        expect(comp.systemnutzung).toBe(foundSystemnutzung);
      });
    });
  });
});
