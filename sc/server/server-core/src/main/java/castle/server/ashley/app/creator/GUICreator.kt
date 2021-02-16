package castle.server.ashley.app.creator

import com.badlogic.gdx.graphics.g2d.SpriteBatch
import com.badlogic.gdx.graphics.g3d.ModelBatch
import com.badlogic.gdx.graphics.g3d.decals.DecalBatch
import com.badlogic.gdx.graphics.g3d.decals.GroupStrategy
import com.badlogic.gdx.graphics.glutils.ShapeRenderer
import com.badlogic.gdx.scenes.scene2d.Stage

interface GUICreator {
    fun createModelBatch(): ModelBatch
    fun createSpriteBatch(): SpriteBatch
    fun createShapeRenderer(): ShapeRenderer
    fun createStage(): Stage
    fun createDecalBatch(groupStrategy: GroupStrategy): DecalBatch
}