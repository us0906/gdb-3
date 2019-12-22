import { mixins } from 'vue-class-component';

import { Component, Inject } from 'vue-property-decorator';
import Vue2Filters from 'vue2-filters';
import { ISystemtyp } from '@/shared/model/systemtyp.model';
import AlertMixin from '@/shared/alert/alert.mixin';

import SystemtypService from './systemtyp.service';

@Component
export default class Systemtyp extends mixins(Vue2Filters.mixin, AlertMixin) {
  @Inject('systemtypService') private systemtypService: () => SystemtypService;
  public currentSearch = '';
  private removeId: number = null;
  public systemtyps: ISystemtyp[] = [];

  public isFetching = false;

  public mounted(): void {
    this.retrieveAllSystemtyps();
  }

  public search(query): void {
    if (!query) {
      return this.clear();
    }
    this.currentSearch = query;
    this.retrieveAllSystemtyps();
  }

  public clear(): void {
    this.currentSearch = '';
    this.retrieveAllSystemtyps();
  }

  public retrieveAllSystemtyps(): void {
    this.isFetching = true;

    if (this.currentSearch) {
      this.systemtypService()
        .search(this.currentSearch)
        .then(
          res => {
            this.systemtyps = res;
            this.isFetching = false;
          },
          err => {
            this.isFetching = false;
          }
        );
      return;
    }
    this.systemtypService()
      .retrieve()
      .then(
        res => {
          this.systemtyps = res.data;
          this.isFetching = false;
        },
        err => {
          this.isFetching = false;
        }
      );
  }

  public prepareRemove(instance: ISystemtyp): void {
    this.removeId = instance.id;
  }

  public removeSystemtyp(): void {
    this.systemtypService()
      .delete(this.removeId)
      .then(() => {
        const message = this.$t('gdb3App.systemtyp.deleted', { param: this.removeId });
        this.alertService().showAlert(message, 'danger');
        this.getAlertFromStore();

        this.removeId = null;
        this.retrieveAllSystemtyps();
        this.closeDialog();
      });
  }

  public closeDialog(): void {
    (<any>this.$refs.removeEntity).hide();
  }
}
