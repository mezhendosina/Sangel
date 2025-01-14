package ru.sangel.zaya.presentation.components.login.signIn

import com.arkivanov.decompose.ComponentContext
import com.arkivanov.decompose.value.MutableValue
import com.arkivanov.decompose.value.Value
import com.arkivanov.decompose.value.update
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import ru.sangel.zaya.data.auth.AuthRepository
import ru.sangel.zaya.data.firebase.FirebaseRepository
import ru.sangel.zaya.presentation.entities.States
import ru.sangel.zaya.presentation.utils.coroutineExceptionHandler

class DefaultSignInComponent(
    private val authRepository: AuthRepository,
    private val firebaseRepository: FirebaseRepository,
    private val componentContext: ComponentContext,
    private val toCheckCode: () -> Unit,
    private val toSignUp: (email: String) -> Unit,
) : SignInComponent,
    ComponentContext by componentContext {
    private val _model =
        MutableValue<SignInComponent.Model>(
            SignInComponent.Model(
                "",
                "",
                States.Loaded,
            ),
        )
    private val exceptionHandler =
        coroutineExceptionHandler { errorMessage ->
            if (errorMessage == null) return@coroutineExceptionHandler

            _model.update {
                it.copy(
                    state = States.Error(errorMessage),
                )
            }
        }

    override val model: Value<SignInComponent.Model> = _model

    override fun onEmailChange(email: String) {
        _model.update { it.copy(email = email) }
    }

    override fun onPasswordChange(password: String) {
        _model.update { it.copy(password = password) }
    }

    override fun signIn() {
        _model.update { it.copy(state = States.Loading) }
        val messagingToken = firebaseRepository.getMessagingToken()
        messagingToken.addOnSuccessListener { token ->
            CoroutineScope(Dispatchers.IO).launch(exceptionHandler) {
                authRepository.signIn(
                    token,
                    _model.value.email,
                    _model.value.password,
                )
                withContext(Dispatchers.Main) {
                    toCheckCode.invoke()
                }
            }
        }
        messagingToken.addOnFailureListener { exception ->
            _model.update { it.copy(state = States.Error(exception.localizedMessage ?: "")) }
        }
    }

    override fun toSignUp() = toSignUp(model.value.email)

    override fun toCheckCode() {
        toCheckCode.invoke()
    }
}
