var eb = vertx.eventBus();
var gameBot = require('./game-bot');

console.log("Board engine started");

var GameManager = require('./game-manager');
var InputManager = require('./websocket-input-manager');
var StorageManager = require('./redis-storage-manager');

var gameManager = new GameManager(4, InputManager, StorageManager);

for (var x=0; x<100; x++) {
  gameBot(eb, "Bot" + x.toString());
}
