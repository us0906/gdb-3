'use strict';
const utils = require('../utils');

function getRandomSerialNumber(faker) {
  return '#' + faker.random.number(9999);
}

function getRandomBezeichnung(faker) {
  return faker.lorem.word();
}

function create(faker, systeme, betreiber, betriebsstaetten) {
  const randomSystem = utils.getRandomItemOf(systeme);
  const randomBetreiber = utils.getRandomItemOf(betreiber);
  const randomBetriebsstaette = utils.getRandomItemOf(betriebsstaetten);
  const randomBaujahr = 2008 + faker.random.number(10);
  const randomGeraetNummer = getRandomSerialNumber(faker);
  const randomBezeichnung = getRandomBezeichnung(faker);

  const result = {
    bemerkung: faker.lorem.text(),
    betreiberId: randomBetreiber.id,
    betriebsstaetteId: randomBetriebsstaette.id,
    bezeichnung: randomBezeichnung,
    geraetBaujahr: randomBaujahr,
    geraetNummer: randomGeraetNummer,
    systemtypId: randomSystem.id
  };

  return result;
}

module.exports = {
  create
};
