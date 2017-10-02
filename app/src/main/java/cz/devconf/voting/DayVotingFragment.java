package cz.devconf.voting;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import butterknife.BindView;
import cz.devconf.Feedback;
import cz.devconf.home.MainActivity;
import cz.devconf.R;
import cz.devconf.Voting;
import cz.devconf.VotingViewHolder;
import cz.devconf.base.BaseFragment;

public class DayVotingFragment extends BaseFragment {

    private static final String TAG = DayVotingFragment.class.getName();

    @BindView(R.id.loading_label)
    TextView loadingLabel;

    @BindView(R.id.loading_bar)
    ProgressBar progressBar;

    @BindView(R.id.recycler_view)
    RecyclerView recycler;

    @BindView(R.id.loading_box)
    LinearLayout loading;

    List<Voting> list;
    FirebaseRecyclerAdapter mAdapter;
    int day;

    @Override
    public int getLayoutId() {
        return R.layout.fragment_speakers;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        list = new ArrayList<>();

        recycler.setHasFixedSize(true);
        recycler.setLayoutManager(new LinearLayoutManager(getActivity()));

        day = getArguments().getInt("index") + 1;

        DatabaseReference myRef = MainActivity.FBDB.getDatabase().getReference("votes");

        // TODO extract this to a separated file
        mAdapter = new FirebaseRecyclerAdapter<Voting, VotingViewHolder>(Voting.class, R.layout.row_voting, VotingViewHolder.class, myRef) {
            @Override
            public void populateViewHolder(VotingViewHolder votingHolder, Voting item, int position) {
                if (list.size() > 0) {
                    votingHolder.setVote(list.get(position));
                }
            }

            @Override
            public int getItemCount() {
                return list.size();
            }

            @Override
            public Voting getItem(int position) {
                return list.get(position);
            }
        };

        recycler.setAdapter(mAdapter);

        myRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                // This method is called once with the initial value and again
                // whenever data at this location is updated.
                if (list.size() > 0) {
                    list.clear();
                }

                // TODO extract this logic out
                for (DataSnapshot vote : dataSnapshot.getChildren()) {
                    for (DataSnapshot ds : vote.getChildren()) {
                        try {
                            int talkId = Integer.parseInt(ds.getKey());
                            int index = getVote(talkId);
                            Feedback fb = ds.getValue(Feedback.class);
                            if (index < 0) {
                                Voting voting = new Voting(talkId);
                                if (voting.talk.getDay() == day) {
                                    voting.add(Integer.parseInt(fb.rating));
                                    list.add(voting);
                                }
                            } else {
                                list.get(index).add(Integer.parseInt(fb.rating));
                            }
                        } catch (NumberFormatException e) {
                            Log.e("NFE", e.getMessage());
                        }
                    }
                }

                countVotes();
                setLoadingBox();
                mAdapter.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(DatabaseError error) {
                Log.w(TAG, "Failed to read value.", error.toException());
                // TODO show error popup
            }
        });
    }

    public int getVote(int talkId) {
        for (int i = 0; i < list.size(); i++) {
            if (list.get(i).talk.getId() == talkId) {
                return i;
            }
        }
        return -1;
    }

    public void countVotes() {
        Collections.sort(list, sortByVotes);
        for (int i = 0; i < list.size(); i++) {
            list.get(i).addScore(i + 1);
        }

        Collections.sort(list, sortByAvg);
        for (int i = 0; i < list.size(); i++) {
            list.get(i).addScore(i + 1);
        }

        Collections.sort(list, sortByScore);

    }

    public static Comparator<Voting> sortByVotes = new Comparator<Voting>() {
        @Override
        public int compare(Voting talk, Voting t1) {
            int vote = talk.numVotes;
            int vote2 = t1.numVotes;
            int dateComparision = vote - vote2;
            if (dateComparision > 0) {
                return 1;
            }

            if (dateComparision < 0) {
                return -1;
            }

            dateComparision = Float.compare(talk.getAvgVotes(), t1.getAvgVotes());

            if (dateComparision > 0) {
                return 1;
            }

            if (dateComparision < 0) {
                return -1;
            }

            return 0;
        }
    };

    public static Comparator<Voting> sortByAvg = new Comparator<Voting>() {
        @Override
        public int compare(Voting talk, Voting t1) {
            float vote = talk.getAvgVotes();
            float vote2 = t1.getAvgVotes();
            int dateComparision = Float.compare(vote, vote2);

            if (dateComparision > 0) {
                return 1;
            }

            if (dateComparision < 0) {
                return -1;
            }

            return 0;
        }
    };

    public static Comparator<Voting> sortByScore = new Comparator<Voting>() {
        @Override
        public int compare(Voting talk, Voting t1) {
            int vote = talk.score;
            int vote2 = t1.score;
            int dateComparision = vote2 - vote;

            if (dateComparision > 0) {
                return 1;
            }

            if (dateComparision < 0) {
                return -1;
            }

            dateComparision = t1.votes - talk.votes;

            if (dateComparision > 0) {
                return 1;
            }

            if (dateComparision < 0) {
                return -1;
            }

            return 0;

        }
    };

    public void setLoadingBox() {
        loading.setVisibility(View.GONE);
        recycler.setVisibility(View.VISIBLE);
    }

}
