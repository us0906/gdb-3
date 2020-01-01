'use strict';

const utils = require('../utils');

const productNameRoot = ['Sono', 'Medi', 'Sani', 'Pulsar', 'SonoStar', 'UltraStar', 'Tron', 'XR'];

function HealthProductName(faker) {
  return utils.getRandomItemOf(productNameRoot) + '-' + faker.random.number();
}

function create(faker, geraetTypDaten, herstellerDaten) {
  const result = {
    bezeichnung: HealthProductName(faker),
    geraetTypId: utils.getRandomItemOf(geraetTypDaten).id,
    herstellerId: utils.getRandomItemOf(herstellerDaten).id
  };

  return result;
}

module.exports = {
  create
};
