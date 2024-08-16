package com.example.myapplication

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.Menu
import android.view.MenuItem
import androidx.appcompat.app.AppCompatActivity
import com.example.myapplication.databinding.ActivityDetailBinding

class DetailActivity : AppCompatActivity() {


    lateinit var binding: ActivityDetailBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityDetailBinding.inflate(layoutInflater)
        setContentView(binding.root)

        supportActionBar?.setDisplayHomeAsUpEnabled(true)



        binding.tvName.text = intent.getStringExtra("name")
        binding.tvFamilyN.text = intent.getStringExtra("FamilyN")
        binding.tvScienceN.text = intent.getStringExtra("ScienceN")
        binding.tvSort.text = intent.getStringExtra("sort")
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.menu_overflow, menu)

        // 설정 메뉴를 제외한 다른 메뉴 항목 제거
        menu?.let {
            for (i in 0 until it.size()) {
                val menuItem = it.getItem(i)
                if (menuItem.itemId != R.id.menu_main_setting) {
                    menuItem.isVisible = false
                }
            }
        }

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
