/* tslint:disable max-line-length */
import { shallowMount, createLocalVue, Wrapper } from '@vue/test-utils';
import sinon, { SinonStubbedInstance } from 'sinon';

import * as config from '@/shared/config/config';
import ArztDetailComponent from '@/entities/arzt/arzt-details.vue';
import ArztClass from '@/entities/arzt/arzt-details.component';
import ArztService from '@/entities/arzt/arzt.service';

const localVue = createLocalVue();

config.initVueApp(localVue);
const i18n = config.initI18N(localVue);
const store = config.initVueXStore(localVue);
localVue.component('font-awesome-icon', {});
localVue.component('router-link', {});

describe('Component Tests', () => {
  describe('Arzt Management Detail Component', () => {
    let wrapper: Wrapper<ArztClass>;
    let comp: ArztClass;
    let arztServiceStub: SinonStubbedInstance<ArztService>;

    beforeEach(() => {
      arztServiceStub = sinon.createStubInstance<ArztService>(ArztService);

      wrapper = shallowMount<ArztClass>(ArztDetailComponent, { store, i18n, localVue, provide: { arztService: () => arztServiceStub } });
      comp = wrapper.vm;
    });

    describe('OnInit', () => {
      it('Should call load all on init', async () => {
        // GIVEN
        const foundArzt = { id: 123 };
        arztServiceStub.find.resolves(foundArzt);

        // WHEN
        comp.retrieveArzt(123);
        await comp.$nextTick();

        // THEN
        expect(comp.arzt).toBe(foundArzt);
      });
    });
  });
});
