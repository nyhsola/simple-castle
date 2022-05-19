package castle.core.state

import com.badlogic.gdx.ai.fsm.DefaultStateMachine

class StateMachineDelta<E, S : StateDelta<E>>(e: E, s: S) : DefaultStateMachine<E, S>(e, s) {
    fun update(delta: Float) {
        if (globalState != null) globalState.update(owner, delta)
        if (currentState != null) currentState.update(owner, delta)
    }
}