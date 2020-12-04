package com.robosolutions.temixtopsmarket.ui.staff

import android.os.Bundle
import android.view.View
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.android.material.transition.MaterialSharedAxis
import com.robosolutions.temixtopsmarket.R
import com.robosolutions.temixtopsmarket.adapter.UserInfoAdapter
import com.robosolutions.temixtopsmarket.databinding.FragmentContactStaffBinding
import com.robosolutions.temixtopsmarket.extensions.robot
import com.robosolutions.temixtopsmarket.ui.base.BindingViewModelFragment
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.android.synthetic.main.fragment_contact_staff.*
import timber.log.Timber

@AndroidEntryPoint
class ContactStaffFragment :
    BindingViewModelFragment<FragmentContactStaffBinding, ContactStaffFragmentViewModel>() {

    override val viewModel by viewModels<ContactStaffFragmentViewModel>()

    override val showHomeButton = false
    override val showSendBackButton = true

    val adapter by lazy {
        UserInfoAdapter {
            Timber.d("Starting video call with staff ${it.name}")
            robot.startTelepresence(displayName = it.name, peerId = it.userId)
        }
    }

    override val layoutId = R.layout.fragment_contact_staff

    override val titleIdEn = R.string.title_contact_staff_en
    override val titleIdThai = R.string.title_contact_staff_th

    override val entranceSpeechId = R.string.tts_contact_staff

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        enterTransition = MaterialSharedAxis(MaterialSharedAxis.Z, true)
        returnTransition = MaterialSharedAxis(MaterialSharedAxis.Z, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        recyclerView.layoutManager =
            LinearLayoutManager(requireContext(), LinearLayoutManager.HORIZONTAL, false)

        viewModel.contactStaffs.observe(viewLifecycleOwner) { adapter.submitList(it) }
    }
}