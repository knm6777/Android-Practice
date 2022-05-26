package com.example.myapplication

import android.content.DialogInterface
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.Toast
import androidx.appcompat.app.AlertDialog

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val button = findViewById<Button>(R.id.button);
        button.setOnClickListener {
            //Toast.makeText(this, "Test", Toast.LENGTH_SHORT).show();

            with (AlertDialog.Builder(this)) {
                setTitle("Title")
                setMessage("Message")
                setIcon(R.mipmap.ic_launcher)
                setPositiveButton("OK") { p0: DialogInterface?, p1: Int ->
                    Toast.makeText(applicationContext, "Close Dialog", Toast.LENGTH_SHORT).show()
                }
                create()
            }.show()
        }
    }
}