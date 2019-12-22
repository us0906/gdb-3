import { Component, Vue, Inject } from 'vue-property-decorator';

import { IGeraet } from '@/shared/model/geraet.model';
import GeraetService from './geraet.service';

@Component
export default class GeraetDetails extends Vue {
  @Inject('geraetService') private geraetService: () => GeraetService;
  public geraet: IGeraet = {};

  beforeRouteEnter(to, from, next) {
    next(vm => {
      if (to.params.geraetId) {
        vm.retrieveGeraet(to.params.geraetId);
      }
    });
  }

  public retrieveGeraet(geraetId) {
    this.geraetService()
      .find(geraetId)
      .then(res => {
        this.geraet = res;
      });
  }

  public previousState() {
    this.$router.go(-1);
  }
}
