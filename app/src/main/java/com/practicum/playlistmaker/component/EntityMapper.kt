package com.practicum.playlistmaker.component

class EntityMapper<T> {
    companion object {
        fun <T> toPage(
            data: Collection<T>,
            totalCount: Int,
            errors: List<Error> = emptyList()
        ): Page<T> {
            val meta = Meta(
                count = totalCount,
                errors = errors
            )
            return Page(meta, data)
        }
    }
}