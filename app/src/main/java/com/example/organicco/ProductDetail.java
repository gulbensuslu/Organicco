package com.example.organicco;

import android.content.Intent;
import android.os.Bundle;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.google.android.material.floatingactionbutton.FloatingActionButton;


public class ProductDetail extends Fragment {

    String product_name;
    String image;
    String kind;
    String region;
    float product_price;
    String share_productDetail;

    public ProductDetail( String product_name, float product_price,String image, String kind, String region ) {
        this.product_name = product_name;
        this.image = image;
        this.kind = kind;
        this.region = region;
        this.product_price = product_price;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View v= inflater.inflate(R.layout.fragment_product_detail, container, false);
       Toolbar toolbar = (Toolbar) v.findViewById(R.id.toolbar);
        ((MainActivity)getActivity()).setSupportActionBar(toolbar);
        FloatingActionButton FAB= v.findViewById(R.id.share_id);

        ImageView imageholder= v.findViewById(R.id.ProductDetail_img_id);
        TextView nameholder =v.findViewById(R.id.name_id);
        TextView priceholder =v.findViewById(R.id.price_id);
        TextView kindholder=v.findViewById(R.id.kind_id);
        TextView regionholder=v.findViewById(R.id.region_id);

        nameholder.setText(product_name);
        priceholder.setText(String.valueOf(product_price)+"₺");
        kindholder.setText(kind);
        regionholder.setText(region);
        Glide.with(getContext()).load(image).into(imageholder);

        FAB.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                share_productDetail =getResources().getText(R.string.ProductName)+"\n"+nameholder.getText().toString()
                                      +"\n"+getResources().getText(R.string.ProductPrice)+"\n"+priceholder.getText().toString()
                                      +"\n"+getResources().getText(R.string.ProductKind) +"\n"+kindholder.getText().toString()
                                      +"\n"+getResources().getText(R.string.ProductRegion)+"\n"+regionholder.getText().toString();
                Intent shareIntent = new Intent();
                shareIntent.setAction(Intent.ACTION_SEND);
                shareIntent.putExtra(Intent.EXTRA_TEXT,share_productDetail);
                shareIntent.setType("text/plain");
                startActivity(shareIntent);
            }
        });
        return v;
    }
}



