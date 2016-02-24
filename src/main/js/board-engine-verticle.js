var eb = vertx.eventBus();
var gameBot = require('./game-bot');

eb.consumer("org.mmog2048", function(message) {
  console.log("logging from js " + JSON.stringify(message.body()));
});

console.log("Board engine started");

var GameManager = require('./game-manager');
var InputManager = require('./websocket-input-manager');
var StorageManager = require('./redis-storage-manager');

var gameManager = new GameManager(4, InputManager, StorageManager);

eb.consumer("adam.test", function(message) {
  console.log("boom " + JSON.stringify(message.body()));
});

for (var x=0; x<100; x++) {
  gameBot(eb, "Bot" + x.toString());
}
