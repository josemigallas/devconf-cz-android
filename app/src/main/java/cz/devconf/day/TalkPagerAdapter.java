package cz.devconf.day;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;

import cz.devconf.RoomTalkFragment;
import cz.devconf.home.MainActivity;

class TalkPagerAdapter extends FragmentStatePagerAdapter {

    private int day;

    TalkPagerAdapter(FragmentManager fm, int day) {
        super(fm);
        this.day = day;
    }

    @Override
    public Fragment getItem(int position) {
        //Based upon the position you can call the fragment you need here
        //here i have called the same fragment for all the instances
        Fragment fragment = new RoomTalkFragment();

        Bundle args = new Bundle();
        args.putInt("index", position);
        args.putInt("day", day);
        fragment.setArguments(args);

        return fragment;
    }


    @Override
    public int getCount() {
        return MainActivity.ROOMDB.getRooms().size();
    }

    @Override
    public CharSequence getPageTitle(int position) {
        return MainActivity.ROOMDB.getRoomName(position);
    }
}
