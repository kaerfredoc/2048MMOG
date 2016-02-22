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
  }

  onRegister(name) {
    this.eventBus.send(BusRoutes.register, {"name": name}, function (err, res) {
      if (res) {
        this.setState({token: res.body.token, board: res.body.board});
      }
    }.bind(this));
  };

  onMove(move) {
    this.eventBus.send(BusRoutes.move + ":" +this.state.token, {"move": move}, function (err, res) {
      if (res) {
        this.setState({token: this.state.token, board: res.body.board});
      }
    }.bind(this));
  };
}

export default alt.createStore(GameStateStore, 'GameStateStore');