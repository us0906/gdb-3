/* tslint:disable no-unused-expression */
import { browser, element, by } from 'protractor';

import SignInPage from '../../page-objects/signin-page';
import NavBarPage from '../../page-objects/navbar-page';
import { isVisible, waitUntilDisplayed, waitUntilHidden } from '../../util/utils';

const expect = chai.expect;

describe('Account', () => {
  let navBarPage: NavBarPage = new NavBarPage();
  let signInPage: SignInPage;
  const loginPageTitle = 'login-title';

  describe('Login Flow', () => {
    before(async () => {
      await browser.get('/');
      navBarPage = new NavBarPage();
    });

    after(async () => {
      await navBarPage.autoSignOut();
    });

    it('should fail to login with bad password', async () => {
      signInPage = await navBarPage.getSignInPage();
      await signInPage.login('admin', 'foo');
      const alert = element(by.css('.alert-error'));
      if (await alert.isPresent()) {
        // Keycloak
        expect(await alert.getText()).to.eq('Invalid username or password.');
      } else {
        // Okta
        const error = element(by.css('.infobox-error'));
        await waitUntilDisplayed(error);
        expect(await error.getText()).to.eq('Sign in failed!');
      }
      await signInPage.username.clear();
      await signInPage.password.clear();
    });

    it('should login with admin account', async () => {
      await browser.get('/');
      signInPage = await navBarPage.getSignInPage();
      // Keycloak credentials by default, change them to be able to use oauth2 with Okta
      await signInPage.login('admin', 'admin');
      const success = element(by.className('alert-success'));
      await waitUntilDisplayed(success);

      // Success alert should appear in home page
      expect(await isVisible(success)).to.be.true;
    });
  });
});
