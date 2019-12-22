/* tslint:disable max-line-length */
import { shallowMount, createLocalVue, Wrapper } from '@vue/test-utils';
import sinon, { SinonStubbedInstance } from 'sinon';

import * as config from '@/shared/config/config';
import BetreiberDetailComponent from '@/entities/betreiber/betreiber-details.vue';
import BetreiberClass from '@/entities/betreiber/betreiber-details.component';
import BetreiberService from '@/entities/betreiber/betreiber.service';

const localVue = createLocalVue();

config.initVueApp(localVue);
const i18n = config.initI18N(localVue);
const store = config.initVueXStore(localVue);
localVue.component('font-awesome-icon', {});
localVue.component('router-link', {});

describe('Component Tests', () => {
  describe('Betreiber Management Detail Component', () => {
    let wrapper: Wrapper<BetreiberClass>;
    let comp: BetreiberClass;
    let betreiberServiceStub: SinonStubbedInstance<BetreiberService>;

    beforeEach(() => {
      betreiberServiceStub = sinon.createStubInstance<BetreiberService>(BetreiberService);

      wrapper = shallowMount<BetreiberClass>(BetreiberDetailComponent, {
        store,
        i18n,
        localVue,
        provide: { betreiberService: () => betreiberServiceStub }
      });
      comp = wrapper.vm;
    });

    describe('OnInit', () => {
      it('Should call load all on init', async () => {
        // GIVEN
        const foundBetreiber = { id: 123 };
        betreiberServiceStub.find.resolves(foundBetreiber);

        // WHEN
        comp.retrieveBetreiber(123);
        await comp.$nextTick();

        // THEN
        expect(comp.betreiber).toBe(foundBetreiber);
      });
    });
  });
});
