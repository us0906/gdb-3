import { by, element, ElementArrayFinder, ElementFinder } from 'protractor';

import AlertPage from '../../page-objects/alert-page';

export default class ZubehoerTypComponentsPage extends AlertPage {
  title: ElementFinder = element(by.id('zubehoer-typ-heading'));

  footer: ElementFinder = element(by.id('footer'));
  createButton: ElementFinder = element(by.id('jh-create-entity'));

  noRecords: ElementFinder = element(by.css('#page-heading ~ div.alert.alert-warning'));
  table: ElementFinder = element(by.css('#page-heading ~ div.table-responsive > table'));

  records: ElementArrayFinder = this.table.all(by.css('tbody tr'));

  getDetailsButton(record: ElementFinder) {
    return record.element(by.css('.btn-info.details'));
  }
  getEditButton(record: ElementFinder) {
    return record.element(by.css('.btn-primary.edit'));
  }

  getDeleteButton(record: ElementFinder) {
    return record.element(by.css('div table .btn-danger'));
  }
}

export class ZubehoerTypDeleteDialog {
  dialog: ElementFinder = element(by.id('removeEntity'));
  title: ElementFinder = element(by.id('gdb3App.zubehoerTyp.delete.question'));
  confirmButton: ElementFinder = element(by.id('jhi-confirm-delete-zubehoerTyp'));
}
