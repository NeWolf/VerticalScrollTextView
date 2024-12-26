package com.newolf.widget.demo

import android.os.Bundle
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.newolf.widget.VerticalScrollTextView

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_main)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }


        initView()
    }

    private fun initView() {
        val vstv = findViewById<VerticalScrollTextView>(R.id.vstv)

        vstv.setDataList(
            arrayListOf(
                "NeWolf",
                "VerticalScrollTextView",
                "Demo",
                "2024",
                "12-24",
                "今天是2024年的平安夜，明天就是圣诞节了，有礼物吗？"
            )
        )

        vstv.setOnClickListener {
            Toast.makeText(this, vstv.getCurrentShow(), Toast.LENGTH_SHORT).show()
        }

    }


}