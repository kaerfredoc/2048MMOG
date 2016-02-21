import alt from '../libs/alt';
import BusRoutes from '../libs/vertx-bus-routes';
import VertxActions from '../actions/VertxActions';

class StatusStore {
  constructor() {
    this.state = {gameStatus: "Welcome to the 2048 Context"};
    this.bindActions(VertxActions);
  }

  onVertxReady(eventBus) {
    eventBus.registerHandler(
      BusRoutes.status_message,
      VertxActions.statusUpdate
    );
  }

  onStatusUpdate(message) {
    this.setState({gameStatus: message[1].body});
  };
}

export default alt.createStore(StatusStore, 'StatusStore');