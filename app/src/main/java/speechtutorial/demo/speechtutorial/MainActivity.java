package speechtutorial.demo.speechtutorial;

import android.content.Intent;
import android.speech.tts.TextToSpeech;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.SeekBar;
import android.widget.Toast;

import java.util.Locale;

public class MainActivity extends ActionBarActivity {

    public static int TTS_DATA_CHECK = 1;
    public static int VOICE_RECOGNITION = 2;

    public static float MIN_RATE = 0f;
    public static float MAX_RATE = 2f;

    public static float MIN_PITCH = 0f;
    public static float MAX_PITCH = 2f;

    private TextToSpeech tts, ttsagain;
    private boolean ttsIsInit, ttsagainIsInit = false;

    public String demotext = "This is a test of the text-to-speech engine in Android.";
    private EditText txtinput;

    private float pitch, rate;
    private SeekBar sbpitch, sbrate;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        txtinput = (EditText)findViewById(R.id.editText);
        sbpitch = (SeekBar)findViewById(R.id.pitch_sb);
        sbrate = (SeekBar)findViewById(R.id.rate_sb);
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    public void testTTS(View v) {
        demotext = txtinput.getText().toString();
        pitch = MIN_PITCH + (MAX_PITCH - MIN_PITCH) * ((float)sbpitch.getProgress() / sbpitch.getMax());
        Log.i("SpeechDemo", "pitch: " + sbpitch.getProgress());
        rate = MIN_RATE + (MAX_RATE - MIN_RATE) * ((float)sbrate.getProgress() / sbrate.getMax());
        Log.i("SpeechDemo", "rate: " + sbrate.getProgress());
        if (demotext.isEmpty()) {
            Log.i("SpeechDemo", "## ERROR 02: Field is Empty");
            demotext = "The field is empty. Type something first!";
        }else {
            Intent intent = new Intent(TextToSpeech.Engine.ACTION_CHECK_TTS_DATA);
            startActivityForResult(intent, TTS_DATA_CHECK);
        }
        Toast.makeText(getBaseContext(), "Testing Text to Speech", Toast.LENGTH_SHORT).show();
    }

    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == TTS_DATA_CHECK) {
            Log.i("SpeechDemo", "## INFO 01: RequestCode TTS_DATA_CHECK = " + requestCode);
            if (resultCode == TextToSpeech.Engine.CHECK_VOICE_DATA_PASS) {
                Log.i("SpeechDemo", "## INFO 03: CHECK_VOICE_DATA_PASS");
                tts = new TextToSpeech(this, new TextToSpeech.OnInitListener() {
                    public void onInit(int arg0) {
                        ttsIsInit = true;
                        if (tts.isLanguageAvailable(Locale.US) >= 0) {
                            tts.setLanguage(Locale.US);
                            tts.setPitch(pitch);
                            tts.setSpeechRate(rate);
                            tts.speak(demotext, TextToSpeech.QUEUE_ADD, null);
                        }
                    }
                });
            } else {
                Log.i("SpeechDemo", "## INFO 04: CHECK_VOICE_DATA_FAILED, resultCode = " + resultCode);
                Intent installVoice = new Intent(TextToSpeech.Engine.ACTION_INSTALL_TTS_DATA);
                startActivity(installVoice);
            }
        } else if (requestCode == VOICE_RECOGNITION) {
            Log.i("SpeechDemo", "## INFO 02: RequestCode VOICE_RECOGNITION = " + requestCode);
        } else {
            Log.i("SpeechDemo", "## ERROR 01: Unexpected RequestCode = " + requestCode);
        }
    }
}
