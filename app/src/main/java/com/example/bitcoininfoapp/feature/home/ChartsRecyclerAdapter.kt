package com.example.bitcoininfoapp.feature.home

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.bitcoininfoapp.R
import com.example.bitcoininfoapp.data.models.ChartDetailsResponse
import com.example.bitcoininfoapp.utils.getDateStringWithFullMonthName
import com.github.mikephil.charting.charts.LineChart
import com.github.mikephil.charting.components.AxisBase
import com.github.mikephil.charting.components.Description
import com.github.mikephil.charting.components.XAxis
import com.github.mikephil.charting.components.YAxis
import com.github.mikephil.charting.data.Entry
import com.github.mikephil.charting.data.LineData
import com.github.mikephil.charting.data.LineDataSet
import com.github.mikephil.charting.formatter.ValueFormatter
import kotlinx.android.synthetic.main.item_chart.view.*


class ChartsRecyclerAdapter(
    private val charts: List<ChartDetailsResponse>
) : RecyclerView.Adapter<ChartsRecyclerAdapter.ChartViewHolder>() {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int) = ChartViewHolder(
        LayoutInflater.from(parent.context).inflate(
            R.layout.item_chart,
            parent,
            false
        )
    )

    override fun getItemCount() = charts.size

    override fun onBindViewHolder(holder: ChartViewHolder, position: Int) =
        holder.bind(charts[position])

    class ChartViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        fun bind(chartDetailsResponse: ChartDetailsResponse) {

            with(itemView) {
                chartTitle.text = chartDetailsResponse.name
                chartSubtitle.text = chartDetailsResponse.description
                val dataSet = createChartLineDataSet(chartDetailsResponse)

                chartView.data = LineData(dataSet)
                chartStyling(chartView)

                chartView.zoomIn()
                chartView.invalidate()
            }
        }

        /**
         * Creates the chart LineDataSet and
         */
        private fun createChartLineDataSet(chartDetailsResponse: ChartDetailsResponse): LineDataSet {
            val entries = chartDetailsResponse.values.map { valueItem ->
                Entry(
                    valueItem.timestamp,
                    valueItem.value
                )
            }
            val dataSet = LineDataSet(entries, "BTC")
            dataSet.color = R.color.colorAccent
            dataSet.axisDependency = YAxis.AxisDependency.LEFT
            dataSet.setDrawValues(false)
            return dataSet
        }


        /**
         * Make the chart styling and format
         */
        private fun chartStyling(chartView: LineChart){
            val formatter = object : ValueFormatter() {
                override fun getAxisLabel(value: Float, axis: AxisBase?): String {
                    return value.toLong().getDateStringWithFullMonthName()
                }
            }
            val xAxis = chartView.xAxis
            xAxis.granularity = 1f
            xAxis.valueFormatter = formatter
            xAxis.position = XAxis.XAxisPosition.BOTTOM

            val yAxis = chartView.axisRight
            yAxis.isEnabled = false

            chartView.description = null
        }
    }
}