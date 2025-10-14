package com.practicum.playlistmaker.common.component

interface EntityMapper<in FROM, out TO> {
    fun map(entity: FROM): TO
    fun mapList(entities: List<FROM>): List<TO> = entities.map { map(it) }
}