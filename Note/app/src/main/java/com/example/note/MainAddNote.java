package com.example.note;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;

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

import com.example.note.model.Note;

import java.util.Calendar;

public class MainAddNote extends AppCompatActivity implements View.OnClickListener {
    private ImageView img_back, img_save, img_delete, img_orange, img_red, img_blue, img_green, img_purple, img_yellow;
    private EditText edt_title, edt_content;
    private ConstraintLayout contentLayout, toolbarInfo;
    int color = R.color.orange;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_info);

        ActionBar actionBar = getSupportActionBar();
        actionBar.hide();                                                       //Ẩn thanh ActionBar

        getInit();
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
     * Bắt sự kiện cho các view
     *
     * @param v
     */
    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.img_back:
                ChangeActivity();
                break;
            case R.id.img_save:
                saveNote();
                break;
            case R.id.img_delete:
                startActivity(new Intent(MainAddNote.this, MainActivity.class));
                overridePendingTransition(R.anim.enter_from_right, R.anim.exit_out_right);
                break;
            case R.id.img_orange:
                color = R.color.orange;
                changeColorBackground();
                break;
            case R.id.img_green:
                color = R.color.green;
                changeColorBackground();
                break;
            case R.id.img_blue:
                color = R.color.blue;
                changeColorBackground();
                break;
            case R.id.img_purple:
                color = R.color.purple;
                changeColorBackground();
                break;
            case R.id.img_red:
                color = R.color.red;
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
     * Lưu ghi chú vào database và danh sách
     */
    public void saveNote() {
        Integer id = null;
        String title, content, timeCreate;

        if (TextUtils.isEmpty(edt_title.getText().toString().trim()))
            title = "Tiêu đề";
        else
            title = edt_title.getText().toString().trim();
        content = edt_content.getText().toString();
        timeCreate = getTimeCreate();

        Note note = new Note(id, title, content, timeCreate, color);
        MainActivity.addNote(note);
        Toast.makeText(MainAddNote.this, R.string.message2, Toast.LENGTH_SHORT).show();
        startActivity(new Intent(MainAddNote.this, MainActivity.class));
    }

    /**
     * Thay đổi màu nền khi người dùng chọn màu
     */
    public void changeColorBackground() {
        toolbarInfo.setBackground(getDrawable(color));
        contentLayout.setBackground(getDrawable(color));
    }

    /**
     * Xử lý nút back trên điện thoại
     */
    @Override
    public void onBackPressed() {
        ChangeActivity();
    }

    /**
     * Xử lý khi thay đổi màn hình
     */
    public void ChangeActivity() {
        AlertDialog.Builder dialog = new AlertDialog.Builder(MainAddNote.this);
        dialog.setIcon(R.drawable.notification);
        dialog.setTitle(R.string.title1);
        dialog.setMessage(R.string.message1);
        dialog.setPositiveButton("Có", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                saveNote();
            }
        });
        dialog.setNegativeButton("Không", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                startActivity(new Intent(MainAddNote.this, MainActivity.class));
                overridePendingTransition(R.anim.enter_from_right, R.anim.exit_out_right);
            }
        });
        dialog.show();
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
}