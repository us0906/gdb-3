import { Component, Vue, Inject } from 'vue-property-decorator';

import { numeric, required, minLength, maxLength } from 'vuelidate/lib/validators';

import SystemtypService from '../systemtyp/systemtyp.service';
import { ISystemtyp } from '@/shared/model/systemtyp.model';

import HerstellerService from '../hersteller/hersteller.service';
import { IHersteller } from '@/shared/model/hersteller.model';

import ZubehoerTypService from '../zubehoer-typ/zubehoer-typ.service';
import { IZubehoerTyp } from '@/shared/model/zubehoer-typ.model';

import AlertService from '@/shared/alert/alert.service';
import { IZubehoer, Zubehoer } from '@/shared/model/zubehoer.model';
import ZubehoerService from './zubehoer.service';

const validations: any = {
  zubehoer: {
    bezeichnung: {
      required,
      minLength: minLength(1),
      maxLength: maxLength(200)
    },
    gueltigBis: {},
    herstellerId: {
      required
    },
    zubehoerTypId: {
      required
    }
  }
};

@Component({
  validations
})
export default class ZubehoerUpdate extends Vue {
  @Inject('alertService') private alertService: () => AlertService;
  @Inject('zubehoerService') private zubehoerService: () => ZubehoerService;
  public zubehoer: IZubehoer = new Zubehoer();

  @Inject('systemtypService') private systemtypService: () => SystemtypService;

  public systemtyps: ISystemtyp[] = [];

  @Inject('herstellerService') private herstellerService: () => HerstellerService;

  public herstellers: IHersteller[] = [];

  @Inject('zubehoerTypService') private zubehoerTypService: () => ZubehoerTypService;

  public zubehoerTyps: IZubehoerTyp[] = [];
  public isSaving = false;

  beforeRouteEnter(to, from, next) {
    next(vm => {
      if (to.params.zubehoerId) {
        vm.retrieveZubehoer(to.params.zubehoerId);
      }
      vm.initRelationships();
    });
  }

  public save(): void {
    this.isSaving = true;
    if (this.zubehoer.id) {
      this.zubehoerService()
        .update(this.zubehoer)
        .then(param => {
          this.isSaving = false;
          this.$router.go(-1);
          const message = this.$t('gdb3App.zubehoer.updated', { param: param.id });
          this.alertService().showAlert(message, 'info');
        });
    } else {
      this.zubehoerService()
        .create(this.zubehoer)
        .then(param => {
          this.isSaving = false;
          this.$router.go(-1);
          const message = this.$t('gdb3App.zubehoer.created', { param: param.id });
          this.alertService().showAlert(message, 'success');
        });
    }
  }

  public retrieveZubehoer(zubehoerId): void {
    this.zubehoerService()
      .find(zubehoerId)
      .then(res => {
        this.zubehoer = res;
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
    this.herstellerService()
      .retrieve()
      .then(res => {
        this.herstellers = res.data.sort((n1, n2) => n1.bezeichnung > n2.bezeichnung);
      });
    this.zubehoerTypService()
      .retrieve()
      .then(res => {
        this.zubehoerTyps = res.data;
      });
  }
}
