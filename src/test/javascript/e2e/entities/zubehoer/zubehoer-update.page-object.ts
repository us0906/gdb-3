import { by, element, ElementFinder } from 'protractor';

import AlertPage from '../../page-objects/alert-page';

export default class ZubehoerUpdatePage extends AlertPage {
  title: ElementFinder = element(by.id('gdb3App.zubehoer.home.createOrEditLabel'));
  footer: ElementFinder = element(by.id('footer'));
  saveButton: ElementFinder = element(by.id('save-entity'));
  cancelButton: ElementFinder = element(by.id('cancel-save'));

  bezeichnungInput: ElementFinder = element(by.css('input#zubehoer-bezeichnung'));

  gueltigBisInput: ElementFinder = element(by.css('input#zubehoer-gueltigBis'));

  herstellerSelect = element(by.css('select#zubehoer-hersteller'));

  zubehoerTypSelect = element(by.css('select#zubehoer-zubehoerTyp'));
}
