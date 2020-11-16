package com.robosolutions.temixtopsmarket.ui.base

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.databinding.ViewDataBinding
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import com.robosolutions.temixtopsmarket.extensions.executePendingBindings
import com.robosolutions.temixtopsmarket.ui.activity.MainActivityViewModel

abstract class BindingFragment<T : ViewDataBinding> : Fragment() {
    protected val mainViewModel by activityViewModels<MainActivityViewModel>()

    private lateinit var binding: T

    abstract val layoutId: Int

    /** Whether the screen should display the header part (title with back and home button). */
    open val useHeader = true

    /** English title string resource id. */
    open val titleIdEn: Int? = null

    /** Thai title string resource id. */
    open val titleIdThai: Int? = null

    final override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Update title
        mainViewModel.updateHeaderTitle(titleIdEn, titleIdThai)

        mainViewModel.updateHeader(useHeader)

        binding = DataBindingUtil.inflate(inflater, layoutId, container, false)
        binding.executePendingBindings {
            // Binding using reflection
            onBindingReflection(this)

            // Manual binding
            onBinding(this)

            lifecycleOwner = viewLifecycleOwner
        }

        return binding.root
    }

    /**
     * Assigns data to the binding object.
     * After assignment, [ViewDataBinding.executePendingBindings] will be called.
     *
     * @param binding The binding object.
     */
    protected open fun onBinding(binding: T) {}

    /**
     * Assigns data to the binding object via reflection mechanism. This method should **NOT** be
     * exposed to a non-abstract class.
     *
     * @param binding The binding object.
     */
    protected open fun onBindingReflection(binding: T) {
        // Set fragment
        tryAssignBinding("setFragment", this::class.java, binding, this)

        // Set shared view model
        tryAssignBinding("setSharedModel", mainViewModel::class.java, binding, mainViewModel)
    }

    /**
     * Tries to bind an object if it is declared in the binding.
     *
     * @param method The generated method name for setting the object.
     * @param argClass The class type for the object to set.
     * @param binding The binding object.
     * @param value The object to bind.
     */
    protected fun tryAssignBinding(method: String, argClass: Class<*>, binding: T, value: Any) {
        try {
            binding::class.java.getMethod(method, argClass).invoke(binding, value)
        } catch (e: NoSuchMethodException) {
            // Do nothing
        }
    }
}