package architecture.game

import architecture.engine.*
import com.badlogic.gdx.Gdx
import com.badlogic.gdx.Input
import com.badlogic.gdx.graphics.g2d.Sprite
import com.badlogic.gdx.math.Vector2
import java.lang.Exception
import kotlin.math.abs


class SpaceInvaders : Game() {

    // my worlds
    val worlds: Array<World> = arrayOf(World(), World())

    fun assert(good: Boolean) {
        if (!good) throw Exception("Error asserting, expected ${true}; result = $good")
    }

    companion object {
        lateinit var sprites: MutableList<Sprite>
    }

    override fun start() {
        Gdx.graphics.setResizable(false)
        Gdx.graphics.setWindowedMode(1020, 530)
        World.setCurrentWorld(worlds[1])
        val rendererOpt = RendererOptimizer()
        val rendererBackground = RendererOptimizer()
        sprites = rendererOpt.consumeSprites("ASSETS_05")
        val baseURL = "assets/sprites/256px"
        if (sprites.isEmpty()) {
            // PLAYER SPRITE (0)
            assert(rendererOpt.sprite("$baseURL/PlayerRed_Frame_01_png_processed.png"))
            // ... FUTURE SPRITES BUT ALLOCATE THESE FOR FILLING SPACE FOR THE MOMENT (1, 2)
            assert(rendererOpt.sprite("$baseURL/PlayerRed_Frame_02_png_processed.png"))
            assert(rendererOpt.sprite("$baseURL/PlayerRed_Frame_03_png_processed.png"))
            // Shooting (3, 8) inclusive
            for (i in 1..6) assert(rendererOpt.sprite("$baseURL/Exhaust_Frame_0${i}_png_processed.png"))
            // Explosion (9, 17)
            for (i in 1..9) assert(rendererOpt.sprite("$baseURL/Explosion01_Frame_0${i}_png_processed.png"))

            // ENEMY (18)
            assert(rendererOpt.sprite("$baseURL/Enemy01_Red_Frame_1_png_processed.png"))

            // ENEMY (19)
            assert(rendererOpt.sprite("$baseURL/Enemy01_Teal_Frame_1_png_processed.png"))

            // ENEMY (20)
            assert(rendererOpt.sprite("$baseURL/Enemy02_Teal_Frame_1_png_processed.png"))

            // Joysticks (21, 22)
            assert(rendererOpt.sprite("joyyy.png"))
            assert(rendererOpt.sprite("circle.png"))

            // Shooting Special (23)
            assert(rendererOpt.sprite("$baseURL/Explosion02_Frame_06_png_processed.png"))

            // Shooting Enemy 24
            assert(rendererOpt.sprite("$baseURL/Laser_Large_png_processed.png"))
            // Shooting 25
            assert(rendererOpt.sprite("$baseURL/Explosion01_Frame_07_png_processed.png"))
            // Enemy 26
            assert(rendererOpt.sprite("$baseURL/Enemy02_Teal_Frame_1_png_processed.png"))
            // Save them
            sprites = rendererOpt.consumeSprites()

            rendererOpt.saveConsumedSprites("ASSETS_05")
        }

        var spriteBackground = rendererBackground.consumeSprites("ASSET_BACKGROUND3")
        if (spriteBackground.isEmpty()) {
            rendererBackground.sprite("assets/background/NebulaAqua-Pink.png")
            spriteBackground = rendererBackground.consumeSprites()
            rendererBackground.saveConsumedSprites("ASSET_BACKGROUND3")
        }
        instantiatePlayerAndJoysticks()
        World.world.instantiate(Enemy(Vector2(100f, 100f)))
        World.world.instantiate(SpecialAttackBar())

        Background(spriteBackground[0])
        HealthBar()
        LevelManager()
    }

    fun instantiatePlayerAndJoysticks() {
        val sprite0 = sprites[21]
        val sprite1 = sprites[22]
        val diff = (Gdx.graphics.width / 3) / 2

        /**
         * There are 3 virutal joysticks
         *
         * The function that we are passing into the constructors are fallbacks for when there is no mobile device, so we implement our own dist and dir functions
         * for each joystick, for keyboard
         */
        val joy = Joy(
                sprite0,
                sprite1,
                200f,
                50f,
                0.0f,
                Pair(0, Gdx.graphics.width / 3),
                Vector2(
                        diff.toFloat(),
                        Gdx.graphics.height - Gdx.graphics.height / 10f
                ),
                // dir() function
                {
                    val x = if (Gdx.input.isKeyPressed(Input.Keys.D)) 1 else if (Gdx.input.isKeyPressed(Input.Keys.A)) -1 else 0
                    val y = if (Gdx.input.isKeyPressed(Input.Keys.W)) 1 else if (Gdx.input.isKeyPressed(Input.Keys.S)) -1 else 0
                    Vector2(x.toFloat(), y.toFloat()).nor()
                },
                // dist() function
                {
                    val x = if (Gdx.input.isKeyPressed(Input.Keys.D)) 1 else if (Gdx.input.isKeyPressed(Input.Keys.A)) -1 else 0
                    val y = if (Gdx.input.isKeyPressed(Input.Keys.W)) 1 else if (Gdx.input.isKeyPressed(Input.Keys.S)) -1 else 0
                    if (abs(x) > 0 || abs(y) > 0) it.maximumValueOnDistCall else 0.0f
                }
        )
        val joy2 = Joy(
                sprite0,
                sprite1,
                200f,
                50f,
                0.0f,
                Pair((Gdx.graphics.width * 2 + 1) / 3, Gdx.graphics.width),
                Vector2(
                        (Gdx.graphics.width * 2f / 3f) + diff,
                        Gdx.graphics.height - Gdx.graphics.height / 10f
                ),
                // dir()
                {
                    val x = if (Gdx.input.isKeyPressed(Input.Keys.RIGHT)) 1 else if (Gdx.input.isKeyPressed(Input.Keys.LEFT)) -1 else 0
                    val y = if (Gdx.input.isKeyPressed(Input.Keys.UP)) 1 else if (Gdx.input.isKeyPressed(Input.Keys.DOWN)) -1 else 0
                    Vector2(x.toFloat(), y.toFloat()).nor()
                },
                // dist()
                {
                    val x = if (Gdx.input.isKeyPressed(Input.Keys.RIGHT)) 1 else if (Gdx.input.isKeyPressed(Input.Keys.LEFT)) -1 else 0
                    val y = if (Gdx.input.isKeyPressed(Input.Keys.UP)) 1 else if (Gdx.input.isKeyPressed(Input.Keys.DOWN)) -1 else 0
                    if (abs(x) > 0 || abs(y) > 0) it.maximumValueOnDistCall else 0.0f
                }
        )

        val joy3AttackSpecial = Joy(
                sprite0,
                sprite1,
                100f,
                25f,
                0.0f,
                Pair((Gdx.graphics.width + 1) / 3, Gdx.graphics.width * 2 / 3),
                Vector2((Gdx.graphics.width / 3f) + diff,
                        Gdx.graphics.height - Gdx.graphics.height / 10f),
                // dir() *** we dont use this callback ***
                {
                    Vector2()
                },
                /**
                 * We use this for non mobile platforms
                 * When joy3AttackSpecial.dist() is called, this will be fired! (Only when not mobile)
                 *
                 * .dist() is called inside Player for getting inputs
                 */
                {
                    val x = if (Gdx.input.isKeyPressed(Input.Keys.D)) 1 else if (Gdx.input.isKeyPressed(Input.Keys.A)) -1 else 0
                    val y = if (Gdx.input.isKeyPressed(Input.Keys.W)) 1 else if (Gdx.input.isKeyPressed(Input.Keys.S)) -1 else 0
                    // call subscribers manually
                    it.callSubscribers(Vector2(x.toFloat(), y.toFloat()).nor(), if (Gdx.input.isKeyPressed(Input.Keys.E)) it.maximumValueOnDistCall else 0.0f)
                    0.0f
                },
                "JOYSTICK ATTACK")
        World.world.instantiate(joy)
        World.world.instantiate(joy2)
        World.world.instantiate(joy3AttackSpecial)
        World.world.instantiate(Player(sprites.slice(0..2).toTypedArray(), joy, joy2, joy3AttackSpecial))
    }
}