package co.example.hp.myapplication;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.google.android.material.tabs.TabLayout;

import androidx.fragment.app.Fragment;
import androidx.viewpager.widget.ViewPager;

public class DashboardMainFragment extends Fragment {
    private TabAdabterMainActivity adapter;
    private TabLayout tabLayout;
    private ViewPager viewPager;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_dashboard_main, container, false);


        viewPager = (ViewPager) view.findViewById(R.id.viewpager_mainactivity);
        tabLayout = (TabLayout) view.findViewById(R.id.tab_layout);

        adapter = new TabAdabterMainActivity(getParentFragmentManager());
        adapter.addFragment(new TabDashboardFragment(), "Dashboard");
        adapter.addFragment(new TabCarsFragment(), getString(R.string.Cars_main));
        adapter.addFragment(new TabServiceCenterFragment(), getString(R.string.Service_Centers_main));
        viewPager.setAdapter(adapter);
        tabLayout.setupWithViewPager(viewPager);

        return view;
    }
}
