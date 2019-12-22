/* tslint:disable no-unused-expression */
import { browser } from 'protractor';

import NavBarPage from './../../page-objects/navbar-page';
import ArztComponentsPage, { ArztDeleteDialog } from './arzt.page-object';
import ArztUpdatePage from './arzt-update.page-object';
import ArztDetailsPage from './arzt-details.page-object';

import {
  clear,
  click,
  getRecordsCount,
  isVisible,
  selectLastOption,
  waitUntilAllDisplayed,
  waitUntilAnyDisplayed,
  waitUntilCount,
  waitUntilDisplayed,
  waitUntilHidden
} from '../../util/utils';

const expect = chai.expect;

describe('Arzt e2e test', () => {
  let navBarPage: NavBarPage;
  let updatePage: ArztUpdatePage;
  let detailsPage: ArztDetailsPage;
  let listPage: ArztComponentsPage;
  let deleteDialog: ArztDeleteDialog;
  let beforeRecordsCount = 0;

  before(async () => {
    await browser.get('/');
    navBarPage = new NavBarPage();
    await navBarPage.login('admin', 'admin');
  });

  after(async () => {
    await navBarPage.autoSignOut();
  });

  it('should load Arzts', async () => {
    await navBarPage.getEntityPage('arzt');
    listPage = new ArztComponentsPage();

    await waitUntilAllDisplayed([listPage.title, listPage.footer]);

    expect(await listPage.title.getText()).not.to.be.empty;
    expect(await listPage.createButton.isEnabled()).to.be.true;

    await waitUntilAnyDisplayed([listPage.noRecords, listPage.table]);
    beforeRecordsCount = (await isVisible(listPage.noRecords)) ? 0 : await getRecordsCount(listPage.table);
  });
  describe('Create flow', () => {
    it('should load create Arzt page', async () => {
      await listPage.createButton.click();
      updatePage = new ArztUpdatePage();

      await waitUntilAllDisplayed([updatePage.title, updatePage.footer, updatePage.saveButton]);

      expect(await updatePage.title.getAttribute('id')).to.match(/gdb3App.arzt.home.createOrEditLabel/);
    });

    it('should create and save Arzts', async () => {
      await updatePage.lanrInput.sendKeys('lanr');
      expect(await updatePage.lanrInput.getAttribute('value')).to.match(/lanr/);

      await updatePage.titelInput.sendKeys('titel');
      expect(await updatePage.titelInput.getAttribute('value')).to.match(/titel/);

      await updatePage.vornameInput.sendKeys('vorname');
      expect(await updatePage.vornameInput.getAttribute('value')).to.match(/vorname/);

      await updatePage.nachnameInput.sendKeys('nachname');
      expect(await updatePage.nachnameInput.getAttribute('value')).to.match(/nachname/);

      expect(await updatePage.saveButton.isEnabled()).to.be.true;
      await updatePage.saveButton.click();

      await waitUntilHidden(updatePage.saveButton);
      expect(await isVisible(updatePage.saveButton)).to.be.false;

      await waitUntilDisplayed(listPage.successAlert);
      expect(await listPage.successAlert.isDisplayed()).to.be.true;

      await waitUntilCount(listPage.records, beforeRecordsCount + 1);
      expect(await listPage.records.count()).to.eq(beforeRecordsCount + 1);
    });

    describe('Details, Update, Delete flow', () => {
      after(async () => {
        const deleteButton = listPage.getDeleteButton(listPage.records.last());
        await click(deleteButton);

        deleteDialog = new ArztDeleteDialog();
        await waitUntilDisplayed(deleteDialog.dialog);

        expect(await deleteDialog.title.getAttribute('id')).to.match(/gdb3App.arzt.delete.question/);

        await click(deleteDialog.confirmButton);
        await waitUntilHidden(deleteDialog.dialog);

        expect(await isVisible(deleteDialog.dialog)).to.be.false;
        expect(await listPage.dangerAlert.isDisplayed()).to.be.true;

        await waitUntilCount(listPage.records, beforeRecordsCount);
        expect(await listPage.records.count()).to.eq(beforeRecordsCount);
      });

      it('should load details Arzt page and fetch data', async () => {
        const detailsButton = listPage.getDetailsButton(listPage.records.last());
        await click(detailsButton);

        detailsPage = new ArztDetailsPage();

        await waitUntilAllDisplayed([detailsPage.title, detailsPage.backButton, detailsPage.firstDetail]);

        expect(await detailsPage.title.getText()).not.to.be.empty;
        expect(await detailsPage.firstDetail.getText()).not.to.be.empty;

        await click(detailsPage.backButton);
        await waitUntilCount(listPage.records, beforeRecordsCount + 1);
      });

      it('should load edit Arzt page, fetch data and update', async () => {
        const editButton = listPage.getEditButton(listPage.records.last());
        await click(editButton);

        await waitUntilAllDisplayed([updatePage.title, updatePage.footer, updatePage.saveButton]);

        expect(await updatePage.title.getText()).not.to.be.empty;

        await updatePage.lanrInput.clear();
        await updatePage.lanrInput.sendKeys('modified');
        expect(await updatePage.lanrInput.getAttribute('value')).to.match(/modified/);

        await updatePage.titelInput.clear();
        await updatePage.titelInput.sendKeys('modified');
        expect(await updatePage.titelInput.getAttribute('value')).to.match(/modified/);

        await updatePage.vornameInput.clear();
        await updatePage.vornameInput.sendKeys('modified');
        expect(await updatePage.vornameInput.getAttribute('value')).to.match(/modified/);

        await updatePage.nachnameInput.clear();
        await updatePage.nachnameInput.sendKeys('modified');
        expect(await updatePage.nachnameInput.getAttribute('value')).to.match(/modified/);

        await updatePage.saveButton.click();

        await waitUntilHidden(updatePage.saveButton);

        expect(await isVisible(updatePage.saveButton)).to.be.false;
        expect(await listPage.infoAlert.isDisplayed()).to.be.true;
        await waitUntilCount(listPage.records, beforeRecordsCount + 1);
      });
    });
  });
});
