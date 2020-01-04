import { Component, Inject } from 'vue-property-decorator';

import { mixins } from 'vue-class-component';
import JhiDataUtils from '@/shared/data/data-utils.service';

import { numeric, required, minLength, maxLength } from 'vuelidate/lib/validators';

import SystemnutzungService from '../systemnutzung/systemnutzung.service';
import { ISystemnutzung } from '@/shared/model/systemnutzung.model';

import SystemtypService from '../systemtyp/systemtyp.service';
import { ISystemtyp } from '@/shared/model/systemtyp.model';

import BetriebsstaetteService from '../betriebsstaette/betriebsstaette.service';
import { IBetriebsstaette } from '@/shared/model/betriebsstaette.model';

import BetreiberService from '../betreiber/betreiber.service';
import { IBetreiber } from '@/shared/model/betreiber.model';

import AlertService from '@/shared/alert/alert.service';
import { ISysteminstanz, Systeminstanz } from '@/shared/model/systeminstanz.model';
import SysteminstanzService from './systeminstanz.service';

const validations: any = {
  systeminstanz: {
    bezeichnung: {
      required,
      minLength: minLength(1),
      maxLength: maxLength(200)
    },
    geraetNummer: {
      required,
      minLength: minLength(1),
      maxLength: maxLength(200)
    },
    geraetBaujahr: {
      required,
      minLength: minLength(4),
      maxLength: maxLength(4)
    },
    gueltigBis: {},
    gwe: {},
    bemerkung: {},
    systemtypId: {
      required
    },
    betriebsstaetteId: {
      required
    },
    betreiberId: {
      required
    }
  }
};

@Component({
  validations
})
export default class SysteminstanzUpdate extends mixins(JhiDataUtils) {
  @Inject('alertService') private alertService: () => AlertService;
  @Inject('systeminstanzService') private systeminstanzService: () => SysteminstanzService;
  public systeminstanz: ISysteminstanz = new Systeminstanz();

  @Inject('systemnutzungService') private systemnutzungService: () => SystemnutzungService;

  public systemnutzungs: ISystemnutzung[] = [];

  @Inject('systemtypService') private systemtypService: () => SystemtypService;

  public systemtyps: ISystemtyp[] = [];

  @Inject('betriebsstaetteService') private betriebsstaetteService: () => BetriebsstaetteService;

  public betriebsstaettes: IBetriebsstaette[] = [];

  @Inject('betreiberService') private betreiberService: () => BetreiberService;

  public betreibers: IBetreiber[] = [];
  public isSaving = false;

  beforeRouteEnter(to, from, next) {
    next(vm => {
      if (to.params.systeminstanzId) {
        vm.retrieveSysteminstanz(to.params.systeminstanzId);
      }
      vm.initRelationships();
    });
  }

  public save(): void {
    this.isSaving = true;
    if (this.systeminstanz.id) {
      this.systeminstanzService()
        .update(this.systeminstanz)
        .then(param => {
          this.isSaving = false;
          this.$router.go(-1);
          const message = this.$t('gdb3App.systeminstanz.updated', { param: param.id });
          this.alertService().showAlert(message, 'info');
        });
    } else {
      this.systeminstanzService()
        .create(this.systeminstanz)
        .then(param => {
          this.isSaving = false;
          this.$router.go(-1);
          const message = this.$t('gdb3App.systeminstanz.created', { param: param.id });
          this.alertService().showAlert(message, 'success');
        });
    }
  }

  public retrieveSysteminstanz(systeminstanzId): void {
    this.systeminstanzService()
      .find(systeminstanzId)
      .then(res => {
        this.systeminstanz = res;
      });
  }

  public previousState(): void {
    this.$router.go(-1);
  }

  public clearInputImage(field, fieldContentType, idInput): void {
    if (this.systeminstanz && field && fieldContentType) {
      if (this.systeminstanz.hasOwnProperty(field)) {
        this.systeminstanz[field] = null;
      }
      if (this.systeminstanz.hasOwnProperty(fieldContentType)) {
        this.systeminstanz[fieldContentType] = null;
      }
      if (idInput) {
        (<any>this).$refs[idInput] = null;
      }
    }
  }

  public initRelationships(): void {
    this.systemnutzungService()
      .retrieve()
      .then(res => {
        this.systemnutzungs = res.data;
      });
    this.systemtypService()
      .retrieve()
      .then(res => {
        this.systemtyps = res.data;
      });
    this.betriebsstaetteService()
      .retrieve()
      .then(res => {
        this.betriebsstaettes = res.data;
      });
    this.betreiberService()
      .retrieve()
      .then(res => {
        this.betreibers = res.data;
      });
  }
}
