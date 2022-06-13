package com.example.organicco;

import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.viewpager.widget.ViewPager;

import android.os.IBinder;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.firebase.ui.firestore.FirestoreRecyclerOptions;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.Transaction;
import com.example.organicco.TimeService.timeBinder;

import java.sql.Time;


public class MyProfile extends Fragment {
    FirebaseFirestore FirebaseFirestore_database;
    FirebaseAuth firebaseAuth;
    EditText username;
    EditText usersurname;
    EditText phonenumber;
    TextView email;
    String uName,uSurname,uPhone,uEmail;
    String user_id;
    ViewPager viewPager;
    Button update;
    String editName;
    String editUsername;
    String editPhoneNumber;
    TimeService timeService=new TimeService();
    boolean connection=false;

    private static final String TAG = "MyActivity";
    DocumentReference documentReference;

    public MyProfile() {

    }


    public static MyProfile newInstance(String param1, String param2) {
        MyProfile fragment = new MyProfile();
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        viewPager=getActivity().findViewById(R.id.viewpager_id);

    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View v = inflater.inflate(R.layout.fragment_my_profile, container, false);

        username = v.findViewById(R.id.U_name_id);
        usersurname = v.findViewById(R.id.U_surname_id);
        phonenumber = v.findViewById(R.id.U_phone_id);
        email=v.findViewById(R.id.U_email_id);
        update=v.findViewById(R.id.update_id);
        firebaseAuth = FirebaseAuth.getInstance();
        FirebaseFirestore_database = FirebaseFirestore.getInstance();

        Intent intent=new Intent(getActivity(), TimeService.class);
        getActivity().bindService(intent,connectionTimeService, Context.BIND_AUTO_CREATE);

        update.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                UpdateProfile();
                Toast.makeText(getActivity(), getResources().getText(R.string.UpdateToast)+ timeService.getUpdateTime(),Toast.LENGTH_LONG).show();
            }
        });
        user_id = FirebaseAuth.getInstance().getCurrentUser().getUid();
            FirebaseFirestore.getInstance().collection("USER").document(user_id).get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                @Override
                public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                        if(task.isSuccessful()){
                            uName=task.getResult().getString("name");
                            uSurname=task.getResult().getString("surname");
                            uPhone=task.getResult().getString("phone");
                            uEmail=task.getResult().getString("email");

                            username.setText(uName);
                            usersurname.setText(uSurname);
                            phonenumber.setText(uPhone);
                            email.setText(uEmail);
                        }
                }
            });
        return v;
    }

    private  void UpdateProfile(){
      editName= username.getText().toString();
      editUsername=usersurname.getText().toString();
      editPhoneNumber=phonenumber.getText().toString();

        final DocumentReference refDocument = FirebaseFirestore_database.collection("USER").document(firebaseAuth.getUid());

        FirebaseFirestore_database.runTransaction(new Transaction.Function<Void>() {
            @Override
            public Void apply(Transaction transaction) throws FirebaseFirestoreException {
                DocumentSnapshot snapshot = transaction.get(refDocument);

                transaction.update(refDocument, "name",editName);
                transaction.update(refDocument, "surname", editUsername);
                transaction.update(refDocument, "phone", editPhoneNumber);

                return null;
            }
        }).addOnSuccessListener(new OnSuccessListener<Void>() {
            @Override
            public void onSuccess(Void aVoid) {
            }
        })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                    }
                });
    }
    @Override
    public void onStop(){
        super.onStop();
        viewPager.setVisibility(View.VISIBLE);
    }
    private ServiceConnection connectionTimeService= new ServiceConnection() {
        @Override
        public void onServiceConnected(ComponentName componentName, IBinder iBinder) {
            timeBinder bindertime=(timeBinder) iBinder;
            timeService =bindertime.getBoundService();
            connection=true;
        }
        @Override
        public void onServiceDisconnected(ComponentName componentName) {
                connection=false;
        }
    };
}

