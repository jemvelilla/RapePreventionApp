package com.build1.rapepreventionapp;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.util.AbstractQueue;
import java.util.ArrayList;
import java.util.List;

public class BottomNavigation extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_bottom_navigation);

        //for link clicked
        final Intent intent = getIntent();
        final String action = intent.getAction();

        if (Intent.ACTION_VIEW.equals(action)) {
            DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference();
            final List<String> segments = intent.getData().getPathSegments();
            for (int i=0; i< segments.size(); i++) {
                Log.v("data", segments.get(i));
            }
//
//            Query query = databaseReference.child(segments.get(0)).orderByChild("first_name");
//            query.addListenerForSingleValueEvent(new ValueEventListener() {
//                @Override
//                public void onDataChange(DataSnapshot dataSnapshot) {
//                    if (dataSnapshot.exists()) {
//                        // dataSnapshot is the "issue" node with all children with id 0
//                        for (DataSnapshot issue : dataSnapshot.getChildren()) {
//                            // do something with the individual "issues"
//                            Log.v("data", issue.getValue().toString());
//                        }
//                    }
//                }
//
//                @Override
//                public void onCancelled(DatabaseError databaseError) {
//
//                }
//            });

        }


        setupNavigationView();
    }

    private void setupNavigationView() {
        BottomNavigationView bottomNavigationView = (BottomNavigationView) findViewById(R.id.bottomNavigation);
        if (bottomNavigationView != null) {

            // Select first menu item by default and show Fragment accordingly.
            Menu menu = bottomNavigationView.getMenu();
            selectFragment(menu.getItem(0));

            // Set action to perform when any menu-item is selected.
            bottomNavigationView.setOnNavigationItemSelectedListener(
                    new BottomNavigationView.OnNavigationItemSelectedListener() {
                        @Override
                        public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                            selectFragment(item);
                            return false;
                        }
                    });
        }
    }

    /**
     * Perform action when any item is selected.
     *
     * @param item Item that is selected.
     */
    protected void selectFragment(MenuItem item) {

        item.setChecked(true);

        switch (item.getItemId()) {
            case R.id.homeBtn:
                pushFragment(new Map());
                Toast.makeText(getApplicationContext(),"Map", Toast.LENGTH_SHORT).show();
                break;
            case R.id.contactsBtn:
                // Action to perform when Bag Menu item is selected.
                pushFragment(new Contacts());
                Toast.makeText(getApplicationContext(),"Contacts", Toast.LENGTH_SHORT).show();
                break;
            case R.id.profileBtn:
                // Action to perform when Account Menu item is selected.
                pushFragment(new Profile());
                Toast.makeText(getApplicationContext(),"Profile", Toast.LENGTH_SHORT).show();
                break;
        }


    }

    /**
     * Method to push any fragment into given id.
     *
     * @param fragment An instance of Fragment to show into the given id.
     */
    protected void pushFragment(Fragment fragment) {
        if (fragment == null)
            return;

        FragmentManager fragmentManager = getSupportFragmentManager();
        if (fragmentManager != null) {
            FragmentTransaction ft = fragmentManager.beginTransaction();
            if (ft != null) {
                ft.replace(R.id.rootLayout, fragment);
                ft.commit();
            }
        }
    }
}
