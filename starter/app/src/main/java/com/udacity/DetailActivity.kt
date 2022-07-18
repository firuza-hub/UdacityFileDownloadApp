package com.udacity

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import kotlinx.android.synthetic.main.activity_detail.*

class DetailActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_detail)
        setSupportActionBar(toolbar)

        val tvFileName = findViewById<TextView>(R.id.tvFileName)
        val tvStatus = findViewById<TextView>(R.id.tvStatus)
        val btnOk = findViewById<Button>(R.id.btnOk)
        val fileName = intent.getStringExtra("fileName").toString()

        tvFileName.text = fileName
        tvStatus.text = intent.getStringExtra("downloadStatus").toString()


        btnOk.setOnClickListener {
            val mainIntent = Intent(applicationContext, MainActivity::class.java)
            startActivity(mainIntent)
        }
    }
}
