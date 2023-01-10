package com.example.lab5.adapter

import android.app.Dialog
import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.lab5.R
import com.example.lab5.data.ListViewActivity
import com.example.lab5.database.DatabaseRepository
import com.example.lab5.database.Utils
import com.example.lab5.service.UpdateStockActivity

import kotlinx.android.synthetic.main.list_item.view.*


class ItemAdapter(private val context: ListViewActivity): RecyclerView.Adapter<ItemAdapter.ItemViewHolder>() {

    var dbHelper = Utils(this.context)
    var stockRepository = DatabaseRepository.getInstance(dbHelper.readableDatabase, dbHelper.writableDatabase)!!


    inner class ItemViewHolder(view: View): RecyclerView.ViewHolder(view)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ItemViewHolder {
        val adapterLayout = LayoutInflater.from(parent.context).inflate(R.layout.list_item, parent, false)
        return ItemViewHolder(adapterLayout)
    }

    override fun onBindViewHolder(holder: ItemViewHolder, position: Int) {

        val item = stockRepository.getStories()[position]
        holder.itemView.apply {
            symbolID.text = item.symbol;
            buy_priceID.text = item.buy_price.toString();
            sell_priceID.text = item.sell_price.toString();
        }


        holder.itemView.deleteButton.setOnClickListener{

            val dialog = Dialog(context)
            dialog.setCancelable(true)
            dialog.setContentView(R.layout.delete_popup)

            val symbolLabel = dialog.findViewById(R.id.symbolLabel) as TextView

            var stockSymbol = stockRepository.getStories()[position].symbol
            stockSymbol += " ?"

            symbolLabel.text = stockSymbol

            val yesView = dialog.findViewById(R.id.yesButton) as View

            val noView = dialog.findViewById(R.id.noButton) as View

            yesView.setOnClickListener {
                context.deleteStock(item.id!!)
                dialog.dismiss()
            }

            noView.setOnClickListener {
                dialog.dismiss()
            }

            dialog.show()
        }

        holder.itemView.editButton.setOnClickListener(){
            val bundle = Bundle();
            val intent = Intent(context, UpdateStockActivity::class.java)

            bundle.putParcelable("stock", stockRepository.getStories()[position]);
            intent.putExtra("stockBundle", bundle);
            intent.putExtra("id", stockRepository.getStories()[position].id);

            context.startActivityForResult(intent, 5)
        }

    }


    override fun getItemCount(): Int {
        return stockRepository.getStories().size
    }

}