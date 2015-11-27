package me.rikinmarfatia.quack;

import android.app.Fragment;

public class SearchActivity extends SingleFragmentActivity {

    @Override
    protected Fragment createFragment() {
        return new SearchFragment();
    }
}
