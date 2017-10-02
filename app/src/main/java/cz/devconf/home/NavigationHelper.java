package cz.devconf.home;


import android.support.v4.app.FragmentManager;

import cz.devconf.HomeFragment;
import cz.devconf.R;
import cz.devconf.day.TalkFragment;
import cz.devconf.base.BaseFragment;

public class NavigationHelper {

    private final FragmentManager supportFragmentManager;

    private Section currentSection;

    public NavigationHelper(FragmentManager supportFragmentManager) {
        this.supportFragmentManager = supportFragmentManager;
    }

    public void navigate(Section section) {
        if (section == currentSection) {
            return;
        }

        currentSection = section;

        BaseFragment fragment = getBaseFragment(section);

        replaceFragment(fragment);
    }

    private BaseFragment getBaseFragment(Section section) {
        BaseFragment fragment;

        switch (section) {
            case HOME:
                fragment = HomeFragment.newInstance();
                break;

            case DAY1:
                fragment = TalkFragment.newInstance(1);
                break;

            case DAY2:
                fragment = TalkFragment.newInstance(2);
                break;

            case DAY3:
                fragment = TalkFragment.newInstance(3);
                break;

            default:
                throw new IllegalArgumentException("Section does not exist: " + section);
        }

        return fragment;
    }

    private void replaceFragment(BaseFragment fragment) {
        supportFragmentManager
                .beginTransaction()
                .replace(R.id.fragment_holder, fragment)
                .commit();
    }

    enum Section {
        HOME,
        DAY1,
        DAY2,
        DAY3,
        VENUE,
        FLOORPLAN,
        SPEAKERS,
        SOCIALEVENT,
        ABOUT,
        VOTING,
        FAVORITES
    }
}
