package com.demoo.ddhpolice

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.example.ddhpolice.databinding.ActivityProfileViewBinding

class profileView : AppCompatActivity() {
    lateinit var binding: ActivityProfileViewBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityProfileViewBinding.inflate(layoutInflater)
        setContentView(binding.root)

//        binding.
    }
}