// This thread controls the move actions of all entities and objects

package threads;

import animating.Animation;
import entities.bases.BaseMonster;
import entities.player.Player;
import javafx.application.Platform;
import objects.BaseObject;
import scenes.BaseScene;

import java.util.ArrayList;

import static utils.AnimationStatus.MOVE_FRAME;

public class MovingThread extends Thread {
    private BaseScene scene;
    private boolean isRunning = true;
    private Player player;
    public MovingThread(BaseScene scene) {
        this.scene = scene;
        this.player = scene.getPlayer();
    }

    public void stopThread(){
        isRunning = false;
    }

    @Override
    public void run() {
        try {
            while (isRunning) {
                player.move(scene);
                moveAllMonsters();
                moveAllObjects();
                scene.updateUI();
                sleep(1000 / MOVE_FRAME);
            }
        } catch (InterruptedException ignored) {}
    }

    private void moveAllMonsters() {
        ArrayList<BaseMonster> allMonsters = new ArrayList<>(player.getMonstersInStage());
        for (BaseMonster monster : allMonsters) {
            if (player.getMonstersInStage().contains(monster)) {
                monster.move(scene);
            }
        }
    }

    private void moveAllObjects() {
        ArrayList<BaseObject> objectsToRemove = new ArrayList<>();
        ArrayList<Animation> objectsAnimationToRemove = new ArrayList<>();
        ArrayList<BaseObject> allObjects = new ArrayList<>(player.getObjectsInStage());
        for (BaseObject object : allObjects) {
            if (player.getObjectsInStage().contains(object)) {
                object.move(scene);
            }
        }
        removeObjects(objectsToRemove, objectsAnimationToRemove);
    }

    private void removeObjects(ArrayList<BaseObject> objectsToRemove, ArrayList<Animation> objectsAnimationToRemove) {
        ArrayList<BaseObject> allObjects = new ArrayList<>(player.getObjectsInStage());
        for (BaseObject object : allObjects){
            if (player.getObjectsInStage().contains(object)) {
                if (object.willRemove()) {
                    objectsToRemove.add(object);
                    objectsAnimationToRemove.add(object.getAnimation());
                }
            }
        }
        player.getObjectsInStage().removeAll(objectsToRemove);
        Platform.runLater(() -> scene.getChildren().removeAll(objectsAnimationToRemove));
    }
}
