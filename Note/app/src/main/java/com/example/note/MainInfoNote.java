package com.example.note;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.PopupMenu;
import android.widget.Toast;

import com.example.note.database.NoteDatabase;
import com.example.note.model.Note;

import java.util.Calendar;

public class MainInfoNote extends AppCompatActivity implements View.OnClickListener {
    private ImageView img_back, img_save, img_delete, img_orange, img_red, img_blue, img_green, img_purple, img_yellow;
    private EditText edt_title, edt_content;
    private ConstraintLayout contentLayout, toolbarInfo;
    int color = R.color.orange;
    int idDatabase, idList;             //Thứ tự trong database ,trong danh sách

    public static final String KEY_ID = "id";
    public static final String KEY_TITLE = "title";
    public static final String KEY_CONTENT = "content";
    public static final String KEY_TIME_CREATE = "timeCreate";
    public static final String KEY_COLOR = "color";
    public static final String ID_LIST = "idList";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_info);

        ActionBar actionBar = getSupportActionBar();
        actionBar.hide();                                   //Ẩn thanh ActionBar

        getInit();
        getDataFromMainActivity();

    }

    /**
     * Ánh xạ
     */
    public void getInit() {
        img_back = (ImageView) findViewById(R.id.img_back);
        img_save = (ImageView) findViewById(R.id.img_save);
        img_delete = (ImageView) findViewById(R.id.img_delete);
        edt_title = (EditText) findViewById(R.id.edt_title);
        edt_content = (EditText) findViewById(R.id.edt_content);
        contentLayout = (ConstraintLayout) findViewById(R.id.contentLayout);
        toolbarInfo = (ConstraintLayout) findViewById(R.id.toolbarInfo);
        img_orange = (ImageView) findViewById(R.id.img_orange);
        img_red = (ImageView) findViewById(R.id.img_red);
        img_blue = (ImageView) findViewById(R.id.img_blue);
        img_green = (ImageView) findViewById(R.id.img_green);
        img_purple = (ImageView) findViewById(R.id.img_purple);
        img_yellow = (ImageView) findViewById(R.id.img_yellow);

        img_back.setOnClickListener(this);
        img_save.setOnClickListener(this);
        img_delete.setOnClickListener(this);
        edt_title.setOnClickListener(this);
        edt_content.setOnClickListener(this);
        contentLayout.setOnClickListener(this);
        toolbarInfo.setOnClickListener(this);
        img_orange.setOnClickListener(this);
        img_red.setOnClickListener(this);
        img_blue.setOnClickListener(this);
        img_green.setOnClickListener(this);
        img_purple.setOnClickListener(this);
        img_yellow.setOnClickListener(this);
    }

    /**
     * Chuyển đổi dữ liệu khi chuyển từ màn hình Activity sang màn hình MainInfoNote
     */
    public void getDataFromMainActivity() {
        Intent intent = getIntent();
        Bundle bundle = intent.getExtras();
        if (bundle != null) {
            idList = bundle.getInt(ID_LIST);                //Thứ tự trong danh sách
            idDatabase = bundle.getInt(KEY_ID);                     //Thứ tự trong database
            edt_title.setText(bundle.getString(KEY_TITLE));
            edt_content.setText(bundle.getString(KEY_CONTENT));
            color = bundle.getInt(KEY_COLOR);
            toolbarInfo.setBackground(getDrawable(color));
            contentLayout.setBackground(getDrawable(color));
        }
    }

    /**
     * Bắt sự kiện cho các view
     *
     * @param v
     */
    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.img_back:
                changeActivity();
                break;
            case R.id.img_save:
                saveNote();
                break;
            case R.id.img_delete:
                deleteNote();
                break;
            case R.id.img_orange:
                color = R.color.orange;
                changeColorBackground();
                break;
            case R.id.img_red:
                color = R.color.red;
                changeColorBackground();
                break;
            case R.id.img_blue:
                color = R.color.blue;
                changeColorBackground();
                break;
            case R.id.img_green:
                color = R.color.green;
                changeColorBackground();
                break;
            case R.id.img_purple:
                color = R.color.purple;
                changeColorBackground();
                break;
            case R.id.img_yellow:
                color = R.color.yellow;
                changeColorBackground();
                break;
            default:
                return;
        }
    }

    /**
     * Lưu thay đổi nội dung của 1 ghi chú có sẵn trong database
     */
    public void saveNote() {
        String title, content, timeCreate;

        if (TextUtils.isEmpty(edt_title.getText().toString()))
            title = "Tiêu đề";
        else
            title = edt_title.getText().toString();
        content = edt_content.getText().toString();
        timeCreate = getTimeCreate();

        Note note = new Note(idDatabase, title, content, timeCreate, color);
        MainActivity.updateNote(note);
        startActivity(new Intent(MainInfoNote.this, MainActivity.class));
        Toast.makeText(MainInfoNote.this, R.string.message2, Toast.LENGTH_SHORT).show();
    }

    /**
     * Thay đổi màu nền khi người dùng chọn màu
     */
    public void changeColorBackground() {
        toolbarInfo.setBackground(getDrawable(color));
        contentLayout.setBackground(getDrawable(color));
    }

    /**
     * Xóa ghi chú theo yêu cầu
     */
    public void deleteNote() {
        AlertDialog.Builder dialog = new AlertDialog.Builder(MainInfoNote.this);
        dialog.setTitle(R.string.title1);
        dialog.setIcon(R.drawable.notification);
        dialog.setMessage(R.string.message7);
        dialog.setPositiveButton("Có", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                MainActivity.deleteNote(idList, idDatabase);
                changeActivity();
                Toast.makeText(MainInfoNote.this, R.string.message3, Toast.LENGTH_SHORT).show();
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
        changeActivity();
    }

    /**
     * Lấy thời gian hiện tại của ứng dụng
     *
     * @return
     */
    public String getTimeCreate() {
        Calendar calendar = Calendar.getInstance();
        int day = calendar.get(Calendar.DAY_OF_MONTH);
        int month = calendar.get(Calendar.MONTH);
        int year = calendar.get(Calendar.YEAR);
        return String.valueOf(day) + "/" + String.valueOf(month+1) + "/" + String.valueOf(year);
    }

    /**
     * Xử lý thay đổi màn hình
     */
    public void changeActivity() {
        startActivity(new Intent(MainInfoNote.this, MainActivity.class));
        overridePendingTransition(R.anim.enter_from_right, R.anim.exit_out_right);
    }
}