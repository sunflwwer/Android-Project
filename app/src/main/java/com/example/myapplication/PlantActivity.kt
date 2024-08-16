package com.example.myapplication

import android.app.Activity
import android.content.Intent
import android.content.SharedPreferences
import android.graphics.Color
import android.net.Uri
import android.os.Bundle
import android.util.Log
import android.view.Menu
import android.view.MenuItem
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.preference.PreferenceManager
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.myapplication.databinding.ActivityPlantBinding
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class PlantActivity : AppCompatActivity() {

    lateinit var binding: ActivityPlantBinding
    lateinit var sharedPreference: SharedPreferences

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityPlantBinding.inflate(layoutInflater)
        setContentView(binding.root)

        supportActionBar?.setDisplayHomeAsUpEnabled(true)

        sharedPreference = PreferenceManager.getDefaultSharedPreferences(this)
        val color = sharedPreference.getString("color", "#CDDC39")
        binding.btnSearch.setBackgroundColor(Color.parseColor(color))

        binding.btnSearch.setOnClickListener {
            val name = binding.edtName.text.toString()
            Log.d("mobileapp", name)

            // 첫 번째 API에서 데이터 가져오기
            val call: Call<XmlResponse2> = RetrofitConnection2.xmlNetworkService2.getXmlList(
                name,
                "DOC5gbTFMW4MJMNNLIeRJFhmq1nECkXilEccND7rm7J2cWnynmM9m3+woMjaLxQ5Mq30uzQI+dvZogaW5RqtMw==",
                1,
                10,
                "xml"
            )

            call.enqueue(object : Callback<XmlResponse2> {
                override fun onResponse(
                    call: Call<XmlResponse2>,
                    response: Response<XmlResponse2>
                ) {
                    if (response.isSuccessful) {
                        Log.d("mobileApp", "$response")
                        Log.d("mobileApp", "${response.body()}")

                        val items = response.body()?.body?.items?.item ?: mutableListOf()
                        binding.xmlRecyclerView.layoutManager =
                            LinearLayoutManager(this@PlantActivity)
                        binding.xmlRecyclerView.adapter = XmlAdapter2(items)
                        binding.xmlRecyclerView.addItemDecoration(
                            DividerItemDecoration(
                                this@PlantActivity,
                                LinearLayoutManager.VERTICAL
                            )
                        )
                    } else {
                        Log.e(
                            "mobileApp",
                            "Response not successful: ${response.errorBody()?.string()}"
                        )
                    }
                }

                override fun onFailure(call: Call<XmlResponse2>, t: Throwable) {
                    Log.e("mobileApp", "API call failed: ${t.message}")
                }
            })
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

        // 설정 반영
        sharedPreference = PreferenceManager.getDefaultSharedPreferences(this)
        val color = sharedPreference.getString("color", "#CDDC39")
        binding.btnSearch.setBackgroundColor(Color.parseColor(color))
    }


    override fun onSupportNavigateUp(): Boolean {
        val intent = intent
        setResult(Activity.RESULT_OK, intent)
        finish()
        return true
    }
}
