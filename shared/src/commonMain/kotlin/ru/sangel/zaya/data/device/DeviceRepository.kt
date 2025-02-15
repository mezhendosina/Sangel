package ru.sangel.zaya.data.device

import com.juul.kable.Advertisement
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.SharedFlow
import ru.sangel.zaya.data.device.db.DeviceEntity
import ru.sangel.zaya.presentation.entities.DeviceUiEntity

interface DeviceRepository {
    val pairedDevices: Flow<List<DeviceUiEntity>>
    val emergency: SharedFlow<Boolean>

    suspend fun setEmergency(value: Boolean)

    suspend fun getAvaliableDevices(): Flow<Advertisement>

    suspend fun getDeviceFromDb(address: String): DeviceEntity?

    suspend fun connect(
        address: String,
        onConnected: () -> Unit,
    )
}
