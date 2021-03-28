package dp.ibps.generalawareness.Fragments;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import dp.ibps.generalawareness.AppUtils.AppConstant;
import dp.ibps.generalawareness.R;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link EnglishNCERTBooks#newInstance} factory method to
 * create an instance of this fragment.
 */
public class EnglishNCERTBooks extends Fragment {
    private RecyclerView engNCERTRV;

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    public EnglishNCERTBooks() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment EnglishNCERTBooks.
     */
    // TODO: Rename and change types and number of parameters
    public static EnglishNCERTBooks newInstance(String param1, String param2) {
        EnglishNCERTBooks fragment = new EnglishNCERTBooks();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_english_n_c_e_r_t_books, container, false);
        initialise(view);
        return view;
    }

    private void initialise(View view) {
        engNCERTRV = view.findViewById(R.id.engNCERTRV);
        engNCERTRV.setLayoutManager(new LinearLayoutManager(getContext()));
        engNCERTRV.setAdapter(new NCERTEngAdapter(getContext()));

    }

    public class NCERTEngAdapter extends RecyclerView.Adapter<NCERTEngAdapter.EnglishViewHolder> {

        private Context context;

        public NCERTEngAdapter(Context context) {
            this.context = context;
        }

        @NonNull
        @Override
        public EnglishViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.ncert_books_view, parent, false);
            EnglishViewHolder holder = new EnglishViewHolder(view);
            return holder;
        }

        @Override
        public void onBindViewHolder(@NonNull EnglishViewHolder holder, int position) {
            holder.titleTV.setText(AppConstant.ncertEnglishList.get(position).getBookName());
            holder.classTV.setText(AppConstant.ncertEnglishList.get(position).getClassName());
            holder.subjectTV.setText(AppConstant.ncertEnglishList.get(position).getSubName());
            holder.hindiItemViewCardView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    AlertDialog.Builder builder = new AlertDialog.Builder(context);
                    builder.setCancelable(false);
                    builder.setMessage("Download process will take some time according to internet speed.");
                    builder.setTitle("Start Download");
                    builder.setPositiveButton("Okay", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            Intent browserIntent = new Intent(Intent.ACTION_VIEW, Uri.parse(AppConstant.ncertEnglishList.get(position).getLinkUrl()));
                            startActivity(browserIntent);
                            dialog.dismiss();
                        }
                    }).setNegativeButton("Later", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            dialog.dismiss();
                        }
                    });
                    builder.show();
                }
            });
        }

        @Override
        public int getItemCount() {
            return AppConstant.ncertEnglishList.size();
        }

        public class EnglishViewHolder extends RecyclerView.ViewHolder {

            private TextView titleTV, classTV, subjectTV;
            private CardView hindiItemViewCardView;

            public EnglishViewHolder(@NonNull View itemView) {
                super(itemView);
                titleTV = itemView.findViewById(R.id.titleTV);
                classTV = itemView.findViewById(R.id.classTV);
                subjectTV = itemView.findViewById(R.id.subjectTV);
                hindiItemViewCardView = itemView.findViewById(R.id.hindiItemViewCardView);
            }
        }
    }

}