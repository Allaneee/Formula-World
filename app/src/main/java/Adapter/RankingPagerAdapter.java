package Adapter;

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;

import Fragments.DriverRankingFragment;
import Fragments.TeamFragment;

public class RankingPagerAdapter extends FragmentPagerAdapter {

    private static final int NUM_ITEMS = 2;

    public RankingPagerAdapter(FragmentManager fragmentManager) {
        super(fragmentManager, BEHAVIOR_RESUME_ONLY_CURRENT_FRAGMENT);
    }

    @Override
    public Fragment getItem(int position) {
        switch (position) {
            case 0:
                return new DriverRankingFragment();
            case 1:
                return new TeamFragment();
            default:
                return null;
        }
    }


    @Override
    public int getCount() {
        return NUM_ITEMS;
    }


    @Nullable
    @Override
    public CharSequence getPageTitle(int position) {
        switch (position) {
            case 0:
                return "Pilotes";
            case 1:
                return "Constructeurs";
            default:
                return null;
        }
    }
}

