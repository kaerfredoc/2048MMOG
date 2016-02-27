import alt from '../libs/alt';
import BusRoutes from '../libs/vertx-bus-routes';
import VertxActions from '../actions/VertxActions';

var KEY_MAP = {
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

class GameStateStore {
  eventBus;
  constructor() {
    this.state = {serverTime: "-", "token": "-"};
    this.bindActions(VertxActions);
  }

  onVertxReady(eventBus) {
    this.eventBus = eventBus;
  }

  onRegister(name) {
    this.eventBus.send(BusRoutes.register, {"name": name}, function (err, res) {
      if (res) {
        this.setState({token: res.body.token, board: res.body.board});

        // Respond to direction keys
        document.addEventListener("keydown", function (event) {
          var modifiers = event.altKey || event.ctrlKey || event.metaKey ||
            event.shiftKey;
          var mapped = KEY_MAP[event.which];

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
      }
    }.bind(this));
  };

  onMove(move) {
    this.eventBus.send(BusRoutes.move + ":" +this.state.token, {"move": move}, function (err, res) {
      if (res) {
        this.setState({board: res.body.board});
      }
    }.bind(this));
  };
}

export default alt.createStore(GameStateStore, 'GameStateStore');