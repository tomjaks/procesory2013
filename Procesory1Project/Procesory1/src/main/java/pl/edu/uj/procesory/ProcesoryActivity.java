package pl.edu.uj.procesory;

import android.app.Activity;
import android.opengl.GLSurfaceView;
import android.os.Bundle;


public class ProcesoryActivity extends Activity {

    private GLSurfaceView surface;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        surface = new ProcesorySurface(this);
        setContentView(surface);

        }
    }