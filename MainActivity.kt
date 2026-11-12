package com.raza.aivideostudio

import android.app.Activity
import android.os.Bundle
import android.graphics.Color
import android.view.Gravity
import android.widget.*

class MainActivity : Activity() {
    private val bg = Color.rgb(13, 18, 32)
    private val card = Color.rgb(27, 36, 57)
    private val accent = Color.rgb(104, 94, 255)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val scroll = ScrollView(this).apply { setBackgroundColor(bg) }
        val root = LinearLayout(this).apply {
            orientation = LinearLayout.VERTICAL
            setPadding(22, 32, 22, 28)
        }
        scroll.addView(root)

        root.addView(label("RAZA AI VIDEO STUDIO", 25f, Color.WHITE, true))
        root.addView(label("Create videos from text or images", 14f, 0xFFB9C3D8.toInt(), false))
        spacer(root, 24)

        root.addView(section("Video creation"))
        val mode = Spinner(this).apply {
            adapter = ArrayAdapter(this@MainActivity, android.R.layout.simple_spinner_dropdown_item,
                listOf("Text to Video", "Image to Video"))
        }
        root.addView(mode)
        spacer(root, 14)

        root.addView(section("Describe your video"))
        val prompt = EditText(this).apply {
            hint = "Example: A cinematic view of mountains at sunrise..."
            setHintTextColor(0xFF9AA7C0.toInt())
            setTextColor(Color.WHITE)
            setPadding(14, 14, 14, 14)
            minLines = 3
            gravity = Gravity.TOP
            setBackgroundColor(card)
        }
        root.addView(prompt, matchWrap())
        spacer(root, 14)

        root.addView(section("Aspect ratio"))
        val ratio = Spinner(this).apply {
            adapter = ArrayAdapter(this@MainActivity, android.R.layout.simple_spinner_dropdown_item,
                listOf("9:16 – Reels / Shorts", "16:9 – YouTube", "1:1 – Square"))
        }
        root.addView(ratio)
        spacer(root, 14)

        root.addView(section("Quality"))
        val quality = Spinner(this).apply {
            adapter = ArrayAdapter(this@MainActivity, android.R.layout.simple_spinner_dropdown_item,
                listOf("720p HD", "1080p Full HD", "4K UHD (backend required)"))
        }
        root.addView(quality)
        spacer(root, 20)

        val create = Button(this).apply {
            text = "GENERATE VIDEO"
            setBackgroundColor(accent)
            setTextColor(Color.WHITE)
            setOnClickListener {
                Toast.makeText(this@MainActivity,
                    "App interface is ready. Connect an AI video-generation service to generate videos.",
                    Toast.LENGTH_LONG).show()
            }
        }
        root.addView(create, matchWrap())
        spacer(root, 20)
        root.addView(label("Status: Interface preview build", 13f, 0xFFFFD27A.toInt(), true))
        root.addView(label("AI generation is not connected yet. A compatible service/API and server are required. Free unlimited 4K cannot be guaranteed.", 12f, 0xFFB9C3D8.toInt(), false))
        setContentView(scroll)
    }

    private fun section(s: String) = label(s, 16f, Color.WHITE, true)
    private fun label(s: String, size: Float, color: Int, bold: Boolean) =
        TextView(this).apply {
            text = s; textSize = size; setTextColor(color)
            if (bold) setTypeface(null, android.graphics.Typeface.BOLD)
            setPadding(0, 5, 0, 8)
        }
    private fun spacer(root: LinearLayout, h: Int) {
        root.addView(android.view.View(this), LinearLayout.LayoutParams(1, h))
    }
    private fun matchWrap() = LinearLayout.LayoutParams(
        LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT
    )
}
