'use strict';

function lanr(faker) {
  var result = '';

  for (var i = 0; i < 7; i++) {
    result += faker.random.number(9).toString();
  }
  return result;
}

function medTitel(faker) {
  var result = '';
  var randomValue = faker.random.number(4);

  switch (randomValue) {
    case 0:
      result = '';
      break;
    case 1:
      result = 'Dr. med.';
      break;
    case 2:
      result = 'PD Dr. med.';
      break;
    case 4:
      result = 'Dr. Dr. med. Dr. rer. nat';
      break;
  }
  return result;
}

function create(faker) {
  var result = {
    vorname: faker.name.firstName(),
    nachname: faker.name.lastName(),
    strasse: faker.address.streetName(),
    hausnummer: faker.random.number(100).toString(),
    plz: faker.address.zipCode(),
    ort: faker.address.city()
  };

  return result;
}

module.exports = {
  create
};
