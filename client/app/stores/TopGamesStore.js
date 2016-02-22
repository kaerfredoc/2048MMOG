import alt from '../libs/alt';
import BusRoutes from '../libs/vertx-bus-routes';
import VertxActions from '../actions/VertxActions';

class TopGameStore {
  eventBus;
  constructor() {
    this.state = {boards: []};
    this.bindActions(VertxActions);
  }

  onVertxReady(eventBus) {
    this.eventBus = eventBus;
  }

  onVertxReady(eventBus) {
    this.eventBus = eventBus;
    eventBus.registerHandler(
      BusRoutes.game_state,
      VertxActions.gameUpdate
    );
  }

  onGameUpdate(message) {
    this.setState({boards: message[1].body.boards});
  };
}

export default alt.createStore(TopGameStore, 'TopGameStore');