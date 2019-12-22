import { Component, Vue, Inject } from 'vue-property-decorator';

import { numeric, required, minLength, maxLength } from 'vuelidate/lib/validators';

import GeraetService from '../geraet/geraet.service';
import { IGeraet } from '@/shared/model/geraet.model';

import ZubehoerService from '../zubehoer/zubehoer.service';
import { IZubehoer } from '@/shared/model/zubehoer.model';

import AlertService from '@/shared/alert/alert.service';
import { IHersteller, Hersteller } from '@/shared/model/hersteller.model';
import HerstellerService from './hersteller.service';

const validations: any = {
  hersteller: {
    bezeichnung: {
      required,
      minLength: minLength(1),
      maxLength: maxLength(200)
    },
    gueltigBis: {}
  }
};

@Component({
  validations
})
export default class HerstellerUpdate extends Vue {
  @Inject('alertService') private alertService: () => AlertService;
  @Inject('herstellerService') private herstellerService: () => HerstellerService;
  public hersteller: IHersteller = new Hersteller();

  @Inject('geraetService') private geraetService: () => GeraetService;

  public geraets: IGeraet[] = [];

  @Inject('zubehoerService') private zubehoerService: () => ZubehoerService;

  public zubehoers: IZubehoer[] = [];
  public isSaving = false;

  beforeRouteEnter(to, from, next) {
    next(vm => {
      if (to.params.herstellerId) {
        vm.retrieveHersteller(to.params.herstellerId);
      }
      vm.initRelationships();
    });
  }

  public save(): void {
    this.isSaving = true;
    if (this.hersteller.id) {
      this.herstellerService()
        .update(this.hersteller)
        .then(param => {
          this.isSaving = false;
          this.$router.go(-1);
          const message = this.$t('gdb3App.hersteller.updated', { param: param.id });
          this.alertService().showAlert(message, 'info');
        });
    } else {
      this.herstellerService()
        .create(this.hersteller)
        .then(param => {
          this.isSaving = false;
          this.$router.go(-1);
          const message = this.$t('gdb3App.hersteller.created', { param: param.id });
          this.alertService().showAlert(message, 'success');
        });
    }
  }

  public retrieveHersteller(herstellerId): void {
    this.herstellerService()
      .find(herstellerId)
      .then(res => {
        this.hersteller = res;
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
    this.zubehoerService()
      .retrieve()
      .then(res => {
        this.zubehoers = res.data;
      });
  }
}
