package tech.dsckiet.budgetbucket;

import android.Manifest;
import android.content.pm.PackageManager;
import android.os.Build;
import android.support.annotation.NonNull;
import android.support.annotation.RequiresApi;
import android.support.design.widget.BottomNavigationView;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.widget.Toast;

import com.ittianyu.bottomnavigationviewex.BottomNavigationViewEx;

public class MainActivity extends AppCompatActivity {
    public static final int MY_PERMISSIONS_REQUEST_RECEIVE_SMS = 0;
    private BottomNavigationViewEx navigationView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //check if the permission is not granted
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.RECEIVE_SMS) != PackageManager.PERMISSION_GRANTED) {
            //if the permission is not been granted then check
            //if the user has denied the permission
            if (ActivityCompat.shouldShowRequestPermissionRationale(this, Manifest.permission.RECEIVE_SMS)) {
                // do nothing as user has denied
            } else {
                // a popup will appear asking for permission
                ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.RECEIVE_SMS}, MY_PERMISSIONS_REQUEST_RECEIVE_SMS);

            }
        }


        //bottom navigation
        navigationView = (BottomNavigationViewEx) findViewById(R.id.bottom_nav);
        navigationView.enableAnimation(false);
        navigationView.enableShiftingMode(true);
        navigationView.enableItemShiftingMode(false);
//        navigationView.setItemBackground(0,R.color.colorPrimary);
//        navigationView.setItemBackground(2,R.color.colorPrimary);
        navigationView.setOnNavigationItemSelectedListener(navListener);
        replaceFragment(new FragmentDashboard());
        getSupportFragmentManager()
                .addOnBackStackChangedListener(
                        new FragmentManager.OnBackStackChangedListener() {
                            @Override
                            public void onBackStackChanged() {
                                MainActivity.this.updateBottomNavigationTitle(MainActivity.this.getSupportFragmentManager().findFragmentById(R.id.container));
                            }
                        }
                );

    }




    //Bottom Navigation
    private BottomNavigationView.OnNavigationItemSelectedListener navListener
            = new BottomNavigationView.OnNavigationItemSelectedListener() {
        @Override
        public boolean onNavigationItemSelected(MenuItem item) {
            switch (item.getItemId()) {
                case R.id.nav_home:
                    replaceFragment(new FragmentDashboard());
                    break;
                case R.id.nav_account:
                    replaceFragment(new FragmentProfile());
                    break;
                case R.id.nav_settings:
                    replaceFragment(new FragmentSettings());
                    break;
            }
            return true;
        }

    };

    private void replaceFragment(Fragment fragment) {
        String backStateName = fragment.getClass().getName();
        FragmentManager manager = getSupportFragmentManager();
        boolean fragmentPopped = manager.popBackStackImmediate(backStateName, 0);

        if (!fragmentPopped && manager.findFragmentByTag(backStateName) == null) {
            // fragment not in back stack, create it
            FragmentTransaction fragmentTransaction = manager.beginTransaction();
            fragmentTransaction.replace(R.id.container, fragment, backStateName);
            fragmentTransaction.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_FADE);
            fragmentTransaction.addToBackStack(backStateName);
            fragmentTransaction.commit();
        }
    }

    public void updateBottomNavigationTitle(Fragment f){
        String className = f.getClass().getName();

        if(className.equals(FragmentDashboard.class.getName()))
            navigationView.getMenu().getItem(1).setChecked(true);

    }

    @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN)
    @Override
    public void onBackPressed() {
        int count = getSupportFragmentManager().getBackStackEntryCount();
        Log.e("qwer", count + "");
        if (count == 1) {
            this.finishAffinity();
        } else {
            super.onBackPressed();
        }
    }
    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        switch (requestCode)
        {
            case MY_PERMISSIONS_REQUEST_RECEIVE_SMS:
            {
                //check whether the length of grantResults is greater than 0 and is equal to PERMISSION_GRANTED
                if (grantResults.length>0 && grantResults[0] == PackageManager.PERMISSION_GRANTED){
                    //broadcast receiver works in background
                    Toast.makeText(this, "Permission Permitted", Toast.LENGTH_SHORT).show();
                }else {
                    Toast.makeText(this, "Please Permit the permission for further functioning", Toast.LENGTH_SHORT).show();
                }
            }
        }
    }
}