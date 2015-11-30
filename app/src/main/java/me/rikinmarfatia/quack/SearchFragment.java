package me.rikinmarfatia.quack;

import android.app.Activity;
import android.app.Fragment;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.SearchView;
import android.widget.TextView;

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
        setHasOptionsMenu(true);

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
                            removeEmptyTopics(topics);
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

    @Override
    public void startActivity(Intent intent) {
        super.startActivity(intent);
        getActivity().overridePendingTransition(R.anim.slide_in_left, R.anim.slide_out_left);
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        super.onCreateOptionsMenu(menu, inflater);
        inflater.inflate(R.menu.menu_main, menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch(item.getItemId()) {
            case R.id.quack_about:
                Intent aboutIntent = new Intent(getActivity(), AboutActivity.class);
                startActivity(aboutIntent);
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    private class ResultHolder extends RecyclerView.ViewHolder {

        private TextView mSearchResultTitle;
        private TextView mSearchResultDetails;
        private Button mSearchResultInfo;

        public ResultHolder(View itemView) {
            super(itemView);

            mSearchResultTitle = (TextView)itemView.findViewById(R.id.searchresult_title);
            mSearchResultDetails = (TextView)itemView.findViewById(R.id.searchresult_details);
            mSearchResultInfo = (Button)itemView.findViewById(R.id.searchresults_info);

        }

        public void bindTopic(final Topic topic) {
            String resultString = topic.getResult();
            parseTitleAndDetails(resultString);

            mSearchResultInfo.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Uri webResult = Uri.parse(topic.getFirstURL());
                    Intent browserIntent = new Intent(Intent.ACTION_VIEW, webResult);
                    if(browserIntent.resolveActivity(getActivity().getPackageManager()) != null) {
                        startActivity(browserIntent);
                    }
                }
            });

        }

        // TODO: possibly find a better way to extract details, regex filter maybe
        private void parseTitleAndDetails(String resultString) {
            String[] firstSplit = resultString.split("</a>");

            // some results don't have description
            if(firstSplit.length > 1) {
                // some descriptions are hyphen (-) separated from the title
                String details = firstSplit[1].trim();
                if(details.charAt(0) == '-') {
                    mSearchResultDetails.setText(details.substring(2));
                } else {
                    mSearchResultDetails.setText(details);
                }
            }

            // after getting the description, get the title
            String[] secondSplit = firstSplit[0].split(">");
            mSearchResultTitle.setText(secondSplit[1]);
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
            View v = inflater.inflate(R.layout.view_searchresult, parent, false);

            return new ResultHolder(v);
        }

        @Override
        public void onBindViewHolder(ResultHolder holder, int position) {
            Topic topic = mResults.get(position);
            holder.bindTopic(topic);
        }

        @Override
        public int getItemCount() {
            return mResults.size();
        }
    }

    // Utility Functions =====================================================================

    /**
     * Removes the Empty Objects in the topics list caused by
     * the disambiguation type.
     */
    private void removeEmptyTopics(List<Topic> topics) {
        for(int i = 0; i < topics.size(); i++) {
            if(topics.get(i).getResult() == null) {
                while(i < topics.size()) {
                    topics.remove(i);
                }
            }
        }
    }

    /**
     * Hides keyboard, used for if submit button is pushed to make sure
     * the keyboard is no longer shown.
     */
    private void hideKeyboard() {
        Activity activity = getActivity();
        InputMethodManager inputManager = (InputMethodManager) activity
                                            .getSystemService(Context.INPUT_METHOD_SERVICE);

        inputManager.hideSoftInputFromWindow(null == activity.getCurrentFocus() ?
                                null : activity.getCurrentFocus().getWindowToken(),
                                InputMethodManager.HIDE_NOT_ALWAYS);
    }
}