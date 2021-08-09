package castle.core.common.creator

import com.badlogic.gdx.graphics.g2d.SpriteBatch
import com.badlogic.gdx.graphics.g3d.ModelBatch
import com.badlogic.gdx.graphics.g3d.decals.DecalBatch
import com.badlogic.gdx.graphics.g3d.decals.GroupStrategy
import com.badlogic.gdx.graphics.glutils.ShapeRenderer
import com.badlogic.gdx.scenes.scene2d.Stage

interface GUIConfig {
    fun modelBatch(): ModelBatch
    fun spriteBatch(): SpriteBatch
    fun shapeRenderer(): ShapeRenderer
    fun stage(): Stage
    fun decalBatch(groupStrategy: GroupStrategy): DecalBatch
}