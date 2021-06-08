package castle.server.ashley.app.creator

import com.badlogic.gdx.graphics.g2d.SpriteBatch
import com.badlogic.gdx.graphics.g3d.ModelBatch
import com.badlogic.gdx.graphics.g3d.decals.DecalBatch
import com.badlogic.gdx.graphics.g3d.decals.GroupStrategy
import com.badlogic.gdx.graphics.glutils.ShapeRenderer
import com.badlogic.gdx.scenes.scene2d.Stage
import com.badlogic.gdx.utils.viewport.ScreenViewport

class GUICreatorImpl : GUICreator {
    override fun createModelBatch(): ModelBatch {
        return ModelBatch()
    }

    override fun createSpriteBatch(): SpriteBatch {
        return SpriteBatch()
    }

    override fun createShapeRenderer(): ShapeRenderer {
        return ShapeRenderer()
    }

    override fun createStage(): Stage {
        return Stage(ScreenViewport())
    }

    override fun createDecalBatch(groupStrategy: GroupStrategy): DecalBatch {
        return DecalBatch(groupStrategy)
    }
}