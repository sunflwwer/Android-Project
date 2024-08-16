package com.example.myapplication

import android.app.Activity
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.util.Log
import android.view.Menu
import android.view.MenuItem
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.viewpager2.adapter.FragmentStateAdapter
import com.example.myapplication.databinding.ActivityFragmentBinding
import com.google.android.material.tabs.TabLayoutMediator

class FragmentActivity : AppCompatActivity() {

    lateinit var binding: ActivityFragmentBinding

    class MyFragmentPagerAdapter(activity: FragmentActivity) : FragmentStateAdapter(activity) {
        private val fragments = listOf(OneFragment(), TwoFragment(), ThreeFragment())

        override fun getItemCount(): Int {
            return fragments.size
        }

        override fun createFragment(position: Int): Fragment {
            return fragments[position]
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityFragmentBinding.inflate(layoutInflater)
        setContentView(binding.root)

        supportActionBar?.setDisplayHomeAsUpEnabled(true)


        binding.viewpager.adapter = MyFragmentPagerAdapter(this)

        // 탭 이름을 설정합니다.
        val tabTitles = arrayOf("알림 설정", "유튜브 영상", "보호 식물 현황")

        // TabLayout과 ViewPager2를 연결합니다.
        TabLayoutMediator(binding.tabLayout, binding.viewpager) { tab, position ->
            tab.text = tabTitles[position]
        }.attach()
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

    override fun onSupportNavigateUp(): Boolean {
        val intent = intent
        setResult(Activity.RESULT_OK, intent)
        finish()
        return true
    }
}
