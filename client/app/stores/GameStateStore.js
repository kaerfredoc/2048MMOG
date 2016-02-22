import alt from '../libs/alt';
import BusRoutes from '../libs/vertx-bus-routes';
import VertxActions from '../actions/VertxActions';

class GameStateStore {
  eventBus;
  constructor() {
    this.state = {serverTime: "-", "token": "-", "board": []};
    this.bindActions(VertxActions);
  }

  onVertxReady(eventBus) {
    this.eventBus = eventBus;
    eventBus.registerHandler(
      BusRoutes.game_state,
      VertxActions.gameUpdate
    );
  }

  onRegister(name) {
    this.eventBus.send(BusRoutes.register, {"name": name}, function (err, res) {
      if (res) {
        this.setState({token: res.body.token, board: res.body.board});
      }
    }.bind(this));
  };

  onGameUpdate(message) {
    this.setState({boardState: message[1].body.board});
  };
}

export default alt.createStore(GameStateStore, 'GameStateStore');