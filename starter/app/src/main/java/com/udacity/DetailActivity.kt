package com.udacity

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.udacity.databinding.ActivityDetailBinding

class DetailActivity : AppCompatActivity() {

    private lateinit var binding: ActivityDetailBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityDetailBinding.inflate(layoutInflater)
        setContentView(binding.root)
        setSupportActionBar(binding.toolbar)


        val fileName = intent.getStringExtra(FILE_NAME_KEY)
        val status = intent.getStringExtra(STATUS_KEY)

        binding.contentDetail.txtFilename.text=fileName
        binding.contentDetail.txtStatus.text=status

        binding.contentDetail.btnOk.setOnClickListener{
            startActivity(Intent(this,MainActivity::class.java))
        }
    }
}
