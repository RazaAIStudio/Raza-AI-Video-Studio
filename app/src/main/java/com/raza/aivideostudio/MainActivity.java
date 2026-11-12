package com.raza.aivideostudio;

import android.app.*;
import android.os.*;
import android.graphics.Color;
import android.view.*;
import android.widget.*;
import android.content.*;
import android.net.Uri;

public class MainActivity extends Activity {

    LinearLayout layout;
    String ratio = "9:16";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        ScrollView scroll = new ScrollView(this);
        layout = new LinearLayout(this);
        layout.setOrientation(LinearLayout.VERTICAL);
        layout.setPadding(25, 30, 25, 30);
        layout.setBackgroundColor(Color.rgb(18,18,25));
        scroll.addView(layout);
        setContentView(scroll);

        TextView title = new TextView(this);
        title.setText("RAZA AI VIDEO STUDIO");
        title.setTextSize(25);
        title.setTextColor(Color.WHITE);
        title.setGravity(Gravity.CENTER);
        layout.addView(title);

        addText("Select Video Ratio");

        addButton("9:16");
        addButton("16:9");
        addButton("1:1");

        addText("Text To Video");

        EditText prompt = new EditText(this);
        prompt.setHint("Describe your video...");
        prompt.setTextColor(Color.WHITE);
        prompt.setHintTextColor(Color.GRAY);
        layout.addView(prompt);

        Button textGenerate = button("Generate From Text");
        layout.addView(textGenerate);

        addText("Image To Video");

        Button imageSelect = button("Select Image");
        layout.addView(imageSelect);

        Button imageGenerate = button("Generate From Image");
        layout.addView(imageGenerate);

        addText("Video Quality");

        Spinner quality = new Spinner(this);
        String[] qualities = {"720p", "1080p", "4K"};
        ArrayAdapter<String> adapter = new ArrayAdapter<>(
            this, android.R.layout.simple_spinner_dropdown_item, qualities);
        quality.setAdapter(adapter);
        layout.addView(quality);

        textGenerate.setOnClickListener(v ->
            Toast.makeText(this,
            "AI service connection is required for generation.",
            Toast.LENGTH_LONG).show());

        imageSelect.setOnClickListener(v -> {
            Intent intent = new Intent(Intent.ACTION_GET_CONTENT);
            intent.setType("image/*");
            startActivityForResult(intent, 101);
        });

        imageGenerate.setOnClickListener(v ->
            Toast.makeText(this,
            "AI service connection is required for generation.",
            Toast.LENGTH_LONG).show());
    }

    void addText(String text) {
        TextView t = new TextView(this);
        t.setText(text);
        t.setTextSize(18);
        t.setTextColor(Color.WHITE);
        t.setPadding(0, 30, 0, 12);
        layout.addView(t);
    }

    Button button(String text) {
        Button b = new Button(this);
        b.setText(text);
        return b;
    }

    void addButton(String text) {
        Button b = button(text);
        layout.addView(b);
        b.setOnClickListener(v -> {
            ratio = text;
            Toast.makeText(this,
                "Selected: " + ratio, Toast.LENGTH_SHORT).show();
        });
    }
}
