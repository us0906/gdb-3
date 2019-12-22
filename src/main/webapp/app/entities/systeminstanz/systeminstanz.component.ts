import { mixins } from 'vue-class-component';

import { Component, Inject } from 'vue-property-decorator';
import Vue2Filters from 'vue2-filters';
import { ISysteminstanz } from '@/shared/model/systeminstanz.model';
import AlertMixin from '@/shared/alert/alert.mixin';

import JhiDataUtils from '@/shared/data/data-utils.service';

import SysteminstanzService from './systeminstanz.service';

@Component
export default class Systeminstanz extends mixins(JhiDataUtils, Vue2Filters.mixin, AlertMixin) {
  @Inject('systeminstanzService') private systeminstanzService: () => SysteminstanzService;
  public currentSearch = '';
  private removeId: number = null;
  public systeminstanzs: ISysteminstanz[] = [];

  public isFetching = false;

  public mounted(): void {
    this.retrieveAllSysteminstanzs();
  }

  public search(query): void {
    if (!query) {
      return this.clear();
    }
    this.currentSearch = query;
    this.retrieveAllSysteminstanzs();
  }

  public clear(): void {
    this.currentSearch = '';
    this.retrieveAllSysteminstanzs();
  }

  public retrieveAllSysteminstanzs(): void {
    this.isFetching = true;

    if (this.currentSearch) {
      this.systeminstanzService()
        .search(this.currentSearch)
        .then(
          res => {
            this.systeminstanzs = res;
            this.isFetching = false;
          },
          err => {
            this.isFetching = false;
          }
        );
      return;
    }
    this.systeminstanzService()
      .retrieve()
      .then(
        res => {
          this.systeminstanzs = res.data;
          this.isFetching = false;
        },
        err => {
          this.isFetching = false;
        }
      );
  }

  public prepareRemove(instance: ISysteminstanz): void {
    this.removeId = instance.id;
  }

  public removeSysteminstanz(): void {
    this.systeminstanzService()
      .delete(this.removeId)
      .then(() => {
        const message = this.$t('gdb3App.systeminstanz.deleted', { param: this.removeId });
        this.alertService().showAlert(message, 'danger');
        this.getAlertFromStore();

        this.removeId = null;
        this.retrieveAllSysteminstanzs();
        this.closeDialog();
      });
  }

  public closeDialog(): void {
    (<any>this.$refs.removeEntity).hide();
  }
}
