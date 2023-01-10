package com.example.lab5.database

import android.provider.BaseColumns

class TableEntry
private constructor(){
    object Entry : BaseColumns {
        const val TABLE_NAME = "Stock"
        const val COLUMN_NAME_ID = "id"
        const val COLUMN_NAME_SYMBOL = "symbol"
        const val COLUMN_NAME_DESC = "description"
        const val COLUMN_NAME_BUY = "buy_price"
        const val COLUMN_NAME_SELL = "sell_price"
    }
}
