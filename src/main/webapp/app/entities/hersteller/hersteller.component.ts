import { mixins } from 'vue-class-component';

import { Component, Inject } from 'vue-property-decorator';
import Vue2Filters from 'vue2-filters';
import { IHersteller } from '@/shared/model/hersteller.model';
import AlertMixin from '@/shared/alert/alert.mixin';

import HerstellerService from './hersteller.service';

@Component
export default class Hersteller extends mixins(Vue2Filters.mixin, AlertMixin) {
  @Inject('herstellerService') private herstellerService: () => HerstellerService;
  public currentSearch = '';
  private removeId: number = null;

  public herstellers: IHersteller[] = [];

  public isFetching = false;

  public mounted(): void {
    this.retrieveAllHerstellers();
  }

  public search(query): void {
    if (!query) {
      return this.clear();
    }
    this.currentSearch = query;
    this.retrieveAllHerstellers();
  }

  public clear(): void {
    this.currentSearch = '';
    this.retrieveAllHerstellers();
  }

  public retrieveAllHerstellers(): void {
    this.isFetching = true;

    if (this.currentSearch) {
      this.herstellerService()
        .search(this.currentSearch)
        .then(
          res => {
            this.herstellers = res;
            this.isFetching = false;
          },
          err => {
            this.isFetching = false;
          }
        );
      return;
    }
    this.herstellerService()
      .retrieve()
      .then(
        res => {
          this.herstellers = res.data;
          this.isFetching = false;
        },
        err => {
          this.isFetching = false;
        }
      );
  }

  public prepareRemove(instance: IHersteller): void {
    this.removeId = instance.id;
    if (<any>this.$refs.removeEntity) {
      (<any>this.$refs.removeEntity).show();
    }
  }

  public removeHersteller(): void {
    this.herstellerService()
      .delete(this.removeId)
      .then(() => {
        const message = this.$t('gdb3App.hersteller.deleted', { param: this.removeId });
        this.alertService().showAlert(message, 'danger');
        this.getAlertFromStore();
        this.removeId = null;
        this.retrieveAllHerstellers();
        this.closeDialog();
      });
  }

  public closeDialog(): void {
    (<any>this.$refs.removeEntity).hide();
  }
}
