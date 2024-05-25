package com.example.blurbackgroundpractice.bottom_sheet.ui.activity

import android.os.Bundle
import android.view.Menu
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.databinding.DataBindingUtil
import com.example.blurbackgroundpractice.R
import com.example.blurbackgroundpractice.databinding.ActivityMainBinding

/**
 * Main entry point to perform all the operations
 * Application start with this activity
 * */
class MainActivity : AppCompatActivity() {

    // Binding object to interact with view
    private lateinit var binding: ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        // initializing the binding object
        binding = DataBindingUtil.setContentView(this, R.layout.activity_main)

        // Nav host fragment
        val navHomeFragment = supportFragmentManager.findFragmentById(R.id.nav_host_fragment)
        setSupportActionBar(binding.toolbar)
        window.statusBarColor = ContextCompat.getColor(this, R.color.colorPrimaryDark)
    }

    // Used to inflate menu items for toolbar
    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(
            R.menu.menu_home_screen, menu
        )
        return true
    }
}

