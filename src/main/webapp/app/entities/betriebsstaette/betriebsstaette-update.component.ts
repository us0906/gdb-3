import { Component, Vue, Inject } from 'vue-property-decorator';

import { numeric, required, minLength, maxLength } from 'vuelidate/lib/validators';

import SysteminstanzService from '../systeminstanz/systeminstanz.service';
import { ISysteminstanz } from '@/shared/model/systeminstanz.model';

import AlertService from '@/shared/alert/alert.service';
import { IBetriebsstaette, Betriebsstaette } from '@/shared/model/betriebsstaette.model';
import BetriebsstaetteService from './betriebsstaette.service';

const validations: any = {
  betriebsstaette: {
    bsnr: {},
    strasse: {},
    hausnummer: {},
    plz: {},
    ort: {},
    bezeichnung: {}
  }
};

@Component({
  validations
})
export default class BetriebsstaetteUpdate extends Vue {
  @Inject('alertService') private alertService: () => AlertService;
  @Inject('betriebsstaetteService') private betriebsstaetteService: () => BetriebsstaetteService;
  public betriebsstaette: IBetriebsstaette = new Betriebsstaette();

  @Inject('systeminstanzService') private systeminstanzService: () => SysteminstanzService;

  public systeminstanzs: ISysteminstanz[] = [];
  public isSaving = false;

  beforeRouteEnter(to, from, next) {
    next(vm => {
      if (to.params.betriebsstaetteId) {
        vm.retrieveBetriebsstaette(to.params.betriebsstaetteId);
      }
      vm.initRelationships();
    });
  }

  public save(): void {
    this.isSaving = true;
    if (this.betriebsstaette.id) {
      this.betriebsstaetteService()
        .update(this.betriebsstaette)
        .then(param => {
          this.isSaving = false;
          this.$router.go(-1);
          const message = this.$t('gdb3App.betriebsstaette.updated', { param: param.id });
          this.alertService().showAlert(message, 'info');
        });
    } else {
      this.betriebsstaetteService()
        .create(this.betriebsstaette)
        .then(param => {
          this.isSaving = false;
          this.$router.go(-1);
          const message = this.$t('gdb3App.betriebsstaette.created', { param: param.id });
          this.alertService().showAlert(message, 'success');
        });
    }
  }

  public retrieveBetriebsstaette(betriebsstaetteId): void {
    this.betriebsstaetteService()
      .find(betriebsstaetteId)
      .then(res => {
        this.betriebsstaette = res;
      });
  }

  public previousState(): void {
    this.$router.go(-1);
  }

  public initRelationships(): void {
    this.systeminstanzService()
      .retrieve()
      .then(res => {
        this.systeminstanzs = res.data;
      });
  }
}
