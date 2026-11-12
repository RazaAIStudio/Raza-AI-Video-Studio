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

        action("PLAY VOICE", () -> {
            String text = voiceText.getText().toString();

            if (!text.isEmpty()) {
                tts.speak(
                    text,
                    TextToSpeech.QUEUE_FLUSH,
                    null,
                    "voice"
                );
            } else {
                message("Enter some text first");
            }
        });

        heading("AI SCRIPT GENERATOR");

        EditText scriptTopic = edit("Enter your video topic...");

        action("GENERATE SCRIPT", () -> {
            String topic = scriptTopic.getText().toString();

            if (topic.isEmpty()) {
                message("Enter video topic");
            } else {
                prompt.setText(
                    "Create an engaging video about " + topic
                );
                message("Script idea added to video prompt");
            }
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
