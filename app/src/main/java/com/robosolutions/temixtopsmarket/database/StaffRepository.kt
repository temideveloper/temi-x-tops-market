package com.robosolutions.temixtopsmarket.database

import javax.inject.Inject

class StaffRepository @Inject constructor(
    private val dao: ContactableStaffDao
) : ContactableStaffDao by dao