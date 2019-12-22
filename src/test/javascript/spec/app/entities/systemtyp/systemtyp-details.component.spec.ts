/* tslint:disable max-line-length */
import { shallowMount, createLocalVue, Wrapper } from '@vue/test-utils';
import sinon, { SinonStubbedInstance } from 'sinon';

import * as config from '@/shared/config/config';
import SystemtypDetailComponent from '@/entities/systemtyp/systemtyp-details.vue';
import SystemtypClass from '@/entities/systemtyp/systemtyp-details.component';
import SystemtypService from '@/entities/systemtyp/systemtyp.service';

const localVue = createLocalVue();

config.initVueApp(localVue);
const i18n = config.initI18N(localVue);
const store = config.initVueXStore(localVue);
localVue.component('font-awesome-icon', {});
localVue.component('router-link', {});

describe('Component Tests', () => {
  describe('Systemtyp Management Detail Component', () => {
    let wrapper: Wrapper<SystemtypClass>;
    let comp: SystemtypClass;
    let systemtypServiceStub: SinonStubbedInstance<SystemtypService>;

    beforeEach(() => {
      systemtypServiceStub = sinon.createStubInstance<SystemtypService>(SystemtypService);

      wrapper = shallowMount<SystemtypClass>(SystemtypDetailComponent, {
        store,
        i18n,
        localVue,
        provide: { systemtypService: () => systemtypServiceStub }
      });
      comp = wrapper.vm;
    });

    describe('OnInit', () => {
      it('Should call load all on init', async () => {
        // GIVEN
        const foundSystemtyp = { id: 123 };
        systemtypServiceStub.find.resolves(foundSystemtyp);

        // WHEN
        comp.retrieveSystemtyp(123);
        await comp.$nextTick();

        // THEN
        expect(comp.systemtyp).toBe(foundSystemtyp);
      });
    });
  });
});
