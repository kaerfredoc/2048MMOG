import alt from './libs/alt';
import makeFinalStore from 'alt-utils/lib/makeFinalStore';
import React from 'react';
import App from './views/App';
import VertxActions from './actions/VertxActions.js';

function main() {
  const finalStore = makeFinalStore(alt);

  finalStore.listen(() => {
    console.log("Dispatch cycle complete");
  });

  const app = document.createElement('div');
  document.body.appendChild(app);

  React.render(<App />, app);
}

main();

const Vertx = require('vertx3-eventbus-client');
var eb = new Vertx("http://localhost:8080/eventbus");
eb.onopen = function () {
  eb.publish("org.mmog2048", {"msg":"From the client"});
  eb.publish("adam.test", {"msg": "Giggidy"});
};
