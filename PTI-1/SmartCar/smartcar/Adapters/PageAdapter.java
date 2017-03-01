package com.uminho.pti.smartcar.Adapters;



import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentPagerAdapter;
import com.uminho.pti.smartcar.Fragments.MapFragment;
import com.uminho.pti.smartcar.Fragments.RoutesFragment;
import com.uminho.pti.smartcar.Fragments.EventsFragment;



public class PageAdapter extends FragmentPagerAdapter {

    private final Bundle Bundle;

    public PageAdapter(android.support.v4.app.FragmentManager fm,Bundle data)
    {
        super(fm);
        Bundle = data;
    }


    @Override
    public android.support.v4.app.Fragment getItem(int index) {
        switch (index) {
            case 0:
                return MapFragment.getInstance();
            case 1:
                String email = Bundle.getString("email");
                Bundle bundle = new Bundle();
                bundle.putString("email",email);
                Fragment fragment = new RoutesFragment();
                fragment.setArguments(bundle);
                return fragment;
            case 2:
                int route_id = Bundle.getInt("route_id");
                Bundle bundle1 = new Bundle();
                bundle1.putInt("route_id",route_id);
                Fragment fragment1 = new EventsFragment();
                fragment1.setArguments(bundle1);
                return fragment1;
        }

        return null;
    }

    @Override
    public int getCount() {
        return 3;
    }

}