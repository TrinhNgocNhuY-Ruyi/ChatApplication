package android.example.appchoco.adapter;

import android.content.Context;
import android.content.Intent;
import android.example.appchoco.MessgagesActivity;
import android.example.appchoco.R;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.List;

import android.example.appchoco.model.Chat;
import android.example.appchoco.model.User;

public class UserAdapter extends RecyclerView.Adapter<UserAdapter.ViewUser> {
    Context context;
    List<User> userData;
    boolean ischat;
    String theLastMessage;

    public UserAdapter(Context context, List<User> userData, boolean ischat) {
        this.context = context;
        this.userData = userData;
        this.ischat = ischat;
    }

    @NonNull
    @Override
    public ViewUser onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View viewholder = LayoutInflater.from(context).inflate(R.layout.item_user, parent, false);
        ViewUser vuser = new ViewUser(viewholder);

        return vuser;
    }

    @Override
    public void onBindViewHolder(@NonNull ViewUser holder, int position) {
        final User user = userData.get(position);
        holder.userName.setText(user.getUserName());
        if (user.getImageURL().equals("default")){
            holder.avatarUser.setImageResource(R.drawable.avt3);
        }else {
            Glide.with(context).load(user.getImageURL()).into(holder.avatarUser);
        }

        if (ischat){
            lastMessage(user.getID(), holder.last_msg);
        } else {
            holder.last_msg.setVisibility(View.GONE);
        }



        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(context, MessgagesActivity.class);
                intent.putExtra("UserID", user.getID());
                context.startActivity(intent);
            }
        });
    }

    @Override
    public int getItemCount() {
        return userData.size();
    }


    public class ViewUser extends RecyclerView.ViewHolder {
        ImageView avatarUser;
        TextView userName;
        TextView last_msg;

        public ViewUser(@NonNull View itemView) {
            super(itemView);
            avatarUser = itemView.findViewById(R.id.avatarUser);
            userName = itemView.findViewById(R.id.UserName);
            last_msg = itemView.findViewById(R.id.last_msg);
        }
    }

    private void lastMessage(final String UserID, final TextView last_msg){
        theLastMessage = "default";
        final FirebaseUser firebaseUser = FirebaseAuth.getInstance().getCurrentUser();
        DatabaseReference reference = FirebaseDatabase.getInstance().getReference("Chats");

        reference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                for (DataSnapshot snapshot : dataSnapshot.getChildren()){
                    Chat chat = snapshot.getValue(Chat.class);
                    if (firebaseUser != null && chat != null) {
                        if (chat.getReceiver().equals(firebaseUser.getUid()) && chat.getSender().equals(UserID) ||
                                chat.getReceiver().equals(UserID) && chat.getSender().equals(firebaseUser.getUid())) {
                            theLastMessage = chat.getMessage();
                        }
                    }
                }
                switch (theLastMessage){
                    case  "default":
                        last_msg.setText("No Message");
                        break;

                    default:
                        last_msg.setText(theLastMessage);
                        break;
                }
                theLastMessage = "default";
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }
}
