/* tslint:disable max-line-length */
import { shallowMount, createLocalVue, Wrapper } from '@vue/test-utils';
import sinon, { SinonStubbedInstance } from 'sinon';
import Router from 'vue-router';

import AlertService from '@/shared/alert/alert.service';
import * as config from '@/shared/config/config';
import BetriebsstaetteUpdateComponent from '@/entities/betriebsstaette/betriebsstaette-update.vue';
import BetriebsstaetteClass from '@/entities/betriebsstaette/betriebsstaette-update.component';
import BetriebsstaetteService from '@/entities/betriebsstaette/betriebsstaette.service';

import SysteminstanzService from '@/entities/systeminstanz/systeminstanz.service';

const localVue = createLocalVue();

config.initVueApp(localVue);
const i18n = config.initI18N(localVue);
const store = config.initVueXStore(localVue);
const router = new Router();
localVue.use(Router);
localVue.component('font-awesome-icon', {});

describe('Component Tests', () => {
  describe('Betriebsstaette Management Update Component', () => {
    let wrapper: Wrapper<BetriebsstaetteClass>;
    let comp: BetriebsstaetteClass;
    let betriebsstaetteServiceStub: SinonStubbedInstance<BetriebsstaetteService>;

    beforeEach(() => {
      betriebsstaetteServiceStub = sinon.createStubInstance<BetriebsstaetteService>(BetriebsstaetteService);

      wrapper = shallowMount<BetriebsstaetteClass>(BetriebsstaetteUpdateComponent, {
        store,
        i18n,
        localVue,
        router,
        provide: {
          alertService: () => new AlertService(store),
          betriebsstaetteService: () => betriebsstaetteServiceStub,

          systeminstanzService: () => new SysteminstanzService()
        }
      });
      comp = wrapper.vm;
    });

    describe('save', () => {
      it('Should call update service on save for existing entity', async () => {
        // GIVEN
        const entity = { id: 123 };
        comp.betriebsstaette = entity;
        betriebsstaetteServiceStub.update.resolves(entity);

        // WHEN
        comp.save();
        await comp.$nextTick();

        // THEN
        expect(betriebsstaetteServiceStub.update.calledWith(entity)).toBeTruthy();
        expect(comp.isSaving).toEqual(false);
      });

      it('Should call create service on save for new entity', async () => {
        // GIVEN
        const entity = {};
        comp.betriebsstaette = entity;
        betriebsstaetteServiceStub.create.resolves(entity);

        // WHEN
        comp.save();
        await comp.$nextTick();

        // THEN
        expect(betriebsstaetteServiceStub.create.calledWith(entity)).toBeTruthy();
        expect(comp.isSaving).toEqual(false);
      });
    });
  });
});
