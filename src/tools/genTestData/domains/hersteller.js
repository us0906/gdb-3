'use strict';

function create(faker) {
  const result = {
    bezeichnung: faker.company.companyName()
  };

  return result;
}

module.exports = {
  create
};
