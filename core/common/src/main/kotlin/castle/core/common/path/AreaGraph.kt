package castle.core.common.path

import com.badlogic.gdx.ai.pfa.Connection
import com.badlogic.gdx.ai.pfa.DefaultGraphPath
import com.badlogic.gdx.ai.pfa.GraphPath
import com.badlogic.gdx.ai.pfa.indexed.IndexedAStarPathFinder
import com.badlogic.gdx.ai.pfa.indexed.IndexedGraph
import com.badlogic.gdx.utils.Array
import com.badlogic.gdx.utils.ObjectMap

class AreaGraph : IndexedGraph<Area> {
    private var areaHeuristic: AreaHeuristic = AreaHeuristic()
    private var areas: Array<Area> = Array<Area>()
    private var areaConnections: Array<AreaConnection> = Array<AreaConnection>()
    private var map: ObjectMap<Area, Array<Connection<Area>>> = ObjectMap<Area, Array<Connection<Area>>>()
    private var lastNodeIndex = 0

    fun addArea(area: Area) {
        area.index = lastNodeIndex++
        areas.add(area)
    }

    fun connect(from: Area, to: Area) {
        val indexedFrom = getArea(from.x, from.y)
        val indexedTo = getArea(to.x, to.y)
        val areaConnection = AreaConnection(indexedFrom, indexedTo)
        if (!map.containsKey(indexedFrom)) {
            map.put(indexedFrom, Array())
        }
        map.get(indexedFrom).add(areaConnection)
        areaConnections.add(areaConnection)
    }

    fun findPath(from: Area, to: Area): GraphPath<Area> {
        val indexedFrom = getArea(from.x, from.y)
        val indexedTo = getArea(to.x, to.y)
        val outPath = DefaultGraphPath<Area>()
        IndexedAStarPathFinder(this).searchNodePath(indexedFrom, indexedTo, areaHeuristic, outPath)
        return outPath
    }

    private fun getArea(x: Int, y: Int): Area {
        return areas.first { it.x == x && it.y == y }
    }

    override fun getConnections(fromNode: Area): Array<Connection<Area>> = if (map.containsKey(fromNode)) map.get(fromNode) else Array()

    override fun getIndex(node: Area): Int = node.index

    override fun getNodeCount(): Int = lastNodeIndex
}