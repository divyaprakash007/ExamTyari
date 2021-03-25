package dp.ibps.generalawareness.Activity;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.provider.Settings;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.safetynet.SafetyNet;
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
import com.google.firebase.auth.PhoneAuthProvider.OnVerificationStateChangedCallbacks;

import java.util.concurrent.TimeUnit;

import dp.ibps.generalawareness.AppUtils.AppConstant;
import dp.ibps.generalawareness.AppUtils.AppPrefs;
import dp.ibps.generalawareness.AppUtils.AppUtils;
import dp.ibps.generalawareness.R;

public class LoginActivity extends AppCompatActivity {
    private FirebaseAuth mAuth;
    private TextInputLayout mobile_til, otp_til;
    private TextInputEditText mobile_et, otp_et;
    private Button otp_login_button;
    private TextView privacy_policy_tv, about_us_tv;
    private String mobileNumber = "";
    private String otp_number = "";
    private PhoneAuthCredential credential;
    private String verificationId;
    private static final String KEY_VERIFICATION_ID = "key_verification_id";
    private TextView skipTV;
    private int loginAttempt = 0;
    private ProgressDialog progressDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        initialise();

        mobile_et.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if (s.length() >= 10) {
                    AppUtils.hideKeyboard(LoginActivity.this);
                }
            }


            @Override
            public void afterTextChanged(Editable s) {

            }
        });

        otp_et.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if (s.length() >= 6) {
                    AppUtils.hideKeyboard(LoginActivity.this);
                }
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });

        otp_login_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                controlAllButtons();
                progressDialog.show();
                if (otp_login_button.getText().toString().equals("Send OTP")) {
                    if (mobile_et.getText().toString().length() != 10) {
                        Toast.makeText(LoginActivity.this, "Please enter a valid number", Toast.LENGTH_LONG).show();
                    } else {
                        sendOtpToMobile(mobile_et.getText().toString().trim());
                    }
                } else {
                    if (otp_et.getText().toString().length() != 6) {
                        Toast.makeText(LoginActivity.this, "Please enter a valid otp", Toast.LENGTH_LONG).show();
                    } else {
                        otp_number = otp_et.getText().toString();
                        credential = PhoneAuthProvider.getCredential(verificationId, otp_number);
                        signInWithPhoneAuthCredential(credential);
                        if (loginAttempt >= 3) {
                            skipTV.setVisibility(View.VISIBLE);
                        }
                    }
                }
            }
        });
        // Restore instance state
        // put this code after starting phone number verification
        if (verificationId == null && savedInstanceState != null) {
            onRestoreInstanceState(savedInstanceState);
        }

    }

    private void sendOtpToMobile(String mobile_number) {
        PhoneAuthOptions options =
                PhoneAuthOptions.newBuilder(mAuth)
                        .setPhoneNumber("+91" + mobile_number)       // Phone number to verify
                        .setTimeout(60L, TimeUnit.SECONDS) // Timeout and unit
                        .setActivity(this)                 // Activity (for callback binding)
                        .setCallbacks(new OnVerificationStateChangedCallbacks() {
                            @Override
                            public void onVerificationCompleted(@NonNull PhoneAuthCredential phoneAuthCredential) {
                                Toast.makeText(LoginActivity.this, "Verification Complete", Toast.LENGTH_LONG).show();
                                otp_login_button.setText("Processing...");
                                otp_login_button.setClickable(false);
                                mobile_til.setEnabled(false);
                                otp_til.setEnabled(false);
                                signInWithPhoneAuthCredential(phoneAuthCredential);
                            }

                            @Override
                            public void onVerificationFailed(@NonNull FirebaseException e) {
                                Toast.makeText(LoginActivity.this, "" + e.getMessage(), Toast.LENGTH_LONG).show();
                                Log.d(getResources().getString(R.string.tag), "onVerificationFailed: " + e.getMessage());
                                resetActivity();
                                if (progressDialog != null) {
                                    progressDialog.dismiss();
                                }
                            }

                            @Override
                            public void onCodeSent(@NonNull String s, @NonNull PhoneAuthProvider.ForceResendingToken forceResendingToken) {
                                otp_login_button.setText("Verify & Login");
                                mobile_til.setEnabled(false);
                                otp_til.setEnabled(true);
                                verificationId = s;
                                Toast.makeText(LoginActivity.this, "OTP Sent", Toast.LENGTH_LONG).show();
                                mobileNumber = mobile_number;
                                loginAttempt++;
                                if (progressDialog != null) {
                                    progressDialog.dismiss();
                                }

                            }
                        })          // OnVerificationStateChangedCallbacks
                        .build();
        PhoneAuthProvider.verifyPhoneNumber(options);

    }

    private void resetActivity() {
        otp_login_button.setText("Send OTP");
        otp_login_button.setEnabled(true);
        mobile_til.setEnabled(true);
        otp_til.setEnabled(false);
        otp_et.setText("");
        mobile_et.setText("");
    }

    @Override
    protected void onSaveInstanceState(@NonNull Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putString(KEY_VERIFICATION_ID, verificationId);
    }

    @Override
    protected void onRestoreInstanceState(@NonNull Bundle savedInstanceState) {
        super.onRestoreInstanceState(savedInstanceState);
        verificationId = savedInstanceState.getString(KEY_VERIFICATION_ID);
    }

    private void signInWithPhoneAuthCredential(PhoneAuthCredential credential) {
        mAuth.signInWithCredential(credential).addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                if (task.isSuccessful()) {
                    AppPrefs.setMobile(LoginActivity.this, mobileNumber);
                    startActivity(new Intent(LoginActivity.this, UpdateProfileActivity.class));
                    finish();
                } else {
                    Toast.makeText(LoginActivity.this, "Verification Failed.", Toast.LENGTH_LONG).show();
                    resetActivity();
                }
                if (progressDialog != null) {
                    progressDialog.dismiss();
                }
            }
        });
    }


    private void initialise() {
        mobile_til = findViewById(R.id.mobile_til);
        otp_til = findViewById(R.id.otp_til);
        mobile_et = findViewById(R.id.mobile_et);
        otp_et = findViewById(R.id.otp_et);
        otp_login_button = findViewById(R.id.otp_login_button);
        privacy_policy_tv = findViewById(R.id.privacy_policy_tv);
        about_us_tv = findViewById(R.id.about_us_tv);
        skipTV = findViewById(R.id.skipTV);
        progressDialog = new ProgressDialog(this);
        progressDialog.setTitle("Processing...");
        progressDialog.setMessage(getResources().getString(R.string.wait_message));
        progressDialog.setCancelable(false);

        skipTV.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                progressDialog.show();
                startActivity(new Intent(LoginActivity.this, HomeActivity.class));
                progressDialog.dismiss();
                finish();
            }
        });

        mAuth = FirebaseAuth.getInstance();

        about_us_tv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                controlAllButtons();
                progressDialog.show();
                Intent intent = new Intent(LoginActivity.this, WebViewActivity.class);
                intent.putExtra(AppConstant.webIntentKey, 0);
                progressDialog.dismiss();
                startActivity(intent);
            }
        });

        privacy_policy_tv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                controlAllButtons();
                progressDialog.show();
                Intent intent = new Intent(LoginActivity.this, WebViewActivity.class);
                intent.putExtra(AppConstant.webIntentKey, 1);
                progressDialog.dismiss();
                startActivity(intent);
            }
        });
    }

    private void controlAllButtons() {
        otp_login_button.setClickable(false);
        privacy_policy_tv.setClickable(false);
        about_us_tv.setClickable(false);
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                otp_login_button.setClickable(true);
                privacy_policy_tv.setClickable(true);
                about_us_tv.setClickable(true);
            }
        }, 1000);

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