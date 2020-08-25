package com.daerong.couplemap

import android.annotation.SuppressLint
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import android.view.View
import android.widget.EditText
import android.widget.TextView
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import kotlinx.android.synthetic.main.activity_sign_up.*

class SignUpActivity : AppCompatActivity() {
    private var db = Firebase.firestore
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_sign_up)

        val idChecker=MadeBooleanObject(false)
        val pwChecker=MadeBooleanObject(false)
        setEditTextAction(sign_up_input_id,sign_up_id_error,"id", idChecker)
        setEditTextAction(sign_up_input_pw,sign_up_pw_error,"pw", pwChecker)
        //확인버튼 클릭
        sign_up_success_check.setOnClickListener {
            if(idChecker.checker==true&&pwChecker.checker==true){
                //아이디 중복, 비번 통과
                setFailErrorMessage(sign_up_id_error,"아이디 중복.")
                setSuccessErrorMessage(sign_up_pw_error,"멋진 패스워드 입니다.")
                return@setOnClickListener
            }else{
                if(idChecker.checker==true){
                    if (pwChecker.checker==false){
                        //아이디 중복, 비번 불합격
                        setFailErrorMessage(sign_up_id_error,"아이디 중복.")
                        setFailErrorMessage(sign_up_pw_error,"공백 포함x, 숫자, 영문자 포함, 4자리 이상")
                        return@setOnClickListener
                    }
                }else{
                    if (pwChecker.checker==true){
                        //아이디 통과, 비번 통과->db저장
                        val idpwData= hashMapOf(
                            "code" to "",
                            "connect" to false,
                            "id" to sign_up_input_id.text.toString(),
                            "pw" to sign_up_input_pw.text.toString()
                        )
                        setSuccessErrorMessage(sign_up_id_error,"멋진 아이디 입니다.")
                        setSuccessErrorMessage(sign_up_pw_error,"멋진 패스워드 입니다.")
                        db.collection("info")
                            .document()
                            .set(idpwData)
                            .addOnSuccessListener { Log.d("tlqkf","추가성공") }
                            .addOnFailureListener { Log.d("tlqkf","추가실패") }

                        return@setOnClickListener
                    }
                }
                //아이디 통과, 비번 불합격
                if(sign_up_input_id.text.isEmpty()){
                    setFailErrorMessage(sign_up_id_error, "필수 입력 정보입니다.")
                    sign_up_input_id.setBackgroundResource(R.drawable.error_edit_text)
                    if(sign_up_input_pw.text.isEmpty()) {
                        setFailErrorMessage(sign_up_pw_error, "필수 입력 정보입니다.")
                        sign_up_input_pw.setBackgroundResource(R.drawable.error_edit_text)
                    }
                }else{
                    setSuccessErrorMessage(sign_up_id_error,"멋진 아이디 입니다.")
                    sign_up_input_id.setBackgroundResource(R.drawable.basic_edit_text)
                }
            }
        }
    }
    class MadeBooleanObject(var checker:Boolean)
    private fun setEditTextAction(
        editTextId:EditText,
        errorTextViewId:TextView,
        IDorPW:String,
        duplicateChecker: MadeBooleanObject
    ):Boolean{
        //아이디창 포커스->입력->포커스 해제시 error창 띄우기
        editTextId.setOnFocusChangeListener { v, hasFocus ->
            if (!hasFocus) {
                if(editTextId.text.isEmpty()){ setFailErrorMessage(errorTextViewId,"!hasfocus필수 입력 정보입니다.") }
                else{
                    if(IDorPW=="id")
                        duplicationIDChecking( editTextId, errorTextViewId, duplicateChecker)
                    else
                        passwordChecking(editTextId,duplicateChecker)
                }
            }
            else{
                editTextId.setBackgroundResource(R.drawable.basic_edit_text)
                setSuccessErrorMessage(errorTextViewId,"$IDorPW 입력중")
                editTextId.addTextChangedListener(object : TextWatcher {
                    override fun afterTextChanged(s: Editable?) {
                        Log.d("focus","$IDorPW after : "+s.toString())
                    }
                    override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
                        Log.d("focus","$IDorPW before : "+s.toString()+",start"+start.toString()+",count"+count.toString()+",after"+after.toString())
                    }
                    override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                        Log.d("focus","$IDorPW 변화감지")
                        //비어있는지 검사
                        //아이디 비어있으면.
                        Log.d("focus","on : "+s.toString()+",start"+start.toString()+",before"+before.toString()+",count"+count.toString())
                        if (s?.length==0) {
                            editTextId.setBackgroundResource(R.drawable.error_edit_text)
                            setFailErrorMessage(errorTextViewId,"필수 입력 정보입니다.")
                        }
                        else{
                            editTextId.setBackgroundResource(R.drawable.basic_edit_text)
                            setSuccessErrorMessage(errorTextViewId,"$IDorPW 입력중") }
                    }
                })
            }
        }
        return duplicateChecker.checker
    }
    private fun passwordChecking(editTextId:EditText, duplicateChecker: MadeBooleanObject)
    {//정규식 통과시 -> true 공백 포함x,숫자, 영문자 포함,4자리 이상
        val regExp=Regex("^(?=.*[0-9])(?=.*[a-z])(?=\\S+\$).{4,}\$")
        duplicateChecker.checker=editTextId.text.matches(regExp)
        if(duplicateChecker.checker){
            setSuccessErrorMessage(sign_up_pw_error,"멋진 비밀번호 입니다.")
            editTextId.setBackgroundResource(R.drawable.basic_edit_text)
        }
        else{
            setFailErrorMessage(sign_up_pw_error,"공백 포함x, 숫자, 영문자 포함, 4자리 이상")
            editTextId.setBackgroundResource(R.drawable.error_edit_text)
        }
    }
    private fun duplicationIDChecking(
        editTextId:EditText,
        errorTextViewId:TextView,
        duplicateChecker: MadeBooleanObject){
        db.collection("info")
            .get()
            .addOnSuccessListener {result->//연결성공
                duplicateChecker.checker=false
                for (document in result){
                    if(editTextId.text.toString().toLowerCase()==document.getString("id").toString()){
                        editTextId.setBackgroundResource(R.drawable.error_edit_text)
                        duplicateChecker.checker=true
                        Log.d("tlqkf", "duplicateChecker : "+"id" +"검사 :" +duplicateChecker.checker.toString())
                        break
                    }
                }
                Log.d("tlqkf", "duplicateChecker : "+"id" +"검사:"+duplicateChecker.checker.toString())
                //아이디 없는 경우
                if(!duplicateChecker.checker){
                    editTextId.setBackgroundResource(R.drawable.basic_edit_text)
                    setSuccessErrorMessage(errorTextViewId,"멋진 아이디 입니다.") }
                //아이디 있는 경우
                else{
                    editTextId.setBackgroundResource(R.drawable.error_edit_text)
                    setFailErrorMessage(errorTextViewId,"중복된 아이디 입니다.") }
            }
            .addOnFailureListener {  }
    }
    @SuppressLint("ResourceAsColor")
    private fun setSuccessErrorMessage(itemId:TextView, message:String){
        itemId.let {
            it.text=message
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                it.setTextColor(getColor(R.color.success))
            }else{
                it.setTextColor(resources.getColor(R.color.success))
            }
            it.visibility=View.VISIBLE
        }
    }
    @SuppressLint("ResourceAsColor")
    private fun setFailErrorMessage(itemId:TextView, message:String){
        itemId.let {
            it.text=message
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                it.setTextColor(getColor(R.color.error_edit_text_border))
            }else{
                it.setTextColor(resources.getColor(R.color.error_edit_text_border))
            }
            it.visibility=View.VISIBLE
        }
    }
}