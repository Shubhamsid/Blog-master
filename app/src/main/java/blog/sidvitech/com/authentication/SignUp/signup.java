package blog.sidvitech.com.authentication.SignUp;


import android.annotation.TargetApi;
import android.app.Activity;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.support.design.widget.TextInputLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.util.EntityUtils;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.List;

import blog.sidvitech.com.authentication.LogIn.Login;
import blog.sidvitech.com.activity.R;
import blog.sidvitech.com.authentication.Otp_Verification.otp_verfication;
import blog.sidvitech.com.models.request.SignUpRequestData;
import blog.sidvitech.com.models.response.SignUpResponseData;


/**
 * A simple {@link Fragment} subclass.
 */
public class signup extends Fragment implements Animation.AnimationListener {

    private static final String access_token="access_token";
    private static final String token_type="token_type";
    private static final String expires_in="expires_in";
    private static final String refresh_token="refresh_token";
    private static final String scope="scope";
    public static final String passwod="password";
    public static final String name="full_name";
    public static final String mail="email";
    public static final String confirm_password="confirm_password";
    public static final String source="source";
    public static final String BASE_URL = "http://192.168.0.3/api/user/";

    private TextInputLayout namelayout,unamelayout,passlayout,compasslayout,phonelayout;
    private TextView tvsgnpg;
    private View view;
    private Button submite;
    private EditText reg_name, reg_uname, reg_pass, reg_conpass, reg_phone;
    private SignUpRequestData signUpRequestData;
    private SignUpResponseData signUpResponseData;
    private Animation animator, exit_animation;
    private LinearLayout signup_layout;

    private TextWatcher mTextWatcher = new TextWatcher() {
        @Override
        public void beforeTextChanged(CharSequence charSequence, int i, int i2, int i3) {
        }

        @Override
        public void onTextChanged(CharSequence charSequence, int i, int i2, int i3) {
        }

        @Override
        public void afterTextChanged(Editable editable) {
            // check Fields For Empty Values
//            if (!(reg_name.getText().toString().equals(""))) {
//                namelayout.setError(null);
//            }if (!(reg_uname.getText().toString().equals(""))) {
//                unamelayout.setError(null);
//            }if (!(reg_pass.getText().toString().equals(""))) {
//                passlayout.setError(null);
//            }if (!(reg_conpass.getText().toString().equals(""))) {
//                compasslayout.setError(null);
//            }
            checkFieldsForEmptyValues();
        }
    };

    @TargetApi(Build.VERSION_CODES.JELLY_BEAN)
    void checkFieldsForEmptyValues(){
        Button b = submite;

        String s1 = reg_name.getText().toString();
        String s2 = reg_uname.getText().toString();
        String s3 = reg_pass.getText().toString();
        String s4 = reg_conpass.getText().toString();

        if(s1.equals("")){
            b.setEnabled(false);
            b.setBackground(getResources().getDrawable(R.drawable.disableshape));
        }else if (s2.equals("")){
            b.setEnabled(false);
            b.setBackground(getResources().getDrawable(R.drawable.disableshape));
        }else if (s3.equals("")){
            b.setEnabled(false);
            b.setBackground(getResources().getDrawable(R.drawable.disableshape));
        }else if (s4.equals("")){
            b.setEnabled(false);
            b.setBackground(getResources().getDrawable(R.drawable.disableshape));
        }else {
            b.setEnabled(true);
            b.setBackground(getResources().getDrawable(R.drawable.shape));
        }
    }
    public signup() {
        // Required empty public constructor
    }

    void init(View view){
        signUpRequestData = new SignUpRequestData();
        signUpResponseData = new SignUpResponseData();

        reg_name = (EditText) view.findViewById(R.id.reg_name);
        reg_uname = (EditText) view.findViewById(R.id.reg_email);
        reg_pass = (EditText) view.findViewById(R.id.reg_pass);
        reg_conpass = (EditText) view.findViewById(R.id.reg_confirmpass);
        reg_phone = (EditText) view.findViewById(R.id.reg_phone);
        namelayout = (TextInputLayout) view.findViewById(R.id.register_layout);
        unamelayout = (TextInputLayout) view.findViewById(R.id.uname_layout);
        passlayout = (TextInputLayout) view.findViewById(R.id.pass_layout);
        compasslayout = (TextInputLayout) view.findViewById(R.id.compass_layout);
        submite = (Button) view.findViewById(R.id.btnsignup);




    }

    public void setFragment(Fragment f) {
        FragmentManager fm = getActivity().getSupportFragmentManager();
        FragmentTransaction ft = fm.beginTransaction();
        ft.replace(R.id.flfrholder, f);
        ft.addToBackStack(null);
        ft.commit();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        view = inflater.inflate(R.layout.fragment_signup, container, false);
        init(view);

        submite.getBackground().setAlpha(175);

        reg_name.addTextChangedListener(mTextWatcher);
        reg_uname.addTextChangedListener(mTextWatcher);
        reg_pass.addTextChangedListener(mTextWatcher);
        reg_conpass.addTextChangedListener(mTextWatcher);

        animator = AnimationUtils.loadAnimation(getContext(),R.anim.bounchanim);
        animator.setAnimationListener(this);

        ViewGroup.MarginLayoutParams marginLayoutParams;
        marginLayoutParams = (ViewGroup.MarginLayoutParams)container.getLayoutParams();
        marginLayoutParams.setMargins(0,50,0,0);

        signup_layout = (LinearLayout) view.findViewById(R.id.signup_layout);
        signup_layout.setAnimation(animator);

        checkFieldsForEmptyValues();

        setupUI(view.findViewById(R.id.signup_activity));
        tvsgnpg= (TextView) view.findViewById(R.id.login_link);
        tvsgnpg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Fragment f=new Login();
                FragmentManager fm=getActivity().getSupportFragmentManager();
                FragmentTransaction ft=fm.beginTransaction();
                ft.replace(R.id.flfrholder,f);
                ft.addToBackStack(null);
                ft.commit();
            }
        });

        submite.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                signUpRequestData.setUserFullName(reg_name.getText().toString());
                signUpRequestData.setUserEmail(reg_uname.getText().toString());
                signUpRequestData.setPassword(reg_pass.getText().toString());
                signUpRequestData.setConfirmPassword(reg_conpass.getText().toString());
                new AsyncT().execute();
            }
        });
        return view;
    }


    public void setupUI(View view) {

        //Set up touch listener for non-text box views to hide keyboard.
        if(!(view instanceof EditText)) {

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
        InputMethodManager inputMethodManager = (InputMethodManager)  activity.getSystemService(Activity.INPUT_METHOD_SERVICE);
        if (activity.getCurrentFocus()!=null)
            inputMethodManager.hideSoftInputFromWindow(activity.getCurrentFocus().getWindowToken(), 0);
    }

    @Override
    public void onAnimationStart(Animation animation) {

    }

    @Override
    public void onAnimationEnd(Animation animation) {

    }

    @Override
    public void onAnimationRepeat(Animation animation) {

    }

    class AsyncT extends AsyncTask<Void , Void  , Void> {
        @Override
        protected Void doInBackground(Void ... voids) {
            HttpClient httpclient = new DefaultHttpClient();
            HttpPost httppost = new HttpPost(BASE_URL+"register/");

            try {

//                List<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>();
                JSONObject nameValue=new JSONObject();
                nameValue.put(passwod,signUpRequestData.getPassword());
                nameValue.put(confirm_password,signUpRequestData.getConfirmPassword());
                nameValue.put(mail,signUpRequestData.getUserEmail());
                nameValue.put(name,signUpRequestData.getUserFullName());
                nameValue.put(source,signUpRequestData.getSource());
//                nameValuePairs.add(new BasicNameValuePair(passwod,signUpRequestData.getPassword()));
//                nameValuePairs.add(new BasicNameValuePair(confirm_password,signUpRequestData.getConfirmPassword()));
//                nameValuePairs.add(new BasicNameValuePair(mail,signUpRequestData.getUserEmail()));
//                nameValuePairs.add(new BasicNameValuePair(name,signUpRequestData.getUserFullName()));
//                nameValuePairs.add(new BasicNameValuePair(source,signUpRequestData.getSource()));
                Log.e("mainToPost", "mainToPost" + nameValue.toString());
                try {
//                    httppost.setEntity(new UrlEncodedFormEntity(nameValuePairs));

                    StringEntity se=new StringEntity(nameValue.toString());
                    httppost.setEntity(se);
                    httppost.setHeader("Accept", "application/json");
                    httppost.setHeader("Content-type", "application/json");

                    Log.d("myapp", "works till here. 2");
                    try {
                        HttpResponse response = httpclient.execute(httppost);
                        String rbody = EntityUtils.toString(response.getEntity());

                        final JSONObject jsonObject=new JSONObject(rbody);
                        if(jsonObject!=null){
                            System.out.println("Login Successfully");
                        }
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


                        Log.d("myapp", "response " + response.getEntity());
                        int rcode = response.getStatusLine().getStatusCode();
                        System.out.println("Code : " + rbody);
                        System.out.println("Status: " + rcode);
                        if(rcode==200){
                            JSONObject jsonArray=jsonObject.getJSONObject("token");
                            signUpResponseData.setAccess_token(jsonArray.get(access_token).toString());
                            signUpResponseData.setExpires_in(jsonArray.get(expires_in).toString());
                            signUpResponseData.setRefresh_token(jsonArray.get(refresh_token).toString());
                            signUpResponseData.setScope(jsonArray.get(scope).toString());
                            signUpResponseData.setToken_type(token_type);
                            System.out.print("Signupresponsedata set successfully");



                            otp_verfication f=new otp_verfication();
                            f.setSignUpResponseData(signUpResponseData);
                                setFragment(f);


                        }

                        System.out.println("Server is available");
                    } catch (ClientProtocolException e) {
                        e.printStackTrace();
                    } catch (IOException e) {
                        e.printStackTrace();
                    } catch (Exception e) {
                        System.out.println("Server not available"+e.toString());
                    }
                } catch (UnsupportedEncodingException e) {
                    e.printStackTrace();
                }

            } catch (Exception e) {

            }
            return null;
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            super.onPostExecute(aVoid);

        }
    }

}
