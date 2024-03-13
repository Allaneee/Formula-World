package Fragments;

import android.graphics.Typeface;
import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.viewpager.widget.ViewPager;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.formula_world.R;

import Adapter.RankingPagerAdapter;

public class RankingFragment extends Fragment {

    private ViewPager viewPager;
    private RankingPagerAdapter rankingPagerAdapter;
    private TextView tvPilotes, tvConstructeurs;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_ranking, container, false);

        viewPager = view.findViewById(R.id.viewPager);
        tvPilotes = view.findViewById(R.id.tvPilotes);
        tvConstructeurs = view.findViewById(R.id.tvConstructeurs);

        rankingPagerAdapter = new RankingPagerAdapter(getChildFragmentManager());
        viewPager.setAdapter(rankingPagerAdapter);

        // Mettre à jour les onglets lors du changement de page
        viewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
                // Cette méthode peut être laissée vide
            }

            @Override
            public void onPageSelected(int position) {
                updateTabSelection(position);
            }

            @Override
            public void onPageScrollStateChanged(int state) {
                // Cette méthode peut être laissée vide
            }
        });

        // Configurer les clics sur les onglets pour changer la page du ViewPager
        tvPilotes.setOnClickListener(v -> viewPager.setCurrentItem(0));
        tvConstructeurs.setOnClickListener(v -> viewPager.setCurrentItem(1));

        // Sélection initiale de l'onglet Pilotes
        updateTabSelection(0);

        return view;
    }

    private void updateTabSelection(int position) {
        tvPilotes.setTypeface(null, position == 0 ? Typeface.BOLD : Typeface.NORMAL);
        tvConstructeurs.setTypeface(null, position == 1 ? Typeface.BOLD : Typeface.NORMAL);
    }
}