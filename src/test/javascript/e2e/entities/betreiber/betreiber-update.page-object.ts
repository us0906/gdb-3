import { by, element, ElementFinder } from 'protractor';

import AlertPage from '../../page-objects/alert-page';

export default class BetreiberUpdatePage extends AlertPage {
  title: ElementFinder = element(by.id('gdb3App.betreiber.home.createOrEditLabel'));
  footer: ElementFinder = element(by.id('footer'));
  saveButton: ElementFinder = element(by.id('save-entity'));
  cancelButton: ElementFinder = element(by.id('cancel-save'));

  vornameInput: ElementFinder = element(by.css('input#betreiber-vorname'));

  nachnameInput: ElementFinder = element(by.css('input#betreiber-nachname'));

  strasseInput: ElementFinder = element(by.css('input#betreiber-strasse'));

  hausnummerInput: ElementFinder = element(by.css('input#betreiber-hausnummer'));

  plzInput: ElementFinder = element(by.css('input#betreiber-plz'));

  ortInput: ElementFinder = element(by.css('input#betreiber-ort'));
}
