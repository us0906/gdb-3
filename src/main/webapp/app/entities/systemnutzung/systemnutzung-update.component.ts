import { Component, Vue, Inject } from 'vue-property-decorator';

import { numeric, required, minLength, maxLength } from 'vuelidate/lib/validators';

import SysteminstanzService from '../systeminstanz/systeminstanz.service';
import { ISysteminstanz } from '@/shared/model/systeminstanz.model';

import ArztService from '../arzt/arzt.service';
import { IArzt } from '@/shared/model/arzt.model';

import AlertService from '@/shared/alert/alert.service';
import { ISystemnutzung, Systemnutzung } from '@/shared/model/systemnutzung.model';
import SystemnutzungService from './systemnutzung.service';

const validations: any = {
  systemnutzung: {
    systeminstanzId: {
      required
    },
    arztId: {
      required
    }
  }
};

@Component({
  validations
})
export default class SystemnutzungUpdate extends Vue {
  @Inject('alertService') private alertService: () => AlertService;
  @Inject('systemnutzungService') private systemnutzungService: () => SystemnutzungService;
  public systemnutzung: ISystemnutzung = new Systemnutzung();

  @Inject('systeminstanzService') private systeminstanzService: () => SysteminstanzService;

  public systeminstanzs: ISysteminstanz[] = [];

  @Inject('arztService') private arztService: () => ArztService;

  public arzts: IArzt[] = [];
  public isSaving = false;

  beforeRouteEnter(to, from, next) {
    next(vm => {
      if (to.params.systemnutzungId) {
        vm.retrieveSystemnutzung(to.params.systemnutzungId);
      }
      vm.initRelationships();
    });
  }

  public save(): void {
    this.isSaving = true;
    if (this.systemnutzung.id) {
      this.systemnutzungService()
        .update(this.systemnutzung)
        .then(param => {
          this.isSaving = false;
          this.$router.go(-1);
          const message = this.$t('gdb3App.systemnutzung.updated', { param: param.id });
          this.alertService().showAlert(message, 'info');
        });
    } else {
      this.systemnutzungService()
        .create(this.systemnutzung)
        .then(param => {
          this.isSaving = false;
          this.$router.go(-1);
          const message = this.$t('gdb3App.systemnutzung.created', { param: param.id });
          this.alertService().showAlert(message, 'success');
        });
    }
  }

  public retrieveSystemnutzung(systemnutzungId): void {
    this.systemnutzungService()
      .find(systemnutzungId)
      .then(res => {
        this.systemnutzung = res;
      });
  }

  public previousState(): void {
    this.$router.go(-1);
  }

  public initRelationships(): void {
    this.systeminstanzService()
      .retrieve()
      .then(res => {
        this.systeminstanzs = res.data.sort((n1, n2) => n1.bezeichnung > n2.bezeichnung);
      });
    this.arztService()
      .retrieve()
      .then(res => {
        this.arzts = res.data.sort((n1, n2) => n1.bezeichnung > n2.bezeichnung);
      });
  }
}
