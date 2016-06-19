module.exports = function (eb, botName) {
  //Every x seconds try to move...
  var directions = ['U', 'D', 'L', 'R'];
  // console.log(botName);

  eb.send("org.mmog2048:register", {"name": botName}, function (res) {
    var token;
    var moveAvailable = true;

    if (res) {
      // console.log("Received token: " + res.body().token);
      // console.log("Received board: " + res.body().board);
      token = res.body().token;
    }

    var scheduleATurn = function (milliseconds) {
      setTimeout(function () {
        nextTurnTime = (Math.random() * 3000) + 500;
        makeAMove();
        scheduleATurn(nextTurnTime);
      }, milliseconds);
    };
    scheduleATurn(10);

    var makeAMove = function () {
      if (moveAvailable && token) {
        var move = directions[Math.round(Math.random() * 3)];
        // TODO: Consider any edge cases where this deadlocks
        moveAvailable = false;
        //console.log('sending move ' + move);
        eb.send("org.mmog2048:move:" + token, {"move": move}, function (res, err) {
          moveAvailable = true;
          if (res) {
            //console.log("Received newA board: " + JSON.stringify(res.body().board));
          }
        });
      }
    };
  })
};




