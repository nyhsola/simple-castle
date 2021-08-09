package castle.core.common.creator

import com.badlogic.gdx.graphics.g2d.SpriteBatch
import com.badlogic.gdx.graphics.g3d.ModelBatch
import com.badlogic.gdx.graphics.g3d.decals.DecalBatch
import com.badlogic.gdx.graphics.g3d.decals.GroupStrategy
import com.badlogic.gdx.graphics.glutils.ShapeRenderer
import com.badlogic.gdx.scenes.scene2d.Stage
import com.badlogic.gdx.utils.viewport.ScreenViewport

class GUIConfigImpl : GUIConfig {
    override fun modelBatch(): ModelBatch {
        return ModelBatch()
    }

    override fun spriteBatch(): SpriteBatch {
        return SpriteBatch()
    }

    override fun shapeRenderer(): ShapeRenderer {
        return ShapeRenderer()
    }

    override fun stage(): Stage {
        return Stage(ScreenViewport())
    }

    override fun decalBatch(groupStrategy: GroupStrategy): DecalBatch {
        return DecalBatch(groupStrategy)
    }
}