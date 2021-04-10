package com.example.hr_application;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.cardview.widget.CardView;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.recyclerview.widget.AdapterListUpdateCallback;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.text.Html;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.airbnb.lottie.LottieAnimationView;
import com.bumptech.glide.Glide;
import com.example.hr_application.adapters.TaskAdapter;
import com.example.hr_application.adapters.attendanceAdapter;
import com.example.hr_application.models.TaskModel;
import com.example.hr_application.models.attendanceModel;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.material.navigation.NavigationView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import org.naishadhparmar.zcustomcalendar.CustomCalendar;
import org.naishadhparmar.zcustomcalendar.OnDateSelectedListener;
import org.naishadhparmar.zcustomcalendar.OnNavigationButtonClickedListener;
import org.naishadhparmar.zcustomcalendar.Property;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

import static java.lang.Thread.sleep;

public class MainActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {

    private static final String TAG = "MainActivity";
    private final String textMeetNo = "no time mention";
    private TextView headerName, headerEmail, todayDate, nameUser, navHeaderName, navHeaderEmail;
    private ImageView profile, navProfile;
    private DrawerLayout mDrawer;
    private boolean not_found=true;
    private attendanceAdapter attendanceAdapter;
    private ActionBarDrawerToggle toggle;
    private NavigationView navigationView;
    private View headerView;
    private FirebaseUser user;
    private TextView checkin, checkout, meetview;
    private ImageView checkinIndicator, checkoutIndicator;
    private Button checkoutbtn, checkinbtn;
    private CustomCalendar customCalendar;
    private int month, year;
    CustomLoadingClass cl;
    private String status;
    private String stats;
    LottieAnimationView done;
    private LottieAnimationView loading, loadingC, empty;
    private ArrayList<String> date = new ArrayList<>();

    RecyclerView taskRecyclerView;
    TaskAdapter taskAdapter;
    Calendar calendar = Calendar.getInstance();
    ArrayList<String> time = new ArrayList<>();
    ArrayList<TaskModel> taskList = new ArrayList<>();
    boolean doubleBackToExitPressedOnce = false;
    public Toolbar toolbar;
    private CardView cardView;
    private HashMap<Integer, Object> dateHashMap = null;

    @SuppressLint("SetTextI18n")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        calendar = Calendar.getInstance();
        checkin = findViewById(R.id.checkin);
        checkout = findViewById(R.id.checkout);
        cardView=(CardView)findViewById(R.id.cardView2);
        checkinIndicator = findViewById(R.id.checkinIndicator);
        checkoutIndicator = findViewById(R.id.checkoutIndicator);
        checkinbtn = findViewById(R.id.inbtn);
        checkoutbtn = findViewById(R.id.outBtn);
        done = (LottieAnimationView) findViewById(R.id.doneAnimation_);
        loading = (LottieAnimationView) findViewById(R.id.loadingAnimation);
        loadingC = (LottieAnimationView) findViewById(R.id.loadingAnimationC);
        empty = (LottieAnimationView) findViewById(R.id.empty);
        cl=new CustomLoadingClass(MainActivity.this);
        cl.setCanceledOnTouchOutside(false);
        cl.show();
        if (!isOnline()) {
            Toast.makeText(this, "Not Connection to Network", Toast.LENGTH_SHORT).show();
            final AlertDialog.Builder alert = new AlertDialog.Builder(MainActivity.this);
            View view = getLayoutInflater().inflate(R.layout.internet_dialog, null);
            Button retry = view.findViewById(R.id.retryBtn);
            TextView heading = view.findViewById(R.id.dialog_heading);
            heading.setText("Not Connection to Network");
            alert.setView(view);
            final AlertDialog alertDialog = alert.create();
            retry.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    startActivity(new Intent(MainActivity.this, MainActivity.class));
                }
            });
            alertDialog.show();
        }
        customCalendar = findViewById(R.id.custom_calender);
        taskRecyclerView = findViewById(R.id.taskRecyclerView);
        toolbar = (Toolbar) findViewById(R.id.main_tool_bar_);
        mDrawer = findViewById(R.id.drawerLayout);
        navigationView = findViewById(R.id.side_navView);

        headerView = navigationView.getHeaderView(0);
        navHeaderName = headerView.findViewById(R.id.headerName);
        navHeaderEmail = headerView.findViewById(R.id.headerEmail);
        navProfile = headerView.findViewById(R.id.headerImage);

        todayDate = findViewById(R.id.todayDate);
        nameUser = findViewById(R.id.userName);
        profile = findViewById(R.id.profileImageHome);
        supportToolbar();
        taskRecyclerView.setHasFixedSize(true);
        taskRecyclerView.setLayoutManager(new LinearLayoutManager(getApplicationContext()));
        readTask();
        readDate();
        meetview = findViewById(R.id.meeting_time);
        DatabaseReference time = FirebaseDatabase.getInstance().getReference("meetingLink").child("time");
        time.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if(snapshot.exists()) {
                    String lin = snapshot.getValue().toString().trim();
                    if (lin != null) {
                        meetview.setText(lin.replace("{", "").replace("}", "").replace("time", "")
                                .replace("=", ""));
                    }
                }else {
                    meetview.setText(textMeetNo);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
        HideMenu();
        if (!not_found){
            startActivity(new Intent(getApplicationContext(),no_data.class));
            Toast.makeText(MainActivity.this, "Contact HR", Toast.LENGTH_SHORT).show();
        }
        Calendar todayDateView = Calendar.getInstance();
        String[] a = todayDateView.getTime().toString().split(" ");
        todayDate.setText(a[0] + "\n" + a[1] + " " + a[2] + " " + a[5]);
        checkoutbtn.setOnClickListener(v -> {
            DatabaseReference reference = FirebaseDatabase.getInstance().getReference("users").child(FirebaseAuth.getInstance().getCurrentUser().getUid()).child("attendance").push();
            HashMap<String, String> hashMap = new HashMap<>();
            Calendar calendar = Calendar.getInstance();
            String currentDate = String.valueOf(calendar.get(Calendar.DATE));
            String currentMonth = String.valueOf(calendar.get(Calendar.MONTH) + 1);
            String currentYear = String.valueOf(calendar.get(Calendar.YEAR));
            hashMap.put("status", "out");
            hashMap.put("time", System.currentTimeMillis() + "");
            hashMap.put("date", currentDate + "/" + currentMonth + "/" + currentYear);
            reference.setValue(hashMap);
            Toast.makeText(this, "Adios Amigo, see you soon", Toast.LENGTH_SHORT).show();
        });
        checkinbtn.setOnClickListener(v -> {
            DatabaseReference reference = FirebaseDatabase.getInstance().getReference("users").child(FirebaseAuth.getInstance().getCurrentUser().getUid()).child("attendance").push();
            HashMap<String, String> hashMap = new HashMap<>();
            Calendar calendar = Calendar.getInstance();
            String currentDate = String.valueOf(calendar.get(Calendar.DATE));
            String currentMonth = String.valueOf(calendar.get(Calendar.MONTH) + 1);
            String currentYear = String.valueOf(calendar.get(Calendar.YEAR));
            hashMap.put("status", "in");
            hashMap.put("time", System.currentTimeMillis() + "");
            hashMap.put("date", currentDate + "/" + currentMonth + "/" + currentYear);
            reference.setValue(hashMap);
            Toast.makeText(this, "Welcome Amigo, good to have you", Toast.LENGTH_SHORT).show();
        });
        DatabaseReference reference = FirebaseDatabase.getInstance().getReference("users").child(FirebaseAuth.getInstance().getCurrentUser().getUid()).child("attendance");
        Query query = (reference.orderByChild("time"));
        query.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                for (DataSnapshot dataSnapshot : snapshot.getChildren()) {
                    attendanceModel attendanceModel = dataSnapshot.getValue(attendanceModel.class);
                    if (attendanceModel.getStatus().equals("in")) {
                        checkinIndicator.setVisibility(View.VISIBLE);
                        checkoutIndicator.setVisibility(View.INVISIBLE);
                        checkoutbtn.setVisibility(View.VISIBLE);
                        checkout.setVisibility(View.INVISIBLE);
                        checkinbtn.setVisibility(View.INVISIBLE);
                        checkin.setVisibility(View.VISIBLE);
                    } else {
                        checkoutIndicator.setVisibility(View.VISIBLE);
                        checkinIndicator.setVisibility(View.INVISIBLE);
                        checkinbtn.setVisibility(View.VISIBLE);
                        checkin.setVisibility(View.INVISIBLE);
                        checkoutbtn.setVisibility(View.INVISIBLE);
                        checkout.setVisibility(View.VISIBLE);
                    }
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
        View view = LayoutInflater.from(this).inflate(R.layout.nav_header, null, false);
        headerName = view.findViewById(R.id.headerName);
        headerEmail = view.findViewById(R.id.headerEmail);
        headerName.setText("");

        user = FirebaseAuth.getInstance().getCurrentUser();
        FirebaseDatabase.getInstance().getReference().child("users").child(user.getUid())
                .addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        if (snapshot.exists()) {
                            String[] name = (snapshot.child("username").getValue()).toString().trim().split(" ");
                            nameUser.setText("Hi " + name[0] + "!");
                            if (name.length > 1) {
                                navHeaderName.setText(name[0] + " " + name[1]);
                            } else navHeaderName.setText(name[0] + "");
                            String email = (snapshot.child("email").getValue()).toString().trim();
                            navHeaderEmail.setText(email);
                            if(snapshot.child("ImageUrl").exists()) {
                                String url = snapshot.child("ImageUrl").getValue().toString();
                                if (!url.equals("No Profile Image")){
                                    loadImage(url);
                                }else {
                                    loadImage("https://firebasestorage.googleapis.com/v0/b/hr-application-10b16.appspot.com/o/logo_e_city.jpg?alt=media&token=f1f952a8-cd67-4346-935b-ef754ae57928");
                                }
                            }else{
                                loadImage("https://firebasestorage.googleapis.com/v0/b/hr-application-10b16.appspot.com/o/logo_e_city.jpg?alt=media&token=f1f952a8-cd67-4346-935b-ef754ae57928");
                            }
                        }
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {

                    }
                });
    }

    private void loadImage(String url) {
        try {
            Glide.with(this).load(url).placeholder(R.drawable.logo_circle).into(profile);
            Glide.with(this).load(url).placeholder(R.drawable.logo_circle).into(navProfile);
            cl.dismiss();

        } catch (Exception e) {
            e.getStackTrace();
        }
    }

    private void supportToolbar() {
        setSupportActionBar(toolbar);
        toggle = new ActionBarDrawerToggle(this, mDrawer, R.string.open, R.string.close);
        navigationView.setNavigationItemSelectedListener(this);
        mDrawer.addDrawerListener(toggle);
        toggle.syncState();
        getSupportActionBar().setDisplayShowTitleEnabled(false);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setHomeButtonEnabled(true);
        navigationView.setCheckedItem(R.id.dashboard);
    }

    public void meet(View view) {
        DatabaseReference reference = FirebaseDatabase.getInstance().getReference("meetingLink").child("googleMeet").child("GoogleMeetLink");
        reference.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                String link = snapshot.getValue().toString().trim();

                    Uri uri = Uri.parse(link);
                    Intent intent = new Intent(Intent.ACTION_VIEW, uri);
                    startActivity(intent);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Toast.makeText(MainActivity.this, "No meetings", Toast.LENGTH_SHORT).show();
            }
        });

        // missing 'http://' will cause crashed
    }

    private void calendar(HashMap<Integer, Object> dateHashMap) {
        HashMap<Object, Property> descHashmap = new HashMap<>();
        //default
        Property defaultProperty = new Property();
        defaultProperty.layoutResource = R.layout.defaultview;
        defaultProperty.dateTextViewResource = R.id.text_view;
        descHashmap.put("default", defaultProperty);
        //current
        Property currentProperty = new Property();
        currentProperty.layoutResource = R.layout.currentview;
        currentProperty.dateTextViewResource = R.id.text_view;
        descHashmap.put("c", currentProperty);
        //present
        Property presentProperty = new Property();
        presentProperty.layoutResource = R.layout.presentview;
        presentProperty.dateTextViewResource = R.id.text_view;
        descHashmap.put("p", presentProperty);
        //absent
        Property absentProperty = new Property();
        absentProperty.layoutResource = R.layout.absentview;
        absentProperty.dateTextViewResource = R.id.text_view;
        descHashmap.put("a", absentProperty);
        customCalendar.setMapDescToProp(descHashmap);
        //initialize date hashmap
        customCalendar.setClickable(false);
        customCalendar.setNavigationButtonEnabled(CustomCalendar.PREVIOUS, false);
        customCalendar.setNavigationButtonEnabled(CustomCalendar.NEXT, false);//don't touch it
        dateHashMap.put(calendar.get(Calendar.DAY_OF_MONTH), "c");
        for (int i = 1; i < dateHashMap.size(); i++) {
            if (dateHashMap.containsKey(i)) {
                dateHashMap.put(i, "p");
            } else {
                dateHashMap.put(i, "a");
            }
        }
        if (calendar != null)
            customCalendar.setDate(calendar, dateHashMap);
        customCalendar.invalidate();
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        int id = item.getItemId();
        if (toggle.onOptionsItemSelected(item)) {
            return true;
        }
        if (id == R.id.news) {
            String uid = FirebaseAuth.getInstance().getCurrentUser().getUid();
            Intent intent = new Intent(this, NewsFeed.class);
            intent.putExtra("uid", uid);
            intent.putExtra("status",status);
            startActivity(intent);
        }
        return super.onOptionsItemSelected(item);
    }

    public void HideMenu() {
        navigationView = (NavigationView) findViewById(R.id.side_navView);
        Menu myMenu = navigationView.getMenu();
        MenuItem LeaveApplication = myMenu.findItem(R.id.grantLeave);
        MenuItem Employees = myMenu.findItem(R.id.employees);
        MenuItem CreateTeam = myMenu.findItem(R.id.createTeam);
        MenuItem UpdateMeet = myMenu.findItem(R.id.meet_update);
        MenuItem UploadTask = myMenu.findItem(R.id.uploadTask);
        MenuItem AddUser = myMenu.findItem(R.id.add_user);
        LeaveApplication.setVisible(false);
        Employees.setVisible(false);
        CreateTeam.setVisible(false);
        UpdateMeet.setVisible(false);
        UploadTask.setVisible(false);
        AddUser.setVisible(false);
        DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference("users").child(FirebaseAuth.getInstance().getCurrentUser().getUid());
        databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if(snapshot.exists()) {
                    stats=snapshot.child("Developer").getValue().toString();
                    if (snapshot.child("Developer").getValue().toString().toUpperCase().equals("HR")) {
                        LeaveApplication.setVisible(true);
                        Employees.setVisible(true);
                        CreateTeam.setVisible(true);
                        UpdateMeet.setVisible(true);
                        UploadTask.setVisible(true);
                        status = "HR";
                    }
                    if ((snapshot.child("Developer").getValue().toString().toLowerCase().equals("admin"))) {
                        LeaveApplication.setVisible(true);
                        Employees.setVisible(true);
                        CreateTeam.setVisible(true);
                        UpdateMeet.setVisible(true);
                        UploadTask.setVisible(true);
                        status = "Admin";
                    }
                    if (((snapshot.child("Developer").getValue().toString().toLowerCase().equals("super admin")))) {
                        LeaveApplication.setVisible(true);
                        Employees.setVisible(true);
                        CreateTeam.setVisible(true);
                        UpdateMeet.setVisible(true);
                        UploadTask.setVisible(true);
                        AddUser.setVisible(true);
                        status = "Super Admin";
                    }
                }
                if (!snapshot.exists()){
                    not_found=false;
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

                error.getDetails();
            }
        });
    }

    @SuppressLint("NonConstantResourceId")
    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()) {
            case R.id.myAccount:
                Intent i = new Intent(this, MyAccountActivity.class);
                i.putExtra("uid", FirebaseAuth.getInstance().getCurrentUser().getUid());
                i.putExtra("editable", "no");
                startActivity(i);
                break;
            case R.id.dashboard:
                if (mDrawer.isDrawerOpen(GravityCompat.START))
                    mDrawer.closeDrawers();
                break;
            case R.id.attendance:
                startActivity(new Intent(this, AttendanceActivity.class));
                break;
            case R.id.employees:
                Intent intent1 = new Intent(this, EmployeeActivity.class);
                intent1.putExtra("status", status);
                startActivity(intent1);
                break;
            case R.id.uploadTask:
                startActivity(new Intent(this, TaskUploadActivity.class));
                break;
            case R.id.teamDesk:
                Intent intent2 = new Intent(this, TeamDeskListActivity.class);
                intent2.putExtra("status", status);
                startActivity(intent2);
                break;
            case R.id.applyForLeave:
                startActivity(new Intent(this, ApplyLeaveActivity.class));
                break;
            case R.id.grantLeave:
                startActivity(new Intent(this, LeaveApplicationActivity.class));
                break;
            case R.id.refer:
                try {
                    Intent intent = new Intent(Intent.ACTION_SEND);
                    intent.setType("text/plain");
                    intent.putExtra(Intent.EXTRA_SUBJECT, "ECV Job Application");
                    String shareMessage = "Share your RESUME with us to get a role in E-City Vibes : http://ecityvibes.com/\n";
                    intent.putExtra(Intent.EXTRA_TEXT, shareMessage);
                    startActivity(Intent.createChooser(intent, "share by"));
                    break;
                } catch (Exception e) {
                    e.getStackTrace();
                }
            case R.id.createTeam:
                startActivity(new Intent(this, TeamNameActivity.class));
                break;
            case R.id.meet_update:
                CustomDialogClass cdd = new CustomDialogClass(MainActivity.this);
                cdd.show();
                break;
            case R.id.logout:
                FirebaseAuth.getInstance().signOut();
                startActivity(new Intent(MainActivity.this, loginActivity.class));
                finishAffinity();
                break;
            case R.id.add_user:
                startActivity(new Intent(this,DashboardActivity.class));
                break;
        }
        mDrawer.closeDrawer(GravityCompat.START);
        return true;
    }

    private void readTask() {
        loading.setSpeed(1);
        loading.playAnimation();
        final DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference("users").child(FirebaseAuth.getInstance().getCurrentUser().getUid()).child("Tasks");
        databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                taskList.clear();
                // start
                for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                    TaskModel taskModel = snapshot.getValue(TaskModel.class);
                    if (taskModel.getStatus().equals("Assigned")) {
                        taskList.add(taskModel);
                    }
                }
                loading.setVisibility(View.INVISIBLE);
                // end
                taskAdapter = new TaskAdapter(getApplicationContext(), taskList);
                taskRecyclerView.setLayoutManager(new LinearLayoutManager(MainActivity.this));
                taskRecyclerView.setAdapter(taskAdapter);
                try {
                    EmptyListAnimation();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                Toast.makeText(MainActivity.this, "check your network connection", Toast.LENGTH_SHORT).show();

            }
        });
    }

    @Override
    public void onBackPressed() {
        if (mDrawer.isDrawerOpen(GravityCompat.START)) {
            mDrawer.closeDrawer(GravityCompat.START);
        }
        if (doubleBackToExitPressedOnce) {
            finishAffinity();
            return;
        }

        this.doubleBackToExitPressedOnce = true;
        Toast.makeText(this, "Please click BACK again to exit", Toast.LENGTH_SHORT).show();
        new Handler().postDelayed(() -> doubleBackToExitPressedOnce = false, 2000);
    }

    //read present
    private void readDate() {
        loadingC.setSpeed(1);
        loadingC.playAnimation();
        DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference("users").child(FirebaseAuth.getInstance().getCurrentUser().getUid()).child("attendance");
        Query query = (databaseReference.orderByChild("time"));
        query.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                date.clear();
                dateHashMap = new HashMap<>();
                dateHashMap.clear();
                for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                    attendanceModel model = snapshot.getValue(attendanceModel.class);
                    String[] month = model.getDate().split("/");//don't touch it
                    if (Integer.parseInt(month[1]) == calendar.get(Calendar.MONTH) + 1)
                        date.add(model.getDate());
                    if (date != null) {
                        for (int i = 1; i < date.size(); i++) {

                            String[] m = date.get(i).split("/");
                            Log.d(TAG, "onDataChange: " + m[0]);
                            dateHashMap.put(Integer.parseInt(m[0]), "p");
                        }
                    }
                }
                loadingC.setVisibility(View.INVISIBLE);
                calendar(dateHashMap);
                customCalendar.invalidate();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                Toast.makeText(MainActivity.this, "check your network connection", Toast.LENGTH_SHORT).show();

            }
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu, menu);
        return true;
    }

    @Override
    protected void onStart() {
        super.onStart();
    }

    private void EmptyListAnimation() throws InterruptedException {
//        sleep(1100);
        if (taskList.isEmpty()) {
            empty.setVisibility(View.VISIBLE);
            empty.setSpeed(1);
            empty.playAnimation();
        } else {
            empty.setVisibility(View.GONE);
        }
    }
    @Override
    protected void onResume() {
        super.onResume();
    }

    public boolean isOnline() {
        ConnectivityManager conMgr = (ConnectivityManager) getApplicationContext().getSystemService(MainActivity.this.CONNECTIVITY_SERVICE);
        NetworkInfo netInfo = conMgr.getActiveNetworkInfo();

        if (netInfo == null || !netInfo.isConnected() || !netInfo.isAvailable()) {
            Toast.makeText(this, "No Internet connection!", Toast.LENGTH_LONG).show();
            return false;
        }
        return true;
    }
}