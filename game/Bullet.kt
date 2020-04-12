package architecture.game

import architecture.engine.World
import com.badlogic.gdx.graphics.g2d.Sprite
import com.badlogic.gdx.math.Vector2
import com.my.architecture.engine.structs.GameObject

open class Bullet(pos: Vector2,
                  private val dir: Vector2,
                  private val onHit: () -> (Unit),
                  private val sprites: Array<Sprite>,
                  private val speed: Float,
                  private val damage: Float,
                  w: Float = 50f,
                  h: Float = 75f)
    : GameObject(sprites, w, h, pos.cpy()) {

    override fun start() {
    }

    override fun update(dt: Float) {
        position.y += dt * speed * dir.y
        position.x += dt * speed * dir.x
        World.world.overlaps(this)
    }

    /**
     * Standard behaviour
     */
    override fun onCollide(other: GameObject) {
        if (other.get<BasicEnemy>() == null) return
        other.get<BasicEnemy>()!!.hp -= damage
        onHit()
        World.world.destroy(this)
    }
}