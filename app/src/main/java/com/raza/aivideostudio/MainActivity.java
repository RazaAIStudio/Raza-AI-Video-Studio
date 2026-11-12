package com.raza.aivideostudio;

import android.app.*;
import android.os.*;
import android.graphics.Color;
import android.view.*;
import android.widget.*;
import android.content.*;
import android.net.Uri;
import android.speech.tts.TextToSpeech;
import java.util.*;

public class MainActivity extends Activity {

    LinearLayout layout;
    EditText prompt;
    String ratio = "9:16";
    String qualityValue = "1080p";
    Uri selectedImage;
    TextToSpeech tts;

    int bg = Color.rgb(15, 18, 30);
    int accent = Color.rgb(0, 220, 180);

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        ScrollView scroll = new ScrollView(this);

        layout = new LinearLayout(this);
        layout.setOrientation(LinearLayout.VERTICAL);
        layout.setPadding(25, 30, 25, 30);
        layout.setBackgroundColor(bg);

        scroll.addView(layout);
        setContentView(scroll);

        tts = new TextToSpeech(this, status -> {});

        title("RAZA AI VIDEO STUDIO");
        heading("Create AI Videos");

        heading("VIDEO RATIO");

        LinearLayout ratios = new LinearLayout(this);

        String[] ratioOptions = {"9:16", "16:9", "1:1"};

        for (String r : ratioOptions) {
            Button b = new Button(this);
            b.setText(r);
            ratios.addView(b);

            b.setOnClickListener(v -> {
                ratio = r;
                message("Selected Ratio: " + ratio);
            });
        }

        layout.addView(ratios);

        heading("AI VIDEO PROMPT");

        prompt = edit("Write your video idea here...");

        action("GENERATE AI VIDEO", () ->
            message("AI video generation service connection required.")
        );

        heading("IMAGE TO VIDEO");

        action("SELECT IMAGE", () -> openImagePicker());

        action("GENERATE FROM IMAGE", () -> {
            if (selectedImage == null) {
                message("Please select an image first");
            } else {
                message("AI image-to-video service connection required.");
            }
        });

        heading("VIDEO QUALITY");

        Spinner quality = new Spinner(this);

        String[] qualities = {
            "720p",
            "1080p",
            "4K"
        };

        ArrayAdapter<String> adapter =
            new ArrayAdapter<>(
                this,
                android.R.layout.simple_spinner_dropdown_item,
                qualities
            );

        quality.setAdapter(adapter);

        quality.setOnItemSelectedListener(
            new android.widget.AdapterView.OnItemSelectedListener() {

                @Override
                public void onItemSelected(
                    android.widget.AdapterView<?> parent,
                    View view,
                    int position,
                    long id
                ) {
                    qualityValue = qualities[position];
                }

                @Override
                public void onNothingSelected(
                    android.widget.AdapterView<?> parent
                ) {}
            }
        );

        layout.addView(quality);

        
heading("AI VOICE GENERATOR");

EditText voiceText = edit("Enter text for AI voice...");

heading("SELECT VOICE LANGUAGE");

Spinner languageSpinner = new Spinner(this);

String[] languages = {
    "Hindi",
    "English"
};

ArrayAdapter<String> languageAdapter =
    new ArrayAdapter<>(
        this,
        android.R.layout.simple_spinner_dropdown_item,
        languages
    );

languageSpinner.setAdapter(languageAdapter);

layout.addView(languageSpinner);

heading("VOICE SPEED");

SeekBar speedBar = new SeekBar(this);
speedBar.setMax(15);
speedBar.setProgress(5);

layout.addView(speedBar);

action("PLAY VOICE", () -> {

    String text = voiceText.getText().toString().trim();

    if (text.isEmpty()) {
        message("Enter text first");
        return;
    }

    if (tts == null) {
        message("Voice engine loading");
        return;
    }

    String language =
        languageSpinner.getSelectedItem().toString();

    Locale locale;

    if (language.equals("Hindi")) {
        locale = new Locale("hi", "IN");
    } else {
        locale = Locale.US;
    }

    int result = tts.setLanguage(locale);

    if (result == TextToSpeech.LANG_MISSING_DATA ||
        result == TextToSpeech.LANG_NOT_SUPPORTED) {

        message("Selected language not supported");
        return;
    }

    float speed = 0.5f + (speedBar.getProgress() * 0.1f);

    tts.setSpeechRate(speed);

    tts.speak(
        text,
        TextToSpeech.QUEUE_FLUSH,
        null,
        "raza_voice"
    );
});

action("STOP VOICE", () -> {
    if (tts != null) {
        tts.stop();
    }
});
        heading("AI SCRIPT GENERATOR");

        EditText scriptTopic = edit("Enter your video topic...");

        
action("GENERATE SCRIPT", () -> {

    String topic = scriptTopic.getText().toString().trim();

    if (topic.isEmpty()) {
        message("Please enter video topic");
        return;
    }

    String script =
        "TITLE: " + topic + "\n\n" +

        "HOOK:\n" +
        "Kya aap jaante hain " + topic +
        " ke baare mein ek interesting baat?\n\n" +

        "INTRO:\n" +
        "Aaj hum baat karne wale hain " +
        topic + " ke baare mein.\n\n" +

        "MAIN CONTENT:\n" +
        topic + " ek interesting topic hai. " +
        "Iske baare mein jaanne ke liye video ko " +
        "dhyan se dekhiye.\n\n" +

        "ENDING:\n" +
        "Agar aapko ye video pasand aayi ho " +
        "to Like, Share aur Follow zaroor karein.\n\n" +

        "SUBSCRIBE:\n" +
        "Thanks for watching!";

    prompt.setText(script);

    message("Script Generated Successfully!");
});
        heading("BACKGROUND MUSIC");

        action("Music Feature - Coming Soon", () ->
            message("Background music feature coming soon")
        );

        heading("DOWNLOAD VIDEO");

        action("Download - Coming Soon", () ->
            message("Video download will be available after generation")
        );
    }

    void title(String text) {
        TextView t = new TextView(this);
        t.setText(text);
        t.setTextSize(25);
        t.setTextColor(Color.WHITE);
        t.setGravity(Gravity.CENTER);
        t.setPadding(0,10,0,30);
        t.setTypeface(null, 1);
        layout.addView(t);
    }

    void heading(String text) {
        TextView t = new TextView(this);
        t.setText(text);
        t.setTextSize(19);
        t.setTextColor(accent);
        t.setPadding(0,28,0,12);
        t.setTypeface(null, 1);
        layout.addView(t);
    }

    EditText edit(String hint) {
        EditText e = new EditText(this);
        e.setHint(hint);
        e.setTextColor(Color.WHITE);
        e.setHintTextColor(Color.GRAY);
        e.setPadding(15,12,15,12);
        layout.addView(e);
        return e;
    }

    Button button(String text) {
        Button b = new Button(this);
        b.setText(text);
        return b;
    }

    void action(String text, Runnable run) {
        Button b = button(text);
        layout.addView(b);
        b.setOnClickListener(v -> run.run());
    }

    void openImagePicker() {
        Intent intent = new Intent(Intent.ACTION_OPEN_DOCUMENT);
        intent.setType("image/*");
        intent.addCategory(Intent.CATEGORY_OPENABLE);

        startActivityForResult(intent, 101);
    }

    void message(String text) {
        Toast.makeText(
            this,
            text,
            Toast.LENGTH_LONG
        ).show();
    }

    @Override
    protected void onActivityResult(
        int requestCode,
        int resultCode,
        Intent data
    ) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == 101 &&
            resultCode == RESULT_OK &&
            data != null) {

            selectedImage = data.getData();

            message("Image Selected Successfully!");
        }
    }

    @Override
    protected void onDestroy() {
        if (tts != null) {
            tts.stop();
            tts.shutdown();
        }

        super.onDestroy();
    }
}
