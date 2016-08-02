package blog.sidvitech.com.authentication.LogIn;


import android.Manifest;
import android.annotation.TargetApi;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.IntentSender;
import android.content.pm.PackageManager;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.support.design.widget.TextInputLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.content.ContextCompat;
import android.support.v4.view.ViewPager;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.view.inputmethod.InputMethodManager;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.facebook.AccessToken;
import com.facebook.CallbackManager;
import com.facebook.FacebookCallback;
import com.facebook.FacebookException;
import com.facebook.FacebookSdk;
import com.facebook.GraphRequest;
import com.facebook.GraphResponse;
import com.facebook.Profile;
import com.facebook.ProfileTracker;
import com.facebook.appevents.AppEventsLogger;
import com.facebook.login.LoginManager;
import com.facebook.login.LoginResult;
import com.facebook.login.widget.LoginButton;
import com.google.android.gms.auth.GoogleAuthUtil;
import com.google.android.gms.auth.UserRecoverableAuthException;
import com.google.android.gms.auth.api.Auth;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.auth.api.signin.GoogleSignInResult;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GooglePlayServicesUtil;
import com.google.android.gms.common.Scopes;
import com.google.android.gms.common.SignInButton;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.plus.Plus;
import com.google.android.gms.plus.model.people.Person;

import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicHeader;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.protocol.HTTP;
import org.apache.http.util.EntityUtils;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import blog.sidvitech.com.activity.MainActivity;
import blog.sidvitech.com.activity.R;
import blog.sidvitech.com.authentication.ForgotPass.forgotPassword;
import blog.sidvitech.com.authentication.Otp_Verification.otp_verfication;
import blog.sidvitech.com.authentication.SignUp.signup;
import blog.sidvitech.com.models.request.LoginRequestData;
import blog.sidvitech.com.models.response.LoginResponseData;
import blog.sidvitech.com.models.response.UserData;

//import com.facebook.FacebookSdk;
//

//import android.widget.Button;


/**
 * A simple {@link Fragment} subclass.
 */
public class Login extends Fragment implements GoogleApiClient.ConnectionCallbacks, GoogleApiClient.OnConnectionFailedListener, Animation.AnimationListener {


    public static final String TAG = "google";
    public static final int RC_SIGN_IN = 0;
    private static final int REQUEST_CODE_ASK_PERMISSIONS = 100;
    private Context mContext;
    private Activity mActivity;
    SignInButton signInButton;
    private boolean mSignInClicked;
    private boolean mIntentInProgress;
    private ConnectionResult mConnectionResult;
    private GoogleApiClient mGoogleApiClient;
    private LoginButton loginButton;
    private CallbackManager callbackManager;
    private AccessToken accessToken;
    private ProfileTracker profileTracker;
    private ImageView logo_image;
    private AutoCompleteTextView tvUsername;
    private EditText etpassword;
    private Button login, glogin;
    private TextInputLayout textInputLayout, til;
    private View view;
    public static Button btnlogin;
    private TextView tvlink, tvlinksignup;
    private Fragment f;
    private Animation animator, exit_anim;
    private LinearLayout login_layout;
    private ProgressBar progressBar;

    private LoginRequestData loginRequestData;
    private LoginResponseData loginResponseData;
    private UserData userData1;


    public static final String BASE_URL = "http://192.168.0.3/api/user/";
    public static final String email = "email";
    public static final String password ="password";
    public static final String source="source";
    public static final String access_token="access_token";
    public static final String expires_in="expires_in";
    public static final String token_type="token_type";
    public static final String is_email_verified="is_email_verified";
    public static final String scope="scope";
    public static final String refresh_token="refresh_token";
    public static final String user="user";
    public static final String id="id";
    public static final String full_name="full_name";
    public static final String infulence_score="infulence_score";
    public static final String first_name="first_name";
    public static final String middle_name="middle_name";
    public static final String last_name="last_name";
    public String gid;


    public static final String social_details="social_details";
    public static final String provider="provider";
    public static final String provider_id="provider_id";
    public static final String user_data="user_data";
    public static final String date_of_birth="date_of_birth";
    public static final String gender="gender";
    public static final String role="role";
    public static final String Normal_User="Normal User";




    //  create a textWatcher member for disable and enabel the button
    public TextWatcher mTextWatcher = new TextWatcher() {
        @Override
        public void beforeTextChanged(CharSequence charSequence, int i, int i2, int i3) {
        }

        @Override
        public void onTextChanged(CharSequence charSequence, int i, int i2, int i3) {
        }

        @Override
        public void afterTextChanged(Editable editable) {
            // check Fields For Empty Values
            checkFieldsForEmptyValues();
        }
    };

    @TargetApi(Build.VERSION_CODES.JELLY_BEAN)
    public void checkFieldsForEmptyValues() {
        Button b = btnlogin;

        String s1 = tvUsername.getText().toString();
        String s2 = etpassword.getText().toString();

        if (s1.contains("@") && s2.length() > 6) {
            b.setEnabled(true);
            b.setBackground(getResources().getDrawable(R.drawable.shape));
        } else {
            b.setEnabled(false);
            b.setBackground(getResources().getDrawable(R.drawable.disableshape));
        }
    }
    public Login() {

        // Required empty public constructor

    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mActivity = getActivity();
        mContext = getActivity().getApplicationContext();

            FacebookSdk.sdkInitialize(mContext);
            callbackManager = CallbackManager.Factory.create();
            AppEventsLogger.activateApp(mActivity);

            accessToken = AccessToken.getCurrentAccessToken();

        LoginManager.getInstance().registerCallback(callbackManager, new FacebookCallback<LoginResult>() {
            @Override
            public void onSuccess(LoginResult loginResult) {

            }

            @Override
            public void onCancel() {

            }

            @Override
            public void onError(FacebookException error) {

            }
        });
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {


        GoogleSignInOptions googleSignInOptions = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestEmail()
                .build();


        view = inflater.inflate(R.layout.fragment_login, container, false);
        signInButton = (SignInButton) view.findViewById(R.id.sign_in_button);
        btnlogin = (Button) view.findViewById(R.id.btnlogin);
        tvlink = (TextView) view.findViewById(R.id.tvforgotpassword);
        tvlinksignup = (TextView) view.findViewById(R.id.signup);
        tvUsername = (AutoCompleteTextView) view.findViewById(R.id.email);
        textInputLayout = (TextInputLayout) view.findViewById(R.id.textinputlayout);
        til = (TextInputLayout) view.findViewById(R.id.textlayout);
        etpassword = (EditText) view.findViewById(R.id.etpassword);
        login = (Button) view.findViewById(R.id.fb);
        glogin = (Button) view.findViewById(R.id.gplus);
        logo_image = (ImageView) view.findViewById(R.id.logo);
        login_layout = (LinearLayout) view.findViewById(R.id.login_layout);
        progressBar= (ProgressBar) view.findViewById(R.id.progressBar);
        loginRequestData=new LoginRequestData();
        loginResponseData=new LoginResponseData();
        userData1=new UserData();


        //Initialize Animation
        animator = AnimationUtils.loadAnimation(mContext,R.anim.bounchanim);
        animator.setAnimationListener((Animation.AnimationListener) this);
//        exit_anim = AnimationUtils.loadAnimation(mContext,R.anim.exit);
//        exit_anim.setAnimationListener(this);

        ViewGroup.MarginLayoutParams marginLayoutParams;
        marginLayoutParams = (ViewGroup.MarginLayoutParams)container.getLayoutParams();
        marginLayoutParams.setMargins(0,50,0,0);

//        PercentRelativeLayout.LayoutParams layoutParams;
//        layoutParams = (PercentRelativeLayout.LayoutParams) logo_image.getLayoutParams();
//        PercentLayoutHelper.PercentLayoutInfo percentLayoutInfo = layoutParams.getPercentLayoutInfo();
//        percentLayoutInfo.leftMarginPercent = 15 * 0.01f; //15 is the percentage value you want to set it to
//        logo_image.setLayoutParams(layoutParams);

//        ViewGroup.LayoutParams param ;
//        param = logo_image.getLayoutParams();
//        param.height = 250;
//        param.width = 250;
//        logo_image.setLayoutParams(param);
        //to perform operation on back button
        view.setFocusableInTouchMode(true);
        view.requestFocus();
        view.setOnKeyListener(new View.OnKeyListener() {
            @Override
            public boolean onKey(View v, int keyCode, KeyEvent event) {
                if (keyCode == KeyEvent.KEYCODE_BACK) {

                    mActivity.finish(); //exit from activity
                    System.exit(0);
                }
                return false;
            }
        });

        //facebook loginbutton initial operation
        loginButton = (LoginButton) view.findViewById(R.id.login_button);
        loginButton.setReadPermissions("user_friends", "email", "user_birthday","user_status");
        loginButton.setCompoundDrawablesWithIntrinsicBounds(null, null, null, null);

        loginButton.setFragment(this);

        //Start Animation
        login_layout.startAnimation(animator);

        btnlogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                loginRequestData.setUserEmail(tvUsername.getText().toString());
                loginRequestData.setPassword(etpassword.getText().toString());
                new AsyncT().execute();

            }
        });

        login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (v == login) {
                    loginButton.performClick();
                }
            }
        });

        loginButton.registerCallback(callbackManager, new FacebookCallback<LoginResult>() {
            @Override
            public void onSuccess(LoginResult loginResult) {

                System.out.println("OnSucess Method");

                final String accestoken = loginResult.getAccessToken().getToken();
                Log.i("acessToken", accestoken);
                Log.d("login result ---:---",loginResult.toString());

                GraphRequest graphRequest = GraphRequest.newMeRequest(loginResult.getAccessToken(), new GraphRequest.GraphJSONObjectCallback() {
                    @Override
                    public void onCompleted(JSONObject object, GraphResponse response) {
                        Log.i("LogInActivity",response.toString());
                        try {
                            Bundle getData = getFacebookData(object, accestoken);
                        }catch (JSONException e){
                            e.printStackTrace();
                        }
//                        try {
//                            System.out.print(object.get("gender"));
//                        } catch (JSONException e) {
//                            e.printStackTrace();
//                            Log.e("Lol","Gender nahi");
//                        }
                        //Log.e("Facebbok Data :- ",getData.toString());

                    }
                });


            Bundle parameters = new Bundle();
                parameters.putString("fields","id,name,email");
                graphRequest.setParameters(parameters);
                graphRequest.executeAsync();

                Profile profile = Profile.getCurrentProfile();

                try{
                    Toast.makeText(getContext(),"Log In as "+profile.getFirstName()+profile.getLastName(),Toast.LENGTH_LONG).show();
                }catch (Exception e){
                    Toast.makeText(getContext(),"Error to log in",Toast.LENGTH_LONG).show();
                }

            }

            @Override
            public void onCancel() {
                Toast.makeText(getContext(),"LogIn Cancel",Toast.LENGTH_LONG).show();
            }

            @Override
            public void onError(FacebookException error) {
                Toast.makeText(getContext(),"Failed to log in",Toast.LENGTH_LONG).show();
            }

        });

        mGoogleApiClient = new GoogleApiClient.Builder(view.getContext())
                .addConnectionCallbacks(this)
                .addOnConnectionFailedListener(this)
                .addApi(Auth.GOOGLE_SIGN_IN_API, googleSignInOptions)
                .addApi(Plus.API)
//                .addScope(Scopes.PLUS_LOGIN)
//                .addScope(Scopes.PLUS_ME)
                .build();

//                .enableAutoManage(this /* FragmentActivity */, this /* OnConnectionFailedListener */)



        //SetSignInButton Size
        signInButton.setSize(SignInButton.SIZE_WIDE);
        signInButton.setScopes(googleSignInOptions.getScopeArray());


        glogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                signInWithGplus();
            }
        });

        // set listeners
        tvUsername.addTextChangedListener(mTextWatcher);
        etpassword.addTextChangedListener(mTextWatcher);

        signInButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                signInWithGplus();
            }
        });

        // run once to disable if empty
        checkFieldsForEmptyValues();

        textInputLayout.setErrorEnabled(false);
        til.setErrorEnabled(false);
        setupUI(view.findViewById(R.id.loginactivity));



        tvlink.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                f = new forgotPassword();
                setFragment(f);
            }
        });

        tvlinksignup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                f = new signup();
                setFragment(f);
            }
        });

        return view;
    }

    private Bundle getFacebookData(JSONObject object,String access_token1) throws JSONException{
        System.out.print("getFacebookData");
        String id1 = object.getString("id");

        Bundle bundle = new Bundle();
        try {


//            Log.v("Facebook DaTa == ",object.toString());

            if (object.getString("email")==null)
                Toast.makeText(getContext(),"Email not available",Toast.LENGTH_LONG).show();
            else {
                Toast.makeText(getContext(), "Email available", Toast.LENGTH_LONG).show();
            }
            try {
                URL profile_pic = new URL("https://graph.facebook.com/" + id + "/picture?width=200&height=150");
//                Log.i("profile_pic", profile_pic + "");
//                bundle.putString("profile_pic", profile_pic.toString());

            } catch (MalformedURLException e) {
                e.printStackTrace();

//                return null;
            }
            Log.e("error","errorr");
//
//            bundle.putString("idFacebook", id);
//            if (object.has("first_name"))
//                bundle.putString("first_name", object.getString("first_name"));
//            if (object.has("last_name"))
//                bundle.putString("last_name", object.getString("last_name"));
//            if (object.has("email"))
//                bundle.putString("email", object.getString("email"));
////            if (object.has("gender"))
////                bundle.putString("gender", object.getString("gender"));
//            if (object.has("birthday"))
//                bundle.putString("birthday", object.getString("birthday"));
//            if (object.has("location"))
//                bundle.putString("location", object.getJSONObject("location").getString("name"));



        }catch (Exception e)
        { e.printStackTrace();
        System.out.print("Exception");
        }
            try {


                try {

                    Log.d("Connection", "establish");
                    JSONObject jsonObject=new JSONObject();
                    jsonObject.put(access_token,access_token1);
                    List<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>();
                    System.out.print("valuepair");
//                    nameValuePairs.add(new BasicNameValuePair(access_token, access_token1));
                    System.out.print("access token " + access_token1);
                    jsonObject.put(provider,"facebook");
//                    nameValuePairs.add(new BasicNameValuePair(provider, "facebook"));
                    jsonObject.put(provider_id,id1 );
//                    nameValuePairs.add(new BasicNameValuePair(provider_id,id1 ));
                    List<NameValuePair> user = new ArrayList<NameValuePair>();
                    JSONObject jsonObject1=new JSONObject();
                    if(object.has("birthday")){
                        jsonObject1.put(date_of_birth, object.getString("birthday"));
//                    user.add(new BasicNameValuePair(date_of_birth, object.getString("birthday")));
                    System.out.print("Birthday " + object.get("birthday"));}
                    else{
                        jsonObject1.put(date_of_birth, null);
//                        user.add(new BasicNameValuePair(date_of_birth, ""));
                        System.out.print("Birthday not available " );
                    }
                    jsonObject1.put(email, object.getString("email"));
//                    user.add(new BasicNameValuePair(email, object.getString("email")));
                    System.out.print("email " + object.getString("email"));
                    jsonObject1.put(full_name, object.getString("name"));
//                    user.add(new BasicNameValuePair(full_name, object.getString("name")));
                    if (object.has(gender)) {
                        jsonObject1.put(gender, object.getString(gender));
//                        user.add(new BasicNameValuePair(gender, object.getString(gender)));
                    } else
                        jsonObject1.put(gender, null);
//                    user.add(new BasicNameValuePair(gender, ""));
                    jsonObject1.put(role, Normal_User);
//                    user.add(new BasicNameValuePair(role, Normal_User));
                    List<NameValuePair> data = new ArrayList<NameValuePair>();
                    JSONObject jsonObject2=new JSONObject();
                    jsonObject2.put(social_details,jsonObject);
//                    data.add(new BasicNameValuePair(social_details, nameValuePairs.toString()));
                    jsonObject2.put(source,"USER_APP");
//                    data.add(new BasicNameValuePair(source, "USER_APP"));
                    jsonObject2.put(user_data,jsonObject1);
                    data.add(new BasicNameValuePair(user_data, user.toString()));


                    Log.e("mainToPost", "mainToPost" + jsonObject2);

                    new FBAsyncT().execute(jsonObject2);

                } catch (Exception e) {
                    System.out.print("stop " + e);
                }
            }catch (Exception e){
                Log.i("Connection", "error");
            }
        return bundle;
    }

    public void setFragment(Fragment f) {
        FragmentManager fm = getActivity().getSupportFragmentManager();
        FragmentTransaction ft = fm.beginTransaction();
        ft.replace(R.id.flfrholder, f);
        ft.addToBackStack(null);
        ft.commit();
    }


    private boolean isEmailValid(String email) {
        //TODO: Replace this with your own logic
        return email.contains("@");
    }

    public boolean isValidEmail(CharSequence target) {
        if (target == null) {
            return false;
        } else {
            return android.util.Patterns.EMAIL_ADDRESS.matcher(target).matches();
        }
    }

    public void setupUI(View view) {

        //Set up touch listener for non-text box views to hide keyboard.
        if (!(view instanceof EditText)) {

            view.setOnTouchListener(new View.OnTouchListener() {

                public boolean onTouch(View v, MotionEvent event) {
                    hideSoftKeyboard(getActivity());
                    return false;
                }

            });
        }

        //If a layout container, iterate over children and seed recursion.
        if (view instanceof ViewGroup) {

            for (int i = 0; i < ((ViewGroup) view).getChildCount(); i++) {

                View innerView = ((ViewGroup) view).getChildAt(i);

                setupUI(innerView);
            }
        }
    }

    public static void hideSoftKeyboard(Activity activity) {
        InputMethodManager inputMethodManager = (InputMethodManager) activity.getSystemService(Activity.INPUT_METHOD_SERVICE);
        if (activity.getCurrentFocus() != null)
            inputMethodManager.hideSoftInputFromWindow(activity.getCurrentFocus().getWindowToken(), 0);
    }

    @Override
    public void onActivityResult(int requestCode, int responseCode,
                                 Intent data) {
        super.onActivityResult(requestCode,responseCode,data);

        callbackManager.onActivityResult(requestCode, responseCode, data);

        if (requestCode == RC_SIGN_IN) {
                GoogleSignInResult result = Auth.GoogleSignInApi.getSignInResultFromIntent(data);
                handleSignInResult(result);

            if (responseCode != Activity.RESULT_OK) {
                mSignInClicked = false;
            }

            mIntentInProgress = false;

            if (!mGoogleApiClient.isConnecting()) {
                mGoogleApiClient.connect(GoogleApiClient.SIGN_IN_MODE_OPTIONAL);
            }
        }
    }

    @Override
    public void onConnected(Bundle bundle) {

        try {
            if (Plus.PeopleApi.getCurrentPerson(mGoogleApiClient) != null) {
                Person currentPerson = Plus.PeopleApi
                        .getCurrentPerson(mGoogleApiClient);
                String personName = currentPerson.getDisplayName();
                String personPhotoUrl = currentPerson.getImage().getUrl();
                String personGooglePlusProfile = currentPerson.getUrl();
                String bdate=currentPerson.getBirthday();
//                String email = Plus.AccountApi.getAccountName(mGoogleApiClient);
                gid=currentPerson.getId();
                Log.e(TAG, "Name: " + personName + ", plusProfile: "
                        + personGooglePlusProfile + ", email: " + email
                        + ", Image: " + personPhotoUrl + " user id:"
                        + currentPerson.getId()+"Birthday "+bdate);

//                    nameValuePairs.add(new BasicNameValuePair(provider_id,id1 ));
                List<NameValuePair> user = new ArrayList<NameValuePair>();
                JSONObject jsonObject1=new JSONObject();
                    jsonObject1.put(date_of_birth, bdate);
//                    user.add(new BasicNameValuePair(date_of_birth, object.getString("birthday")));

                if(android.os.Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {

                    int hasWriteContactsPermission = ContextCompat.checkSelfPermission(getContext(), Manifest.permission.GET_ACCOUNTS);
                    if (hasWriteContactsPermission != PackageManager.PERMISSION_GRANTED) {
                        requestPermissions(new String[] {Manifest.permission.GET_ACCOUNTS},
                                REQUEST_CODE_ASK_PERMISSIONS);
                        return;
                    }
                }

                jsonObject1.put(email,Plus.AccountApi.getAccountName(mGoogleApiClient));
                jsonObject1.put(full_name, personName);
//                    user.add(new BasicNameValuePair(full_name, object.getString("name")));
                jsonObject1.put(gender, currentPerson.getGender());
              jsonObject1.put(role, Normal_User);
//                    user.add(new BasicNameValuePair(role, Normal_User));
                List<NameValuePair> data = new ArrayList<NameValuePair>();
                JSONObject jsonObject2=new JSONObject();

//                    data.add(new BasicNameValuePair(social_details, nameValuePairs.toString()));
                jsonObject2.put(source,"USER_APP");
//                    data.add(new BasicNameValuePair(source, "USER_APP"));
                jsonObject2.put(user_data,jsonObject1);

                new GoogleAsyncT().execute(jsonObject2);
            } else {
                Toast.makeText(mContext, "Person information is null",
                        Toast.LENGTH_LONG).show();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {

                Map<String, Integer> perms = new HashMap<String, Integer>();
                // Initial
                perms.put(Manifest.permission.GET_ACCOUNTS, PackageManager.PERMISSION_GRANTED);
                // Fill with results
                for (int i = 0; i < permissions.length; i++)
                    perms.put(permissions[i], grantResults[i]);




    }

    @Override
    public void onConnectionSuspended(int i) {
        mGoogleApiClient.connect();
    }

    @Override
    public void onConnectionFailed(ConnectionResult result) {

        if (!result.hasResolution()) {
            GooglePlayServicesUtil.getErrorDialog(result.getErrorCode(),
                    mActivity, 0).show();
            Log.e(TAG, "" + result.getErrorCode());
            return;
        }

        if (!mIntentInProgress) {

            mConnectionResult = result;

            if (mSignInClicked) {

                Log.e(TAG, "" + result.getErrorCode());
                resolveSignInError();
            }
        }
    }

    /**
     * Sign-in into google
     */
    private void signInWithGplus() {
        if (!mGoogleApiClient.isConnecting()) {
            mSignInClicked = true;
//            resolveSignInError();
            Intent signInIntent = Auth.GoogleSignInApi.getSignInIntent(mGoogleApiClient);
            startActivityForResult(signInIntent, RC_SIGN_IN);
        }
    }

    private void resolveSignInError() {
        if (mConnectionResult.hasResolution()) {
            try {
                mIntentInProgress = true;
                mConnectionResult.startResolutionForResult(mActivity,
                        RC_SIGN_IN);
            } catch (IntentSender.SendIntentException e) {
                mIntentInProgress = false;
                mGoogleApiClient.connect();
            }
        }

    }

    private void handleSignInResult(GoogleSignInResult result) {
        Log.d(TAG, "handleSignInResult:" + result.isSuccess());
        if (result.isSuccess()) {
            // Signed in successfully, show authenticated UI.
            GoogleSignInAccount acct = result.getSignInAccount();

            System.out.println("Sign in : " + acct.getDisplayName());


            try {
                System.out.print("acess  toookkkeenn");
                String authCode = acct.getServerAuthCode();

                System.out.print("access_token google " + authCode);
            }catch (Exception e){
                e.printStackTrace();
            }
//            mStatusTextView.setText(getString(R.string.signed_in_fmt, acct.getDisplayName()));

        } else {
            // Signed out, show unauthenticated UI.
        }
    }

    @Override
    public void onAnimationStart(Animation animation) {
        if (animation == animator){
//            Toast.makeText(mContext,"Bounch Animaiton",Toast.LENGTH_LONG).show();
        }
    }

    @Override
    public void onAnimationEnd(Animation animation) {

    }

    @Override
    public void onAnimationRepeat(Animation animation) {

    }

    class FBAsyncT extends AsyncTask<JSONObject ,Void , Void>{

        @Override
        protected Void doInBackground(JSONObject... data) {
            try {
                HttpClient httpclient = new DefaultHttpClient();
                HttpPost httppost = new HttpPost(BASE_URL +"/social/register/");
//                List<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>();
//                nameValuePairs=data[0];
                System.out.print("\n DDDDaaaTTAA"+data[0]);
                StringEntity se=new StringEntity(data[0].toString());
//                se.setContentEncoding(new BasicHeader(HTTP.CONTENT_TYPE, "application/json"));
                httppost.setEntity(se);
                httppost.setHeader("Accept", "application/json");
                httppost.setHeader("Content-type", "application/json");
                Log.d("myapp", "works till here. 2");
                try {
                    HttpResponse response = httpclient.execute(httppost);
                    String rbody = EntityUtils.toString(response.getEntity());
                    System.out.println("Code : " + rbody);

                    final JSONObject jsonObject = new JSONObject(rbody);
                    if (jsonObject != null) {
                        System.out.println("Login Successfully");
                    }
                            Handler handler = new Handler(getContext().getMainLooper());
                            handler.post(new Runnable() {
                                public void run() {
                                    try {
                                        Toast.makeText(getContext(), jsonObject.get("msg").toString(), Toast.LENGTH_LONG).show();
                                    } catch (JSONException e) {
                                        e.printStackTrace();
                                    }
                                }
                            });


                    Log.d("myapp", "response " + response.getEntity());
                    int rcode = response.getStatusLine().getStatusCode();
                    System.out.println("Code : " + rbody);
                    System.out.println("Status: " + rcode);
                    if (rcode == 200) {
                        System.out.print("Signupresponsedata set successfully");

                    }

                    System.out.println("Server is available");
                } catch (ClientProtocolException e) {
                    System.out.print(e);
                } catch (IOException e) {
                    System.out.print(e);
                } catch (Exception e) {
                    System.out.println("Server not available" + e.toString());
                }
            } catch (UnsupportedEncodingException e) {
                System.out.print(e);
            }
            return null;
        }


    }

    class GoogleAsyncT extends AsyncTask<JSONObject , Void , Void>{

        @Override
        protected Void doInBackground(JSONObject... data) {


                final String SCOPES = "https://www.googleapis.com/auth/plus.login ";
                String accessToken="";
                try {
                    accessToken = GoogleAuthUtil.getToken(
                            getContext(),
                            Plus.AccountApi.getAccountName(mGoogleApiClient), "oauth2:"
                                    + SCOPES);

                    System.out.println("Access token==" + accessToken);
                } catch (UserRecoverableAuthException e) {
                    e.printStackTrace();
                    Intent recover=e.getIntent();

//                    startActivityForResult(getContext(), REQUEST_CODE_TOKEN_AUTH);
                    startActivityForResult(recover, 100);

                    try {

                        accessToken = GoogleAuthUtil.getToken(
                                getContext(),
                                Plus.AccountApi.getAccountName(mGoogleApiClient), "oauth2:"
                                        + SCOPES);

                        System.out.println("Access token==" + accessToken);
                    }catch (Exception e1){
                        e1.printStackTrace();

                    }

                }catch (Exception e){

                }


            try {
                HttpClient httpclient = new DefaultHttpClient();
                HttpPost httppost = new HttpPost(BASE_URL +"/social/register/");
//                List<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>();
//                nameValuePairs=data[0];
                JSONObject jsonObject=new JSONObject();
//                jsonObject.put(access_token,access_token1);
                jsonObject.put(provider,"google");
//                    nameValuePairs.add(new BasicNameValuePair(provider, "facebook"));
                jsonObject.put(provider_id,getId() );
                jsonObject.put(access_token,accessToken);
                data[0].put(social_details,jsonObject);
                System.out.print("\n DDDDaaaTTAA"+data[0]);
                StringEntity se=new StringEntity(data[0].toString());
//                se.setContentEncoding(new BasicHeader(HTTP.CONTENT_TYPE, "application/json"));
                httppost.setEntity(se);
                httppost.setHeader("Accept", "application/json");
                httppost.setHeader("Content-type", "application/json");
                Log.d("myapp", "works till here. 2");
                try {
                    HttpResponse response = httpclient.execute(httppost);
                    String rbody = EntityUtils.toString(response.getEntity());
                    System.out.println("Code : " + rbody);

                    final JSONObject jsonObject1 = new JSONObject(rbody);
                    if (jsonObject != null) {
                        System.out.println("Login Successfully");
                    }
                    Handler handler = new Handler(getContext().getMainLooper());
                    handler.post(new Runnable() {
                        public void run() {
                            try {
                                Toast.makeText(getContext(), jsonObject1.get("msg").toString(), Toast.LENGTH_LONG).show();
                            } catch (JSONException e) {
                                e.printStackTrace();
                            }
                        }
                    });


                    Log.d("myapp", "response " + response.getEntity());
                    int rcode = response.getStatusLine().getStatusCode();
                    System.out.println("Code : " + rbody);
                    System.out.println("Status: " + rcode);
                    if (rcode == 200) {
                        System.out.print("Signupresponsedata set successfully");

                    }

                    System.out.println("Server is available");
                } catch (ClientProtocolException e) {
                    System.out.print(e);
                } catch (IOException e) {
                    System.out.print(e);
                } catch (Exception e) {
                    System.out.println("Server not available" + e.toString());
                }
            } catch (UnsupportedEncodingException e) {
                System.out.print(e);
            }catch (Exception e){
                e.printStackTrace();
            }


            return null;

        }

    }

    class AsyncT extends AsyncTask<Void , Void  , Void> {

        @Override
        protected Void doInBackground(Void ... voids) {


            try {


                HttpClient httpclient = new DefaultHttpClient();
                HttpPost httppost = new HttpPost(BASE_URL+"login/");



//                List<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>();
                JSONObject nameValue =new JSONObject();
                nameValue.put(email,loginRequestData.getUserEmail());
                nameValue.put(password,loginRequestData.getPassword());
                nameValue.put(source,loginRequestData.getSource());
//                nameValuePairs.add((new BasicNameValuePair(email,loginRequestData.getUserEmail())));
//                nameValuePairs.add(new BasicNameValuePair(password,loginRequestData.getPassword()));
//                nameValuePairs.add(new BasicNameValuePair(source,loginRequestData.getSource()));

                Log.e("mainToPost", "mainToPost" + nameValue.toString());
                try {
                    StringEntity se=new StringEntity(nameValue.toString());
                    httppost.setEntity(se);
                    httppost.setHeader("Accept", "application/json");
                    httppost.setHeader("Content-type", "application/json");
                    Log.d("myapp", "works till here. 2");
                    try {
                        HttpResponse response = httpclient.execute(httppost);
                        String rbody = EntityUtils.toString(response.getEntity());

                        System.out.println("Code : " + rbody);

                        final JSONObject jsonObject=new JSONObject(rbody);
                        if(jsonObject!=null){
                            System.out.println("Login Successfully");
                        }


                        Log.d("myapp", "response " + response.getEntity());
                        int rcode = response.getStatusLine().getStatusCode();
                        System.out.println("Status: " + rcode);
                        if(rcode==200){

                            loginResponseData.setAccess_token(jsonObject.get(access_token).toString());
                            loginResponseData.setExpires_in(jsonObject.get(expires_in).toString());
                            loginResponseData.setToken_type(jsonObject.get(token_type).toString());
                            loginResponseData.setIs_email_verified((Boolean)jsonObject.get(is_email_verified));
                            loginResponseData.setScope(jsonObject.get(scope).toString());
                            loginResponseData.setRefresh_token(jsonObject.get(refresh_token).toString());

                            JSONObject jsonObject1= (JSONObject) jsonObject.get(user);
                            UserData userData=new UserData();
                            userData.setStrUserId(jsonObject1.get(id).toString());
                            userData.setStrEmail(jsonObject1.get(email).toString());
                            userData.setStrFullName(jsonObject1.get(full_name).toString());
                            userData.setStrInfluenceScore(jsonObject1.get(infulence_score).toString());
                            userData.setStrFirstName(jsonObject1.get(first_name).toString());
                            userData.setStrMiddleName(jsonObject1.get(middle_name).toString());
                            userData.setStrLastName(jsonObject1.get(last_name).toString());
                            System.out.print("EScope== "+loginResponseData.getScope());
                            System.out.print("UserData   ==="+userData.getStrFirstName());
                            loginResponseData.setUserData(userData);
                            if(!loginResponseData.is_email_verified()){
                               otp_verfication f1=new otp_verfication();
                                f1.setLoginResponseData(loginResponseData);

                                System.out.println(loginResponseData.getUserData().getStrFirstName());
                                setFragment(f1);
                            }
                            Handler handler =  new Handler(getContext().getMainLooper());
                            handler.post( new Runnable(){
                                public void run(){
                                    Toast.makeText(getContext(),"Login Successfully",Toast.LENGTH_SHORT).show();
                                }
                            });


//                            JSONObject jsonArray=jsonObject.getJSONObject("token");
                        }else{
                            Handler handler =  new Handler(getContext().getMainLooper());
                            handler.post( new Runnable(){
                                public void run(){
                                    try {
                                        Toast.makeText(getContext(), jsonObject.get("msg").toString(),Toast.LENGTH_LONG).show();
                                    } catch (JSONException e) {
                                        e.printStackTrace();
                                    }
                                }
                            });
                        }
                        System.out.println("Server is available");
                    } catch (ClientProtocolException e) {
                        e.printStackTrace();

                    } catch (IOException e) {
                        Handler handler =  new Handler(getContext().getMainLooper());
                        handler.post( new Runnable(){
                            public void run(){

                                Toast.makeText(getContext(), " Server under maintenance sorry for inconvenience Try again after some time ",Toast.LENGTH_LONG).show();

                            }
                        });
                        e.printStackTrace();
                    } catch (Exception e) {
                        System.out.println("Server not available"+e.toString());
                    }
                } catch (UnsupportedEncodingException e) {
                    e.printStackTrace();

                }

            } catch (Exception e) {
                Handler handler =  new Handler(getContext().getMainLooper());
                handler.post( new Runnable(){
                    public void run(){

                        Toast.makeText(getContext(), " Server under maintenance sorry for inconvenience  Try again after some time",Toast.LENGTH_LONG).show();

                    }
                });
            }
            return null;
        }

        @Override
        protected void onPreExecute() {
            progressBar.setVisibility(View.VISIBLE);

        }

        @Override
        protected void onPostExecute(Void Void) {
            progressBar.setVisibility(View.GONE);
//            super.onPostExecute(Void);

        }
    }
}
