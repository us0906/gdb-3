/* tslint:disable max-line-length */
import { shallowMount, createLocalVue, Wrapper } from '@vue/test-utils';
import sinon, { SinonStubbedInstance } from 'sinon';

import * as config from '@/shared/config/config';
import HerstellerDetailComponent from '@/entities/hersteller/hersteller-details.vue';
import HerstellerClass from '@/entities/hersteller/hersteller-details.component';
import HerstellerService from '@/entities/hersteller/hersteller.service';

const localVue = createLocalVue();

config.initVueApp(localVue);
const i18n = config.initI18N(localVue);
const store = config.initVueXStore(localVue);
localVue.component('font-awesome-icon', {});
localVue.component('router-link', {});

describe('Component Tests', () => {
  describe('Hersteller Management Detail Component', () => {
    let wrapper: Wrapper<HerstellerClass>;
    let comp: HerstellerClass;
    let herstellerServiceStub: SinonStubbedInstance<HerstellerService>;

    beforeEach(() => {
      herstellerServiceStub = sinon.createStubInstance<HerstellerService>(HerstellerService);

      wrapper = shallowMount<HerstellerClass>(HerstellerDetailComponent, {
        store,
        i18n,
        localVue,
        provide: { herstellerService: () => herstellerServiceStub }
      });
      comp = wrapper.vm;
    });

    describe('OnInit', () => {
      it('Should call load all on init', async () => {
        // GIVEN
        const foundHersteller = { id: 123 };
        herstellerServiceStub.find.resolves(foundHersteller);

        // WHEN
        comp.retrieveHersteller(123);
        await comp.$nextTick();

        // THEN
        expect(comp.hersteller).toBe(foundHersteller);
      });
    });
  });
});
