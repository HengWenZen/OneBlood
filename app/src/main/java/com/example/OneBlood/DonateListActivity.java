package com.example.OneBlood;
import androidx.fragment.app.Fragment;

public class DonateListActivity extends SingleFragmentActivity{

    protected Fragment createFragment(){
        return new DonateLocationFragment();
    }
}
