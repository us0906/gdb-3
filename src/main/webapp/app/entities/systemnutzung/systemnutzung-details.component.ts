import { Component, Vue, Inject } from 'vue-property-decorator';

import { ISystemnutzung } from '@/shared/model/systemnutzung.model';
import SystemnutzungService from './systemnutzung.service';

@Component
export default class SystemnutzungDetails extends Vue {
  @Inject('systemnutzungService') private systemnutzungService: () => SystemnutzungService;
  public systemnutzung: ISystemnutzung = {};

  beforeRouteEnter(to, from, next) {
    next(vm => {
      if (to.params.systemnutzungId) {
        vm.retrieveSystemnutzung(to.params.systemnutzungId);
      }
    });
  }

  public retrieveSystemnutzung(systemnutzungId) {
    this.systemnutzungService()
      .find(systemnutzungId)
      .then(res => {
        this.systemnutzung = res;
      });
  }

  public previousState() {
    this.$router.go(-1);
  }
}
