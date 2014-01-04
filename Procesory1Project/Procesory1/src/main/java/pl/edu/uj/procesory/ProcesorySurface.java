package pl.edu.uj.procesory;

import android.content.Context;
import android.opengl.GLSurfaceView;

/**
 * Created by mmasiuk on 22.12.13.
 */
public class ProcesorySurface extends GLSurfaceView {

    private ProcesoryRenderer procesoryRenderer;

    public ProcesorySurface(Context context) {
        super(context);

    setEGLContextClientVersion(2);

    procesoryRenderer = new ProcesoryRenderer();
    setRenderer(procesoryRenderer);

    setRenderMode(GLSurfaceView.RENDERMODE_CONTINUOUSLY);

    }
}
