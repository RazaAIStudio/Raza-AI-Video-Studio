package com.raza.aivideostudio;

import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.Gravity;
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
        page.setGravity(Gravity.CENTER);
        page.setBackgroundColor(0xFF10131A);

        TextView title = new TextView(this);
        title.setText("RAZA AI VIDEO STUDIO");
        title.setTextColor(0xFFFFFFFF);
        title.setTextSize(25);
        title.setGravity(Gravity.CENTER);
        page.addView(title);

        TextView subtitle = new TextView(this);
        subtitle.setText("Create Videos on Your Phone");
        subtitle.setTextColor(0xFFFFFFFF);
        page.addView(subtitle);

        LinearLayout ratios = new LinearLayout(this);

        for (String ratio : new String[]{"9:16", "16:9", "1:1"}) {
            Button button = new Button(this);
            button.setText(ratio);
            button.setOnClickListener(v -> {
                selectedRatio = ratio;
                status.setText("Selected: " + ratio);
            });
            ratios.addView(button, new LinearLayout.LayoutParams(0, -2, 1));
        }

        page.addView(ratios);

        Button choose = new Button(this);
        choose.setText("SELECT VIDEO");
        choose.setOnClickListener(v -> {
            Intent intent = new Intent(Intent.ACTION_GET_CONTENT);
            intent.setType("video/*");
            startActivityForResult(intent, PICK_VIDEO);
        });
        page.addView(choose);

        videoView = new VideoView(this);
        page.addView(videoView, new LinearLayout.LayoutParams(-1, 0, 1));

        status = new TextView(this);
        status.setText("Ready | 9:16");
        status.setTextColor(0xFFFFFFFF);
        page.addView(status);

        setContentView(page);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == PICK_VIDEO && resultCode == RESULT_OK && data != null) {
            Uri uri = data.getData();
            videoView.setVideoURI(uri);
            videoView.setOnPreparedListener(mp -> videoView.start());
            status.setText("Video loaded | " + selectedRatio);
        }
    }
}
