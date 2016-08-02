package blog.sidvitech.com.authentication.ForgotPass;

import android.annotation.TargetApi;
import android.app.Activity;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.support.design.widget.TextInputLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.text.Editable;
import android.text.TextWatcher;
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

import blog.sidvitech.com.authentication.LogIn.Login;
import blog.sidvitech.com.activity.R;

/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * to handle interaction events.
 * Use the {@link forgotPassword#newInstance} factory method to
 * create an instance of this fragment.
 */
public class forgotPassword extends Fragment implements Animation.AnimationListener{

    private TextView tvrtrnlgnpg;
    private View view;
    private EditText forgot_mail;
    private Button reset_button;
    private TextInputLayout resettextlayout;
    private LinearLayout forgot_layout;
    private Animation animation;
    private Button demo;
    public static final String email="email";

    //  create a textWatcher member for disable and enabel the button
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
            if (!(forgot_mail.getText().toString().contains("@"))) {
                resettextlayout.setError(null);
            }
            checkFieldsForEmptyValues();
        }
    };


    @TargetApi(Build.VERSION_CODES.JELLY_BEAN)
    void checkFieldsForEmptyValues(){
        Button b = reset_button;

        String s1 = forgot_mail.getText().toString();

        if(s1.contains("@")){
            reset_button.setEnabled(true);
            System.out.print("Button activated");
            b.setBackground(getResources().getDrawable(R.drawable.shape));
        } else {
            b.setEnabled(false);
//            b.setBackground(getResources().getDrawable(R.drawable.disableshape));
        }
    }



    public forgotPassword() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment forgotPassword.
     */
    // TODO: Rename and change types and number of parameters
    public static forgotPassword newInstance(String param1, String param2) {
        forgotPassword fragment = new forgotPassword();
        Bundle args = new Bundle();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_forgot_password, container, false);

        ViewGroup.MarginLayoutParams marginLayoutParams;
        marginLayoutParams = (ViewGroup.MarginLayoutParams)container.getLayoutParams();
        marginLayoutParams.setMargins(0,50,0,0);


        forgot_mail = (EditText) view.findViewById(R.id.etrstmail);
        reset_button = (Button) view.findViewById(R.id.btnreset1);
        demo= (Button) view.findViewById(R.id.btndemo);

        resettextlayout = (TextInputLayout) view.findViewById(R.id.resettextlayout);
        tvrtrnlgnpg= (TextView) view.findViewById(R.id.tvrtrnlgnfp);
        forgot_layout = (LinearLayout) view.findViewById(R.id.forgot_layout);

        animation = AnimationUtils.loadAnimation(getContext(),R.anim.bounchanim);
        animation.setAnimationListener(this);

        forgot_layout.setAnimation(animation);

        reset_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
               System.out.print("truuuuueeee");

            }
        });
        demo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                System.out.print(forgot_mail.getText());
            }
        });

        tvrtrnlgnpg.setOnClickListener(new View.OnClickListener() {
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



        reset_button.getBackground().setAlpha(175);

        // set listeners
        forgot_mail.addTextChangedListener(mTextWatcher);
        reset_button.addTextChangedListener(mTextWatcher);

        // run once to disable if empty
        checkFieldsForEmptyValues();
        setupUI(view.findViewById(R.id.forgotactivity));

        forgot_mail.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if (!(forgot_mail.getText().toString().contains("@"))) {
                    resettextlayout.setError("Please enter valid email");
                }
            }
        });

        return view;
    }
        private void attemptlog(){
            String email = forgot_mail.getText().toString();
            View focusView = null ;
            boolean cancel = false;

            if (!isEmailValid(email)) {
                 resettextlayout.setError(getString(R.string.error_invalid_email));
                focusView = forgot_mail;
                cancel = true;
            }else{
                resettextlayout.setError(null);
            }

        }

    private boolean isEmailValid(String email) {
        return email.contains("@");
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



    class Async extends AsyncTask<String,Void,Void>{

        @Override
        protected Void doInBackground(String... m) {
           String mail=m[0];
            System.out.print(email+":"+mail);


            return null;
        }
    }
}
