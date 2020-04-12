package architecture.game

import architecture.engine.World
import com.badlogic.gdx.Gdx
import com.badlogic.gdx.graphics.Color
import com.badlogic.gdx.graphics.glutils.ShapeRenderer
import com.my.architecture.engine.structs.GameObject

class SpecialAttackBar : GameObject(arrayOf(), 0.0f, 0.0f) {
    val shape: ShapeRenderer = ShapeRenderer()
    var player: Player? = null
    var joy: Joy? = null

    override fun start() {
        player = World.world.findGameObjects<Player>()[0]
        if (player == null) World.world.destroy(this)
        joy = World.world.findGameObjects<Joy>().filter { joy -> joy.tag == "JOYSTICK ATTACK" }[0]
    }

    override fun update(dt: Float) {
        val deg = ((player!!.accumulatorSpecialAttack * 360f) + 1) / 100f
        shape.color = Color.GOLD
        shape.begin(ShapeRenderer.ShapeType.Filled)
        shape.arc(100.0f, Gdx.graphics.height - 60f, 60f, 90f, deg)
        shape.color = Color.GOLDENROD
        shape.circle(100.0f, Gdx.graphics.height - 60f, 40f)
        shape.end()
        if (deg < 360) {
            joy?.activate(false)
            return
        }
        joy?.activate(true)
    }

}