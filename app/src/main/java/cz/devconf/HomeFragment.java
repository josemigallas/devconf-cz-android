package cz.devconf;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

import butterknife.BindView;
import cz.devconf.base.BaseFragment;
import cz.devconf.home.MainActivity;

public class HomeFragment extends BaseFragment {

    @BindView(R.id.loading_label)
    TextView loadingLabel;

    @BindView(R.id.loading_bar)
    ProgressBar progressBar;

    @BindView(R.id.loading_box)
    LinearLayout loading;

    @BindView(R.id.confernceend)
    TextView confEnd;

    @BindView(R.id.recycler_view)
    RecyclerView recycler;

    public HomeRecycleViewAdapter mAdapter;
    static Date dayFour;

    @Override
    public int getLayoutId() {
        return R.layout.fragment_home;
    }

    public HomeFragment() {
    }

    public static HomeFragment newInstance() {
        return new HomeFragment();
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        SimpleDateFormat sdf = new SimpleDateFormat("dd/M/yyyy");
        try {
            dayFour = sdf.parse("31/1/2017");
        } catch (ParseException e) {
            e.printStackTrace();
        }

        recycler.setHasFixedSize(true);
        recycler.setLayoutManager(new LinearLayoutManager(getActivity()));

        mAdapter = new HomeRecycleViewAdapter();
        if (new Date().compareTo(dayFour) >= 0) {
            loading.setVisibility(View.GONE);
            recycler.setVisibility(View.GONE);
            confEnd.setVisibility(View.VISIBLE);
        } else {
            loading.setVisibility(View.VISIBLE);
            recycler.setVisibility(View.GONE);
            confEnd.setVisibility(View.GONE);
        }

        MainActivity.ALLLOADED.fragment = this;
        recycler.setAdapter(mAdapter);
        if (MainActivity.TALKS.getActualTalks().size() > 0) {
            mAdapter.updateData(MainActivity.TALKS.getActualTalks());
            setLoadingBox();
        }
    }


    public void setLoadingBox() {
        loading.setVisibility(View.GONE);
        recycler.setVisibility(View.VISIBLE);
    }
}