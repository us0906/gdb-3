import { $, ElementFinder, element, by } from 'protractor';

import AlertPage from './alert-page';

export default class SignInPage extends AlertPage {
  loginForm: ElementFinder = $('#login-page');
  username: ElementFinder = element(by.name('username'));
  password: ElementFinder = element(by.name('password'));
  loginButton: ElementFinder = element(by.css('input[type=submit]'));
  title: ElementFinder = this.loginForm.$('#login-title');
  closeButton: ElementFinder = this.loginForm.$('.close');

  async getTitle() {
    return this.title.getAttribute('id');
  }

  async login(username: string, password: string) {
    if (await this.username.isPresent()) {
      await this.username.sendKeys(username);
      await this.password.sendKeys(password);
      await this.loginButton.click();
    }
  }
}
