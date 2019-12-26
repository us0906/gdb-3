'use strict';

const utils = require('../utils');

const familie = ['Pulsar', 'Puls', 'Sono', 'Med'];
const zubehoerPrefix = ['US', 'SK', 'VR', 'ZQ', 'PR'];

function zubehoerBezeichnung() {
  return `${familie[Math.floor(Math.random() * familie.length)]}-${
    zubehoerPrefix[Math.floor(Math.random() * zubehoerPrefix.length)]
  } ${Math.trunc(Math.random() * 1000).toString()}`;
}

function create(faker, zubehoerTypDaten, herstellerDaten) {
  const result = {
    bezeichnung: zubehoerBezeichnung(),
    zubehoerTypId: utils.getRandomItemOf(zubehoerTypDaten).id,
    herstellerId: utils.getRandomItemOf(herstellerDaten).id
  };

  return result;
}

module.exports = {
  create
};
