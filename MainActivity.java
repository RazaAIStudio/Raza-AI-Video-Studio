package com.raza.aivideostudio;

import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.Gravity;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.VideoView;

public class MainActivity extends Activity {
    private static final int PICK_VIDEO = 101;
    private VideoView videoView;
    private TextView status;
    private String selectedRatio = "9:16";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        LinearLayout page = new LinearLayout(this);
        page.setOrientation(LinearLayout.VERTICAL);
        page.setPadding(24, 42, 24, 24);
        page.setGravity(Gravity.CENTER_HORIZONTAL);
        page.setBackgroundColor(0xFF10131A);

        TextView title = new TextView(this);
        title.setText("RAZA AI VIDEO STUDIO");
        title.setTextColor(0xFFFFFFFF);
        title.setTextSize(25);
        title.setGravity(Gravity.CENTER);
        title.setPadding(0, 8, 0, 12);
        page.addView(title, matchWrap());

        TextView subtitle = new TextView(this);
        subtitle.setText("Create and preview videos on your phone");
        subtitle.setTextColor(0xFFB8C1D1);
        subtitle.setTextSize(14);
        subtitle.setGravity(Gravity.CENTER);
        page.addView(subtitle, matchWrap());

        TextView ratioLabel = new TextView(this);
        ratioLabel.setText("VIDEO RATIO");
        ratioLabel.setTextColor(0xFFFFFFFF);
        ratioLabel.setTextSize(15);
        ratioLabel.setPadding(0, 28, 0, 10);
        page.addView(ratioLabel, matchWrap());

        LinearLayout ratios = new LinearLayout(this);
        ratios.setOrientation(LinearLayout.HORIZONTAL);
        String[] choices = {"9:16", "16:9", "1:1"};
        for (String choice : choices) {
            Button b = new Button(this);
            b.setText(choice);
            b.setOnClickListener(v -> {
                selectedRatio = choice;
                status.setText("Selected ratio: " + selectedRatio);
            });
            ratios.addView(b, new LinearLayout.LayoutParams(0, -2, 1));
        }
        page.addView(ratios, matchWrap());

        Button choose = new Button(this);
        choose.setText("SELECT VIDEO FROM PHONE");
        choose.setOnClickListener(v -> {
            Intent intent = new Intent(Intent.ACTION_GET_CONTENT);
            intent.setType("video/*");
            startActivityForResult(Intent.createChooser(intent, "Choose a video"), PICK_VIDEO);
        });
        LinearLayout.LayoutParams buttonParams = matchWrap();
        buttonParams.topMargin = 24;
        page.addView(choose, buttonParams);

        videoView = new VideoView(this);
        LinearLayout.LayoutParams videoParams = new LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.MATCH_PARENT, 0, 1);
        videoParams.topMargin = 18;
        videoParams.bottomMargin = 12;
        page.addView(videoView, videoParams);

        status = new TextView(this);
        status.setText("Ready • Selected ratio: 9:16");
        status.setTextColor(0xFFFFFFFF);
        status.setTextSize(14);
        status.setGravity(Gravity.CENTER);
        page.addView(status, matchWrap());

        TextView note = new TextView(this);
        note.setText("Starter version: local video preview and ratio selection. AI generation and 4K export are not connected yet.");
        note.setTextColor(0xFFFFD27A);
        note.setTextSize(12);
        note.setGravity(Gravity.CENTER);
        note.setPadding(0, 18, 0, 0);
        page.addView(note, matchWrap());

        setContentView(page);
    }

    private LinearLayout.LayoutParams matchWrap() {
        return new LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.MATCH_PARENT,
                LinearLayout.LayoutParams.WRAP_CONTENT);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == PICK_VIDEO && resultCode == RESULT_OK && data != null) {
            Uri uri = data.getData();
            if (uri != null) {
                videoView.setVideoURI(uri);
                videoView.setOnPreparedListener(mp -> {
                    mp.setLooping(true);
                    videoView.start();
                });
                status.setText("Video loaded • Ratio: " + selectedRatio);
            }
        }
    }
}
