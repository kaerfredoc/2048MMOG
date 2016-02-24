import alt from './libs/alt';
import makeFinalStore from 'alt-utils/lib/makeFinalStore';
import React from 'react';
import ReactDOM from 'react-dom';
import App from './views/App';
import VertxActions from './actions/VertxActions.js';
import VertxStore from './stores/VertxStore';
require("bootstrap-webpack!./bootstrap.config.js");

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
