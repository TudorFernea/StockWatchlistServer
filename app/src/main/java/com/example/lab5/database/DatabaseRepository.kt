package com.example.lab5.database

import android.content.ContentValues
import android.database.sqlite.SQLiteDatabase
import android.util.Log
import com.example.lab5.model.Stock


class DatabaseRepository private constructor(
    private var readableDatabase: SQLiteDatabase,
    private var writableDatabase: SQLiteDatabase
){
    private var stockList: MutableList<Stock> = ArrayList();

    fun getStories(): MutableList<Stock> {
        val data: MutableList<Stock> = ArrayList()
        val cursor = readableDatabase.query(Utils.TABLE_NAME, null, null, null, null, null, null);

        while(cursor.moveToNext()){
            val id = cursor.getLong(cursor.getColumnIndexOrThrow("id"))
            val symbol = cursor.getString(cursor.getColumnIndexOrThrow("symbol"))
            val desc = cursor.getString(cursor.getColumnIndexOrThrow("description"))
            val buy_price = cursor.getDouble(cursor.getColumnIndexOrThrow("buy_price"))
            val sell_price = cursor.getDouble(cursor.getColumnIndexOrThrow("sell_price"))

            val stock = Stock(id, symbol, desc, buy_price, sell_price)

            data.add(stock)
        }

        cursor.close()

        stockList = data

        return stockList.toMutableList()
    }

    fun add(stock: Stock): Long {
        val values = ContentValues()
        values.put(Utils.COL_SYMBOL, stock.symbol)
        values.put(Utils.COL_DESC, stock.desc)
        values.put(Utils.COL_BUY, stock.buy_price)
        values.put(Utils.COL_SELL, stock.sell_price)

        val result = writableDatabase.insert(Utils.TABLE_NAME,null, values)

        if(result == (-1).toLong()){
            val str = "DATABASE -> ADD"
            Log.i(str, "Error occurred when adding an element")
            return -1
        }

        return result
    }

    fun delete(id: Long): Boolean {
        val selection: String = Utils.COL_ID + " = ?"
        val selectionArgs = arrayOf(id.toString())

        val result = writableDatabase.delete(Utils.TABLE_NAME, selection, selectionArgs)

        if(result == -1){
            val str = "DATABASE -> Delete"
            Log.i(str, "Error occurred when deleting an element")
            return false
        }

        return true
    }

    fun update(stock: Stock, id: Long): Stock? {
        val values = ContentValues()
        values.put(Utils.COL_SYMBOL, stock.symbol)
        values.put(Utils.COL_DESC, stock.desc)
        values.put(Utils.COL_BUY, stock.buy_price)
        values.put(Utils.COL_SELL, stock.sell_price)

        val selection: String = Utils.COL_ID + " = ?"
        val selectionArgs = arrayOf(id.toString())

        val result = writableDatabase.update(Utils.TABLE_NAME, values, selection, selectionArgs)

        if(result == -1){
            val str = "DATABASE -> Update"
            Log.i(str, "Error occurred when updating an element")
            return null
        }

        return stock
    }

    fun getById(id: Long): Stock? {
        for(i in stockList.indices){
            if(id == stockList[i].id){
                return stockList[i]
            }
        }
        return null
    }

    companion object{
        private var instance: DatabaseRepository? = null
        fun getInstance(readableDatabase: SQLiteDatabase, writableDatabase: SQLiteDatabase): DatabaseRepository? {
            if(instance == null) instance = DatabaseRepository(readableDatabase, writableDatabase)
            return instance
        }
    }

}