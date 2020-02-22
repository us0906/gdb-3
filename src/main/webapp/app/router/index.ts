import Vue from 'vue';
import Component from 'vue-class-component';
Component.registerHooks([
  'beforeRouteEnter',
  'beforeRouteLeave',
  'beforeRouteUpdate' // for vue-router 2.2+
]);
import Router from 'vue-router';
import { Authority } from '@/shared/security/authority';
const Home = () => import('../core/home/home.vue');
const Error = () => import('../core/error/error.vue');
const JhiConfigurationComponent = () => import('../admin/configuration/configuration.vue');
const JhiDocsComponent = () => import('../admin/docs/docs.vue');
const JhiHealthComponent = () => import('../admin/health/health.vue');
const JhiLogsComponent = () => import('../admin/logs/logs.vue');
const JhiAuditsComponent = () => import('../admin/audits/audits.vue');
const JhiMetricsComponent = () => import('../admin/metrics/metrics.vue');
/* tslint:disable */
// prettier-ignore
const GeraetTyp = () => import('../entities/geraet-typ/geraet-typ.vue');
// prettier-ignore
const GeraetTypUpdate = () => import('../entities/geraet-typ/geraet-typ-update.vue');
// prettier-ignore
const GeraetTypDetails = () => import('../entities/geraet-typ/geraet-typ-details.vue');
// prettier-ignore
const Hersteller = () => import('../entities/hersteller/hersteller.vue');
// prettier-ignore
const HerstellerUpdate = () => import('../entities/hersteller/hersteller-update.vue');
// prettier-ignore
const HerstellerDetails = () => import('../entities/hersteller/hersteller-details.vue');
// prettier-ignore
const ZubehoerTyp = () => import('../entities/zubehoer-typ/zubehoer-typ.vue');
// prettier-ignore
const ZubehoerTypUpdate = () => import('../entities/zubehoer-typ/zubehoer-typ-update.vue');
// prettier-ignore
const ZubehoerTypDetails = () => import('../entities/zubehoer-typ/zubehoer-typ-details.vue');
// prettier-ignore
const Geraet = () => import('../entities/geraet/geraet.vue');
// prettier-ignore
const GeraetUpdate = () => import('../entities/geraet/geraet-update.vue');
// prettier-ignore
const GeraetDetails = () => import('../entities/geraet/geraet-details.vue');
// prettier-ignore
const Zubehoer = () => import('../entities/zubehoer/zubehoer.vue');
// prettier-ignore
const ZubehoerUpdate = () => import('../entities/zubehoer/zubehoer-update.vue');
// prettier-ignore
const ZubehoerDetails = () => import('../entities/zubehoer/zubehoer-details.vue');
// prettier-ignore
const Systemtyp = () => import('../entities/systemtyp/systemtyp.vue');
// prettier-ignore
const SystemtypUpdate = () => import('../entities/systemtyp/systemtyp-update.vue');
// prettier-ignore
const SystemtypDetails = () => import('../entities/systemtyp/systemtyp-details.vue');
// prettier-ignore
const Systeminstanz = () => import('../entities/systeminstanz/systeminstanz.vue');
// prettier-ignore
const SysteminstanzUpdate = () => import('../entities/systeminstanz/systeminstanz-update.vue');
// prettier-ignore
const SysteminstanzDetails = () => import('../entities/systeminstanz/systeminstanz-details.vue');
// prettier-ignore
const Arzt = () => import('../entities/arzt/arzt.vue');
// prettier-ignore
const ArztUpdate = () => import('../entities/arzt/arzt-update.vue');
// prettier-ignore
const ArztDetails = () => import('../entities/arzt/arzt-details.vue');
// prettier-ignore
const Betreiber = () => import('../entities/betreiber/betreiber.vue');
// prettier-ignore
const BetreiberUpdate = () => import('../entities/betreiber/betreiber-update.vue');
// prettier-ignore
const BetreiberDetails = () => import('../entities/betreiber/betreiber-details.vue');
// prettier-ignore
const Betriebsstaette = () => import('../entities/betriebsstaette/betriebsstaette.vue');
// prettier-ignore
const BetriebsstaetteUpdate = () => import('../entities/betriebsstaette/betriebsstaette-update.vue');
// prettier-ignore
const BetriebsstaetteDetails = () => import('../entities/betriebsstaette/betriebsstaette-details.vue');
// prettier-ignore
const Systemnutzung = () => import('../entities/systemnutzung/systemnutzung.vue');
// prettier-ignore
const SystemnutzungUpdate = () => import('../entities/systemnutzung/systemnutzung-update.vue');
// prettier-ignore
const SystemnutzungDetails = () => import('../entities/systemnutzung/systemnutzung-details.vue');
// jhipster-needle-add-entity-to-router-import - JHipster will import entities to the router here

Vue.use(Router);

// prettier-ignore
export default new Router({
  mode: 'history',
  routes: [
    {
      path: '/',
      name: 'Home',
      component: Home
    },
    {
      path: '/forbidden',
      name: 'Forbidden',
      component: Error,
      meta: { error403: true }
    },
    {
      path: '/not-found',
      name: 'NotFound',
      component: Error,
      meta: { error404: true }
    },
    {
      path: '/admin/docs',
      name: 'JhiDocsComponent',
      component: JhiDocsComponent,
      meta: { authorities: [Authority.ADMIN] }
    },
    {
      path: '/admin/audits',
      name: 'JhiAuditsComponent',
      component: JhiAuditsComponent,
      meta: { authorities: [Authority.ADMIN] }
    },
    {
      path: '/admin/jhi-health',
      name: 'JhiHealthComponent',
      component: JhiHealthComponent,
      meta: { authorities: [Authority.ADMIN] }
    },
    {
      path: '/admin/logs',
      name: 'JhiLogsComponent',
      component: JhiLogsComponent,
      meta: { authorities: [Authority.ADMIN] }
    },
    {
      path: '/admin/jhi-metrics',
      name: 'JhiMetricsComponent',
      component: JhiMetricsComponent,
      meta: { authorities: [Authority.ADMIN] }
    },
    {
      path: '/admin/jhi-configuration',
      name: 'JhiConfigurationComponent',
      component: JhiConfigurationComponent,
      meta: { authorities: [Authority.ADMIN] }
    }
    ,
    {
      path: '/geraet-typ',
      name: 'GeraetTyp',
      component: GeraetTyp,
      meta: { authorities: [Authority.USER] }
    },
    {
      path: '/geraet-typ/new',
      name: 'GeraetTypCreate',
      component: GeraetTypUpdate,
      meta: { authorities: [Authority.USER] }
    },
    {
      path: '/geraet-typ/:geraetTypId/edit',
      name: 'GeraetTypEdit',
      component: GeraetTypUpdate,
      meta: { authorities: [Authority.USER] }
    },
    {
      path: '/geraet-typ/:geraetTypId/view',
      name: 'GeraetTypView',
      component: GeraetTypDetails,
      meta: { authorities: [Authority.USER] }
    }
    ,
    {
      path: '/hersteller',
      name: 'Hersteller',
      component: Hersteller,
      meta: { authorities: [Authority.USER] }
    },
    {
      path: '/hersteller/new',
      name: 'HerstellerCreate',
      component: HerstellerUpdate,
      meta: { authorities: [Authority.USER] }
    },
    {
      path: '/hersteller/:herstellerId/edit',
      name: 'HerstellerEdit',
      component: HerstellerUpdate,
      meta: { authorities: [Authority.USER] }
    },
    {
      path: '/hersteller/:herstellerId/view',
      name: 'HerstellerView',
      component: HerstellerDetails,
      meta: { authorities: [Authority.USER] }
    }
    ,
    {
      path: '/zubehoer-typ',
      name: 'ZubehoerTyp',
      component: ZubehoerTyp,
      meta: { authorities: [Authority.USER] }
    },
    {
      path: '/zubehoer-typ/new',
      name: 'ZubehoerTypCreate',
      component: ZubehoerTypUpdate,
      meta: { authorities: [Authority.USER] }
    },
    {
      path: '/zubehoer-typ/:zubehoerTypId/edit',
      name: 'ZubehoerTypEdit',
      component: ZubehoerTypUpdate,
      meta: { authorities: [Authority.USER] }
    },
    {
      path: '/zubehoer-typ/:zubehoerTypId/view',
      name: 'ZubehoerTypView',
      component: ZubehoerTypDetails,
      meta: { authorities: [Authority.USER] }
    }
    ,
    {
      path: '/geraet',
      name: 'Geraet',
      component: Geraet,
      meta: { authorities: [Authority.USER] }
    },
    {
      path: '/geraet/new',
      name: 'GeraetCreate',
      component: GeraetUpdate,
      meta: { authorities: [Authority.USER] }
    },
    {
      path: '/geraet/:geraetId/edit',
      name: 'GeraetEdit',
      component: GeraetUpdate,
      meta: { authorities: [Authority.USER] }
    },
    {
      path: '/geraet/:geraetId/view',
      name: 'GeraetView',
      component: GeraetDetails,
      meta: { authorities: [Authority.USER] }
    }
    ,
    {
      path: '/zubehoer',
      name: 'Zubehoer',
      component: Zubehoer,
      meta: { authorities: [Authority.USER] }
    },
    {
      path: '/zubehoer/new',
      name: 'ZubehoerCreate',
      component: ZubehoerUpdate,
      meta: { authorities: [Authority.USER] }
    },
    {
      path: '/zubehoer/:zubehoerId/edit',
      name: 'ZubehoerEdit',
      component: ZubehoerUpdate,
      meta: { authorities: [Authority.USER] }
    },
    {
      path: '/zubehoer/:zubehoerId/view',
      name: 'ZubehoerView',
      component: ZubehoerDetails,
      meta: { authorities: [Authority.USER] }
    }
    ,
    {
      path: '/systemtyp',
      name: 'Systemtyp',
      component: Systemtyp,
      meta: { authorities: [Authority.USER] }
    },
    {
      path: '/systemtyp/new',
      name: 'SystemtypCreate',
      component: SystemtypUpdate,
      meta: { authorities: [Authority.USER] }
    },
    {
      path: '/systemtyp/:systemtypId/edit',
      name: 'SystemtypEdit',
      component: SystemtypUpdate,
      meta: { authorities: [Authority.USER] }
    },
    {
      path: '/systemtyp/:systemtypId/view',
      name: 'SystemtypView',
      component: SystemtypDetails,
      meta: { authorities: [Authority.USER] }
    }
    ,
    {
      path: '/systeminstanz',
      name: 'Systeminstanz',
      component: Systeminstanz,
      meta: { authorities: [Authority.USER] }
    },
    {
      path: '/systeminstanz/new',
      name: 'SysteminstanzCreate',
      component: SysteminstanzUpdate,
      meta: { authorities: [Authority.USER] }
    },
    {
      path: '/systeminstanz/:systeminstanzId/edit',
      name: 'SysteminstanzEdit',
      component: SysteminstanzUpdate,
      meta: { authorities: [Authority.USER] }
    },
    {
      path: '/systeminstanz/:systeminstanzId/view',
      name: 'SysteminstanzView',
      component: SysteminstanzDetails,
      meta: { authorities: [Authority.USER] }
    }
    ,
    {
      path: '/arzt',
      name: 'Arzt',
      component: Arzt,
      meta: { authorities: [Authority.USER] }
    },
    {
      path: '/arzt/new',
      name: 'ArztCreate',
      component: ArztUpdate,
      meta: { authorities: [Authority.USER] }
    },
    {
      path: '/arzt/:arztId/edit',
      name: 'ArztEdit',
      component: ArztUpdate,
      meta: { authorities: [Authority.USER] }
    },
    {
      path: '/arzt/:arztId/view',
      name: 'ArztView',
      component: ArztDetails,
      meta: { authorities: [Authority.USER] }
    }
    ,
    {
      path: '/betreiber',
      name: 'Betreiber',
      component: Betreiber,
      meta: { authorities: [Authority.USER] }
    },
    {
      path: '/betreiber/new',
      name: 'BetreiberCreate',
      component: BetreiberUpdate,
      meta: { authorities: [Authority.USER] }
    },
    {
      path: '/betreiber/:betreiberId/edit',
      name: 'BetreiberEdit',
      component: BetreiberUpdate,
      meta: { authorities: [Authority.USER] }
    },
    {
      path: '/betreiber/:betreiberId/view',
      name: 'BetreiberView',
      component: BetreiberDetails,
      meta: { authorities: [Authority.USER] }
    }
    ,
    {
      path: '/betriebsstaette',
      name: 'Betriebsstaette',
      component: Betriebsstaette,
      meta: { authorities: [Authority.USER] }
    },
    {
      path: '/betriebsstaette/new',
      name: 'BetriebsstaetteCreate',
      component: BetriebsstaetteUpdate,
      meta: { authorities: [Authority.USER] }
    },
    {
      path: '/betriebsstaette/:betriebsstaetteId/edit',
      name: 'BetriebsstaetteEdit',
      component: BetriebsstaetteUpdate,
      meta: { authorities: [Authority.USER] }
    },
    {
      path: '/betriebsstaette/:betriebsstaetteId/view',
      name: 'BetriebsstaetteView',
      component: BetriebsstaetteDetails,
      meta: { authorities: [Authority.USER] }
    }
    ,
    {
      path: '/systemnutzung',
      name: 'Systemnutzung',
      component: Systemnutzung,
      meta: { authorities: [Authority.USER] }
    },
    {
      path: '/systemnutzung/new',
      name: 'SystemnutzungCreate',
      component: SystemnutzungUpdate,
      meta: { authorities: [Authority.USER] }
    },
    {
      path: '/systemnutzung/:systemnutzungId/edit',
      name: 'SystemnutzungEdit',
      component: SystemnutzungUpdate,
      meta: { authorities: [Authority.USER] }
    },
    {
      path: '/systemnutzung/:systemnutzungId/view',
      name: 'SystemnutzungView',
      component: SystemnutzungDetails,
      meta: { authorities: [Authority.USER] }
    }
    // jhipster-needle-add-entity-to-router - JHipster will add entities to the router here
  ]
});
