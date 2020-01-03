// The Vue build version to load with the `import` command
// (runtime-only or standalone) has been set in webpack.common with an alias.
import Vue from 'vue';
import { FontAwesomeIcon } from '@fortawesome/vue-fontawesome';
import App from './app.vue';
import Vue2Filters from 'vue2-filters';
import router from './router';
import * as config from './shared/config/config';
import * as bootstrapVueConfig from './shared/config/config-bootstrap-vue';
import JhiItemCountComponent from './shared/jhi-item-count.vue';
import AuditsService from './admin/audits/audits.service';

import HealthService from './admin/health/health.service';
import MetricsService from './admin/metrics/metrics.service';
import LogsService from './admin/logs/logs.service';

import LoginService from './account/login.service';
import AccountService from './account/account.service';

import '../content/scss/vendor.scss';
import 'vue-select/dist/vue-select.css';
import AlertService from '@/shared/alert/alert.service';
import TranslationService from '@/locale/translation.service';
import ConfigurationService from '@/admin/configuration/configuration.service';

import GeraetTypService from '@/entities/geraet-typ/geraet-typ.service';
import HerstellerService from '@/entities/hersteller/hersteller.service';
import ZubehoerTypService from '@/entities/zubehoer-typ/zubehoer-typ.service';
import GeraetService from '@/entities/geraet/geraet.service';
import ZubehoerService from '@/entities/zubehoer/zubehoer.service';
import SystemtypService from '@/entities/systemtyp/systemtyp.service';
import SysteminstanzService from '@/entities/systeminstanz/systeminstanz.service';
import ArztService from '@/entities/arzt/arzt.service';
import BetreiberService from '@/entities/betreiber/betreiber.service';
import BetriebsstaetteService from '@/entities/betriebsstaette/betriebsstaette.service';
import SystemnutzungService from '@/entities/systemnutzung/systemnutzung.service';
// jhipster-needle-add-entity-service-to-main-import - JHipster will import entities services here

import vSelect from 'vue-select';

Vue.config.productionTip = false;
config.initVueApp(Vue);
config.initFortAwesome(Vue);
bootstrapVueConfig.initBootstrapVue(Vue);
Vue.use(Vue2Filters);
Vue.component('font-awesome-icon', FontAwesomeIcon);
Vue.component('jhi-item-count', JhiItemCountComponent);
Vue.component('v-select', vSelect);

const i18n = config.initI18N(Vue);
const store = config.initVueXStore(Vue);

const alertService = new AlertService(store);
const translationService = new TranslationService(store, i18n);
const loginService = new LoginService();
const accountService = new AccountService(store, translationService, (<any>Vue).cookie, router);

router.beforeEach((to, from, next) => {
  if (!to.matched.length) {
    next('/not-found');
  }

  if (to.meta && to.meta.authorities && to.meta.authorities.length > 0) {
    if (!accountService.hasAnyAuthority(to.meta.authorities)) {
      sessionStorage.setItem('requested-url', to.fullPath);
      next('/forbidden');
    } else {
      next();
    }
  } else {
    // no authorities, so just proceed
    next();
  }
});

/* tslint:disable */
new Vue({
  el: '#app',
  components: { App },
  template: '<App/>',
  router,
  provide: {
    loginService: () => loginService,

    auditsService: () => new AuditsService(),

    healthService: () => new HealthService(),

    configurationService: () => new ConfigurationService(),
    logsService: () => new LogsService(),
    metricsService: () => new MetricsService(),
    alertService: () => alertService,
    translationService: () => translationService,
    geraetTypService: () => new GeraetTypService(),
    herstellerService: () => new HerstellerService(),
    zubehoerTypService: () => new ZubehoerTypService(),
    geraetService: () => new GeraetService(),
    zubehoerService: () => new ZubehoerService(),
    systemtypService: () => new SystemtypService(),
    systeminstanzService: () => new SysteminstanzService(),
    arztService: () => new ArztService(),
    betreiberService: () => new BetreiberService(),
    betriebsstaetteService: () => new BetriebsstaetteService(),
    systemnutzungService: () => new SystemnutzungService(),
    // jhipster-needle-add-entity-service-to-main - JHipster will import entities services here
    accountService: () => accountService
  },
  i18n,
  store
});
