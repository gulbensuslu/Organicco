package com.example.organicco;

import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;

public class Login_fragment extends Fragment {

    TextView signUp;
    Button Login;
    EditText mail;
    EditText password;
    ImageView eng;
    ImageView tr;


    private FirebaseAuth firebaseAuth;
    public Login_fragment() {

    }

    public static Login_fragment newInstance(String param1, String param2) {
        return new Login_fragment();
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);

    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {


        View Login_fragment = inflater.inflate(R.layout.fragment_login, container, false);
        signUp = (TextView) Login_fragment.findViewById(R.id.signUpButton_id);
        mail = Login_fragment.findViewById(R.id.mail_edittext_id);
        password=Login_fragment.findViewById(R.id.password_edittext_id);
        signUp.setOnClickListener(this::onClick);
        firebaseAuth= FirebaseAuth.getInstance();
        Login= (Button) Login_fragment.findViewById(R.id.loginButton_id);

        eng=Login_fragment.findViewById(R.id.eng_id);
        tr=Login_fragment.findViewById(R.id.tr_id);
        LanguageManager lang=new LanguageManager(getActivity());

        eng.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                lang.updateResource("en");
                getActivity().recreate();

            }
        });
        tr.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                lang.updateResource("tr");
                getActivity().recreate();

            }
        });

        Login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(!TextUtils.isEmpty(mail.getText()) &&!TextUtils.isEmpty(password.getText())){
                    firebaseAuth.signInWithEmailAndPassword(mail.getText().toString(), password.getText().toString())
                            .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                                @Override
                                public void onComplete(@NonNull Task<AuthResult> task) {
                                    if (task.isSuccessful()) {
                                        Intent i = new Intent(getActivity(), MainActivity.class);
                                        startActivity(i);
                                        getActivity().finish();
                                    } else {
                                        String error = task.getException().getMessage();
                                        Toast.makeText(getActivity(), error, Toast.LENGTH_SHORT).show();
                                    }
                                }
                            });
                }else{
                    Toast.makeText(getActivity(),getResources().getText(R.string.LoginControl), Toast.LENGTH_SHORT).show();
                }
            }
        });
        return Login_fragment;
    }

    public void onClick(View view){
        if(view.getId() == R.id.signUpButton_id){
            Registration.FM.beginTransaction().replace(R.id.mainFragment_id,new SignUp_fragment(),null).addToBackStack(null).commit();
        }
    }
    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);


    }
}