package ru.sangel.zaya.presentation.components.main.settings

import android.content.Context
import com.arkivanov.decompose.ComponentContext
import com.arkivanov.decompose.router.stack.ChildStack
import com.arkivanov.decompose.router.stack.StackNavigation
import com.arkivanov.decompose.router.stack.childStack
import com.arkivanov.decompose.router.stack.pop
import com.arkivanov.decompose.router.stack.push
import com.arkivanov.decompose.value.Value
import kotlinx.serialization.Serializable
import org.koin.core.component.KoinComponent
import org.koin.core.component.inject
import org.koin.java.KoinJavaComponent.inject
import ru.sangel.zaya.SETTINGS_STACK
import ru.sangel.zaya.data.contacts.ContactsRepository
import ru.sangel.zaya.data.settings.AppPrefs
import ru.sangel.zaya.presentation.components.main.settings.about.DefaultAboutAppComponent
import ru.sangel.zaya.presentation.components.main.settings.contacts.DefaultContactsComponent
import ru.sangel.zaya.presentation.components.main.settings.debug.DefaultDebugComponent
import ru.sangel.zaya.presentation.components.main.settings.privacy.DefaultPrivacyComponent
import ru.sangel.zaya.presentation.components.main.settings.profile.DefaultProfileComponent
import ru.sangel.zaya.presentation.components.main.settings.root.DefaultSettingsRootComponent

class DefaultSettingsComponent(
    private val componentContext: ComponentContext,
) : ru.sangel.zaya.presentation.components.main.settings.SettingsComponent,
    ComponentContext by componentContext,
    KoinComponent {
    private val contactsRepository: ContactsRepository by inject()
    private val appPrefs: AppPrefs by inject()

    private val context: Context by inject(Context::class.java)

    private val navigation = StackNavigation<Config>()
    override val stack: Value<ChildStack<*, ru.sangel.zaya.presentation.components.main.settings.SettingsComponent.Child>> =
        childStack(
            navigation,
            serializer = Config.serializer(),
            key = SETTINGS_STACK,
            initialConfiguration = Config.Root,
            handleBackButton = true,
            childFactory = ::child,
        )

    override fun onBack() {
        navigation.pop()
    }

    private fun child(
        config: Config,
        componentContext: ComponentContext,
    ): ru.sangel.zaya.presentation.components.main.settings.SettingsComponent.Child =
        when (config) {
            is Config.Root ->
                ru.sangel.zaya.presentation.components.main.settings.SettingsComponent.Child.Root(settingsRootComponent(componentContext))

            is Config.Account -> ru.sangel.zaya.presentation.components.main.settings.SettingsComponent.Child.Account(profileComponent())
            is Config.About -> ru.sangel.zaya.presentation.components.main.settings.SettingsComponent.Child.About(aboutAppComponent())
            is Config.Privacy -> ru.sangel.zaya.presentation.components.main.settings.SettingsComponent.Child.Privacy(privacyComponent(componentContext))
            is Config.Contacts ->
                ru.sangel.zaya.presentation.components.main.settings.SettingsComponent.Child.Contacts(
                    contactsComponent(componentContext),
                )

            is Config.Debug -> ru.sangel.zaya.presentation.components.main.settings.SettingsComponent.Child.Debug(debugComponent(componentContext))
        }

    private fun contactsComponent(componentContext: ComponentContext) =
        DefaultContactsComponent(
            componentContext,
            contactsRepository,
        )

    private fun privacyComponent(componentContext: ComponentContext) =
        DefaultPrivacyComponent(componentContext, contactsRepository, appPrefs)

    private fun settingsRootComponent(componentContext: ComponentContext) =
        DefaultSettingsRootComponent(
            componentContext,
            { navigation.push(Config.Account) },
            { navigation.push(Config.Privacy) },
            { navigation.push(Config.About) },
            { navigation.push(Config.Contacts) },
            { navigation.push(Config.Debug) },
        )

    private fun debugComponent(componentContext: ComponentContext) = DefaultDebugComponent(componentContext)

    private fun aboutAppComponent() = DefaultAboutAppComponent()

    private fun profileComponent() = DefaultProfileComponent(componentContext)

    @Serializable
    private sealed interface Config {
        @Serializable
        data object Root : Config

        @Serializable
        data object Account : Config

        @Serializable
        data object About : Config

        @Serializable
        data object Privacy : Config

        @Serializable
        data object Contacts : Config

        @Serializable
        data object Debug : Config
    }
}
