package me.rikinmarfatia.quack;

import android.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

public class AboutActivity extends SingleFragmentActivity {

    @Override
    protected Fragment createFragment() {
        return new AboutFragment();
    }
}
