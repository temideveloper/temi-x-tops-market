package com.robosolutions.temixtopsmarket.ui.staff

import android.os.Bundle
import android.view.View
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.DividerItemDecoration
import com.robosolutions.temixtopsmarket.R
import com.robosolutions.temixtopsmarket.adapter.SettingsUserInfoAdapter
import com.robosolutions.temixtopsmarket.databinding.FragmentStaffsBinding
import com.robosolutions.temixtopsmarket.ui.base.BindingViewModelFragment
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class StaffsFragment : BindingViewModelFragment<FragmentStaffsBinding, StaffsFragmentViewModel>() {
    override val viewModel by viewModels<StaffsFragmentViewModel>()

    val userInfoAdapter by lazy {
        SettingsUserInfoAdapter { _, user, checked ->
            val id = user.userInfo.userId

            if (checked) {
                viewModel.saveStaff(id)
            } else {
                viewModel.removeStaff(id)
            }
        }
    }

    val listItemDecoration by lazy {
        DividerItemDecoration(requireContext(), DividerItemDecoration.VERTICAL)
    }

    override val layoutId = R.layout.fragment_staffs

    override val titleIdEn = R.string.title_staff_en
    override val titleIdThai = R.string.title_staff_th

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        viewModel.itemsToDisplay.observe(viewLifecycleOwner) { list ->
            userInfoAdapter.submitList(list.sortedBy { it.userInfo.name })
        }
    }
}