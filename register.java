package com.example.myapplication;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.SignInMethodQueryResult;
import com.google.firebase.database.FirebaseDatabase;

public class register extends AppCompatActivity implements View.OnClickListener{
    private EditText editTextname, editTextsname, editTextmail, editTextpw, editTextbakiye;
    private TextView reghome, register2;

    private FirebaseAuth mAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register2);
        mAuth = FirebaseAuth.getInstance();

        reghome = (Button) findViewById(R.id.reghome);
        reghome.setOnClickListener(this);

        register2 = (Button) findViewById(R.id.register2);
        register2.setOnClickListener(this);

        editTextname  = (EditText) findViewById(R.id.registername);
        editTextsname  = (EditText) findViewById(R.id.registersname);
        editTextmail  = (EditText) findViewById(R.id.registermail);
        editTextpw  = (EditText) findViewById(R.id.registerpw);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.reghome:
                startActivity(new Intent(this, MainActivity.class));
                break;
            case R.id.register2:
                register2();
                break;
        }

    }
    public void register2() {
        String name = editTextname.getText().toString().trim();
        String sname = editTextsname.getText().toString().trim();
        String mail = editTextmail.getText().toString().trim();
        String pw = editTextpw.getText().toString().trim();

        if (name.isEmpty()) {
            editTextname.setError("Lütfen isminizi girin");
            editTextname.requestFocus();
            return;
        }
        if (name.isEmpty()) {
            editTextsname.setError("Lütfen soy isminizi girin");
            editTextsname.requestFocus();
            return;
        }
        if (name.isEmpty()) {
            editTextmail.setError("Lütfen e-posta adresinizi girin");
            editTextname.requestFocus();
            return;
        }
        if (name.isEmpty()) {
            editTextpw.setError("Lütfen bir şifre belirleyin");
            editTextpw.requestFocus();
            return;
        }
        if (pw.length() < 6) {
            editTextpw.setError("Şifreniz en az 6 haneli olmalıdır.");
            editTextpw.requestFocus();
        }

        mAuth.createUserWithEmailAndPassword(mail,pw)
                .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if(task.isSuccessful()) {
                            user user = new user(name,sname,mail,pw);

                            FirebaseDatabase.getInstance().getReference("users")
                                    .child(FirebaseAuth.getInstance().getCurrentUser().getUid())
                                    .setValue(user).addOnCompleteListener(new OnCompleteListener<Void>() {
                                @Override
                                public void onComplete(@NonNull Task<Void> task) {
                                    if (task.isSuccessful()) {
                                        Toast.makeText(register.this, "Başarıyla kayıt oldunuz!", Toast.LENGTH_LONG).show();
                                    }else {
                                        Toast.makeText(register.this,"Kayıt olurken bir hata oldu!",Toast.LENGTH_LONG).show();
                                    }
                                }
                            });
                        }else {
                            mAuth.fetchSignInMethodsForEmail(mail.toString())
                                    .addOnCompleteListener(new OnCompleteListener<SignInMethodQueryResult>() {
                                        @Override
                                        public void onComplete(@NonNull Task<SignInMethodQueryResult> task) {
                                            boolean check= !task.getResult().getSignInMethods().isEmpty();
                                            if (check) {
                                                Toast.makeText(register.this,"Bu e-posta adresi zaten kayıtlı!",Toast.LENGTH_LONG).show();
                                        }
                                    }

                            });
                        }
                    }
                });
    }
}