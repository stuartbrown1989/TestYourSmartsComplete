package com.example.testyoursmarts;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.navigation.ui.AppBarConfiguration;

import android.content.Intent;
import android.os.Bundle;
import android.util.Patterns;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import org.w3c.dom.Text;

import java.text.DecimalFormat;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

import static com.example.testyoursmarts.dbQuery.g_question_list;
import static com.example.testyoursmarts.dbQuery.statisticTime;
import static com.example.testyoursmarts.dbQuery.userScoreList;

public class account_page extends AppCompatActivity {


    public static FirebaseFirestore g_firestore;
    private String quizType;
    private Spinner spinner;
    private String userEmail;
    private String guestEmail = "123@hotmail.com";
    private static final String[] options = {
            "Select Quiz Type",
            "General Knowledge",
            "Picture",
            "Speed Run",
            "Topic - FilmTV",
            "Topic - Geography",
            "Topic - Music",
            "Topic - History",
            "Topic - Science",
            "Topic - Sports",
            "All Quizzes"};
    private AppBarConfiguration mAppBarConfiguration;
    private DocumentReference alleasyref, alleasyPercentageRef;
    private  DocumentReference allmediumref, allMediumPercentageRef;
    private DocumentReference allhardref, allHardPercentageRef;
    private FirebaseAuth firebaseAuth;
    private TextView easytime, mediumtime, hardtime, easyPercentage, mediumPercentage, hardPercentage;
    private Double
            checkEasy, checkMedium, checkHard,
            checkFilmTVEasy, checkFilmTVMedium, checkFilmTVHard,
            checkGKEasy, checkGKMedium , checkGKHard,
            checkGeoEasy , checkGeoMedium, checkGeoHard,
            checkHistoryEasy, checkHistoryMedium, checkHistoryHard,
            checkMusicEasy, checkMusicMedium, checkMusicHard,
            checkPictureEasy, checkPictureMedium, checkPictureHard,
            checkScienceEasy, checkScienceMedium, checkScienceHard,
            checkSportsEasy, checkSportsMedium, checkSportsHard,
            checkSpeedEasy, checkSpeedMedium, checkSpeedHard,

    checkPercentageEasy, checkPercentageMedium, checkPercentageHard,
    checkFilmTVPercentageEasy, checkFilmTVPercentageMedium, checkFilmTVPercentageHard,
    checkGKPercentageEasy, checkGKPercentageMedium , checkGKPercentageHard,
    checkGeoPercentageEasy , checkGeoPercentageMedium, checkGeoPercentageHard,
    checkHistoryPercentageEasy, checkHistoryPercentageMedium, checkHistoryPercentageHard,
    checkMusicPercentageEasy, checkMusicPercentageMedium, checkMusicPercentageHard,
    checkPicturePercentageEasy, checkPicturePercentageMedium, checkPicturePercentageHard,
    checkSciencePercentageEasy, checkSciencePercentageMedium, checkSciencePercentageHard,
    checkSportsPercentageEasy, checkSportsPercentageMedium, checkSportsPercentageHard,
    checkSpeedPercentageEasy, checkSpeedPercentageMedium, checkSpeedPercentageHard;

    private BottomNavigationView bottomNavigationView;
    private FrameLayout main_frame;
    private BottomNavigationView.OnNavigationItemSelectedListener onNavigationItemReselectedListener =
            new BottomNavigationView.OnNavigationItemSelectedListener() {
                @Override
                public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {

                    switch (menuItem.getItemId()) {
                        case R.id.nav_home:
                            Intent intentHome = new Intent(account_page.this, MainActivity.class);
                            startActivity(intentHome);
                            return true;

                        case R.id.nav_leaderboard:
                            Intent intentleaderB = new Intent(account_page.this, leaderboardChoiceActivity.class);
                            startActivity(intentleaderB);
                            return true;

                        case R.id.nav_account:
                            Intent intentAccount = new Intent(account_page.this, account_page.class);
                            startActivity(intentAccount);
                            return true;

                        case R.id.nav_logout:
                            FirebaseAuth.getInstance().signOut();
                            GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                                    .requestIdToken(getString(R.string.default_web_client_id))
                                    .requestEmail()
                                    .build();
                            GoogleSignInClient mGoogleClient = GoogleSignIn.getClient(getBaseContext(), gso);
                            mGoogleClient.signOut().addOnCompleteListener(new OnCompleteListener<Void>() {
                                @Override
                                public void onComplete(@NonNull Task<Void> task) {
                                    Intent intentLogout = new Intent(getBaseContext(), LoginActivity.class);
                                    intentLogout.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                                    startActivity(intentLogout);
                                    finish();
                                }
                            });


                    }
                    return false;
                }
            };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        FirebaseUser firebaseUser = FirebaseAuth.getInstance().getCurrentUser();
        userEmail = firebaseUser.getEmail();
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_account_page);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle("Test Your Smarts: Statistics");
        bottomNavigationView = findViewById(R.id.top_nav_bar);
        main_frame = findViewById(R.id.main_frame);
        bottomNavigationView.setOnNavigationItemSelectedListener(onNavigationItemReselectedListener);
        g_firestore = FirebaseFirestore.getInstance();
        easytime = findViewById(R.id.account_easy_value);
        mediumtime = findViewById(R.id.account_medium_value);
        hardtime = findViewById(R.id.account_hard_value);
        easyPercentage = findViewById(R.id.account_easy_percentageValue);
        mediumPercentage = findViewById(R.id.account_Medium_percentageValue);
        hardPercentage = findViewById(R.id.account_Hard_percentageValue);
        spinner = findViewById(R.id.spinner_statistics);

        ArrayAdapter<String> adapter = new ArrayAdapter<String>(account_page.this, android.R.layout.simple_spinner_item, options);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner.setAdapter(adapter);
        //Initialize all times from function
        getcombinedTime();
        getPercentage();
        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                DecimalFormat form = new DecimalFormat("0.0");
                //Based on selected item from Dropdown, setText to the appropriate values
                switch(position){

                    case 0:
                        easytime.setText("Select Quiz Type");
                        mediumtime.setText("Select Quiz Type");
                        hardtime.setText("Select Quiz Type");
                        easyPercentage.setText("Select Quiz Type");
                        mediumPercentage.setText("Select Quiz Type");
                        hardPercentage.setText("Select Quiz Type");
                        break;
                    case 1:
                        guestLoggedinMsg();
                        easytime.setText(form.format(checkGKEasy));
                        mediumtime.setText(form.format(checkGKMedium));
                        hardtime.setText(form.format(checkGKHard));
                        easyPercentage.setText(form.format(checkGKPercentageEasy) + "%");
                        mediumPercentage.setText(form.format(checkGKPercentageMedium) + "%");
                        hardPercentage.setText(form.format(checkGKPercentageHard) + "%");
                        break;
                    case 2:
                        guestLoggedinMsg();
                        easytime.setText(form.format(checkPictureEasy));
                        mediumtime.setText(form.format(checkPictureMedium));
                        hardtime.setText(form.format(checkPictureHard));
                        easyPercentage.setText(form.format(checkPicturePercentageEasy) + "%");
                        mediumPercentage.setText(form.format(checkPicturePercentageMedium) + "%");
                        hardPercentage.setText(form.format(checkPicturePercentageHard) + "%");
                        break;
                    case 3:
                        guestLoggedinMsg();
                        easytime.setText(form.format(checkSpeedEasy));
                        mediumtime.setText(form.format(checkSpeedMedium));
                        hardtime.setText(form.format(checkSpeedHard));
                        easyPercentage.setText(form.format(checkSpeedPercentageEasy) + "%");
                        mediumPercentage.setText(form.format(checkSpeedPercentageMedium) + "%");
                        hardPercentage.setText(form.format(checkSpeedPercentageHard) + "%");
                        break;
                    case 4:
                        guestLoggedinMsg();
                        easytime.setText(form.format(checkFilmTVEasy));
                        mediumtime.setText(form.format(checkFilmTVMedium));
                        hardtime.setText(form.format(checkFilmTVHard));
                        easyPercentage.setText(form.format(checkFilmTVPercentageEasy) + "%");
                        mediumPercentage.setText(form.format(checkFilmTVPercentageMedium) + "%");
                        hardPercentage.setText(form.format(checkFilmTVPercentageHard) + "%");
                        break;
                    case 5:
                        guestLoggedinMsg();
                        easytime.setText(form.format(checkGeoEasy));
                        mediumtime.setText(form.format(checkGeoMedium));
                        hardtime.setText(form.format(checkGeoHard));
                        easyPercentage.setText(form.format(checkGeoPercentageEasy) + "%");
                        mediumPercentage.setText(form.format(checkGeoPercentageMedium) + "%");
                        hardPercentage.setText(form.format(checkGeoPercentageHard) + "%");
                        break;
                    case 6:
                        guestLoggedinMsg();
                        easytime.setText(form.format(checkMusicEasy));
                        mediumtime.setText(form.format(checkMusicMedium));
                        hardtime.setText(form.format(checkMusicHard));
                        easyPercentage.setText(form.format(checkMusicPercentageEasy) + "%");
                        mediumPercentage.setText(form.format(checkMusicPercentageMedium) + "%");
                        hardPercentage.setText(form.format(checkMusicPercentageHard) + "%");
                        break;
                    case 7:
                        guestLoggedinMsg();
                        easytime.setText(form.format(checkHistoryEasy));
                        mediumtime.setText(form.format(checkHistoryMedium));
                        hardtime.setText(form.format(checkHistoryHard));
                        easyPercentage.setText(form.format(checkHistoryPercentageEasy) + "%");
                        mediumPercentage.setText(form.format(checkHistoryPercentageMedium) + "%");
                        hardPercentage.setText(form.format(checkHistoryPercentageHard) + "%");
                        break;
                    case 8:
                        guestLoggedinMsg();
                        easytime.setText(form.format(checkScienceEasy));
                        mediumtime.setText(form.format(checkScienceMedium));
                        hardtime.setText(form.format(checkScienceHard));
                        easyPercentage.setText(form.format(checkSciencePercentageEasy) + "%");
                        mediumPercentage.setText(form.format(checkSciencePercentageMedium) + "%");
                        hardPercentage.setText(form.format(checkSciencePercentageHard) + "%");
                        break;
                    case 9:
                        guestLoggedinMsg();
                        easytime.setText(form.format(checkSportsEasy));
                        mediumtime.setText(form.format(checkSportsMedium));
                        hardtime.setText(form.format(checkSportsHard));
                        easyPercentage.setText(form.format(checkSportsPercentageEasy) + "%");
                        mediumPercentage.setText(form.format(checkSportsPercentageMedium) + "%");
                        hardPercentage.setText(form.format(checkSportsPercentageHard) + "%");
                        break;
                    case 10:
                        guestLoggedinMsg();
                        easytime.setText(form.format(checkEasy));
                        mediumtime.setText(form.format(checkMedium));
                        hardtime.setText(form.format(checkHard));
                        easyPercentage.setText(form.format(checkPercentageEasy) + "%");
                        mediumPercentage.setText(form.format(checkPercentageMedium) + "%");
                        hardPercentage.setText(form.format(checkPercentageHard) + "%");
                        break;
                }
            }
            @Override
            public void onNothingSelected(AdapterView<?> parent) {
                easytime.setText("Select Quiz Type");
                mediumtime.setText("Select Quiz Type");
                hardtime.setText("Select Quiz Type");
            }
        });
    }
//Function getting overall average from Firestore
    private void getcombinedTime() {
        //If userEmail upon checking Current User is null, then it is a guest login, so change the email address to a default one, so all fields will be 0
if(userEmail == null)
{
    userEmail = "123@hotmail.com";
}
       if(!Patterns.EMAIL_ADDRESS.matcher(userEmail).matches())
        {
            alleasyref = g_firestore.collection("Statistics").document("Easy").collection("Users").document(guestEmail);
        }
        if(Patterns.EMAIL_ADDRESS.matcher(userEmail).matches())
        {
            alleasyref = g_firestore.collection("Statistics").document("Easy").collection("Users").document(userEmail);
        }
        //Checking Statistics with document name of users email address
          alleasyref.get()
                  .addOnFailureListener(new OnFailureListener() {
                      @Override
                      public void onFailure(@NonNull Exception e) {
                          checkSpeedEasy = 0.0;
                          checkSportsEasy = 0.0;
                          checkScienceEasy = 0.0;
                          checkPictureEasy = 0.0;
                          checkMusicEasy = 0.0;
                          checkHistoryEasy = 0.0;
                          checkGeoEasy = 0.0;
                          checkGKEasy = 0.0;
                          checkFilmTVEasy = 0.0;
                          checkEasy = 0.0;
                          Toast.makeText(account_page.this, "Login with an account to record Statistics", Toast.LENGTH_SHORT).show();
                      }
                  })
                  .addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
                      @Override
                      public void onSuccess(DocumentSnapshot snapshot) {
                          //If document with email address as id exist - Show average stored
                          if (snapshot.exists()) {
                              checkEasy = snapshot.getDouble("Average Time");
                              checkFilmTVEasy = snapshot.getDouble("FilmTV Average");
                              checkGKEasy = snapshot.getDouble("GK Average");
                              checkGeoEasy = snapshot.getDouble("Geography Average");
                              checkHistoryEasy = snapshot.getDouble("History Average");
                              checkMusicEasy = snapshot.getDouble("Music Average");
                              checkPictureEasy = snapshot.getDouble("Picture Average");
                              checkScienceEasy = snapshot.getDouble("Science Average");
                              checkSportsEasy = snapshot.getDouble("Sports Average");
                              checkSpeedEasy = snapshot.getDouble("Speed Run Average");
                          }
                          //If any Topic is non existing in firestore, just set the value to 0.0
                          //Has to be given a formatted value, as the textview is formatted to this, and it will throw an exception if it is null
                          if (checkSpeedEasy == null) {
                              checkSpeedEasy = 0.0;
                          }
                          if (checkEasy == null) {
                              checkEasy = 0.0;
                          }
                          if (checkFilmTVEasy == null) {
                              checkFilmTVEasy = 0.0;
                          }
                          if (checkGKEasy == null) {
                              checkGKEasy = 0.0;
                          }
                          if (checkHistoryEasy == null) {
                              checkHistoryEasy = 0.0;
                          }
                          if (checkGeoEasy == null) {
                              checkGeoEasy = 0.0;
                          }
                          if (checkMusicEasy == null) {
                              checkMusicEasy = 0.0;
                          }
                          if (checkPictureEasy == null) {
                              checkPictureEasy = 0.0;
                          }
                          if (checkScienceEasy == null) {
                              checkScienceEasy = 0.0;
                          }
                          if (checkSportsEasy == null) {
                              checkSportsEasy = 0.0;
                          }
                          //If no doc exists, value is 0.0
                          if (!snapshot.exists()) {
                              checkSpeedEasy = 0.0;
                              checkSportsEasy = 0.0;
                              checkScienceEasy = 0.0;
                              checkPictureEasy = 0.0;
                              checkMusicEasy = 0.0;
                              checkHistoryEasy = 0.0;
                              checkGeoEasy = 0.0;
                              checkGKEasy = 0.0;
                              checkFilmTVEasy = 0.0;
                              checkEasy = 0.0;
                          }
                      }
                  });
        if(!Patterns.EMAIL_ADDRESS.matcher(userEmail).matches())
        {
            allmediumref = g_firestore.collection("Statistics").document("Medium").collection("Users").document(guestEmail);
        }
        if(Patterns.EMAIL_ADDRESS.matcher(userEmail).matches())
        {
            allmediumref = g_firestore.collection("Statistics").document("Medium").collection("Users").document(userEmail);
        }
          allmediumref.get()
                  .addOnFailureListener(new OnFailureListener() {
                      @Override
                      public void onFailure(@NonNull Exception e) {
                          checkSpeedMedium = 0.0;
                          checkSportsMedium = 0.0;
                          checkScienceMedium = 0.0;
                          checkPictureMedium = 0.0;
                          checkMusicMedium = 0.0;
                          checkHistoryMedium = 0.0;
                          checkGeoMedium = 0.0;
                          checkGKMedium = 0.0;
                          checkFilmTVMedium = 0.0;
                          checkMedium = 0.0;
                          Toast.makeText(account_page.this, "Login with an account to record Statistics", Toast.LENGTH_SHORT).show();
                      }
                  })
                  .addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
                      @Override
                      public void onSuccess(DocumentSnapshot snapshot) {
                          //If document with email address as id exist - Show average stored
                          if (snapshot.exists()) {
                              checkMedium = snapshot.getDouble("Average Time");
                              checkFilmTVMedium = snapshot.getDouble("FilmTV Average");
                              checkGKMedium = snapshot.getDouble("GK Average");
                              checkGeoMedium = snapshot.getDouble("Geography Average");
                              checkHistoryMedium = snapshot.getDouble("History Average");
                              checkMusicMedium = snapshot.getDouble("Music Average");
                              checkPictureMedium = snapshot.getDouble("Picture Average");
                              checkScienceMedium = snapshot.getDouble("Science Average");
                              checkSportsMedium = snapshot.getDouble("Sports Average");
                              checkSpeedMedium = snapshot.getDouble("Speed Run Average");
                          }
                          //If any Topic is non existing in firestore, just set the value to 0.0
                          //Has to be given a formatted value, as the textview is formatted to this, and it will throw an exception if it is null
                          if (checkSpeedMedium == null) {
                              checkSpeedMedium = 0.0;
                          }
                          if (checkMedium == null) {
                              checkMedium = 0.0;
                          }
                          if (checkFilmTVMedium == null) {
                              checkFilmTVMedium = 0.0;
                          }
                          if (checkGKMedium == null) {
                              checkGKMedium = 0.0;
                          }
                          if (checkHistoryMedium == null) {
                              checkHistoryMedium = 0.0;
                          }
                          if (checkGeoMedium == null) {
                              checkGeoMedium = 0.0;
                          }
                          if (checkMusicMedium == null) {
                              checkMusicMedium = 0.0;
                          }
                          if (checkPictureMedium == null) {
                              checkPictureMedium = 0.0;
                          }
                          if (checkScienceMedium == null) {
                              checkScienceMedium = 0.0;
                          }
                          if (checkSportsMedium == null) {
                              checkSportsMedium = 0.0;
                          }
                          //If no doc exists, value is 0.0
                          if (!snapshot.exists()) {
                              checkSpeedMedium = 0.0;
                              checkSportsMedium = 0.0;
                              checkScienceMedium = 0.0;
                              checkPictureMedium = 0.0;
                              checkMusicMedium = 0.0;
                              checkHistoryMedium = 0.0;
                              checkGeoMedium = 0.0;
                              checkGKMedium = 0.0;
                              checkFilmTVMedium = 0.0;
                              checkMedium = 0.0;
                          }
                      }
                  });
        if(!Patterns.EMAIL_ADDRESS.matcher(userEmail).matches())
        {
            allhardref = g_firestore.collection("Statistics").document("Hard").collection("Users").document(guestEmail);
        }
        if(Patterns.EMAIL_ADDRESS.matcher(userEmail).matches())
        {
            allhardref = g_firestore.collection("Statistics").document("Hard").collection("Users").document(userEmail);
        }
          allhardref.get()
                  .addOnFailureListener(new OnFailureListener() {
                      @Override
                      public void onFailure(@NonNull Exception e) {
                          checkSpeedHard = 0.0;
                          checkSportsHard = 0.0;
                          checkScienceHard = 0.0;
                          checkPictureHard = 0.0;
                          checkMusicHard = 0.0;
                          checkHistoryHard = 0.0;
                          checkGeoHard = 0.0;
                          checkGKHard = 0.0;
                          checkFilmTVHard = 0.0;
                          checkHard = 0.0;
                          Toast.makeText(account_page.this, "Login with an account to record Statistics", Toast.LENGTH_SHORT).show();
                      }
                  })
                  .addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
                      @Override
                      public void onSuccess(DocumentSnapshot snapshot) {
                          //If document with email address as id exist - Show average stored
                          if (snapshot.exists()) {
                              FirebaseUser firebaseUser = FirebaseAuth.getInstance().getCurrentUser();
                              String userEmail = firebaseUser.getEmail();
                              checkHard = snapshot.getDouble("Average Time");
                              checkFilmTVHard = snapshot.getDouble("FilmTV Average");
                              checkGKHard = snapshot.getDouble("GK Average");
                              checkGeoHard = snapshot.getDouble("Geography Average");
                              checkHistoryHard = snapshot.getDouble("History Average");
                              checkMusicHard = snapshot.getDouble("Music Average");
                              checkPictureHard = snapshot.getDouble("Picture Average");
                              checkScienceHard = snapshot.getDouble("Science Average");
                              checkSportsHard = snapshot.getDouble("Sports Average");
                              checkSpeedHard = snapshot.getDouble("Speed Run Average");

                              //If any Topic is non existing in firestore, just set the value to 0.0
                              //Has to be given a formatted value, as the textview is formatted to this, and it will throw an exception if it is null
                              if (checkSpeedHard == null) {
                                  checkSpeedHard = 0.0;
                              }
                              if (checkHard == null) {
                                  checkHard = 0.0;
                              }
                              if (checkFilmTVHard == null) {
                                  checkFilmTVHard = 0.0;
                              }
                              if (checkGKHard == null) {
                                  checkGKHard = 0.0;
                              }
                              if (checkHistoryHard == null) {
                                  checkHistoryHard = 0.0;
                              }
                              if (checkGeoHard == null) {
                                  checkGeoHard = 0.0;
                              }
                              if (checkMusicHard == null) {
                                  checkMusicHard = 0.0;
                              }
                              if (checkPictureHard == null) {
                                  checkPictureHard = 0.0;
                              }
                              if (checkScienceHard == null) {
                                  checkScienceHard = 0.0;
                              }
                              if (checkSportsHard == null) {
                                  checkSportsHard = 0.0;
                              }
                          }
                          //If no doc exists, value is 0.0
                          if (!snapshot.exists()) {
                              checkSpeedHard = 0.0;
                              checkSportsHard = 0.0;
                              checkScienceHard = 0.0;
                              checkPictureHard = 0.0;
                              checkMusicHard = 0.0;
                              checkHistoryHard = 0.0;
                              checkGeoHard = 0.0;
                              checkGKHard = 0.0;
                              checkFilmTVHard = 0.0;
                              checkHard = 0.0;
                          }
                      }
                  });
        }

       private void getPercentage()
       {
           if(userEmail == null)
           {
               userEmail = "123@hotmail.com";
           }
           if(!Patterns.EMAIL_ADDRESS.matcher(userEmail).matches())
           {
               alleasyref = g_firestore.collection("Statistics").document("Easy").collection("Users").document(guestEmail);
           }
           if(Patterns.EMAIL_ADDRESS.matcher(userEmail).matches())
           {
               alleasyref = g_firestore.collection("Statistics").document("Easy").collection("Users").document(userEmail);
           }
           //Checking Statistics with document name of users email address
           alleasyref.get()
                   .addOnFailureListener(new OnFailureListener() {
                       @Override
                       public void onFailure(@NonNull Exception e) {
                           checkSpeedPercentageEasy = 0.0;
                           checkSportsPercentageEasy = 0.0;
                           checkSciencePercentageEasy = 0.0;
                           checkPicturePercentageEasy = 0.0;
                           checkMusicPercentageEasy = 0.0;
                           checkHistoryPercentageEasy = 0.0;
                           checkGeoPercentageEasy = 0.0;
                           checkGKPercentageEasy = 0.0;
                           checkFilmTVPercentageEasy = 0.0;
                           checkPercentageEasy = 0.0;
                           checkPercentageMedium = 0.0;
                           checkPercentageHard = 0.0;
                           Toast.makeText(account_page.this, "Login with an account to record Statistics", Toast.LENGTH_SHORT).show();
                       }
                   })
                   .addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
                       @Override
                       public void onSuccess(DocumentSnapshot snapshot) {
                           //If document with email address as id exist - Show average stored
                           if (snapshot.exists()) {
                               checkPercentageEasy = snapshot.getDouble("Correct Percentage");
                               checkFilmTVPercentageEasy = snapshot.getDouble("FilmTV Correct Answer Percentage");
                               checkGKPercentageEasy = snapshot.getDouble("GK Correct Answer Percentage");
                               checkGeoPercentageEasy = snapshot.getDouble("Geography Correct Answer Percentage");
                               checkHistoryPercentageEasy = snapshot.getDouble("History Correct Answer Percentage");
                               checkMusicPercentageEasy = snapshot.getDouble("Music Correct Answer Percentage");
                               checkPicturePercentageEasy = snapshot.getDouble("Picture Correct Answer Percentage");
                               checkSciencePercentageEasy = snapshot.getDouble("Science Correct Answer Percentage");
                               checkSportsPercentageEasy = snapshot.getDouble("Sports Correct Answer Percentage");
                               checkSpeedPercentageEasy = snapshot.getDouble("Speed Run Correct Answer Percentage");
                           }
                           //If any Topic is non existing in firestore, just set the value to 0.0
                           //Has to be given a formatted value, as the textview is formatted to this, and it will throw an exception if it is null
                           if (checkSpeedPercentageEasy == null) {
                               checkSpeedPercentageEasy = 0.0;
                           }
                           if (checkPercentageEasy == null) {
                               checkPercentageEasy = 0.0;
                           }
                           if (checkFilmTVPercentageEasy == null) {
                               checkFilmTVPercentageEasy = 0.0;
                           }
                           if (checkGKPercentageEasy == null) {
                               checkGKPercentageEasy = 0.0;
                           }
                           if (checkHistoryPercentageEasy == null) {
                               checkHistoryPercentageEasy = 0.0;
                           }
                           if (checkGeoPercentageEasy == null) {
                               checkGeoPercentageEasy = 0.0;
                           }
                           if (checkMusicPercentageEasy == null) {
                               checkMusicPercentageEasy = 0.0;
                           }
                           if (checkPicturePercentageEasy == null) {
                               checkPicturePercentageEasy = 0.0;
                           }
                           if (checkSciencePercentageEasy == null) {
                               checkSciencePercentageEasy = 0.0;
                           }
                           if (checkSportsPercentageEasy == null) {
                               checkSportsPercentageEasy = 0.0;
                           }
                           //If no doc exists, value is 0.0
                           if (!snapshot.exists()) {
                               checkSpeedPercentageEasy = 0.0;
                               checkSportsPercentageEasy = 0.0;
                               checkSciencePercentageEasy = 0.0;
                               checkPicturePercentageEasy = 0.0;
                               checkMusicPercentageEasy = 0.0;
                               checkHistoryPercentageEasy = 0.0;
                               checkGeoPercentageEasy = 0.0;
                               checkGKPercentageEasy = 0.0;
                               checkFilmTVPercentageEasy = 0.0;
                               checkPercentageEasy = 0.0;
                           }
                       }
                   });
           if(!Patterns.EMAIL_ADDRESS.matcher(userEmail).matches())
           {
               allmediumref = g_firestore.collection("Statistics").document("Medium").collection("Users").document(guestEmail);
           }
           if(Patterns.EMAIL_ADDRESS.matcher(userEmail).matches())
           {
               allmediumref = g_firestore.collection("Statistics").document("Medium").collection("Users").document(userEmail);
           }
           allmediumref.get()
                   .addOnFailureListener(new OnFailureListener() {
                       @Override
                       public void onFailure(@NonNull Exception e) {
                           checkSpeedPercentageMedium = 0.0;
                           checkSportsPercentageMedium = 0.0;
                           checkSciencePercentageMedium = 0.0;
                           checkPicturePercentageMedium = 0.0;
                           checkMusicPercentageMedium = 0.0;
                           checkHistoryPercentageMedium = 0.0;
                           checkGeoPercentageMedium = 0.0;
                           checkGKPercentageMedium = 0.0;
                           checkFilmTVPercentageMedium = 0.0;
                           checkPercentageMedium = 0.0;
                           Toast.makeText(account_page.this, "Login with an account to record Statistics", Toast.LENGTH_SHORT).show();
                       }
                   })
                   .addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
                       @Override
                       public void onSuccess(DocumentSnapshot snapshot) {
                           //If document with email address as id exist - Show average stored
                           if (snapshot.exists()) {
                               checkPercentageMedium = snapshot.getDouble("Correct Percentage");
                               checkFilmTVPercentageMedium = snapshot.getDouble("FilmTV Correct Answer Percentage");
                               checkGKPercentageMedium = snapshot.getDouble("GK Correct Answer Percentage");
                               checkGeoPercentageMedium = snapshot.getDouble("Geography Correct Answer Percentage");
                               checkHistoryPercentageMedium = snapshot.getDouble("History Correct Answer Percentage");
                               checkMusicPercentageMedium = snapshot.getDouble("Music Correct Answer Percentage");
                               checkPicturePercentageMedium = snapshot.getDouble("Picture Correct Answer Percentage");
                               checkSciencePercentageMedium = snapshot.getDouble("Science Correct Answer Percentage");
                               checkSportsPercentageMedium = snapshot.getDouble("Sports Correct Answer Percentage");
                               checkSpeedPercentageMedium = snapshot.getDouble("Speed Run Correct Answer Percentage");
                           }
                           //If any Topic is non existing in firestore, just set the value to 0.0
                           //Has to be given a formatted value, as the textview is formatted to this, and it will throw an exception if it is null
                           if (checkSpeedPercentageMedium == null) {
                               checkSpeedPercentageMedium = 0.0;
                           }
                           if (checkPercentageMedium == null) {
                               checkPercentageMedium = 0.0;
                           }
                           if (checkFilmTVPercentageMedium == null) {
                               checkFilmTVPercentageMedium = 0.0;
                           }
                           if (checkGKPercentageMedium == null) {
                               checkGKPercentageMedium = 0.0;
                           }
                           if (checkHistoryPercentageMedium == null) {
                               checkHistoryPercentageMedium = 0.0;
                           }
                           if (checkGeoPercentageMedium == null) {
                               checkGeoPercentageMedium = 0.0;
                           }
                           if (checkMusicPercentageMedium == null) {
                               checkMusicPercentageMedium = 0.0;
                           }
                           if (checkPicturePercentageMedium == null) {
                               checkPicturePercentageMedium = 0.0;
                           }
                           if (checkSciencePercentageMedium == null) {
                               checkSciencePercentageMedium = 0.0;
                           }
                           if (checkSportsPercentageMedium == null) {
                               checkSportsPercentageMedium = 0.0;
                           }
                           //If no doc exists, value is 0.0
                           if (!snapshot.exists()) {
                               checkSpeedPercentageMedium = 0.0;
                               checkSportsPercentageMedium = 0.0;
                               checkSciencePercentageMedium = 0.0;
                               checkPicturePercentageMedium = 0.0;
                               checkMusicPercentageMedium = 0.0;
                               checkHistoryPercentageMedium = 0.0;
                               checkGeoPercentageMedium = 0.0;
                               checkGKPercentageMedium = 0.0;
                               checkFilmTVPercentageMedium = 0.0;
                               checkPercentageMedium = 0.0;
                           }
                       }
                   });
           if(!Patterns.EMAIL_ADDRESS.matcher(userEmail).matches())
           {
               allhardref = g_firestore.collection("Statistics").document("Hard").collection("Users").document(guestEmail);
           }
           if(Patterns.EMAIL_ADDRESS.matcher(userEmail).matches())
           {
               allhardref = g_firestore.collection("Statistics").document("Hard").collection("Users").document(userEmail);
           }
           allhardref.get()
                   .addOnFailureListener(new OnFailureListener() {
                       @Override
                       public void onFailure(@NonNull Exception e) {
                           checkSpeedPercentageHard = 0.0;
                           checkSportsPercentageHard = 0.0;
                           checkSciencePercentageHard = 0.0;
                           checkPicturePercentageHard = 0.0;
                           checkMusicPercentageHard = 0.0;
                           checkHistoryPercentageHard = 0.0;
                           checkGeoPercentageHard = 0.0;
                           checkGKPercentageHard = 0.0;
                           checkFilmTVPercentageHard = 0.0;
                           checkPercentageHard = 0.0;
                           Toast.makeText(account_page.this, "Login with an account to record Statistics", Toast.LENGTH_SHORT).show();
                       }
                   })
                   .addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
                       @Override
                       public void onSuccess(DocumentSnapshot snapshot) {
                           //If document with email address as id exist - Show average stored
                           if (snapshot.exists()) {
                               FirebaseUser firebaseUser = FirebaseAuth.getInstance().getCurrentUser();
                               String userEmail = firebaseUser.getEmail();
                               checkPercentageHard = snapshot.getDouble("Correct Percentage");
                               checkFilmTVPercentageHard = snapshot.getDouble("FilmTV Correct Answer Percentage");
                               checkGKPercentageHard = snapshot.getDouble("GK Correct Answer Percentage");
                               checkGeoPercentageHard = snapshot.getDouble("Geography Correct Answer Percentage");
                               checkHistoryPercentageHard = snapshot.getDouble("History Correct Answer Percentage");
                               checkMusicPercentageHard = snapshot.getDouble("Music Correct Answer Percentage");
                               checkPicturePercentageHard = snapshot.getDouble("Picture Correct Answer Percentage");
                               checkSciencePercentageHard = snapshot.getDouble("Science Correct Answer Percentage");
                               checkSportsPercentageHard = snapshot.getDouble("Sports Correct Answer Percentage");
                               checkSpeedPercentageHard = snapshot.getDouble("Speed Run Correct Answer Percentage");

                               //If any Topic is non existing in firestore, just set the value to 0.0
                               //Has to be given a formatted value, as the textview is formatted to this, and it will throw an exception if it is null
                               if (checkSpeedPercentageHard == null) {
                                   checkSpeedPercentageHard = 0.0;
                               }
                               if (checkPercentageHard == null) {
                                   checkPercentageHard = 0.0;
                               }
                               if (checkFilmTVPercentageHard == null) {
                                   checkFilmTVPercentageHard = 0.0;
                               }
                               if (checkGKPercentageHard == null) {
                                   checkGKPercentageHard = 0.0;
                               }
                               if (checkHistoryPercentageHard == null) {
                                   checkHistoryPercentageHard = 0.0;
                               }
                               if (checkGeoPercentageHard == null) {
                                   checkGeoPercentageHard = 0.0;
                               }
                               if (checkMusicPercentageHard == null) {
                                   checkMusicPercentageHard = 0.0;
                               }
                               if (checkPicturePercentageHard == null) {
                                   checkPicturePercentageHard = 0.0;
                               }
                               if (checkSciencePercentageHard == null) {
                                   checkSciencePercentageHard = 0.0;
                               }
                               if (checkSportsPercentageHard == null) {
                                   checkSportsPercentageHard = 0.0;
                               }
                           }
                           //If no doc exists, value is 0.0
                           if (!snapshot.exists()) {
                               checkSpeedPercentageHard = 0.0;
                               checkSportsPercentageHard = 0.0;
                               checkSciencePercentageHard = 0.0;
                               checkPicturePercentageHard = 0.0;
                               checkMusicPercentageHard = 0.0;
                               checkHistoryPercentageHard = 0.0;
                               checkGeoPercentageHard = 0.0;
                               checkGKPercentageHard = 0.0;
                               checkFilmTVPercentageHard = 0.0;
                               checkPercentageHard = 0.0;
                           }
                       }
                   });
       }

        private void guestLoggedinMsg()
        {
           if(userEmail.equals(guestEmail))
           {
               Toast.makeText(account_page.this, "Login or create an Account to get Statistics", Toast.LENGTH_SHORT).show();
           }

        }
    }



