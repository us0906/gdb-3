import { Component, Vue, Inject } from 'vue-property-decorator';

import { IArzt } from '@/shared/model/arzt.model';
import ArztService from './arzt.service';

@Component
export default class ArztDetails extends Vue {
  @Inject('arztService') private arztService: () => ArztService;
  public arzt: IArzt = {};

  beforeRouteEnter(to, from, next) {
    next(vm => {
      if (to.params.arztId) {
        vm.retrieveArzt(to.params.arztId);
      }
    });
  }

  public retrieveArzt(arztId) {
    this.arztService()
      .find(arztId)
      .then(res => {
        this.arzt = res;
      });
  }

  public previousState() {
    this.$router.go(-1);
  }
}
