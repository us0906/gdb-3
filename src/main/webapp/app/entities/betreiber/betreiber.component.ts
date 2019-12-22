import { mixins } from 'vue-class-component';

import { Component, Inject } from 'vue-property-decorator';
import Vue2Filters from 'vue2-filters';
import { IBetreiber } from '@/shared/model/betreiber.model';
import AlertMixin from '@/shared/alert/alert.mixin';

import BetreiberService from './betreiber.service';

@Component
export default class Betreiber extends mixins(Vue2Filters.mixin, AlertMixin) {
  @Inject('betreiberService') private betreiberService: () => BetreiberService;
  public currentSearch = '';
  private removeId: number = null;
  public betreibers: IBetreiber[] = [];

  public isFetching = false;

  public mounted(): void {
    this.retrieveAllBetreibers();
  }

  public search(query): void {
    if (!query) {
      return this.clear();
    }
    this.currentSearch = query;
    this.retrieveAllBetreibers();
  }

  public clear(): void {
    this.currentSearch = '';
    this.retrieveAllBetreibers();
  }

  public retrieveAllBetreibers(): void {
    this.isFetching = true;

    if (this.currentSearch) {
      this.betreiberService()
        .search(this.currentSearch)
        .then(
          res => {
            this.betreibers = res;
            this.isFetching = false;
          },
          err => {
            this.isFetching = false;
          }
        );
      return;
    }
    this.betreiberService()
      .retrieve()
      .then(
        res => {
          this.betreibers = res.data;
          this.isFetching = false;
        },
        err => {
          this.isFetching = false;
        }
      );
  }

  public prepareRemove(instance: IBetreiber): void {
    this.removeId = instance.id;
  }

  public removeBetreiber(): void {
    this.betreiberService()
      .delete(this.removeId)
      .then(() => {
        const message = this.$t('gdb3App.betreiber.deleted', { param: this.removeId });
        this.alertService().showAlert(message, 'danger');
        this.getAlertFromStore();

        this.removeId = null;
        this.retrieveAllBetreibers();
        this.closeDialog();
      });
  }

  public closeDialog(): void {
    (<any>this.$refs.removeEntity).hide();
  }
}
