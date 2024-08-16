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
import com.example.myapplication.databinding.ActivityDictBinding
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class DictActivity : AppCompatActivity() {

    lateinit var binding: ActivityDictBinding
    lateinit var sharedPreference: SharedPreferences

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityDictBinding.inflate(layoutInflater)
        setContentView(binding.root)

        sharedPreference = PreferenceManager.getDefaultSharedPreferences(this)
        val color = sharedPreference.getString("color", "#CDDC39")
        binding.btnPhp.setBackgroundColor(Color.parseColor(color))

        supportActionBar?.setDisplayHomeAsUpEnabled(true)

        binding.btnPhp.setOnClickListener {
            val name = binding.etName.text.toString()
            val call: Call<PhpResponse> = RetrofitConnection2.phpNetworkService.getPhpList(name)

            call.enqueue(object : Callback<PhpResponse> {
                override fun onResponse(call: Call<PhpResponse>, response: Response<PhpResponse>) {
                    if (response.isSuccessful) {
                        Log.d("mobileApp", "$response")
                        Log.d("mobileApp", "${response.body()}")
                        binding.phpRecyclerView.adapter =
                            PhpAdapter(this@DictActivity, response.body()?.result!!)
                        binding.phpRecyclerView.layoutManager =
                            LinearLayoutManager(this@DictActivity)
                        binding.phpRecyclerView.addItemDecoration(
                            DividerItemDecoration(
                                this@DictActivity,
                                LinearLayoutManager.VERTICAL
                            )
                        )
                    }
                }

                override fun onFailure(call: Call<PhpResponse>, t: Throwable) {
                    Log.d("mobileApp", "onFailure")
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
        binding.btnPhp.setBackgroundColor(Color.parseColor(color))
    }


    override fun onSupportNavigateUp(): Boolean {
        val intent = intent
        setResult(Activity.RESULT_OK, intent)
        finish()
        return true
    }
}