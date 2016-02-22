
// TODO: Initialize with game token?
function WebsocketInputManager() {
  var eb = vertx.eventBus();
  var self = this;
  this.events = {};
  ["move", "restart", "keepPlaying"].forEach(function(channelName) {
    eb.consumer(channelName, function(message) {
      self.emit(channelName, message.body());
    });
  });
}

WebsocketInputManager.prototype.on = function (event, callback) {
  if (!this.events[event]) {
    this.events[event] = [];
  }
  this.events[event].push(callback);
};

WebsocketInputManager.prototype.emit = function (event, data) {
  var callbacks = this.events[event];
  if (callbacks) {
    callbacks.forEach(function (callback) {
      callback(data);
    });
  }
};

module.exports = WebsocketInputManager;
