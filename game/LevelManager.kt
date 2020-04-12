package architecture.game


import architecture.engine.Game
import architecture.engine.World
import com.badlogic.gdx.Gdx
import com.badlogic.gdx.graphics.Camera
import com.badlogic.gdx.math.MathUtils
import com.badlogic.gdx.math.Vector2
import com.badlogic.gdx.math.Vector3
import com.my.architecture.engine.structs.GameObject

class Spawn (val positionToSpawn: Float, val enemies: Array<BasicEnemy>, val timeBetweenSpawns: Float, val offsetPlayerX: Float): GameObject(arrayOf(), 0.0f, 0.0f) {
    var time = timeBetweenSpawns
    var currentEnemy = 0

    lateinit var player: Player

    override fun start() {
        player = World.world.findGameObjects<Player>().last()
    }

    override fun update(dt: Float) {
        time += dt

        if (timeBetweenSpawns <= time) {
            time = 0.0f
            val enemy = World.world.instantiate(enemies[currentEnemy])
            enemy.position = Vector2(player.position.x + offsetPlayerX, LevelManager.level.topBounds)
            currentEnemy++
        }

        if (currentEnemy >= enemies.size) {
            World.world.destroy(this)
        }

    }
}


/**
 * @NOTE THIS class should be the last thing to be initialized.
 */
class LevelManager : GameObject(arrayOf(), 0.0f, 0.0f){

    val enemiesToSpawn: MutableList<Spawn> = mutableListOf(
            Spawn(
                    1500.0f,
                    Config.config01(),
                    2.2f,
                    -200f
            )
    )

    var topBounds: Float = 0.0f
    var bottomBounds: Float = 0.0f
    var rightBounds: Float = 0.0f
    var leftBounds: Float = 0.0f

    lateinit var currentPlayer: Player
    lateinit var camera: Camera
    var currentSpawner = 0

    init {
        World.world.instantiate(this)
        level = this
    }

    companion object {
        lateinit var level: LevelManager
    }

    override fun update(dt: Float) {
        cameraWork(dt)
        spawner()
    }

    override fun start() {
        currentPlayer = World.world.findGameObjects<Player>()[0]
        camera = Game.camera
        camera.position.x = (Background.currentBackground.width / 2) - camera.viewportWidth / 2
        camera.position.y = camera.viewportHeight
        currentPlayer.position = Vector2(camera.position.x, camera.position.y)
        camera.update()
        level = this
    }

    private fun spawner() {
        if (currentSpawner >= enemiesToSpawn.size) return
        if (camera.position.y + camera.viewportHeight / 2 >= enemiesToSpawn[currentSpawner].positionToSpawn) {
            World.world.instantiate(enemiesToSpawn[currentSpawner])
            currentSpawner++
        }
    }

    private fun cameraWork(dt: Float) {
        camera.position.y += dt * 20f
        topBounds = camera.viewportHeight / 2 + camera.position.y
        leftBounds = Background.currentBackground.position.x
        rightBounds = Background.currentBackground.position.x + Background.currentBackground.width
        bottomBounds = camera.position.y - camera.viewportHeight / 2
        val lerp = 3f
        val position: Vector3 = camera.position
        position.x += (currentPlayer.position.x - position.x) * lerp * dt
        camera.position.x = MathUtils.clamp(camera.position.x, leftBounds + camera.viewportWidth / 2, rightBounds - camera.viewportWidth / 2)
        camera.update()
    }

}

class Config {
    companion object {
        fun config01(): Array<BasicEnemy> {
            return arrayOf(
                    EnemyFollow(Vector2()),
                    Enemy(Vector2(),
                            40f,
                            arrayOf(
                                    Point(Vector2(0.4f, -0.6f), 100f, 10f),
                                    Point(Vector2(0.7f, -0.9f), 200f, 10f)
                            )
                    ),
                    Enemy(Vector2(),
                            40f,
                            arrayOf(
                                    Point(Vector2(0.4f, -0.6f), 100f, 10f),
                                    Point(Vector2(0.7f, -0.9f), 200f, 10f)
                            )
                    ),
                    Enemy(Vector2(),
                            40f,
                            arrayOf(
                                    Point(Vector2(0.4f, -0.6f), 100f, 10f),
                                    Point(Vector2(0.7f, -0.9f), 200f, 10f)
                            )
                    ),
                    Enemy(Vector2(),
                            40f,
                            arrayOf(
                                    Point(Vector2(0.4f, -0.6f), 100f, 10f),
                                    Point(Vector2(0.7f, -0.9f), 200f, 10f)
                            )
                    ),
                    Enemy(Vector2(),
                            40f,
                            arrayOf(
                                    Point(Vector2(0.4f, -0.6f), 100f, 10f),
                                    Point(Vector2(0.7f, -0.9f), 200f, 10f)
                            )
                    ),
                    Enemy(Vector2(),
                            40f,
                            arrayOf(
                                    Point(Vector2(0.4f, -0.6f), 100f, 10f),
                                    Point(Vector2(0.7f, -0.9f), 200f, 10f)
                            )
                    ),
                    Enemy(Vector2(),
                            40f,
                            arrayOf(
                                    Point(Vector2(0.4f, -0.6f), 100f, 10f),
                                    Point(Vector2(0.7f, -0.9f), 200f, 10f)
                            )
                    ),
                    Enemy(Vector2(),
                            40f,
                            arrayOf(
                                    Point(Vector2(0.4f, -0.6f), 100f, 10f),
                                    Point(Vector2(0.7f, -0.9f), 200f, 10f)
                            )
                    ),
                    Enemy(Vector2(),
                            40f,
                            arrayOf(
                                    Point(Vector2(0.4f, -0.8f), 100f, 10f),
                                    Point(Vector2(0.7f, -0.9f), 200f, 10f)
                            )
                    ),
                    Enemy(Vector2(),
                            40f,
                            arrayOf(
                                    Point(Vector2(0.4f, -0.8f), 100f, 10f),
                                    Point(Vector2(0.7f, -0.9f), 200f, 10f)
                            )
                    ),
                    Enemy(Vector2(),
                            40f,
                            arrayOf(
                                    Point(Vector2(-0.4f, -0.8f), 100f, 10f),
                                    Point(Vector2(0.7f, -0.9f), 200f, 10f)
                            )
                    ),
                    Enemy(Vector2(),
                            40f,
                            arrayOf(
                                    Point(Vector2(-0.4f, -0.6f), 100f, 10f),
                                    Point(Vector2(0.7f, -0.9f), 200f, 10f)
                            )
                    ),
                    Enemy(Vector2(),
                            40f,
                            arrayOf(
                                    Point(Vector2(-0.4f, -0.6f), 100f, 10f),
                                    Point(Vector2(0.7f, -0.9f), 200f, 10f)
                            )
                    ),
                    Enemy(Vector2(),
                            40f,
                            arrayOf(
                                    Point(Vector2(-0.4f, -0.6f), 100f, 10f),
                                    Point(Vector2(0.7f, -0.9f), 200f, 10f)
                            )
                    ),
                    Enemy(Vector2(),
                            40f,
                            arrayOf(
                                    Point(Vector2(0.4f, -0.6f), 100f, 10f),
                                    Point(Vector2(0.7f, -0.9f), 200f, 10f)
                            )
                    )
            )
        }
    }
}