package cz.devconf.day;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.TabLayout;
import android.support.v4.view.ViewPager;
import android.view.View;
import android.widget.TextView;

import java.text.SimpleDateFormat;
import java.util.Locale;

import butterknife.BindView;
import cz.devconf.R;
import cz.devconf.base.BaseFragment;
import cz.devconf.home.MainActivity;

public class TalkFragment extends BaseFragment {

    public static final String ARG_DAY = "NUMBER_OF_DAY";

    @BindView(R.id.title)
    TextView title;

    @BindView(R.id.tabs)
    TabLayout tabs;

    @BindView(R.id.view_pager)
    ViewPager viewPager;

    TalkPagerAdapter pAdapter;

    @Override
    public int getLayoutId() {
        return R.layout.fragment_talk;
    }

    public static TalkFragment newInstance(int day) {
        TalkFragment fragment = new TalkFragment();

        Bundle args = new Bundle();
        args.putInt(ARG_DAY, day);
        fragment.setArguments(args);

        return fragment;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        SimpleDateFormat sdf = new SimpleDateFormat("MMMM d", Locale.US);

        int day = getArguments().getInt(ARG_DAY, 1);
        title.setText(getString(R.string.talk_fragment_title, day, sdf.format(MainActivity.TALKS.getDay(day))));

        pAdapter = new TalkPagerAdapter(getFragmentManager(), day);
        viewPager.setAdapter(pAdapter);
        tabs.setTabsFromPagerAdapter(pAdapter);
        tabs.setupWithViewPager(viewPager);
        viewPager.addOnPageChangeListener(new TabLayout.TabLayoutOnPageChangeListener(tabs));
    }
}

