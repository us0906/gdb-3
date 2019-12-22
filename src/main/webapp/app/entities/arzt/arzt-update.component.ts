import { Component, Vue, Inject } from 'vue-property-decorator';

import { numeric, required, minLength, maxLength } from 'vuelidate/lib/validators';

import SystemnutzungService from '../systemnutzung/systemnutzung.service';
import { ISystemnutzung } from '@/shared/model/systemnutzung.model';

import AlertService from '@/shared/alert/alert.service';
import { IArzt, Arzt } from '@/shared/model/arzt.model';
import ArztService from './arzt.service';

const validations: any = {
  arzt: {
    lanr: {
      required,
      minLength: minLength(7),
      maxLength: maxLength(7)
    },
    titel: {},
    vorname: {
      required
    },
    nachname: {
      required
    }
  }
};

@Component({
  validations
})
export default class ArztUpdate extends Vue {
  @Inject('alertService') private alertService: () => AlertService;
  @Inject('arztService') private arztService: () => ArztService;
  public arzt: IArzt = new Arzt();

  @Inject('systemnutzungService') private systemnutzungService: () => SystemnutzungService;

  public systemnutzungs: ISystemnutzung[] = [];
  public isSaving = false;

  beforeRouteEnter(to, from, next) {
    next(vm => {
      if (to.params.arztId) {
        vm.retrieveArzt(to.params.arztId);
      }
      vm.initRelationships();
    });
  }

  public save(): void {
    this.isSaving = true;
    if (this.arzt.id) {
      this.arztService()
        .update(this.arzt)
        .then(param => {
          this.isSaving = false;
          this.$router.go(-1);
          const message = this.$t('gdb3App.arzt.updated', { param: param.id });
          this.alertService().showAlert(message, 'info');
        });
    } else {
      this.arztService()
        .create(this.arzt)
        .then(param => {
          this.isSaving = false;
          this.$router.go(-1);
          const message = this.$t('gdb3App.arzt.created', { param: param.id });
          this.alertService().showAlert(message, 'success');
        });
    }
  }

  public retrieveArzt(arztId): void {
    this.arztService()
      .find(arztId)
      .then(res => {
        this.arzt = res;
      });
  }

  public previousState(): void {
    this.$router.go(-1);
  }

  public initRelationships(): void {
    this.systemnutzungService()
      .retrieve()
      .then(res => {
        this.systemnutzungs = res.data;
      });
  }
}
