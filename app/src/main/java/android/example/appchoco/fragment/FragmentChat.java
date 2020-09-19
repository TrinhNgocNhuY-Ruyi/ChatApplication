package android.example.appchoco.fragment;

import android.app.ActionBar;
import android.app.Activity;
import android.content.Intent;
import android.example.appchoco.ChatFragmnet;
import android.example.appchoco.MainActivity;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.example.appchoco.R;

import com.bumptech.glide.Glide;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

import android.example.appchoco.adapter.UserAdapter;

import android.example.appchoco.model.Chatlist;
import android.example.appchoco.model.User;
import android.widget.TextView;

import de.hdodenhof.circleimageview.CircleImageView;

public class FragmentChat extends Fragment {
    CircleImageView avatarUser;
    TextView UserName;
    Intent intent;
    String UserID;

    RecyclerView listChat;

     UserAdapter userAdapter;
     List<User> userData;

    FirebaseUser fuser;
    DatabaseReference reference;

    List<Chatlist> usersList;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_chat, container, false);

        listChat = view.findViewById(R.id.listChat);
        listChat.setHasFixedSize(true);
        listChat.setLayoutManager(new LinearLayoutManager(getContext()));

        avatarUser = view.findViewById(R.id.avatarUser);
        UserName = view.findViewById(R.id.UserName);

        Toolbar toolbar = view.findViewById(R.id.toolbarChat);
        

        fuser = FirebaseAuth.getInstance().getCurrentUser();

        usersList = new ArrayList<>();

        reference = FirebaseDatabase.getInstance().getReference("Chatlist").child(fuser.getUid());
        reference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                usersList.clear();
                for (DataSnapshot snapshot : dataSnapshot.getChildren()){
                    Chatlist chatlist = snapshot.getValue(Chatlist.class);
                    usersList.add(chatlist);
                }
                chatList();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
        return view;
    }

    private void chatList() {
        userData = new ArrayList<>();
        reference = FirebaseDatabase.getInstance().getReference("User");
        reference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                userData.clear();
                for (DataSnapshot snapshot : dataSnapshot.getChildren()){
                    User user = snapshot.getValue(User.class);
                    for (Chatlist chatlist : usersList){
                        if (user.getID().equals(chatlist.getID())){
                            userData.add(user);
                        }
                    }
                }
                userAdapter = new UserAdapter(getContext(),userData,true);
                listChat.setAdapter(userAdapter);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }

}