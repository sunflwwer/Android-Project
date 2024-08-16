package com.example.myapplication

import android.app.Activity
import android.content.Intent
import android.content.SharedPreferences
import android.net.Uri
import android.os.Bundle
import android.util.Log
import android.view.Menu
import android.view.MenuItem
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.preference.PreferenceManager
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.myapplication.databinding.ActivityMyPlantBinding
import java.io.BufferedReader
import java.io.File
import java.text.SimpleDateFormat

class MyPlantActivity : AppCompatActivity() {

    lateinit var binding: ActivityMyPlantBinding
    var datas: MutableList<String>? = null
    lateinit var adapter: MyAdapter

    lateinit var sharedPreference: SharedPreferences


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityMyPlantBinding.inflate(layoutInflater)
        setContentView(binding.root)

        supportActionBar?.setDisplayHomeAsUpEnabled(true)

        sharedPreference = PreferenceManager.getDefaultSharedPreferences(this)
        val idStr = sharedPreference.getString("id", "나의 식물 일지")
        binding.plantTitle.text = idStr

        datas = mutableListOf<String>()
        val db = DBHelper(this).readableDatabase
        val cursor = db.rawQuery("select * from plant_db", null)
        while (cursor.moveToNext()) {
            datas?.add(cursor.getString(1))
        }
        db.close()
        /*
        datas  = savedInstanceState?.let {
            it.getStringArrayList("datas")?.toMutableList()
        }
            ?: let {
                mutableListOf<String>()
            }
*/
        // 파일 읽기 (초기화시 이부분 지우고 다시실행)
        val file = File(filesDir, "plant.txt")
        val readstream: BufferedReader = file.reader().buffered()
        binding.lastsaved.text = "마지막 일지 : " + readstream.readLine()

        adapter = MyAdapter(datas)
        binding.recyclerView.adapter = adapter
        val layoutManager = LinearLayoutManager(this)
        binding.recyclerView.layoutManager = layoutManager
        binding.recyclerView.addItemDecoration(
            DividerItemDecoration(
                this,
                LinearLayoutManager.VERTICAL
            )
        )

        val requestLauncher: ActivityResultLauncher<Intent> = registerForActivityResult(
            ActivityResultContracts.StartActivityForResult()
        ) {
            it.data!!.getStringExtra("result")?.let {// "result"에 값이 저장되어 있으면(non-null)
                if (it != "") {
                    datas?.add(it)
                    adapter.notifyDataSetChanged()

                }
            }
        }

        binding.mainFab.setOnClickListener {
            val intent = Intent(this, AddPlantActivity::class.java)

            val dateFormat = SimpleDateFormat("yyyy-MM-dd") // 년 월 일
            intent.putExtra("today", dateFormat.format(System.currentTimeMillis()))

            requestLauncher.launch(intent)
        }
    }

    /*
        override fun onSaveInstanceState(outState: Bundle) {
            super.onSaveInstanceState(outState)
            outState.putStringArrayList("datas", ArrayList(datas))  // 지금까지의 datas 저장
        }
        */
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

            R.id.item_info -> {
                Log.d("mobileapp", "웹사이트 방문 메뉴")
                val intent = Intent(Intent.ACTION_VIEW, Uri.parse("https://www.koagi.or.kr/"))
                startActivity(intent)
                return true
            }

            R.id.item_map -> {
                Log.d("mobileapp", "지도보기 메뉴")
                val intent = Intent(
                    Intent.ACTION_VIEW,
                    Uri.parse("https://www.google.com/maps/dir/덕성여자대학교/국립백두대간수목원")
                )
                startActivity(intent)
                return true
            }

            R.id.item_gallery -> {
                Log.d("mobileapp", "갤러리보기 메뉴")
                val intent =
                    Intent(Intent.ACTION_VIEW, Uri.parse("content://media/internal/images/media"))
                startActivity(intent)
                return true
            }

            R.id.item_call -> {
                Log.d("mobileapp", "전화걸기 메뉴")
                val intent = Intent(Intent.ACTION_DIAL, Uri.parse("tel:044-270-5005"))
                startActivity(intent)
                return true
            }

            R.id.item_mail -> {
                Log.d("mobileapp", "메일보내기 메뉴")
                val intent = Intent(Intent.ACTION_SENDTO, Uri.parse("mailto:jito007@koagi.or.kr"))
                startActivity(intent)
                return true
            }

            R.id.item_exit -> {
                AlertDialog.Builder(this).run {
                    setTitle("알림")
                    setIcon(android.R.drawable.ic_dialog_alert)
                    setMessage("정말 종료하시겠습니까?")
                    setPositiveButton("예") { dialog, which ->
                        finishAffinity()
                        android.os.Process.killProcess(android.os.Process.myPid())
                    }
                    setNegativeButton("아니오", null)
                    show()
                }
                return true
            }
        }


        return super.onOptionsItemSelected(item)
    }


    override fun onResume() {
        super.onResume()

        sharedPreference = PreferenceManager.getDefaultSharedPreferences(this)
        val idStr = sharedPreference.getString("id", "나의 식물 일지")
        binding.plantTitle.text = idStr
    }

    override fun onSupportNavigateUp(): Boolean {
        val intent = intent
        setResult(Activity.RESULT_OK, intent)
        finish()
        return true
    }

}