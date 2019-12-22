/* tslint:disable max-line-length */
import { shallowMount, createLocalVue, Wrapper } from '@vue/test-utils';
import sinon, { SinonStubbedInstance } from 'sinon';

import * as config from '@/shared/config/config';
import SysteminstanzDetailComponent from '@/entities/systeminstanz/systeminstanz-details.vue';
import SysteminstanzClass from '@/entities/systeminstanz/systeminstanz-details.component';
import SysteminstanzService from '@/entities/systeminstanz/systeminstanz.service';

const localVue = createLocalVue();

config.initVueApp(localVue);
const i18n = config.initI18N(localVue);
const store = config.initVueXStore(localVue);
localVue.component('font-awesome-icon', {});
localVue.component('router-link', {});

describe('Component Tests', () => {
  describe('Systeminstanz Management Detail Component', () => {
    let wrapper: Wrapper<SysteminstanzClass>;
    let comp: SysteminstanzClass;
    let systeminstanzServiceStub: SinonStubbedInstance<SysteminstanzService>;

    beforeEach(() => {
      systeminstanzServiceStub = sinon.createStubInstance<SysteminstanzService>(SysteminstanzService);

      wrapper = shallowMount<SysteminstanzClass>(SysteminstanzDetailComponent, {
        store,
        i18n,
        localVue,
        provide: { systeminstanzService: () => systeminstanzServiceStub }
      });
      comp = wrapper.vm;
    });

    describe('OnInit', () => {
      it('Should call load all on init', async () => {
        // GIVEN
        const foundSysteminstanz = { id: 123 };
        systeminstanzServiceStub.find.resolves(foundSysteminstanz);

        // WHEN
        comp.retrieveSysteminstanz(123);
        await comp.$nextTick();

        // THEN
        expect(comp.systeminstanz).toBe(foundSysteminstanz);
      });
    });
  });
});
