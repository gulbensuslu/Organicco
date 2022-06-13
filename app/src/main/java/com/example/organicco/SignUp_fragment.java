package com.example.organicco;

import static android.content.Context.NOTIFICATION_SERVICE;



import android.app.AlarmManager;
import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.app.NotificationCompat;
import androidx.core.app.NotificationManagerCompat;
import androidx.fragment.app.Fragment;

import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.HashMap;
import java.util.Map;


public class SignUp_fragment extends Fragment {
    EditText name;
    EditText surname;
    EditText email;
    EditText password;
    EditText confirmPassword;
    EditText phoneNumber;
    CheckBox privatePolicy;
    Button save;
    private FirebaseAuth firebaseAuth;
    private FirebaseFirestore firebaseFirestore;
    NotificationManagerCompat managerCompat;
    String notificationTitle;
    static final String id = "Notification";
    public SignUp_fragment() {

    }

    public static SignUp_fragment newInstance(String param1, String param2) {
        SignUp_fragment SignUp_fragment = new SignUp_fragment();
        return SignUp_fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        name.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });
    }
        private void checkInputs(){

            String user_password=password.getText().toString();
            String user_confirmPassword=confirmPassword.getText().toString();
            String user_email=email.getText().toString();

        if(!TextUtils.isEmpty(name.getText()) && !TextUtils.isEmpty(surname.getText()) && !TextUtils.isEmpty(email.getText())
                &&!TextUtils.isEmpty(password.getText())&&!TextUtils.isEmpty(confirmPassword.getText())
                &&!TextUtils.isEmpty(phoneNumber.getText())&&privatePolicy.isChecked()){

            if(!user_password.equals(user_confirmPassword)){
                Toast.makeText(getContext(), getResources().getText(R.string.error), Toast.LENGTH_SHORT).show();

            }else{
                firebaseAuth.createUserWithEmailAndPassword(user_email,user_password)
                        .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                            @Override
                            public void onComplete(@NonNull Task<AuthResult> task) {
                                if(task.isSuccessful()){
                                    Map<Object,String> userInfo = new HashMap<>();
                                    userInfo.put("name",name.getText().toString());
                                    userInfo.put("surname",surname.getText().toString());
                                    userInfo.put("phone",phoneNumber.getText().toString());
                                    userInfo.put("email",email.getText().toString());

                                    firebaseFirestore.collection("USER").document(firebaseAuth.getUid())
                                            .set(userInfo)
                                           .addOnCompleteListener(new OnCompleteListener<Void>() {
                                               @Override
                                               public void onComplete(@NonNull Task<Void> task) {

                                                   if (task.isSuccessful()) {

                                                       Intent i = new Intent(getActivity(), MainActivity.class);
                                                       startActivity(i);
                                                       getActivity().finish();
                                                       displayNotification();
                                                       Toast.makeText(getContext(), getResources().getText(R.string.successful), Toast.LENGTH_SHORT).show();

                                                   } else {
                                                       String error = task.getException().getMessage();
                                                       Toast.makeText(getContext(), error, Toast.LENGTH_SHORT).show();
                                                   }

                                               }
                                           });


                                }else{
                                    String error =task.getException().getMessage();
                                    Toast.makeText(getContext(), error, Toast.LENGTH_SHORT).show();
                                }
                            }
                        });

            }
        }else{
            Toast.makeText(getContext(),getResources().getText(R.string.not), Toast.LENGTH_SHORT).show();
        }

  }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {


        String title="Welcome";
        View SignUp_fragment = inflater.inflate(R.layout.fragment_sign_up, container, false);
        name = SignUp_fragment.findViewById(R.id.name_id);
        surname = SignUp_fragment.findViewById(R.id.surname_id);
        email = SignUp_fragment.findViewById(R.id.email_id);
        password = SignUp_fragment.findViewById(R.id.pass_id);
        confirmPassword = SignUp_fragment.findViewById(R.id.confirmpass_id);
        phoneNumber = SignUp_fragment.findViewById(R.id.phonenumber_id);
        privatePolicy = SignUp_fragment.findViewById(R.id.checkBox_id);
        firebaseAuth= FirebaseAuth.getInstance();
        firebaseFirestore=FirebaseFirestore.getInstance();
        save = SignUp_fragment.findViewById(R.id.saveButton_id);
        managerCompat=NotificationManagerCompat.from(getActivity());

        notificationTitle = (String)getResources().getText(R.string.notification);

        save.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                checkInputs();
            }
        });

        return SignUp_fragment;
    }

    private void displayNotification(){
        createChannel();
        NotificationCompat.Builder builder= new NotificationCompat.Builder(getActivity(),id);
        builder.setSmallIcon(R.drawable.ic_baseline_how_to_reg_24);
        builder.setContentTitle(notificationTitle);
        builder.setPriority(NotificationCompat.PRIORITY_DEFAULT);

        NotificationManagerCompat managerCompat = NotificationManagerCompat.from(getActivity());
        managerCompat.notify(1,builder.build());
    }

   private void createChannel(){
       if(Build.VERSION.SDK_INT>=Build.VERSION_CODES.O){
           CharSequence channelName = "MyChannel";
           int importance =NotificationManager.IMPORTANCE_DEFAULT;
           NotificationChannel channel=new NotificationChannel(id,channelName,importance);
           NotificationManager manager=(NotificationManager) getActivity().getSystemService(NOTIFICATION_SERVICE);
           manager.createNotificationChannel(channel);

       }
   }
}