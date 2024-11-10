// A Class that is used as a game drawer

package utils;

import entities.bases.BaseBoss;
import entities.bases.BaseMonster;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.effect.ColorAdjust;
import javafx.scene.image.Image;
import javafx.scene.image.WritableImage;
import javafx.scene.paint.Color;
import objects.BaseObject;
import objects.Door;
import objects.Shroom;
import scenes.BaseScene;
import scenes.FightScene;

import java.util.ArrayList;
import java.util.Arrays;

import static enums.FaceDirection.RIGHT;
import static enums.States.MONSTER_DEAD;
import static utils.AnimationStatus.PIXEL_PER_TILE;
import static utils.AnimationStatus.SCALE;
import static utils.GameConstants.airTile;
import static utils.GameConstants.ROOM_TYPES;
import static utils.HelperMethods.createDoorChoices;
import static utils.LevelData.NUMBER_OF_FIGHT_ROOM_BEFORE_MARKET;
import static utils.ObjectStatus.DOOR_HEIGHT;
import static utils.ObjectStatus.SHROOM_HEIGHT;

public class CanvasDrawer extends Canvas {
    private static Image template = new Image(
            ClassLoader.getSystemResource("background/StageTemplate.png").toString());
    private BaseScene scene;

    public CanvasDrawer(BaseScene scene, double width, double height) {
        super(width, height);
        this.scene = scene;
    }

    private int getValueInThatCoor(int x, int y) {
        return scene.getLvlData()[y][x];
    }

    private int getXCoor(int x, int y) {
        int value = getValueInThatCoor(x, y);
        if (airTile.contains(value)) {
            return 11;
        }
        return value % 12;
    }

    private int getYCoor(int x, int y) {
        int value = getValueInThatCoor(x, y);
        if (airTile.contains(value)) {
            return 0;
        }
        return value / 12;
    }

    public void drawBackground(GraphicsContext gc) {
        for (int y = 0; y < scene.getLvlData().length - 1; y++) {
            for (int x = 0; x < scene.getLvlData()[0].length; x++) {
                if (scene.getLvlData()[y][x] == 99 || scene.getLvlData()[y][x] == 98) {
                    // Winning Background
                    Image waveClearBg = new Image(
                            ClassLoader.getSystemResource(
                                    "background/WaveClearBg.png").toString());
                    gc.drawImage(waveClearBg,
                            x * PIXEL_PER_TILE, (y + 1) * PIXEL_PER_TILE - waveClearBg.getHeight() * SCALE,
                            waveClearBg.getWidth() * SCALE, waveClearBg.getHeight() * SCALE);
                    if (scene instanceof FightScene) {
                        ArrayList<String> randomRoomTypes = new ArrayList<>(ROOM_TYPES);
                        randomRoomTypes.remove(((FightScene) scene).getRoomType());
                        drawDoors(randomRoomTypes, x, y);
                    }
                } else if (scene.getLvlData()[y][x] == 97) {
                    BaseObject shroom = new Shroom(scene.getPlayer(), x * PIXEL_PER_TILE,
                            (y + 1) * PIXEL_PER_TILE - SHROOM_HEIGHT * SCALE, RIGHT);
                    scene.getChildren().add(shroom.getAnimation());
                    scene.getPlayer().getObjectsInStage().add(shroom);
                } else {
                    ColorAdjust colorAdjust = new ColorAdjust();
                    if (scene.getLvlData()[scene.getLvlData().length - 1][1] == 3) {
                        colorAdjust.setHue(0.24);
                        colorAdjust.setSaturation(0.1);

                    } else if (scene.getLvlData()[scene.getLvlData().length - 1][1] == 4) {
                        colorAdjust.setHue(-0.76);
                        colorAdjust.setSaturation(0.16);
                    }
                    WritableImage croppedImage = new WritableImage(template.getPixelReader(),
                            getXCoor(x, y) * 96, getYCoor(x, y) * 96,
                            96, 96);

                    gc.drawImage(croppedImage, x * PIXEL_PER_TILE, y * PIXEL_PER_TILE, PIXEL_PER_TILE, PIXEL_PER_TILE);
                    gc.setEffect(colorAdjust);
                }
            }
        }
    }

    private void drawDoors(ArrayList<String> randomRoomTypes, int x, int y) {
        String firstDoorType, secondDoorType;
        if (FightScene.getNumberOfFightScene() % NUMBER_OF_FIGHT_ROOM_BEFORE_MARKET != 0) {
            ArrayList<String> chosenDoorTypes = createDoorChoices(randomRoomTypes, 2);
            firstDoorType = chosenDoorTypes.get(0);
            secondDoorType = chosenDoorTypes.get(1);
        } else {
            firstDoorType = "Market";
            secondDoorType = "Broken";
        }
        Door firstDoor = new Door(scene.getPlayer(), (x + 1.2) * PIXEL_PER_TILE,
                (y + 1) * PIXEL_PER_TILE - DOOR_HEIGHT * SCALE, RIGHT, firstDoorType);
        Door secondDoor = new Door(scene.getPlayer(), (x + 7.6) * PIXEL_PER_TILE,
                (y + 1) * PIXEL_PER_TILE - DOOR_HEIGHT * SCALE, RIGHT, secondDoorType);
        ArrayList<Door> doors = new ArrayList<>(Arrays.asList(firstDoor, secondDoor));
        scene.getChildren().addAll(firstDoor.getAnimation(), secondDoor.getAnimation());
        scene.getPlayer().getObjectsInStage().addAll(doors);
        scene.setDoors(doors);
    }

    public void drawMinimap(GraphicsContext gc) {
        for (int y = 0; y < scene.getLvlData().length - 1; y++) {
            for (int x = 0; x < scene.getLvlData()[0].length; x++) {
                ColorAdjust colorAdjust = new ColorAdjust();
                if (scene.getLvlData()[scene.getLvlData().length - 1][1] == 3) {
                    colorAdjust.setHue(0.24);
                    colorAdjust.setSaturation(0.1);
                } else if (scene.getLvlData()[scene.getLvlData().length - 1][1] == 4) {
                    colorAdjust.setHue(-0.76);
                    colorAdjust.setSaturation(0.16);
                }
                WritableImage croppedImage = new WritableImage(template.getPixelReader(),
                        getXCoor(x, y) * 96, getYCoor(x, y) * 96,
                        96, 96);

                gc.drawImage(croppedImage, (double) (x * PIXEL_PER_TILE) / 10 * SCALE, (double) (y * PIXEL_PER_TILE) / 10 * SCALE,
                        (double) PIXEL_PER_TILE / 10 * SCALE, (double) PIXEL_PER_TILE / 10 * SCALE);
                gc.setEffect(colorAdjust);
            }
        }
    }

    public void drawEntities(GraphicsContext gc) {
        gc.clearRect(0, 0, this.getWidth(), this.getHeight());
        drawDoorsMinimap(gc);
        drawPlayerMinimap(gc);
        drawAllMonstersMinimap(gc);
    }

    private void drawDoorsMinimap(GraphicsContext gc) {
        ArrayList<Door> doors = new ArrayList<>(scene.getDoors());
        for (Door door : doors) {
            double positionX = (int)(door.getHitBox().getTranslateX() / PIXEL_PER_TILE) + 1;
            double positionY = (int)(door.getHitBox().getTranslateY() / PIXEL_PER_TILE) + 1;
            gc.setFill(Color.GREEN);
            gc.fillRect(positionX * PIXEL_PER_TILE * SCALE / 10 , positionY * PIXEL_PER_TILE * SCALE / 10 ,
                    2 * PIXEL_PER_TILE * SCALE / 10 , 3 * PIXEL_PER_TILE * SCALE / 10 );
        }
    }

    private void drawPlayerMinimap(GraphicsContext gc) {
        double positionX = (scene.getPlayer().getHitBox().getTranslateX() / PIXEL_PER_TILE);
        double positionY = (scene.getPlayer().getHitBox().getTranslateY() / PIXEL_PER_TILE);

        gc.setFill(Color.YELLOW);
        gc.fillRect((positionX * PIXEL_PER_TILE) / 10 * SCALE, (positionY * PIXEL_PER_TILE) / 10 * SCALE,
                (double) PIXEL_PER_TILE / 10 * SCALE, (double) PIXEL_PER_TILE / 10 * SCALE);
    }

    private void drawAllMonstersMinimap(GraphicsContext gc) {
        ArrayList<BaseMonster> allMonsters = new ArrayList<>(scene.getPlayer().getMonstersInStage());
        for (BaseMonster monster : allMonsters) {
            if (scene.getPlayer().getMonstersInStage().contains(monster)) {
                if (monster.getAnimation().getState() != MONSTER_DEAD) {
                    double positionX = (monster.getHitBox().getTranslateX() / PIXEL_PER_TILE);
                    double positionY = (monster.getHitBox().getTranslateY() / PIXEL_PER_TILE);
                    gc.setFill(Color.RED);
                    if (monster instanceof BaseBoss) {
                        gc.fillRect((positionX * PIXEL_PER_TILE) / 10 * SCALE, (positionY * PIXEL_PER_TILE) / 10 * SCALE,
                                (double) PIXEL_PER_TILE / 5 * SCALE, (double) PIXEL_PER_TILE / 5 * SCALE);
                    } else {
                        gc.fillRect((positionX * PIXEL_PER_TILE) / 10 * SCALE, (positionY * PIXEL_PER_TILE) / 10 * SCALE,
                                (double) PIXEL_PER_TILE / 10 * SCALE, (double) PIXEL_PER_TILE / 10 * SCALE);
                    }
                }
            }
        }
    }
}



