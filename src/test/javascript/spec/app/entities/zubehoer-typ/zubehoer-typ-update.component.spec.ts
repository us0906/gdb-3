/* tslint:disable max-line-length */
import { shallowMount, createLocalVue, Wrapper } from '@vue/test-utils';
import sinon, { SinonStubbedInstance } from 'sinon';
import Router from 'vue-router';

import AlertService from '@/shared/alert/alert.service';
import * as config from '@/shared/config/config';
import ZubehoerTypUpdateComponent from '@/entities/zubehoer-typ/zubehoer-typ-update.vue';
import ZubehoerTypClass from '@/entities/zubehoer-typ/zubehoer-typ-update.component';
import ZubehoerTypService from '@/entities/zubehoer-typ/zubehoer-typ.service';

import ZubehoerService from '@/entities/zubehoer/zubehoer.service';

const localVue = createLocalVue();

config.initVueApp(localVue);
const i18n = config.initI18N(localVue);
const store = config.initVueXStore(localVue);
const router = new Router();
localVue.use(Router);
localVue.component('font-awesome-icon', {});

describe('Component Tests', () => {
  describe('ZubehoerTyp Management Update Component', () => {
    let wrapper: Wrapper<ZubehoerTypClass>;
    let comp: ZubehoerTypClass;
    let zubehoerTypServiceStub: SinonStubbedInstance<ZubehoerTypService>;

    beforeEach(() => {
      zubehoerTypServiceStub = sinon.createStubInstance<ZubehoerTypService>(ZubehoerTypService);

      wrapper = shallowMount<ZubehoerTypClass>(ZubehoerTypUpdateComponent, {
        store,
        i18n,
        localVue,
        router,
        provide: {
          alertService: () => new AlertService(store),
          zubehoerTypService: () => zubehoerTypServiceStub,

          zubehoerService: () => new ZubehoerService()
        }
      });
      comp = wrapper.vm;
    });

    describe('save', () => {
      it('Should call update service on save for existing entity', async () => {
        // GIVEN
        const entity = { id: 123 };
        comp.zubehoerTyp = entity;
        zubehoerTypServiceStub.update.resolves(entity);

        // WHEN
        comp.save();
        await comp.$nextTick();

        // THEN
        expect(zubehoerTypServiceStub.update.calledWith(entity)).toBeTruthy();
        expect(comp.isSaving).toEqual(false);
      });

      it('Should call create service on save for new entity', async () => {
        // GIVEN
        const entity = {};
        comp.zubehoerTyp = entity;
        zubehoerTypServiceStub.create.resolves(entity);

        // WHEN
        comp.save();
        await comp.$nextTick();

        // THEN
        expect(zubehoerTypServiceStub.create.calledWith(entity)).toBeTruthy();
        expect(comp.isSaving).toEqual(false);
      });
    });
  });
});
