package com.robosolutions.temixtopsmarket.ui.password

import android.os.Bundle
import android.view.View
import androidx.core.widget.doOnTextChanged
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.navArgs
import com.google.android.material.transition.MaterialSharedAxis
import com.robosolutions.temixtopsmarket.R
import com.robosolutions.temixtopsmarket.databinding.FragmentPasswordBinding
import com.robosolutions.temixtopsmarket.extensions.navigate
import com.robosolutions.temixtopsmarket.extensions.singleLatest
import com.robosolutions.temixtopsmarket.ui.base.BindingViewModelFragment
import com.robosolutions.temixtopsmarket.utils.PasswordOperation
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.android.synthetic.main.fragment_password.*
import kotlinx.coroutines.launch
import timber.log.Timber

@AndroidEntryPoint
class PasswordFragment :
    BindingViewModelFragment<FragmentPasswordBinding, PasswordFragmentViewModel>() {

    private val args by navArgs<PasswordFragmentArgs>()

    override val viewModel by viewModels<PasswordFragmentViewModel>()

    override val layoutId = R.layout.fragment_password

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        enterTransition = MaterialSharedAxis(MaterialSharedAxis.Z, true)
        returnTransition = MaterialSharedAxis(MaterialSharedAxis.Z, false)
    }

    override fun onBinding(binding: FragmentPasswordBinding) {
        super.onBinding(binding)

        // Set the password operation
        lifecycleScope.launch {
            viewModel.persistedPassword.singleLatest().also { pass ->
                when {
                    args.changePassword -> PasswordOperation.CHANGE_PASSWORD
                    pass.isBlank() -> PasswordOperation.INITIAL_PASSWORD
                    else -> PasswordOperation.INPUT_PASSWORD
                }.also {
                    Timber.d("Password operation: $it")

                    viewModel.submitOperation(it)
                }
            }
        }
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        editTextOldPassword.doOnTextChanged { text, _, _, _ ->
            viewModel.oldPasswordInputChange(text?.toString() ?: "")
        }

        editTextConfirmPassword.doOnTextChanged { text, _, _, _ ->
            viewModel.confirmPasswordInputChange(text?.toString() ?: "")
        }

        editTextPassword.doOnTextChanged { text, _, _, _ ->
            viewModel.passwordInputChange(text?.toString() ?: "")
        }
    }

    fun onPasswordSubmit(v: View) {
        lifecycleScope.launchWhenCreated {
            Timber.d("Submitting password for checking")

            viewModel.requirements.singleLatest().also { (satisfied, passwordInput, op) ->
                Timber.d("Password is satisfied: $satisfied")

                if (satisfied) {
                    if (op != PasswordOperation.INPUT_PASSWORD) {
                        viewModel.savePassword(passwordInput)
                    }

                    v.navigate(R.id.action_passwordFragment_to_adminFragment)
                }
            }
        }
    }

}