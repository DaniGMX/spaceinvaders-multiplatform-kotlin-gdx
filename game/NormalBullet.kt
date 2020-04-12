package architecture.game

import architecture.engine.Animation
import architecture.engine.Animator
import com.badlogic.gdx.math.Vector2

class NormalBullet(pos: Vector2, dir: Vector2, onHit: () -> (Unit))
    : Bullet(
        pos,
        dir,
        onHit,
        SpaceInvaders.sprites.slice(3..8).toTypedArray(),
        1000f,
        10f
     ) {
    lateinit var animation: Animation

    override fun start() {
        super.start()
        animation = Animator.animate(this, 0, 4, 6, true)
        animation.start()
    }

    override fun onDestroy() {
        super.onDestroy()
        Animator.destroy(animation)
    }
}