package ru.sangel.zaya.data.device.api

class StubDeviceSource: DeviceSource {
    override suspend fun addDevice(macAddress: String) {

    }

    override suspend fun deleteDevice(macAddress: String) {
    }
}