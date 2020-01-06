import { by, element, ElementFinder } from 'protractor';

import AlertPage from '../../page-objects/alert-page';

export default class SystemnutzungUpdatePage extends AlertPage {
  title: ElementFinder = element(by.id('gdb3App.systemnutzung.home.createOrEditLabel'));
  footer: ElementFinder = element(by.id('footer'));
  saveButton: ElementFinder = element(by.id('save-entity'));
  cancelButton: ElementFinder = element(by.id('cancel-save'));

  systeminstanzSelect: ElementFinder = element(by.id('systemnutzung-systeminstanz')).element(by.css("input[type='search']"));

  arztSelect: ElementFinder = element(by.id('systemnutzung-arzt')).element(by.css("input[type='search']"));
}
