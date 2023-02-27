package com.android.example.pw1

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.Menu
import android.view.MenuInflater
import android.view.MenuItem

class MainActivity2 : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main2)
    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        val inflater: MenuInflater = menuInflater
        inflater.inflate(R.menu.gallery_menu, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        // Handle item selection
        return when (item.itemId) {
            R.id.take_images -> {
                val record = Intent(this, MainActivity::class.java)
                startActivity(record)
                true
            }
            R.id.delete -> {

                true
            }
            R.id.audio -> {
                val record = Intent(this, MainActivity3::class.java)
                startActivity(record)
                true
            }
            else -> super.onOptionsItemSelected(item)
        }
    }
}