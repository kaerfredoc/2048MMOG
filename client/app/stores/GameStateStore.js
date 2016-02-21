import alt from '../libs/alt';
import BusRoutes from '../libs/vertx-bus-routes';
import VertxActions from '../actions/VertxActions';

class GameStateStore {
  constructor() {
    this.state = {serverTime: "-"};
    this.bindActions(VertxActions);
  }

  onVertxReady(eventBus) {
    console.log("eb" + eventBus);
    eventBus.registerHandler(
      BusRoutes.game_state,
      VertxActions.gameUpdate
    );
  }

  onGameUpdate(message) {
    console.log(message);
    this.setState({serverTime: message[1].body.serverTime});
  };
}

export default alt.createStore(GameStateStore, 'GameStateStore');