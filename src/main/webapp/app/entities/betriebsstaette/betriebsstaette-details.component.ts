import { Component, Vue, Inject } from 'vue-property-decorator';

import { IBetriebsstaette } from '@/shared/model/betriebsstaette.model';
import BetriebsstaetteService from './betriebsstaette.service';

@Component
export default class BetriebsstaetteDetails extends Vue {
  @Inject('betriebsstaetteService') private betriebsstaetteService: () => BetriebsstaetteService;
  public betriebsstaette: IBetriebsstaette = {};

  beforeRouteEnter(to, from, next) {
    next(vm => {
      if (to.params.betriebsstaetteId) {
        vm.retrieveBetriebsstaette(to.params.betriebsstaetteId);
      }
    });
  }

  public retrieveBetriebsstaette(betriebsstaetteId) {
    this.betriebsstaetteService()
      .find(betriebsstaetteId)
      .then(res => {
        this.betriebsstaette = res;
      });
  }

  public previousState() {
    this.$router.go(-1);
  }
}
