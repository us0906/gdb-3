import { by, element, ElementFinder } from 'protractor';

import AlertPage from '../../page-objects/alert-page';

export default class ZubehoerTypUpdatePage extends AlertPage {
  title: ElementFinder = element(by.id('gdb3App.zubehoerTyp.home.createOrEditLabel'));
  footer: ElementFinder = element(by.id('footer'));
  saveButton: ElementFinder = element(by.id('save-entity'));
  cancelButton: ElementFinder = element(by.id('cancel-save'));

  bezeichnungInput: ElementFinder = element(by.css('input#zubehoer-typ-bezeichnung'));

  gueltigBisInput: ElementFinder = element(by.css('input#zubehoer-typ-gueltigBis'));

  technologieSelect = element(by.css('select#zubehoer-typ-technologie'));
}
