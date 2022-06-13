package com.example.organicco;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.localbroadcastmanager.content.LocalBroadcastManager;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.viewpager.widget.ViewPager;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.firebase.ui.firestore.FirestoreRecyclerAdapter;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.snackbar.Snackbar;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class MyCart extends Fragment {

    FirebaseAuth firebaseAuth;
    FirebaseFirestore FirebaseFirestore_database;
    RecyclerView recyclerView;
    List<Products> cartHolderList;
    cartViewAdapter cartViewAdapter;
    ViewPager viewPager;
    private static final String TAG = "MyActivity";
    Button confirm_button;
    TextView order_price;

    public MyCart() {
    }


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        viewPager=getActivity().findViewById(R.id.viewpager_id);

        }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View v= inflater.inflate(R.layout.fragment_my_cart, container, false);

        FirebaseFirestore_database=FirebaseFirestore.getInstance();
        firebaseAuth=FirebaseAuth.getInstance();
        recyclerView=v.findViewById(R.id.recyclerView_cart);
        order_price = v.findViewById(R.id.totalPrice_id);
        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        cartHolderList=new ArrayList<>();
        cartViewAdapter= new cartViewAdapter(getActivity(),cartHolderList);
        recyclerView.setAdapter(cartViewAdapter);
        confirm_button=v.findViewById(R.id.cart_button_id);

        confirm_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                AlertDialog.Builder dialog = new AlertDialog.Builder(getActivity());
                dialog.setTitle(getResources().getText(R.string.DialogTitle));
                dialog.setMessage(getResources().getText(R.string.DialogContent)).setCancelable(false);
                dialog.setPositiveButton(getResources().getText(R.string.YES), new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        Snackbar.make(view,getResources().getText(R.string.Order),Snackbar.LENGTH_LONG).setAction("",null).show();
                    }
                });
                dialog.setNegativeButton(getResources().getText(R.string.NO), new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        dialog.setCancelable(true);

                    }
                });

                dialog.show();
            }
        });

        FirebaseFirestore_database.collection("Cart").document(firebaseAuth.getCurrentUser().getUid())
                .collection("current").get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
            @Override
            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                if (task.isSuccessful()) {
                    for(DocumentSnapshot dS : task.getResult().getDocuments()){
                        Products products=dS.toObject(Products.class);
                        cartHolderList.add(products);
                        cartViewAdapter.notifyDataSetChanged();
                    }
                calculateTotalPrice(cartHolderList);
                }
            }


        });
        return v;
    }

    private void calculateTotalPrice(List<Products> cartHolderList) {
        float totalPrice = 0;
        for(Products products:cartHolderList){
            totalPrice += products.getProduct_price();
            order_price.setText(String.valueOf(totalPrice)+ "₺");
        }
    }

    public class cartViewAdapter extends RecyclerView.Adapter<cartViewAdapter.ViewHolder> {

        Context context;
        List<Products> cartHolderList;


        public cartViewAdapter(Context context, List<Products> cartHolderList) {
            this.context = context;
            this.cartHolderList = cartHolderList;
        }

        public class ViewHolder extends RecyclerView.ViewHolder {
            ImageView orderProduct_image;
            TextView orderProduct_name;
            TextView orderProduct_price;
            TextView orderProduct_kind;


            public ViewHolder(@NonNull View itemView) {
                super(itemView);
                orderProduct_image = itemView.findViewById(R.id.product_image_id);
                orderProduct_name = itemView.findViewById(R.id.product_nameView_id);
                orderProduct_kind = itemView.findViewById(R.id.product_kindView_id);
                orderProduct_price = itemView.findViewById(R.id.price_view_id);

            }
        }

        @NonNull
        @Override
        public cartViewAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            return new ViewHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.cart_cardview, parent, false));
        }

        @Override
        public void onBindViewHolder(@NonNull cartViewAdapter.ViewHolder holder, int position) {

            Glide.with(holder.orderProduct_image.getContext()).load(cartHolderList.get(position).getImage()).into(holder.orderProduct_image);
            holder.orderProduct_name.setText(cartHolderList.get(position).getProduct_name());
            holder.orderProduct_price.setText(String.valueOf(cartHolderList.get(position).getProduct_price()) + "₺");
            holder.orderProduct_kind.setText(cartHolderList.get(position).getKind());
        }

        @Override
        public int getItemCount() {
            return cartHolderList.size();
        }

    }
    @Override
    public void onStop(){
        super.onStop();
        viewPager.setVisibility(View.VISIBLE);
    }

}

