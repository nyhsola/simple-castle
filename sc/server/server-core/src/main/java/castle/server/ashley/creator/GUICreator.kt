package castle.server.ashley.creator

import com.badlogic.gdx.graphics.g3d.ModelBatch
import com.badlogic.gdx.graphics.g3d.decals.DecalBatch
import com.badlogic.gdx.graphics.g3d.decals.GroupStrategy

interface GUICreator {
    fun createModelBatch(): ModelBatch
    fun createDecalBatch(groupStrategy: GroupStrategy): DecalBatch
}