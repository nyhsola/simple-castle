package castle.server.ashley.utils

import castle.server.ashley.component.RenderComponent
import com.badlogic.ashley.core.Entity
import com.badlogic.gdx.graphics.Camera
import com.badlogic.gdx.graphics.g3d.ModelInstance
import com.badlogic.gdx.math.Intersector
import com.badlogic.gdx.math.Vector3
import com.badlogic.gdx.math.collision.BoundingBox
import com.badlogic.gdx.math.collision.Ray
import java.util.stream.StreamSupport

object IntersectUtils {
    fun intersectPositionPoint(tmp: BoundingBox?, baseCamera: Camera, modelInstance: ModelInstance, touchedX: Int, touchedY: Int): Vector3? {
        val pickRay = baseCamera.getPickRay(touchedX.toFloat(), touchedY.toFloat())
        val intersection = Vector3()
        val boundingBox = modelInstance.calculateBoundingBox(tmp)
        boundingBox.mul(modelInstance.transform)
        return if (Intersector.intersectRayBounds(pickRay, boundingBox, intersection)) {
            intersection
        } else null
    }

    fun intersect(camera: Camera, gameObjectList: Iterable<Entity>, touchedY: Float, touchedX: Float, temp: BoundingBox): Entity? {
        val pickRay: Ray = camera.getPickRay(touchedX, touchedY)
        val intersection = Vector3()
        return StreamSupport.stream(gameObjectList.spliterator(), false).filter { entity ->
            val modelInstance = RenderComponent.mapper.get(entity).modelInstance
            val boundingBox: BoundingBox = modelInstance.calculateBoundingBox(temp)
            boundingBox.mul(modelInstance.transform)
            Intersector.intersectRayBounds(pickRay, boundingBox, intersection)
        }.findFirst().orElse(null)
    }
}