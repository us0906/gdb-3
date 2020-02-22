import { Component, Vue, Inject } from 'vue-property-decorator';

import { numeric, required, minLength, maxLength, minValue, maxValue } from 'vuelidate/lib/validators';

import SystemtypService from '../systemtyp/systemtyp.service';
import { ISystemtyp } from '@/shared/model/systemtyp.model';

import GeraetTypService from '../geraet-typ/geraet-typ.service';
import { IGeraetTyp } from '@/shared/model/geraet-typ.model';

import HerstellerService from '../hersteller/hersteller.service';
import { IHersteller } from '@/shared/model/hersteller.model';

import AlertService from '@/shared/alert/alert.service';
import { IGeraet, Geraet } from '@/shared/model/geraet.model';
import GeraetService from './geraet.service';

const validations: any = {
  geraet: {
    bezeichnung: {
      required,
      minLength: minLength(1),
      maxLength: maxLength(200)
    },
    gueltigBis: {},
    geraetTypId: {
      required
    },
    herstellerId: {
      required
    }
  }
};

@Component({
  validations
})
export default class GeraetUpdate extends Vue {
  @Inject('alertService') private alertService: () => AlertService;
  @Inject('geraetService') private geraetService: () => GeraetService;
  public geraet: IGeraet = new Geraet();

  @Inject('systemtypService') private systemtypService: () => SystemtypService;

  public systemtyps: ISystemtyp[] = [];

  @Inject('geraetTypService') private geraetTypService: () => GeraetTypService;

  public geraetTyps: IGeraetTyp[] = [];

  @Inject('herstellerService') private herstellerService: () => HerstellerService;

  public herstellers: IHersteller[] = [];
  public isSaving = false;

  beforeRouteEnter(to, from, next) {
    next(vm => {
      if (to.params.geraetId) {
        vm.retrieveGeraet(to.params.geraetId);
      }
      vm.initRelationships();
    });
  }

  public save(): void {
    this.isSaving = true;
    if (this.geraet.id) {
      this.geraetService()
        .update(this.geraet)
        .then(param => {
          this.isSaving = false;
          this.$router.go(-1);
          const message = this.$t('gdb3App.geraet.updated', { param: param.id });
          this.alertService().showAlert(message, 'info');
        });
    } else {
      this.geraetService()
        .create(this.geraet)
        .then(param => {
          this.isSaving = false;
          this.$router.go(-1);
          const message = this.$t('gdb3App.geraet.created', { param: param.id });
          this.alertService().showAlert(message, 'success');
        });
    }
  }

  public retrieveGeraet(geraetId): void {
    this.geraetService()
      .find(geraetId)
      .then(res => {
        this.geraet = res;
      });
  }

  public previousState(): void {
    this.$router.go(-1);
  }

  public initRelationships(): void {
    this.systemtypService()
      .retrieve()
      .then(res => {
        this.systemtyps = res.data;
      });
    this.geraetTypService()
      .retrieve()
      .then(res => {
        this.geraetTyps = res.data;
      });
    this.herstellerService()
      .retrieve()
      .then(res => {
        this.herstellers = res.data;
      });
  }
}
