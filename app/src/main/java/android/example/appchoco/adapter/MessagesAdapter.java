package android.example.appchoco.adapter;

import android.content.Context;
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

import java.util.List;

import android.example.appchoco.model.Chat;


public class MessagesAdapter extends RecyclerView.Adapter<MessagesAdapter.ViewChat> {
    public static  final int MSG_TYPE_LEFT = 0;
    public static final int MSG_TYPE_RIGHT = 1;

    Context context;
    List<Chat> chatData;
    String ImageURL;
    FirebaseUser fuser;

    public MessagesAdapter(Context context, List<Chat> chatData, String imageURL) {
        this.context = context;
        this.chatData = chatData;
        ImageURL = imageURL;
    }

    @NonNull
    @Override
    public ViewChat onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        if (viewType == MSG_TYPE_RIGHT) {
            View view = LayoutInflater.from(context).inflate(R.layout.chat_item_right, parent, false);
            return new MessagesAdapter.ViewChat(view);
        } else {
            View view = LayoutInflater.from(context).inflate(R.layout.chat_item_left, parent, false);
            return new MessagesAdapter.ViewChat(view);
        }
    }

    @Override
    public void onBindViewHolder(@NonNull ViewChat holder, int position) {
        Chat chat = chatData.get(position);

        holder.show_message.setText(chat.getMessage());

        if (ImageURL.equals("default")){
            holder.avatarUser.setImageResource(R.drawable.avt3);
        } else {
            Glide.with(context).load(ImageURL).into(holder.avatarUser);
        }

        if (position == chatData.size()-1){
            if (chat.isIsseen()){
                holder.txt_seen.setText("Seen");
            } else {
                holder.txt_seen.setText("Delivered");
            }
        } else {
            holder.txt_seen.setVisibility(View.GONE);
        }

    }

    @Override
    public int getItemCount() {
        return chatData.size();
    }


    public class ViewChat extends RecyclerView.ViewHolder {
        ImageView avatarUser;
        TextView show_message,txt_seen;
        public ViewChat(@NonNull View itemView) {
            super(itemView);
            avatarUser = itemView.findViewById(R.id.profile_image);
            show_message = itemView.findViewById(R.id.show_message);
            txt_seen = itemView.findViewById(R.id.txt_seen);
        }
    }

    @Override
    public int getItemViewType(int position) {
        fuser = FirebaseAuth.getInstance().getCurrentUser();
        if (chatData.get(position).getSender().equals(fuser.getUid())){
            return MSG_TYPE_RIGHT;
        } else {
            return MSG_TYPE_LEFT;
        }
    }

}
