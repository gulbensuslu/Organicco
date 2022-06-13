package com.example.organicco;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.firebase.ui.firestore.FirestoreRecyclerAdapter;
import com.firebase.ui.firestore.FirestoreRecyclerOptions;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;

import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QuerySnapshot;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.HashMap;


public class vegetablesFruitsFragment extends Fragment {

    FirebaseStorage firebaseStorage;
    StorageReference storageReference;
    private static final String TAG = "MyActivity";
    FirebaseFirestore FirebaseFirestore_database;
    RecyclerView recylerView;
    FirestoreRecyclerAdapter firestoreRecyclerAdapter;
    FirebaseAuth firebaseAuth;


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);



    }



    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View v= inflater.inflate(R.layout.fragment_vegetables_fruits, container, false);

        recylerView=v.findViewById(R.id.recyclerView);
        FirebaseFirestore_database=FirebaseFirestore.getInstance();
        firebaseAuth=FirebaseAuth.getInstance();

        Query query= FirebaseFirestore_database.collection("VegetablesAndFruits");
        FirestoreRecyclerOptions<Products> options =new FirestoreRecyclerOptions.Builder<Products>()
                .setQuery(query,Products.class)
                .build();



        firestoreRecyclerAdapter= new FirestoreRecyclerAdapter<Products, VandFViewHolder>(options) {
            @NonNull
            @Override
            public VandFViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
                View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.products_cardview, parent, false);

                return new VandFViewHolder(view);
            }

            @Override
            protected void onBindViewHolder(@NonNull VandFViewHolder holder, int position, @NonNull Products model) {

                Glide.with(holder.productImage.getContext()).load(model.getImage()).into(holder.productImage);
                holder.productName.setText(model.product_name);
                holder.productPrice.setText(String.valueOf(model.product_price)+"₺");



                holder.add_cart.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        final HashMap<String,Object> cartMap=new HashMap<>();
                        cartMap.put("image",model.image);
                        cartMap.put("product_name",model.product_name);
                        cartMap.put("product_price",model.product_price);
                        cartMap.put("kind",model.kind);

                        FirebaseFirestore_database.collection("Cart").document(firebaseAuth.getCurrentUser().getUid())
                                .collection("current").add(cartMap).addOnCompleteListener(new OnCompleteListener<DocumentReference>() {
                            @Override
                            public void onComplete(@NonNull Task<DocumentReference> task) {
                                Toast.makeText(getActivity(),getResources().getText(R.string.added),Toast.LENGTH_SHORT).show();
                            }
                        });

                    }
                });
                holder.productImage.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                      AppCompatActivity activity= (AppCompatActivity) view.getContext();
                      activity.getSupportFragmentManager().beginTransaction().replace(R.id.drawerLayout_id,new ProductDetail(model.getProduct_name(),model.getProduct_price(),model.getImage(),model.getKind(),model.getRegion())).addToBackStack(null).commit();
                    }
                });


            }
        };
        recylerView.setHasFixedSize(true);
        GridLayoutManager gridLayoutManager=new GridLayoutManager(getActivity(),2);
        recylerView.setLayoutManager(gridLayoutManager);
        recylerView.setAdapter(firestoreRecyclerAdapter);

        return v;
    }

    private class VandFViewHolder extends RecyclerView.ViewHolder {

        ImageView productImage;
        TextView productName;
        TextView productPrice;
        ImageView add_cart;

        public VandFViewHolder(@NonNull View itemView) {
            super(itemView);
            productName=itemView.findViewById(R.id.product_name);
            productImage= itemView.findViewById(R.id.product_image);
            productPrice=itemView.findViewById(R.id.priceOfProduct);
            add_cart=itemView.findViewById(R.id.addBasket_id);

        }
    }

    @Override
    public void onStop(){
        super.onStop();
        firestoreRecyclerAdapter.stopListening();
    }
    @Override
    public void onStart(){
        super.onStart();
        firestoreRecyclerAdapter.startListening();
    }


}



