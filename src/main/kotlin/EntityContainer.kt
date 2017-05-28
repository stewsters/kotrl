import java.util.*


class EntityContainer {

    internal var components: ArrayList<Component> = ArrayList()

    fun send(message: Int) {

        for (i in components.indices) {

            components[i].receive(message)

        }
    }

    fun add(component: Component) {
        components.add(component)
    }
}