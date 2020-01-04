import { by, element, ElementFinder } from 'protractor';

import AlertPage from '../../page-objects/alert-page';

export default class ArztUpdatePage extends AlertPage {
  title: ElementFinder = element(by.id('gdb3App.arzt.home.createOrEditLabel'));
  footer: ElementFinder = element(by.id('footer'));
  saveButton: ElementFinder = element(by.id('save-entity'));
  cancelButton: ElementFinder = element(by.id('cancel-save'));

  lanrInput: ElementFinder = element(by.css('input#arzt-lanr'));

  titelInput: ElementFinder = element(by.css('input#arzt-titel'));

  vornameInput: ElementFinder = element(by.css('input#arzt-vorname'));

  nachnameInput: ElementFinder = element(by.css('input#arzt-nachname'));

  bezeichnungInput: ElementFinder = element(by.css('input#arzt-bezeichnung'));
}
