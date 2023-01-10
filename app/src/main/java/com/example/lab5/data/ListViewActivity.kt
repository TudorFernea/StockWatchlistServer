package com.example.lab5.data

import android.app.Activity
import android.app.AlertDialog
import android.content.Context
import android.content.Intent
import android.database.sqlite.SQLiteDatabase
import android.net.ConnectivityManager
import android.net.Network
import android.net.NetworkCapabilities
import android.os.Bundle
import android.util.Log
import android.view.Window
import android.widget.ImageView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.lab5.R
import com.example.lab5.adapter.ItemAdapter
import com.example.lab5.client.StockAPI
import com.example.lab5.database.DatabaseRepository
import com.example.lab5.database.Utils
import com.example.lab5.model.Stock
import com.example.lab5.service.AddStockActivity
import kotlinx.android.synthetic.main.activity_list_notes.*
import kotlinx.coroutines.launch
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class ListViewActivity : AppCompatActivity() {

    private lateinit var readableDatabase: SQLiteDatabase
    private lateinit var writableDatabase: SQLiteDatabase
    private lateinit var stockRepository: DatabaseRepository;

    lateinit var addButton: ImageView
    private lateinit var dbHelper: Utils;

    private var context = this;

    private fun isNetworkConnected(): Boolean {
        val connectivityManager =
            getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
        val activeNetwork = connectivityManager.activeNetwork
        val networkCapabilities = connectivityManager.getNetworkCapabilities(activeNetwork)
        return networkCapabilities != null &&
                networkCapabilities.hasCapability(NetworkCapabilities.NET_CAPABILITY_INTERNET)
    }

    override fun onCreate(savedInstanceState: Bundle?) {

        val str = "LIST ACTIVITY"
        Log.i(str, "Entered list activity")

        super.onCreate(savedInstanceState)

        setContentView(R.layout.activity_list_notes)

        supportActionBar?.hide()

        val window: Window = this@ListViewActivity.window
        window.statusBarColor = ContextCompat.getColor(this@ListViewActivity, R.color.black)


        dbHelper = Utils(this)
        readableDatabase = dbHelper.readableDatabase;
        writableDatabase = dbHelper.writableDatabase;
        stockRepository = DatabaseRepository.getInstance(readableDatabase, writableDatabase)!!

        lifecycleScope.launch {
            recyclerView.layoutManager = LinearLayoutManager(context)
            recyclerView.adapter = ItemAdapter(context)
        }

        if (!isNetworkConnected()) {
            showDialog()
        }

        val connectivityManager =
            getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
        connectivityManager.registerDefaultNetworkCallback(object :
            ConnectivityManager.NetworkCallback() {
            override fun onAvailable(network: Network) {
                lifecycleScope.launch {
                    StockAPI.retrofitService.retrieveAllStories()
                        .enqueue(object : Callback<List<Stock>?> {
                            override fun onResponse(
                                call: Call<List<Stock>?>,
                                response: Response<List<Stock>?>
                            ) {

                                val stocksServer = response.body()!!
                                Log.d("Stocks server", stocksServer.toString())
                                val stocksDatabase = stockRepository.getStories();

                                for (s1: Stock in stocksDatabase) {
                                    var exists = false
                                    for (s2: Stock in stocksServer) {
                                        if (s1.id!!.equals(s2.id)) {
                                            exists = true
                                        }
                                    }

                                    if (!exists) {
                                        StockAPI.retrofitService.createStock(s1)
                                            .enqueue(object : Callback<Stock?> {
                                                override fun onResponse(
                                                    call: Call<Stock?>,
                                                    response: Response<Stock?>
                                                ) {
                                                    Log.d(
                                                        "Added item from local db",
                                                        "Success: " + s1
                                                    )
                                                }

                                                override fun onFailure(
                                                    call: Call<Stock?>,
                                                    t: Throwable
                                                ) {
                                                    Toast.makeText(
                                                        context,
                                                        "Failed to add item from local db!",
                                                        Toast.LENGTH_SHORT
                                                    ).show()
                                                    Log.d(
                                                        "Added item from local db",
                                                        "Failed: " + t.message
                                                    )
                                                }
                                            })
                                    }

                                }


                                for (s2: Stock in stocksServer) {
                                    var exists = false
                                    for (s1: Stock in stocksDatabase) {
                                        if (s1.id!!.equals(s2.id)) {
                                            exists = true
                                        }
                                    }

                                    if (!exists) {
                                        StockAPI.retrofitService.deleteStock(s2.id!!)
                                            .enqueue(object : Callback<Stock?> {
                                                override fun onResponse(
                                                    call: Call<Stock?>,
                                                    response: Response<Stock?>
                                                ) {

//                                                stocks.remove(s2)
                                                    Log.d(
                                                        "Deleted item from server",
                                                        "Success!"
                                                    )
                                                }

                                                override fun onFailure(
                                                    call: Call<Stock?>,
                                                    t: Throwable
                                                ) {
                                                    Toast.makeText(
                                                        context,
                                                        "Failed to delete item from server",
                                                        Toast.LENGTH_SHORT
                                                    ).show()
                                                    Log.d(
                                                        "Deleted item from server",
                                                        "Failed! " + t.message
                                                    )
                                                }
                                            })
                                    }
                                }


                                for (s1: Stock in stocksDatabase) {
                                    var different = false
                                    for (s2: Stock in stocksServer) {
                                        if (s1.id!!.equals(s2.id) && ((!s1.symbol.equals(s2.symbol)) || !s1.sell_price?.equals(s2.sell_price)!! || !s1.desc.equals(s2.desc) || !s1.buy_price?.equals(s2.buy_price)!!)) {
                                            different = true
                                            Log.d(
                                                "Updated item from the server",
                                                "Success: " + s2
                                            )
                                        }
                                    }

                                    if (different) {
                                        StockAPI.retrofitService.updateStock(s1.id!!, s1)
                                            .enqueue(object : Callback<Stock?> {
                                                override fun onResponse(
                                                    call: Call<Stock?>,
                                                    response: Response<Stock?>
                                                ) {
                                                    Log.d(
                                                        "Updated item from the server",
                                                        "Success: " + s1
                                                    )
                                                }

                                                override fun onFailure(
                                                    call: Call<Stock?>,
                                                    t: Throwable
                                                ) {
                                                    Toast.makeText(
                                                        context,
                                                        "Failed to update item from server",
                                                        Toast.LENGTH_SHORT
                                                    ).show()
                                                    Log.d(
                                                        "Updated item from the server",
                                                        "Failed: " + t.message
                                                    )
                                                }
                                            })
                                    }

                                }
                            }

                            override fun onFailure(call: Call<List<Stock>?>, t: Throwable) {
                                lifecycleScope.launch {
                                    Toast.makeText(
                                        context,
                                        "Failed to check differences between local db and server",
                                        Toast.LENGTH_SHORT
                                    ).show()
                                    Log.d(
                                        "Check for differences between local db and server",
                                        "Failed: " + t.message
                                    )
                                }
                            }
                        })
                }
            }
        }
        )

        addButton = findViewById(R.id.addNoteButton)

        addButton.setOnClickListener {
            val intent = Intent(applicationContext, AddStockActivity::class.java)
            startActivityForResult(intent, 3);
        }


    }

    private fun showDialog() {
        AlertDialog.Builder(this).setTitle("No Internet Connection")
            .setMessage("Fallback on local DB")
            .setPositiveButton(android.R.string.ok) { _, _ -> }
            .setIcon(android.R.drawable.ic_dialog_alert).show()
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == 3) {
            if (resultCode == Activity.RESULT_OK) {
                if (data != null) {
                    val bundle = data.getBundleExtra("stockBundle")
                    val stock = bundle?.getParcelable<Stock>("stock")
                    if (stock != null) {
                        addStock(stock)
                    }
                }
                Toast.makeText(this, "Added!", Toast.LENGTH_SHORT).show()
            }
        } else if (requestCode == 5) {
            if (resultCode == Activity.RESULT_OK) {
                if (data != null) {
                    val bundle = data.getBundleExtra("stockBundle")
                    val stock = bundle?.getParcelable<Stock>("stock")
                    val id = data.getLongExtra("id", -1)
                    if (stock != null && id != (-1).toLong()) {
                        updateStock(stock, id)
                        Toast.makeText(this, "Updated!", Toast.LENGTH_SHORT).show()
                    }
                }
            }
        }
    }

    fun deleteStock(id: Long) {
        if (isNetworkConnected()) {
            lifecycleScope.launch {
                StockAPI.retrofitService.deleteStock(id).enqueue(object : Callback<Stock?> {
                    override fun onResponse(call: Call<Stock?>, response: Response<Stock?>) {
                        stockRepository.delete(id)
                        recyclerView.adapter?.notifyDataSetChanged()
                        Log.d("Delete stock action - server", "Success: " + response.body())
                    }

                    override fun onFailure(call: Call<Stock?>, t: Throwable) {
                        Toast.makeText(
                            context,
                            "Failed to delete stock" + t.message,
                            Toast.LENGTH_SHORT
                        ).show()
                        Log.d("Delete stock action - server", "Failed: " + t.message)
                    }
                })
            }

        } else {
            showDialog()
            stockRepository.delete(id)
            Log.d("Delete stock action - local database", "Success!")
        }
        recyclerView.adapter?.notifyDataSetChanged()

    }

    fun updateStock(stock: Stock, id: Long) {
        if (isNetworkConnected()) {
            lifecycleScope.launch {
                StockAPI.retrofitService.updateStock(id, stock).enqueue(object : Callback<Stock?> {
                    override fun onResponse(call: Call<Stock?>, response: Response<Stock?>) {
                        stockRepository.update(stock, id)
                        recyclerView.adapter?.notifyDataSetChanged()
                        Log.d("Update stock action - server", "Success: " + response.body())
                    }

                    override fun onFailure(call: Call<Stock?>, t: Throwable) {
                        Toast.makeText(
                            context,
                            "Failed to update stock" + t.message,
                            Toast.LENGTH_SHORT
                        ).show()
                        Log.d("Update stock action - server", "Failed: " + t.message)
                    }
                })
            }
        } else {
            showDialog()
            stockRepository.update(stock, id)
            recyclerView.adapter?.notifyDataSetChanged()
            Log.d("Update stock action - local database", "Success!")
        }
    }

    fun addStock(stock: Stock) {

        var stockId = stockRepository.add(stock)

        stock.id = stockId

        if (isNetworkConnected()) {
            lifecycleScope.launch {
                StockAPI.retrofitService.createStock(stock).enqueue(object : Callback<Stock?> {
                    override fun onResponse(call: Call<Stock?>, response: Response<Stock?>) {
                        recyclerView.adapter?.notifyDataSetChanged()
                        Log.d("Add stock action - server", "Success: " + response.body().toString())
                    }

                    override fun onFailure(call: Call<Stock?>, t: Throwable) {
                        Toast.makeText(
                            context,
                            "Failed to add stock" + t.message,
                            Toast.LENGTH_SHORT
                        ).show()
                        Log.d("Add stock action - server", "Failed: " + t.message)
                    }
                })
            }
        } else {
            showDialog()
            recyclerView.adapter?.notifyDataSetChanged()
            Log.d("Add stock action - local database", "Success!")
        }
    }
}