package dp.ibps.generalawareness.Fragments;

import android.app.AlertDialog;
import android.app.ProgressDialog;
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

import java.util.List;

import dp.ibps.generalawareness.AppUtils.AppConstant;
import dp.ibps.generalawareness.R;
import dp.ibps.generalawareness.Room.DAO.MainDAOClass;
import dp.ibps.generalawareness.Room.Model.NCERTHindiModel;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link HindiNCERTBooks#newInstance} factory method to
 * create an instance of this fragment.
 */
public class HindiNCERTBooks extends Fragment {

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";
    private RecyclerView hindiNCERTRV;

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    public HindiNCERTBooks() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment HindiNCERTBooks.
     */
    // TODO: Rename and change types and number of parameters
    public static HindiNCERTBooks newInstance(String param1, String param2) {
        HindiNCERTBooks fragment = new HindiNCERTBooks();
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
        View view = inflater.inflate(R.layout.fragment_hindi_n_c_e_r_t_books, container, false);
        initislise(view);


        return view;
    }

    private void initislise(View view) {
        hindiNCERTRV = view.findViewById(R.id.hindiNCERTRV);
        hindiNCERTRV.setLayoutManager(new LinearLayoutManager(getContext()));
        hindiNCERTRV.setAdapter(new NCERTHindiAdapter(getContext()));


    }

    public class NCERTHindiAdapter extends RecyclerView.Adapter<NCERTHindiAdapter.NCERTHindiView> {

        private Context context;

        public NCERTHindiAdapter(Context context) {
            this.context = context;
        }

        @NonNull
        @Override
        public NCERTHindiAdapter.NCERTHindiView onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.ncert_books_view, parent, false);
            NCERTHindiAdapter.NCERTHindiView holder = new NCERTHindiAdapter.NCERTHindiView(view);
            return holder;
        }

        @Override
        public void onBindViewHolder(@NonNull NCERTHindiAdapter.NCERTHindiView holder, int position) {
            holder.titleTV.setText(AppConstant.ncertHindiList.get(position).getBookName());
            holder.classTV.setText(AppConstant.ncertHindiList.get(position).getClassName());
            holder.subjectTV.setText(AppConstant.ncertHindiList.get(position).getSubName());
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
                            Intent browserIntent = new Intent(Intent.ACTION_VIEW, Uri.parse(AppConstant.ncertHindiList.get(position).getLinkUrl()));
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
            return AppConstant.ncertHindiList.size();
        }

        public class NCERTHindiView extends RecyclerView.ViewHolder {

            private TextView titleTV, classTV, subjectTV;
            private CardView hindiItemViewCardView;

            public NCERTHindiView(@NonNull View itemView) {
                super(itemView);
                titleTV = itemView.findViewById(R.id.titleTV);
                classTV = itemView.findViewById(R.id.classTV);
                subjectTV = itemView.findViewById(R.id.subjectTV);
                hindiItemViewCardView = itemView.findViewById(R.id.hindiItemViewCardView);
            }
        }
    }


}