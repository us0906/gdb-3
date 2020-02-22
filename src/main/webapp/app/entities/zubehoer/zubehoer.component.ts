import { mixins } from 'vue-class-component';

import { Component, Inject } from 'vue-property-decorator';
import Vue2Filters from 'vue2-filters';
import { IZubehoer } from '@/shared/model/zubehoer.model';
import AlertMixin from '@/shared/alert/alert.mixin';

import ZubehoerService from './zubehoer.service';

@Component
export default class Zubehoer extends mixins(Vue2Filters.mixin, AlertMixin) {
  @Inject('zubehoerService') private zubehoerService: () => ZubehoerService;
  public currentSearch = '';
  private removeId: number = null;

  public zubehoers: IZubehoer[] = [];

  public isFetching = false;

  public mounted(): void {
    this.retrieveAllZubehoers();
  }

  public search(query): void {
    if (!query) {
      return this.clear();
    }
    this.currentSearch = query;
    this.retrieveAllZubehoers();
  }

  public clear(): void {
    this.currentSearch = '';
    this.retrieveAllZubehoers();
  }

  public retrieveAllZubehoers(): void {
    this.isFetching = true;

    if (this.currentSearch) {
      this.zubehoerService()
        .search(this.currentSearch)
        .then(
          res => {
            this.zubehoers = res;
            this.isFetching = false;
          },
          err => {
            this.isFetching = false;
          }
        );
      return;
    }
    this.zubehoerService()
      .retrieve()
      .then(
        res => {
          this.zubehoers = res.data;
          this.isFetching = false;
        },
        err => {
          this.isFetching = false;
        }
      );
  }

  public prepareRemove(instance: IZubehoer): void {
    this.removeId = instance.id;
    if (<any>this.$refs.removeEntity) {
      (<any>this.$refs.removeEntity).show();
    }
  }

  public removeZubehoer(): void {
    this.zubehoerService()
      .delete(this.removeId)
      .then(() => {
        const message = this.$t('gdb3App.zubehoer.deleted', { param: this.removeId });
        this.alertService().showAlert(message, 'danger');
        this.getAlertFromStore();
        this.removeId = null;
        this.retrieveAllZubehoers();
        this.closeDialog();
      });
  }

  public closeDialog(): void {
    (<any>this.$refs.removeEntity).hide();
  }
}
