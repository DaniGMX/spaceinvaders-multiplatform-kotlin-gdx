package architecture.engine

import com.badlogic.gdx.Application
import com.badlogic.gdx.Gdx
import com.badlogic.gdx.InputMultiplexer
import com.badlogic.gdx.input.GestureDetector
import com.my.architecture.engine.structs.GameObject
import com.badlogic.gdx.math.Intersector

class World {
    // region Private
    private var currentID: Int = 0
    private var _gameObjects: MutableList<GameObject> = mutableListOf()

    // endregion

    // region Public
    val gameObjects: MutableList<GameObject>
    get() = _gameObjects
    // endregion

    private var _inputProcess: Input = Input()

    init {
        currentID = 1
    }

    companion object {

        private var currentInput = Input()
        val input: Input
        get() = currentInput

        /**
         * Current world
         */
        var world: World = World()

        /**
         * Set the current world which Game class will use in render. Use this in Game.start(), if you are gonna change a world in rendering use world.start()
         * @see Game.render
         */
        fun setCurrentWorld(w: World) {
            world = w
        }
    }

    /**
     * Starts this world and Game class will use it as the current world
     * @see Game.render
     */
    fun start() {
        val inputs: InputMultiplexer = InputMultiplexer()
        val gesture = GestureDetector(input)
        inputs.addProcessor(_inputProcess)
        Gdx.app.log("type", "Is mobile or apple!!!?")
        inputs.addProcessor(gesture)
        if (Gdx.app.type == Application.ApplicationType.Android || Gdx.app.type == Application.ApplicationType.Applet) {
            Gdx.app.log("type", "Is mobile or apple")
//            inputs.addProcessor(gesture)
        }
        Gdx.input.inputProcessor = inputs
        currentInput = _inputProcess
        world = this
    }


    /**
     * Destroys a gameObject from the world.
     * @param gameObject GameObject you want to destroy
     */
    fun destroy(gameObject: GameObject) {
        _gameObjects = _gameObjects.filter { g -> g != gameObject }.toMutableList()
        gameObject.onDestroy()
    }

    /**
     * Instantiates a gameObject to world. If this world is the same as the current world, gameObject.start() will be fired.
     * @param gameObject GameObject to instantiate
     */
    fun <T: GameObject> instantiate(gameObject: T): T{
        gameObject.instanceID = currentID++
        _gameObjects.add(gameObject)
        changedDepth()
        return gameObject
    }

    /**
     * Will store the current update() copy of gameObjects so it's safe to make alterations to the original gameObject array in updates() and onCollides() etc.
     */
    var currentCopyOfGameObjects: MutableList<GameObject> = arrayListOf()

    /**
     * Executes .update() on every instantiated gameObject in the world
     */
    fun update() {
        val dt = Gdx.graphics.deltaTime
        currentCopyOfGameObjects = _gameObjects.toMutableList()
        for (gameObject in currentCopyOfGameObjects) {
            if (!gameObject.active) continue
            if (!gameObject.initialized) {
                gameObject.start()
                gameObject.initialized = true
            }
            gameObject.update(dt)
        }
    }

    /**
     * Check if the gameObject is overlapping with another. TODO: Have a GameObject collider and optimize the loop
     * Will call onCollide on the gameObjects that it collides with
     * @param gameObject to check
     * @return if it's overlapping
     */
    fun overlaps(gameObject: GameObject): Boolean {
        var overlaps = false
        gameObject.sprite()?.setPosition(gameObject.position.x, gameObject.position.y)
        for (otherGameObject in currentCopyOfGameObjects) {
            if (otherGameObject.instanceID == gameObject.instanceID) continue
            // Reset because maybe the sprite has been compromised sharing references with other GameObjects
            otherGameObject.sprite()?.setPosition(otherGameObject.position.x, otherGameObject.position.y)
            otherGameObject.sprite()?.setSize(otherGameObject.width, otherGameObject.height)
            gameObject.sprite()?.setPosition(gameObject.position.x, gameObject.position.y)
            if (!Intersector.overlaps(otherGameObject.sprite()?.boundingRectangle, gameObject.sprite()?.boundingRectangle)) continue
            gameObject.onCollide(otherGameObject)
            otherGameObject.onCollide(gameObject)
            overlaps = true
        }
        return overlaps
    }

    /**
     * Finds all the instantiated gameObjects in the current World
     * @return gameObjects
     */
    inline fun <reified T: GameObject> findGameObjects(): Array<T> {
        val gameObjectsToReturn = mutableListOf<T>()
        for (gameObject in gameObjects) {
            if (gameObject is T) {
                gameObjectsToReturn.add(gameObject)
            }
        }
        return gameObjectsToReturn.toTypedArray()
    }

    fun changedDepth() {
        world.gameObjects.sortBy { gameObject -> gameObject.depth }
    }
}