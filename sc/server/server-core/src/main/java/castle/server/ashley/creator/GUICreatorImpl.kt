package castle.server.ashley.creator

import com.badlogic.gdx.graphics.g3d.ModelBatch
import com.badlogic.gdx.graphics.g3d.decals.DecalBatch
import com.badlogic.gdx.graphics.g3d.decals.GroupStrategy

class GUICreatorImpl : GUICreator {
    override fun createModelBatch(): ModelBatch {
        return ModelBatch()
    }

    override fun createDecalBatch(groupStrategy: GroupStrategy): DecalBatch {
        return DecalBatch(groupStrategy)
    }
}