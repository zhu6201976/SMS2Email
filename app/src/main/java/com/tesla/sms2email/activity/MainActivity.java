package com.tesla.sms2email.activity;

import android.Manifest;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.tesla.sms2email.R;
import com.tesla.sms2email.global.Constants;
import com.tesla.sms2email.utils.SmtplUtils;
import com.tesla.sms2email.utils.SpUtils;

import java.util.ArrayList;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;


public class MainActivity extends AppCompatActivity implements View.OnClickListener {
    private EditText et_smtp_server;
    private EditText et_email;
    private EditText et_password;
    private SpUtils spUtils;
    private SmtplUtils smtpUtils;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        initPermission();
        initUi();
        initData();
    }

    private void initPermission() {
        ArrayList<String> permissionList = new ArrayList<>();
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.RECEIVE_SMS) !=
                PackageManager.PERMISSION_GRANTED) {
            permissionList.add(Manifest.permission.RECEIVE_SMS);
        }
        String[] permissions = new String[permissionList.size()];
        permissions = permissionList.toArray(permissions);
        if (permissions.length > 0) {
            ActivityCompat.requestPermissions(this, permissions, 1);
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions,
                                           @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        switch (requestCode) {
            case 1:
                boolean is_exist = exist(grantResults, -1);
                if (is_exist) {
                    Toast.makeText(this, "please allow all permissions",
                            Toast.LENGTH_SHORT).show();
                    finish();
                } else {
                    Toast.makeText(this, "allow all permissions",
                            Toast.LENGTH_SHORT).show();
                }
                break;
        }
    }

    public boolean exist(int[] arr, int key) {
        for (int value : arr) {
            if (value == key) {
                return true;
            }
        }
        return false;
    }

    private void initUi() {
        et_smtp_server = findViewById(R.id.et_smtp_server);
        et_email = findViewById(R.id.et_email);
        et_password = findViewById(R.id.et_password);

        Button bt_save = findViewById(R.id.bt_save);
        Button bt_check = findViewById(R.id.bt_check);
        bt_save.setOnClickListener(this);
        bt_check.setOnClickListener(this);
    }

    private void initData() {
        this.spUtils = new SpUtils(this);
        this.smtpUtils = new SmtplUtils(spUtils);

        String smtp_server = spUtils.getString(Constants.smtpServerKey, "");
        String email = spUtils.getString(Constants.emailKey, "");
        String password = spUtils.getString(Constants.passwordKey, "");

        // Echo User Configuration
        if (!smtp_server.isEmpty() || !email.isEmpty() || !password.isEmpty()) {
            this.et_smtp_server.setText(smtp_server);
            this.et_email.setText(email);
            this.et_password.setText(password);
        }
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.bt_save:
                this.save();
                break;
            case R.id.bt_check:
                this.check();
                break;
        }
    }

    /**
     * Save User Configuration
     */
    private void save() {
        String smtp_server = this.et_smtp_server.getText().toString();
        String email = this.et_email.getText().toString();
        String password = this.et_password.getText().toString();

        if (smtp_server.isEmpty() || email.isEmpty() || password.isEmpty()) {
            Toast.makeText(this, "please input", Toast.LENGTH_SHORT).show();
            return;
        }

        try {
            spUtils.putString(Constants.smtpServerKey, smtp_server);
            spUtils.putString(Constants.emailKey, email);
            spUtils.putString(Constants.passwordKey, password);
            Toast.makeText(this, "Save success", Toast.LENGTH_SHORT).show();
        } catch (Exception e) {
            e.printStackTrace();
            Toast.makeText(this, "Save fail", Toast.LENGTH_SHORT).show();
        }
    }

    private void check() {
        smtpUtils.sendEmail(Constants.TAG, Constants.TAG);
        Toast.makeText(this, "Check finish", Toast.LENGTH_SHORT).show();
    }

}
