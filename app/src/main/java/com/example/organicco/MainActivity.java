 package com.example.organicco;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.DialogTitle;
import androidx.appcompat.widget.Toolbar;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;
import androidx.fragment.app.FragmentTransaction;
import androidx.viewpager.widget.ViewPager;

import android.app.NotificationManager;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;

import com.google.android.material.navigation.NavigationView;
import com.google.android.material.snackbar.Snackbar;
import com.google.android.material.tabs.TabLayout;

import java.util.ArrayList;


 public class MainActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {

     int id_item;

     TabLayout tabLayout;
     ViewPager viewPager;

     @Override
     protected void onCreate(Bundle savedInstanceState) {
         super.onCreate(savedInstanceState);
         setContentView(R.layout.activity_main);

         Toolbar tbar = findViewById(R.id.toolbar);
         setSupportActionBar(tbar);

         DrawerLayout Dlayout = findViewById(R.id.drawerLayout_id);
         ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(this, Dlayout, tbar, R.string.open_drawer, R.string.close_drawer);

         Dlayout.addDrawerListener(toggle);
         toggle.syncState();

         NavigationView navView = findViewById(R.id.navigationView_id);
         navView.setNavigationItemSelectedListener(this);

        viewPager =  findViewById(R.id.viewpager_id);
        tabLayout = findViewById(R.id.tabs_id);
        tabLayout.setupWithViewPager(viewPager);


         MyAdapter myAdapter = new MyAdapter(getSupportFragmentManager(), FragmentPagerAdapter.BEHAVIOR_RESUME_ONLY_CURRENT_FRAGMENT);
         myAdapter.addFragment(new vegetablesFruitsFragment(), (String) getResources().getText(R.string.vegetablesfruits));
         myAdapter.addFragment(new MilkFragment(),  (String) getResources().getText(R.string.milkdairyProducts));
         myAdapter.addFragment(new MarmaladeFragment(), (String) getResources().getText(R.string.Marmalade));
         viewPager.setAdapter(myAdapter);

     }

     public boolean onCreateOptionsMenu(Menu menu){

         MenuInflater inflaterMenu = getMenuInflater();
         inflaterMenu.inflate(R.menu.exit_menu,menu);

         return true;
     }

     @Override
     public boolean onOptionsItemSelected(MenuItem item){
         switch (item.getItemId()){
             case R.id.exit_id:
                 System.exit(1);
                 break;
             case R.id.contact_id:
                 AlertDialog.Builder dialog = new AlertDialog.Builder(this);
                 dialog.setTitle(getResources().getText(R.string.contactUs));
                 dialog.setMessage(getResources().getText(R.string.ContactInfo)).setCancelable(false);
                 dialog.setPositiveButton(getResources().getText(R.string.OK), new DialogInterface.OnClickListener() {
                     @Override
                     public void onClick(DialogInterface dialogInterface, int i) {
                         dialog.setCancelable(true);
                     }
                 });
                 dialog.show();
         }
         return super.onOptionsItemSelected(item);
     }


     @Override
     public boolean onNavigationItemSelected(MenuItem i) {

         id_item = i.getItemId();
         Fragment f = null;
         Intent intent= null;
         switch (id_item) {
             case R.id.Home_id:
                 intent= new Intent(this,MainActivity.class);
                 break;
             case R.id.myProfile_id:
                 f = new MyProfile();
                 break;
             case R.id.myCart_id:
                 f = new MyCart();
                 break;
             case R.id.aboutItem_id:
                 f = new AboutUs();
                 break;
             case R.id.privacyPolicyItem_id:
                 f = new PrivacyPolicy();
                 break;
         }
         if (f != null) {

             FragmentTransaction fTransaction = getSupportFragmentManager().beginTransaction();
             fTransaction.replace(R.id.frameofMain_id, f);
             fTransaction.addToBackStack(null);
             fTransaction.commit();
         }else{
              startActivity(intent);
         }

         DrawerLayout Dlayout = findViewById(R.id.drawerLayout_id);
         Dlayout.closeDrawer(GravityCompat.START);
         return true;
     }

     @Override
     public void onBackPressed() {
         DrawerLayout Dlayout = findViewById(R.id.drawerLayout_id);
         if (Dlayout.isDrawerOpen(GravityCompat.START)) {
             Dlayout.closeDrawer(GravityCompat.START);
         } else {
             super.onBackPressed();
         }
     }

     public static class MyAdapter extends FragmentPagerAdapter{

         private final ArrayList<Fragment> FragmentArrayList =new ArrayList<>();
         private final ArrayList<String>fragmentTitle=new ArrayList<>();
         public MyAdapter(@NonNull FragmentManager fm, int behavior) {
             super(fm, behavior);
         }

         @NonNull
         @Override
         public Fragment getItem(int position) {
             return FragmentArrayList.get(position);
         }

         @Override
         public int getCount() {
             return FragmentArrayList.size();
         }

        public void addFragment(Fragment fragment,String title){
             FragmentArrayList.add(fragment);
             fragmentTitle.add(title);
        }

        @NonNull
         @Override
         public CharSequence getPageTitle(int position){
             return fragmentTitle.get(position);
        }
     }
 }

