package blog.sidvitech.com.authentication.Otp_Verification;

import android.content.Context;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.util.EntityUtils;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.List;

import blog.sidvitech.com.activity.R;
import blog.sidvitech.com.models.response.LoginResponseData;
import blog.sidvitech.com.models.response.SignUpResponseData;
import blog.sidvitech.com.models.response.UserData;


/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * to handle interaction events.
 * Use the {@link otp_verfication#newInstance} factory method to
 * create an instance of this fragment.
 */
public class otp_verfication extends Fragment  {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";
    SignUpResponseData signUpResponseData;
    LoginResponseData loginResponseData;
    UserData userData;

    private static final String email="email";
    private static final String otp_number="otp_number";
    private static final String status="status";
    private static final String Registration_OTP="Registration OTP";
    private String user_email;
    private EditText etOTP;
    View view;
    Button btnVerify;
    String otp;
    public static final String BASE_URL = "http://192.168.0.3/api";



    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;
    public otp_verfication(){


    }



    public void setSignUpResponseData(SignUpResponseData signUpResponseData){
        this.signUpResponseData=signUpResponseData;
        userData=signUpResponseData.getUserData();
        Handler handler =  new Handler(getContext().getMainLooper());
        user_email=signUpResponseData.getUserData().getStrEmail();
        handler.post( new Runnable(){
            public void run(){

                Toast.makeText(getContext(), "Call From Sign up fragment ",Toast.LENGTH_LONG).show();

            }
        });
    }

    public void setLoginResponseData(LoginResponseData loginResponseData1){
        try {
            loginResponseData=loginResponseData1;
            System.out.print("Access Token"+loginResponseData1.getAccess_token());
            userData = loginResponseData.getUserData();
            System.out.print(userData.getStrFullName());

            user_email=loginResponseData.getUserData().getStrEmail();

        Handler handler =  new Handler(getContext().getMainLooper());
        handler.post( new Runnable(){
            public void run(){

                Toast.makeText(getContext(), "Call From Login fragment ",Toast.LENGTH_LONG).show();

            }
        });
        }catch (Exception e){
            System.out.print("OTP fragment "+e.toString());
        }
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment otp_verfication.
     */
    // TODO: Rename and change types and number of parameters
    public static otp_verfication newInstance(String param1, String param2) {
        otp_verfication fragment = new otp_verfication();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment

        view = inflater.inflate(R.layout.fragment_otp_verfication, container, false);
        etOTP= (EditText) view.findViewById(R.id.otp_mail);
        btnVerify= (Button) view.findViewById(R.id.btnverify);

        btnVerify.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(etOTP.getText().toString()!=null) {
                    otp = String.valueOf(etOTP.getText());
                }
                new AsyncT().execute();
            }
        });

        return view;
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        btnVerify = (Button)getActivity().findViewById(R.id.btnverify);

    }




    class AsyncT extends AsyncTask<Void , Void  , Void> {
        @Override
        protected Void doInBackground(Void ... voids) {



            try {
                HttpClient httpclient = new DefaultHttpClient();
                HttpPost httppost = new HttpPost(BASE_URL+"/otp/verify/");




                List<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>();
                nameValuePairs.add((new BasicNameValuePair(email,user_email)));
                nameValuePairs.add(new BasicNameValuePair(otp_number,otp));
                nameValuePairs.add(new BasicNameValuePair(status,Registration_OTP));



                Log.e("mainToPost", "mainToPost" + nameValuePairs.toString());
                try {
                    httppost.setEntity(new UrlEncodedFormEntity(nameValuePairs));
                    Log.d("myapp", "works till here. 2");
                    try {
                        HttpResponse response = httpclient.execute(httppost);
                        String rbody = EntityUtils.toString(response.getEntity());

                        System.out.print("String to json"+rbody);

//
                        final JSONObject jsonObject=new JSONObject(rbody);
                        if(jsonObject!=null){
                            System.out.println("Login Successfully");
                        }


                        Log.d("myapp", "response " + response.getEntity());
                        int rcode = response.getStatusLine().getStatusCode();
                        System.out.println("Code : " + rbody);
                        System.out.println("Status: " + rcode);
                        if(rcode==200){


                            Handler handler =  new Handler(getContext().getMainLooper());
                            handler.post( new Runnable(){
                                public void run(){
                                    Toast.makeText(getContext(),"Login Successfully",Toast.LENGTH_SHORT).show();
                                }
                            });


//                            JSONObject jsonArray=jsonObject.getJSONObject("token");
                        }else{
//                            Handler handler =  new Handler(getContext().getMainLooper());
//                            handler.post( new Runnable(){
//                                public void run(){
//                                    try {
////                                        Toast.makeText(getContext(), jsonObject.get("msg").toString(),Toast.LENGTH_LONG).show();
//                                    } catch (JSONException e) {
//                                        e.printStackTrace();
//                                    }
//                                }
//                            });
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
        protected void onPostExecute(Void aVoid) {
            super.onPostExecute(aVoid);

        }
    }

}
