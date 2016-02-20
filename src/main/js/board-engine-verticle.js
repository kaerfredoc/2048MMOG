vertx.eventBus().consumer("cbweb.validation", (message) => {
  console.log(message)
});

console.log("Board engine started");
