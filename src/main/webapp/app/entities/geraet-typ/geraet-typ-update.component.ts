import { Component, Vue, Inject } from 'vue-property-decorator';

import { numeric, required, minLength, maxLength, minValue, maxValue } from 'vuelidate/lib/validators';

import GeraetService from '../geraet/geraet.service';
import { IGeraet } from '@/shared/model/geraet.model';

import AlertService from '@/shared/alert/alert.service';
import { IGeraetTyp, GeraetTyp } from '@/shared/model/geraet-typ.model';
import GeraetTypService from './geraet-typ.service';

const validations: any = {
  geraetTyp: {
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
export default class GeraetTypUpdate extends Vue {
  @Inject('alertService') private alertService: () => AlertService;
  @Inject('geraetTypService') private geraetTypService: () => GeraetTypService;
  public geraetTyp: IGeraetTyp = new GeraetTyp();

  @Inject('geraetService') private geraetService: () => GeraetService;

  public geraets: IGeraet[] = [];
  public isSaving = false;

  beforeRouteEnter(to, from, next) {
    next(vm => {
      if (to.params.geraetTypId) {
        vm.retrieveGeraetTyp(to.params.geraetTypId);
      }
      vm.initRelationships();
    });
  }

  public save(): void {
    this.isSaving = true;
    if (this.geraetTyp.id) {
      this.geraetTypService()
        .update(this.geraetTyp)
        .then(param => {
          this.isSaving = false;
          this.$router.go(-1);
          const message = this.$t('gdb3App.geraetTyp.updated', { param: param.id });
          this.alertService().showAlert(message, 'info');
        });
    } else {
      this.geraetTypService()
        .create(this.geraetTyp)
        .then(param => {
          this.isSaving = false;
          this.$router.go(-1);
          const message = this.$t('gdb3App.geraetTyp.created', { param: param.id });
          this.alertService().showAlert(message, 'success');
        });
    }
  }

  public retrieveGeraetTyp(geraetTypId): void {
    this.geraetTypService()
      .find(geraetTypId)
      .then(res => {
        this.geraetTyp = res;
      });
  }

  public previousState(): void {
    this.$router.go(-1);
  }

  public initRelationships(): void {
    this.geraetService()
      .retrieve()
      .then(res => {
        this.geraets = res.data;
      });
  }
}
