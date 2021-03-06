import { by, element, ElementFinder } from 'protractor';

import AlertPage from '../../page-objects/alert-page';

export default class SystemtypUpdatePage extends AlertPage {
  title: ElementFinder = element(by.id('gdb3App.systemtyp.home.createOrEditLabel'));
  footer: ElementFinder = element(by.id('footer'));
  saveButton: ElementFinder = element(by.id('save-entity'));
  cancelButton: ElementFinder = element(by.id('cancel-save'));

  bezeichnungInput: ElementFinder = element(by.css('input#systemtyp-bezeichnung'));

  gueltigBisInput: ElementFinder = element(by.css('input#systemtyp-gueltigBis'));

  geraetSelect = element(by.css('select#systemtyp-geraet'));

  zubehoerSelect = element(by.css('select#systemtyp-zubehoer'));
}
