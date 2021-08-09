package castle.core.common.creator

import com.badlogic.gdx.graphics.g2d.SpriteBatch
import com.badlogic.gdx.graphics.g3d.ModelBatch
import com.badlogic.gdx.graphics.g3d.decals.DecalBatch
import com.badlogic.gdx.graphics.g3d.decals.GroupStrategy
import com.badlogic.gdx.graphics.glutils.ShapeRenderer
import com.badlogic.gdx.scenes.scene2d.Stage
import org.mockito.Mockito

class GUIConfigMock : GUIConfig {
    override fun modelBatch(): ModelBatch {
        return Mockito.mock(ModelBatch::class.java)
    }

    override fun spriteBatch(): SpriteBatch {
        return Mockito.mock(SpriteBatch::class.java)
    }

    override fun shapeRenderer(): ShapeRenderer {
        return Mockito.mock(ShapeRenderer::class.java)
    }

    override fun stage(): Stage {
        return Mockito.mock(Stage::class.java)
    }

    override fun decalBatch(groupStrategy: GroupStrategy): DecalBatch {
        return Mockito.mock(DecalBatch::class.java)
    }
}