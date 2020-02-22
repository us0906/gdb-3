import { mixins } from 'vue-class-component';

import { Component, Inject } from 'vue-property-decorator';
import Vue2Filters from 'vue2-filters';
import { ISystemnutzung } from '@/shared/model/systemnutzung.model';
import AlertMixin from '@/shared/alert/alert.mixin';

import SystemnutzungService from './systemnutzung.service';

@Component
export default class Systemnutzung extends mixins(Vue2Filters.mixin, AlertMixin) {
  @Inject('systemnutzungService') private systemnutzungService: () => SystemnutzungService;
  public currentSearch = '';
  private removeId: number = null;

  public systemnutzungs: ISystemnutzung[] = [];

  public isFetching = false;

  public mounted(): void {
    this.retrieveAllSystemnutzungs();
  }

  public search(query): void {
    if (!query) {
      return this.clear();
    }
    this.currentSearch = query;
    this.retrieveAllSystemnutzungs();
  }

  public clear(): void {
    this.currentSearch = '';
    this.retrieveAllSystemnutzungs();
  }

  public retrieveAllSystemnutzungs(): void {
    this.isFetching = true;

    if (this.currentSearch) {
      this.systemnutzungService()
        .search(this.currentSearch)
        .then(
          res => {
            this.systemnutzungs = res;
            this.isFetching = false;
          },
          err => {
            this.isFetching = false;
          }
        );
      return;
    }
    this.systemnutzungService()
      .retrieve()
      .then(
        res => {
          this.systemnutzungs = res.data;
          this.isFetching = false;
        },
        err => {
          this.isFetching = false;
        }
      );
  }

  public prepareRemove(instance: ISystemnutzung): void {
    this.removeId = instance.id;
    if (<any>this.$refs.removeEntity) {
      (<any>this.$refs.removeEntity).show();
    }
  }

  public removeSystemnutzung(): void {
    this.systemnutzungService()
      .delete(this.removeId)
      .then(() => {
        const message = this.$t('gdb3App.systemnutzung.deleted', { param: this.removeId });
        this.alertService().showAlert(message, 'danger');
        this.getAlertFromStore();
        this.removeId = null;
        this.retrieveAllSystemnutzungs();
        this.closeDialog();
      });
  }

  public closeDialog(): void {
    (<any>this.$refs.removeEntity).hide();
  }
}
