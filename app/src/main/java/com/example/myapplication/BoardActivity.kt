package com.example.myapplication

import android.app.Activity
import android.content.Intent
import android.content.SharedPreferences
import android.os.Bundle
import android.util.Log
import android.view.Menu
import android.view.MenuItem
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.preference.PreferenceManager
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.myapplication.databinding.ActivityBoardBinding

class BoardActivity : AppCompatActivity() {

    lateinit var binding: ActivityBoardBinding
    lateinit var sharedPreference: SharedPreferences
    var textSize: Float = 14.0f // 기본값 14로 설정

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityBoardBinding.inflate(layoutInflater)
        setContentView(binding.root)

        supportActionBar?.setDisplayHomeAsUpEnabled(true)

        sharedPreference = PreferenceManager.getDefaultSharedPreferences(this)
        textSize = sharedPreference.getString("size", "14.0f")!!.toFloat()

        binding.mainFab.setOnClickListener {
            if (MyApplication.checkAuth() || MyApplication.email != null) {
                startActivity(Intent(this, AddActivity::class.java))
            } else {
                Toast.makeText(this, "로그인 후 이용 가능합니다.", Toast.LENGTH_LONG).show()
            }
        }
    }

    override fun onStart() {
        super.onStart()

        if (MyApplication.checkAuth() || MyApplication.email != null) {
            MyApplication.db.collection("comments")
                .orderBy("date_time")
                .get()
                .addOnSuccessListener { result ->
                    val itemList = mutableListOf<ItemData>()
                    for (document in result) {
                        val item = document.toObject(ItemData::class.java)
                        item.docId = document.id
                        itemList.add(item)
                    }
                    binding.recyclerView.layoutManager = LinearLayoutManager(this)
                    binding.recyclerView.adapter = BoardAdapter(this, itemList, textSize)
                }
                .addOnFailureListener {
                    Toast.makeText(this, "서버 데이터 획득을 실패하였습니다. (네이버 회원은 Firebase에 연결되지 않아 이용불가)", Toast.LENGTH_LONG).show()
                }
        }
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.menu_overflow, menu)
        return super.onCreateOptionsMenu(menu)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            R.id.menu_main_setting -> {
                Log.d("mobileapp", "설정 메뉴")
                val intent = Intent(this, SettingActivity::class.java)
                startActivity(intent)
                return true
            }
        }
        return super.onOptionsItemSelected(item)
    }

    override fun onResume() {
        super.onResume()

        sharedPreference = PreferenceManager.getDefaultSharedPreferences(this)
        textSize = sharedPreference.getString("size", "14.0f")!!.toFloat()

        binding.recyclerView.adapter?.notifyDataSetChanged()
    }

    override fun onSupportNavigateUp(): Boolean {
        val intent = intent
        setResult(Activity.RESULT_OK, intent)
        finish()
        return true
    }
}
