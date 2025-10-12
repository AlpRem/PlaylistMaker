package com.practicum.playlistmaker.common.component

data class Page<T> (val meta: Meta, val data: Collection<T>) {
    companion object {
        fun <T> empty(): Page<T> = Page(
            meta = Meta(count = 0, errors = emptyList()),
            data = emptyList()
        )

        fun <T> withError(errorMessage: String): Page<T> = Page(
            meta = Meta(count = 0, errors = listOf(Error(errorMessage))),
            data = emptyList()
        )
        fun <T> of(data: Collection<T>, totalCount: Int = data.size): Page<T> = Page(
            meta = Meta(count = totalCount, errors = emptyList()),
            data = data
        )
    }
    fun isEmpty(): Boolean = data.isEmpty()

    fun hasErrors(): Boolean = meta.errors.isNotEmpty()

    fun <R> map(transform: (T) -> R): Page<R> = Page(
        meta = meta.copy(),
        data = data.map(transform)
    )
}