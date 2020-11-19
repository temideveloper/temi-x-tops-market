package com.robosolutions.temixtopsmarket.ui.staff

import androidx.hilt.lifecycle.ViewModelInject
import androidx.lifecycle.ViewModel
import androidx.lifecycle.asLiveData
import com.robosolutions.temixtopsmarket.database.StaffRepository
import com.robosolutions.temixtopsmarket.extensions.contactsAndAdmin
import com.robosolutions.temixtopsmarket.extensions.robot
import kotlinx.coroutines.flow.map

class ContactStaffFragmentViewModel @ViewModelInject constructor(
    private val repository: StaffRepository
) : ViewModel() {
    val contactStaffs = repository.getStaffs()
        .map { list -> list.map { it.userId } }
        .map { staffsToShow ->
            robot.contactsAndAdmin.filter {
                it.userId in staffsToShow
            }
        }.asLiveData()
}