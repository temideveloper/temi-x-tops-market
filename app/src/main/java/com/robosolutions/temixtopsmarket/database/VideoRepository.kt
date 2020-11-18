package com.robosolutions.temixtopsmarket.database

import javax.inject.Inject

class VideoRepository @Inject constructor(
    private val dao: PromotionVideoDao
) : PromotionVideoDao by dao