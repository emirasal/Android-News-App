package com.example.cs310_hw2;


import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.viewpager2.adapter.FragmentStateAdapter;

public class VPAdapter extends FragmentStateAdapter {


    public VPAdapter(@NonNull FragmentActivity fragmentActivity) {
        super(fragmentActivity);
    }

    @NonNull
    @Override
    public Fragment createFragment(int position) {

        switch (position){
            case 0:
                return new EconomicsFragment();
            case 1:
                return new SportsFragment();
            case 2:
                return new PoliticsFragment();
        }

        return new EconomicsFragment();
    }

    @Override
    public int getItemCount() {
        return 3;
    }
}
