/* tslint:disable max-line-length */
import { shallowMount, createLocalVue, Wrapper } from '@vue/test-utils';
import sinon, { SinonStubbedInstance } from 'sinon';
import Router from 'vue-router';

import AlertService from '@/shared/alert/alert.service';
import * as config from '@/shared/config/config';
import BetreiberUpdateComponent from '@/entities/betreiber/betreiber-update.vue';
import BetreiberClass from '@/entities/betreiber/betreiber-update.component';
import BetreiberService from '@/entities/betreiber/betreiber.service';

import SysteminstanzService from '@/entities/systeminstanz/systeminstanz.service';

const localVue = createLocalVue();

config.initVueApp(localVue);
const i18n = config.initI18N(localVue);
const store = config.initVueXStore(localVue);
const router = new Router();
localVue.use(Router);
localVue.component('font-awesome-icon', {});

describe('Component Tests', () => {
  describe('Betreiber Management Update Component', () => {
    let wrapper: Wrapper<BetreiberClass>;
    let comp: BetreiberClass;
    let betreiberServiceStub: SinonStubbedInstance<BetreiberService>;

    beforeEach(() => {
      betreiberServiceStub = sinon.createStubInstance<BetreiberService>(BetreiberService);

      wrapper = shallowMount<BetreiberClass>(BetreiberUpdateComponent, {
        store,
        i18n,
        localVue,
        router,
        provide: {
          alertService: () => new AlertService(store),
          betreiberService: () => betreiberServiceStub,

          systeminstanzService: () => new SysteminstanzService()
        }
      });
      comp = wrapper.vm;
    });

    describe('save', () => {
      it('Should call update service on save for existing entity', async () => {
        // GIVEN
        const entity = { id: 123 };
        comp.betreiber = entity;
        betreiberServiceStub.update.resolves(entity);

        // WHEN
        comp.save();
        await comp.$nextTick();

        // THEN
        expect(betreiberServiceStub.update.calledWith(entity)).toBeTruthy();
        expect(comp.isSaving).toEqual(false);
      });

      it('Should call create service on save for new entity', async () => {
        // GIVEN
        const entity = {};
        comp.betreiber = entity;
        betreiberServiceStub.create.resolves(entity);

        // WHEN
        comp.save();
        await comp.$nextTick();

        // THEN
        expect(betreiberServiceStub.create.calledWith(entity)).toBeTruthy();
        expect(comp.isSaving).toEqual(false);
      });
    });
  });
});
