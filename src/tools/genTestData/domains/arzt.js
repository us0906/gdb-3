'use strict';

function lanr(faker) {
  let result = '';

  for (let i = 0; i < 7; i++) {
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
    titel: medTitel(faker),
    vorname: faker.name.firstName(),
    nachname: faker.name.lastName(),
    lanr: lanr(faker)
  };
  return result;
}

module.exports = {
  create
};
