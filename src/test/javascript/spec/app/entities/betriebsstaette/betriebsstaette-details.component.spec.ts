/* tslint:disable max-line-length */
import { shallowMount, createLocalVue, Wrapper } from '@vue/test-utils';
import sinon, { SinonStubbedInstance } from 'sinon';

import * as config from '@/shared/config/config';
import BetriebsstaetteDetailComponent from '@/entities/betriebsstaette/betriebsstaette-details.vue';
import BetriebsstaetteClass from '@/entities/betriebsstaette/betriebsstaette-details.component';
import BetriebsstaetteService from '@/entities/betriebsstaette/betriebsstaette.service';

const localVue = createLocalVue();

config.initVueApp(localVue);
const i18n = config.initI18N(localVue);
const store = config.initVueXStore(localVue);
localVue.component('font-awesome-icon', {});
localVue.component('router-link', {});

describe('Component Tests', () => {
  describe('Betriebsstaette Management Detail Component', () => {
    let wrapper: Wrapper<BetriebsstaetteClass>;
    let comp: BetriebsstaetteClass;
    let betriebsstaetteServiceStub: SinonStubbedInstance<BetriebsstaetteService>;

    beforeEach(() => {
      betriebsstaetteServiceStub = sinon.createStubInstance<BetriebsstaetteService>(BetriebsstaetteService);

      wrapper = shallowMount<BetriebsstaetteClass>(BetriebsstaetteDetailComponent, {
        store,
        i18n,
        localVue,
        provide: { betriebsstaetteService: () => betriebsstaetteServiceStub }
      });
      comp = wrapper.vm;
    });

    describe('OnInit', () => {
      it('Should call load all on init', async () => {
        // GIVEN
        const foundBetriebsstaette = { id: 123 };
        betriebsstaetteServiceStub.find.resolves(foundBetriebsstaette);

        // WHEN
        comp.retrieveBetriebsstaette(123);
        await comp.$nextTick();

        // THEN
        expect(comp.betriebsstaette).toBe(foundBetriebsstaette);
      });
    });
  });
});
