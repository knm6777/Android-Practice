package com.example.firebasecloudmessage

import android.content.ClipData
import android.content.ClipboardManager
import android.content.Context
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import com.example.firebasecloudmessage.databinding.ActivityMainBinding
import com.google.firebase.messaging.FirebaseMessaging

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val intent = getIntent()
        val message = intent.getStringExtra("message")
        binding.message.text = message

        FirebaseMessaging.getInstance().token.addOnCompleteListener {
            binding.textFCMToken.text = if (it.isSuccessful) it.result else "Token Error!"

            // copy FCM token to clipboard
            val clipboard = getSystemService(Context.CLIPBOARD_SERVICE) as ClipboardManager
            val clip = ClipData.newPlainText("FCM Token", binding.textFCMToken.text)
            clipboard.setPrimaryClip(clip)

            // write to logcat
            Log.d(MyFirebaseMessagingService.TAG, "FCM token: ${binding.textFCMToken.text}")
        }
    }
}