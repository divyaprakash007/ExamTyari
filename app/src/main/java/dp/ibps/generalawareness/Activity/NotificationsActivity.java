package dp.ibps.generalawareness.Activity;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.Notification;
import android.app.ProgressDialog;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.List;

import dp.ibps.generalawareness.AppUtils.AppUtils;
import dp.ibps.generalawareness.R;

public class NotificationsActivity extends AppCompatActivity {
    private FirebaseFirestore db;
    private RecyclerView notificationRV;
    private ProgressDialog notiDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_notifications);
        getSupportActionBar().setTitle("Notifications");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        db = FirebaseFirestore.getInstance();
        notiDialog = new ProgressDialog(this);
        notiDialog.setTitle("Loading");
        notiDialog.setMessage("Please wait");
        notiDialog.setCancelable(false);

        if (AppUtils.isNetworkAvailable(NotificationsActivity.this)) {
            notiDialog.show();
            db.collection("notifications").get().addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
                @Override
                public void onSuccess(QuerySnapshot queryDocumentSnapshots) {
                    if (queryDocumentSnapshots.isEmpty()) {
                        // TODO: 20-03-2021 get Data from Firebase db 
                        Log.d("TAG", "onSuccess: ");
                    } else {
                        notificationRV = findViewById(R.id.notificationRV);
                        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(NotificationsActivity.this);
                        notificationRV.setLayoutManager(linearLayoutManager);
                        notificationRV.setAdapter(new NotificationAdapter(queryDocumentSnapshots));
                    }
                    notiDialog.dismiss();
                }
            }).addOnFailureListener(new OnFailureListener() {
                @Override
                public void onFailure(@NonNull Exception e) {
                    // TODO: 20-03-2021 get Data from Firebase db
//                    Toast.makeText(NotificationsActivity.this, "", Toast.LENGTH_SHORT).show();
                    notiDialog.dismiss();
                }
            });
        } else {
            startActivity(new Intent(NotificationsActivity.this, NoInternetConnection.class));
        }
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        switch (id) {
            case android.R.id.home:
                onBackPressed();
                break;
        }
        return super.onOptionsItemSelected(item);
    }

    public class NotificationAdapter extends RecyclerView.Adapter<NotificationAdapter.NotificationViewHolder> {

        private QuerySnapshot queryDocumentSnapshots;

        public NotificationAdapter(QuerySnapshot queryDocumentSnapshots) {
            this.queryDocumentSnapshots = queryDocumentSnapshots;
        }

        @NonNull
        @Override
        public NotificationViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.notifications_view, parent, false);
            NotificationViewHolder holder = new NotificationViewHolder(view);
            return holder;
        }

        @Override
        public void onBindViewHolder(@NonNull NotificationViewHolder holder, int position) {
            holder.noti_title_tv.setText(queryDocumentSnapshots.getDocuments().get((queryDocumentSnapshots.getDocuments().size() - 1) - position).get("title").toString());
            holder.noti_message_tv.setText(queryDocumentSnapshots.getDocuments().get((queryDocumentSnapshots.getDocuments().size() - 1) - position).get("message").toString());
            holder.noti_date_tv.setText("Date : " + queryDocumentSnapshots.getDocuments().get((queryDocumentSnapshots.getDocuments().size() - 1) - position).get("date").toString());
            if (queryDocumentSnapshots.getDocuments().get((queryDocumentSnapshots.getDocuments().size() - 1) - position).get("link").toString().length() > 0) {
                holder.noti_link_tv.setText(queryDocumentSnapshots.getDocuments().get((queryDocumentSnapshots.getDocuments().size() - 1) - position).get("link").toString());
                holder.noti_link_tv.setVisibility(View.VISIBLE);
                holder.noti_link_tv.setEnabled(true);
                holder.noti_cardView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        try {
                            String url = "" + queryDocumentSnapshots.getDocuments().get((queryDocumentSnapshots.getDocuments().size() - 1) - position).get("link").toString();
                            if (!url.contains("https://")) {
                                url = "https://" + url;
                            }
                            Intent browserIntent = new Intent(Intent.ACTION_VIEW, Uri.parse(url));
                            startActivity(browserIntent);
                        } catch (Exception e) {
                            Toast.makeText(NotificationsActivity.this, "Server busy! Please try after some time.", Toast.LENGTH_SHORT).show();
                            AppUtils.sendErrorMessage(NotificationsActivity.this, AppUtils.getTodayDate(), e.getMessage(), queryDocumentSnapshots.getDocuments().get((queryDocumentSnapshots.getDocuments().size() - 1) - position).get("link").toString(), "notifications");
                        }
                    }
                });

            } else {
                holder.noti_link_tv.setVisibility(View.GONE);
                holder.noti_link_tv.setEnabled(false);
            }
        }

        @Override
        public int getItemCount() {
            return queryDocumentSnapshots.getDocuments().size();
        }

        public class NotificationViewHolder extends RecyclerView.ViewHolder {
            public TextView noti_link_tv, noti_message_tv, noti_title_tv, noti_date_tv;
            public CardView noti_cardView;

            public NotificationViewHolder(@NonNull View itemView) {
                super(itemView);
                noti_link_tv = itemView.findViewById(R.id.noti_link_tv);
                noti_message_tv = itemView.findViewById(R.id.noti_message_tv);
                noti_title_tv = itemView.findViewById(R.id.noti_title_tv);
                noti_date_tv = itemView.findViewById(R.id.noti_date_tv);
                noti_cardView = itemView.findViewById(R.id.noti_cardView);

            }
        }
    }
}


