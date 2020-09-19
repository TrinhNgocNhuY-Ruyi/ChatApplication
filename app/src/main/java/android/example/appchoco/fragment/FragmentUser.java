package android.example.appchoco.fragment;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.example.appchoco.R;
import android.widget.EditText;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

import android.example.appchoco.adapter.UserAdapter;
import android.example.appchoco.model.User;


public class FragmentUser extends Fragment {

    RecyclerView listUser;
    UserAdapter userAdapter;
    List<User> userData;
    EditText search_users;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_user, container, false);

        listUser = view.findViewById(R.id.listUser);
        listUser.setHasFixedSize(true);
        listUser.setLayoutManager(new LinearLayoutManager(getContext()));



        userData = new ArrayList<>();

        search_users = view.findViewById(R.id.search_users);
        search_users.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                searchUsers(charSequence.toString().toLowerCase());
            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });

        readUsers();

        return view;
    }

    private void readUsers()
    {

        final FirebaseUser firebaseUser = FirebaseAuth.getInstance().getCurrentUser();
        DatabaseReference reference = FirebaseDatabase.getInstance().getReference("User");

        reference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if (search_users.getText().toString().equals("")) {
                    userData.clear();
                    for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                        User user = snapshot.getValue(User.class);

                        if (!user.getID().equals(firebaseUser.getUid())) {
                            userData.add(user);
                        }

                    }

                    userAdapter = new UserAdapter(getContext(), userData, false);
                    listUser.setAdapter(userAdapter);
                }
            }

//        reference.addValueEventListener(new ValueEventListener() {
//            @Override
//            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
//                userData.clear();
//                for (DataSnapshot snapshot : dataSnapshot.getChildren())
//                {
//                    User user = snapshot.getValue(User.class);
//
//                    assert user != null;
//                    assert firebaseUser != null;
//                    if (!user.getID().equals(firebaseUser.getUid()))
//                    {
//                        userData.add(user);
//                    }
//                }
//                userAdapter = new UserAdapter(getContext(),userData, false);
//                listUser.setAdapter(userAdapter);
//            }
//
            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }

    private void searchUsers(String s) {

        final FirebaseUser fuser = FirebaseAuth.getInstance().getCurrentUser();
        Query query = FirebaseDatabase.getInstance().getReference("User").orderByChild("UserName")
                .startAt(s)
                .endAt(s+"\uf8ff");

        query.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                userData.clear();
                for (DataSnapshot snapshot : dataSnapshot.getChildren()){
                    User user = snapshot.getValue(User.class);

                    assert user != null;
                    assert fuser != null;
                    if (!user.getID().equals(fuser.getUid())){
                        userData.add(user);
                    }
                }

                userAdapter = new UserAdapter(getContext(), userData, false);
                listUser.setAdapter(userAdapter);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

    }


}