import alt from './libs/alt';
import makeFinalStore from 'alt-utils/lib/makeFinalStore';
import React from 'react';
import ReactDOM from 'react-dom';
import App from './views/App';
import VertxActions from './actions/VertxActions.js';
import VertxStore from './stores/VertxStore';

function main() {
  const finalStore = makeFinalStore(alt);

  finalStore.listen(() => {
    console.log("Dispatch cycle complete");
  });

  const app = document.createElement('div');
  document.body.appendChild(app);

  ReactDOM.render(<App />, app);
}

main();


const Vertx = require('vertx3-eventbus-client');
var eb = new Vertx("/eventbus");

eb.onopen = function () {
  eb.send("org.mmog2048:register", {"name": "test"}, function(err, res) {
    if (res) {
      console.log("Received reply: " + res.body.token);
    }
  })
};
