package ru.sangel.zaya.data.contacts.api

class StubFavoritesSource: FavoritesSource {
    override suspend fun addFavorite(phone: String) {

    }

    override suspend fun delteFavorite(phone: String) {
    }
}