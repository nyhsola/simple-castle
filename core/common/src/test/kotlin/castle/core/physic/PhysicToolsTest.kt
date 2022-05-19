package castle.core.physic

import org.junit.Test

internal class PhysicToolsTest {
    @Test
    fun test() {
        val filterGroup = PhysicTools.getFilterGroup(4)
        val filterMask = PhysicTools.getFilterMask(listOf(1, 4))

        println(filterGroup.toString(2).padStart(8, '0'))
        println(filterMask.toString(2).padStart(8, '0'))
    }
}