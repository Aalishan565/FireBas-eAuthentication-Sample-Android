package com.firebaseauthenticationpoc;

import android.app.ProgressDialog;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {
    private EditText mEtUserName;
    private EditText mEtPassword;
    private Button mBtnSingUp;
    private Button mBtnLogin;
    private TextView mTvAlreadyRegisterd;
    private String TAG = "MainActivity";
    private ProgressDialog progressDialog;
    private FirebaseAuth firebaseAuth;
    String username = "";
    String password = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        firebaseAuth = FirebaseAuth.getInstance();
        progressDialog = new ProgressDialog(this);

        initUI();
    }

    private void initUI() {

        mEtUserName = (EditText) findViewById(R.id.et_username);
        mEtPassword = (EditText) findViewById(R.id.et_password);
        mBtnSingUp = (Button) findViewById(R.id.btn_register);
        mBtnLogin = (Button) findViewById(R.id.btn_login);
        mTvAlreadyRegisterd = (TextView) findViewById(R.id.tv_already_registered);
        mBtnSingUp.setOnClickListener(this);
        mBtnLogin.setOnClickListener(this);
        mTvAlreadyRegisterd.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {

        switch (v.getId()) {
            case R.id.btn_register:
                username = mEtUserName.getText().toString().trim();
                password = mEtPassword.getText().toString().trim();
                registerUser(username, password);

                break;
            case R.id.tv_already_registered:
                mBtnSingUp.setVisibility(View.GONE);
                mBtnLogin.setVisibility(View.VISIBLE);
                break;
            case R.id.btn_login:
                username = mEtUserName.getText().toString().trim();
                password = mEtPassword.getText().toString().trim();
                progressDialog.setTitle("FireBase Login");
                progressDialog.setMessage("Please wait..");
                progressDialog.show();
                firebaseAuth.signInWithEmailAndPassword(username, password).addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(Task<AuthResult> task) {
                        progressDialog.dismiss();
                        if (!task.isSuccessful()) {
                            Toast.makeText(MainActivity.this, "sign In With Email:failed",
                                    Toast.LENGTH_SHORT).show();
                        } else {
                            String email =firebaseAuth.getCurrentUser().getEmail();
                            Toast.makeText(MainActivity.this, "sign In successfully  "+email,
                                    Toast.LENGTH_SHORT).show();
                        }

                    }
                });

                break;
        }
    }

    private void registerUser(String username, String password) {
        if (TextUtils.isEmpty(username)) {
            Toast.makeText(this, "user name should not be empty", Toast.LENGTH_SHORT).show();
            return;

        }
        if (TextUtils.isEmpty(password)) {
            Toast.makeText(this, "user password should not be empty", Toast.LENGTH_SHORT).show();
            return;
        }
        progressDialog.setTitle("FireBase Registration");
        progressDialog.setMessage("Please wait..");
        progressDialog.show();
        firebaseAuth.createUserWithEmailAndPassword(username, password).addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(Task<AuthResult> task) {
                progressDialog.dismiss();
                if (task.isSuccessful()) {
                    Toast.makeText(MainActivity.this, "Successfully registered", Toast.LENGTH_SHORT).show();

                } else
                    Toast.makeText(MainActivity.this, "Some thing went wrong..", Toast.LENGTH_SHORT).show();


            }
        });
    }


}
