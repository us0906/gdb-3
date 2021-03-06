/* tslint:disable no-unused-expression */
import { browser } from 'protractor';

import NavBarPage from './../../page-objects/navbar-page';
import ZubehoerComponentsPage, { ZubehoerDeleteDialog } from './zubehoer.page-object';
import ZubehoerUpdatePage from './zubehoer-update.page-object';
import ZubehoerDetailsPage from './zubehoer-details.page-object';

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

describe('Zubehoer e2e test', () => {
  let navBarPage: NavBarPage;
  let updatePage: ZubehoerUpdatePage;
  let detailsPage: ZubehoerDetailsPage;
  let listPage: ZubehoerComponentsPage;
  /*let deleteDialog: ZubehoerDeleteDialog;*/
  let beforeRecordsCount = 0;

  before(async () => {
    await browser.get('/');
    navBarPage = new NavBarPage();
    await navBarPage.login('admin', 'admin');
  });

  after(async () => {
    await navBarPage.autoSignOut();
  });

  it('should load Zubehoers', async () => {
    await navBarPage.getEntityPage('zubehoer');
    listPage = new ZubehoerComponentsPage();

    await waitUntilAllDisplayed([listPage.title, listPage.footer]);

    expect(await listPage.title.getText()).not.to.be.empty;
    expect(await listPage.createButton.isEnabled()).to.be.true;

    await waitUntilAnyDisplayed([listPage.noRecords, listPage.table]);
    beforeRecordsCount = (await isVisible(listPage.noRecords)) ? 0 : await getRecordsCount(listPage.table);
  });
  describe('Create flow', () => {
    it('should load create Zubehoer page', async () => {
      await listPage.createButton.click();
      updatePage = new ZubehoerUpdatePage();

      await waitUntilAllDisplayed([updatePage.title, updatePage.footer, updatePage.saveButton]);

      expect(await updatePage.title.getAttribute('id')).to.match(/gdb3App.zubehoer.home.createOrEditLabel/);
    });

    /* it('should create and save Zubehoers', async () => {

      await updatePage.bezeichnungInput.sendKeys('bezeichnung');
      expect(await updatePage.bezeichnungInput.getAttribute('value')).to.match(/bezeichnung/);


      await updatePage.gueltigBisInput.sendKeys('01-01-2001');
      expect(await updatePage.gueltigBisInput.getAttribute('value')).to.eq('2001-01-01');

      // await  selectLastOption(updatePage.herstellerSelect);
      // await  selectLastOption(updatePage.zubehoerTypSelect);

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

        deleteDialog = new ZubehoerDeleteDialog();
        await waitUntilDisplayed(deleteDialog.dialog);

        expect(await deleteDialog.title.getAttribute('id')).to.match(/gdb3App.zubehoer.delete.question/);

        await click(deleteDialog.confirmButton);
        await waitUntilHidden(deleteDialog.dialog);

        expect(await isVisible(deleteDialog.dialog)).to.be.false;
        expect(await listPage.dangerAlert.isDisplayed()).to.be.true;

        await waitUntilCount(listPage.records, beforeRecordsCount);
        expect(await listPage.records.count()).to.eq(beforeRecordsCount);
      });

      it('should load details Zubehoer page and fetch data', async () => {

        const detailsButton = listPage.getDetailsButton(listPage.records.last());
        await click(detailsButton);

        detailsPage = new ZubehoerDetailsPage();

        await waitUntilAllDisplayed([detailsPage.title, detailsPage.backButton, detailsPage.firstDetail]);

        expect(await detailsPage.title.getText()).not.to.be.empty;
        expect(await detailsPage.firstDetail.getText()).not.to.be.empty;

        await click(detailsPage.backButton);
        await waitUntilCount(listPage.records, beforeRecordsCount + 1);
      });

      it('should load edit Zubehoer page, fetch data and update', async () => {

        const editButton = listPage.getEditButton(listPage.records.last());
        await click(editButton);

        await waitUntilAllDisplayed([updatePage.title, updatePage.footer, updatePage.saveButton]);

        expect(await updatePage.title.getText()).not.to.be.empty;

          await updatePage.bezeichnungInput.clear();
          await updatePage.bezeichnungInput.sendKeys('modified');
          expect(await updatePage.bezeichnungInput.getAttribute('value')).to.match(/modified/);

          await updatePage.gueltigBisInput.clear();
          await updatePage.gueltigBisInput.sendKeys('01-01-2019');
          expect(await updatePage.gueltigBisInput.getAttribute('value')).to.eq('2019-01-01');


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
