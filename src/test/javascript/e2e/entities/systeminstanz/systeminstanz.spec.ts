/* tslint:disable no-unused-expression */
import { browser } from 'protractor';

import NavBarPage from './../../page-objects/navbar-page';
import SysteminstanzComponentsPage, { SysteminstanzDeleteDialog } from './systeminstanz.page-object';
import SysteminstanzUpdatePage from './systeminstanz-update.page-object';
import SysteminstanzDetailsPage from './systeminstanz-details.page-object';

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

import path from 'path';

const expect = chai.expect;

describe('Systeminstanz e2e test', () => {
  let navBarPage: NavBarPage;
  let updatePage: SysteminstanzUpdatePage;
  let detailsPage: SysteminstanzDetailsPage;
  let listPage: SysteminstanzComponentsPage;
  /*let deleteDialog: SysteminstanzDeleteDialog;*/
  const fileToUpload = '../../../../../main/webapp/content/images/logo-jhipster.png';
  const absolutePath = path.resolve(__dirname, fileToUpload);
  let beforeRecordsCount = 0;

  before(async () => {
    await browser.get('/');
    navBarPage = new NavBarPage();
    await navBarPage.login('admin', 'admin');
  });

  after(async () => {
    await navBarPage.autoSignOut();
  });

  it('should load Systeminstanzs', async () => {
    await navBarPage.getEntityPage('systeminstanz');
    listPage = new SysteminstanzComponentsPage();

    await waitUntilAllDisplayed([listPage.title, listPage.footer]);

    expect(await listPage.title.getText()).not.to.be.empty;
    expect(await listPage.createButton.isEnabled()).to.be.true;

    await waitUntilAnyDisplayed([listPage.noRecords, listPage.table]);
    beforeRecordsCount = (await isVisible(listPage.noRecords)) ? 0 : await getRecordsCount(listPage.table);
  });
  describe('Create flow', () => {
    it('should load create Systeminstanz page', async () => {
      await listPage.createButton.click();
      updatePage = new SysteminstanzUpdatePage();

      await waitUntilAllDisplayed([updatePage.title, updatePage.footer, updatePage.saveButton]);

      expect(await updatePage.title.getAttribute('id')).to.match(/gdb3App.systeminstanz.home.createOrEditLabel/);
    });

    /* it('should create and save Systeminstanzs', async () => {

      await updatePage.bezeichnungInput.sendKeys('bezeichnung');
      expect(await updatePage.bezeichnungInput.getAttribute('value')).to.match(/bezeichnung/);


      await updatePage.geraetNummerInput.sendKeys('geraetNummer');
      expect(await updatePage.geraetNummerInput.getAttribute('value')).to.match(/geraetNummer/);


      await updatePage.geraetBaujahrInput.sendKeys('geraetBaujahr');
      expect(await updatePage.geraetBaujahrInput.getAttribute('value')).to.match(/geraetBaujahr/);


      await updatePage.gueltigBisInput.sendKeys('01-01-2001');
      expect(await updatePage.gueltigBisInput.getAttribute('value')).to.eq('2001-01-01');


      await waitUntilDisplayed(updatePage.gweInput);
      await updatePage.gweInput.sendKeys(absolutePath);


      await waitUntilDisplayed(updatePage.bemerkungInput);
      await updatePage.bemerkungInput.sendKeys('bemerkung');

      expect(await updatePage.bemerkungInput.getAttribute('value')).to.match(/bemerkung/);

      // await  selectLastOption(updatePage.systemtypSelect);
      // await  selectLastOption(updatePage.betriebsstaetteSelect);
      // await  selectLastOption(updatePage.betreiberSelect);

      expect(await updatePage.saveButton.isEnabled()).to.be.true;
      await updatePage.saveButton.click();

      await waitUntilHidden(updatePage.saveButton);
      expect(await isVisible(updatePage.saveButton)).to.be.false;

      await waitUntilDisplayed(listPage.successAlert);
      expect(await listPage.successAlert.isDisplayed()).to.be.true;

      await waitUntilCount(listPage.records, beforeRecordsCount + 1);
      expect(await listPage.records.count()).to.eq(beforeRecordsCount + 1);
    });*/

    /*
    describe('Details, Update, Delete flow', () => {

      after(async () => {

        const deleteButton = listPage.getDeleteButton(listPage.records.last());
        await click(deleteButton);

        deleteDialog = new SysteminstanzDeleteDialog();
        await waitUntilDisplayed(deleteDialog.dialog);

        expect(await deleteDialog.title.getAttribute('id')).to.match(/gdb3App.systeminstanz.delete.question/);

        await click(deleteDialog.confirmButton);
        await waitUntilHidden(deleteDialog.dialog);

        expect(await isVisible(deleteDialog.dialog)).to.be.false;
        expect(await listPage.dangerAlert.isDisplayed()).to.be.true;

        await waitUntilCount(listPage.records, beforeRecordsCount);
        expect(await listPage.records.count()).to.eq(beforeRecordsCount);
      });

      it('should load details Systeminstanz page and fetch data', async () => {

        const detailsButton = listPage.getDetailsButton(listPage.records.last());
        await click(detailsButton);

        detailsPage = new SysteminstanzDetailsPage();

        await waitUntilAllDisplayed([detailsPage.title, detailsPage.backButton, detailsPage.firstDetail]);

        expect(await detailsPage.title.getText()).not.to.be.empty;
        expect(await detailsPage.firstDetail.getText()).not.to.be.empty;

        await click(detailsPage.backButton);
        await waitUntilCount(listPage.records, beforeRecordsCount + 1);
      });

      it('should load edit Systeminstanz page, fetch data and update', async () => {

        const editButton = listPage.getEditButton(listPage.records.last());
        await click(editButton);

        await waitUntilAllDisplayed([updatePage.title, updatePage.footer, updatePage.saveButton]);

        expect(await updatePage.title.getText()).not.to.be.empty;

          await updatePage.bezeichnungInput.clear();
          await updatePage.bezeichnungInput.sendKeys('modified');
          expect(await updatePage.bezeichnungInput.getAttribute('value')).to.match(/modified/);

          await updatePage.geraetNummerInput.clear();
          await updatePage.geraetNummerInput.sendKeys('modified');
          expect(await updatePage.geraetNummerInput.getAttribute('value')).to.match(/modified/);

          await updatePage.geraetBaujahrInput.clear();
          await updatePage.geraetBaujahrInput.sendKeys('modified');
          expect(await updatePage.geraetBaujahrInput.getAttribute('value')).to.match(/modified/);

          await updatePage.gueltigBisInput.clear();
          await updatePage.gueltigBisInput.sendKeys('01-01-2019');
          expect(await updatePage.gueltigBisInput.getAttribute('value')).to.eq('2019-01-01');

          await updatePage.bemerkungInput.clear();
          await updatePage.bemerkungInput.sendKeys('updatedbemerkung');
          expect(await updatePage.bemerkungInput.getAttribute('value')).to.match(/updatedbemerkung/);


        await updatePage.saveButton.click();

        await waitUntilHidden(updatePage.saveButton);

        expect(await isVisible(updatePage.saveButton)).to.be.false;
        expect(await listPage.infoAlert.isDisplayed()).to.be.true;
        await waitUntilCount(listPage.records, beforeRecordsCount + 1);
      });
    });
    */
  });
});
