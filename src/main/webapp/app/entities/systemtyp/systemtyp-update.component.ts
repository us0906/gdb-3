import { Component, Vue, Inject } from 'vue-property-decorator';

import { numeric, required, minLength, maxLength } from 'vuelidate/lib/validators';

import SysteminstanzService from '../systeminstanz/systeminstanz.service';
import { ISysteminstanz } from '@/shared/model/systeminstanz.model';

import GeraetService from '../geraet/geraet.service';
import { IGeraet } from '@/shared/model/geraet.model';

import ZubehoerService from '../zubehoer/zubehoer.service';
import { IZubehoer } from '@/shared/model/zubehoer.model';

import AlertService from '@/shared/alert/alert.service';
import { ISystemtyp, Systemtyp } from '@/shared/model/systemtyp.model';
import SystemtypService from './systemtyp.service';

const validations: any = {
  systemtyp: {
    bezeichnung: {
      required,
      minLength: minLength(1),
      maxLength: maxLength(200)
    },
    gueltigBis: {},
    geraetId: {
      required
    }
  }
};

@Component({
  validations
})
export default class SystemtypUpdate extends Vue {
  @Inject('alertService') private alertService: () => AlertService;
  @Inject('systemtypService') private systemtypService: () => SystemtypService;
  public systemtyp: ISystemtyp = new Systemtyp();

  @Inject('systeminstanzService') private systeminstanzService: () => SysteminstanzService;

  public systeminstanzs: ISysteminstanz[] = [];

  @Inject('geraetService') private geraetService: () => GeraetService;

  public geraets: IGeraet[] = [];

  @Inject('zubehoerService') private zubehoerService: () => ZubehoerService;

  public zubehoers: IZubehoer[] = [];
  public isSaving = false;

  beforeRouteEnter(to, from, next) {
    next(vm => {
      if (to.params.systemtypId) {
        vm.retrieveSystemtyp(to.params.systemtypId);
      }
      vm.initRelationships();
    });
  }

  public save(): void {
    this.isSaving = true;
    if (this.systemtyp.id) {
      this.systemtypService()
        .update(this.systemtyp)
        .then(param => {
          this.isSaving = false;
          this.$router.go(-1);
          const message = this.$t('gdb3App.systemtyp.updated', { param: param.id });
          this.alertService().showAlert(message, 'info');
        });
    } else {
      this.systemtypService()
        .create(this.systemtyp)
        .then(param => {
          this.isSaving = false;
          this.$router.go(-1);
          const message = this.$t('gdb3App.systemtyp.created', { param: param.id });
          this.alertService().showAlert(message, 'success');
        });
    }
  }

  public retrieveSystemtyp(systemtypId): void {
    this.systemtypService()
      .find(systemtypId)
      .then(res => {
        this.systemtyp = res;
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
    this.geraetService()
      .retrieve()
      .then(res => {
        this.geraets = res.data.sort(
          (n1, n2) => n1.herstellerBezeichnung + ' - ' + n1.bezeichnung > n2.herstellerBezeichnung + ' - ' + n2.bezeichnung
        );
      });
    this.zubehoerService()
      .retrieve()
      .then(res => {
        this.zubehoers = res.data.sort(
          (n1, n2) => n1.herstellerBezeichnung + ' - ' + n1.bezeichnung > n2.herstellerBezeichnung + ' - ' + n2.bezeichnung
        );
      });
  }
}
