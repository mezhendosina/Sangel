package ru.sangel.zaya.data.map

import com.yandex.mapkit.map.CameraPosition
import kotlinx.coroutines.flow.StateFlow

interface MapRepository {
    val zoom: StateFlow<Float>

    val cameraPosition: StateFlow<CameraPosition?>

    fun plusZoom()

    fun minusZoom()

    fun updateCameraPosition(cameraPosition: CameraPosition)

    fun setZoom(zoom: Float)

    fun initMap()

    /**
     * Единоразово отдает местоположение и представляет его ссылкой на Яндекс Карты
     */
    suspend fun getLinkLocation(): String?

    suspend fun updateLocation(
        latitude: Double,
        longtitude: Double,
    )
}
