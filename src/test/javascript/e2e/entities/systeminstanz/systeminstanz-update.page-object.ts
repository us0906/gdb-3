import { by, element, ElementFinder } from 'protractor';

import AlertPage from '../../page-objects/alert-page';

export default class SysteminstanzUpdatePage extends AlertPage {
  title: ElementFinder = element(by.id('gdb3App.systeminstanz.home.createOrEditLabel'));
  footer: ElementFinder = element(by.id('footer'));
  saveButton: ElementFinder = element(by.id('save-entity'));
  cancelButton: ElementFinder = element(by.id('cancel-save'));

  bezeichnungInput: ElementFinder = element(by.css('input#systeminstanz-bezeichnung'));

  geraetNummerInput: ElementFinder = element(by.css('input#systeminstanz-geraetNummer'));

  geraetBaujahrInput: ElementFinder = element(by.css('input#systeminstanz-geraetBaujahr'));

  gueltigBisInput: ElementFinder = element(by.css('input#systeminstanz-gueltigBis'));

  gweInput: ElementFinder = element(by.css('input#file_gwe'));

  bemerkungInput: ElementFinder = element(by.css('textarea#systeminstanz-bemerkung'));

  systemtypSelect = element(by.id('systeminstanz-systemtyp')).element(by.css("input[type='search']"));

  betriebsstaetteSelect = element(by.id('systeminstanz-betriebsstaette')).element(by.css("input[type='search']"));

  betreiberSelect = element(by.id('systeminstanz-betreiber')).element(by.css("input[type='search']"));
}
