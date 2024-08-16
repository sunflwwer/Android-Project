package com.example.myapplication

import android.app.Activity
import android.content.Intent
import android.content.SharedPreferences
import android.graphics.Color
import android.net.Uri
import android.os.Bundle
import android.util.Log
import android.util.TypedValue
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.widget.Button
import android.widget.TextView
import androidx.appcompat.app.ActionBarDrawerToggle
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.preference.PreferenceManager
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.myapplication.databinding.ActivityMainBinding
import com.google.android.material.navigation.NavigationView
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class MainActivity : AppCompatActivity(), NavigationView.OnNavigationItemSelectedListener {

    lateinit var toggle: ActionBarDrawerToggle
    lateinit var binding: ActivityMainBinding
    lateinit var headerView: View
    lateinit var sharedPreference: SharedPreferences

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        supportActionBar?.setDisplayHomeAsUpEnabled(true)

        sharedPreference = PreferenceManager.getDefaultSharedPreferences(this)
        val color = sharedPreference.getString("color", "#CDDC39")
        binding.btnSearch.setBackgroundColor(Color.parseColor(color))


        // DrawerLayout Toggle
        toggle = ActionBarDrawerToggle(
            this,
            binding.drawer,
            R.string.drawer_opened,
            R.string.drawer_closed
        )
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        toggle.syncState()

        // Drawer 메뉴
        binding.mainDrawerView.setNavigationItemSelectedListener(this)

        headerView = binding.mainDrawerView.getHeaderView(0)
        val button = headerView.findViewById<Button>(R.id.btnAuth)
        button.setOnClickListener {
            Log.d("mobileapp", "button.setOnClickListener")

            val intent = Intent(this, AuthActivity::class.java)
            if (button.text.equals("로그인")) {
                intent.putExtra("status", "logout")
            } else if (button.text.equals("로그아웃")) {
                intent.putExtra("status", "login")
            }
            startActivity(intent)
            binding.drawer.closeDrawers()
        }


        binding.btnSearch.setOnClickListener {
            val name = binding.edtName.text.toString()
            Log.d("mobileapp", name)

            val call: Call<XmlResponse> = RetrofitConnection.xmlNetworkService.getXmlList(
                name,
                1,
                10,
                "xml",
                "DOC5gbTFMW4MJMNNLIeRJFhmq1nECkXilEccND7rm7J2cWnynmM9m3+woMjaLxQ5Mq30uzQI+dvZogaW5RqtMw==" // 일반인증키(Decoding)
            )

            call.enqueue(object : Callback<XmlResponse> {
                override fun onResponse(call: Call<XmlResponse>, response: Response<XmlResponse>) {
                    if (response.isSuccessful) {
                        Log.d("mobileApp", "$response")
                        Log.d("mobileApp", "${response.body()}")
                        binding.xmlRecyclerView.layoutManager =
                            LinearLayoutManager(applicationContext)
                        binding.xmlRecyclerView.adapter =
                            XmlAdapter(response.body()!!.body.items.item)
                        binding.xmlRecyclerView.addItemDecoration(
                            DividerItemDecoration(
                                applicationContext,
                                LinearLayoutManager.VERTICAL
                            )
                        )
                    }
                }

                override fun onFailure(call: Call<XmlResponse>, t: Throwable) {
                    Log.d("mobileApp", "onFailure ${call.request()}")
                }
            })
        }
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.menu_overflow, menu)

        return super.onCreateOptionsMenu(menu)
    }

    // Option menu item selected
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

        if (toggle.onOptionsItemSelected(item)) {
            return true
        }

        return super.onOptionsItemSelected(item)
    }

    // Navigation drawer item selected
    override fun onNavigationItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            R.id.item_fragment -> {
                Log.d("mobileapp", "프래그먼트 메뉴")
                val intent = Intent(this, FragmentActivity::class.java)
                startActivity(intent)
                binding.drawer.closeDrawers()
                return true
            }

            R.id.item_myplant -> {
                Log.d("mobileapp", "나의식물도감 메뉴")
                val intent = Intent(this, MyPlantActivity::class.java)
                startActivity(intent)
                binding.drawer.closeDrawers()
                return true
            }

            R.id.item_board -> {
                Log.d("mobileapp", "게시판 메뉴")
                val intent = Intent(this, BoardActivity::class.java)
                startActivity(intent)
                binding.drawer.closeDrawers()
                return true
            }

            R.id.item_myplace -> {
                Log.d("mobileapp", "전시회 메뉴")
                val intent = Intent(this, PlantActivity::class.java)
                startActivity(intent)
                binding.drawer.closeDrawers()
                return true
            }

            R.id.item_plantdict -> {
                Log.d("mobileapp", "희귀 식물 도감 메뉴")
                val intent = Intent(this, DictActivity::class.java)
                startActivity(intent)
                binding.drawer.closeDrawers()
                return true
            }

        }
        return false
    }

    override fun onStart() {
        super.onStart()
        val button = headerView.findViewById<Button>(R.id.btnAuth)
        val tv = headerView.findViewById<TextView>(R.id.tvID)

        if (MyApplication.checkAuth() || MyApplication.email != null) {
            button.text = "로그아웃"
            tv.text = "${MyApplication.email}님 \n\uD83C\uDF3A 환영합니다 \uD83C\uDF3A"
        } else {
            button.text = "로그인"
            tv.text = "\uD83D\uDD90\uFE0F 반갑습니다 \uD83D\uDD90"
        }
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
