package com.raza.aivideostudio;

import android.app.*;
import android.os.*;
import android.graphics.Color;
import android.view.*;
import android.widget.*;
import android.content.*;
import android.net.Uri;
import android.speech.tts.TextToSpeech;
import android.speech.tts.Voice;
import android.media.MediaPlayer;
import android.graphics.ImageDecoder;
import android.graphics.drawable.Drawable;

import java.util.*;

public class MainActivity extends Activity {

    LinearLayout layout;
    EditText prompt, voiceText, scriptTopic;
    TextToSpeech tts;

    Spinner languageSpinner, genderSpinner;
    SeekBar speedBar, pitchBar;

    String ratio = "9:16";
    String qualityValue = "720p";

    Uri selectedImage;
    Uri selectedMusic;

    ImageView imagePreview;
    MediaPlayer player;

    boolean ttsReady = false;

    int bg = Color.rgb(15,18,30);
    int accent = Color.rgb(0,220,180);

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        ScrollView scroll = new ScrollView(this);

        layout = new LinearLayout(this);
        layout.setOrientation(LinearLayout.VERTICAL);
        layout.setPadding(25,30,25,30);
        layout.setBackgroundColor(bg);

        scroll.addView(layout);
        setContentView(scroll);

        title("RAZA AI VIDEO STUDIO");
        heading("CREATE AI VIDEOS");

        tts = new TextToSpeech(this, status -> {
            if(status == TextToSpeech.SUCCESS) {
                ttsReady = true;
                message("Voice Engine Ready");
            }
        });

        // VIDEO RATIO

        heading("VIDEO RATIO");

        String[] ratios = {"9:16","16:9","1:1"};

        Spinner ratioSpinner = spinner(ratios);

        ratioSpinner.setOnItemSelectedListener(
            new android.widget.AdapterView.OnItemSelectedListener() {
                public void onItemSelected(
                    android.widget.AdapterView<?> p,
                    View v,int pos,long id) {
                    ratio = ratios[pos];
                }

                public void onNothingSelected(
                    android.widget.AdapterView<?> p) {}
            }
        );

        layout.addView(ratioSpinner);

        // VIDEO PROMPT

        heading("AI VIDEO PROMPT");

        prompt = edit("Write your video idea here...");

        action("GENERATE AI VIDEO", () -> {
            if(prompt.getText().toString().trim().isEmpty()) {
                message("Enter video idea first");
            } else {
                message("AI video API connection is required. Video generation is not connected yet.");
            }
        });

        // IMAGE TO VIDEO

        heading("IMAGE TO VIDEO");

        action("SELECT IMAGE", () -> openPicker(false));

        imagePreview = new ImageView(this);
        imagePreview.setAdjustViewBounds(true);
        imagePreview.setMaxHeight(500);
        layout.addView(imagePreview);

        action("GENERATE FROM IMAGE", () -> {
            if(selectedImage == null) {
                message("Select an image first");
            } else {
                message("Image selected. AI video API connection is required.");
            }
        });

        // QUALITY

        heading("VIDEO QUALITY");

        String[] qualities = {"720p","1080p","4K"};

        Spinner qualitySpinner = spinner(qualities);

        qualitySpinner.setOnItemSelectedListener(
            new android.widget.AdapterView.OnItemSelectedListener() {
                public void onItemSelected(
                    android.widget.AdapterView<?> p,
                    View v,int pos,long id) {
                    qualityValue = qualities[pos];
                }

                public void onNothingSelected(
                    android.widget.AdapterView<?> p) {}
            }
        );

        layout.addView(qualitySpinner);

        // VOICE GENERATOR

        heading("AI VOICE GENERATOR");

        voiceText = edit("Enter text for AI voice...");

        heading("VOICE LANGUAGE");

        languageSpinner = spinner(
            new String[]{"Hindi","English"}
        );

        layout.addView(languageSpinner);

        heading("VOICE GENDER");

        genderSpinner = spinner(
            new String[]{"Male","Female"}
        );

        layout.addView(genderSpinner);

        heading("VOICE SPEED");

        speedBar = new SeekBar(this);
        speedBar.setMax(15);
        speedBar.setProgress(5);
        layout.addView(speedBar);

        heading("VOICE PITCH");

        pitchBar = new SeekBar(this);
        pitchBar.setMax(15);
        pitchBar.setProgress(5);
        layout.addView(pitchBar);

        action("PLAY VOICE", () -> playVoice());

        action("STOP VOICE", () -> {
            if(tts != null) tts.stop();
        });

        // SCRIPT GENERATOR

        heading("AI SCRIPT GENERATOR");

        scriptTopic = edit("Enter your video topic...");

        action("GENERATE SCRIPT", () -> generateScript());

        // BACKGROUND MUSIC

        heading("BACKGROUND MUSIC");

        action("SELECT MUSIC", () -> openPicker(true));

        action("PLAY MUSIC", () -> playMusic());

        action("STOP MUSIC", () -> {
            if(player != null) {
                player.stop();
                player.release();
                player = null;
            }
            message("Music stopped");
        });

        // DOWNLOAD

        heading("DOWNLOAD VIDEO");

        action("DOWNLOAD VIDEO", () -> {
            message("Download will work after AI video generation is connected.");
        });

        heading("RAZA AI VIDEO STUDIO");
        heading("CREATE • EDIT • SHARE");

    }

    void playVoice() {

        String text = voiceText.getText().toString().trim();

        if(text.isEmpty()) {
            message("Enter text first");
            return;
        }

        if(!ttsReady) {
            message("Voice engine loading. Try again.");
            return;
        }

        String lang =
            languageSpinner.getSelectedItem().toString();

        String gender =
            genderSpinner.getSelectedItem().toString();

        Locale locale = lang.equals("Hindi")
            ? new Locale("hi","IN")
            : Locale.US;

        int result = tts.setLanguage(locale);

        if(result == TextToSpeech.LANG_MISSING_DATA ||
           result == TextToSpeech.LANG_NOT_SUPPORTED) {
            message("Language not supported on this phone");
            return;
        }

        // Try to find a matching installed voice

        Set<Voice> voices = tts.getVoices();

        if(voices != null) {
            for(Voice voice : voices) {

                if(voice.getLocale().getLanguage()
                    .equals(locale.getLanguage())) {

                    String name =
                        voice.getName().toLowerCase(Locale.ROOT);

                    boolean match =
                        gender.equals("Male")
                        ? (name.contains("male") ||
                           name.contains("man"))
                        : (name.contains("female") ||
                           name.contains("woman"));

                    if(match) {
                        tts.setVoice(voice);
                        break;
                    }
                }
            }
        }

        float speed =
            0.5f + speedBar.getProgress() * 0.1f;

        float pitch =
            0.5f + pitchBar.getProgress() * 0.1f;

        tts.setSpeechRate(speed);
        tts.setPitch(pitch);

        tts.speak(
            text,
            TextToSpeech.QUEUE_FLUSH,
            null,
            "raza_voice"
        );
    }

    void generateScript() {

        String topic =
            scriptTopic.getText().toString().trim();

        if(topic.isEmpty()) {
            message("Enter video topic");
            return;
        }

        String script =
            "TITLE: " + topic + "\n\n" +

            "HOOK:\n" +
            "Kya aap jaante hain " + topic +
            " ke baare mein?\n\n" +

            "INTRO:\n" +
            "Namaskar dosto! Aaj hum jaanenge " +
            topic + " ke baare mein.\n\n" +

            "MAIN CONTENT:\n" +
            topic +
            " ek interesting topic hai. " +
            "Is video mein hum iske important points " +
            "aur useful information samjhenge.\n\n" +

            "ENDING:\n" +
            "Agar video pasand aaye to Like, Share " +
            "aur Follow zaroor karein.\n\n" +

            "THANK YOU FOR WATCHING!";

        prompt.setText(script);

        message("Script Generated!");
    }

    void openPicker(boolean music) {

        Intent intent =
            new Intent(Intent.ACTION_OPEN_DOCUMENT);

        intent.setType(music ? "audio/*" : "image/*");

        intent.addCategory(Intent.CATEGORY_OPENABLE);

        startActivityForResult(intent,music ? 102 : 101);
    }

    void playMusic() {

        if(selectedMusic == null) {
            message("Select music first");
            return;
        }

        try {

            if(player != null) {
                player.release();
            }

            player = new MediaPlayer();

            player.setDataSource(
                this,selectedMusic
            );

            player.setOnPreparedListener(mp -> {
                mp.start();
                message("Music playing");
            });

            player.prepareAsync();

        } catch(Exception e) {
            message("Unable to play music");
        }
    }

    Spinner spinner(String[] values) {

        Spinner s = new Spinner(this);

        ArrayAdapter<String> adapter =
            new ArrayAdapter<>(
                this,
                android.R.layout.simple_spinner_item,
                values
            );

        adapter.setDropDownViewResource(
            android.R.layout.simple_spinner_dropdown_item
        );

        s.setAdapter(adapter);

        return s;
    }

    void title(String text) {

        TextView t = new TextView(this);

        t.setText(text);
        t.setTextSize(25);
        t.setTextColor(Color.WHITE);
        t.setGravity(Gravity.CENTER);
        t.setPadding(0,10,0,30);
        t.setTypeface(null,1);

        layout.addView(t);
    }

    void heading(String text) {

        TextView t = new TextView(this);

        t.setText(text);
        t.setTextSize(19);
        t.setTextColor(accent);
        t.setPadding(0,28,0,12);
        t.setTypeface(null,1);

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

    void action(String text,Runnable run) {

        Button b = button(text);

        layout.addView(b);

        b.setOnClickListener(v -> run.run());
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
        Intent data) {

        super.onActivityResult(
            requestCode,resultCode,data
        );

        if(resultCode == RESULT_OK &&
           data != null) {

            Uri uri = data.getData();

            if(requestCode == 101) {

                selectedImage = uri;

                try {
                    Drawable drawable =
                        Drawable.createFromStream(
                            getContentResolver()
                            .openInputStream(uri),
                            null
                        );

                    imagePreview.setImageDrawable(drawable);

                    message("Image selected!");

                } catch(Exception e) {
                    message("Image preview failed");
                }

            } else if(requestCode == 102) {

                selectedMusic = uri;

                message("Music selected!");
            }
        }
    }

    @Override
    protected void onDestroy() {

        if(tts != null) {
            tts.stop();
            tts.shutdown();
        }

        if(player != null) {
            player.release();
            player = null;
        }

        super.onDestroy();
    }
}
