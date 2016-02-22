package org.mmog2048.verticles;

import io.vertx.core.Handler;
import io.vertx.core.json.JsonArray;
import io.vertx.core.json.JsonObject;
import io.vertx.core.logging.Logger;
import io.vertx.core.logging.LoggerFactory;
import org.mmog2048.dao.RedisDAO;
import org.mmog2048.models.Tile;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;

public class GameEngine {
    private static final Logger LOG = LoggerFactory.getLogger(GameEngine.class);

    // https://github.com/bulenkov/2048/blob/master/src/com/bulenkov/game2048/Game2048.java


    /**
     * - Get a token & move (U/D/L/R)
     * - Pull from redis key=token
     * - Turn value into json
     * - Get board property
     * - Run move
     * - Store board back in redis w/timestamp - SET
     * - Store score in top_scores as - ZADD
     * - Return board as array - io.vertx.core.json.JsonArray
     *
     * @param token
     * @param move
     * @param redisDAO
     * @return
     */
    public static void updateGame(String token, String move, RedisDAO redisDAO, Handler<JsonArray> handler) {
        //TODO: contest needs to come from somewhere else
        String contest = "thecontest";
        redisDAO.getBoardInfo(contest, token, boardInfo -> {
            if (boardInfo == null || boardInfo.isEmpty()) {
                return;
            }
            switch (move) {
                case "L":
                    left(boardInfo, postGameInfo -> {
                        if (postGameInfo == null || postGameInfo.isEmpty()) {
                            LOG.error("postGameInfo is null or empty for contest: " + contest + " and token: " + token);
                            handler.handle(null);
                        } else {
                            redisDAO.saveBoardInfo(contest, token, postGameInfo, result -> handler.handle(result));
                        }
                    });
                    break;
                case "U":
                    up(boardInfo, postGameInfo -> {
                        if (postGameInfo == null || postGameInfo.isEmpty()) {
                            LOG.error("postGameInfo is null or empty for contest: " + contest + " and token: " + token);
                            handler.handle(null);
                        } else {
                            redisDAO.saveBoardInfo(contest, token, postGameInfo, result -> handler.handle(result));
                        }
                    });
                    break;
                case "R":
                    right(boardInfo, postGameInfo -> {
                        if (postGameInfo == null || postGameInfo.isEmpty()) {
                            LOG.error("postGameInfo is null or empty for contest: " + contest + " and token: " + token);
                            handler.handle(null);
                        } else {
                            redisDAO.saveBoardInfo(contest, token, postGameInfo, result -> handler.handle(result));
                        }
                    });
                case "D":
                    down(boardInfo, postGameInfo -> {
                        if (postGameInfo == null || postGameInfo.isEmpty()) {
                            LOG.error("postGameInfo is null or empty for contest: " + contest + " and token: " + token);
                            handler.handle(null);
                        } else {
                            redisDAO.saveBoardInfo(contest, token, postGameInfo, result -> handler.handle(result));
                        }
                    });
                    break;
                default:
                    return;
            }
        });
    }

    private static void left(final JsonObject boardInfo, final Handler<JsonObject> handler) {
        leftInternal(boardInfo);
        handler.handle(boardInfo);
    }

    private static Tile[] leftInternal(JsonObject boardInfo) {
        Tile[] myTiles = getMyTiles(boardInfo);
        boolean needAddTile = false;
        for (int i = 0; i < 4; i++) {
            Tile[] line = getLine(i, myTiles);
            Tile[] merged = mergeLine(moveLine(line));
            setLine(i, merged, myTiles);
            if (!needAddTile && !compare(line, merged)) {
                needAddTile = true;
            }
        }
        if (needAddTile) {
            addTile(myTiles);
        }
        List<Tile> currentTileState = Arrays.asList(myTiles);
        boardInfo.put("tiles",new JsonArray(currentTileState));
        return myTiles;
    }

    private static void up(final JsonObject boardInfo, final Handler<JsonObject> handler) {
        Tile[] myTiles = getMyTiles(boardInfo);
        myTiles = rotate(90,myTiles);
        leftInternal(boardInfo);
        myTiles = rotate(270,myTiles);
        List<Tile> currentTileState = Arrays.asList(myTiles);
        boardInfo.put("tiles",new JsonArray(currentTileState));
        handler.handle(boardInfo);
    }

    private static void right(final JsonObject boardInfo, final Handler<JsonObject> handler) {
        Tile[] myTiles = getMyTiles(boardInfo);
        myTiles = rotate(180,myTiles);
        leftInternal(boardInfo);
        myTiles = rotate(180,myTiles);
        List<Tile> currentTileState = Arrays.asList(myTiles);
        boardInfo.put("tiles",new JsonArray(currentTileState));
        handler.handle(boardInfo);
    }

    private static void down(final JsonObject boardInfo, final Handler<JsonObject> handler) {
        Tile[] myTiles = getMyTiles(boardInfo);
        myTiles = rotate(90,myTiles);
        leftInternal(boardInfo);
        myTiles = rotate(270,myTiles);
        List<Tile> currentTileState = Arrays.asList(myTiles);
        boardInfo.put("tiles",new JsonArray(currentTileState));
        handler.handle(boardInfo);
    }

    private static Tile[] getMyTiles(JsonObject boardInfo) {
        Tile[] newTiles = new Tile[4 * 4];
        JsonArray tiles = boardInfo.getJsonArray("tiles");
        tiles.forEach(tile -> {
//TODO: figure out how to map the tiles from boardInfo to the Tiles array
        });
        return newTiles;
    }

    private static Tile[] rotate(int angle, Tile[] myTiles) {
        Tile[] newTiles = new Tile[4 * 4];
        int offsetX = 3, offsetY = 3;
        if (angle == 90) {
            offsetY = 0;
        } else if (angle == 270) {
            offsetX = 0;
        }

        double rad = Math.toRadians(angle);
        int cos = (int) Math.cos(rad);
        int sin = (int) Math.sin(rad);
        for (int x = 0; x < 4; x++) {
            for (int y = 0; y < 4; y++) {
                int newX = (x * cos) - (y * sin) + offsetX;
                int newY = (x * sin) + (y * cos) + offsetY;
                newTiles[(newX) + (newY) * 4] = tileAt(x, y, myTiles);
            }
        }
        return newTiles;
    }

    private static Tile[] moveLine(Tile[] oldLine) {
        LinkedList<Tile> l = new LinkedList<>();
        for (int i = 0; i < 4; i++) {
            if (!oldLine[i].isEmpty())
                l.addLast(oldLine[i]);
        }
        if (l.size() == 0) {
            return oldLine;
        } else {
            Tile[] newLine = new Tile[4];
            ensureSize(l, 4);
            for (int i = 0; i < 4; i++) {
                newLine[i] = l.removeFirst();
            }
            return newLine;
        }
    }

    private static Tile[] mergeLine(Tile[] oldLine) {
        LinkedList<Tile> list = new LinkedList<>();
        for (int i = 0; i < 4 && !oldLine[i].isEmpty(); i++) {
            int num = oldLine[i].getValue();
            if (i < 3 && oldLine[i].getValue() == oldLine[i + 1].getValue()) {
                num *= 2;
                //TODO: write the score here or later?
                //myScore += num;
                int ourTarget = 2048;
                if (num == ourTarget) {
                    //TODO: when should we say we won?
                    //myWin = true;
                }
                i++;
            }
            list.add(new Tile(num));
        }
        if (list.size() == 0) {
            return oldLine;
        } else {
            ensureSize(list, 4);
            return list.toArray(new Tile[4]);
        }
    }

    private static Tile[] getLine(int index, Tile[] myTiles) {
        Tile[] result = new Tile[4];
        for (int i = 0; i < 4; i++) {
            result[i] = tileAt(i, index, myTiles);
        }
        return result;
    }

    private static void ensureSize(java.util.List<Tile> l, int s) {
        while (l.size() != s) {
            l.add(new Tile());
        }
    }

    private static Tile tileAt(int x, int y, Tile[] myTiles) {
        return myTiles[x + y * 4];
    }

    private static void setLine(int index, Tile[] re, Tile[] myTiles) {
        System.arraycopy(re, 0, myTiles, index * 4, 4);
    }

    private static void addTile(Tile[] myTiles) {
        List<Tile> list = availableSpace(myTiles);
        if (!list.isEmpty()) {
            int index = (int) (Math.random() * list.size()) % list.size();
            Tile emptyTime = list.get(index);
            emptyTime.setValue(Math.random() < 0.9 ? 2 : 4);
        }
    }


    private static List<Tile> availableSpace(Tile[] myTiles) {
        final List<Tile> list = new ArrayList<>(16);
        for (Tile t : myTiles) {
            if (t.isEmpty()) {
                list.add(t);
            }
        }
        return list;
    }

    private static boolean compare(Tile[] line1, Tile[] line2) {
        if (line1 == line2) {
            return true;
        } else if (line1.length != line2.length) {
            return false;
        }

        for (int i = 0; i < line1.length; i++) {
            if (line1[i].getValue() != line2[i].getValue()) {
                return false;
            }
        }
        return true;
    }

}