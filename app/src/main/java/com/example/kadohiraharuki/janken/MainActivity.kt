package com.example.kadohiraharuki.janken

import android.content.Intent
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.preference.PreferenceManager
import android.view.View
import kotlinx.android.synthetic.main.activity_main.*
import org.jetbrains.anko.startActivity

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        gu.setOnClickListener{onJankenButtonTapped(it)}
        choki.setOnClickListener{onJankenButtonTapped(it)}
        pa.setOnClickListener{onJankenButtonTapped(it)}

        //共有プリファレンスをクリア:clearメソッド
        val pref = PreferenceManager.getDefaultSharedPreferences(this)
        val editor = pref.edit()
        editor.clear().apply()

    }
        /*fun onJankenButtonTapped(view: View?){
            val intent = Intent(this, ResultActivity::class.java)
            intent.putExtra("MY_HAND", view?.id)
            startActivity(intent)
            */
    fun onJankenButtonTapped(view: View?){
            startActivity<ResultActivity>("MY_HAND" to view?.id)
        }

    }

