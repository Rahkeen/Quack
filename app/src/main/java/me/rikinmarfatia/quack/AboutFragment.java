package me.rikinmarfatia.quack;

import android.app.Fragment;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

/**
 * Created by rikin on 11/30/15.
 */
public class AboutFragment extends Fragment {

    private ImageView mDuckDuckLogo;
    private MediaPlayer mMediaPlayer;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getActivity().setTitle(R.string.action_about);
        mMediaPlayer = MediaPlayer.create(getActivity(), R.raw.quak2);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_about, container, false);

        mDuckDuckLogo = (ImageView) v.findViewById(R.id.iv_ducklogo);
        mDuckDuckLogo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mMediaPlayer.start();
            }
        });

        return v;
    }
}
