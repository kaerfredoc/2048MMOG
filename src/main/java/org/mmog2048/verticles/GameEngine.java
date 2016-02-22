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
import java.util.stream.Collectors;

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

    private static List<Tile> leftInternal(JsonObject boardInfo) {
        List<Tile> myTiles = getMyTiles(boardInfo);
        boolean needAddTile = false;
        for (int i = 0; i < 4; i++) {
            List<Tile> line = getLine(i, myTiles);
            List<Tile> merged = mergeLine(moveLine(line));
            setLine(i, merged, myTiles);
            if (!needAddTile && !compare(line, merged)) {
                needAddTile = true;
            }
        }
        if (needAddTile) {
            addTile(myTiles);
        }
        boardInfo.put("tiles",new JsonArray(myTiles));
        return myTiles;
    }

    private static void up(final JsonObject boardInfo, final Handler<JsonObject> handler) {
        List<Tile> myTiles = getMyTiles(boardInfo);
        myTiles = rotate(90,myTiles);
        leftInternal(boardInfo);
        myTiles = rotate(270,myTiles);
        boardInfo.put("tiles",new JsonArray(myTiles));
        handler.handle(boardInfo);
    }

    private static void right(final JsonObject boardInfo, final Handler<JsonObject> handler) {
        List<Tile> myTiles = getMyTiles(boardInfo);
        myTiles = rotate(180,myTiles);
        leftInternal(boardInfo);
        myTiles = rotate(180,myTiles);
        boardInfo.put("tiles",new JsonArray(myTiles));
        handler.handle(boardInfo);
    }

    private static void down(final JsonObject boardInfo, final Handler<JsonObject> handler) {
        List<Tile> myTiles = getMyTiles(boardInfo);
        myTiles = rotate(90,myTiles);
        leftInternal(boardInfo);
        myTiles = rotate(270,myTiles);
        boardInfo.put("tiles",new JsonArray(myTiles));
        handler.handle(boardInfo);
    }

    private static List<Tile> getMyTiles(JsonObject boardInfo) {
        JsonArray tiles = boardInfo.getJsonArray("tiles");
        return tiles.getList();
    }

    private static List<Tile> rotate(int angle, List<Tile> myTiles) {
        List<Tile> newTiles = new ArrayList<>(16);
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
                newTiles.set((newX) + (newY) * 4,tileAt(x, y, myTiles));
            }
        }
        return newTiles;
    }

    private static List<Tile> moveLine(List<Tile> oldLine) {
        LinkedList<Tile> l = new LinkedList<>();
        for (int i = 0; i < 4; i++) {
            if (!oldLine.get(i).isEmpty())
                l.addLast(oldLine.get(i));
        }
        if (l.size() == 0) {
            return oldLine;
        } else {
            List<Tile> newLine = new ArrayList<>(4);
            ensureSize(l, 4);
            for (int i = 0; i < 4; i++) {
                newLine.set(i,l.removeFirst());
            }
            return newLine;
        }
    }

    private static List<Tile> mergeLine(List<Tile> oldLine) {
        List<Tile> list = new LinkedList<>();
        for (int i = 0; i < 4 && !oldLine.get(i).isEmpty(); i++) {
            int num = oldLine.get(i).getValue();
            if (i < 3 && oldLine.get(i).getValue() == oldLine.get(i+1).getValue()) {
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
            return new ArrayList<>(4);
        }
    }

    private static List<Tile> getLine(int index, List<Tile> myTiles) {
        List<Tile> result = new ArrayList<>(4);
        for (int i = 0; i < 4; i++) {
            result.set(i,tileAt(i, index, myTiles));
        }
        return result;
    }

    private static void ensureSize(java.util.List<Tile> l, int s) {
        while (l.size() != s) {
            l.add(new Tile());
        }
    }

    private static Tile tileAt(int x, int y, List<Tile> myTiles) {
        return myTiles.get(x + y * 4);
    }

    private static void setLine(int index, List<Tile> re, List<Tile> myTiles) {
        System.arraycopy(re, 0, myTiles, index * 4, 4);
    }

    private static void addTile(List<Tile> myTiles) {
        List<Tile> list = availableSpace(myTiles);
        if (!list.isEmpty()) {
            int index = (int) (Math.random() * list.size()) % list.size();
            Tile emptyTime = list.get(index);
            emptyTime.setValue(Math.random() < 0.9 ? 2 : 4);
        }
    }


    private static List<Tile> availableSpace(List<Tile> myTiles) {
        final List<Tile> list = new ArrayList<>(16);
        list.addAll(myTiles.stream().filter(t -> t.isEmpty()).collect(Collectors.toList()));
        return list;
    }

    private static boolean compare(List<Tile> line1, List<Tile> line2) {
        if (line1 == line2) {
            return true;
        } else if (line1.size() != line2.size()) {
            return false;
        }

        for (int i = 0; i < line1.size(); i++) {
            if (line1.get(i).getValue() != line2.get(i).getValue()) {
                return false;
            }
        }
        return true;
    }

}