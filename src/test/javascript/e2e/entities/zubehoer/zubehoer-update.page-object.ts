import { by, element, ElementFinder } from 'protractor';

import AlertPage from '../../page-objects/alert-page';

export default class ZubehoerUpdatePage extends AlertPage {
  title: ElementFinder = element(by.id('gdb3App.zubehoer.home.createOrEditLabel'));
  footer: ElementFinder = element(by.id('footer'));
  saveButton: ElementFinder = element(by.id('save-entity'));
  cancelButton: ElementFinder = element(by.id('cancel-save'));

  bezeichnungInput: ElementFinder = element(by.css('input#zubehoer-bezeichnung'));

  gueltigBisInput: ElementFinder = element(by.css('input#zubehoer-gueltigBis'));

  herstellerSelect: ElementFinder = element(by.id('zubehoer-hersteller')).element(by.css("input[type='search']"));

  zubehoerTypSelect: ElementFinder = element(by.id('zubehoer-zubehoerTyp')).element(by.css("input[type='search']"));
}
