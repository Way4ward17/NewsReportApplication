package com.theway4wardacademy.report.Activity;


        import android.Manifest;
        import android.content.pm.PackageManager;
        import android.os.Build;
        import android.os.Bundle;
        import android.view.Menu;
        import android.view.MenuItem;

        import androidx.annotation.NonNull;
        import androidx.appcompat.app.AppCompatActivity;
        import androidx.core.app.ActivityCompat;
        import androidx.core.content.ContextCompat;
        import androidx.fragment.app.Fragment;
        import androidx.fragment.app.FragmentManager;
        import androidx.navigation.NavController;
        import androidx.navigation.Navigation;
        import androidx.navigation.ui.AppBarConfiguration;
        import androidx.navigation.ui.NavigationUI;


        import com.google.android.material.bottomnavigation.BottomNavigationView;
        import com.theway4wardacademy.report.Fragment.HomeFragment;
        import com.theway4wardacademy.report.Fragment.NotificationsFragment;
        import com.theway4wardacademy.report.Fragment.TrendingTop;
        import com.theway4wardacademy.report.R;


public class Mainbottom extends AppCompatActivity {
    private static final int PERMISSION_STORAGE_CODE = 1000;

    final Fragment fragment1 = new HomeFragment();
    final Fragment fragment2 = new TrendingTop();
    final Fragment fragment5 = new NotificationsFragment();

    final FragmentManager fm = getSupportFragmentManager();
    Fragment active = fragment1;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);


        if (ContextCompat.checkSelfPermission(this, Manifest.permission.READ_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.READ_EXTERNAL_STORAGE,Manifest.permission.WRITE_EXTERNAL_STORAGE,Manifest.permission.CAMERA}, 1);
        }



        setContentView(R.layout.activity_mainbottom);
        BottomNavigationView navigation = findViewById(R.id.nav_view);



        AppBarConfiguration appBarConfiguration = new AppBarConfiguration.Builder(
                R.id.navigation_home, R.id.search,R.id.navigation_library , R.id.navigation_notifications)
                .build();
        NavController navController = Navigation.findNavController(this, R.id.nav_host_fragment);
        // NavigationUI.setupActionBarWithNavController(this, navController, appBarConfiguration);
        NavigationUI.setupWithNavController(navigation, navController);


    }


    @Override
    protected void onRestart() {

        super.onRestart();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.bottom_nav_menu, menu);
        return super.onCreateOptionsMenu(menu);
    }
}
