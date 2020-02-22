import { mixins } from 'vue-class-component';

import { Component, Inject } from 'vue-property-decorator';
import Vue2Filters from 'vue2-filters';
import { IBetriebsstaette } from '@/shared/model/betriebsstaette.model';
import AlertMixin from '@/shared/alert/alert.mixin';

import BetriebsstaetteService from './betriebsstaette.service';

@Component
export default class Betriebsstaette extends mixins(Vue2Filters.mixin, AlertMixin) {
  @Inject('betriebsstaetteService') private betriebsstaetteService: () => BetriebsstaetteService;
  public currentSearch = '';
  private removeId: number = null;

  public betriebsstaettes: IBetriebsstaette[] = [];

  public isFetching = false;

  public mounted(): void {
    this.retrieveAllBetriebsstaettes();
  }

  public search(query): void {
    if (!query) {
      return this.clear();
    }
    this.currentSearch = query;
    this.retrieveAllBetriebsstaettes();
  }

  public clear(): void {
    this.currentSearch = '';
    this.retrieveAllBetriebsstaettes();
  }

  public retrieveAllBetriebsstaettes(): void {
    this.isFetching = true;

    if (this.currentSearch) {
      this.betriebsstaetteService()
        .search(this.currentSearch)
        .then(
          res => {
            this.betriebsstaettes = res;
            this.isFetching = false;
          },
          err => {
            this.isFetching = false;
          }
        );
      return;
    }
    this.betriebsstaetteService()
      .retrieve()
      .then(
        res => {
          this.betriebsstaettes = res.data;
          this.isFetching = false;
        },
        err => {
          this.isFetching = false;
        }
      );
  }

  public prepareRemove(instance: IBetriebsstaette): void {
    this.removeId = instance.id;
    if (<any>this.$refs.removeEntity) {
      (<any>this.$refs.removeEntity).show();
    }
  }

  public removeBetriebsstaette(): void {
    this.betriebsstaetteService()
      .delete(this.removeId)
      .then(() => {
        const message = this.$t('gdb3App.betriebsstaette.deleted', { param: this.removeId });
        this.alertService().showAlert(message, 'danger');
        this.getAlertFromStore();
        this.removeId = null;
        this.retrieveAllBetriebsstaettes();
        this.closeDialog();
      });
  }

  public closeDialog(): void {
    (<any>this.$refs.removeEntity).hide();
  }
}
