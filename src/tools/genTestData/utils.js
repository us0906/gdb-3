'strict on';
const axios = require('axios');
const ClientOAuth2 = require('client-oauth2');

const baseURL = 'http://localhost:8080/api/';

async function postDataToUrl(entity, data, jwt) {
  return await axios.post(baseURL + entity, data, { headers: { Authorization: `Bearer ${jwt}` } });
}

async function send(entity, jsonData, authKey) {
  console.log(JSON.stringify(jsonData));

  return await postDataToUrl(entity, jsonData, authKey)
    .then(res => {
      return res;
    })
    .catch(error => {
      //console.log (error);

      console.log(error);
    });
}

async function getOAuthAuthorization(user, pw) {
  var jhipsterAuth = new ClientOAuth2({
    clientId: 'web_app',
    clientSecret: '2acc97cb-8eab-4daa-ae2b-6535f4bea249',
    accessTokenUri: 'http://localhost:9080/auth/realms/jhipster/protocol/openid-connect/token',
    authorizationUri: 'http://localhost:9080/auth/realms/jhipster/protocol/openid-connect/auth',
    redirectUri: 'http://localhost:8080',
    scopes: ['openid']
  });
  var token = await jhipsterAuth.owner.getToken(user, pw).then(function(user) {
    console.log(user); //=> { accessToken: '...', tokenType: 'bearer', ... }
    return user.accessToken;
  });

  return token;
}

async function getAuthorizationKey(user, pw) {
  const login = { password: pw, username: user, rememberMe: false };
  return axios
    .post(baseURL + 'authenticate', login)
    .then(response => {
      return response.data.id_token;
    })
    .catch(error => {
      if (error.response != undefined) {
        console.log(error.response.data);
      } else {
        console.log(error.Error);
        throw error;
      }
    });
}

async function getData(entity, jwt) {
  return await axios
    .get(baseURL + entity, { headers: { Authorization: `Bearer ${jwt}` } })
    .then(response => {
      console.log(response.data);
      return response.data;
    })
    .catch(error => {
      console.log(error);
    });
}

function getRandomItemOf(Data) {
  const i = Math.floor(Math.random() * Data.length);
  return Data[i];
}

module.exports = {
  getAuthorizationKey,
  getOAuthAuthorization,
  send,
  getData,
  getRandomItemOf
};
