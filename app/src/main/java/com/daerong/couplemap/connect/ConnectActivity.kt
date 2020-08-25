package com.daerong.couplemap.connect

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentStatePagerAdapter
import com.daerong.couplemap.R
import com.google.android.material.tabs.TabLayout
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import kotlinx.android.synthetic.main.activity_connect.*

class ConnectActivity : AppCompatActivity() {
    lateinit var user_sid:String
    var code:String? = null
    val db = Firebase.firestore


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_connect)
        init()
        val fragmentManager=supportFragmentManager
        tab_layout.addTab(tab_layout.newTab().setText("초대"))
        tab_layout.addTab(tab_layout.newTab().setText("생성"))
        val connectPagerAdapter=ConnectPagerAdapter(fragmentManager,2)
        view_pager.adapter=connectPagerAdapter
        tab_layout.addOnTabSelectedListener(object : TabLayout.OnTabSelectedListener{
            override fun onTabReselected(tab: TabLayout.Tab?) {
            }

            override fun onTabSelected(tab: TabLayout.Tab?) {
                view_pager.currentItem=tab!!.position
            }

            override fun onTabUnselected(tab: TabLayout.Tab?) {

            }
        })
        view_pager.addOnPageChangeListener(TabLayout.TabLayoutOnPageChangeListener(tab_layout))


        /*connect_add_btn.setOnClickListener {
            //추가 프라그먼트로 이동

        }
        connect_create_btn.setOnClickListener {
            *//*생성 버튼을 누른 경우*//*
            if(code==""){
                createCouple()
            }
            *//*이미 누르고 다시 들어오는 경우*//*

            //생성 프라그먼트로 이동

        }*/


    }
    fun init() {
        user_sid = intent.getStringExtra("sid")
        db.collection("info").document(user_sid)
            .get()
            .addOnSuccessListener { document ->
                if (document != null) {
                    code = document.getString("code")
                }
            }
            .addOnFailureListener { exception ->

            }
        //collection 생성 후
        if(code!=""){
            //생성 버튼 누른 프라그먼트로 이동
        }

    }
    fun createCouple(){
        val doc = db.collection("couples").document()
        //사용자의 code에 collection id 추가
        db.collection("info").document(user_sid)
            .update("code", doc.id)
        code = doc.id
        // collection 추가
        val data = hashMapOf("id" to user_sid)
        doc.collection("user_info").document("id1").set(data)
    }

    class ConnectPagerAdapter(
        fragmentManager: FragmentManager,
        private val tabCount:Int
    ): FragmentStatePagerAdapter(fragmentManager){
        override fun getItem(position: Int): Fragment {
            return when(position){
                0->{ InviteFragment() }
                1->{ CreateFragment() }
                else->{ InviteFragment() }
            }
        }

        override fun getCount(): Int {
            return tabCount
        }
    }
}