import { by, element, ElementFinder } from 'protractor';

import AlertPage from '../../page-objects/alert-page';

export default class SystemnutzungUpdatePage extends AlertPage {
  title: ElementFinder = element(by.id('gdb3App.systemnutzung.home.createOrEditLabel'));
  footer: ElementFinder = element(by.id('footer'));
  saveButton: ElementFinder = element(by.id('save-entity'));
  cancelButton: ElementFinder = element(by.id('cancel-save'));

  systeminstanzSelect = element(by.css('select#systemnutzung-systeminstanz'));

  arztSelect = element(by.css('select#systemnutzung-arzt'));
}
