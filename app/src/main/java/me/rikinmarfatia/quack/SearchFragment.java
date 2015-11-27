package me.rikinmarfatia.quack;

import android.app.Fragment;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import java.util.List;

import me.rikinmarfatia.quack.models.SearchResult;

/**
 * Handles input for queries and displays results
 *
 * @author Rikin (rikinm10@gmail.com)
 */
public class SearchFragment extends Fragment {

    private EditText mSearchBox;
    private Button mSearchButton;
    private RecyclerView mSearchResults;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_search, container, false);

        mSearchBox = (EditText) v.findViewById(R.id.edittext_search);

        mSearchButton = (Button) v.findViewById(R.id.btn_search);
        mSearchButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // TODO: Process the search query.
            }
        });

        mSearchResults = (RecyclerView) v.findViewById(R.id.recyclerview_search_results);
        mSearchResults.setLayoutManager(new LinearLayoutManager(getActivity()));

        return v;
    }

    private class ResultHolder extends RecyclerView.ViewHolder {

        public TextView mResultText;

        public ResultHolder(View itemView) {
            super(itemView);

            mResultText = (TextView) itemView;
        }
    }

    private class ResultAdapter extends RecyclerView.Adapter<ResultHolder> {

        private List<SearchResult> mResults;

        public ResultAdapter(List<SearchResult> results) {
            mResults = results;
        }

        @Override
        public ResultHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            LayoutInflater inflater = getActivity().getLayoutInflater();
            View v = inflater.inflate(android.R.layout.simple_list_item_1, parent, false);
            return new ResultHolder(v);
        }

        @Override
        public void onBindViewHolder(ResultHolder holder, int position) {

        }

        @Override
        public int getItemCount() {
            return 0;
        }
    }
}
