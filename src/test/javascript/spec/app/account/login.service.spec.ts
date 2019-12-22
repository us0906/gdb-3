import LoginService from '@/account/login.service';
import axios from 'axios';

const mockedAxios: any = axios;
jest.mock('axios', () => ({
  post: jest.fn()
}));

describe('Login Service test suite', () => {
  let loginService;

  beforeEach(() => {
    loginService = new LoginService();
  });

  it('should build url for login', () => {
    const loc = { href: '', hostname: 'localhost', pathname: '/' };

    loginService.login(loc);

    expect(loc.href).toBe('//localhost/oauth2/authorization/oidc');
  });

  it('should build url for login behind client proxy', () => {
    const loc = { href: '', port: '8080', hostname: 'localhost', pathname: '/' };

    loginService.login(loc);

    expect(loc.href).toBe('//localhost:8080/oauth2/authorization/oidc');
  });

  it('should call global logout when asked to', () => {
    loginService.logout();

    expect(mockedAxios.post).toHaveBeenCalledWith('api/logout');
  });
});
