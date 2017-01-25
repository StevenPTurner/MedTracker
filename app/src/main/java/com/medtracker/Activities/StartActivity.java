package com.medtracker.Activities;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.FragmentActivity;
import android.util.Log;
import android.view.View;
import android.widget.Toast;
import com.google.android.gms.auth.api.Auth;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.auth.api.signin.GoogleSignInResult;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.GoogleAuthProvider;
import com.google.firebase.auth.UserProfileChangeRequest;
import com.medtracker.Utilities.RC;
import com.medtracker.medtracker.R;

//Tutorial here was used and from various soruces for the firebase authentication
//https://developers.google.com/identity/sign-in/android/sign-in
//https://firebase.google.com/docs/auth/android/password-auth
//https://firebase.google.com/docs/auth/android/google-signin
//This is the starting class that handles all authentications to the database using the firebase
//api, it also makes very heavy use of google apis as well for google sign in too.
// is a bit resource heavy and will be refactored to use fragments if i have time
public class StartActivity extends FragmentActivity implements
        GoogleApiClient.OnConnectionFailedListener, View.OnClickListener{

    private static final String TAG = "LogStartActivity";
    private static final int RC_SIGN_IN_GOOGLE = RC.SIGN_IN_GOOGLE;
    private static final int RC_SIGN_IN_EMAIL = RC.SIGN_IN_EMAIL;
    private static final int RC_CREATE_ACCOUNT_EMAIL = RC.CREATE_ACCOUNT_EMAIL;
    private Intent mainActivityIntent;
    private GoogleApiClient mGoogleApiClient;
    private FirebaseAuth mAuth;
    private FirebaseAuth.AuthStateListener mAuthListener;
    private String displayName;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_start);
        findViewById(R.id.button_sign_in_with_google).setOnClickListener(this);

        // Configure Google Sign In
        GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestIdToken(getString(R.string.default_web_client_id))
                .requestEmail()
                .build();

        // Build a GoogleApiClient with access to the Google Sign-In API and the
        mGoogleApiClient = new GoogleApiClient.Builder(this)
                .enableAutoManage(this /* FragmentActivity */, this /* OnConnectionFailedListener */)
                .addApi(Auth.GOOGLE_SIGN_IN_API, gso)
                .build();

        //set up firebase objects and listener
        mainActivityIntent = new Intent(this, HomeActivity.class);
        mAuth = FirebaseAuth.getInstance();
        mAuthListener = new FirebaseAuth.AuthStateListener() {
            @Override
            public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {
                FirebaseUser user = firebaseAuth.getCurrentUser();
                if (user != null) {
                    // User is signed in
                    Log.d(TAG, "onAuthStateChanged:signed_in:" + user.getUid());
                    mainActivityIntent.putExtra("displayName",displayName);
                    startActivity(mainActivityIntent);
                } else {
                    // User is signed out
                    Log.d(TAG, "onAuthStateChanged:signed_out");
                }
            }
        };
    }

    @Override
    public void onStart() {
        super.onStart();
        mAuth.addAuthStateListener(mAuthListener);
    }

    @Override
    public void onStop() {
        super.onStop();
        if (mAuthListener != null) {
            mAuth.removeAuthStateListener(mAuthListener);
        }
    }

    //monitors for button clicks
    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.button_sign_in_with_google:
                signInWithGoogle();
                break;
        }
    }

    //starts intent for google signing
    private void signInWithGoogle() {
        Intent signInWithGoogleIntent = Auth.GoogleSignInApi.getSignInIntent(mGoogleApiClient);
        startActivityForResult(signInWithGoogleIntent, RC_SIGN_IN_GOOGLE);
    }

    //starts intent to gather user input for e-mail sign in
    public void signInEmail(View view) {
        Intent signInWithEmailIntent = new Intent(this, SignInActivity.class);
        startActivityForResult(signInWithEmailIntent, RC_SIGN_IN_EMAIL);
    }

    //starts intent to gather user input for email create an account
    public void createAccountEmail(View view) {
        Log.d(TAG, "creatingAccount");
        Intent createAccountWithEmailIntent = new Intent(this, CreateAccountActivity.class);
        startActivityForResult(createAccountWithEmailIntent, RC_CREATE_ACCOUNT_EMAIL);
    }


    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        // Result returned from launching the Intent from GoogleSignInApi.getSignInIntent(...);
        if (requestCode == RC_SIGN_IN_GOOGLE) {
            GoogleSignInResult result = Auth.GoogleSignInApi.getSignInResultFromIntent(data);
            handleGoogleSignInResult(result);
        } else if(requestCode == RC_SIGN_IN_EMAIL) {
            String email = data.getStringExtra("email");
            String password = data.getStringExtra("password");
            firebaseSignInWithEmail(email, password);
        } else if(requestCode == RC_CREATE_ACCOUNT_EMAIL) {
            String email = data.getStringExtra("email");
            String password = data.getStringExtra("password");
            displayName = data.getStringExtra("displayName");
            firebaseRegisterAccountWithEmail(email, password);
        }
    }

    private void handleGoogleSignInResult(GoogleSignInResult result) {
        Log.d(TAG, "handleSignInResult:" + result.isSuccess());
        if (result.isSuccess()) {
            // Signed in successfully, show authenticated UI.
            Log.d(TAG, "was a success");
            GoogleSignInAccount account = result.getSignInAccount();
            displayName = account.getDisplayName();
            firebaseAuthWithGoogle(account);
        } else {
            //error logging
            Log.d(TAG, "was a failure");
        }
    }

    private void firebaseAuthWithGoogle(GoogleSignInAccount acct) {
        Log.d(TAG, "firebaseAuthWithGoogle:" + acct.getId());
        AuthCredential credential = GoogleAuthProvider.getCredential(acct.getIdToken(), null);
        mAuth.signInWithCredential(credential)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        Log.d(TAG, "signInWithCredential:onComplete:" + task.isSuccessful());

                        // If sign in fails, display a message to the user. If sign in succeeds
                        // the auth state listener will be notified and logic to handle the
                        // signed in user can be handled in the listener.
                        if (!task.isSuccessful()) {
                            Log.w(TAG, "signInWithCredential", task.getException());
                            Toast.makeText(StartActivity.this, "Authentication failed.",
                                    Toast.LENGTH_SHORT).show();
                        }
                    }

                });
    }

    private void firebaseSignInWithEmail(String email, String password) {
        Log.d(TAG, "signIn:" + email);
        mAuth.signInWithEmailAndPassword(email, password)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        Log.d(TAG, "signInWithEmail:onComplete:" + task.isSuccessful());

                        // If sign in fails, display a message to the user. If sign in succeeds
                        // the auth state listener will be notified
                        if (!task.isSuccessful()) {
                            Log.w(TAG, "signInWithEmail:failed", task.getException());
                            Toast.makeText(StartActivity.this, R.string.auth_failed,
                                    Toast.LENGTH_SHORT).show();
                        }
                    }
                });
    }

    private void firebaseRegisterAccountWithEmail(String email, String password) {
        Log.d(TAG, "createAccount:" + email);
        mAuth.createUserWithEmailAndPassword(email, password)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        Log.d(TAG, "createUserWithEmail:onComplete:" + task.isSuccessful());
                        // If sign in fails, display a message to the user. If sign in succeeds
                        // the auth state listener will be notified
                        if (!task.isSuccessful()) {
                            Toast.makeText(StartActivity.this, R.string.auth_failed,
                                    Toast.LENGTH_SHORT).show();
                        }
                    }
                });
    }

    @Override
    public void onConnectionFailed(ConnectionResult connectionResult) {
        Log.d(TAG, "onConnectionFailed:" + connectionResult);
    }

}




