package architecture.engine

import com.my.architecture.engine.structs.GameObject

class Animation(private val gameObject: GameObject, private val start: Int, private val end: Int, private val framesBetween: Int, private val repeat: Boolean) {
    private var running: Boolean = false
    private var currentFrame: Int = 0

    public var finished = false


    fun update() {
        if (!running) {
            currentFrame = 0
            gameObject.spriteIndex = start
        }

        if (currentFrame >= framesBetween) {
            gameObject.spriteIndex++
            currentFrame = 0
        }

        if (gameObject.spriteIndex > end && repeat) {
            currentFrame = 0
            gameObject.spriteIndex = currentFrame
            return
        }

        if (gameObject.spriteIndex > end) {
            gameObject.spriteIndex = end
            finished = true
            return
        }
        currentFrame++
    }

    fun start() {
        running = true
        currentFrame = 0
        gameObject.spriteIndex = start
    }

    fun end() {
        running = false
        currentFrame = 0
        gameObject.spriteIndex = start
    }

    fun dispose() {
        Animator.destroy(this)
    }

}

class Animator {

    companion object {
        var animations: MutableList<Animation> = mutableListOf()
        fun animate(gameObject: GameObject, start: Int, end: Int, framesBetween: Int, repeat: Boolean): Animation {
            animations.add(Animation(gameObject, start, end, framesBetween, repeat))
            return animations.last()
        }

        fun destroy(animation: Animation) {
            animations.remove(animation)
        }
    }

}