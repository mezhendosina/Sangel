package ru.sangel.zaya.presentation

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.provider.Telephony
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch
import org.koin.core.component.KoinComponent
import org.koin.core.component.inject
import ru.sangel.zaya.CLARIFYING_MESSAGE_START
import ru.sangel.zaya.REQUIRING_INFO_REGEX
import ru.sangel.zaya.data.messages.MessagesRepository
import ru.sangel.zaya.data.settings.AppPrefs

class IncomingEmergencyReciever :
    BroadcastReceiver(),
    KoinComponent {
    private val clarrifyingMessageRegEx = Regex(CLARIFYING_MESSAGE_START + REQUIRING_INFO_REGEX)

    private val emergencyChat by inject<MessagesRepository>()
    private val appPrefs by inject<AppPrefs>()

    private val coroutineScope = CoroutineScope(Dispatchers.IO)

    override fun onReceive(
        context: Context?,
        intent: Intent?,
    ) {
        coroutineScope.launch(Dispatchers.Main) {
            val number =
                appPrefs.getValue(AppPrefs.EMERGENCY_INCOMING_PHONE_NUMBER).first() ?: return@launch
            val message =
                Telephony.Sms.Intents
                    .getMessagesFromIntent(intent)
                    .sortedBy { it.timestampMillis }
                    .filter { it.displayOriginatingAddress == number }
            message.forEach { smsMessage ->
                if (clarrifyingMessageRegEx.matches(smsMessage.displayMessageBody)) {
                    val listOfRequiringInfo =
                        smsMessage.displayMessageBody
                            .removePrefix(CLARIFYING_MESSAGE_START)
                            .split(",")
                            .map { word ->
                                word.dropWhile { it == ' ' }.dropLastWhile { it != ' ' }
                            }.mapNotNull { it.toMessateType() }
                    coroutineScope.launch {
                        emergencyChat.sendClarifyingMessageToPolice(*listOfRequiringInfo.toTypedArray())
                    }
                }
            }
        }
    }

    private fun String.toMessateType(): MessagesRepository.Companion.MessageType? =
        when (this) {
            "номер телефона" -> MessagesRepository.Companion.MessageType.PhoneNumber
            "отчество" -> MessagesRepository.Companion.MessageType.MiddleName
            "возраст" -> MessagesRepository.Companion.MessageType.Age
            else -> null
        }
}
