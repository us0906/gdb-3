import { Component, Vue, Inject } from 'vue-property-decorator';

import { numeric, required, minLength, maxLength } from 'vuelidate/lib/validators';

import SysteminstanzService from '../systeminstanz/systeminstanz.service';
import { ISysteminstanz } from '@/shared/model/systeminstanz.model';

import AlertService from '@/shared/alert/alert.service';
import { IBetreiber, Betreiber } from '@/shared/model/betreiber.model';
import BetreiberService from './betreiber.service';

const validations: any = {
  betreiber: {
    vorname: {
      required
    },
    nachname: {
      required
    },
    strasse: {},
    hausnummer: {},
    plz: {},
    ort: {}
  }
};

@Component({
  validations
})
export default class BetreiberUpdate extends Vue {
  @Inject('alertService') private alertService: () => AlertService;
  @Inject('betreiberService') private betreiberService: () => BetreiberService;
  public betreiber: IBetreiber = new Betreiber();

  @Inject('systeminstanzService') private systeminstanzService: () => SysteminstanzService;

  public systeminstanzs: ISysteminstanz[] = [];
  public isSaving = false;

  beforeRouteEnter(to, from, next) {
    next(vm => {
      if (to.params.betreiberId) {
        vm.retrieveBetreiber(to.params.betreiberId);
      }
      vm.initRelationships();
    });
  }

  public save(): void {
    this.isSaving = true;
    if (this.betreiber.id) {
      this.betreiberService()
        .update(this.betreiber)
        .then(param => {
          this.isSaving = false;
          this.$router.go(-1);
          const message = this.$t('gdb3App.betreiber.updated', { param: param.id });
          this.alertService().showAlert(message, 'info');
        });
    } else {
      this.betreiberService()
        .create(this.betreiber)
        .then(param => {
          this.isSaving = false;
          this.$router.go(-1);
          const message = this.$t('gdb3App.betreiber.created', { param: param.id });
          this.alertService().showAlert(message, 'success');
        });
    }
  }

  public retrieveBetreiber(betreiberId): void {
    this.betreiberService()
      .find(betreiberId)
      .then(res => {
        this.betreiber = res;
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
