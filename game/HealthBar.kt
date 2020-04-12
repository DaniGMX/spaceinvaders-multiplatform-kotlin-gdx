package architecture.game

import architecture.engine.World
import com.badlogic.gdx.Gdx
import com.badlogic.gdx.graphics.Color
import com.badlogic.gdx.graphics.glutils.ShapeRenderer
import com.my.architecture.engine.structs.GameObject

class HealthBar : GameObject(arrayOf(), 0.0f, 0.0f) {
    val shape: ShapeRenderer = ShapeRenderer()
    var player: Player? = null

    init {
        World.world.instantiate(this)
    }

    override fun start() {

        player = World.world.findGameObjects<Player>()[0]
        if (player == null) World.world.destroy(this)

    }

    override fun update(dt: Float) {
        Gdx.app.log("hp","${player!!.hp}")
        val deg = ((player!!.hp * 360f)) / 100f
        shape.color = Color.GREEN
        shape.begin(ShapeRenderer.ShapeType.Filled)
        shape.arc(550.0f, Gdx.graphics.height - 60f, 60f, 90f, deg)
        shape.color = Color.BROWN
        shape.circle(550.0f, Gdx.graphics.height - 60f, 40f)
        shape.end()
    }

}