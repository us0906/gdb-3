import { Component, Vue, Inject } from 'vue-property-decorator';

import { IZubehoerTyp } from '@/shared/model/zubehoer-typ.model';
import ZubehoerTypService from './zubehoer-typ.service';

@Component
export default class ZubehoerTypDetails extends Vue {
  @Inject('zubehoerTypService') private zubehoerTypService: () => ZubehoerTypService;
  public zubehoerTyp: IZubehoerTyp = {};

  beforeRouteEnter(to, from, next) {
    next(vm => {
      if (to.params.zubehoerTypId) {
        vm.retrieveZubehoerTyp(to.params.zubehoerTypId);
      }
    });
  }

  public retrieveZubehoerTyp(zubehoerTypId) {
    this.zubehoerTypService()
      .find(zubehoerTypId)
      .then(res => {
        this.zubehoerTyp = res;
      });
  }

  public previousState() {
    this.$router.go(-1);
  }
}
