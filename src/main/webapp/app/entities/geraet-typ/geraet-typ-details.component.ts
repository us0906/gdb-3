import { Component, Vue, Inject } from 'vue-property-decorator';

import { IGeraetTyp } from '@/shared/model/geraet-typ.model';
import GeraetTypService from './geraet-typ.service';

@Component
export default class GeraetTypDetails extends Vue {
  @Inject('geraetTypService') private geraetTypService: () => GeraetTypService;
  public geraetTyp: IGeraetTyp = {};

  beforeRouteEnter(to, from, next) {
    next(vm => {
      if (to.params.geraetTypId) {
        vm.retrieveGeraetTyp(to.params.geraetTypId);
      }
    });
  }

  public retrieveGeraetTyp(geraetTypId) {
    this.geraetTypService()
      .find(geraetTypId)
      .then(res => {
        this.geraetTyp = res;
      });
  }

  public previousState() {
    this.$router.go(-1);
  }
}
