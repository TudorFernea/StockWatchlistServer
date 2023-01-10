package com.example.lab5.database


import android.content.Context
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper


class Utils(context: Context?) :
    SQLiteOpenHelper(
        context,
        DATABASE_NAME,
        null,
        DATABASE_VERSION
    ) {

    override fun onCreate(db: SQLiteDatabase) {
        db.execSQL(SQL_CREATE_ENTRIES)
    }

    override fun onUpgrade(
        db: SQLiteDatabase,
        oldVersion: Int,
        newVersion: Int
    ) {

        db.execSQL(SQL_DELETE_ENTRIES)
        onCreate(db)
    }

    override fun onDowngrade(
        db: SQLiteDatabase,
        oldVersion: Int,
        newVersion: Int
    ) {
        onUpgrade(db, oldVersion, newVersion)
    }

    companion object {
        // If you change the database schema, you must increment the database version.
        const val DATABASE_VERSION = 1
        const val DATABASE_NAME = "StocksDbs.db"
        const val TABLE_NAME = "Stock"
        const val COL_ID = "id"
        const val COL_SYMBOL = "symbol"
        const val COL_DESC = "description"
        const val COL_BUY = "buy_price"
        const val COL_SELL = "sell_price"

        private const val SQL_CREATE_ENTRIES =
            "CREATE TABLE " + TABLE_NAME + " (" +
                    COL_ID + " INTEGER PRIMARY KEY AUTOINCREMENT ," +
                    COL_SYMBOL + " TEXT, " +
                    COL_DESC + " TEXT, " +
                    COL_BUY + " TEXT, " +
                    COL_SELL + " TEXT)"

        private const val SQL_DELETE_ENTRIES =
            "DROP TABLE IF EXISTS " + TABLE_NAME
    }
}