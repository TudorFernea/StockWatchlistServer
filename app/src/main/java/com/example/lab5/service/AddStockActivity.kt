package com.example.lab5.service

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.Window
import android.widget.Button
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import com.example.lab5.model.Stock
import com.example.lab5.R
import kotlinx.android.synthetic.main.add_note.*

class AddStockActivity : AppCompatActivity() {

    lateinit var cancelButton: Button;
    lateinit var saveButton: Button;
    lateinit var id: String;


    override fun onCreate(savedInstanceState: Bundle?) {

        val str = "ADD STOCK ACTIVITY"
        Log.i(str, "Entered Add stock activity")

        super.onCreate(savedInstanceState)
        setContentView(R.layout.add_note)

        supportActionBar?.hide()

        val window: Window = this@AddStockActivity.window
        window.statusBarColor = ContextCompat.getColor(this@AddStockActivity, R.color.black)

        saveButton = findViewById(R.id.saveButtonCreate);
        cancelButton = findViewById(R.id.cancelButtonCreate);

        saveButton.setOnClickListener() {
            addStock()
        }

        cancelButton.setOnClickListener() {
            goBack();
        }

    }

    private fun addStock() {
        if (checkInputs()) {
            val stock = Stock(
                0,
                symbolInputCreate.text.toString(),
                descInputCreate.text.toString(),
                buy_priceInputCreate.text.toString().toDoubleOrNull(),
                sell_priceInputCreate.text.toString().toDoubleOrNull(),
            )

            val bundle = Bundle()
            bundle.putParcelable("stock", stock);
            intent.putExtra("stockBundle", bundle)
            setResult(RESULT_OK, intent)

            finish()
        } else {
            Toast.makeText(
                this,
                "Symbol and price fields must be non empty",
                Toast.LENGTH_LONG
            ).show();
        }
    }

    private fun checkInputs(): Boolean {
        if (symbolInputCreate.text.isEmpty() or buy_priceInputCreate.text.isEmpty() or sell_priceInputCreate.text.isEmpty() or descInputCreate.text.isEmpty() or buy_priceInputCreate.text.isEmpty()) {
            return false
        }

        return true
    }

    private fun goBack() {
        intent = Intent()
        finish()
    }
}