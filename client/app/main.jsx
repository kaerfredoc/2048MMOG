import alt from './libs/alt';
import makeFinalStore from 'alt/utils/makeFinalStore';
import React from 'react';
import App from './views/App';

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
VertxActions.doVertxConnect();