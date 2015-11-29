package me.rikinmarfatia.quack;

import android.app.Activity;
import android.app.Fragment;
import android.content.Context;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.SearchView;
import android.widget.TextView;
import android.widget.Toast;

import java.util.List;

import me.rikinmarfatia.quack.models.SearchResult;
import me.rikinmarfatia.quack.models.Topic;
import me.rikinmarfatia.quack.services.DuckDuckGoService;
import me.rikinmarfatia.quack.services.RetrofitService;
import me.rikinmarfatia.quack.util.DuckOptions;
import retrofit.Call;
import retrofit.Callback;
import retrofit.Response;
import retrofit.Retrofit;

/**
 * Handles input for queries and displays results
 *
 * @author Rikin (rikinm10@gmail.com)
 */
public class SearchFragment extends Fragment {

    public static final String TAG = "SearchFragment";

    private SearchView mSearchBar;
    private RecyclerView mSearchResults;
    private ResultAdapter mAdapter;
    private DuckDuckGoService mQuacker;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        mQuacker = RetrofitService.createService(DuckDuckGoService.class);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_search, container, false);

        mSearchBar = (SearchView) v.findViewById(R.id.searchview_submit);
        mSearchBar.setSubmitButtonEnabled(true);
        mSearchBar.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {

                if(query.length() != 0) {
                    Call<SearchResult> searchCall = mQuacker.search(query,
                            DuckOptions.DUCK_FORMAT_JSON, 0);
                    searchCall.enqueue(new Callback<SearchResult>() {
                        @Override
                        public void onResponse(Response<SearchResult> response, Retrofit retrofit) {
                            List<Topic> topics = response.body().getRelatedTopics();
                            mAdapter = new ResultAdapter(topics);
                            mSearchResults.setAdapter(mAdapter);
                        }

                        @Override
                        public void onFailure(Throwable t) {
                            Log.d(TAG, t.getMessage());
                        }
                    });
                }

                hideKeyboard();

                return true;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                return false;
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

        private List<Topic> mResults;

        public ResultAdapter(List<Topic> results) {
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
            Topic topic = mResults.get(position);
            holder.mResultText.setText(topic.getText());
        }

        @Override
        public int getItemCount() {
            return mResults.size();
        }
    }

    public void hideKeyboard() {
        Activity activity = getActivity();
        InputMethodManager inputManager = (InputMethodManager) activity
                                            .getSystemService(Context.INPUT_METHOD_SERVICE);

        inputManager.hideSoftInputFromWindow(null == activity.getCurrentFocus() ?
                                null : activity.getCurrentFocus().getWindowToken(),
                                InputMethodManager.HIDE_NOT_ALWAYS);
    }
}