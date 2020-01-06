'strict on';

const program = require('commander');

const utils = require('./utils.js');
const arzt = require('./domains/arzt.js');
const betreiber = require('./domains/betreiber.js');
const betriebsstaette = require('./domains/betriebsstaette.js');

const hersteller = require('./domains/hersteller.js');
const geraet = require('./domains/geraet');
const zubehoer = require('./domains/zubehoer');
const systemtyp = require('./domains/system');
const systeminstanz = require('./domains/systeminstanz');
const geraetTyp = require('./domains/geraetTyp');
const zubehoerTyp = require('./domains/zubehoerTyp');
const faker = require('faker/locale/de');

faker.locale = 'de';
//faker.seed(100);

async function getAuthorization(user, pw) {
  return await utils.getOAuthAuthorization(user, pw);
}

program
  .version('0.0.1')
  .command('init')
  .action(async function() {
    console.log('Initialisiere GDB mit Stamm- und Demo-Daten');

    var authorizationkey = await utils.getOAuthAuthorization('admin', 'admin');
    console.log('Token is: ' + authorizationkey);
    const ANZAHL_BETREIBER = 50;
    const ANZAHL_AERZTE = 50;
    const ANZAHL_BETRIEBSSTAETTEN = 50;
    const ANZAHL_GERAETE = 50;
    const ANZAHL_ZUBEHOER = 50;
    const ANZAHL_SYSTEME = 50;
    const ANZAHL_HERSTELLER = 50;
    const ANZAHL_SYSTEMINSTANZEN = 20;

    if (authorizationkey) {
      let jsonData = geraetTyp.create(faker);
      utils.send('geraet-typs', jsonData, authorizationkey);

      jsonData = zubehoerTyp.create(faker);
      utils.send('zubehoer-typs', jsonData, authorizationkey);

      for (var i = 0; i < ANZAHL_AERZTE; i++) {
        const jsonData = arzt.create(faker);
        utils.send('arzts', jsonData, authorizationkey);
      }

      for (var i = 0; i < ANZAHL_BETREIBER; i++) {
        const jsonData = betreiber.create(faker);
        utils.send('betreibers', jsonData, authorizationkey);
      }

      for (var i = 0; i < ANZAHL_BETRIEBSSTAETTEN; i++) {
        const jsonData = betriebsstaette.create(faker);
        utils.send('betriebsstaettes', jsonData, authorizationkey);
      }

      for (var i = 0; i < ANZAHL_HERSTELLER; i++) {
        const jsonData = hersteller.create(faker);
        utils.send('herstellers', jsonData, authorizationkey);
      }
      const herstellerDaten = await utils.getData('herstellers', authorizationkey);
      const geraetTypDaten = await utils.getData('geraet-typs', authorizationkey);

      for (var i = 0; i < ANZAHL_GERAETE; i++) {
        const jsonData = geraet.create(faker, geraetTypDaten, herstellerDaten);
        utils.send('geraets', jsonData, authorizationkey);
      }
      const zubehoerTypDaten = await utils.getData('zubehoer-typs', authorizationkey);

      for (var i = 0; i < ANZAHL_ZUBEHOER; i++) {
        const jsonData = zubehoer.create(faker, zubehoerTypDaten, herstellerDaten);
        utils.send('zubehoers', jsonData, authorizationkey);
      }
      const geraetDaten = await utils.getData('geraets', authorizationkey);

      if (geraetDaten == null || geraetDaten.length == 0) {
        console.log('keine GeraetDaten gefunden!');
        process.exit(1);
      }
      console.log('Geraete: ', geraetDaten.length);
      //console.log (JSON.stringify(geraetDaten));

      const zubehoerDaten = await utils.getData('zubehoers', authorizationkey);

      if (zubehoerDaten == null || zubehoerDaten.length == 0) {
        console.log('keine zubehoerDaten gefunden!');
        process.exit(1);
      }
      for (var i = 0; i < ANZAHL_SYSTEME; i++) {
        const jsonData = systemtyp.create(faker, geraetDaten, zubehoerDaten);
        utils.send('systemtyps', jsonData, authorizationkey);
      }

      const systemdaten = await utils.getData('systemtyps', authorizationkey);

      if (systemdaten == null || systemdaten.length == 0) {
        console.log('keine systemdaten gefunden!');
        process.exit(1);
      }
      console.log('systemdaten: ', systemdaten.length);
      //console.log (JSON.stringify(geraetDaten));

      const betreiberDaten = await utils.getData('betreibers', authorizationkey);

      if (betreiberDaten == null || betreiberDaten.length == 0) {
        console.log('keine betreiberDaten gefunden!');
        process.exit(1);
      }
      console.log('betreiberDaten: ' + betreiberDaten.length);
      //console.log (JSON.stringify(betreiberDaten));

      const betriebsstaettenDaten = await utils.getData('betriebsstaettes', authorizationkey);

      if (betriebsstaettenDaten == null || betriebsstaettenDaten.length == 0) {
        console.log('keine betriebsstaettenDaten gefunden!');
        process.exit(1);
      }
      console.log('betriebsstaettenDaten: ' + betriebsstaettenDaten.length);
      //console.log (JSON.stringify(betriebsstaettenDaten));

      for (var i = 0; i < ANZAHL_SYSTEMINSTANZEN; i++) {
        const jsonData = systeminstanz.create(faker, systemdaten, betreiberDaten, betriebsstaettenDaten);
        utils.send('systeminstanzs', jsonData, authorizationkey);
      }
    }
  });

program
  .version('0.0.1')
  .command('arzt <anzahl>')
  .action(async function(anzahl) {
    console.log('Erzeuge ' + anzahl + ' neue Arzt Instanzen');

    var authorizationkey = await utils.getOAuthAuthorization('admin', 'admin');
    console.log('Token is: ' + authorizationkey);
    if (authorizationkey) {
      for (var i = 0; i < anzahl; i++) {
        const jsonData = arzt.create(faker);
        utils.send('arzts', jsonData, authorizationkey);
      }
    }
  });

program
  .version('0.0.1')
  .command('betreiber <anzahl>')
  .action(async function(anzahl) {
    console.log('Erzeuge ' + anzahl + ' neue Betreiber Instanzen');

    var authorizationkey = await utils.getOAuthAuthorization('admin', 'admin');

    console.log('Token is: ' + authorizationkey);

    for (var i = 0; i < anzahl; i++) {
      const jsonData = betreiber.create(faker);
      utils.send('betreibers', jsonData, authorizationkey);
    }
  });

program
  .version('0.0.1')
  .command('betriebsstaette <anzahl>')
  .action(async function(anzahl) {
    console.log('Erzeuge ' + anzahl + ' neue betriebsstaette Instanzen');

    var authorizationkey = await utils.getOAuthAuthorization('admin', 'admin');

    for (var i = 0; i < anzahl; i++) {
      const jsonData = betriebsstaette.create(faker);
      utils.send('betriebsstaettes', jsonData, authorizationkey);
    }
  });

program
  .version('0.0.1')
  .command('hersteller <anzahl>')
  .action(async function(anzahl) {
    console.log('Erzeuge ' + anzahl + ' neue hersteller Instanzen');

    var authorizationkey = await utils.getOAuthAuthorization('admin', 'admin');

    //  console.log('Token is: ' + authorizationkey);

    for (var i = 0; i < anzahl; i++) {
      const jsonData = hersteller.create(faker);
      utils.send('herstellers', jsonData, authorizationkey);
    }
  });

program
  .version('0.0.1')
  .command('geraet <anzahl>')
  .action(async function(anzahl) {
    console.log('Erzeuge ' + anzahl + ' neue geraet Instanzen');

    var authorizationkey = await utils.getOAuthAuthorization('admin', 'admin');

    console.log('Token is: ' + authorizationkey);

    const geraetTypDaten = await utils.getData('geraet-typs', authorizationkey);

    console.log(JSON.stringify(geraetTypDaten));

    const herstellerDaten = await utils.getData('herstellers', authorizationkey);

    console.log(JSON.stringify(herstellerDaten));

    for (var i = 0; i < anzahl; i++) {
      const jsonData = geraet.create(faker, geraetTypDaten, herstellerDaten);
      utils.send('geraets', jsonData, authorizationkey);
    }
  });

program
  .version('0.0.1')
  .command('zubehoer <anzahl>')
  .action(async function(anzahl) {
    console.log('Erzeuge ' + anzahl + ' neue zubehoer Instanzen');

    var authorizationkey = await utils.getOAuthAuthorization('admin', 'admin');

    console.log('Token is: ' + authorizationkey);

    const zubehoerTypDaten = await utils.getData('zubehoer-typs', authorizationkey);

    console.log(JSON.stringify(zubehoerTypDaten));

    const herstellerDaten = await utils.getData('herstellers', authorizationkey);

    console.log(JSON.stringify(herstellerDaten));

    for (var i = 0; i < anzahl; i++) {
      const jsonData = zubehoer.create(faker, zubehoerTypDaten, herstellerDaten);
      utils.send('zubehoers', jsonData, authorizationkey);
    }
  });

program
  .version('0.0.1')
  .command('system <anzahl>')
  .action(async function(anzahl) {
    console.log('Erzeuge ' + anzahl + ' neue system Instanzen');
    var authorizationkey = await utils.getOAuthAuthorization('admin', 'admin');
    console.log('Token is: ' + authorizationkey);

    const geraetDaten = await utils.getData('geraets', authorizationkey);

    if (geraetDaten == null || geraetDaten.length == 0) {
      console.log('keine GeraetDaten gefunden!');
      process.exit(1);
    }
    console.log('Geraete: ', geraetDaten.length);
    //console.log (JSON.stringify(geraetDaten));

    const zubehoerDaten = await utils.getData('zubehoers', authorizationkey);

    if (zubehoerDaten == null || zubehoerDaten.length == 0) {
      console.log('keine zubehoerDaten gefunden!');
      process.exit(1);
    }
    console.log('Zubehöre: ' + zubehoerDaten.length);
    console.log(JSON.stringify(zubehoerDaten));

    for (var i = 0; i < anzahl; i++) {
      const jsonData = systemtyp.create(faker, geraetDaten, zubehoerDaten);
      utils.send('systemtyps', jsonData, authorizationkey);
    }
  });

program
  .version('0.0.1')
  .command('systeminstanz <anzahl>')
  .action(async function(anzahl) {
    console.log('Erzeuge ' + anzahl + ' neue system Instanzen');
    var authorizationkey = await utils.getOAuthAuthorization('admin', 'admin');
    console.log('Token is: ' + authorizationkey);

    const systemdaten = await utils.getData('systemtyps', authorizationkey);

    if (systemdaten == null || systemdaten.length == 0) {
      console.log('keine systemdaten gefunden!');
      process.exit(1);
    }
    console.log('systemdaten: ', systemdaten.length);
    //console.log (JSON.stringify(geraetDaten));

    const betreiberDaten = await utils.getData('betreibers', authorizationkey);

    if (betreiberDaten == null || betreiberDaten.length == 0) {
      console.log('keine betreiberDaten gefunden!');
      process.exit(1);
    }
    console.log('betreiberDaten: ' + betreiberDaten.length);
    //console.log (JSON.stringify(betreiberDaten));

    const betriebsstaettenDaten = await utils.getData('betriebsstaettes', authorizationkey);

    if (betriebsstaettenDaten == null || betriebsstaettenDaten.length == 0) {
      console.log('keine betriebsstaettenDaten gefunden!');
      process.exit(1);
    }
    console.log('betriebsstaettenDaten: ' + betriebsstaettenDaten.length);
    //console.log (JSON.stringify(betriebsstaettenDaten));

    for (var i = 0; i < anzahl; i++) {
      const jsonData = systeminstanz.create(faker, systemdaten, betreiberDaten, betriebsstaettenDaten);
      utils.send('systeminstanzs', jsonData, authorizationkey);
    }
  });

program.parse(process.argv);
