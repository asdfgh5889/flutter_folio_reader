package uz.raisense.flutter_folio_reader

import android.content.ContentValues
import android.content.Context
import android.content.pm.PackageManager
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper


class ReadLocatorContract(context: Context): SQLiteOpenHelper(context, "read_state", null, 1) {
    val TABLE_NAME = "locator"
    val COLUMN_ID = "book_id"
    val COLUMN_LOCATOR = "locator"

    private val SQL_CREATE_ENTRIES =
            "CREATE TABLE $TABLE_NAME ($COLUMN_ID TEXT PRIMARY KEY, $COLUMN_LOCATOR TEXT)"

    private val SQL_DELETE_ENTRIES = "DROP TABLE IF EXISTS $TABLE_NAME"

    override fun onCreate(db: SQLiteDatabase?) {
        db?.execSQL(SQL_CREATE_ENTRIES)
    }

    override fun onUpgrade(db: SQLiteDatabase?, p1: Int, p2: Int) {
        db?.execSQL(SQL_DELETE_ENTRIES)
        onCreate(db)
    }

    fun saveState(bookId: String, locator: String) {
        val db = this.readableDatabase
        val values = ContentValues().apply {
            put(COLUMN_ID, bookId)
            put(COLUMN_LOCATOR, locator)
        }
        db.insertWithOnConflict(TABLE_NAME, null, values, SQLiteDatabase.CONFLICT_REPLACE)
    }

    fun readState(bookId: String): String? {
        val db = this.readableDatabase
        val projection = arrayOf(COLUMN_ID, COLUMN_LOCATOR)
        val selection = "$COLUMN_ID = $bookId"
        val selectionArg = arrayOf("*")

        val cursor = db.query(
                TABLE_NAME,
                projection,
                selection,
                null,
                null,
                null,
                null
        )

        var result: String? = null
        with(cursor) {
            if (moveToNext())
                result = getString(getColumnIndex(COLUMN_LOCATOR))
            close()
        }
        return result
    }
}