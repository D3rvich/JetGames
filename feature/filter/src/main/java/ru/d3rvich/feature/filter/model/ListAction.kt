package ru.d3rvich.feature.filter.model

internal sealed interface ListAction<out T : Any> {
    data class AddItem<T : Any>(val item: T) : ListAction<T>
    data class RemoveItem<T : Any>(val item: T) : ListAction<T>
    class Clear<T : Any> : ListAction<T>
}

internal fun <E : Any> MutableSet<E>.update(action: ListAction<E>): MutableSet<E> = this.apply {
    when (action) {
        is ListAction.AddItem -> add(action.item)
        is ListAction.Clear -> clear()
        is ListAction.RemoveItem -> remove(action.item)
    }
}