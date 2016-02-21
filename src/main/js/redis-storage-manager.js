
// Initialize from game token?
function RedisStorageManager() {
  // TODO: Initialize from Redis?
  this.gameState = null;
  this.bestScore = null;
}

RedisStorageManager.prototype.clearGameState = function() {
  // TODO: Save to Redis
  this.gameState = null;
};

RedisStorageManager.prototype.setGameState = function(gameState) {
  // TODO: Save to Redis
  this.gameState = gameState;
};

RedisStorageManager.prototype.getGameState = function() {
  return this.gameState
};

RedisStorageManager.prototype.setBestScore = function() {
  // TODO: Save to Redis
  this.bestScore = bestScore;
};
