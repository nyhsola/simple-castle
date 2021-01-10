package castle.server.ashley.creator

import com.badlogic.gdx.graphics.g2d.SpriteBatch
import com.badlogic.gdx.graphics.g3d.ModelBatch
import com.badlogic.gdx.graphics.g3d.decals.DecalBatch
import com.badlogic.gdx.graphics.g3d.decals.GroupStrategy
import com.badlogic.gdx.graphics.glutils.ShapeRenderer

interface GUICreator {
    fun createModelBatch(): ModelBatch
    fun createSpriteBatch(): SpriteBatch
    fun createShapeRenderer(): ShapeRenderer
    fun createDecalBatch(groupStrategy: GroupStrategy): DecalBatch
}