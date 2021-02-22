package dp.ibps.generalawareness.Activity;

import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.os.Handler;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import java.util.Timer;

import dp.ibps.generalawareness.AppUtils.AppUtils;
import dp.ibps.generalawareness.R;

public class NoInternetConnection extends AppCompatActivity {
    private ImageView internet_status_img;
    private TextView internet_msg_tv;
    private Button open_internet_settings, recheck_internet;
    private ProgressDialog progressDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_no_internet_connection);
        initialise();

        open_internet_settings.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivityForResult(new Intent(android.provider.Settings.ACTION_SETTINGS), 0);
            }
        });

        recheck_internet.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                CountDownTimer countDownTimer = new CountDownTimer(6000, 2000) {
                    @Override
                    public void onTick(long millisUntilFinished) {
                        if (AppUtils.isNetworkAvailable(NoInternetConnection.this)) {
                            internet_status_img.setImageDrawable(getResources().getDrawable(R.drawable.ic_internet_available));
                            new Handler().postDelayed(new Runnable() {
                                @Override
                                public void run() {
                                    if (progressDialog.isShowing()) {
                                        progressDialog.dismiss();
                                    }
                                    finish();
                                }
                            }, 500);

                        } else {
                            if (!progressDialog.isShowing()) {
                                internet_status_img.setImageDrawable(getResources().getDrawable(R.drawable.ic_check_internet));
                                progressDialog.show();
                            }
                        }
                    }

                    @Override
                    public void onFinish() {
                        if (progressDialog.isShowing()) {
                            internet_status_img.setImageDrawable(getResources().getDrawable(R.drawable.ic_no_internet));
                            progressDialog.dismiss();
                        }
                    }
                }.start();

            }
        });

    }

    private void initialise() {
        internet_status_img = findViewById(R.id.internet_status_img);
        internet_msg_tv = findViewById(R.id.internet_msg_tv);
        open_internet_settings = findViewById(R.id.open_internet_settings);
        recheck_internet = findViewById(R.id.recheck_internet);
        progressDialog = new ProgressDialog(NoInternetConnection.this);
        progressDialog.setTitle("Connecting...");
        progressDialog.setMessage("Please wait.");
        progressDialog.setCancelable(false);


    }

    @Override
    public void onBackPressed() {

    }
}