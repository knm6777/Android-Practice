package com.example.seasonimage

import android.content.Intent
import android.graphics.BitmapFactory
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.ImageView
import android.widget.Toast
import com.example.seasonimage.databinding.ActivityMainBinding
import com.google.firebase.ktx.Firebase
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.remoteconfig.ktx.remoteConfig
import com.google.firebase.remoteconfig.ktx.remoteConfigSettings
import com.google.firebase.storage.FirebaseStorage
import com.google.firebase.storage.StorageReference
import com.google.firebase.storage.ktx.storage
import java.lang.StringBuilder

class MainActivity : AppCompatActivity() {
    lateinit var auth: FirebaseAuth
    lateinit var storage: FirebaseStorage
    lateinit var storageReference: StorageReference

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val configSettings = remoteConfigSettings {
            minimumFetchIntervalInSeconds = 1 // For test purpose only, 3600 seconds for production
        }

        val remoteConfig = Firebase.remoteConfig
        remoteConfig.setConfigSettingsAsync(configSettings)
        remoteConfig.setDefaultsAsync(R.xml.remote_config_defaults)

        auth = Firebase.auth
        if (auth.currentUser == null) {
            startActivity(
                Intent(this, LoginActivity::class.java)
            )
            finish()
        }
        Firebase.auth.currentUser ?: finish()
        storage = Firebase.storage

        storageReference = storage.reference // reference to root
        val season = remoteConfig.getString("season")
        val sb = StringBuilder()
        sb.append("images/").append(season).append(".jpg")
        val seasonVal = sb.toString()
        val imageRef1 = storageReference.child(seasonVal)
        displayImageRef(imageRef1, binding.seasonImage)

        binding.button.setOnClickListener{
            remoteConfig.fetchAndActivate()
                    .addOnCompleteListener(this) {
                        val season = remoteConfig.getString("season")
                        val sb = StringBuilder()
                        sb.append("images/").append(season).append(".jpg")
                        val seasonVal = sb.toString()
                        val imageRef2 = storageReference.child(seasonVal)
                        displayImageRef(imageRef2, binding.seasonImage)
                    }
        }

    }

    private fun displayImageRef(imageRef: StorageReference?, view: ImageView) {
        imageRef?.getBytes(Long.MAX_VALUE)?.addOnSuccessListener {
            val bmp = BitmapFactory.decodeByteArray(it, 0, it.size)
            view.setImageBitmap(bmp)
        }?.addOnFailureListener {
            // Failed to download the image
            //Toast.makeText(this@MainActivity, "Image Download Failed", Toast.LENGTH_SHORT).show()
        }
    }
}