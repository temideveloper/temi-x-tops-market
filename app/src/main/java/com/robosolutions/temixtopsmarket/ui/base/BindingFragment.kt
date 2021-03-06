package com.robosolutions.temixtopsmarket.ui.base

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.databinding.ViewDataBinding
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import com.robosolutions.temixtopsmarket.R
import com.robosolutions.temixtopsmarket.extensions.executePendingBindings
import com.robosolutions.temixtopsmarket.extensions.logAnalyticsScreenEvent
import com.robosolutions.temixtopsmarket.ui.activity.MainActivityViewModel
import com.robosolutions.temixtopsmarket.utils.tryAssignBinding

abstract class BindingFragment<T : ViewDataBinding> : Fragment() {
    protected val mainViewModel by activityViewModels<MainActivityViewModel>()

    private lateinit var binding: T

    abstract val layoutId: Int

    /** String resource id to speak when the fragment is created. */
    open val entranceSpeechId: Int? = null

    open val entranceSpeechArgs: Array<Any?>? = null

    /** Whether the screen should display the header part (title with back and home button). */
    open val useHeader = true

    /** English title string resource id. */
    open val titleIdEn: Int? = null

    /** Thai title string resource id. */
    open val titleIdThai: Int? = null

    /** Header image id to use. */
    open val headerImage: Int? = null

    /** Padding for the content of the screen. */
    open val contentPadding: Int? = R.dimen.content_padding

    /** Header home button. */
    open val showHomeButton = true

    /** Header close button. */
    open val showCloseButton = true

    /** Header send back button. */
    open val showSendBackButton = false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        entranceSpeechId?.let {
            mainViewModel.requestTts(it, *(entranceSpeechArgs ?: arrayOf()))
        }
    }

    final override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        mainViewModel.run {
            // Update header
            updateHeaderTitle(titleIdEn, titleIdThai)
            updateHeaderImageId(headerImage)
            updateHeader(useHeader)
            showHomeButton(this@BindingFragment.showHomeButton)
            showCloseButton(this@BindingFragment.showCloseButton)
            showSendBackButton(this@BindingFragment.showSendBackButton)

            updateContentPadding(this@BindingFragment.contentPadding)
        }

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

    override fun onResume() {
        super.onResume()

        logAnalyticsScreenEvent()
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

    protected fun updateThaiTitle(title: String) = mainViewModel.updateHeaderThai(title)

    protected fun updateEnglishTitle(title: String) = mainViewModel.updateHeaderEnglish(title)
}