import { Component, Vue, Inject } from 'vue-property-decorator';

import { ISystemtyp } from '@/shared/model/systemtyp.model';
import SystemtypService from './systemtyp.service';

@Component
export default class SystemtypDetails extends Vue {
  @Inject('systemtypService') private systemtypService: () => SystemtypService;
  public systemtyp: ISystemtyp = {};

  beforeRouteEnter(to, from, next) {
    next(vm => {
      if (to.params.systemtypId) {
        vm.retrieveSystemtyp(to.params.systemtypId);
      }
    });
  }

  public retrieveSystemtyp(systemtypId) {
    this.systemtypService()
      .find(systemtypId)
      .then(res => {
        this.systemtyp = res;
      });
  }

  public previousState() {
    this.$router.go(-1);
  }
}
