package com.hitss.ui.launch

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.activity.viewModels
import com.hitss.databinding.ActivityLaunchBinding
import com.hitss.ui.dialogs.ProgressDialog
import com.hitss.ui.main.MainActivity
import com.hitss.utils.observe

class LaunchActivity : AppCompatActivity() {
    private val progress: ProgressDialog by lazy {
        ProgressDialog(this)
    }

    private val launchViewModel: LaunchViewModel by viewModels()

    private lateinit var binding: ActivityLaunchBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityLaunchBinding.inflate(layoutInflater)
        setContentView(binding.root)
        progress.show()
        setObservers()
    }

    private fun setObservers() {
        observe(launchViewModel.launch, ::handleLaunch)
    }

    // This function handles the launch of an activity in the app.
    // If the argument "b" is true, it dismisses the progress dialog, starts the MainActivity,
    // and finishes the current activity.
    private fun handleLaunch(b: Boolean) {
        if (b) {
            progress.dismiss()
            startActivity(Intent(this, MainActivity::class.java))
            finish()
        }
    }
}