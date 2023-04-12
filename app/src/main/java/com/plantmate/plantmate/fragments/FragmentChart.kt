package com.plantmate.plantmate.fragments

import android.annotation.SuppressLint
import android.os.Bundle
import android.security.identity.CredentialDataResult.Entries
import android.util.Log
import android.view.LayoutInflater
import android.view.MotionEvent
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import com.github.mikephil.charting.charts.LineChart
import com.github.mikephil.charting.components.XAxis
import com.github.mikephil.charting.components.YAxis
import com.github.mikephil.charting.data.Entry
import com.github.mikephil.charting.data.LineData
import com.github.mikephil.charting.data.LineDataSet
import com.github.mikephil.charting.highlight.Highlight
import com.github.mikephil.charting.listener.ChartTouchListener
import com.github.mikephil.charting.listener.OnChartGestureListener
import com.github.mikephil.charting.listener.OnChartValueSelectedListener
import com.google.firebase.Timestamp
import com.google.type.DateTime
import com.plantmate.plantmate.CallBack.EntryCallback
import com.plantmate.plantmate.DAO.DataHelper.Companion.dataGenerate
import com.plantmate.plantmate.DAO.TransactionDao
import com.plantmate.plantmate.R
import com.plantmate.plantmate.databinding.FragmentChartBinding
import com.plantmate.plantmate.objects.FragmentUtils
import java.sql.Time
import java.text.DecimalFormat
import java.text.SimpleDateFormat
import java.util.*
import kotlin.math.abs


class FragmentChart : Fragment(R.layout.fragment_chart)  {
    private lateinit var binding: FragmentChartBinding
    private lateinit var btnNetProfit : ConstraintLayout
    private lateinit var btnSale : ConstraintLayout
    private lateinit var btnInventory : ConstraintLayout
    private lateinit var btnPurchase : ConstraintLayout
    private lateinit var btnWithered : ConstraintLayout

    private lateinit var btn1W : Button
    private lateinit var btn1M : Button
    private lateinit var btn3M : Button
    private lateinit var btn1Y : Button
    private lateinit var btnAll : Button

    private lateinit var lineChart: LineChart
    private lateinit var documents: MutableList<Map<String, Any>>
    var chartsData: Array<Array<MutableList<Entry>>> = Array(5) { Array(5) { mutableListOf() } }
    private var currType: Int = 0
    private var currSize: Int = 0
    val td = TransactionDao()
    var minY = Array(5){Float.MAX_VALUE}
    val trend = Array(5){ mutableMapOf<String, Any>() }

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
        btn1W = binding.fragmentChartBtn1w
        btn1M = binding.fragmentChartBtn1m
        btn3M = binding.fragmentChartBtn3m
        btn1Y = binding.fragmentChartBtn1y
        btnAll = binding.fragmentChartBtnAll

        lineChart = binding.lineChart
        val transactionFragment = FragmentTransaction()
        FragmentUtils.replaceFragmentInit(transactionFragment, R.id.fragment_chart_fragment_view, parentFragmentManager)
        setupLineChart()
        chartDataGet()


        btnNetProfit.setOnClickListener{
            removeBtn(btnNetProfit)
            currType=0
            binding.fragmentChartIvBackground.setBackgroundColor(resources.getColor(R.color.primary))

            setData(chartsData[currType][currSize])
            currentValue(currType)
        }
        btnSale.setOnClickListener{
            removeBtn(btnSale)
            currType=1
            binding.fragmentChartIvBackground.setBackgroundColor(resources.getColor(R.color.yellow_primary))

            setData(chartsData[currType][currSize])
            currentValue(currType)
        }
        btnInventory.setOnClickListener{
            removeBtn(btnInventory)
            currType=3
            binding.fragmentChartIvBackground.setBackgroundColor(resources.getColor(R.color.purple_primary))

            setData(chartsData[currType][currSize])
            currentValue(currType)
        }
        btnPurchase.setOnClickListener{
            removeBtn(btnPurchase)
            currType=2
            binding.fragmentChartIvBackground.setBackgroundColor(resources.getColor(R.color.orange_primary))

            setData(chartsData[currType][currSize])
            currentValue(currType)
        }
        btnWithered.setOnClickListener{
            removeBtn(btnWithered)
            currType=4
            binding.fragmentChartIvBackground.setBackgroundColor(resources.getColor(R.color.red_primary))

            setData(chartsData[currType][currSize])
            currentValue(currType)
        }
        btn1W.setOnClickListener {
            removeTimeLine(btn1W)
            currSize=0
            setData(chartsData[currType][currSize])
        }
        btn1M.setOnClickListener {
            removeTimeLine(btn1M)
            currSize=1
            setData(chartsData[currType][currSize])
        }
        btn3M.setOnClickListener {
            removeTimeLine(btn3M)
            currSize=2
            setData(chartsData[currType][currSize])
        }
        btn1Y.setOnClickListener {
            removeTimeLine(btn1Y)
            currSize=3
            setData(chartsData[currType][currSize])
        }
        btnAll.setOnClickListener {
            removeTimeLine(btnAll)
            currSize=4
            setData(chartsData[currType][currSize])
        }

        return binding.root
    }

    override fun onResume() {
        if(::lineChart.isInitialized){
            currType = 0
            currSize = 0
            chartDataGet()
        }
        super.onResume()
    }
    private fun selectButton(button: ConstraintLayout){
        button.setBackgroundResource(R.drawable.round_rectangle)
        button.elevation = 9.0F
    }
    private fun selectTimeLine(button: Button){
        button.setBackgroundResource(R.drawable.chart_fill_gradient)
    }
    private fun removeTimeLine(button: Button){
        listOf(btn1W, btn1M, btn3M, btn1Y, btnAll).forEach {
            it.setBackgroundResource(R.color.transparent)
        }
        selectTimeLine(button)
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
                if(h!=null){
                    val current = DecimalFormat("#,##0.00").format(h.y+minY[currType])
                    Log.d("values", "${h.y} and ${minY[currType]}")
                    binding.fragmentChartTvValue.text = current.substring(0, current.length-3)
                    binding.fragmentChartTvDecimal.text = current.substring(current.length-3, current.length)
                    val time = documents[h.x!!.toInt()]["date"] as Timestamp
                    binding.fragmentChartTvDate.text = getDateWithoutTime(time.toDate())
                    binding.fragmentChartTvAllTimeIncrease.text = "${h.x}"
                }
            }

            override fun onNothingSelected() {
            }

        })

        // Set up the listener for when the user stops interacting with the chart
        lineChart.onChartGestureListener = object : OnChartGestureListener {
            override fun onChartGestureStart(me: MotionEvent?, lastPerformedGesture: ChartTouchListener.ChartGesture?) {
                val h = lineChart.getHighlightByTouchPoint(me?.x ?: 0F, me?.y ?: 0F)
                if(h!=null){

                    val current = DecimalFormat("#,##0.00").format(h.y+minY[currType])
                    Log.d("values", "${h.y} and ${minY[currType]}")
                    binding.fragmentChartAllTime.visibility = View.GONE
                    binding.fragmentChartTvDate.visibility = View.VISIBLE
                    binding.fragmentChartTvValue.text = current.substring(0, current.length-3)
                    binding.fragmentChartTvDecimal.text = current.substring(current.length-3, current.length)
                    val time = documents[h.x!!.toInt()]["date"] as Timestamp
                    binding.fragmentChartTvDate.text = getDateWithoutTime(time.toDate())
                    binding.fragmentChartTvAllTimeIncrease.text = "${h.x}"
                }
            }


            @SuppressLint("SetTextI18n")
            override fun onChartGestureEnd(
                me: MotionEvent?,
                lastPerformedGesture: ChartTouchListener.ChartGesture?
            ) {
                currentValue(currType)
            }

            override fun onChartLongPressed(me: MotionEvent?) {}
            override fun onChartDoubleTapped(me: MotionEvent?) {}
            override fun onChartFling(me1: MotionEvent?, me2: MotionEvent?, velocityX: Float, velocityY: Float) {}
            override fun onChartScale(me: MotionEvent?, scaleX: Float, scaleY: Float) {}
            override fun onChartTranslate(me: MotionEvent?, dX: Float, dY: Float) {}
            override fun onChartSingleTapped(me: MotionEvent?) {}
        }

    }
    fun setFragmentVal(){
        val formatter = DecimalFormat("₱#,##0.00")
        binding.fragmentChartTvNetProfit.text = formatter.format(trend[0]["Value"])
        binding.fragmentChartTvSales.text = formatter.format(trend[1]["Value"])
        binding.fragmentChartTvPurchase.text = formatter.format(trend[2]["Value"])
        binding.fragmentChartTvInventory.text = formatter.format(trend[3]["Value"])
        binding.fragmentChartTvWithered.text = formatter.format(trend[4]["Value"])
    }
    @SuppressLint("SetTextI18n")
    fun currentValue(transactionType: Int){
        binding.fragmentChartTvDate.visibility= View.GONE
        binding.fragmentChartAllTime.visibility = View.VISIBLE
        val current = DecimalFormat("#,##0.00").format(trend[transactionType]["Value"] as Float)
        binding.fragmentChartTvValue.text = current.substring(0, current.length-3)
        binding.fragmentChartTvDecimal.text = current.substring(current.length-3, current.length)
        if((trend[transactionType]["Percentage"] as Float)>=0f){
            binding.fragmentChartIvTrend.setImageResource(R.drawable.up)
            binding.fragmentChartTvAllTimeIncrease.setTextColor(ContextCompat.getColor(requireContext(), R.color.primary))
        }
        else {
            binding.fragmentChartIvTrend.setImageResource(R.drawable.down)
            binding.fragmentChartTvAllTimeIncrease.setTextColor(ContextCompat.getColor(requireContext(), R.color.red_primary))
        }
        binding.fragmentChartTvAllTimeIncrease.text =  "${DecimalFormat("#.##").format(trend[transactionType]["Percentage"])}%"

        lineChart.highlightValue(null)
    }
    private fun chartDataGet(){
        td.read("aggregate", object: EntryCallback{
            override fun onSuccess(values: List<Map<String, Any>>) {
                for(i in 0 .. 4){
                    for(j in 0 .. 4){
                        chartsData[i][j]= mutableListOf()
                    }
                }
                for(i in 0..4)
                    minY[i] = Float.MAX_VALUE

                documents= mutableListOf()
                val lastVal = Array(5){0f}
                val week = Calendar.getInstance()
                val month= Calendar.getInstance()
                val month3= Calendar.getInstance()
                val year= Calendar.getInstance()
                week.add(Calendar.WEEK_OF_YEAR, -1)
                month.add(Calendar.MONTH,-1)
                month3.add(Calendar.MONTH, -3)
                year.add(Calendar.YEAR, -1)

                val dateTimeline = arrayOf<Calendar>(
                    week,
                    month,
                    month3,
                    year
                )

                var previousDate: Date = year.time
                previousDate.seconds=0
                previousDate.minutes=0
                previousDate.hours=0

                var index=0
                val initValue = arrayListOf(
                    -1f,
                    -1f,
                    -1f,
                    -1f,
                    -1f
                )
                var firstData = false
                for(value in values){
                    val dateStamp = value["date"] as Timestamp
                    val date = dateStamp.toDate()
                    val netProfit = value["netprofit"] as Number
                    val sales = value["sales"] as Number
                    val purchases = value["purchases"] as Number
                    val withered = value["withered"] as Number
                    val propagation = value["propagation"] as Number
                    if(initValue[0] == -1f){
                        initValue[0] = netProfit.toFloat()
                        initValue[1] = sales.toFloat()
                        initValue[2] = purchases.toFloat()
                        initValue[3] = withered.toFloat()
                        initValue[4] = propagation.toFloat()
                    }
                    date.seconds=0
                    date.minutes=0
                    date.hours=0
                    date.time = date.time/1000*1000

                    if(previousDate>date)
                        previousDate=date

                    val increaser = Calendar.getInstance()


                    increaser.time=Date(previousDate.time / 1000 * 1000)
                    while(previousDate<date){
                        val default = mapOf<String, Any>(
                            "date" to Timestamp(previousDate),
                            "netprofit" to 0f,
                            "sales" to 0f,
                            "purchases" to 0f,
                            "withered" to 0f,
                            "propagation" to 0f
                        )
                        for(j in 0 .. 4){
                            if(firstData)
                                chartsData[j][4].add(Entry(index.toFloat(), lastVal[j]))

                            for(k in 0 .. 3) {
                                if (dateTimeline[k].time <= previousDate) {
                                    chartsData[j][k].add(
                                        Entry(
                                            index.toFloat(),
                                            lastVal[j]
                                        )
                                    )
                                }
                            }
                        }
                        documents.add(index, default)

                        increaser.add(Calendar.DATE, 1)
                        previousDate=increaser.time
                        index++
                    }

                    lastVal[0]+=netProfit.toFloat()
                    lastVal[1]+=sales.toFloat()
                    lastVal[2]+=purchases.toFloat()
                    lastVal[3]+=withered.toFloat()
                    lastVal[4]+=propagation.toFloat()

                    for(j in 0 .. 4){
                        if(!firstData)
                            firstData=true
                        chartsData[j][4].add(Entry(index.toFloat(), lastVal[j]))

                        for(k in 0 .. 3) {
                            if (dateTimeline[k].time <= date) {
                                chartsData[j][k].add(
                                    Entry(
                                        index.toFloat(),
                                        lastVal[j]
                                    )
                                )
                            }
                        }
                    }

                    for(vals in 0..4)
                        if(minY[vals]>lastVal[vals] && lastVal[vals]!=0f)
                            minY[vals]=lastVal[vals]
                    documents.add(index, value)
                    increaser.time = Date(date.time/1000*1000)
                    increaser.add(Calendar.DATE, 1)
                    previousDate = increaser.time
                    index++
                }
                for(i in 0..4){
                    trend[i] = mutableMapOf(
                        "Value" to lastVal[i],
                        "Percentage" to ((lastVal[i]-minY[i])/ abs(minY[i]))*100
                    )
                }
                //translate values
                for(j in 0 .. 4)
                    for(k in 0 .. 4)
                        for(e in chartsData[k][j])
                            e.y -= minY[k]



                currentValue(currType)
                setData(chartsData[currType][currSize])
                setFragmentVal()
            }
            override fun onFailure(exception: Exception) {
                Log.e("Error", "${exception.stackTrace}")
            }
        })
    }

    fun getDateWithoutTime(date: Date): String {
        return SimpleDateFormat("EEEE, MMMM dd, yyyy", Locale.getDefault()).format(date)
    }
    private fun setData(entries: List<Entry>) {
        val dataSet = LineDataSet(entries, "Label")
        dataSet.setDrawFilled(false)
        dataSet.setDrawCircles(false)
        dataSet.setDrawValues(false)
        dataSet.color = ContextCompat.getColor(this.requireContext(), R.color.white)
        dataSet.highLightColor = ContextCompat.getColor(this.requireContext(), R.color.white) // set highlight color to white
        dataSet.lineWidth = 1f
        dataSet.setDrawHorizontalHighlightIndicator(false)

        lineChart.highlightValue(null)

        val lineData = LineData(dataSet)
        lineChart.data = lineData

        lineChart.invalidate()
    }
}