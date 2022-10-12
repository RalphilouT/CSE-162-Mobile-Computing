package com.ralphiloutatoy.lab5;

import java.util.Locale;
import java.util.List;
import java.util.Date;

import androidx.core.content.ContextCompat;
import androidx.core.app.ActivityCompat;
import androidx.appcompat.app.AppCompatActivity;
import androidx.annotation.RequiresApi;
import androidx.annotation.NonNull;

import android.nfc.Tag;
import android.widget.Toast;
import android.widget.CalendarView;
import android.widget.Button;
import android.view.View;
import android.util.Log;
import android.speech.tts.TextToSpeech;
import android.speech.RecognizerIntent;

import android.os.Bundle;
import android.os.Build;
import android.net.Uri;
import android.content.pm.PackageManager;
import android.content.Intent;
import android.Manifest;

import org.w3c.dom.Text;

public class MainActivity extends AppCompatActivity{
    private CalendarView calendarView;
    private Button speechRecognitionButton;
    private static final int REQUEST_PHONE_CALL = 1;
    TextToSpeech tts;

    @Override
    protected void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        if(ContextCompat.checkSelfPermission(MainActivity.this,Manifest.permission.CALL_PHONE)!=PackageManager.PERMISSION_GRANTED){
            ActivityCompat.requestPermissions(MainActivity.this,new String[]{Manifest.permission.CALL_PHONE},REQUEST_PHONE_CALL);
        }
        calendarView = findViewById(R.id.calendarView);
        speechRecognitionButton = findViewById(R.id.button);

        tts = new TextToSpeech(this, new TextToSpeech.OnInitListener() {
            @Override
            public void onInit(int i) {
                if(i == TextToSpeech.SUCCESS){
                    int result = tts.setLanguage((Locale.US));
                    if(result != TextToSpeech.LANG_AVAILABLE && result != TextToSpeech.LANG_COUNTRY_AVAILABLE){
                        Toast.makeText(MainActivity.this, "Non-supporter language!", Toast.LENGTH_SHORT).show();
                    }
                }
            }
        });
        if (calendarView != null){
            calendarView.setOnDateChangeListener(new CalendarView.OnDateChangeListener(){
                @Override
                public void onSelectedDayChange(@NonNull CalendarView view, int year, int month, int dayOfMonth){
                    //Note that months are indexed from 0
                    String monthName = (String)android.text.format.DateFormat.format("MMMM", new Date());
                    String msg = "Selected date is " + monthName + " " + dayOfMonth + ". " + year;
                    Toast.makeText(MainActivity.this,msg,Toast.LENGTH_SHORT).show();
                }

            });
        }
        displaySpeechRecognizer();
    }
    @Override
    protected void onDestroy(){
        super.onDestroy();
        if(tts != null){
            tts.shutdown();
        }
    }
    private static final int SPEECH_REQUEST_CODE = 0;

    //Create an intent that can start the Speech Recognizer activity
    private void displaySpeechRecognizer(){
        Intent intent = new Intent(RecognizerIntent.ACTION_RECOGNIZE_SPEECH);
        intent.putExtra(RecognizerIntent.EXTRA_LANGUAGE_MODEL,RecognizerIntent.LANGUAGE_MODEL_FREE_FORM);
        //This starts the activity and populates the intent with the speech text
        startActivityForResult(intent,SPEECH_REQUEST_CODE);

    }
    public void onButtonClick (View view){displaySpeechRecognizer();}
    //This callback is invoked when the Speech Recognizer returns
    //This is where you provess the intent and extract text from the intent
    @RequiresApi(api = Build.VERSION_CODES.O)
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data){
        if (requestCode == SPEECH_REQUEST_CODE && resultCode == RESULT_OK){
            List<String> results = data.getStringArrayListExtra(RecognizerIntent.EXTRA_RESULTS);
            String spokenText = results.get(0);

            //Do something with spokenText
            if(spokenText.equals("what day is today")){
                long unixTime = System.currentTimeMillis();
                calendarView.setDate(unixTime);
                String monthName = (String)android.text.format.DateFormat.format("MMMM", new Date());
                String day = (String)android.text.format.DateFormat.format("d",new Date());
                String year = (String)android.text.format.DateFormat.format("yyyy", new Date());
                String text = "today is " + monthName + " " + day + " " + year;
                tts.speak(text, TextToSpeech.QUEUE_ADD, null, "speech");
                Log.i("TextToSpeech", "tts");
            }
            if(spokenText.equals("what day is tomorrow")){
                long unixTime = System.currentTimeMillis();
                calendarView.setDate(unixTime+86400000);
                String monthName = (String)android.text.format.DateFormat.format("MMMM", new Date());
                String day = (String)android.text.format.DateFormat.format("d",new Date());
                String year = (String)android.text.format.DateFormat.format("yyyy", new Date());
                String text = "tomorrow is " + monthName + " " + day + " " + year;
                tts.speak(text, TextToSpeech.QUEUE_ADD, null, "speech");

            }
            if(results.get(0).equals("call emergency")){
                StringBuilder sb = new StringBuilder();
                for ( int i = 1; i<results.size();i++){
                    sb.append(results.get(i));
                }
                Intent callIntent = new Intent(Intent.ACTION_CALL, Uri.parse("tel:"+R.string.emergency_number));
                startActivity(callIntent);
                Toast.makeText(MainActivity.this, sb.toString(), Toast.LENGTH_SHORT).show();
            }
            if(results.get(0).equals("navigate home")){

//                Intent intent = new Intent(Intent.ACTION_VIEW);
//                intent.setData(Uri.parse("geo:37.7749,-122.4194"));
//                Intent chooser = Intent.createChooser(intent,"Launch Maps");
//                startActivity(chooser);
//                String url = "https://www.google.com/maps?saddr=My+Location&q=Taronga+Zoo";
//                Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse(url));
//                startActivity(intent);
                Uri gmmIntentUri = Uri.parse("google.navigation:q=San+Francisco,+CA");
                Intent mapIntent = new Intent(Intent.ACTION_VIEW, gmmIntentUri);
                mapIntent.setPackage("com.google.android.apps.maps");
                startActivity(mapIntent);




            }
        }
        super.onActivityResult(requestCode,resultCode, data);
    }

}