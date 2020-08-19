package com.daerong.couplemap

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import android.view.View
import android.widget.TextView
import android.widget.Toast
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import kotlinx.android.synthetic.main.activity_sign_up.*

class SignUpActivity : AppCompatActivity() {
    private var idExisted: Boolean = false
    private var pwExisted: Boolean = false
    private var db = Firebase.firestore
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_sign_up)
        //아이디창 포커스->입력->포커스 해제시 error창 띄우기
        sign_up_input_id.setOnFocusChangeListener { v, hasFocus ->
            if (!hasFocus) {
                Log.d("focus","id입력창말고다른곳선택")
                if(sign_up_input_id.text.isEmpty()){ setErrorMessage(sign_up_id_error,"필수 입력 정보입니다..") }
                else{
                    Log.d("focus","id입력창말고다른곳선택->id입력")
                    db.collection("info")
                        .get()
                        .addOnSuccessListener {result->//연결성공
                            idExisted=false
                            for (document in result){
                                if(sign_up_input_id.text.toString()==document.getString("id").toString()){
                                    idExisted=true
                                    break
                                }
                            }
                            //아이디 없는 경우
                            if(!idExisted){ setErrorMessage(sign_up_id_error,"멋진 아이디 입니다..") }
                            //아이디 있는 경우
                            else{ setErrorMessage(sign_up_id_error,"중복된 아이디 입니다.") }
                        }
                        .addOnFailureListener {  }

                }
            }
            else{
                Log.d("focus","id입력창 포커스된 상태")
                sign_up_input_id.addTextChangedListener(object : TextWatcher {
                    override fun afterTextChanged(s: Editable?) {}
                    override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}
                    override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                        Log.d("focus","id 변화감지")
                        //비어있는지 검사
                        //아이디 비어있으면.
                        if (count==0) { setErrorMessage(sign_up_id_error,"필수 입력 정보입니다.") }
                        else{ setErrorMessage(sign_up_id_error,"아이디 입력중") }
                    }
                })
            }

        }
        sign_up_success_check.setOnClickListener {
            if (sign_up_input_id.text.isEmpty()) { setErrorMessage(sign_up_id_error,"필수 입력 정보입니다.") }
            if (sign_up_input_pw.text.isEmpty()) { setErrorMessage(sign_up_pw_error,"필수 입력 정보입니다.") }
            else { Toast.makeText(this@SignUpActivity, "안빔", Toast.LENGTH_SHORT).show() }
        }
    }
    private fun setErrorMessage(itemId:TextView, message:String){
        itemId.let {
            it.text=message
            it.visibility=View.VISIBLE
        }
    }
}