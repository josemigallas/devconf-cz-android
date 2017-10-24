package cz.devconf2017;

import android.content.Context;
import android.graphics.Color;
import android.support.annotation.Nullable;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.text.ParseException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;
import java.util.HashMap;
import java.util.List;

import cz.devconf2017.util.DateFormatUtils;

public class TalkBusiness {

    private static final String TAG = "TalkBusiness";

    private final Talk talk;

    public TalkBusiness(Talk talk) {
        this.talk = talk;
    }

    public CharSequence printScore() {
        return String.valueOf(talk.getAverageRating());
    }

    public CharSequence printTitle() {
        return talk.getTitle();
    }

    private CharSequence getPrintedSpeakers(@Nullable Collection<Speaker> speakers) {
        if (speakers == null || speakers.size() == 0) {
            return "No speaker";
        }

        final StringBuilder sb = new StringBuilder();

        for (Speaker speaker : speakers) {
            sb.append(speaker.getName())
                    .append(", ");
        }
        return sb.delete(sb.length() - 2, sb.length()).toString();
    }

    public CharSequence printStatistics() {
        return "todo";
    }

    public float calculateAverageRatingOfSession() {
        HashMap<String, Feedback> votes = talk.getVotes();

        if (votes == null) {
            return 0;
        }

        float ratingSum = 0;
        for (Feedback feedback : votes.values()) {
            ratingSum += feedback.getRating();
        }

        return ratingSum / votes.size();
    }

    @Nullable
    public Collection<String> getSpeakerIds() {
        return talk.getSpeakers();
    }

    public String printStart() {
        try {
            Date dateStartTime = DateFormatUtils.DATE_FORMAT_TIME_INPUT.parse(talk.getStart());
            return DateFormatUtils.DATE_FORMAT_TIME.format(dateStartTime);
        } catch (ParseException e) {
            e.printStackTrace();
            return talk.getStart();
        }
    }

    public String printDuration() {
        try {
            Date dateDuration = DateFormatUtils.DATE_FORMAT_DURATION_INPUT.parse(talk.getDuration());
            return DateFormatUtils.DATE_FORMAT_DURATION.format(dateDuration) + " h";
        } catch (ParseException e) {
            e.printStackTrace();
            return talk.getDuration();
        }
    }

    public CharSequence printDay(Context context) {
        return context.getString(R.string.day_format, talk.getDay());
    }

    public CharSequence printRoom() {
        return talk.getRoom().toUpperCase();
    }

    public void getTrackColor(final GetTrackColorListener listener) {
        FirebaseDatabase.getInstance()
                .getReference("tracks")
                .orderByChild("name")
                .equalTo(talk.getTrack())
                .limitToFirst(1)
                .addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {
                        for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                            Track track = snapshot.getValue(Track.class);
                            int color = Color.parseColor(track.getColor());
                            listener.onGetColor(color);
                        }
                    }

                    @Override
                    public void onCancelled(DatabaseError databaseError) {

                    }
                });
    }

    public boolean isRunning() {
        // TODO
        return false;
    }

    public CharSequence printTrack() {
        return talk.getTrack();
    }

    public void getPrintedSpeakers(final GetPrintedSpeakersListener listener) {
        FirebaseDatabase.getInstance()
                .getReference("speakers")
                .addListenerForSingleValueEvent(new ValueEventListener() {
                    private List<Speaker> speakers = new ArrayList<>();

                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {
                        final Iterable<DataSnapshot> speakerSnapshots = dataSnapshot.getChildren();
                        final Collection<String> speakerIds = getSpeakerIds();

                        if (speakerIds != null) {
                            for (DataSnapshot snapshot : speakerSnapshots) {
                                if (speakerIds.contains(snapshot.getKey())) {
                                    Speaker speaker = snapshot.getValue(Speaker.class);
                                    this.speakers.add(speaker);
                                }
                            }
                        }
                        listener.onGetPrintedSpeakers(getPrintedSpeakers(this.speakers));
                    }

                    @Override
                    public void onCancelled(DatabaseError databaseError) {

                    }
                });
    }

    public interface GetTrackColorListener {

        int DEFAULT_COLOR = Color.parseColor("#888888");

        void onGetColor(int trackColor);
    }

    public interface GetPrintedSpeakersListener {

        CharSequence DEFAULT_SPEAKERS = "...";

        void onGetPrintedSpeakers(CharSequence speakers);
    }
}
