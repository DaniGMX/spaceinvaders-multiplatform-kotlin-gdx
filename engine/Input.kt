package architecture.engine

import architecture.engine.structs.GameObjectInput
import com.badlogic.gdx.Gdx
import com.badlogic.gdx.InputProcessor
import com.badlogic.gdx.input.GestureDetector
import com.badlogic.gdx.math.Vector2

/**
 * All the listeners
 */
class Input : InputProcessor, GestureDetector.GestureListener {


    companion object {
        /**
         * This is static because we share it across the inputs of the engine, like the InputProcessor or the GestureListener one.
         */
        var subscribers: MutableList<GameObjectInput> = mutableListOf()
        val input: Input
        get() = World.input
    }

    fun subscribe(g: GameObjectInput) {
        subscribers.add(g)
    }

    fun unsubscribe(g: GameObjectInput) {
        subscribers.remove(g)
    }

    override fun touchUp(screenX: Int, screenY: Int, pointer: Int, button: Int): Boolean {
        subscribers.forEach { g -> g.touchUp(screenX, screenY, pointer, button) }
        return false
    }

    override fun mouseMoved(screenX: Int, screenY: Int): Boolean {
        subscribers.forEach { g -> g.mouseMoved(screenX, screenY) }
        return false
    }

    override fun keyTyped(character: Char): Boolean {
        // todo: Put this on every event
        val subscribersSafe = subscribers.toMutableList()
        subscribersSafe.forEach { g -> g.keyTyped(character) }
        subscribers = subscribersSafe.toMutableList()
        return false
    }

    override fun scrolled(amount: Int): Boolean {
        subscribers.forEach { g -> g.scrolled(amount) }
        return false
    }

    override fun keyUp(keycode: Int): Boolean {
        subscribers.forEach { g -> g.keyUp(keycode) }
        return false
    }

    override fun touchDragged(screenX: Int, screenY: Int, pointer: Int): Boolean {
        subscribers.forEach { g -> g.touchDragged(screenX, screenY, pointer) }
        return false
    }

    override fun keyDown(keycode: Int): Boolean {
        val subscribersSafe = subscribers.toMutableList()
        subscribersSafe.forEach { g -> g.keyDown(keycode) }
        subscribers = subscribersSafe.toMutableList()
        return false
    }

    override fun touchDown(screenX: Int, screenY: Int, pointer: Int, button: Int): Boolean {
        subscribers.forEach { g -> g.touchDown(screenX, screenY, pointer, button) }
        return false
    }

    override fun fling(velocityX: Float, velocityY: Float, button: Int): Boolean {
        subscribers.forEach { g -> g.fling(velocityX, velocityY, button) }
        return false
    }

    override fun zoom(initialDistance: Float, distance: Float): Boolean {
       subscribers.forEach { g -> g.zoom(initialDistance, distance) }
        return false
    }

    override fun pan(x: Float, y: Float, deltaX: Float, deltaY: Float): Boolean {
        subscribers.forEach { g -> g.pan(x, y, deltaX, deltaY) }
        return false
    }

    override fun pinchStop() {
        subscribers.forEach { g -> g.pinchStop() } //To change body of created functions use File | Settings | File Templates.
    }

    override fun tap(x: Float, y: Float, count: Int, button: Int): Boolean {
        subscribers.forEach { g -> g.tap(x, y, count, button) }
        return false
    }

    override fun panStop(x: Float, y: Float, pointer: Int, button: Int): Boolean {
        subscribers.forEach { g -> g.panStop(x, y, pointer, button) }
        return false
    }

    override fun longPress(x: Float, y: Float): Boolean {
        subscribers.forEach { g -> g.longPress(x, y) }
        return false
    }

    override fun touchDown(x: Float, y: Float, pointer: Int, button: Int): Boolean {
        subscribers.forEach { g -> g.touchDown(x, y, pointer, button) }
        return false
    }

    override fun pinch(initialPointer1: Vector2?, initialPointer2: Vector2?, pointer1: Vector2?, pointer2: Vector2?): Boolean {
        subscribers.forEach { g -> g.pinch(initialPointer1, initialPointer2, pointer1, pointer2) }
        return false
    }
}