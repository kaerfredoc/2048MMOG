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

  var map = {
    38: "U", // Up
    39: "R", // Right
    40: "D", // Down
    37: "L", // Left
    75: "U", // Vim up
    76: "R", // Vim right
    74: "D", // Vim down
    72: "L", // Vim left
    87: "U", // W
    68: "R", // D
    83: "D", // S
    65: "L"  // A
  };

  // Respond to direction keys
  document.addEventListener("keydown", function (event) {
    var modifiers = event.altKey || event.ctrlKey || event.metaKey ||
      event.shiftKey;
    var mapped = map[event.which];

    if (!modifiers) {
      if (mapped !== undefined) {
        event.preventDefault();
        VertxActions.move(mapped)
      }
    }

    // R key restarts the game
    if (!modifiers && event.which === 82) {
      self.restart.call(self, event);
    }
  });

  ReactDOM.render(<App />, app);
}

main();
