package architecture.game

import architecture.engine.Game
import architecture.engine.World
import architecture.engine.structs.GameObjectInput
import architecture.engine.structs.IJoystick
import com.badlogic.gdx.Gdx
import com.badlogic.gdx.graphics.g2d.Sprite
import com.badlogic.gdx.math.MathUtils
import com.badlogic.gdx.math.Vector2
import com.my.architecture.engine.structs.GameObject
import kotlin.math.abs

class Player(sprites: Array<Sprite>, private val movementJoystick: IJoystick, private val attackJoystick: IJoystick, private val specialAttackJoy: IJoystick) : GameObjectInput(sprites, 100f, 100f, Vector2(50f, 50f)) {

    private var cooldownAttack: Float = 1.0f
    private var canAttack: Float = 0.3f

    // We start -4 because we want immunity at the start of the game just in case
    private var cooldownAccReceivingDamage: Float = -4.0f
    
    private val cooldownReceivingDamage: Float = 1.0f
    var hp = 100.0f
    var accumulatorSpecialAttack = 0.0f


    override fun keyDown(keycode: Int): Boolean {
        return true
    }

    override fun start() {
        specialAttackJoy.subscribe(this)
    }

    override fun update(dt: Float) {
        specialAttackJoy.dist()

        cooldownAccReceivingDamage += dt

        World.world.overlaps(this)

        val direction = movementJoystick.dir()
        val distance = movementJoystick.dist()
        val canMove = position.x >= LevelManager.level.leftBounds &&
                                position.x + width <= LevelManager.level.rightBounds &&
                                position.y + height <= LevelManager.level.topBounds &&
                                position.y > LevelManager.level.bottomBounds
        if (canMove) {
            position.x += direction.x * 200 * dt * distance
            position.y += direction.y * 200 * dt * distance
        }

        if (LevelManager.level.initialized) {
            position.x = MathUtils.clamp(position.x, LevelManager.level.leftBounds, LevelManager.level.rightBounds - width)
            position.y = MathUtils.clamp(position.y, LevelManager.level.bottomBounds + 1, LevelManager.level.topBounds - height)
        }
        flipX = direction.x > 0.0f

        spriteIndex = when {
            abs(direction.x) > 0.8f -> 2
            abs(direction.x) > 0.5f -> 1
            else                    -> 0
        }

        cooldownAttack += dt

        if (cooldownAttack < canAttack) {
            return
        }

        val dist = attackJoystick.dist()
        if (dist < 0.1f) {
            return
        }

        Gdx.app.log("player", "$accumulatorSpecialAttack")

        val dir = attackJoystick.dir().cpy()
        val bullet = NormalBullet(Vector2(), dir) { accumulatorSpecialAttack += 10f }
        val bull = World.world.instantiate(bullet)

        // -90 deg because the sprite is already looking up
        bull.rotation = (MathUtils.atan2(dir.y, dir.x) * 180 / Math.PI.toFloat()) - 90

        val pos = position.cpy()
        pos.x += (width / 2) - (bull.width / 2)
        pos.y += height - bull.height
        bull.position = pos

        cooldownAttack = 0.0f
    }

    override fun touchUpJoystick(direction: Vector2, dist: Float, joystickTag: String) {
        if (dist <= 0 || direction.len() <= .5f || accumulatorSpecialAttack < 100 ) {
            return
        }
        accumulatorSpecialAttack = 0.0f
        World.world.instantiate(SpecialBullet(position.cpy(), direction){ accumulatorSpecialAttack += 10f })
    }

    override fun onDestroy() {
        super.onDestroy()
        specialAttackJoy.unsubscribe(this)
    }

    override fun onCollide(other: GameObject) {
        val b = other.get<BulletEnemy>()
        if (b != null) {
            hp -= 10
            World.world.destroy(other)
            return
        }

        if (other.get<BasicEnemy>() == null) {
            return
        }

        if (cooldownAccReceivingDamage <= cooldownReceivingDamage) return
        cooldownAccReceivingDamage = 0.0f
        hp -= 20f
    }
}