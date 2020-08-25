package com.daerong.couplemap

import android.content.Context
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import android.view.MotionEvent
import android.view.View
import android.widget.Toast
import com.daerong.couplemap.connect.ConnectActivity
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import kotlinx.android.synthetic.main.activity_login.*

class LoginActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)
        //로그인 정보 입력
        login_delete_id.setOnClickListener { login_input_id.setText("") }
        login_delete_pw.setOnClickListener { login_input_pw.setText("") }
        login_input_id.setOnFocusChangeListener { v, hasFocus ->
            login_input_id.addTextChangedListener(object :TextWatcher{
                override fun afterTextChanged(s: Editable?) {
                }

                override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
                }

                override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                    if(hasFocus){
                        if(count>0)
                            login_delete_id.setVisibility(View.VISIBLE)
                        else
                            login_delete_id.setVisibility(View.INVISIBLE)
                    }
                    else{
                        login_delete_id.setVisibility(View.INVISIBLE)
                    }
                }
            })
        }
        login_input_pw.setOnFocusChangeListener { v, hasFocus ->
            login_input_pw.addTextChangedListener(object :TextWatcher{
                override fun afterTextChanged(s: Editable?) {
                }

                override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
                }

                override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                    if(hasFocus){
                        if(count>0)
                            login_delete_pw.setVisibility(View.VISIBLE)
                        else
                            login_delete_pw.setVisibility(View.INVISIBLE)
                    }
                    else{
                        login_delete_pw.setVisibility(View.INVISIBLE)
                    }
                }
            })
        }
        //로그인
        login_btn.setOnClickListener {
            login_error.visibility = View.GONE
            login_id_error.visibility = View.GONE
            login_pw_error.visibility = View.GONE

            if(login_input_id.text.isEmpty()){
                login_id_error.visibility = View.VISIBLE
            }
            else{
                if(login_input_pw.text.isEmpty()){
                    login_pw_error.visibility = View.VISIBLE
                }
                else{
                    login(login_input_id.text.toString(), login_input_pw.text.toString())
                }
            }
        }
        //회원가입
        login_sign_up.setOnClickListener {
            val intent= Intent(this,SignUpActivity::class.java)
            startActivity(intent)
        }
    }
    fun login(id:String, pw:String){
        var loginStatus:Boolean = false
        val db = Firebase.firestore
        db.collection("info")
            .get()
            .addOnSuccessListener { result ->
                Log.d("hihihi",result.documents[0].get("id").toString())
                for (document in result) {
                    Log.d("hihihi","for문 : "+document.getString("id")+", "+document.getString("pw"))
                    //로그인 성공
                    if(document.getString("id")==id&&document.getString("pw")==pw){
                        //로그인된 sid전달
                        loginStatus = true
                        //연결된 경우
                        if(document.getBoolean("connect")!!){

                            val intent = Intent(this, MainActivity::class.java)
                            intent.putExtra("sid", document.id)
                            startActivity(intent)
                        }
                        //연결 안된 경우
                        else{
                            //
                            val intent = Intent(this, ConnectActivity::class.java)
                            intent.putExtra("sid", document.id)
                            startActivity(intent)
                        }

                    }
                }
                //로그인 실패
                if(!loginStatus){
                    Log.d("hihihi","실패")
                    login_error.visibility = View.VISIBLE
                }

            }
            .addOnFailureListener { exception ->
                Log.d("hihihi","실패"+exception.toString())
            }
    }
}