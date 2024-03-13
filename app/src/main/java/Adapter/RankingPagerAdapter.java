package Adapter;

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;

import Fragments.DriverFragment;
import Fragments.TeamFragment;

public class RankingPagerAdapter extends FragmentPagerAdapter {

    private static final int NUM_ITEMS = 2; // Nombre total de pages

    public RankingPagerAdapter(FragmentManager fragmentManager) {
        super(fragmentManager, BEHAVIOR_RESUME_ONLY_CURRENT_FRAGMENT);
    }

    // Retourne le fragment associé à une position spécifique.
    @Override
    public Fragment getItem(int position) {
        switch (position) {
            case 0: // Fragment #0 - Cela affichera le classement des pilotes
                return new DriverFragment();
            case 1: // Fragment #1 - Cela affichera le classement des constructeurs
                return new TeamFragment();
            default:
                return null; // Ne devrait jamais arriver
        }
    }

    // Retourne le nombre de pages
    @Override
    public int getCount() {
        return NUM_ITEMS;
    }

    // Peut être utilisé pour définir le titre de chaque page
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

