import { Component, Vue, Inject } from 'vue-property-decorator';

import { IHersteller } from '@/shared/model/hersteller.model';
import HerstellerService from './hersteller.service';

@Component
export default class HerstellerDetails extends Vue {
  @Inject('herstellerService') private herstellerService: () => HerstellerService;
  public hersteller: IHersteller = {};

  beforeRouteEnter(to, from, next) {
    next(vm => {
      if (to.params.herstellerId) {
        vm.retrieveHersteller(to.params.herstellerId);
      }
    });
  }

  public retrieveHersteller(herstellerId) {
    this.herstellerService()
      .find(herstellerId)
      .then(res => {
        this.hersteller = res;
      });
  }

  public previousState() {
    this.$router.go(-1);
  }
}
