import alt from '../libs/alt';
import VertxActions from '../actions/VertxActions';

var getEventBus = () => {
  const Vertx = require('vertx3-eventbus-client');
  return (new Vertx('http://localhost:8080/eventbus'));
};

class VertxStore {

  constructor() {
    this.eventBus = null;
    this.eventBusStatus = 'CLOSED';
    this.serviceActive = true;
    this.connectProgress = 4;
    this.connectTries = 0;
    this.connectInterval = 5;
    this.tryLimit = 8;

    this.bindActions(VertxActions);

    window.setTimeout(
      this.connectEventBus,
      1000, this
    );
  }

  connectEventBus(selfObj) {
    var self = selfObj ? selfObj : this;
    var connectProgress = self.connectProgress ? self.connectProgress : 0;
    var indicator = self.connectInterval - connectProgress;

    connectProgress += 1;

    self.setState(
      {
        connectProgress: connectProgress
      }
    );

    if (connectProgress >= self.connectInterval) {

      const EventBus = getEventBus();

      EventBus.onopen = () => {
        console.log("EVENT BUS OPEN");
        var newState = {
          eventBus: EventBus,
          connectProgress: 0,
          connectTries: 0,
          serviceActive: true
        };
        self.setState(newState);

        VertxActions.vertxReady(EventBus);
      };

      EventBus.onclose = () => {
        self.setState({eventBusStatus: 'CLOSED'});
        VertxActions.vertxUnready(EventBus);
      };

      EventBus.onerror = (err) => {
        console.error("EVENT BUS ERROR: " + JSON.stringify(err));
        VertxActions.vertxError(err);
      };

      var newState = {
        eventBus: EventBus,
        connectProgress: 0.0,
        connectTries: self.connectTries + 1
      };
      self.setState(newState);
    } else if (self.connectTries < self.tryLimit) {
      window.setTimeout(
        self.connectEventBus,
        1000, self
      );
    } else {
      self.setState({
        connectProgress: 0,
        connectTries: 0,
        serviceActive: false
      })
    }
  }

  onVertxReady(eventBus) {
    this.setState({eventBusStatus: 'OPEN'});
  }

  onVertxUnready(eventBus) {
    console.log('not ready');
    this.setState({connectProgress: 0});
  }

  onVertxError(err) {
    console.log("VERTX ERROR");
  }
}

export default alt.createStore(VertxStore, 'VertxStore');