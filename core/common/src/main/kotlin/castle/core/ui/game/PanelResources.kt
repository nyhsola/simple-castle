package castle.core.ui.game

import castle.core.service.CommonResources
import com.badlogic.gdx.scenes.scene2d.ui.Table
import com.badlogic.gdx.scenes.scene2d.ui.TextButton

class PanelResources(
    commonResources: CommonResources
) : Table() {
    init {
        val buttonGold = TextButton("Gold: 10 000", commonResources.skin)
        val buttonTree = TextButton("Tree: 10 000", commonResources.skin)

        add(buttonGold).grow()
        add(buttonTree).grow()

        buttonGold.color.a = 0.75f
        buttonTree.color.a = 0.75f
    }
}