package com.robosolutions.temixtopsmarket.ui.password

import android.content.Context
import androidx.hilt.lifecycle.ViewModelInject
import androidx.lifecycle.ViewModel
import androidx.lifecycle.asFlow
import androidx.lifecycle.asLiveData
import androidx.lifecycle.viewModelScope
import com.robosolutions.temixtopsmarket.R
import com.robosolutions.temixtopsmarket.extensions.*
import com.robosolutions.temixtopsmarket.preference.PreferenceRepository
import com.robosolutions.temixtopsmarket.utils.CryptoManager
import com.robosolutions.temixtopsmarket.utils.PasswordOperation
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.FlowPreview
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import timber.log.Timber

@OptIn(FlowPreview::class)
class PasswordFragmentViewModel @ViewModelInject constructor(
    @ApplicationContext private val context: Context,
    private val repository: PreferenceRepository
) : ViewModel() {
    val persistedPassword = repository.password.map {
        CryptoManager.decrypt(it.password, it.iv)
    }

    private val minPasswordLength = context.resources.getInteger(R.integer.min_password_length)

    private val passwordInput = MutableStateFlow("")

    private val confirmationPasswordInput = MutableStateFlow("")

    private val oldPasswordInput = MutableStateFlow("")

    private val operation = MutableStateFlow(PasswordOperation.INPUT_PASSWORD)

    private val _passwordError = MutableSharedFlow<Int>()
    val passwordError = _passwordError.mapStringResource(context).asLiveData()

    private val _oldPasswordError = MutableSharedFlow<Int>()
    val oldPasswordError = _oldPasswordError.mapStringResource(context).asLiveData()

    private val _confirmPasswordError = MutableSharedFlow<Int>()
    val confirmPasswordError = _confirmPasswordError.mapStringResource(context).asLiveData()

    val passwordLengthSatisfied = passwordInput
        .map { it.length >= minPasswordLength }
        .asLiveData()

    val passwordMixedCaseSatisfied = passwordInput
        .map { it.isMixedCase() }
        .asLiveData()

    /** If the confirmation password matches the password input. */
    private val confirmationPasswordCorrect = confirmationPasswordInput
        .combineAndCheckEqualNotBlank(passwordInput)

    /** If the password satisfies the password requirements. */
    private val passwordSatisfied = passwordLengthSatisfied.asFlow()
        .combine(passwordMixedCaseSatisfied.asFlow()) { length, mixedCase -> length && mixedCase }

    /** If the password input is correct. */
    private val passwordCorrect =
        passwordInput.combineAndCheckEqualNotBlank(persistedPassword)

    /** If the old password matches the current password. */
    private val oldPasswordCorrect =
        oldPasswordInput.combineAndCheckEqualNotBlank(persistedPassword)

    /** The requirements that needs to be fulfilled. */
    val requirements = operation.flatMapConcat { op ->
        when (op) {
            PasswordOperation.INPUT_PASSWORD -> passwordCorrect.map {
                if (!it) _passwordError.emit(R.string.error_incorrect_password)

                listOf(it)
            }

            PasswordOperation.INITIAL_PASSWORD ->
                passwordSatisfied.combineToPair(confirmationPasswordCorrect).map {
                    if (!it.first) _passwordError.emit(R.string.error_weak_password)
                    if (!it.second) _confirmPasswordError.emit(R.string.error_confirmation_password)

                    it.toList()
                }

            PasswordOperation.CHANGE_PASSWORD -> oldPasswordCorrect.combineToTriple(
                passwordSatisfied,
                confirmationPasswordCorrect
            ).map {
                if (!it.first) _oldPasswordError.emit(R.string.error_incorrect_password)
                if (!it.second) _passwordError.emit(R.string.error_weak_password)
                if (!it.third) _confirmPasswordError.emit(R.string.error_confirmation_password)

                it.toList()
            }
        }
    }.map { list ->
        list.all { it }.also {
            Timber.d("Requirements for password fulfilled? $it")
        }
    }.combineToTriple(passwordInput, operation)

    fun passwordInputChange(input: String) = passwordInput updateTo input

    fun oldPasswordInputChange(input: String) = oldPasswordInput updateTo input

    fun confirmPasswordInputChange(input: String) = confirmationPasswordInput updateTo input

    fun submitOperation(op: PasswordOperation) = operation updateTo op

    /**
     * Saves password to the data store.
     *
     * @param password The password to save.
     */
    fun savePassword(password: String) = viewModelScope.launch {
        CryptoManager.encrypt(password).also { (cipher, iv) ->
            repository.savePassword(cipher, iv)
        }
    }
}