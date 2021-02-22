package com.example.note.database;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import androidx.annotation.Nullable;

import com.example.note.MainActivity;
import com.example.note.model.Note;

import java.util.ArrayList;

public class NoteDatabase extends SQLiteOpenHelper {
    public static final String DATABASE_NAME = "NoteManager";
    public static final int DATABASE_VERSION = 1;
    public static final String TABLE_NAME = "Note";

    public static final String KEY_ID = "id";
    public static final String KEY_TITLE = "title";
    public static final String KEY_CONTENT = "content";
    public static final String KEY_TIME_CREATE = "timeCreate";
    public static final String KEY_COLOR = "color";

    /**
     * Hàm tạo Database
     *
     * @param context
     */
    public NoteDatabase(@Nullable Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    /**
     * Hàm tạo bảng Note
     *
     * @param db
     */
    @Override
    public void onCreate(SQLiteDatabase db) {
        String createTableSql = String.format("Create table %s(%s Integer primary key AutoIncrement,%s varchar(200),%s varchar(200),%s varchar(200),%s Integer)",
                TABLE_NAME, KEY_ID, KEY_TITLE, KEY_CONTENT, KEY_TIME_CREATE, KEY_COLOR);
        db.execSQL(createTableSql);
    }

    /**
     * Tự động cập nhật bảng dữ liệu khi có phiên bản mới
     *
     * @param db
     * @param oldVersion
     * @param newVersion
     */
    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        String dropTableSql = String.format("Drop table if exists %s", TABLE_NAME);
        db.execSQL(dropTableSql);
        onCreate(db);
    }

    /**
     * Thêm ghi chú vào database
     *
     * @param note
     */
    public void addNote(Note note) {
        SQLiteDatabase database = this.getWritableDatabase();
        ContentValues values = new ContentValues();

        values.put(KEY_ID, note.getId());
        values.put(KEY_TITLE, note.getTitle());
        values.put(KEY_CONTENT, note.getContent());
        values.put(KEY_TIME_CREATE, note.getTimeCreate());
        values.put(KEY_COLOR, note.getColor());

        database.insert(TABLE_NAME, null, values);
        database.close();
    }

    /**
     * Trả về 1 ghi chú khi biết id
     *
     * @param id
     * @return
     */
    public Note getNote(int id) {
        SQLiteDatabase database = this.getReadableDatabase();
        Cursor cursor = database.query(TABLE_NAME, null, KEY_ID + "=?", new String[]{String.valueOf(id)}, null, null, null);
        if (cursor != null)
            cursor.moveToNext();
        Note note = new Note(cursor.getInt(0), cursor.getString(1), cursor.getString(2), cursor.getString(3), cursor.getInt(4));
        return note;
    }

    /**
     * Trả về 1 danh sách các ghi chú
     *
     * @return
     */
    public ArrayList<Note> getAllNote() {
        ArrayList<Note> noteList = new ArrayList<>();
        String query = "Select * from " + TABLE_NAME;

        SQLiteDatabase database = this.getWritableDatabase();
        Cursor cursor = database.rawQuery(query, null);
        while (cursor.moveToNext()) {
            Note note = new Note(cursor.getInt(0), cursor.getString(1), cursor.getString(2), cursor.getString(3), cursor.getInt(4));
            noteList.add(note);
        }
        return noteList;
    }

    /**
     * Cập nhật ghi chú khi biết id
     *
     * @param note
     */
    public void updateNote(Note note) {
        SQLiteDatabase database = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(KEY_TITLE, note.getTitle());
        values.put(KEY_CONTENT, note.getContent());
        values.put(KEY_TIME_CREATE, note.getTimeCreate());
        values.put(KEY_COLOR, note.getColor());

        database.update(TABLE_NAME, values, KEY_ID + "=?", new String[]{String.valueOf(note.getId())});
        database.close();
    }

    /**
     * Xóa ghi chú khi biết id
     *
     * @param id
     */
    public void deleteNote(int id) {
        SQLiteDatabase database = this.getWritableDatabase();
        database.delete(TABLE_NAME, KEY_ID + "=?", new String[]{String.valueOf(id)});
        database.close();
    }

    /**
     * Xóa toàn bộ dữ liệu ghi chú
     */
    public void removeData() {
        SQLiteDatabase database = this.getWritableDatabase();
        String query = "Delete from " + TABLE_NAME;
        database.execSQL(query);
    }
}
