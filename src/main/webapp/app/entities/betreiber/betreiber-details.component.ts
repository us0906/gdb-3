import { Component, Vue, Inject } from 'vue-property-decorator';

import { IBetreiber } from '@/shared/model/betreiber.model';
import BetreiberService from './betreiber.service';

@Component
export default class BetreiberDetails extends Vue {
  @Inject('betreiberService') private betreiberService: () => BetreiberService;
  public betreiber: IBetreiber = {};

  beforeRouteEnter(to, from, next) {
    next(vm => {
      if (to.params.betreiberId) {
        vm.retrieveBetreiber(to.params.betreiberId);
      }
    });
  }

  public retrieveBetreiber(betreiberId) {
    this.betreiberService()
      .find(betreiberId)
      .then(res => {
        this.betreiber = res;
      });
  }

  public previousState() {
    this.$router.go(-1);
  }
}
