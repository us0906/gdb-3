import { mixins } from 'vue-class-component';

import { Component, Inject } from 'vue-property-decorator';
import Vue2Filters from 'vue2-filters';
import { IGeraet } from '@/shared/model/geraet.model';
import AlertMixin from '@/shared/alert/alert.mixin';

import GeraetService from './geraet.service';

@Component
export default class Geraet extends mixins(Vue2Filters.mixin, AlertMixin) {
  @Inject('geraetService') private geraetService: () => GeraetService;
  public currentSearch = '';
  private removeId: number = null;
  public geraets: IGeraet[] = [];

  public isFetching = false;

  public mounted(): void {
    this.retrieveAllGeraets();
  }

  public search(query): void {
    if (!query) {
      return this.clear();
    }
    this.currentSearch = query;
    this.retrieveAllGeraets();
  }

  public clear(): void {
    this.currentSearch = '';
    this.retrieveAllGeraets();
  }

  public retrieveAllGeraets(): void {
    this.isFetching = true;

    if (this.currentSearch) {
      this.geraetService()
        .search(this.currentSearch)
        .then(
          res => {
            this.geraets = res;
            this.isFetching = false;
          },
          err => {
            this.isFetching = false;
          }
        );
      return;
    }
    this.geraetService()
      .retrieve()
      .then(
        res => {
          this.geraets = res.data;
          this.isFetching = false;
        },
        err => {
          this.isFetching = false;
        }
      );
  }

  public prepareRemove(instance: IGeraet): void {
    this.removeId = instance.id;
  }

  public removeGeraet(): void {
    this.geraetService()
      .delete(this.removeId)
      .then(() => {
        const message = this.$t('gdb3App.geraet.deleted', { param: this.removeId });
        this.alertService().showAlert(message, 'danger');
        this.getAlertFromStore();

        this.removeId = null;
        this.retrieveAllGeraets();
        this.closeDialog();
      });
  }

  public closeDialog(): void {
    (<any>this.$refs.removeEntity).hide();
  }
}
