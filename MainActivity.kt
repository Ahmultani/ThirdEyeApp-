package com.thirdeye

import android.Manifest
import android.content.Intent
import android.content.pm.PackageManager
import android.os.Bundle
import android.widget.Button
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat

class MainActivity : AppCompatActivity() {
    private lateinit var startButton: Button
    private lateinit var stopButton: Button

    private val permissions = arrayOf(
        Manifest.permission.CAMERA,
        Manifest.permission.RECORD_AUDIO,
        Manifest.permission.FOREGROUND_SERVICE,
        Manifest.permission.WRITE_EXTERNAL_STORAGE
    )

    private val requestPermissionLauncher =
        registerForActivityResult(ActivityResultContracts.RequestMultiplePermissions()) { permissions ->
            val allGranted = permissions.values.all { it }
            if (!allGranted) {
                finish()
            }
        }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        startButton = findViewById(R.id.startRecording)
        stopButton = findViewById(R.id.stopRecording)

        if (!hasPermissions()) {
            requestPermissionLauncher.launch(permissions)
        }

        startButton.setOnClickListener {
            val serviceIntent = Intent(this, RecordingService::class.java)
            ContextCompat.startForegroundService(this, serviceIntent)
        }

        stopButton.setOnClickListener {
            val serviceIntent = Intent(this, RecordingService::class.java)
            stopService(serviceIntent)
        }
    }

    private fun hasPermissions(): Boolean {
        return permissions.all {
            ContextCompat.checkSelfPermission(this, it) == PackageManager.PERMISSION_GRANTED
        }
    }
}