package com.dicoding.storyapp.custom_view

import android.app.AlertDialog
import android.content.Context
import android.os.Bundle
import android.widget.Button
import com.dicoding.storyapp.R

class ErrorDialog(context: Context): AlertDialog(context) {
    init {
        setCancelable(false)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.error_layout)

        val dismissButton = findViewById<Button>(R.id.dismissButton)
        dismissButton.setOnClickListener {
            dismiss()
        }
    }
}