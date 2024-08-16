package com.example.myapplication

import android.app.NotificationChannel
import android.app.NotificationManager
import android.content.Context
import android.content.pm.PackageManager
import android.graphics.BitmapFactory
import android.graphics.drawable.AnimationDrawable
import android.os.Build
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.animation.AnimationUtils
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.core.app.NotificationCompat
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.preference.PreferenceManager
import com.example.myapplication.databinding.FragmentOneBinding

private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"

class OneFragment : Fragment() {
    private var param1: String? = null
    private var param2: String? = null
    private lateinit var binding: FragmentOneBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            param1 = it.getString(ARG_PARAM1)
            param2 = it.getString(ARG_PARAM2)
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentOneBinding.inflate(inflater, container, false)

        // 알림
        val permissionLauncher = registerForActivityResult(
            ActivityResultContracts.RequestMultiplePermissions()
        ) {
            if (it.all { permission -> permission.value == true }) {
                toggleNotificationSetting()
            } else {
                Toast.makeText(context, "알림이 거부되었습니다.", Toast.LENGTH_SHORT).show()
            }
        }

        binding.fragButton.setOnClickListener {
            val anim = AnimationUtils.loadAnimation(context, R.anim.alpha)
            binding.fragButton.startAnimation(anim)
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
                if (context?.let { it1 ->
                        ContextCompat.checkSelfPermission(
                            it1,
                            "android.permission.POST_NOTIFICATIONS"
                        )
                    } == PackageManager.PERMISSION_GRANTED) {
                    toggleNotificationSetting()
                } else {
                    permissionLauncher.launch(arrayOf("android.permission.POST_NOTIFICATIONS"))
                }
            } else {
                toggleNotificationSetting()
            }
        }

        // 애니메이션
        val plantAnimation: AnimationDrawable
        val plantImage = binding.plantImage.apply {
            setBackgroundResource(R.drawable.plant)
            plantAnimation = background as AnimationDrawable
        }
        plantAnimation.start()

        // 초기화: 현재 알림 설정 상태에 따라 버튼 텍스트 설정
        updateButtonText()

        return binding.root
    }

    override fun onResume() {
        super.onResume()
        updateButtonText()
    }

    private fun toggleNotificationSetting() {
        val sharedPreferences = context?.let { PreferenceManager.getDefaultSharedPreferences(it) }
        val isNotificationsEnabled = sharedPreferences?.getBoolean("notifications", false)

        if (isNotificationsEnabled == true) {
            // 알림이 이미 활성화된 경우 알림 비활성화
            saveNotificationPreference(false)
            binding.fragButton.text = "알림 받기"
        } else {
            // 알림이 비활성화된 경우 알림 활성화
            noti()
            saveNotificationPreference(true)
            binding.fragButton.text = "알림 받기 완료"
        }
    }

    private fun updateButtonText() {
        val sharedPreferences = context?.let { PreferenceManager.getDefaultSharedPreferences(it) }
        val isNotificationsEnabled = sharedPreferences?.getBoolean("notifications", false)
        binding.fragButton.text = if (isNotificationsEnabled == true) "알림 받기 완료" else "알림 받기"
    }

    fun noti() {
        val manager = context?.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager

        val builder: NotificationCompat.Builder
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val channelId = "one-channel"
            val channelName = "My Channel One"
            val channel = NotificationChannel(
                channelId,
                channelName,
                NotificationManager.IMPORTANCE_DEFAULT
            ).apply {
                description = "My Channel One Description"
                setShowBadge(true)
                enableVibration(true)
            }
            manager.createNotificationChannel(channel)
            builder = context?.let { NotificationCompat.Builder(it, channelId) }!!
        } else {
            builder = context?.let { NotificationCompat.Builder(it) }!!
        }

        builder.run {
            setSmallIcon(R.drawable.plant0)
            setWhen(System.currentTimeMillis())
            setContentTitle("알림 받기 완료 ☘\uFE0F")
            setContentText("플래닛의 알림을 받아주셔서 감사합니다.")
            setLargeIcon(BitmapFactory.decodeResource(resources, R.drawable.plant4))
        }

        manager.notify(11, builder.build())
    }

    private fun saveNotificationPreference(enabled: Boolean) {
        val sharedPreferences = context?.let { PreferenceManager.getDefaultSharedPreferences(it) }
        val editor = sharedPreferences?.edit()
        if (editor != null) {
            editor.putBoolean("notifications", enabled)
            editor.apply()
        }
    }

    companion object {
        fun newInstance(param1: String, param2: String) =
            OneFragment().apply {
                arguments = Bundle().apply {
                    putString(ARG_PARAM1, param1)
                    putString(ARG_PARAM2, param2)
                }
            }
    }
}
