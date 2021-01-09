package castle.server.ashley.creator

import com.badlogic.gdx.graphics.g3d.ModelBatch
import com.badlogic.gdx.graphics.g3d.decals.DecalBatch
import com.badlogic.gdx.graphics.g3d.decals.GroupStrategy
import org.mockito.Mockito

class GUICreatorMock : GUICreator {
    override fun createModelBatch(): ModelBatch {
        return Mockito.mock(ModelBatch::class.java)
    }

    override fun createDecalBatch(groupStrategy: GroupStrategy): DecalBatch {
        return Mockito.mock(DecalBatch::class.java)
    }
}