/* tslint:disable no-unused-expression */
import { browser } from 'protractor';

import NavBarPage from './../../page-objects/navbar-page';
import BetreiberComponentsPage, { BetreiberDeleteDialog } from './betreiber.page-object';
import BetreiberUpdatePage from './betreiber-update.page-object';
import BetreiberDetailsPage from './betreiber-details.page-object';

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

describe('Betreiber e2e test', () => {
  let navBarPage: NavBarPage;
  let updatePage: BetreiberUpdatePage;
  let detailsPage: BetreiberDetailsPage;
  let listPage: BetreiberComponentsPage;
  let deleteDialog: BetreiberDeleteDialog;
  let beforeRecordsCount = 0;

  before(async () => {
    await browser.get('/');
    navBarPage = new NavBarPage();
    await navBarPage.login('admin', 'admin');
  });

  after(async () => {
    await navBarPage.autoSignOut();
  });

  it('should load Betreibers', async () => {
    await navBarPage.getEntityPage('betreiber');
    listPage = new BetreiberComponentsPage();

    await waitUntilAllDisplayed([listPage.title, listPage.footer]);

    expect(await listPage.title.getText()).not.to.be.empty;
    expect(await listPage.createButton.isEnabled()).to.be.true;

    await waitUntilAnyDisplayed([listPage.noRecords, listPage.table]);
    beforeRecordsCount = (await isVisible(listPage.noRecords)) ? 0 : await getRecordsCount(listPage.table);
  });
  describe('Create flow', () => {
    it('should load create Betreiber page', async () => {
      await listPage.createButton.click();
      updatePage = new BetreiberUpdatePage();

      await waitUntilAllDisplayed([updatePage.title, updatePage.footer, updatePage.saveButton]);

      expect(await updatePage.title.getAttribute('id')).to.match(/gdb3App.betreiber.home.createOrEditLabel/);
    });

    it('should create and save Betreibers', async () => {
      await updatePage.vornameInput.sendKeys('vorname');
      expect(await updatePage.vornameInput.getAttribute('value')).to.match(/vorname/);

      await updatePage.nachnameInput.sendKeys('nachname');
      expect(await updatePage.nachnameInput.getAttribute('value')).to.match(/nachname/);

      await updatePage.strasseInput.sendKeys('strasse');
      expect(await updatePage.strasseInput.getAttribute('value')).to.match(/strasse/);

      await updatePage.hausnummerInput.sendKeys('hausnummer');
      expect(await updatePage.hausnummerInput.getAttribute('value')).to.match(/hausnummer/);

      await updatePage.plzInput.sendKeys('plz');
      expect(await updatePage.plzInput.getAttribute('value')).to.match(/plz/);

      await updatePage.ortInput.sendKeys('ort');
      expect(await updatePage.ortInput.getAttribute('value')).to.match(/ort/);

      //await updatePage.bezeichnungInput.sendKeys('bezeichnung');
      //expect(await updatePage.bezeichnungInput.getAttribute('value')).to.match(/bezeichnung/);

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

        deleteDialog = new BetreiberDeleteDialog();
        await waitUntilDisplayed(deleteDialog.dialog);

        expect(await deleteDialog.title.getAttribute('id')).to.match(/gdb3App.betreiber.delete.question/);

        await click(deleteDialog.confirmButton);
        await waitUntilHidden(deleteDialog.dialog);

        expect(await isVisible(deleteDialog.dialog)).to.be.false;
        expect(await listPage.dangerAlert.isDisplayed()).to.be.true;

        await waitUntilCount(listPage.records, beforeRecordsCount);
        expect(await listPage.records.count()).to.eq(beforeRecordsCount);
      });

      it('should load details Betreiber page and fetch data', async () => {
        const detailsButton = listPage.getDetailsButton(listPage.records.last());
        await click(detailsButton);

        detailsPage = new BetreiberDetailsPage();

        await waitUntilAllDisplayed([detailsPage.title, detailsPage.backButton, detailsPage.firstDetail]);

        expect(await detailsPage.title.getText()).not.to.be.empty;
        expect(await detailsPage.firstDetail.getText()).not.to.be.empty;

        await click(detailsPage.backButton);
        await waitUntilCount(listPage.records, beforeRecordsCount + 1);
      });

      it('should load edit Betreiber page, fetch data and update', async () => {
        const editButton = listPage.getEditButton(listPage.records.last());
        await click(editButton);

        await waitUntilAllDisplayed([updatePage.title, updatePage.footer, updatePage.saveButton]);

        expect(await updatePage.title.getText()).not.to.be.empty;

        await updatePage.vornameInput.clear();
        await updatePage.vornameInput.sendKeys('modified');
        expect(await updatePage.vornameInput.getAttribute('value')).to.match(/modified/);

        await updatePage.nachnameInput.clear();
        await updatePage.nachnameInput.sendKeys('modified');
        expect(await updatePage.nachnameInput.getAttribute('value')).to.match(/modified/);

        await updatePage.strasseInput.clear();
        await updatePage.strasseInput.sendKeys('modified');
        expect(await updatePage.strasseInput.getAttribute('value')).to.match(/modified/);

        await updatePage.hausnummerInput.clear();
        await updatePage.hausnummerInput.sendKeys('modified');
        expect(await updatePage.hausnummerInput.getAttribute('value')).to.match(/modified/);

        await updatePage.plzInput.clear();
        await updatePage.plzInput.sendKeys('modified');
        expect(await updatePage.plzInput.getAttribute('value')).to.match(/modified/);

        await updatePage.ortInput.clear();
        await updatePage.ortInput.sendKeys('modified');
        expect(await updatePage.ortInput.getAttribute('value')).to.match(/modified/);

        //await updatePage.bezeichnungInput.clear();
        //await updatePage.bezeichnungInput.sendKeys('modified');
        //expect(await updatePage.bezeichnungInput.getAttribute('value')).to.match(/modified/);

        await updatePage.saveButton.click();

        await waitUntilHidden(updatePage.saveButton);

        expect(await isVisible(updatePage.saveButton)).to.be.false;
        expect(await listPage.infoAlert.isDisplayed()).to.be.true;
        await waitUntilCount(listPage.records, beforeRecordsCount + 1);
      });
    });
  });
});
