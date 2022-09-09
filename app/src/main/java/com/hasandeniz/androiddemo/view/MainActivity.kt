package com.hasandeniz.androiddemo.view

import android.content.Context
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.widget.ArrayAdapter
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.gson.Gson
import com.hasandeniz.androiddemo.R
import com.hasandeniz.androiddemo.adapter.StockAdapter
import com.hasandeniz.androiddemo.databinding.ActivityMainBinding
import com.hasandeniz.androiddemo.model.Stock
import com.hasandeniz.androiddemo.model.StockDataDetails
import com.hasandeniz.androiddemo.viewmodel.StockViewModel
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.item_stock.*


class MainActivity : AppCompatActivity() {

    //Variable declarations
    private lateinit var binding: ActivityMainBinding
    private lateinit var stockViewModel: StockViewModel

    private val stockAdapter = StockAdapter(arrayListOf(), arrayListOf())
    private var selectableHeaderFieldsNameList = arrayListOf<String>()
    private var selectableHeaderFieldsKeyList = arrayListOf<String>()

    private var stockList = arrayListOf<Stock>()
    private var stockCodeNameList = arrayListOf<String>()

    private var leftSelectedField = "las"
    private var rightSelectedField = "pdd"

    private val mainHandler = Handler(Looper.getMainLooper())
    private lateinit var updateUI: Runnable

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        val view = binding.root
        setContentView(view)

        //setting up viewmodel
        stockViewModel = ViewModelProvider(this)[StockViewModel::class.java]

        //setting up recyclerview
        stockRecyclerview.layoutManager = LinearLayoutManager(this)
        stockRecyclerview.adapter = stockAdapter

        //initializing runnable object which will make api call every second
        updateUI = object : Runnable{
            override fun run() {
                stockViewModel.stockListAndFieldsFromAPI()
                observeStockListLiveData()
                mainHandler.postDelayed(this, 1000)
            }
        }

        //getting selectable field values for headers from api
        observeHeaderFieldLiveData()

        //setting up both left and right headers
        val arrayAdapter = ArrayAdapter(this, R.layout.item_dropdown, selectableHeaderFieldsNameList)
        leftAutoCompleteTextView.setAdapter(arrayAdapter)
        rightAutoCompleteTextView.setAdapter(arrayAdapter)

        //setting up header onClick events
        //storing which fields are selected for ui and saving it to a variable to send recyclerview adapter later
        leftAutoCompleteTextView.setOnItemClickListener { _, _, position, _ ->
            leftSelectedField = selectableHeaderFieldsKeyList[position]
        }

        rightAutoCompleteTextView.setOnItemClickListener { _, _, position, _ ->
            rightSelectedField = selectableHeaderFieldsKeyList[position]
        }
    }

    //function that makes api call to get selectable header fields
    private fun observeHeaderFieldLiveData(){
        stockViewModel.selectableHeaderFieldsLiveData.observe(this) {
            selectableHeaderFieldsNameList.clear()
            selectableHeaderFieldsKeyList.clear()
            it.forEach { headerFieldsFromAPI ->
                //saving names of the fields to a list to show them on selectable header menus
                selectableHeaderFieldsNameList.add(headerFieldsFromAPI.name)
                //saving keys of the fields to a list to use them to decide which field is selected to show in UI.
                selectableHeaderFieldsKeyList.add(headerFieldsFromAPI.key)
            }
        }
    }

    //function that makes api call to get name list of the stocks in order to show them in recyclerview UI
    //and calling another function to get stock data's
    private fun observeStockListLiveData(){
        stockViewModel.stockListLiveData.observe(this){
            stockCodeNameList.clear()
            it.forEach { stockDetail->
                stockCodeNameList.add(stockDetail.cod)
            }
            stockViewModel.getStockDataFromAPI()
            observeStockDataLiveData()
        }
    }

    //function to make api calls to get related data for stocks and feed the recyclerview with them
    private fun observeStockDataLiveData() {
        stockViewModel.stockDataLiveData.observe(this) {stockDataList ->
            //initializing shared preferences with key "Cache" to use on caching the previous data for stocks
            val sharedPreferences = getSharedPreferences("Cache" , Context.MODE_PRIVATE)
            val prefsEditor = sharedPreferences.edit()

            //getting previous stock data from shared preferences
            val gson = Gson()
            val jsonText = sharedPreferences.getString("cachePrice", null)
            val tempStockList = gson.fromJson(jsonText, Array<Stock>::class.java)

            //calling function to update the stockList which to be sent to recyclerview adapter and shown in the UI
            updateRecyclerviewListFromAPI(tempStockList, stockDataList)

            //saving current stock data to shared preferences in order to use it later for comparing old and current values of stocks
            val getGson = Gson()
            val getJsonText = getGson.toJson(stockList)
            prefsEditor.clear().apply()
            prefsEditor.putString("cachePrice", getJsonText).apply()

            stockAdapter.updateStockList(stockList,stockDataList)
            stockList.clear()

            //removing observers from livedata to prevent triggering more than one observation on each function call
            // (triggering live data more than once prevent the UI from making comparison between old and current data by sending same data more than once)
            stockViewModel.stockDataLiveData.removeObservers(this)

        }
    }

    //function that updates stockList which to be sent to recyclerview and makes some comparisons for UI purposes(e.g comparing old and new "last update time" to decide which
    // recyclerview rows will be highlighted).
    private fun updateRecyclerviewListFromAPI(tempStockList: Array<Stock>?, stockDataList: List<StockDataDetails>){
        var highlight = false
        for (i in stockDataList.indices){
            //creating "Stock" objects to be shown in the UI according to selected fields for headers and comparing old and new price to indicate the price change
            // with colors and arrows.
            if (!tempStockList.isNullOrEmpty() && tempStockList[i].price != null && stockDataList[i].las != null){
                if (stockDataList[i].clo != tempStockList[i].lastUpdateTime)
                    highlight = true
                if (tempStockList[i].price!! < stockDataList[i].las!!){
                    val stock = Stock(stockCodeNameList[i],stockDataList[i].clo,leftSelectedField,rightSelectedField,"Up",stockDataList[i].las,highlight)
                    stockList.add(stock)
                }else if(tempStockList[i].price!! > stockDataList[i].las!!){
                    val stock = Stock(stockCodeNameList[i],stockDataList[i].clo,leftSelectedField,rightSelectedField,"Down",stockDataList[i].las,highlight)
                    stockList.add(stock)
                }else{
                    val stock = Stock(stockCodeNameList[i],stockDataList[i].clo,leftSelectedField,rightSelectedField,"No Change",stockDataList[i].las,highlight)
                    stockList.add(stock)
                }
            }else{
                val stock = Stock(stockCodeNameList[i],stockDataList[i].clo,leftSelectedField,rightSelectedField,"No Change",stockDataList[i].las,highlight)
                stockList.add(stock)
            }
            highlight = false
        }
    }

    //resuming the api calls every second on onResume
    override fun onResume() {
        super.onResume()
        mainHandler.post(updateUI)
    }

    //pausing the api calls when application goes onPause
    override fun onPause() {
        super.onPause()
        mainHandler.removeCallbacks(updateUI)
    }

    //clearing disposable which contains api calls that we make. Helps application to use device memory better
    override fun onDestroy() {
        super.onDestroy()
        stockViewModel.clearDisposable()
    }


}