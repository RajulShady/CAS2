package com.rajul.cas;

import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.widget.AdapterView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.parse.FindCallback;
import com.parse.LogOutCallback;
import com.parse.ParseException;
import com.parse.ParseObject;
import com.parse.ParseQuery;
import com.parse.ParseUser;


import org.joda.time.DateTime;
import org.joda.time.LocalDate;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import uk.co.chrisjenx.calligraphy.CalligraphyContextWrapper;

public class AttendanceUpload extends AppCompatActivity {
    //Creating a list of Students
    private ArrayList<Students> students;
    //Creating Views
    private RecyclerView recyclerView;
    private RecyclerView.LayoutManager layoutManager;
    private RecyclerView.Adapter adapter;
    TextView subhint, sechint, lechint, datehint;
    String sem, bra, sec, lec, sub, aa;
    Packet p = new Packet();
    private LinearLayout headerProgress ;
    LinearLayout dim_layout;

    //DataHolder d=new DataHolder();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_attendance_upload);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        recyclerView = (RecyclerView) findViewById(R.id.recyclerview);
        recyclerView.setHasFixedSize(false);
        students = new ArrayList<>();
        Intent i = getIntent();
        headerProgress = (LinearLayout)findViewById(R.id.lHeaderProgress);
        dim_layout = (LinearLayout) findViewById(R.id.dim_layout);
        sem = i.getExtras().getString("semester");
        sec = i.getExtras().getString("section");
        bra = i.getExtras().getString("branch");
        sub = i.getExtras().getString("subject");
        lec = i.getExtras().getString("lecture");
        aa = i.getExtras().getString("date");
        subhint = (TextView) findViewById(R.id.subhint);
        sechint = (TextView) findViewById(R.id.secselecthint);
        lechint = (TextView) findViewById(R.id.lecturehint);
        datehint = (TextView) findViewById(R.id.date);
        subhint.setText(sub);
        sechint.setText(sec);
        lechint.setText(lec);
        datehint.setText(aa);
        p.setSem(sem);
        Log.i("year",p.getYear()+sem);
        //Method to get Data
        getData();
        recyclerView.setLayoutManager(new LinearLayoutManager(getApplicationContext()));
        Log.i("as", "see");



    }
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_home_teacher, menu);
        return true;
    }
    public void jumptoSuccessfulUplodDialog(View v){
        final Dialog dialog = new Dialog(this); // Context, this, etc.
        dialog.setContentView(R.layout.successfuluploaddialog);
        dialog.setTitle(R.string.dialog_Successful_upload);
        dialog.show();
    }
    public void jumptoHomeTeacher(View v){
        Intent intent = new Intent(getApplicationContext(),HomeTeacher.class);
        startActivity(intent);
        overridePendingTransition(R.anim.slide_in_right,R.anim.slide_out_right);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        if(id== R.id.action_about){
            Intent intent = new Intent(getApplicationContext(), About.class);
            startActivity(intent);
            overridePendingTransition(R.anim.slide_in_down,R.anim.slide_out_down);
        }

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_home) {
            Intent intent = new Intent(getApplicationContext(), HomeTeacher.class);
            startActivity(intent);
            overridePendingTransition(R.anim.slide_in_down,R.anim.slide_out_down);
        }
        if (id == R.id.action_profile) {
            Intent intent = new Intent(getApplicationContext(), TeacherProfile.class);
            startActivity(intent);
            overridePendingTransition(R.anim.slide_in_down,R.anim.slide_out_down);
        }
        if (id == R.id.action_logout) {ParseUser.logOutInBackground(new LogOutCallback() {
            @Override
            public void done(ParseException e) {
                if(e==null) {
                    Intent intent = new Intent(getApplicationContext(), AskLogin.class);
                    startActivity(intent);
                }
            }
        });

            overridePendingTransition(R.anim.slide_in_down,R.anim.slide_out_down);
        }


        return super.onOptionsItemSelected(item);
    }

    public void getData() {
        headerProgress.setVisibility(View.VISIBLE);
        dim_layout.setVisibility(View.VISIBLE);

        ParseQuery<ParseObject> query = new ParseQuery<ParseObject>("students");
        query.whereContains("branch", bra);
        query.whereContains("section", sec);
        query.whereContains("sem", sem);
        //11Log.i("Semm",sem);
        p.setBranch(bra);
        p.setLecture(lec);
        p.setIdt(ParseUser.getCurrentUser().getUsername());
        p.setSec(sec);
        p.setSem(sem);
        p.setSubject(sub);

        LocalDate a = new LocalDate(aa);
        Log.i("date", a.toString());
        p.setDate(a);
        //d.setData(p);
        query.addAscendingOrder("student_id");
        query.findInBackground(new FindCallback<ParseObject>() {
            @Override
            public void done(List<ParseObject> objects, ParseException e) {

                if (e == null) {
                    if (objects.size() > 0) {
                        Log.i("aa", "asaaa");
                        parseData(objects);
                    } else {
                        Log.i("as", "haja");
                    }
                } else {
                    e.printStackTrace();
                    Log.i("asasas", "qwewq");
                }
            }
        });

    }

    private void parseData(List<ParseObject> obj) {
        for (ParseObject stu : obj) {
            Log.i("out", stu.getString("student_id"));
            Students studInfo = new Students();
            try {
                studInfo.setRollno(stu.getString("student_id"));
            } catch (Exception e) {
                Log.i("not", "raeas");
                e.printStackTrace();
            }
            students.add(studInfo);
        }
        adapter = new AttendanceUploadAdapter(students, getApplicationContext());
        adapter.notifyDataSetChanged();
        recyclerView.setAdapter(adapter);
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        headerProgress.setVisibility(View.INVISIBLE);
        dim_layout.setVisibility(View.INVISIBLE);
    }

    @Override
    protected void attachBaseContext(Context newBase) {
        super.attachBaseContext(CalligraphyContextWrapper.wrap(newBase));
    }
}