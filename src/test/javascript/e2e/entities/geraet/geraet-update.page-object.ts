import { by, element, ElementFinder } from 'protractor';

import AlertPage from '../../page-objects/alert-page';

export default class GeraetUpdatePage extends AlertPage {
  title: ElementFinder = element(by.id('gdb3App.geraet.home.createOrEditLabel'));
  footer: ElementFinder = element(by.id('footer'));
  saveButton: ElementFinder = element(by.id('save-entity'));
  cancelButton: ElementFinder = element(by.id('cancel-save'));

  bezeichnungInput: ElementFinder = element(by.css('input#geraet-bezeichnung'));

  gueltigBisInput: ElementFinder = element(by.css('input#geraet-gueltigBis'));

  geraetTypSelect: ElementFinder = element(by.id('geraet-geraetTyp')).element(by.css("input[type='search']"));

  herstellerSelect: ElementFinder = element(by.id('geraet-hersteller')).element(by.css("input[type='search']"));
}
