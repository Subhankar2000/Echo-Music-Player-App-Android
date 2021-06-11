package echo.music.player.subhankar.karmakar.databases

import android.content.ContentValues
import android.content.Context
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper
import android.support.v7.widget.DialogTitle
import echo.music.player.subhankar.karmakar.Songs
import java.security.cert.CertPath

/**
 * Created by Subhankar Karmakar on 12-01-2018.
 */

class EchoDatabase : SQLiteOpenHelper {
    var _songList = ArrayList<Songs>()

    object Staticated{
        var DB_VERSION = 1
        val DB_NAME = "FavouriteDatabase"
        val TABLE_NAME = "FavouriteTable"
        val COLOUMN_ID = "SongID"
        val COLOUMN_SONG_TITLE = "SongTitle"
        val COLOUMN_SONG_ARTIST = "SongArtist"
        val COLOUMN_SONG_PATH = "SongPath"

    }

    override fun onCreate(sqlliteDatabase: SQLiteDatabase?) {
        sqlliteDatabase?.execSQL("CREATE TABLE " + Staticated.TABLE_NAME + "( " + Staticated.COLOUMN_ID + " INTEGER," +
                Staticated.COLOUMN_SONG_ARTIST + " STRING," + Staticated.COLOUMN_SONG_TITLE + " STRING," + Staticated.COLOUMN_SONG_PATH + " STRING);"
        )
    }

    override fun onUpgrade(p0: SQLiteDatabase?, p1: Int, p2: Int) {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    constructor(context: Context?, name: String?, factory: SQLiteDatabase.CursorFactory?, version: Int) : super(context, name, factory, version)
    constructor(context: Context?) : super(context, Staticated.DB_NAME, null, Staticated.DB_VERSION)
    fun storeAsFavourite(id: Int?, artist: String?, songTitle: String?, path: String?) {

        var db = this.writableDatabase
        var contentValues = ContentValues()
        contentValues.put(Staticated.COLOUMN_ID, id)
        contentValues.put(Staticated.COLOUMN_SONG_ARTIST, artist)
        contentValues.put(Staticated.COLOUMN_SONG_TITLE, songTitle)
        contentValues.put(Staticated.COLOUMN_SONG_PATH, path)
        db.insert(Staticated.TABLE_NAME, null, contentValues)
        db.close()
    }

    fun querryDBList(): ArrayList<Songs>? {
        try {
            val db = this.readableDatabase
            val querry_params = "SELECT * FROM" + Staticated.TABLE_NAME
            var cSor = db.rawQuery(querry_params, null)
            if (cSor.moveToFirst()) {
                do {
                    var _id = cSor.getInt(cSor.getColumnIndexOrThrow(Staticated.COLOUMN_ID))
                    var _artist = cSor.getString(cSor.getColumnIndexOrThrow(Staticated.COLOUMN_SONG_ARTIST))
                    var _title = cSor.getString(cSor.getColumnIndexOrThrow(Staticated.COLOUMN_SONG_TITLE))
                    var _songPath = cSor.getString(cSor.getColumnIndexOrThrow(Staticated.COLOUMN_SONG_PATH))
                    _songList.add(Songs(_id.toLong(), _title, _artist, _songPath, 0))
                } while (cSor.moveToNext())
            } else {
                return null
            }
        } catch (e: Exception) {
            e.printStackTrace()
        }
        return _songList
    }

    fun checkifIdExixts(_id: Int): Boolean {
        var storeId = -1090
        val db = this.readableDatabase
        val query_Params = "SELECT * FROM " + Staticated.TABLE_NAME + " WHERE SongId = '$_id'"
        val cSor = db.rawQuery(query_Params, null)
        if (cSor.moveToNext()) {
            do {
                storeId = cSor.getInt(cSor.getColumnIndexOrThrow(Staticated.COLOUMN_ID))
            } while (cSor.moveToNext())
        } else {
            return false
        }
        cSor.close()
        return storeId != -1090
    }

    fun deleteFavourite(_id: Int) {
        val db = this.writableDatabase
        db.delete(Staticated.TABLE_NAME, Staticated.COLOUMN_ID + "=" + _id, null)
        db.close()
    }

    fun checkSize(): Int{
        var counter = 0
        val db = this.readableDatabase
        var query_params = "SELECT * FROM " + Staticated.TABLE_NAME
        val cSor = db.rawQuery(query_params , null)
        if(cSor.moveToFirst()){
            do{
                counter = counter + 1
            }while(cSor.moveToNext())
        }else{
            return 0
        }
        return counter
    }
}
