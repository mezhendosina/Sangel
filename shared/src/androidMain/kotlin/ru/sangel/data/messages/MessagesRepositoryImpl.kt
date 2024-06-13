package ru.sangel.data.messages

import android.content.Context
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.withContext
import org.koin.core.component.KoinComponent
import org.koin.core.context.GlobalContext.get
import org.koin.java.KoinJavaComponent.inject
import ru.sangel.R
import ru.sangel.data.contacts.ContactsRepository
import ru.sangel.data.settings.AppPrefs

actual class MessagesRepositoryImpl() : MessagesRepository, KoinComponent {
    private val messagesSource: MessagesSource by inject(MessagesSource::class.java)
    private val contactsRepository: ContactsRepository by inject(ContactsRepository::class.java)
    private val appPrefs: AppPrefs by inject(AppPrefs::class.java)

    override suspend fun sendMessageToFavorites() {
        val favs = contactsRepository.favorites.first()
        val context = getKoin().get() as Context

        favs.forEach {
            withContext(Dispatchers.IO) {
                messagesSource.sendSms(
                    phoneNumber = it.phoneNumber,
                    context.getString(R.string.sms_message, getLocation()),
                )
            }
        }
    }

    override suspend fun sendMessageToPolice() {
    }

    override suspend fun sendMessagesToNearUsers() {
    }

    private fun getLocation(): String = "TODO: get location"
}
