package com.example.lab5.service

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.Window
import android.widget.Button
import android.widget.Toast
import androidx.core.content.ContextCompat
import com.example.lab5.R
import com.example.lab5.model.Stock
import kotlinx.android.synthetic.main.edit_note.*

class UpdateStockActivity : AppCompatActivity() {

    lateinit var id: Number;
    private lateinit var initialStock: Stock;
    private lateinit var cancelButton: Button;
    private lateinit var saveButton: Button;

    override fun onCreate(savedInstanceState: Bundle?) {

        val str = "UPDATE STOCK ACTIVITY"
        Log.i(str, "Entered Update stock activity")

        super.onCreate(savedInstanceState)

        setContentView(R.layout.edit_note)

        supportActionBar?.hide()

        val window: Window = this@UpdateStockActivity.window
        window.statusBarColor = ContextCompat.getColor(this@UpdateStockActivity, R.color.black)

        val bundle = intent.getBundleExtra("stockBundle")
        if(bundle != null) {
            val stock = bundle.getParcelable<Stock>("stock")
            if (stock != null) {
                initialStock = stock
            }
        }

        id = intent.getLongExtra("id", -1);

        initializeInputs()

        saveButton = saveButtonUpdate
        cancelButton = cancelButtonUpdate

        saveButton.setOnClickListener(){
            editStock()
        }

        cancelButton.setOnClickListener(){
            goBack()
        }

    }

    private fun editStock() {
        if(checkInputs()){

            initialStock.symbol = symbolInputUpdate.text.toString();
            initialStock.desc = descInputUpdate.text.toString()
            initialStock.buy_price = buy_priceInputUpdate.text.toString().toDoubleOrNull()
            initialStock.sell_price = sell_priceInputUpdate.text.toString().toDoubleOrNull()

            val bundle = Bundle()
            bundle.putParcelable("stock", initialStock)
            intent.putExtra("stockBundle", bundle)
            intent.putExtra("id", id)
            setResult(RESULT_OK, intent)
            finish()
        } else
        {
            Toast.makeText(this, "Symbol and price fields must be non empty", Toast.LENGTH_LONG).show();
        }
    }

    private fun goBack(){
        intent = Intent()
        finish()
    }

    private fun checkInputs(): Boolean {
        if(symbolInputUpdate.text.isEmpty() or buy_priceInputUpdate.text.isEmpty() or sell_priceInputUpdate.text.isEmpty() or descInputUpdate.text.isEmpty()){
            return false
        }

        return true
    }

    private fun initializeInputs(){
        symbolInputUpdate.setText(initialStock.symbol)
        descInputUpdate.setText(initialStock.desc)
        buy_priceInputUpdate.setText(initialStock.buy_price.toString())
        sell_priceInputUpdate.setText(initialStock.sell_price.toString())
    }



}