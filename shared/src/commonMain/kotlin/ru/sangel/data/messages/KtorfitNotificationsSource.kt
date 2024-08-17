package ru.sangel.data.messages

import de.jensklingenberg.ktorfit.Ktorfit
import ru.sangel.data.BaseKtorfitSource

class KtorfitNotificationsSource(
    private val ktorfit: Ktorfit
) : NotificationsSource, BaseKtorfitSource() {
    private val notificationsApi = ktorfit.createNotificationsApi()

    override suspend fun sendInDanger() = wrapKtorfitExceptions {
        notificationsApi.sendInDanger()
    }

    override suspend fun sendSaving() = wrapKtorfitExceptions {
        notificationsApi.sendSaving()
    }
    override suspend fun sendOk() = wrapKtorfitExceptions {
        notificationsApi.sendOk()
    }
}