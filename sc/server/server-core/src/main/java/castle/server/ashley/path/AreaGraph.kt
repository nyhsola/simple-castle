package castle.server.ashley.path

import com.badlogic.gdx.ai.pfa.Connection
import com.badlogic.gdx.ai.pfa.DefaultGraphPath
import com.badlogic.gdx.ai.pfa.GraphPath
import com.badlogic.gdx.ai.pfa.indexed.IndexedAStarPathFinder
import com.badlogic.gdx.ai.pfa.indexed.IndexedGraph
import com.badlogic.gdx.utils.Array
import com.badlogic.gdx.utils.ObjectMap

class AreaGraph : IndexedGraph<Area> {
    var areaHeuristic: AreaHeuristic = AreaHeuristic()
    var areas: Array<Area> = Array<Area>()
    var areaConnections: Array<AreaConnection> = Array<AreaConnection>()
    var map: ObjectMap<Area, Array<Connection<Area>>> = ObjectMap<Area, Array<Connection<Area>>>()

    private var lastNodeIndex = 0

    fun addArea(area: Area) {
        area.index = lastNodeIndex
        lastNodeIndex++

        areas.add(area)
    }

    fun getArea(x: Int, y: Int): Area {
        return areas.first { it.x == x && it.y == y }
    }

    fun getAreaOrNull(x: Int, y: Int): Area? {
        return areas.firstOrNull() { it.x == x && it.y == y }
    }

    fun connect(from: Area, to: Area) {
        val areaConnection = AreaConnection(from, to)
        if (!map.containsKey(from)) {
            map.put(from, Array())
        }
        map.get(from).add(areaConnection)
        areaConnections.add(areaConnection)
    }

    fun findPath(from: Area, to: Area): GraphPath<Area> {
        val path = DefaultGraphPath<Area>()
        IndexedAStarPathFinder(this).searchNodePath(from, to, areaHeuristic, path)
        return path
    }

    override fun getConnections(fromNode: Area): Array<Connection<Area>> = if (map.containsKey(fromNode)) map.get(fromNode) else Array()

    override fun getIndex(node: Area): Int = node.index

    override fun getNodeCount(): Int = lastNodeIndex
}