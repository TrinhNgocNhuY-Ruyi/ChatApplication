package android.example.appchoco;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;

import com.google.firebase.auth.FirebaseAuth;
import com.ismaeldivita.chipnavigation.ChipNavigationBar;

import android.example.appchoco.fragment.FragmentChat;
import android.example.appchoco.fragment.FragmentProfile;
import android.example.appchoco.fragment.FragmentUser;
import android.view.Menu;
import android.view.MenuItem;


public class MainActivity extends AppCompatActivity {
    private static final String TAG = MainActivity.class.getSimpleName() ;
    ChipNavigationBar Navigation_Bottom;
    FragmentManager fragmentManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Navigation_Bottom = findViewById(R.id.Navigation_Bottom);

        if (savedInstanceState == null)
        {
            Navigation_Bottom.setItemEnabled(R.id.chat,true);
            fragmentManager = getSupportFragmentManager();
            FragmentChat fragmentChats = new FragmentChat();
            fragmentManager.beginTransaction()
                    .replace(R.id.fragment_container,fragmentChats).commit();
        }

        Navigation_Bottom.setOnItemSelectedListener(new ChipNavigationBar.OnItemSelectedListener() {
            @Override
            public void onItemSelected(int i) {
                Fragment fragment = null;

                switch (i)
                {
                    case R.id.chat:
                        fragment = new FragmentChat();
                        break;
                    case R.id.user:
                        fragment = new FragmentUser();
                        break;
                    case R.id.profile:
                        fragment = new FragmentProfile();
                        break;

                }

                if (fragment != null )
                {
                    fragmentManager = getSupportFragmentManager();
                    fragmentManager.beginTransaction()
                            .replace(R.id.fragment_container, fragment).commit();
                }
                else {
                    Log.e(TAG, "Error creating fragment");
                }
            }
        });


    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.top_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()){

            case  R.id.logout:
                FirebaseAuth.getInstance().signOut();
                startActivity(new Intent(MainActivity.this, LoginActivity.class).setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP));
                return true;
        }

        return false;
    }

}