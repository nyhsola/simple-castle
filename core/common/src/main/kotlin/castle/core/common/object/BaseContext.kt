package castle.core.common.`object`

import castle.core.common.service.ResourceService
import com.badlogic.ashley.core.Engine

open class BaseContext(
    val engine: Engine,
    private val resourceService: ResourceService
) {
    open fun getResourceService() = resourceService
}