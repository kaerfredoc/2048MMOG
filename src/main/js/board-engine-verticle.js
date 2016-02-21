var eb = vertx.eventBus();

eb.consumer("org.mmog2048", function(message) {
  console.log("logging from js " + JSON.stringify(message));
});

console.log("Board engine started");

eb.consumer("adam.test", function(message) {
  console.log("boom " + JSON.stringify(message.body()));
});
