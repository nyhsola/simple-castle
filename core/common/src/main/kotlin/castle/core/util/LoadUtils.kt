package castle.core.util

import com.badlogic.gdx.utils.Json

class LoadUtils {
    companion object {
        private val json = Json()

        @Suppress("UNCHECKED_CAST")
        fun <T> loadList(path: String, clazz: Class<T>): List<T> {
            return json.fromJson(List::class.java, clazz, readResource(path)).map { it!! as T }
        }

        private fun readResource(path: String): String {
            return LoadUtils::class.java.getResource(path)!!.readText()
        }
    }
}