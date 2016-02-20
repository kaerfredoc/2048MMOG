vertx.eventBus().consumer("cbweb.validation", function(message) {
  console.log(message);
})

console.log("Board engine started");
