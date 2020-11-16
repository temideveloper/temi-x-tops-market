package com.robosolutions.temixtopsmarket.ui.activity

import android.os.Bundle
import android.view.View
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import androidx.navigation.NavController
import androidx.navigation.findNavController
import com.robosolutions.temixtopsmarket.R
import com.robosolutions.temixtopsmarket.databinding.ActivityMainBinding
import com.robosolutions.temixtopsmarket.extensions.executePendingBindings
import com.robosolutions.temixtopsmarket.utils.isNightMode
import com.robosolutions.temixtopsmarket.utils.switchNightMode
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : AppCompatActivity() {

    private val mainViewModel by viewModels<MainActivityViewModel>()

    private lateinit var binding: ActivityMainBinding

    private val navController: NavController by lazy { findNavController(R.id.navHostFragment) }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        if (!isNightMode) switchNightMode(true)

        binding = DataBindingUtil.setContentView(this, R.layout.activity_main)
        binding.executePendingBindings {
            lifecycleOwner = this@MainActivity
            viewModel = mainViewModel
        }
    }

    @Suppress("UNUSED_PARAMETER")
    fun onClickBack(v: View) = onBackPressed()

    @Suppress("UNUSED_PARAMETER")
    fun onClickHome(v: View) {
        navController.popBackStack(R.id.homeFragment, false)
    }
}