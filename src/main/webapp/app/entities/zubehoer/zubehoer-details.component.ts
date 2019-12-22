import { Component, Vue, Inject } from 'vue-property-decorator';

import { IZubehoer } from '@/shared/model/zubehoer.model';
import ZubehoerService from './zubehoer.service';

@Component
export default class ZubehoerDetails extends Vue {
  @Inject('zubehoerService') private zubehoerService: () => ZubehoerService;
  public zubehoer: IZubehoer = {};

  beforeRouteEnter(to, from, next) {
    next(vm => {
      if (to.params.zubehoerId) {
        vm.retrieveZubehoer(to.params.zubehoerId);
      }
    });
  }

  public retrieveZubehoer(zubehoerId) {
    this.zubehoerService()
      .find(zubehoerId)
      .then(res => {
        this.zubehoer = res;
      });
  }

  public previousState() {
    this.$router.go(-1);
  }
}
