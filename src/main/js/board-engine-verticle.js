vertx.eventBus().consumer("org.mmog2048", function(message) {
  console.log("logging from js " + JSON.stringify(message));
});

console.log("Board engine started");
