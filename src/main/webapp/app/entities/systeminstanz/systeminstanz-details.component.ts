import { Component, Inject } from 'vue-property-decorator';

import { mixins } from 'vue-class-component';
import JhiDataUtils from '@/shared/data/data-utils.service';

import { ISysteminstanz } from '@/shared/model/systeminstanz.model';
import SysteminstanzService from './systeminstanz.service';

@Component
export default class SysteminstanzDetails extends mixins(JhiDataUtils) {
  @Inject('systeminstanzService') private systeminstanzService: () => SysteminstanzService;
  public systeminstanz: ISysteminstanz = {};

  beforeRouteEnter(to, from, next) {
    next(vm => {
      if (to.params.systeminstanzId) {
        vm.retrieveSysteminstanz(to.params.systeminstanzId);
      }
    });
  }

  public retrieveSysteminstanz(systeminstanzId) {
    this.systeminstanzService()
      .find(systeminstanzId)
      .then(res => {
        this.systeminstanz = res;
      });
  }

  public previousState() {
    this.$router.go(-1);
  }
}
