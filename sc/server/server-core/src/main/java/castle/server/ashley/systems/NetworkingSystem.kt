package castle.server.ashley.systems

import castle.server.ashley.systems.adapter.IteratingSystemAdapter
import castle.server.ashley.systems.component.PositionComponent
import com.badlogic.ashley.core.Family

class NetworkingSystem : IteratingSystemAdapter(Family.all(PositionComponent::class.java).get()) {
    // TODO: 9/9/2020 ...
}