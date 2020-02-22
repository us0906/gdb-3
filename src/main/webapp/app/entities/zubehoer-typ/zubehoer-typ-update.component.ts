import { Component, Vue, Inject } from 'vue-property-decorator';

import { numeric, required, minLength, maxLength, minValue, maxValue } from 'vuelidate/lib/validators';

import ZubehoerService from '../zubehoer/zubehoer.service';
import { IZubehoer } from '@/shared/model/zubehoer.model';

import AlertService from '@/shared/alert/alert.service';
import { IZubehoerTyp, ZubehoerTyp } from '@/shared/model/zubehoer-typ.model';
import ZubehoerTypService from './zubehoer-typ.service';

const validations: any = {
  zubehoerTyp: {
    bezeichnung: {
      required,
      minLength: minLength(1),
      maxLength: maxLength(200)
    },
    gueltigBis: {},
    technologie: {
      required
    }
  }
};

@Component({
  validations
})
export default class ZubehoerTypUpdate extends Vue {
  @Inject('alertService') private alertService: () => AlertService;
  @Inject('zubehoerTypService') private zubehoerTypService: () => ZubehoerTypService;
  public zubehoerTyp: IZubehoerTyp = new ZubehoerTyp();

  @Inject('zubehoerService') private zubehoerService: () => ZubehoerService;

  public zubehoers: IZubehoer[] = [];
  public isSaving = false;

  beforeRouteEnter(to, from, next) {
    next(vm => {
      if (to.params.zubehoerTypId) {
        vm.retrieveZubehoerTyp(to.params.zubehoerTypId);
      }
      vm.initRelationships();
    });
  }

  public save(): void {
    this.isSaving = true;
    if (this.zubehoerTyp.id) {
      this.zubehoerTypService()
        .update(this.zubehoerTyp)
        .then(param => {
          this.isSaving = false;
          this.$router.go(-1);
          const message = this.$t('gdb3App.zubehoerTyp.updated', { param: param.id });
          this.alertService().showAlert(message, 'info');
        });
    } else {
      this.zubehoerTypService()
        .create(this.zubehoerTyp)
        .then(param => {
          this.isSaving = false;
          this.$router.go(-1);
          const message = this.$t('gdb3App.zubehoerTyp.created', { param: param.id });
          this.alertService().showAlert(message, 'success');
        });
    }
  }

  public retrieveZubehoerTyp(zubehoerTypId): void {
    this.zubehoerTypService()
      .find(zubehoerTypId)
      .then(res => {
        this.zubehoerTyp = res;
      });
  }

  public previousState(): void {
    this.$router.go(-1);
  }

  public initRelationships(): void {
    this.zubehoerService()
      .retrieve()
      .then(res => {
        this.zubehoers = res.data;
      });
  }
}
