package dp.ibps.generalawareness.Fragments;

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
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import java.util.List;

import dp.ibps.generalawareness.Activity.NCERTActivity;
import dp.ibps.generalawareness.Activity.NotificationsActivity;
import dp.ibps.generalawareness.Activity.WebViewActivity;
import dp.ibps.generalawareness.AppUtils.AppConstant;
import dp.ibps.generalawareness.AppUtils.AppUtils;
import dp.ibps.generalawareness.R;
import dp.ibps.generalawareness.Room.Model.NotificationsModel;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link MainFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class MainFragment extends Fragment {

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";
    private RecyclerView newsPapersRV;
    private Button ncertBooksBtn;

    private void initialise(View view) {
        newsPapersRV = view.findViewById(R.id.newsPapersRV);
        ncertBooksBtn = view.findViewById(R.id.ncertBooksBtn);

        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getContext(), LinearLayoutManager.HORIZONTAL, false);
        newsPapersRV.setLayoutManager(linearLayoutManager);
        newsPapersRV.setAdapter(new NewsPapersAdapter());


        ncertBooksBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getContext(), NCERTActivity.class));
            }
        });

    }

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    public MainFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment MainFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static MainFragment newInstance(String param1, String param2) {
        MainFragment fragment = new MainFragment();
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
        View view = inflater.inflate(R.layout.fragment_main, container, false);
        initialise(view);
        return view;
    }


    public class NewsPapersAdapter extends RecyclerView.Adapter<NewsPapersAdapter.NewsPapersViewHolder> {

        public String[] NewsPapersName = {"The Hindu", "The Indian Express", "NDTV",
                "Time of India", "Hindustan Times"};

        public NewsPapersAdapter() {

        }

        @NonNull
        @Override
        public NewsPapersViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.news_papers_view, parent, false);
            NewsPapersViewHolder holder = new NewsPapersViewHolder(view);
            return holder;
        }

        @Override
        public void onBindViewHolder(@NonNull NewsPapersViewHolder holder, int position) {
            holder.newsPaperTV.setText(NewsPapersName[position]);
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
            return NewsPapersName.length;
        }

        public class NewsPapersViewHolder extends RecyclerView.ViewHolder {
            public TextView newsPaperTV;
            public CardView newsPapersCardView;

            public NewsPapersViewHolder(@NonNull View itemView) {
                super(itemView);
                newsPaperTV = itemView.findViewById(R.id.newsPaperTV);
                newsPapersCardView = itemView.findViewById(R.id.newsPapersCardView);
            }
        }
    }

}