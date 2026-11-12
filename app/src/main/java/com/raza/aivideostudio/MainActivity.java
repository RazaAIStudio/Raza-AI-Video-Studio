package com.raza.aivideostudio;

import android.app.*;
import android.os.*;
import android.graphics.Color;
import android.view.*;
import android.widget.*;
import android.content.*;
import android.speech.tts.TextToSpeech;
import android.net.Uri;
import java.util.Locale;

public class MainActivity extends Activity {

    LinearLayout layout;
    EditText prompt, voiceText, scriptText;
    TextToSpeech tts;
    String ratio = "9:16";
    Uri imageUri;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        ScrollView scroll = new ScrollView(this);

        layout = new LinearLayout(this);
        layout.setOrientation(LinearLayout.VERTICAL);
        layout.setPadding(25,30,25,30);
        layout.setBackgroundColor(Color.rgb(18,18,25));

        scroll.addView(layout);
        setContentView(scroll);

        tts = new TextToSpeech(this, status -> {
            if(status == TextToSpeech.SUCCESS) {
                tts.setLanguage(Locale.getDefault());
            }
        });

        title("RAZA AI VIDEO STUDIO");

        heading("VIDEO RATIO");

        addButton("9:16");
        addButton("16:9");
        addButton("1:1");

        heading("TEXT TO VIDEO AI");

        prompt = input("Describe your video...");

        action("Generate From Text", () ->
            message("AI Video API connection required.")
        );

        heading("IMAGE TO VIDEO AI");

        action("Select Image", () -> {
            Intent i = new Intent(Intent.ACTION_GET_CONTENT);
            i.setType("image/*");
            startActivityForResult(i,101);
        });

        action("Generate From Image", () ->
            message("AI Image-to-Video API required.")
        );

        heading("AI VOICE GENERATOR");

        voiceText = input("Enter text for AI voice...");

        action("Speak Text", () -> {
            String text = voiceText.getText().toString();

            if(!text.isEmpty()) {
                tts.speak(text,
                    TextToSpeech.QUEUE_FLUSH,
                    null,
                    "voice1");
            } else {
                message("Please enter text.");
            }
        });

        action("Stop Voice", () -> {
            tts.stop();
        });

        heading("AI SCRIPT GENERATOR");

        scriptText = input("Enter your video topic...");

        action("Generate Script", () -> {

            String topic = scriptText.getText().toString();

            if(topic.isEmpty()) {
                message("Enter video topic first.");
                return;
            }

            String result =
                "TITLE: " + topic +
                "\n\nINTRO:\nWelcome to our video!\n" +
                "\nMAIN CONTENT:\n" +
                "Today we are going to explore " + topic +
                ". Stay with us to learn more.\n" +
                "\nENDING:\nThanks for watching! Like, Share and Follow.";

            scriptText.setText(result);
        });

        heading("VIDEO QUALITY");

        Spinner quality = new Spinner(this);

        String[] options = {
            "720p", "1080p", "4K"
        };

        ArrayAdapter<String> adapter =
            new ArrayAdapter<>(
                this,
                android.R.layout.simple_spinner_dropdown_item,
                options
            );

        quality.setAdapter(adapter);
        layout.addView(quality);

        heading("BACKGROUND MUSIC");

        action("Music Feature - Coming Soon", () ->
            message("Music feature will be added next.")
        );

        heading("DOWNLOAD VIDEO");

        action("Download - Coming Soon", () ->
            message("Video export feature will be added.")
        );
    }

    void title(String text) {
        TextView t = new TextView(this);
        t.setText(text);
        t.setTextSize(25);
        t.setTextColor(Color.WHITE);
        t.setGravity(Gravity.CENTER);
        t.setPadding(0,10,0,30);
        layout.addView(t);
    }

    void heading(String text) {
        TextView t = new TextView(this);
        t.setText(text);
        t.setTextSize(19);
        t.setTextColor(Color.rgb(0,220,180));
        t.setPadding(0,28,0,12);
        layout.addView(t);
    }

    EditText input(String hint) {
        EditText e = new EditText(this);
        e.setHint(hint);
        e.setTextColor(Color.WHITE);
        e.setHintTextColor(Color.GRAY);
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

    void addButton(String text) {
        action(text, () -> {
            ratio = text;
            message("Selected Ratio: " + ratio);
        });
    }

    void message(String text) {
        Toast.makeText(this,text,Toast.LENGTH_LONG).show();
    }

    @Override
    protected void onActivityResult(
        int requestCode, int resultCode, Intent data) {

        super.onActivityResult(requestCode,resultCode,data);

        if(requestCode == 101 &&
           resultCode == RESULT_OK &&
           data != null) {

            imageUri = data.getData();
            message("Image Selected Successfully!");
        }
    }

    @Override
    protected void onDestroy() {
        if(tts != null) {
            tts.stop();
            tts.shutdown();
        }
        super.onDestroy();
    }
}
