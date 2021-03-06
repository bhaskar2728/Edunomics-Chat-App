package com.devdroid.edunomics.Adapters;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.devdroid.edunomics.ChatActivity;
import com.devdroid.edunomics.Model.Chats;
import com.devdroid.edunomics.Model.User;
import com.devdroid.edunomics.R;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.squareup.picasso.Picasso;

import org.w3c.dom.Text;

import java.util.ArrayList;

import de.hdodenhof.circleimageview.CircleImageView;

public class DisplayUsersAdapter extends RecyclerView.Adapter<DisplayUsersAdapter.ViewHolder> implements Filterable {

    private Context context;
    private ArrayList<User> userArrayList;
    private ArrayList<User> userArrayListFull;
    String theLastMessage;

    public DisplayUsersAdapter(Context context,ArrayList<User> arrayList){
        this.context = context;
        this.userArrayList = arrayList;
        userArrayListFull = new ArrayList<User>(userArrayList);
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.custom_row_user,parent,false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull final ViewHolder holder, int position) {

        final User user = userArrayList.get(position);

        holder.username.setText(user.getUsername());
        holder.email.setText(user.getEmail());

        lastMessage(user.getID(),holder.last_msg);

        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(context, ChatActivity.class);
                intent.putExtra("userid",user.getID());
                intent.putExtra("status",user.getStatus());
                context.startActivity(intent);
            }
        });

        StorageReference profileRef = FirebaseStorage.getInstance().getReference().child("users/"+user.getID()+".jpg");
        profileRef.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
            @Override
            public void onSuccess(Uri uri) {
                Picasso.get().load(uri).into(holder.imgProfile);
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Log.d("Profile:",e.toString());
            }
        });


    }

    @Override
    public int getItemCount() {
        return userArrayList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder{

        TextView username;
        TextView email;
        TextView last_msg;
        CircleImageView imgProfile;

        public ViewHolder(@NonNull View itemView) {

            super(itemView);
            username = itemView.findViewById(R.id.username);
            email = itemView.findViewById(R.id.email);
            last_msg = itemView.findViewById(R.id.last_msg);
            imgProfile = itemView.findViewById(R.id.profile_img);
        }
    }

    @Override
    public Filter getFilter() {
        return UserFilter;
    }

    public Filter UserFilter = new Filter() {
        @Override
        protected FilterResults performFiltering(CharSequence constraint) {
            ArrayList<User> filteredList = new ArrayList<>();
            if(constraint == null || constraint.length() == 0){
                filteredList.addAll(userArrayListFull);
            }
            else{
                String filterPattern= constraint.toString().toLowerCase().trim();

                for(User user: userArrayListFull){
                    if(user.getUsername().toLowerCase().contains(filterPattern)){
                        filteredList.add(user);
                    }
                }
            }
            FilterResults results = new FilterResults();
            results.values = filteredList;

            return results;
        }

        @Override
        protected void publishResults(CharSequence constraint, FilterResults results) {

            userArrayList.clear();
            userArrayList.addAll((ArrayList<User>) results.values);
            notifyDataSetChanged();
        }
    };
    private void lastMessage(final String userid,final TextView last_msg){
        theLastMessage = "default";
        final FirebaseUser firebaseUser = FirebaseAuth.getInstance().getCurrentUser();
        DatabaseReference reference = FirebaseDatabase.getInstance().getReference("Messages");

        reference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                for(DataSnapshot snapshot: dataSnapshot.getChildren()){
                    Chats chats = snapshot.getValue(Chats.class);
                    if(firebaseUser!=null){
                        if(chats.getReceiver().equals(firebaseUser.getUid()) && chats.getSender().equals(userid) ||
                                chats.getReceiver().equals(userid) && chats.getSender().equals(firebaseUser.getUid())){
                            theLastMessage = chats.getMessage();
                        }
                    }
                }

                switch (theLastMessage){
                    case "default":
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