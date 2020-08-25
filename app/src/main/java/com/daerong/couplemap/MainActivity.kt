package com.daerong.couplemap

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.daerong.couplemap.connect.CreateFragment
import com.daerong.couplemap.connect.InviteFragment
import com.google.android.material.tabs.TabLayout
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : AppCompatActivity() {
    private val fragmentManager=supportFragmentManager
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
    }
}