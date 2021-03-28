package dp.ibps.generalawareness.Fragments;

import android.app.ProgressDialog;
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
    private RecyclerView hindiBooksRV;
    private MainDAOClass mainDAOClass;
    private String dbTableName = "HindiNCERTDB";
    private ProgressDialog dialog;

    private void initislise(View view) {
//        hindiBooksRV = view.findViewById(R.id.hindiBooksRV);
//        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getContext());
//        hindiBooksRV.setLayoutManager(linearLayoutManager);
        dialog = new ProgressDialog(getContext());
        dialog.setTitle(getResources().getString(R.string.title));
        dialog.setMessage(getResources().getString(R.string.wait_message));
        dialog.setCancelable(false);
//        dialog.show();

    }

    public class HindiNCERTAdapter extends RecyclerView.Adapter<HindiNCERTAdapter.HindiNCERTViewHolder> {

        private List<NCERTHindiModel> ncertHindiModelsList;

        public HindiNCERTAdapter(List<NCERTHindiModel> ncertHindiModelsList) {
            this.ncertHindiModelsList = ncertHindiModelsList;
        }

        @NonNull
        @Override
        public HindiNCERTViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.hindi_ncert_view, parent, false);
            HindiNCERTViewHolder holder = new HindiNCERTViewHolder(view);
            return holder;
        }

        @Override
        public void onBindViewHolder(@NonNull HindiNCERTViewHolder holder, int position) {
            holder.titleTV.setText(ncertHindiModelsList.get(position).getBookName());
            holder.classTV.setText(ncertHindiModelsList.get(position).getClassName());
            holder.subjectTV.setText(ncertHindiModelsList.get(position).getSubName());

            holder.hindiItemViewCardView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    // TODO: 27-03-2021 open pdf url in pdf view
                }
            });
        }

        @Override
        public int getItemCount() {
            return ncertHindiModelsList.size();
        }

        public class HindiNCERTViewHolder extends RecyclerView.ViewHolder {
            public TextView titleTV, classTV, subjectTV;
            public CardView hindiItemViewCardView;

            public HindiNCERTViewHolder(@NonNull View itemView) {
                super(itemView);
                hindiItemViewCardView = itemView.findViewById(R.id.hindiItemViewCardView);
                titleTV = itemView.findViewById(R.id.titleTV);
                classTV = itemView.findViewById(R.id.classTV);
                subjectTV = itemView.findViewById(R.id.subjectTV);
            }
        }
    }

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



}