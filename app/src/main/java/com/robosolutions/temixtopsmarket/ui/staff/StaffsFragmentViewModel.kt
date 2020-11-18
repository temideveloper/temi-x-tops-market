package com.robosolutions.temixtopsmarket.ui.staff

import androidx.hilt.lifecycle.ViewModelInject
import androidx.lifecycle.asLiveData
import com.robosolutions.temixtopsmarket.adapter.UserInfoListItem
import com.robosolutions.temixtopsmarket.database.ContactableStaff
import com.robosolutions.temixtopsmarket.database.StaffRepository
import com.robosolutions.temixtopsmarket.extensions.contactsAndAdmin
import com.robosolutions.temixtopsmarket.extensions.robot
import com.robosolutions.temixtopsmarket.extensions.singleLatest
import com.robosolutions.temixtopsmarket.extensions.updateTo
import com.robosolutions.temixtopsmarket.ui.base.AppViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.distinctUntilChanged
import timber.log.Timber

class StaffsFragmentViewModel @ViewModelInject constructor(
    private val repository: StaffRepository,
) : AppViewModel() {
    private val allDatabaseItems = repository.getStaffs()

    private val allRobotItems = MutableStateFlow(robot.contactsAndAdmin)

    private val allItems =
        allRobotItems.combine(allDatabaseItems.distinctUntilChanged()) { listRobot, listDb ->
            listRobot.map { userInfo ->
                UserInfoListItem(
                    selected = userInfo.userId in listDb.map { it.userId },
                    userInfo = userInfo
                )
            }
        }

    private val searchQuery = MutableStateFlow("")

    val itemsToDisplay = searchQuery.combine(allItems) { query, items ->
        items.filter { it.userInfo.name.contains(query, true) }
    }.asLiveData()

    init {
        // Sync database with temi's contact
        viewModelLaunch {
            val currentDbList = allDatabaseItems.singleLatest().map { it.userId }
            val currentRobotContacts = robot.contactsAndAdmin.map { it.userId }

            currentDbList.filter { it !in currentRobotContacts }
                .also {
                    Timber.d("Removing ${it.size} obsolete contacts from local database")
                }
                .forEach(::removeStaff)
        }
    }

    fun queryList(query: String) = searchQuery updateTo query

    fun saveStaff(id: String) = viewModelLaunch { repository.insertStaff(ContactableStaff(id)) }

    fun removeStaff(id: String) = viewModelLaunch { repository.removeStaff(id) }
}