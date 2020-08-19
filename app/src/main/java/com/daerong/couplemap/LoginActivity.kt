package com.daerong.couplemap

import android.content.Context
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import android.view.MotionEvent
import android.view.View
import android.widget.Toast
import kotlinx.android.synthetic.main.activity_login.*

class LoginActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)
        delete_id.setOnClickListener { input_id.setText("") }
        delete_pw.setOnClickListener { input_pw.setText("") }
        input_id.setOnFocusChangeListener { v, hasFocus ->
            input_id.addTextChangedListener(object :TextWatcher{
                override fun afterTextChanged(s: Editable?) {
                }

                override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
                }

                override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                    if(hasFocus){
                        if(count>0)
                            delete_id.setVisibility(View.VISIBLE)
                        else
                            delete_id.setVisibility(View.INVISIBLE)
                    }
                    else{
                        delete_id.setVisibility(View.INVISIBLE)
                    }
                }
            })
        }
        input_pw.setOnFocusChangeListener { v, hasFocus ->
            input_pw.addTextChangedListener(object :TextWatcher{
                override fun afterTextChanged(s: Editable?) {
                }

                override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
                }

                override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                    if(hasFocus){
                        if(count>0)
                            delete_pw.setVisibility(View.VISIBLE)
                        else
                            delete_pw.setVisibility(View.INVISIBLE)
                    }
                    else{
                        delete_pw.setVisibility(View.INVISIBLE)
                    }
                }
            })
        }
    }
}