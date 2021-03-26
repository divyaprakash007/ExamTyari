package dp.ibps.generalawareness.Fragments;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.room.Room;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.List;

import dp.ibps.generalawareness.Activity.WebViewActivity;
import dp.ibps.generalawareness.AppUtils.AppConstant;
import dp.ibps.generalawareness.AppUtils.AppPrefs;
import dp.ibps.generalawareness.AppUtils.AppUtils;
import dp.ibps.generalawareness.R;
import dp.ibps.generalawareness.Room.DAO.MainDAOClass;
import dp.ibps.generalawareness.Room.Model.NCERTHindiModel;
import dp.ibps.generalawareness.Room.Model.NotificationsModel;

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
    private List<NCERTHindiModel> ncertHindiModelsList;

    private void initislise(View view) {
        hindiBooksRV = view.findViewById(R.id.hindiBooksRV);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getContext());
        hindiBooksRV.setLayoutManager(linearLayoutManager);

    }

    public class HindiNCERTAdapter extends RecyclerView.Adapter<HindiNCERTAdapter.HindiNCERTViewHolder> {

        public HindiNCERTAdapter() {

        }

        @NonNull
        @Override
        public HindiNCERTViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.news_papers_view, parent, false);
            HindiNCERTViewHolder holder = new HindiNCERTViewHolder(view);
            return holder;
        }

        @Override
        public void onBindViewHolder(@NonNull HindiNCERTViewHolder holder, int position) {
            holder.newsPaperTV.setText("");
            holder.newsPapersCardView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent(getContext(), WebViewActivity.class);
                    intent.putExtra(AppConstant.webIntentKey, position + 2);
                    startActivity(intent);
                }
            });
        }

        @Override
        public int getItemCount() {
            return 20;
        }

        public class HindiNCERTViewHolder extends RecyclerView.ViewHolder {
            public TextView newsPaperTV;
            public CardView newsPapersCardView;

            public HindiNCERTViewHolder(@NonNull View itemView) {
                super(itemView);
                newsPaperTV = itemView.findViewById(R.id.newsPaperTV);
                newsPapersCardView = itemView.findViewById(R.id.newsPapersCardView);
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
        new HindiNCERTAsynkTask().execute();

        initislise(view);


//        if (ncertHindiModelsList.size()<=0 ){
//            // TODO: 26-03-2021 fetch data from server and save to local db (Using Retrofit)
//
//        } else {
//            hindiBooksRV.setAdapter(new HindiNCERTAdapter());
//        }

        return view;
    }

    private class HindiNCERTAsynkTask extends AsyncTask<String, String, String> {

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
        }

        @Override
        protected String doInBackground(String... strings) {
            mainDAOClass = Room.databaseBuilder(getContext(), MainDAOClass.class, dbTableName)
                    .build();
            ncertHindiModelsList = mainDAOClass.mainRoomDB().getHindiNCERTDetails();
            return null;
        }

        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);
            Log.d(getTag(), "onPostExecute: List Size + " + ncertHindiModelsList.size());
        }
    }


}