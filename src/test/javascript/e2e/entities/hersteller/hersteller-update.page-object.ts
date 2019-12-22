import { by, element, ElementFinder } from 'protractor';

import AlertPage from '../../page-objects/alert-page';

export default class HerstellerUpdatePage extends AlertPage {
  title: ElementFinder = element(by.id('gdb3App.hersteller.home.createOrEditLabel'));
  footer: ElementFinder = element(by.id('footer'));
  saveButton: ElementFinder = element(by.id('save-entity'));
  cancelButton: ElementFinder = element(by.id('cancel-save'));

  bezeichnungInput: ElementFinder = element(by.css('input#hersteller-bezeichnung'));

  gueltigBisInput: ElementFinder = element(by.css('input#hersteller-gueltigBis'));
}
