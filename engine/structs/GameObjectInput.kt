package architecture.engine.structs

import architecture.engine.Input
import com.badlogic.gdx.InputProcessor
import com.badlogic.gdx.graphics.g2d.Sprite
import com.badlogic.gdx.input.GestureDetector
import com.badlogic.gdx.math.Vector2
import com.my.architecture.engine.structs.GameObject

/**
 * Base class for gameObjects that need to listen to input
 * If you're gonna override start or onDestroy, call the base class.
 */
open class GameObjectInput(spritesSet: Array<Sprite> = arrayOf(), w: Float = 500.0f, h: Float = 500.0f, position: Vector2 = Vector2(100.0f, -100.0f)) : GameObject(spritesSet, w, h, position) {
    /**
     * Will listen to touchUp events
     */
    open fun touchUp(screenX: Int, screenY: Int, pointer: Int, button: Int): Boolean {
        return false
    }

    /**
     * Will listen to mouseMoved events
     */
    open fun mouseMoved(screenX: Int, screenY: Int): Boolean {
        return false
    }

    open fun keyTyped(character: Char): Boolean {
        return false
    }

    open fun scrolled(amount: Int): Boolean {
        return false
    }

    open fun keyUp(keycode: Int): Boolean {
        return false
    }

    open fun touchDragged(screenX: Int, screenY: Int, pointer: Int): Boolean {
        return false
    }

    open fun keyDown(keycode: Int): Boolean {
        return false
    }

    open fun touchDown(screenX: Int, screenY: Int, pointer: Int, button: Int): Boolean {
        return false
    }

    override fun start() {
        Input.input.subscribe(this)
    }

    override fun onDestroy() {
        Input.input.unsubscribe(this)
    }

    open fun fling(velocityX: Float, velocityY: Float, button: Int): Boolean {
        return false
    }

    open fun zoom(initialDistance: Float, distance: Float): Boolean {
        return false
    }

    open fun pan(x: Float, y: Float, deltaX: Float, deltaY: Float): Boolean {
        return false
    }

    open fun pinchStop() {
    }

    open fun tap(x: Float, y: Float, count: Int, button: Int): Boolean {
        return false
    }

    open fun panStop(x: Float, y: Float, pointer: Int, button: Int): Boolean {
        return false
    }

    open fun longPress(x: Float, y: Float): Boolean {
        return false
    }

    open fun touchDown(x: Float, y: Float, pointer: Int, button: Int): Boolean {
        return false
    }

    open fun pinch(initialPointer1: Vector2?, initialPointer2: Vector2?, pointer1: Vector2?, pointer2: Vector2?): Boolean {
        return false
    }

    open fun touchUpJoystick(direction: Vector2, dist: Float, joystickTag: String) {
    }
}