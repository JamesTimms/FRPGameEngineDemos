package MiscExamples;

import org.engineFRP.FRP.FRPKeyboard;
import org.engineFRP.maths.Vector3f;
import sodium.Cell;
import sodium.Stream;
import sodium.StreamSink;

import java.lang.Boolean;import static org.lwjgl.glfw.GLFW.*;

/**
 * Created by TekMaTek on 24/04/2015.
 */
public class CameraExample {

    public static final StreamSink<GameState> curState = new StreamSink<>();
    private static GameState currentState = GameState.OTHER;

    public static Stream<Vector3f> movement() {
        final float MOVE_AMOUNT = 1.0f;
        return FRPKeyboard.keyEvent
                .gate(isMode(GameState.PLAY))
                .filter(key -> FRPKeyboard.isArrowKeyPressed(key.key))
                .map(key -> {//Play state movement for camera
                    switch(key.key) {
                        case (GLFW_KEY_RIGHT):
                            return new Vector3f(-MOVE_AMOUNT, 0.0f, 0.0f);
                        case (GLFW_KEY_LEFT):
                            return new Vector3f(MOVE_AMOUNT, 0.0f, 0.0f);
                        case (GLFW_KEY_UP):
                            return new Vector3f(0.0f, -MOVE_AMOUNT, 0.0f);
                        case (GLFW_KEY_DOWN):
                            return new Vector3f(0.0f, MOVE_AMOUNT, 0.0f);
                        default:
                            assert false;//This shouldn't be called.
                            return Vector3f.ZERO;
                    }
                });
    }

    public static void changeState(GameState newState) {
        currentState = newState;//this can be implemented without the currentState.
        curState.send(newState);
    }

    public static Cell<Boolean> isMode(GameState desiredState) {
        return curState
                .map(gameState -> gameState == desiredState)
                .hold(currentState == desiredState);
    }

    public enum GameState {
        PLAY,
        CUT_SCENE,
        OTHER
    }
}