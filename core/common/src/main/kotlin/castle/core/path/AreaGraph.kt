package castle.core.path

import com.badlogic.gdx.ai.pfa.Connection
import com.badlogic.gdx.ai.pfa.DefaultGraphPath
import com.badlogic.gdx.ai.pfa.GraphPath
import com.badlogic.gdx.ai.pfa.indexed.IndexedAStarPathFinder
import com.badlogic.gdx.ai.pfa.indexed.IndexedGraph
import com.badlogic.gdx.utils.Array
import com.badlogic.gdx.utils.ObjectMap

class AreaGraph : IndexedGraph<Area> {
    private val areaHeuristic: AreaHeuristic = AreaHeuristic()
    private val areas: Array<Area> = Array<Area>()
    private val areaConnections: Array<AreaConnection> = Array<AreaConnection>()
    private val map: ObjectMap<Area, Array<Connection<Area>>> = ObjectMap<Area, Array<Connection<Area>>>()
    private val dynamicUpdate: MutableList<Area> = ArrayList()
    private val staticUpdate: MutableList<Area> = ArrayList()
    private var lastNodeIndex = 0

    fun addArea(area: Area) {
        area.index = lastNodeIndex++
        areas.add(area)
    }

    fun connect(from: Area, to: Area) {
        val indexedFrom = getIndexedArea(from.x, from.y)
        val indexedTo = getIndexedArea(to.x, to.y)
        val areaConnection = AreaConnection(indexedFrom, indexedTo)
        if (!map.containsKey(indexedFrom)) {
            map.put(indexedFrom, Array())
        }
        map.get(indexedFrom).add(areaConnection)
        areaConnections.add(areaConnection)
    }

    fun restoreStaticConnections(list: List<Area>) {
        staticUpdate.removeAll(list)
    }

    fun restoreConnections() {
        dynamicUpdate.clear()
    }

    fun disconnect(from: Area, isStatic: Boolean) {
        if (isStatic) {
            staticUpdate.add(from)
        } else {
            dynamicUpdate.add(from)
        }
    }

    fun findPath(from: Area, to: Area): GraphPath<Area> {
        val indexedFrom = getIndexedArea(from.x, from.y)
        val indexedTo = getIndexedArea(to.x, to.y)
        val outPath = DefaultGraphPath<Area>()
        IndexedAStarPathFinder(this).searchNodePath(indexedFrom, indexedTo, areaHeuristic, outPath)
        return outPath
    }

    private fun getIndexedArea(x: Int, y: Int): Area {
        return areas.first { it.x == x && it.y == y }
    }

    override fun getConnections(fromNode: Area): Array<Connection<Area>> {
        if (!(staticUpdate.contains(fromNode) || dynamicUpdate.contains(fromNode)) && map.containsKey(fromNode)) {
            return map.get(fromNode)
        }
        return Array()
    }

    override fun getIndex(node: Area): Int = node.index

    override fun getNodeCount(): Int = lastNodeIndex
}