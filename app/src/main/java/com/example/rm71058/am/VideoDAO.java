package com.example.rm71058.am;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.database.sqlite.SQLiteStatement;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by rm71058 on 27/10/2016.
 */
public class VideoDAO {
    private static final String DATABASE_NAME = "videos.db";
    private static final int DATABASE_VERSION = 1;
    private static final String TABLE_NAME = "video";

    private Context context;
    private SQLiteDatabase db;

    private SQLiteStatement stmt;
    private static final String INSERT = "INSERT INTO " + TABLE_NAME + " (codigo, tempo, descricao) values (?, ?, ?)";

    public VideoDAO(Context context) {
        this.context = context;
        OpenHelper openHelper = new OpenHelper(this.context);
        this.db = openHelper.getWritableDatabase();
        this.stmt = this.db.compileStatement(INSERT);
    }

    public long insert(int codigo, int tempo, String descricao) {
        this.stmt.bindDouble(1, codigo);
        this.stmt.bindDouble(2, tempo);
        this.stmt.bindString(3, descricao);
        return this.stmt.executeInsert();
    }

    public List<Video> all() {
        List<Video> videos = new ArrayList<>();
        Cursor cursor = this.db.query(
                TABLE_NAME,
                new String[]{"codigo", "tempo", "descricao"},
                null,
                null,
                null,
                null,
                null
        );

        try {
            do {
                Video video = new Video(cursor.getInt(0), cursor.getInt(1), cursor.getString(2));
                videos.add(video);
            } while(cursor.moveToNext());

            return videos;
        } finally {
            if(cursor != null && !cursor.isClosed())
                cursor.close();
        }
    }


    public boolean existRecord() {
        Cursor cursor = this.db.query(
                TABLE_NAME,
                null,
                null,
                null,
                null,
                null,
                null,
                "1"
        );

        try {
            return cursor.moveToNext();
        } finally {
            if(cursor != null && !cursor.isClosed())
                cursor.close();
        }
    }

    public void closeConnection() {
        this.db.close();
    }
    private static class OpenHelper extends SQLiteOpenHelper {
        OpenHelper(Context context) {
            super(context, DATABASE_NAME, null, DATABASE_VERSION);
        }

        @Override
        public void onCreate(SQLiteDatabase db) {
            db.execSQL("CREATE TABLE " + TABLE_NAME + " (codigo integer PRIMAY KEY, tempo integer, descricao TEXT ");
        }

        @Override
        public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {}
    }
}
