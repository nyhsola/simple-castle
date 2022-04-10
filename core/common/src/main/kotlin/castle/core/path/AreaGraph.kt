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
    private val removed: MutableMap<Area, Array<Connection<Area>>> = HashMap()
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

    fun restoreConnections() {
        for (connections in removed) {
            val from = connections.key
            for (connection in connections.value) {
                if (!map.containsKey(from)) {
                    map.put(from, Array())
                }
                map.get(from).add(connection)
            }
        }
    }

    fun disconnect(from: Area) {
        // TODO: 2/11/2022 Should be optimized
        val indexedFrom = getIndexedArea(from.x, from.y)
        for (j in areaConnections.size - 1 downTo 0) {
            val areaConnection = areaConnections.get(j)
            val fromNode = areaConnection.fromNode
            val toNode = areaConnection.toNode
            if (fromNode == indexedFrom) {
                map.get(fromNode).removeValue(areaConnection, false)
                removed.getOrPut(fromNode) { Array() }.add(areaConnection)
            }
            if (toNode == indexedFrom) {
                map.get(toNode).removeValue(areaConnection, false)
                removed.getOrPut(toNode) { Array() }.add(areaConnection)
            }
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

    override fun getConnections(fromNode: Area): Array<Connection<Area>> = if (map.containsKey(fromNode)) map.get(fromNode) else Array()

    override fun getIndex(node: Area): Int = node.index

    override fun getNodeCount(): Int = lastNodeIndex
}