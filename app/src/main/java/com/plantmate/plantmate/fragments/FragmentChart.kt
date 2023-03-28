package com.plantmate.plantmate.fragments

import android.annotation.SuppressLint
import android.os.Bundle
import android.view.LayoutInflater
import android.view.MotionEvent
import android.view.View
import android.view.ViewGroup
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import com.github.mikephil.charting.charts.LineChart
import com.github.mikephil.charting.components.XAxis
import com.github.mikephil.charting.data.Entry
import com.github.mikephil.charting.data.LineData
import com.github.mikephil.charting.data.LineDataSet
import com.github.mikephil.charting.highlight.Highlight
import com.github.mikephil.charting.listener.ChartTouchListener
import com.github.mikephil.charting.listener.OnChartGestureListener
import com.github.mikephil.charting.listener.OnChartValueSelectedListener
import com.plantmate.plantmate.DAO.DataHelper
import com.plantmate.plantmate.DAO.DataHelper.Companion.dataGenerate
import com.plantmate.plantmate.R
import com.plantmate.plantmate.databinding.FragmentChartBinding
import java.text.DecimalFormat
import kotlin.random.Random

class FragmentChart : Fragment(R.layout.fragment_chart)  {
    private lateinit var binding: FragmentChartBinding
    private lateinit var btnNetProfit : ConstraintLayout
    private lateinit var btnSale : ConstraintLayout
    private lateinit var btnInventory : ConstraintLayout
    private lateinit var btnPurchase : ConstraintLayout
    private lateinit var btnWithered : ConstraintLayout
    private lateinit var lineChart: LineChart

    @Suppress("DEPRECATION")
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentChartBinding.inflate(inflater)

        btnNetProfit = binding.fragmentChartBtnNetProfit
        btnSale = binding.fragmentChartBtnSale
        btnInventory = binding.fragmentChartBtnInventory
        btnPurchase = binding.fragmentChartBtnPurchase
        btnWithered = binding.fragmentChartBtnWithered

        btnNetProfit.setOnClickListener{
            removeBtn(btnNetProfit)
            binding.fragmentChartIvBackground.setBackgroundColor(resources.getColor(R.color.primary))
        }
        btnSale.setOnClickListener{
            removeBtn(btnSale)
            binding.fragmentChartIvBackground.setBackgroundColor(resources.getColor(R.color.yellow_primary))
        }
        btnInventory.setOnClickListener{
            removeBtn(btnInventory)
            binding.fragmentChartIvBackground.setBackgroundColor(resources.getColor(R.color.purple_primary))
        }
        btnPurchase.setOnClickListener{
            removeBtn(btnPurchase)
            binding.fragmentChartIvBackground.setBackgroundColor(resources.getColor(R.color.orange_primary))
        }
        btnWithered.setOnClickListener{
            removeBtn(btnWithered)
            binding.fragmentChartIvBackground.setBackgroundColor(resources.getColor(R.color.red_primary))
        }
        lineChart = binding.lineChart
        setupLineChart()
        // Add data to the line chart
        setData(dataGenerate(7))

        binding.fragmentChartBtn1w.setOnClickListener { setData(dataGenerate(7)) }
        binding.fragmentChartBtn1m.setOnClickListener { setData(dataGenerate(28)) }
        binding.fragmentChartBtn3m.setOnClickListener { setData(dataGenerate(84)) }
        binding.fragmentChartBtn1y.setOnClickListener { setData(dataGenerate(365)) }
        binding.fragmentChartBtnAll.setOnClickListener { setData(dataGenerate(365)) }

        return binding.root
    }
    private fun selectButton(button: ConstraintLayout){
        button.setBackgroundResource(R.drawable.round_rectangle)
        button.elevation = 9.0F
    }

    private fun removeBtn(button: ConstraintLayout) {
        listOf(btnNetProfit, btnSale, btnInventory, btnPurchase, btnWithered).forEach {
            it.setBackgroundResource(0)
            it.elevation = 0.0F
        }
        selectButton(button)
    }
    private fun setupLineChart() {
        // Set the chart's description
        lineChart.description.isEnabled = false

        // Customize the X-axis
        val xAxis = lineChart.xAxis
        xAxis.position = XAxis.XAxisPosition.BOTTOM
        xAxis.setDrawGridLines(false)
        xAxis.setDrawLabels(false)
        xAxis.setDrawAxisLine(false)

        // Customize the Y-axis
        val yAxis = lineChart.axisLeft
        yAxis.setDrawGridLines(false)
        yAxis.setDrawLabels(false)
        yAxis.setDrawAxisLine(false)
        lineChart.axisRight.isEnabled = false

        // Customize the legend
        lineChart.legend.isEnabled = false

        // Disable zoom and interactions
        lineChart.setTouchEnabled(false)
        lineChart.setPinchZoom(false)
        lineChart.setScaleEnabled(false)
        lineChart.isDoubleTapToZoomEnabled = false

        // Disable drawing of borders and offsets
        lineChart.setDrawBorders(false)
        lineChart.setViewPortOffsets(0f, 0f, 0f, 0f)
        lineChart.setExtraOffsets(0f, 0f, 0f, 0f)

        // Set the visible range and move the view to the last point
        lineChart.setVisibleXRangeMaximum(5f)
        lineChart.moveViewToX(6f)
        lineChart.setTouchEnabled(true)

        lineChart.setOnChartValueSelectedListener(object: OnChartValueSelectedListener {
            override fun onValueSelected(e: Entry?, h: Highlight?) {
                val current = DecimalFormat("#,##0.00").format(h?.y)

                binding.fragmentChartTvValue.text = current.substring(0, current.length-3)
                binding.fragmentChartTvDecimal.text = current.substring(current.length-3, current.length)
                binding.fragmentChartTvAllTimeIncrease.text = "${h?.x}"
                binding.fragmentChartTvDate.text = "${h?.x}"
            }

            override fun onNothingSelected() {
            }

        })

        // Set up the listener for when the user stops interacting with the chart
        lineChart.onChartGestureListener = object : OnChartGestureListener {
            override fun onChartGestureStart(me: MotionEvent?, lastPerformedGesture: ChartTouchListener.ChartGesture?) {
                val h = lineChart.getHighlightByTouchPoint(me?.x ?: 0F, me?.y ?: 0F)
                val current = DecimalFormat("#,##0.00").format(h?.y)

                binding.fragmentChartAllTime.visibility = View.GONE
                binding.fragmentChartTvDate.visibility = View.VISIBLE
                binding.fragmentChartTvValue.text = current.substring(0, current.length-3)
                binding.fragmentChartTvDecimal.text = current.substring(current.length-3, current.length)
                binding.fragmentChartTvDate.text = "${h?.x}"
                binding.fragmentChartTvAllTimeIncrease.text = "${h?.x}"
            }

            @SuppressLint("SetTextI18n")
            override fun onChartGestureEnd(
                me: MotionEvent?,
                lastPerformedGesture: ChartTouchListener.ChartGesture?
            ) {
                binding.fragmentChartTvDate.visibility= View.GONE
                binding.fragmentChartAllTime.visibility = View.VISIBLE
                binding.fragmentChartTvValue.text = "1,000"
                binding.fragmentChartTvDecimal.text = ".00"
                lineChart.highlightValue(null)
            }

            override fun onChartLongPressed(me: MotionEvent?) {}

            override fun onChartDoubleTapped(me: MotionEvent?) {}

            override fun onChartFling(me1: MotionEvent?, me2: MotionEvent?, velocityX: Float, velocityY: Float) {}

            override fun onChartScale(me: MotionEvent?, scaleX: Float, scaleY: Float) {}

            override fun onChartTranslate(me: MotionEvent?, dX: Float, dY: Float) {}

            override fun onChartSingleTapped(me: MotionEvent?) {}
        }

    }

    private fun setData(entries: List<Entry>) {
        val dataSet = LineDataSet(entries, "Label")
        dataSet.setDrawFilled(true)
        dataSet.setDrawCircles(false)
        dataSet.setDrawValues(false)
        dataSet.color = ContextCompat.getColor(this.requireContext(), R.color.white)
        dataSet.highLightColor = ContextCompat.getColor(this.requireContext(), R.color.white) // set highlight color to white
        dataSet.lineWidth = 1f
        dataSet.setDrawHorizontalHighlightIndicator(false)

        // Add gradient to the fill color
        val fillGradient = ContextCompat.getDrawable(this.requireContext(), R.drawable.chart_fill_gradient)
        dataSet.fillDrawable = fillGradient

        lineChart.highlightValue(null)

        val lineData = LineData(dataSet)
        lineChart.data = lineData

        // Invalidate the chart to apply all changes
        lineChart.invalidate()
    }
}