
module.exports = function(eb, botName) {
  var token;
  //Every x seconds try to move...
  var directions = ['U', 'D', 'L', 'R'];
  var moveAvailable = true;
  console.log(botName);

  eb.send("org.mmog2048:register", {"name": botName}, function(err, res) {
    console.log(JSON.stringify(err));
    console.log(JSON.stringify(res));
    if (res) {
      console.log("Received token: " + res.body.token);
      console.log("Received board: " + res.body.board);
      token = res.body.token;
    }
  });


  var makeAMove = function() {
    var move = directions[Math.round(Math.random()*3)];
    // TODO: Consider any edge cases where this deadlocks
    if (moveAvailable && token) {
      moveAvailable = false;
      eb.send("org.mmog2048:move:"+token, {"move": move}, function(err, res) {
        if (res) {
          moveAvailable = true;
          console.log("Received newA board: " + res.body.board);  }
      });
    }
  };

  var scheduleATurn = function(milliseconds) {
    setTimeout(function() {
      nextTurnTime = (Math.random() * 15000) + 500;
      scheduleATurn(nextTurnTime);
    }, milliseconds);
  };

}



