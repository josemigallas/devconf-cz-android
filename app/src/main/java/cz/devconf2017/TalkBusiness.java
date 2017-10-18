package cz.devconf2017;

import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

import java.util.Collection;
import java.util.HashMap;

import cz.devconf2017.Feedback;
import cz.devconf2017.Speaker;
import cz.devconf2017.Talk;

public class TalkBusiness {

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

    public CharSequence printSpeakers(@Nullable Collection<Speaker> speakers) {
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
}