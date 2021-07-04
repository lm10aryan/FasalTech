package com.example.fasaltech;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.FirebaseException;
import com.google.firebase.FirebaseTooManyRequestsException;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseAuthInvalidCredentialsException;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.PhoneAuthCredential;
import com.google.firebase.auth.PhoneAuthOptions;
import com.google.firebase.auth.PhoneAuthProvider;

import org.jetbrains.annotations.NotNull;

import java.util.concurrent.TimeUnit;

public class AuthenticationActivity extends AppCompatActivity {
    String phone_number="";
    private FirebaseAuth mAuth;
    Button button;
    String verificationCodeBySystem;

    private PhoneAuthProvider.OnVerificationStateChangedCallbacks mCallbacks;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_authentication);
        Intent intent=getIntent();
        mAuth = FirebaseAuth.getInstance();
        phone_number=intent.getStringExtra("phone_number");
        button=findViewById(R.id.button3);

        mCallbacks=new PhoneAuthProvider.OnVerificationStateChangedCallbacks() {
            @Override
            public void onCodeSent(@NonNull @NotNull String s, @NonNull @NotNull PhoneAuthProvider.ForceResendingToken forceResendingToken) {
                super.onCodeSent(s, forceResendingToken);
                verificationCodeBySystem=s;
            }

            @Override
            public void onVerificationCompleted(@NonNull @NotNull PhoneAuthCredential phoneAuthCredential) {
                String code=phoneAuthCredential.getSmsCode();
                verifyCode("223394");
                //if(code!=null){
                  //  verifyCode(code);
                //}

            }

            @Override
            public void onVerificationFailed(@NonNull @NotNull FirebaseException e) {
                Log.i("Error verify",e.getMessage());

            }
        };
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try {
                    sendVerificationCodeToUser(phone_number);

                }catch (Exception e){
                    Log.i("Error",e.getLocalizedMessage());
                }
            }
        });
    }
    private void sendVerificationCodeToUser(String phone_number){
        PhoneAuthOptions options =
                PhoneAuthOptions.newBuilder(mAuth)
                        .setPhoneNumber("+91 "+phone_number)       // Phone number to verify
                        .setTimeout(60L, TimeUnit.SECONDS) // Timeout and unit
                        .setActivity(this)                 // Activity (for callback binding)
                        .setCallbacks(mCallbacks)          // OnVerificationStateChangedCallbacks
                        .build();
        PhoneAuthProvider.verifyPhoneNumber(options);
    }
    private void verifyCode(String codeByUser){
        PhoneAuthCredential phoneAuthCredential=PhoneAuthProvider.getCredential(verificationCodeBySystem,codeByUser);
        signInByCredential(phoneAuthCredential);
    }
    private void signInByCredential(PhoneAuthCredential credential){
        FirebaseAuth firebaseAuth=FirebaseAuth.getInstance();
        firebaseAuth.signInWithCredential(credential).addOnCompleteListener(AuthenticationActivity.this, new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull @NotNull Task<AuthResult> task) {
                if(task.isSuccessful()){
                    Log.i("User","OTP Checked");
                }else{
                    Log.i("Error task",task.getException().getMessage());
                }

            }
        });
    }
}
