package com.example.note;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.appcompat.widget.SearchView;

import android.app.AlertDialog;
import android.app.Dialog;
import android.app.SearchManager;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;

import com.example.note.adapter.NoteAdapter;
import com.example.note.database.NoteDatabase;
import com.example.note.model.Note;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity implements RecyclerViewClickInterface {

    public static RecyclerView rcv_note_list;
    public static NoteAdapter noteAdapter;
    public static NoteDatabase noteDatabase;
    public static ArrayList<Note> noteList;
    private SearchView searchView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        setActionBar();

        rcv_note_list = (RecyclerView) findViewById(R.id.rcv_note_list);  //ánh xạ
        noteDatabase = new NoteDatabase(this);
        noteList = new ArrayList<>();
        setNoteList();
    }

    /**
     * Thiết lập ActionBar
     */
    public void setActionBar() {
        ActionBar actionBar = getSupportActionBar();
        actionBar.setLogo(R.drawable.note);
        actionBar.setTitle(R.string.app_name);
        actionBar.setDisplayShowHomeEnabled(true);
        actionBar.setDisplayUseLogoEnabled(true);
    }

    /**
     * Khởi tạo danh sách các ghi chú khi khởi động ứng dụng
     * Nếu database rỗng thì danh sách rỗng
     * Nếu database chứa dữ liệu thì danh sách các ghi chú sẽ hiện lên
     */
    public void setNoteList() {
        rcv_note_list.setLayoutManager(new LinearLayoutManager(this));
        noteList = noteDatabase.getAllNote();
        noteAdapter = new NoteAdapter(noteList, this);
        rcv_note_list.setAdapter(noteAdapter);//Thiết lập noteAdapter cho recycleView
        RecyclerView.ItemDecoration itemDecoration = new DividerItemDecoration(this, DividerItemDecoration.VERTICAL);//Tạo đường kẻ
        rcv_note_list.addItemDecoration(itemDecoration);
    }

    /**
     * Xử lý danh sách khi thêm mới một ghi chú
     * @param note
     */
    public static void addNote(Note note) {
        noteDatabase.addNote(note);
        noteList.add(note);
        noteAdapter.notifyItemInserted(noteList.size());
    }

    /**
     * Xử lý danh sách khi cập nhật thay đổi 1 ghi chú
     * @param note
     */
    public static void updateNote(Note note) {
        noteDatabase.updateNote(note);
        noteList = noteDatabase.getAllNote();
        noteAdapter.notifyDataSetChanged();
    }

    /**
     * Xử lý danh sách khi xóa 1 ghi chú
     * @param idList
     * @param idDatabase
     */
    public static void deleteNote(int idList, int idDatabase) {
        noteDatabase.deleteNote(idDatabase);
        noteAdapter.deleteNote(idList);
        noteAdapter.notifyItemRemoved(idList);
    }

    /**
     * Xử lý tìm kiếm thông tin ghi chú trên danh sách ghi chú
     * @param menu
     * @return
     */
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_start, menu);
        MenuItem item = menu.findItem(R.id.search);

        SearchManager searchManager = (SearchManager) getSystemService(Context.SEARCH_SERVICE);
        searchView = (SearchView) menu.findItem(R.id.search).getActionView();
        searchView.setSearchableInfo(searchManager.getSearchableInfo(getComponentName()));
        searchView.setMaxWidth(Integer.MAX_VALUE);

        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                noteAdapter.getFilter().filter(query);
                return false;
            }
            @Override
            public boolean onQueryTextChange(String newText) {
                noteAdapter.getFilter().filter(newText);
                return false;
            }
        });
        return super.onCreateOptionsMenu(menu);
    }

    /**
     * Hiện thị các mục nhỏ cho người dùng lựa chọn thêm
     * @param item
     * @return
     */
    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()) {
            case R.id.add_note:
                startActivity(new Intent(MainActivity.this, MainAddNote.class));
                overridePendingTransition(R.anim.enter_from_left,R.anim.exit_out_left);
                return true;
            case R.id.clear:
                if (noteList.size() == 0) {
                    Toast.makeText(this, R.string.message4, Toast.LENGTH_SHORT).show();
                } else {
                    AlertDialog.Builder dialog = new AlertDialog.Builder(this);
                    dialog.setTitle(R.string.title1);
                    dialog.setIcon(R.drawable.warning);
                    dialog.setMessage(R.string.message5);
                    dialog.setPositiveButton("Có", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            noteDatabase.removeData();
                            noteAdapter.clearData();
                            noteAdapter.notifyDataSetChanged();
                            Toast.makeText(MainActivity.this, R.string.message3, Toast.LENGTH_SHORT).show();
                        }
                    });
                    dialog.setNegativeButton("Không", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            dialog.dismiss();
                        }
                    });
                    dialog.show();
                }
                return true;
            case R.id.info:
                AlertDialog.Builder dialog = new AlertDialog.Builder(MainActivity.this);
                dialog.setTitle(R.string.title2);
                dialog.setIcon(R.drawable.info);
                dialog.setMessage(R.string.message6);
                dialog.show();
                return true;
        }
        return super.onOptionsItemSelected(item);
    }

    /**
     * Xử lý dữ liệu khi người dùng chọn 1 mục trong danh sách
     * @param position
     */
    @Override
    public void OnItemClick(int position) {
        Intent intent = new Intent(MainActivity.this, MainInfoNote.class);
        Bundle bundle = new Bundle();
        bundle.putInt("idList", position);
        bundle.putInt("id", noteList.get(position).getId());
        bundle.putString("title", noteList.get(position).getTitle());
        bundle.putString("content", noteList.get(position).getContent());
        bundle.putString("timeCreate", noteList.get(position).getTimeCreate());
        bundle.putInt("color", noteList.get(position).getColor());
        intent.putExtras(bundle);
        startActivity(intent);
    }

    /**
     * Xử lý dữ liệu khi người dùng giữ lâu 1 mục trong danh sách
     * @param position
     */
    @Override
    public void OnItemLongClick(int position) {
        AlertDialog.Builder dialog = new AlertDialog.Builder(this);
        dialog.setTitle(R.string.title1);
        dialog.setIcon(R.drawable.notification);
        dialog.setMessage(R.string.message7);
        dialog.setPositiveButton("Có", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                deleteNote(position, noteList.get(position).getId());
                Toast.makeText(MainActivity.this, R.string.message3, Toast.LENGTH_SHORT).show();
            }
        });
        dialog.setNegativeButton("Không", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
            }
        });
        dialog.show();
    }

    /**
     * Xử lý nút back trên điện thoại
     */
    @Override
    public void onBackPressed() {
        finishAffinity();
        System.exit(0);
    }
}