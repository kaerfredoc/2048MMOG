import alt from '../libs/alt';

import BusRoutes from '../libs/vertx-bus-routes';

import VertxActions from '../actions/VertxActions';

class GameStateStore {
  constructor() {
    var self = this;

    this.state = {serverTime: "-"};

    this.bindListeners({
      handleVertxReady: VertxActions.vertxReady,
      handleGameStateUpdate: VertxActions.gameUpdate
    });
  }

  handleVertxReady(eventBus) {
    console.log("eb" + eventBus);
    eventBus.registerHandler(
      BusRoutes.game_state,
      VertxActions.gameUpdate
    );
  }

  handleGameStateUpdate(message) {
    console.log(message);
    this.setState({serverTime: message.systemTime});
  };
}

export default alt.createStore(GameStateStore, 'GameStateStore');