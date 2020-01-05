import { by, element, ElementFinder } from 'protractor';

import AlertPage from '../../page-objects/alert-page';

export default class BetriebsstaetteUpdatePage extends AlertPage {
  title: ElementFinder = element(by.id('gdb3App.betriebsstaette.home.createOrEditLabel'));
  footer: ElementFinder = element(by.id('footer'));
  saveButton: ElementFinder = element(by.id('save-entity'));
  cancelButton: ElementFinder = element(by.id('cancel-save'));

  bsnrInput: ElementFinder = element(by.css('input#betriebsstaette-bsnr'));

  strasseInput: ElementFinder = element(by.css('input#betriebsstaette-strasse'));

  hausnummerInput: ElementFinder = element(by.css('input#betriebsstaette-hausnummer'));

  plzInput: ElementFinder = element(by.css('input#betriebsstaette-plz'));

  ortInput: ElementFinder = element(by.css('input#betriebsstaette-ort'));
}
