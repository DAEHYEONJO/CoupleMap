package com.daerong.couplemap.connect

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.daerong.couplemap.R
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import kotlinx.android.synthetic.main.activity_connect.*

class ConnectActivity : AppCompatActivity() {
    lateinit var user_sid:String
    val db = Firebase.firestore

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_connect)
        init()

        connect_add_btn.setOnClickListener {
            /*있는id값: 사용자의 code에 collection id 추가, connect:true
            상대방의 connect:true -> 감지 필요
            collection > couple_info > id2 > 아이디 추가
            MainActivity(로그인id, collection)
            없는id값: 재입력*/

        }
        connect_create_btn.setOnClickListener {
            /*생성 버튼을 누른 경우:
            collection생성
            collection > couple_info > id1 > 아이디 추가
                    collection에 favorites, locations추가
            사용자의 code에 collection id 추가
            */
            val doc = db.collection("couples")
                .add(){
                }
            db.collection("couples")
                .document(doc.toString())
                .collection("user_info").add("id1")
            

            db.collection("couples")
                .document(doc.toString())
                .collection("loctions")
            db.collection("couples")
                .document(doc.toString())
                .collection("favorites")


        }


    }
    fun init() {
        var code:String? = null
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
        //collection 생성 전
        if(code==""){

        }
        //collection 생성 후
        else{
            //생성 버튼 누른 프라그먼트로 이동
            /*이미 누르고 기다리는 상태:
            사용자 code부분을 전송code로 띄워줌
            connect가 true로 바뀌면 MainActivity로 이동*/
        }
    }
}