package com.hasandeniz.androiddemo.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.hasandeniz.androiddemo.R
import com.hasandeniz.androiddemo.model.Stock
import com.hasandeniz.androiddemo.model.StockDataDetails
import kotlinx.android.synthetic.main.item_stock.view.*

class StockAdapter(private val stockList: ArrayList<Stock>, private val stockDataDetailsList: ArrayList<StockDataDetails>): RecyclerView.Adapter<StockAdapter.StockViewHolder>() {

    class StockViewHolder(var view: View): RecyclerView.ViewHolder(view)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): StockViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        val view = inflater.inflate(R.layout.item_stock,parent,false)
        return StockViewHolder(view)
    }

    override fun onBindViewHolder(holder: StockViewHolder, position: Int) {
        val currentStock = stockList[position]
        holder.view.name.text = currentStock.name
        holder.view.lastUpdateTime.text = currentStock.lastUpdateTime
        if (currentStock.highlight)
            holder.view.setBackgroundColor(holder.view.context.resources.getColor(R.color.highlight, holder.view.context.theme))
        else
            holder.view.setBackgroundColor(holder.view.context.resources.getColor(R.color.white, holder.view.context.theme))

        //arranging indicator colors and arrows for each stock row
        adjustIndicatorColor(holder, currentStock)

        //adjusting the shown ui according to selected header fields
        adjustSelectedFieldUI(holder, currentStock, position)
    }

    override fun getItemCount(): Int {
        return stockList.size
    }

    //function to arrange ui according to selected header fields
    private fun adjustSelectedFieldUI(holder: StockViewHolder, currentStock: Stock, position: Int) {
        holder.view.leftHeaderValue.setTextColor(holder.view.context.resources.getColor(R.color.black,holder.view.context.theme))
        holder.view.rightHeaderValue.setTextColor(holder.view.context.resources.getColor(R.color.black,holder.view.context.theme))

        //switch case for changing shown values according to selected field.
        when(currentStock.selectedLeftFieldValue){
            "pdd" -> {
                holder.view.leftHeaderValue.text = stockDataDetailsList[position].pdd
                if (!stockDataDetailsList[position].pdd.isNullOrEmpty()){
                    if (stockDataDetailsList[position].pdd!! < 0.toString())
                        holder.view.leftHeaderValue.setTextColor(holder.view.context.resources.getColor(R.color.red,holder.view.context.theme))
                    else if (stockDataDetailsList[position].pdd!! > 0.toString())
                        holder.view.leftHeaderValue.setTextColor(holder.view.context.resources.getColor(R.color.green,holder.view.context.theme))
                    else
                        holder.view.leftHeaderValue.setTextColor(holder.view.context.resources.getColor(R.color.black,holder.view.context.theme))
                }
            }
            "las" -> holder.view.leftHeaderValue.text = stockDataDetailsList[position].las
            "ddi" -> {
                holder.view.leftHeaderValue.text = stockDataDetailsList[position].ddi
                if (!stockDataDetailsList[position].ddi.isNullOrEmpty()){
                    if (stockDataDetailsList[position].ddi!! < 0.toString())
                        holder.view.leftHeaderValue.setTextColor(holder.view.context.resources.getColor(R.color.red,holder.view.context.theme))
                    else if (stockDataDetailsList[position].ddi!! > 0.toString())
                        holder.view.leftHeaderValue.setTextColor(holder.view.context.resources.getColor(R.color.green,holder.view.context.theme))
                    else
                        holder.view.leftHeaderValue.setTextColor(holder.view.context.resources.getColor(R.color.black,holder.view.context.theme))
                }
                holder.view.leftHeaderValue.text = stockDataDetailsList[position].ddi
            }
            "low" -> holder.view.leftHeaderValue.text = stockDataDetailsList[position].low
            "hig" -> holder.view.leftHeaderValue.text = stockDataDetailsList[position].hig
            "buy" -> holder.view.leftHeaderValue.text = stockDataDetailsList[position].buy
            "sel" -> holder.view.leftHeaderValue.text = stockDataDetailsList[position].sel
            "pdc" -> holder.view.leftHeaderValue.text = stockDataDetailsList[position].pdc
            "cei" -> holder.view.leftHeaderValue.text = stockDataDetailsList[position].cei
            "flo" -> holder.view.leftHeaderValue.text = stockDataDetailsList[position].flo
            "gco" -> holder.view.leftHeaderValue.text = stockDataDetailsList[position].gco
        }

        when(currentStock.selectedRightFieldValue){
            "pdd" -> {
                holder.view.rightHeaderValue.text = stockDataDetailsList[position].pdd
                if (!stockDataDetailsList[position].pdd.isNullOrEmpty()){
                    if (stockDataDetailsList[position].pdd!! < 0.toString())
                        holder.view.rightHeaderValue.setTextColor(holder.view.context.resources.getColor(R.color.red,holder.view.context.theme))
                    else if (stockDataDetailsList[position].pdd!! > 0.toString())
                        holder.view.rightHeaderValue.setTextColor(holder.view.context.resources.getColor(R.color.green,holder.view.context.theme))
                    else
                        holder.view.rightHeaderValue.setTextColor(holder.view.context.resources.getColor(R.color.black,holder.view.context.theme))
                }
            }
            "las" -> holder.view.rightHeaderValue.text = stockDataDetailsList[position].las
            "ddi" -> {
                holder.view.rightHeaderValue.text = stockDataDetailsList[position].ddi
                if (!stockDataDetailsList[position].ddi.isNullOrEmpty()){
                    if (stockDataDetailsList[position].ddi!! < 0.toString())
                        holder.view.rightHeaderValue.setTextColor(holder.view.context.resources.getColor(R.color.red,holder.view.context.theme))
                    else if (stockDataDetailsList[position].ddi!! > 0.toString())
                        holder.view.rightHeaderValue.setTextColor(holder.view.context.resources.getColor(R.color.green,holder.view.context.theme))
                    else
                        holder.view.rightHeaderValue.setTextColor(holder.view.context.resources.getColor(R.color.black,holder.view.context.theme))
                }

            }
            "low" -> holder.view.rightHeaderValue.text = stockDataDetailsList[position].low
            "hig" -> holder.view.rightHeaderValue.text = stockDataDetailsList[position].hig
            "buy" -> holder.view.rightHeaderValue.text = stockDataDetailsList[position].buy
            "sel" -> holder.view.rightHeaderValue.text = stockDataDetailsList[position].sel
            "pdc" -> holder.view.rightHeaderValue.text = stockDataDetailsList[position].pdc
            "cei" -> holder.view.rightHeaderValue.text = stockDataDetailsList[position].cei
            "flo" -> holder.view.rightHeaderValue.text = stockDataDetailsList[position].flo
            "gco" -> holder.view.rightHeaderValue.text = stockDataDetailsList[position].gco
        }
    }

    //adjusting color of indicator color and way of the arrows according to price change
    private fun adjustIndicatorColor(holder: StockViewHolder, currentStock: Stock){
        if (currentStock.priceChange.equals("Up")){
            holder.view.indicatorArrowUp.visibility = View.VISIBLE
            holder.view.indicatorArrowDown.visibility = View.GONE
            holder.view.indicatorDash.visibility = View.GONE
            holder.view.indicatorColor.setBackgroundColor(holder.view.context.resources.getColor(R.color.green, holder.view.context.theme))
        }else if(currentStock.priceChange.equals("Down")){
            holder.view.indicatorArrowDown.visibility = View.VISIBLE
            holder.view.indicatorArrowUp.visibility = View.GONE
            holder.view.indicatorDash.visibility = View.GONE
            holder.view.indicatorColor.setBackgroundColor(holder.view.context.resources.getColor(R.color.red, holder.view.context.theme))
        }else{
            holder.view.indicatorArrowDown.visibility = View.GONE
            holder.view.indicatorArrowUp.visibility = View.GONE
            holder.view.indicatorDash.visibility = View.VISIBLE
            holder.view.indicatorColor.setBackgroundColor(holder.view.context.resources.getColor(androidx.appcompat.R.color.material_grey_600, holder.view.context.theme))
        }    }

    //function to update the UI from Activity
    fun updateStockList(newStockList: List<Stock>, newStockDataDetailsList: List<StockDataDetails>){
        stockList.clear()
        stockDataDetailsList.clear()
        stockList.addAll(newStockList)
        stockDataDetailsList.addAll(newStockDataDetailsList)
        notifyDataSetChanged()
    }

}