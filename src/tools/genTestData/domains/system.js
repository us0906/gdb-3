'use strict';
const utils = require('../utils');

function create(faker, geraete, zubehoer) {
  const randomGeraet = utils.getRandomItemOf(geraete);
  const randomZubehoer = utils.getRandomItemOf(zubehoer);
  const result = {
    geraetId: randomGeraet.id,
    zubehoerId: randomZubehoer.id,
    bezeichnung: randomGeraet.bezeichnung + ' ' + randomZubehoer.bezeichnung
  };

  return result;
}

module.exports = {
  create
};
