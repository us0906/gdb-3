import { mixins } from 'vue-class-component';

import { Component, Inject } from 'vue-property-decorator';
import Vue2Filters from 'vue2-filters';
import { IGeraetTyp } from '@/shared/model/geraet-typ.model';
import AlertMixin from '@/shared/alert/alert.mixin';

import GeraetTypService from './geraet-typ.service';

@Component
export default class GeraetTyp extends mixins(Vue2Filters.mixin, AlertMixin) {
  @Inject('geraetTypService') private geraetTypService: () => GeraetTypService;
  public currentSearch = '';
  private removeId: number = null;

  public geraetTyps: IGeraetTyp[] = [];

  public isFetching = false;

  public mounted(): void {
    this.retrieveAllGeraetTyps();
  }

  public search(query): void {
    if (!query) {
      return this.clear();
    }
    this.currentSearch = query;
    this.retrieveAllGeraetTyps();
  }

  public clear(): void {
    this.currentSearch = '';
    this.retrieveAllGeraetTyps();
  }

  public retrieveAllGeraetTyps(): void {
    this.isFetching = true;

    if (this.currentSearch) {
      this.geraetTypService()
        .search(this.currentSearch)
        .then(
          res => {
            this.geraetTyps = res;
            this.isFetching = false;
          },
          err => {
            this.isFetching = false;
          }
        );
      return;
    }
    this.geraetTypService()
      .retrieve()
      .then(
        res => {
          this.geraetTyps = res.data;
          this.isFetching = false;
        },
        err => {
          this.isFetching = false;
        }
      );
  }

  public prepareRemove(instance: IGeraetTyp): void {
    this.removeId = instance.id;
    if (<any>this.$refs.removeEntity) {
      (<any>this.$refs.removeEntity).show();
    }
  }

  public removeGeraetTyp(): void {
    this.geraetTypService()
      .delete(this.removeId)
      .then(() => {
        const message = this.$t('gdb3App.geraetTyp.deleted', { param: this.removeId });
        this.alertService().showAlert(message, 'danger');
        this.getAlertFromStore();
        this.removeId = null;
        this.retrieveAllGeraetTyps();
        this.closeDialog();
      });
  }

  public closeDialog(): void {
    (<any>this.$refs.removeEntity).hide();
  }
}
