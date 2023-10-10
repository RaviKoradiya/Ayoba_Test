package com.ayoba.ui

import android.os.Bundle
import android.view.MenuItem
import androidx.appcompat.app.AppCompatActivity
import androidx.navigation.NavController
import androidx.navigation.Navigation
import com.ayoba.databinding.ActivityMainBinding


class MainActivity : AppCompatActivity() {

    lateinit var mBinding: ActivityMainBinding

    private val mNavController: NavController
        get() = Navigation.findNavController(this, mBinding.navHostFragment.id)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        mBinding = ActivityMainBinding.inflate(layoutInflater)

        setContentView(mBinding.root)
    }

    override fun onPostCreate(savedInstanceState: Bundle?) {
        super.onPostCreate(savedInstanceState)

        mNavController.addOnDestinationChangedListener { controller, destination, _ ->
            supportActionBar?.title = ""
            supportActionBar?.subtitle = ""

            if (controller.previousBackStackEntry != null) {
                supportActionBar?.setDisplayHomeAsUpEnabled(true)
            } else {
                supportActionBar?.setDisplayHomeAsUpEnabled(false)
            }
        }
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            android.R.id.home -> {
                onBackPressedDispatcher.onBackPressed()
                return true
            }
        }
        return super.onOptionsItemSelected(item)
    }
}

