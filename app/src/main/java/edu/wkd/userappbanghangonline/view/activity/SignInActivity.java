package edu.wkd.userappbanghangonline.view.activity;


import androidx.activity.result.ActivityResult;
import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.IntentSenderRequest;
import androidx.activity.result.contract.ActivityResultContracts;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import edu.wkd.userappbanghangonline.R;
import edu.wkd.userappbanghangonline.data.api.ApiService;
import edu.wkd.userappbanghangonline.databinding.ActivitySignInBinding;
import edu.wkd.userappbanghangonline.model.obj.User;
import edu.wkd.userappbanghangonline.model.response.UserResponse;
import edu.wkd.userappbanghangonline.ultil.ProgressDialogLoading;
import edu.wkd.userappbanghangonline.ultil.UserUltil;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.util.Patterns;
import android.view.View;
import android.widget.Toast;


import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;

import com.google.android.gms.auth.api.identity.BeginSignInRequest;
import com.google.android.gms.auth.api.identity.BeginSignInResult;
import com.google.android.gms.auth.api.identity.Identity;
import com.google.android.gms.auth.api.identity.SignInClient;
import com.google.android.gms.auth.api.identity.SignInCredential;
import com.google.android.gms.common.api.ApiException;
import com.google.android.gms.common.api.CommonStatusCodes;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.HashMap;



public class SignInActivity extends AppCompatActivity {
    private static final String TAG = SignInActivity.class.toString();
    private ActivitySignInBinding binding;
    private ProgressDialogLoading loading;
    FirebaseAuth firebaseAuth;
    ProgressDialog dialog;
    private SignInClient oneTapClient;
    private BeginSignInRequest signUpRequest;
    private FirebaseDatabase database;
    private DatabaseReference mDatabase;
    private ProgressDialogLoading progressDialogLoading;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivitySignInBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        initView();
        dialog = new ProgressDialog(this);
        firebaseAuth = FirebaseAuth.getInstance();

        progressDialogLoading = new ProgressDialogLoading(this);
        onBack();//Quay trở lại sự kiện trước đó
        goToForgotPasswordActivity();//
        goToMainActivity();
        configureOnTapClient();//Cấu hình đăng nhập google bằng 1 lần chạm
        onClickLayoutGoogle();//Xử lí sự kiện khi người dùng đăng nhập bằng google
    }

    private ActivityResultLauncher<IntentSenderRequest> activityResultLauncher =  registerForActivityResult(new ActivityResultContracts.StartIntentSenderForResult(), new ActivityResultCallback<ActivityResult>() {
        @Override
        public void onActivityResult(ActivityResult result) {
            if (result.getResultCode() == RESULT_OK){
                try {
                    SignInCredential credential = oneTapClient.getSignInCredentialFromIntent(result.getData());
                    String idToken = credential.getGoogleIdToken();
                    if (idToken !=  null) {
                        checkAccountExists(credential.getId(), credential);
                        Log.d(TAG, "Got ID token.");
                    }
                } catch (ApiException e) {
                    switch (e.getStatusCode()) {
                        case CommonStatusCodes.CANCELED:
                            Log.d(TAG, "One-tap dialog was closed.");
                            break;
                        case CommonStatusCodes.NETWORK_ERROR:
                            Log.d(TAG, "One-tap encountered a network error.");
                            // Try again or just ignore.
                            break;
                        default:
                            Log.d(TAG, "Couldn't get credential from result."
                                    + e.getLocalizedMessage());
                            break;
                    }
                }
            }
        }
    });

    //Kiểm tra xem đã có tài khoản trên firebase chưa
    private void checkAccountExists(String id, SignInCredential credential) {
        progressDialogLoading.show();
        if (id != null){
            mDatabase = database.getReference("users");
            mDatabase.addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot snapshot) {
                    boolean emailExists = false;
                    User userFromDatabase = null;
                    for (DataSnapshot childSnapshot : snapshot.getChildren()) {
                        DataSnapshot userSnapshot = childSnapshot.child("user");
                        userFromDatabase = userSnapshot.getValue(User.class);
                        String email = userSnapshot.child("email").getValue(String.class);
                        if (email != null && email.equals(id)) {
                            emailExists = true;
                            break;
                        }
                    }
                    if (emailExists) {
                        progressDialogLoading.dismiss();
                        UserUltil.user = userFromDatabase;
                        Intent intent = new Intent(SignInActivity.this, MainActivity.class);
                        startActivity(intent);
                    } else {
                        progressDialogLoading.dismiss();
                        // Thêm email vào cấu trúc dữ liệu
                        mDatabase = database.getReference("users");
                        User user = new User(0,0,0,credential.getId(),credential.getPassword(),credential.getDisplayName(),"","","","");
                        HashMap<String, Object> mapUser = new HashMap<>();
                        mapUser.put("user", user);
                        mDatabase.push().setValue(mapUser);
                        UserUltil.user = user;
                        Intent intent = new Intent(SignInActivity.this, MainActivity.class);
                        startActivity(intent);
                    }

                }

                @Override
                public void onCancelled(@NonNull DatabaseError error) {
                    Log.e(TAG, "onCancelled: " + error);
                }
            });
        }
    }


    private void onClickLayoutGoogle() {
        binding.layoutLoginGoogle.setOnClickListener(v ->{
            progressDialogLoading.show();
            oneTapClient.beginSignIn(signUpRequest)
                    .addOnSuccessListener(SignInActivity.this, new OnSuccessListener<BeginSignInResult>() {
                        @Override
                        public void onSuccess(BeginSignInResult result) {
                            progressDialogLoading.dismiss();
                            IntentSenderRequest senderRequest = new IntentSenderRequest.Builder(result.getPendingIntent().getIntentSender()).build();
                            activityResultLauncher.launch(senderRequest);
                        }
                    })
                    .addOnFailureListener(SignInActivity.this, new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {
                            // No Google Accounts found. Just continue presenting the signed-out UI.
                            Log.d(TAG, e.getLocalizedMessage());
                        }
                    });
            }
        );
    }


    private void configureOnTapClient() {
        database = FirebaseDatabase.getInstance();
        oneTapClient = Identity.getSignInClient(this);
        signUpRequest = BeginSignInRequest.builder()
                .setGoogleIdTokenRequestOptions(BeginSignInRequest.GoogleIdTokenRequestOptions.builder()
                        .setSupported(true)
                        // Your server's client ID, not your Android client ID.
                        .setServerClientId(getString(R.string.your_web_client_id))
                        // Show all accounts on the device.
                        .setFilterByAuthorizedAccounts(false)
                        .build())
                .build();
    }

    private void goToMainActivity() {
        binding.btnSignIn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String strUsername = binding.edEmailOrPhoneNumberSignIn.getText().toString().trim();
                String strPassword = binding.edPasswordSignIn.getText().toString().trim();
                firebaseAuth.signInWithEmailAndPassword(strUsername, strPassword)
                                .addOnSuccessListener(new OnSuccessListener<AuthResult>() {
                                    @Override
                                    public void onSuccess(AuthResult authResult) {
                                        dialog.cancel();
                                        Toast.makeText(SignInActivity.this, "Đăng nhập thành công", Toast.LENGTH_SHORT).show();
                                    }
                                })
                                        .addOnFailureListener(new OnFailureListener() {
                                            @Override
                                            public void onFailure(@NonNull Exception e) {
                                                dialog.cancel();
                                                Toast.makeText(SignInActivity.this, e.getMessage(), Toast.LENGTH_SHORT).show();

                                            }
                                        });
                CheckValidate();
            }
        });
    }
    public boolean isValidEmail(String email) {
        return Patterns.EMAIL_ADDRESS.matcher(email).matches();
    }
    public void CheckValidate(){
        String email = binding.edEmailOrPhoneNumberSignIn.getText().toString().trim();
        if(isValidEmail(email)){
            loading.show();
            loginUser();
        }else {
            Toast.makeText(this, "Email invalid", Toast.LENGTH_SHORT).show();
        }
    }
    public void loginUser(){
        String strUsername = binding.edEmailOrPhoneNumberSignIn.getText().toString().trim();
        String strPassword = binding.edPasswordSignIn.getText().toString().trim();
        ApiService.apiService.loginUser(strUsername, strPassword).enqueue(new Callback<UserResponse>() {
            @Override
            public void onResponse(Call<UserResponse> call, Response<UserResponse> response) {
                if(response.isSuccessful()){
                    UserResponse response1 = response.body();
                    if(response1.isSuccess()){
                        Intent intent = new Intent(SignInActivity.this, MainActivity.class);
                        startActivity(intent);
                        UserUltil.user = response1.getResult().get(0);
//                        Toast.makeText(SignInActivity.this, "Đăng nhập thành công", Toast.LENGTH_SHORT).show();
                        Log.d("zzzz", "onResponse: " + UserUltil.user);
                    }else {
                        Toast.makeText(SignInActivity.this, "Email or UserName or PassWord invalid", Toast.LENGTH_SHORT).show();
                    }
                    loading.cancel();
                }
            }

            @Override
            public void onFailure(Call<UserResponse> call, Throwable t) {
                Toast.makeText(SignInActivity.this, "call api err", Toast.LENGTH_SHORT).show();
                loading.cancel();
            }
        });

    }

    private void goToForgotPasswordActivity() {
        binding.tvForgotPassword.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(SignInActivity.this, ForgotPasswordActivity.class));
            }
        });
    }

    private void onBack() {
        binding.arrowBackSignIn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
                overridePendingTransition(R.anim.slidle_in_right, R.anim.slidle_out_right);
            }
        });
    }

    private void initView() {
        loading = new ProgressDialogLoading(this);
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        overridePendingTransition(R.anim.slidle_in_right, R.anim.slidle_out_right);
    }
}