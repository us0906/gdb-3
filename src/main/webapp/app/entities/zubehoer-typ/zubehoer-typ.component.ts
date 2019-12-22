import { mixins } from 'vue-class-component';

import { Component, Inject } from 'vue-property-decorator';
import Vue2Filters from 'vue2-filters';
import { IZubehoerTyp } from '@/shared/model/zubehoer-typ.model';
import AlertMixin from '@/shared/alert/alert.mixin';

import ZubehoerTypService from './zubehoer-typ.service';

@Component
export default class ZubehoerTyp extends mixins(Vue2Filters.mixin, AlertMixin) {
  @Inject('zubehoerTypService') private zubehoerTypService: () => ZubehoerTypService;
  public currentSearch = '';
  private removeId: number = null;
  public zubehoerTyps: IZubehoerTyp[] = [];

  public isFetching = false;

  public mounted(): void {
    this.retrieveAllZubehoerTyps();
  }

  public search(query): void {
    if (!query) {
      return this.clear();
    }
    this.currentSearch = query;
    this.retrieveAllZubehoerTyps();
  }

  public clear(): void {
    this.currentSearch = '';
    this.retrieveAllZubehoerTyps();
  }

  public retrieveAllZubehoerTyps(): void {
    this.isFetching = true;

    if (this.currentSearch) {
      this.zubehoerTypService()
        .search(this.currentSearch)
        .then(
          res => {
            this.zubehoerTyps = res;
            this.isFetching = false;
          },
          err => {
            this.isFetching = false;
          }
        );
      return;
    }
    this.zubehoerTypService()
      .retrieve()
      .then(
        res => {
          this.zubehoerTyps = res.data;
          this.isFetching = false;
        },
        err => {
          this.isFetching = false;
        }
      );
  }

  public prepareRemove(instance: IZubehoerTyp): void {
    this.removeId = instance.id;
  }

  public removeZubehoerTyp(): void {
    this.zubehoerTypService()
      .delete(this.removeId)
      .then(() => {
        const message = this.$t('gdb3App.zubehoerTyp.deleted', { param: this.removeId });
        this.alertService().showAlert(message, 'danger');
        this.getAlertFromStore();

        this.removeId = null;
        this.retrieveAllZubehoerTyps();
        this.closeDialog();
      });
  }

  public closeDialog(): void {
    (<any>this.$refs.removeEntity).hide();
  }
}
