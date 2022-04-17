package castle.core.ui.game

import castle.core.service.CommonResources
import com.badlogic.gdx.graphics.Color
import com.badlogic.gdx.scenes.scene2d.Touchable
import com.badlogic.gdx.scenes.scene2d.ui.Table
import com.badlogic.gdx.scenes.scene2d.ui.TextButton

class PanelSkills(commonResources: CommonResources) : Table() {
    init {
        touchable = Touchable.enabled
        for (i in 0 until 5) {
            for (j in 0 until 5) {
                val button = TextButton("U$i$j", commonResources.skin)
                button.color = Color.DARK_GRAY
                button.color.a = 0.75f
                add(button)
                        .pad(2f)
                        .grow()
            }
            row()
        }
    }
}