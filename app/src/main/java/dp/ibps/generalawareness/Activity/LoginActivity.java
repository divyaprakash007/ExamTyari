package dp.ibps.generalawareness.Activity;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;
import com.google.api.LogDescriptor;
import com.google.firebase.FirebaseException;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseAuthInvalidCredentialsException;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.PhoneAuthCredential;
import com.google.firebase.auth.PhoneAuthOptions;
import com.google.firebase.auth.PhoneAuthProvider;

import java.util.concurrent.TimeUnit;

import dp.ibps.generalawareness.AppUtils.AppPrefs;
import dp.ibps.generalawareness.R;

public class LoginActivity extends AppCompatActivity {
    private FirebaseAuth mAuth;
    private TextInputLayout mobile_til,otp_til;
    private TextInputEditText mobile_et, otp_et;
    private Button otp_login_button;
    private TextView privacy_policy_tv, about_us_tv;
    private String mobile_number = "";
    private String otp_number = "";
    private PhoneAuthCredential credential;
    private String verificationId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        initialise();

        otp_login_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mobile_number.equalsIgnoreCase("")){
                    if (mobile_et.getText().toString().length()<10 || !android.util.Patterns.PHONE.matcher(mobile_et.getText().toString()).matches()){
                        Toast.makeText(LoginActivity.this, "Please enter a valid mobile number.", Toast.LENGTH_LONG).show();
                        otp_login_button.setText("Send OTP");
                        mobile_til.setEnabled(true);
                        otp_til.setEnabled(false);
                    } else {
                        mobile_number = mobile_et.getText().toString();
                        otp_login_button.setText("Verify & Login");
                        mobile_til.setEnabled(false);
                        otp_til.setEnabled(true);
                        Toast.makeText(LoginActivity.this, "" + mobile_number, Toast.LENGTH_SHORT).show();
                        // TODO: 14-02-2021 send otp to user using firebase.
                        sendOtpToMobile(mobile_number);
                    }
                } else {
                    if (otp_number.equalsIgnoreCase("")){
                        if (otp_et.getText().length()<6){
                            Toast.makeText(LoginActivity.this, "Please enter a valid OTP.", Toast.LENGTH_SHORT).show();
                        } else {
                            otp_number = otp_et.getText().toString();
                            Toast.makeText(LoginActivity.this, "" +otp_number, Toast.LENGTH_SHORT).show();
                            credential = PhoneAuthProvider.getCredential(verificationId, otp_number);
                            signInWithPhoneAuthCredential(credential);
                        }
                    }
                }
            }
        });


    }

    private void signInWithPhoneAuthCredential(PhoneAuthCredential credential) {
        mAuth.signInWithCredential(credential)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            // Sign in success, update UI with the signed-in user's information
                            Log.d("TAG", "signInWithCredential:success");

                            FirebaseUser user = task.getResult().getUser();
                            Log.d("TAG", "onComplete: User Name " + user.getDisplayName() +
                                   "User email " + user.getEmail() + " Phone num "+ user.getPhoneNumber()
                            +" Provider ID " + user.getProviderId() + " Photo " + user.getPhotoUrl());
                            // ...
                        } else {
                            // Sign in failed, display a message and update the UI
                            Log.w("TAG", "signInWithCredential:failure", task.getException());
                            Toast.makeText(LoginActivity.this, ""+task.getException().getMessage(), Toast.LENGTH_SHORT).show();
                            if (task.getException() instanceof FirebaseAuthInvalidCredentialsException) {
                                Toast.makeText(LoginActivity.this, "Login Fail. Invalid OTP", Toast.LENGTH_SHORT).show();
                            }
                        }
                    }
                });
    }

    private void sendOtpToMobile(String mobile_number) {
        PhoneAuthOptions options =
                PhoneAuthOptions.newBuilder(mAuth)
                        .setPhoneNumber(mobile_number)       // Phone number to verify
                        .setTimeout(60L, TimeUnit.SECONDS) // Timeout and unit
                        .setActivity(this)                 // Activity (for callback binding)
                        .setCallbacks(new PhoneAuthProvider.OnVerificationStateChangedCallbacks() {
                            @Override
                            public void onVerificationCompleted(@NonNull PhoneAuthCredential phoneAuthCredential) {
                                AppPrefs.setMobile(LoginActivity.this,mobile_number);
                                startActivity(new Intent(LoginActivity.this,UpdateProfileActivity.class));
                                finish();
                            }

                            @Override
                            public void onVerificationFailed(@NonNull FirebaseException e) {
                                Toast.makeText(LoginActivity.this, ""+ e.getMessage(), Toast.LENGTH_SHORT).show();
                            }

                            @Override
                            public void onCodeSent(@NonNull String s, @NonNull PhoneAuthProvider.ForceResendingToken forceResendingToken) {
                                verificationId = s;
                            }

                        })
                        .build();
        PhoneAuthProvider.verifyPhoneNumber(options);
    }

    private void initialise() {
        mobile_til = findViewById(R.id.mobile_til);
        otp_til = findViewById(R.id.otp_til);
        mobile_et = findViewById(R.id.mobile_et);
        otp_et = findViewById(R.id.otp_et);
        otp_login_button = findViewById(R.id.otp_login_button);
        privacy_policy_tv = findViewById(R.id.privacy_policy_tv);
        about_us_tv = findViewById(R.id.about_us_tv);
        mAuth = FirebaseAuth.getInstance();

    }

    @Override
    public void onBackPressed() {

        AlertDialog.Builder builder = new AlertDialog.Builder(LoginActivity.this);
        builder.setMessage("Do you want exit ?");
        builder.setPositiveButton("Close App", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                finishAffinity();
                dialog.dismiss();
            }
        }).setNegativeButton("Not now", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
            }
        }).show();

    }
}