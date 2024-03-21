package com.example.formula_world;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.Fragment;
import Fragments.HomeFragment;
import Fragments.RankingFragment;
import Fragments.BettingFragment;
import com.example.formula_world.R;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import com.example.formula_world.databinding.ActivityMainBinding;

public class MainActivity extends AppCompatActivity {

    ActivityMainBinding binding;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        loadFragment(new HomeFragment());

        binding.bottomNavigation.setOnItemSelectedListener(item -> {
            int itemId = item.getItemId();
            if (itemId == R.id.action_globe) {
                loadFragment(new BettingFragment());
                return true;
            } else if (itemId == R.id.action_ranking) {
                loadFragment(new RankingFragment());
                return true;
            } else if (itemId == R.id.action_betting) {
                loadFragment(new BettingFragment());
                return true;
            }
            return false;
        });
    }

        private void loadFragment(Fragment fragment) {
        getSupportFragmentManager().beginTransaction()
                .replace(R.id.fragment_container, fragment)
                .commit();
    }
}