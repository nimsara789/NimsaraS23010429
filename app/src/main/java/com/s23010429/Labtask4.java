package com.s23010429;

import android.content.Context;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

public class Labtask4 extends AppCompatActivity implements SensorEventListener {

    // Define the temperature threshold (last two digits of student ID S23010429)
    private static final float TEMPERATURE_THRESHOLD = 29.0f;

    // UI Components
    private TextView temperatureTextView;
    private Button resetButton;

    // Sensor components
    private SensorManager sensorManager;
    private Sensor temperatureSensor;

    // Media player
    private MediaPlayer mediaPlayer;

    // Track if audio is currently playing
    private boolean isPlaying = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_labtask4);

        // Initialize UI components
        temperatureTextView = findViewById(R.id.temperatureTextView);
        resetButton = findViewById(R.id.resetButton);

        // Set up the reset button click listener
        resetButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                resetMediaPlayer();
                Toast.makeText(Labtask4.this, "Media player reset", Toast.LENGTH_SHORT).show();
            }
        });

        // Initialize sensor manager and get temperature sensor
        sensorManager = (SensorManager) getSystemService(Context.SENSOR_SERVICE);
        if (sensorManager != null) {
            temperatureSensor = sensorManager.getDefaultSensor(Sensor.TYPE_AMBIENT_TEMPERATURE);

            if (temperatureSensor == null) {
                temperatureTextView.setText("Temperature sensor not available");
                Toast.makeText(this, "Temperature sensor not available on this device", Toast.LENGTH_LONG).show();
            }
        }

        // Initialize media player
        initializeMediaPlayer();
    }

    private void initializeMediaPlayer() {
        try {
            mediaPlayer = MediaPlayer.create(this, R.raw.track);
            mediaPlayer.setLooping(true); // Optional: set to loop the audio

            // Set up error listener
            mediaPlayer.setOnErrorListener(new MediaPlayer.OnErrorListener() {
                @Override
                public boolean onError(MediaPlayer mp, int what, int extra) {
                    Toast.makeText(Labtask4.this, "Media player error", Toast.LENGTH_SHORT).show();
                    return false;
                }
            });
        } catch (Exception e) {
            Toast.makeText(this, "Failed to initialize media player: " + e.getMessage(), Toast.LENGTH_SHORT).show();
        }
    }

    private void resetMediaPlayer() {
        if (mediaPlayer != null) {
            if (isPlaying) {
                mediaPlayer.pause();
                isPlaying = false;
            }
            mediaPlayer.seekTo(0);
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        // Register sensor listener when activity becomes visible
        if (temperatureSensor != null) {
            sensorManager.registerListener(this, temperatureSensor, SensorManager.SENSOR_DELAY_NORMAL);
        }
    }

    @Override
    protected void onPause() {
        super.onPause();
        // Unregister sensor listener when activity is not visible
        if (sensorManager != null) {
            sensorManager.unregisterListener(this);
        }

        // Pause the media player when the activity is not visible
        if (mediaPlayer != null && mediaPlayer.isPlaying()) {
            mediaPlayer.pause();
            isPlaying = false;
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        // Release media player resources
        if (mediaPlayer != null) {
            mediaPlayer.release();
            mediaPlayer = null;
        }
    }

    @Override
    public void onSensorChanged(SensorEvent event) {
        if (event.sensor.getType() == Sensor.TYPE_AMBIENT_TEMPERATURE) {
            float temperature = event.values[0];

            // Update the temperature display
            temperatureTextView.setText(String.format("Current Temperature: %.1f°C", temperature));

            // Check if temperature exceeds the threshold
            if (temperature > TEMPERATURE_THRESHOLD) {
                // Play audio if it's not already playing
                if (mediaPlayer != null && !isPlaying) {
                    mediaPlayer.start();
                    isPlaying = true;
                    Toast.makeText(this, "Temperature exceeded threshold! Playing audio.", Toast.LENGTH_SHORT).show();
                }
            } else {
                // Pause audio if it's playing
                if (mediaPlayer != null && isPlaying) {
                    mediaPlayer.pause();
                    isPlaying = false;
                    Toast.makeText(this, "Temperature below threshold. Audio paused.", Toast.LENGTH_SHORT).show();
                }
            }
        }
    }

    @Override
    public void onAccuracyChanged(Sensor sensor, int accuracy) {
        // Handle sensor accuracy changes if needed
    }
}