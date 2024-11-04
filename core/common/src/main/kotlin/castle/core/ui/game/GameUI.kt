package castle.core.ui.game

import castle.core.component.render.StageRenderComponent
import castle.core.event.EventContext
import castle.core.event.EventQueue
import castle.core.path.Area
import castle.core.service.CommonResources
import castle.core.service.MapScanService
import castle.core.service.UIService.Companion.MENU_ENABLE
import castle.core.system.GameManagerSystem
import castle.core.util.UIUtils
import com.badlogic.ashley.core.Entity
import com.badlogic.ashley.signals.Signal
import com.badlogic.gdx.Input
import com.badlogic.gdx.graphics.g2d.SpriteBatch
import com.badlogic.gdx.graphics.glutils.ShapeRenderer
import com.badlogic.gdx.scenes.scene2d.Group
import com.badlogic.gdx.scenes.scene2d.InputEvent
import com.badlogic.gdx.scenes.scene2d.InputListener
import com.badlogic.gdx.scenes.scene2d.Touchable
import com.badlogic.gdx.scenes.scene2d.ui.*
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener
import com.badlogic.gdx.utils.Align
import com.badlogic.gdx.utils.viewport.Viewport
import org.koin.core.annotation.Single

private const val PAD_MAIN = 0.02f

@Single
class GameUI(
    spriteBatch: SpriteBatch,
    viewport: Viewport,
    mapScanService: MapScanService,
    private val commonResources: CommonResources,
    shapeRenderer: ShapeRenderer,
    eventQueue: EventQueue
) : Entity() {
    private val stageRenderComponent: StageRenderComponent = StageRenderComponent(UIUtils.createStage(viewport, spriteBatch)).also { this.add(it) }
    private val signal = Signal<EventContext>()
    private val rootContainer = Container<Table>()
    private val rootTable = Table()

    private val minimap: Minimap = Minimap(shapeRenderer, mapScanService)
    val chat = Chat(commonResources)
    val portrait = Portrait(commonResources)
    val description = Description(commonResources)
    val hpHud = HpHud()
    private val panelResources = PanelResources(commonResources)
    private val panelSkills = PanelSkills(commonResources)

    var debugEnabled: Boolean = false
        set(value) {
            stageRenderComponent.stage.isDebugAll = value
            field = value
        }

    init {
        description.isDisabled = true
        description.style.font.data.markupEnabled = true
        description.touchable = Touchable.disabled
        description.isVisible = false

        signal.add(eventQueue)

        rootTable.add(main()).grow()

        rootContainer.setFillParent(true)
        rootContainer.pad(10f)
        rootContainer.fill()
        rootContainer.actor = rootTable

        stageRenderComponent.stage.addActor(rootContainer)
        stageRenderComponent.stage.addListener(object : InputListener() {
            override fun touchDown(event: InputEvent, x: Float, y: Float, pointer: Int, button: Int): Boolean {
                if (event.target !is Chat) {
                    signal.dispatch(EventContext(GameManagerSystem.CHAT_UNFOCUSED))
                    stageRenderComponent.stage.keyboardFocus = null
                }
                return super.touchDown(event, x, y, pointer, button)
            }

            override fun keyDown(event: InputEvent, keycode: Int): Boolean {
                if (Input.Keys.ENTER == keycode) {
                    signal.dispatch(EventContext(GameManagerSystem.CHAT_FOCUSED))
                    stageRenderComponent.stage.keyboardFocus = chat
                }
                if (Input.Keys.ESCAPE == keycode) {
                    signal.dispatch(EventContext(GameManagerSystem.CHAT_UNFOCUSED))
                    stageRenderComponent.stage.keyboardFocus = null
                }
                return super.keyDown(event, keycode)
            }
        })
    }

    fun init() {
        minimap.init()
    }

    fun update(objectsOnMap: Map<Area, Collection<Entity>>) {
        minimap.update(objectsOnMap)
        description.update()
    }

    private fun main(): Container<out Group> {
        val stack = Stack()
        val container = Container(stack).fill()
        stack.add(escButton())
        stack.add(minimap())
        stack.add(chat())
        stack.add(portraitAndDescription())
        stack.add(panelSkills())
        stack.add(panelResources())
        stack.add(hpHud())
        return container
    }

    private fun escButton(): Container<out Group> {
        val button = TextButton("ESC", commonResources.skin)
        button.color.a = 0.75f

        button.addCaptureListener(object : ClickListener() {
            override fun clicked(event: InputEvent, x: Float, y: Float) {
                signal.dispatch(EventContext(MENU_ENABLE))
            }
        })

        val table = Table()
        val container = Container(table).fill()
        table.add(button)
            .height(Value.percentHeight(0.05f, container))
            .width(Value.percentWidth(0.05f, container))
            .pad(5f)
            .align(Align.topLeft)
            .expand()
        return container
    }

    private fun minimap(): Container<out Group> {
        val table = Table()
        val container = Container(table).fill()
        table.add(minimap)
            .height(Value.percentHeight(0.25f, container))
            .width(Value.percentHeight(0.25f, container))
            .align(Align.bottomLeft)
            .expand()
        return container
    }

    private fun chat(): Container<out Group> {
        val table = Table()
        val container = Container(table).fill()
        table.add(chat)
            .height(Value.percentHeight(0.15f, container))
            .width(Value.percentWidth(0.25f, container))
            .padBottom(Value.percentHeight(0.25f + PAD_MAIN, container))
            .align(Align.bottomLeft)
            .expand()
        return container
    }

    private fun portraitAndDescription(): Container<out Group> {
        val table = Table()
        val container = Container(table).fill()
        table.add(portraitAndDescriptionConcat())
            .height(Value.percentHeight(0.25f, container))
            .width(Value.percentWidth(0.35f, container))
            .padRight(Value.percentWidth(0.25f + PAD_MAIN, container))
            .align(Align.bottomRight)
            .expand()
        return container
    }

    private fun portraitAndDescriptionConcat(): Container<out Group> {
        val table = Table()
        val container = Container(table).fill()
        table.add(description)
            .width(Value.percentWidth(0.50f, container))
            .grow()
        table.add(portrait)
            .width(Value.percentWidth(0.50f, container))
            .grow()
        return container
    }

    private fun panelSkills(): Container<Table> {
        val table = Table()
        val container = Container(table).fill()
        table.add(panelSkills)
            .height(Value.percentHeight(0.25f, container))
            .width(Value.percentWidth(0.25f, container))
            .align(Align.bottomRight)
            .expand()
        return container
    }

    private fun panelResources(): Container<Table> {
        val table = Table()
        val container = Container(table).fill()
        table.add(panelResources)
            .width(Value.percentWidth(0.25f, container))
            .align(Align.topRight)
            .expand()
        return container
    }

    private fun hpHud(): Container<out Group> {
        val table = Table()
        val container = Container(table)
        container.setFillParent(true)
        container.fill()
        container.actor = table
        table.add(hpHud).grow()
        return container
    }
}