import { mixins } from 'vue-class-component';

import { Component, Inject } from 'vue-property-decorator';
import Vue2Filters from 'vue2-filters';
import { IArzt } from '@/shared/model/arzt.model';
import AlertMixin from '@/shared/alert/alert.mixin';

import ArztService from './arzt.service';

@Component
export default class Arzt extends mixins(Vue2Filters.mixin, AlertMixin) {
  @Inject('arztService') private arztService: () => ArztService;
  public currentSearch = '';
  private removeId: number = null;

  public arzts: IArzt[] = [];

  public isFetching = false;

  public mounted(): void {
    this.retrieveAllArzts();
  }

  public search(query): void {
    if (!query) {
      return this.clear();
    }
    this.currentSearch = query;
    this.retrieveAllArzts();
  }

  public clear(): void {
    this.currentSearch = '';
    this.retrieveAllArzts();
  }

  public retrieveAllArzts(): void {
    this.isFetching = true;

    if (this.currentSearch) {
      this.arztService()
        .search(this.currentSearch)
        .then(
          res => {
            this.arzts = res;
            this.isFetching = false;
          },
          err => {
            this.isFetching = false;
          }
        );
      return;
    }
    this.arztService()
      .retrieve()
      .then(
        res => {
          this.arzts = res.data;
          this.isFetching = false;
        },
        err => {
          this.isFetching = false;
        }
      );
  }

  public prepareRemove(instance: IArzt): void {
    this.removeId = instance.id;
    if (<any>this.$refs.removeEntity) {
      (<any>this.$refs.removeEntity).show();
    }
  }

  public removeArzt(): void {
    this.arztService()
      .delete(this.removeId)
      .then(() => {
        const message = this.$t('gdb3App.arzt.deleted', { param: this.removeId });
        this.alertService().showAlert(message, 'danger');
        this.getAlertFromStore();
        this.removeId = null;
        this.retrieveAllArzts();
        this.closeDialog();
      });
  }

  public closeDialog(): void {
    (<any>this.$refs.removeEntity).hide();
  }
}
