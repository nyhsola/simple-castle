package com.simple.castle.core.kt.utils

import com.badlogic.gdx.graphics.g3d.ModelInstance
import com.badlogic.gdx.math.Intersector
import com.badlogic.gdx.math.Vector3
import com.badlogic.gdx.math.collision.BoundingBox
import com.simple.castle.core.kt.render.BaseCamera

object IntersectUtils {
    fun intersectPositionPoint(tmp: BoundingBox?, baseCamera: BaseCamera, modelInstance: ModelInstance, touchedX: Int, touchedY: Int): Vector3? {
        val pickRay = baseCamera.getPickRay(touchedX.toFloat(), touchedY.toFloat())
        val intersection = Vector3()
        val boundingBox = modelInstance.calculateBoundingBox(tmp)
        boundingBox.mul(modelInstance.transform)
        return if (Intersector.intersectRayBounds(pickRay, boundingBox, intersection)) {
            intersection
        } else null
    }
}