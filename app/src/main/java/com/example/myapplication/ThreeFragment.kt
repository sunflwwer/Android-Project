package com.example.myapplication

import android.graphics.Color
import android.graphics.Typeface
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.example.myapplication.databinding.FragmentThreeBinding
import com.github.mikephil.charting.data.RadarData
import com.github.mikephil.charting.data.RadarDataSet
import com.github.mikephil.charting.data.RadarEntry
import com.github.mikephil.charting.formatter.ValueFormatter

private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"

class ThreeFragment : Fragment() {
    private var param1: String? = null
    private var param2: String? = null

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
        val binding = FragmentThreeBinding.inflate(inflater, container, false)

        val radarValues: ArrayList<RadarEntry> = ArrayList()
        radarValues.add(RadarEntry(4F))
        radarValues.add(RadarEntry(144F))
        radarValues.add(RadarEntry(122F))
        radarValues.add(RadarEntry(119F))
        radarValues.add(RadarEntry(70F))

        val radarDataset = RadarDataSet(radarValues, "보호특산식물 지정현황")
        radarDataset.color = Color.parseColor("#388E3C") // 초록색
        radarDataset.lineWidth = 2f
        radarDataset.setDrawFilled(true)
        radarDataset.fillColor = Color.parseColor("#66BB6A") // 밝은 초록색

        val radarData = RadarData(radarDataset)
        radarData.setValueTextSize(14f)
        radarData.setValueTextColor(Color.parseColor("#388E3C")) // 초록색

        binding.radarChart.data = radarData

        // 각 데이터 포인트에 해당하는 라벨 설정
        val labels = arrayOf("야생멸종 (EW)", "멸종위기종 (ER)", "위기종 (EN)", "취약종 (VU)", "약관심종 (LC)")

        val xAxis = binding.radarChart.xAxis
        xAxis.valueFormatter = object : ValueFormatter() {
            override fun getFormattedValue(value: Float): String {
                return if (value.toInt() in labels.indices) labels[value.toInt()] else value.toString()
            }
        }

        xAxis.textSize = 16f
        xAxis.textColor = Color.parseColor("#555555") // 초록색
        xAxis.typeface = Typeface.create(Typeface.SANS_SERIF, Typeface.BOLD) // 글씨체 설정 및 Bold 적용


        val yAxis = binding.radarChart.yAxis
        yAxis.textSize = 14f
        yAxis.textColor = Color.parseColor("#388E3C") // 초록색
        yAxis.typeface = Typeface.SANS_SERIF // 글씨체 설정

        val legend = binding.radarChart.legend
        legend.textColor = Color.parseColor("#388E3C") // 초록색
        legend.textSize = 16f
        legend.typeface = Typeface.SANS_SERIF // 글씨체 설정

        binding.radarChart.description.isEnabled = false // 설명 비활성화
        binding.radarChart.invalidate() // 차트 새로 고침

        return binding.root
    }


    companion object {
        fun newInstance(param1: String, param2: String) =
            ThreeFragment().apply {
                arguments = Bundle().apply {
                    putString(ARG_PARAM1, param1)
                    putString(ARG_PARAM2, param2)
                }
            }
    }
}
