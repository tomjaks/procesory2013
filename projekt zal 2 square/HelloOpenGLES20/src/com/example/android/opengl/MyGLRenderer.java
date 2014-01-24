/*
 * Copyright (C) 2011 The Android Open Source Project
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.example.android.opengl;

import javax.microedition.khronos.egl.EGLConfig;
import javax.microedition.khronos.opengles.GL10;

import android.opengl.GLES20;
import android.opengl.GLSurfaceView;
import android.opengl.Matrix;
import android.util.Log;

/**
 * Provides drawing instructions for a GLSurfaceView object. This class
 * must override the OpenGL ES drawing lifecycle methods:
 * <ul>
 *   <li>{@link android.opengl.GLSurfaceView.Renderer#onSurfaceCreated}</li>
 *   <li>{@link android.opengl.GLSurfaceView.Renderer#onDrawFrame}</li>
 *   <li>{@link android.opengl.GLSurfaceView.Renderer#onSurfaceChanged}</li>
 * </ul>
 */
public class MyGLRenderer implements GLSurfaceView.Renderer {

    private static final String TAG = "MyGLRenderer";
    private Triangle mTriangle1,mTriangle2,mTriangle3,mTriangle4,mTriangle5,mTriangle6;
    private Square   mSquare1, mSquare2, mSquare3, mSquare4, mSquare5, mSquare6;

    // mMVPMatrix is an abbreviation for "Model View Projection Matrix"
    private final float[] mMVPMatrix = new float[16];
    private final float[] mProjectionMatrix = new float[16];
    private final float[] mViewMatrix = new float[16];
    private final float[] mRotationMatrix = new float[16];

    private float mAngle;

    private float co[] = {
            0.0f, 0.0f, 0.7f,   // hight    co[0],co[1],co[2],
            0.0f,  0.5f, 0.0f,   // top     co[3],co[4],co[5],
            -0.5f, 0.0f, 0.0f,   // left    co[6],co[7],co[8],
            0.0f, -0.5f, 0.0f,   // bottom  co[9],co[10],co[11],
            0.5f, 0.0f, 0.0f    // right    co[12],co[13],co[14],

    };

    private float coordPodstawa1[] = {
            // in counterclockwise order:
            co[3],co[4],co[5],
            co[6],co[7],co[8],
            co[9],co[10],co[11]
    };

    private float coordPodstawa2[] = {
            // in counterclockwise order:
            co[3],co[4],co[5],
            co[12],co[13],co[14],
            co[9],co[10],co[11]
    };

    private float coord1[] = {
            // in counterclockwise order:
          co[3],co[4],co[5],
          co[6],co[7],co[8],
          co[0],co[1],co[2]
    };

    private float coord2[] = {
            // in counterclockwise order:
            co[6],co[7],co[8],
            co[9],co[10],co[11],
            co[0],co[1],co[2]
    };

    private float coord3[] = {
            // in counterclockwise order:
            co[9],co[10],co[11],
            co[12],co[13],co[14],
            co[0],co[1],co[2]
    };

    private float coord4[] = {
            // in counterclockwise order:
            co[12],co[13],co[14],
            co[3],co[4],co[5],
            co[0],co[1],co[2]
    };

    private float kwadrat1[] = {
            -0.5f,  0.5f, 0.0f,   // top left
            -0.5f, -0.5f, 0.0f,   // bottom left
            0.5f, -0.5f, 0.0f,   // bottom right
            0.5f,  0.5f, 0.0f
    };

    private float kwadrat2[] = {
            -0.5f,  -0.5f, 0.0f,   // top left
            -0.5f, -0.5f, 1.0f,   // bottom left
            0.5f, -0.5f, 1.0f,   // bottom right
            0.5f,  -0.5f, 0.0f
    };

    private float kwadrat3[] = {
            -0.5f,  0.5f, 0.0f,   // top left
            -0.5f, 0.5f, 1.0f,   // bottom left
            -0.5f, -0.5f, 1.0f,   // bottom right
            -0.5f,  -0.5f, 0.0f
    };
    private float kwadrat4[] = {
            0.5f,  -0.5f, 0.0f,   // top left
            0.5f, -0.5f, 1.0f,   // bottom left
            0.5f, 0.5f, 1.0f,   // bottom right
            0.5f,  0.5f, 0.0f
    };

    private float kwadrat5[] = {
            -0.5f,  0.5f, 0.0f,   // top left
            -0.5f, 0.5f, 1.0f,   // bottom left
            0.5f, 0.5f, 1.0f,   // bottom right
            0.5f,  0.5f, 0.0f
    };
    private float kwadrat6[] = {
            -0.5f,  0.5f, 1.0f,   // top left
            -0.5f, -0.5f, 1.0f,   // bottom left
            0.5f, -0.5f, 1.0f,   // bottom right
            0.5f,  0.5f, 1.0f
    };
    float kolorPodstawy[] = { 0.5f, 0.1f, 0.2f, 0.0f };
    float kolor1[] = { 0.0f, 0.0f, 0.9f, 0.0f };
    float kolor2[] = { 0.0f, 0.9f, 0.0f, 0.0f };
    float kolor3[] = { 0.9f, 0.0f, 0.f, 0.0f };
    float kolor4[] = { 0.0f, 0.5f, 0.5f, 0.0f };
    float kolor5[] = { 0.6f, 0.5f, 0.5f, 0.7f };



    @Override
    public void onSurfaceCreated(GL10 unused, EGLConfig config) {

        // Set the background frame color
        GLES20.glClearColor(0.0f, 0.0f, 0.0f, 1.0f);

        GLES20.glEnable(GLES20.GL_DEPTH_TEST);
        GLES20.glFrontFace(GLES20.GL_CCW);

        mTriangle1 = new Triangle(coordPodstawa1,kolorPodstawy);
        mSquare1   = new Square(kwadrat1,kolorPodstawy);
        mSquare2   = new Square(kwadrat2,kolor1);
        mSquare3   = new Square(kwadrat3,kolor2);
       mSquare4   = new Square(kwadrat4,kolor3);
       mSquare5   = new Square(kwadrat5,kolor4);
       mSquare6   = new Square(kwadrat6,kolor5);
        mTriangle2 = new Triangle(coordPodstawa2,kolorPodstawy);
        mTriangle3 = new Triangle(coord1,kolor1);
        mTriangle4 = new Triangle(coord2,kolor2);
        mTriangle5 = new Triangle(coord3,kolor3);
        mTriangle6 = new Triangle(coord4,kolor4);
    }

    @Override
    public void onDrawFrame(GL10 unused) {
        float[] scratch = new float[16];

        // Draw background color
        GLES20.glClear(GLES20.GL_COLOR_BUFFER_BIT | GLES20.GL_DEPTH_BUFFER_BIT);

        // Set the camera position (View matrix)
        Matrix.setLookAtM(mViewMatrix, 0, 0, 0, 5, 0f, 0f, 0f, 0f, 1.0f, 0.0f);

        // Calculate the projection and view transformation
        Matrix.multiplyMM(mMVPMatrix, 0, mProjectionMatrix, 0, mViewMatrix, 0);

        // Draw square


        // Create a rotation for the triangle

        // Use the following code to generate constant rotation.
        // Leave this code out when using TouchEvents.
        // long time = SystemClock.uptimeMillis() % 4000L;
        // float angle = 0.090f * ((int) time);

        Matrix.setRotateM(mRotationMatrix, 0, mAngle, 1.0f, 0.1f, 0.2f);

        // Combine the rotation matrix with the projection and camera view
        // Note that the mMVPMatrix factor *must be first* in order
        // for the matrix multiplication product to be correct.
        Matrix.multiplyMM(mMVPMatrix, 0, mMVPMatrix, 0, mRotationMatrix, 0);

        mSquare1.draw(mMVPMatrix);
        mSquare2.draw(mMVPMatrix);
        mSquare3.draw(mMVPMatrix);
      mSquare4.draw(mMVPMatrix);
      mSquare5.draw(mMVPMatrix);
      mSquare6.draw(mMVPMatrix);

        // Draw triangle
  //    mTriangle1.draw(scratch);
  //    mTriangle2.draw(scratch);
  //   mTriangle3.draw(scratch);
  //   mTriangle4.draw(scratch);
  //   mTriangle5.draw(scratch);
  //   mTriangle6.draw(scratch);

    }

    @Override
    public void onSurfaceChanged(GL10 unused, int width, int height) {
        // Adjust the viewport based on geometry changes,
        // such as screen rotation
        GLES20.glViewport(0, 0, width, height);

        float ratio = (float) width / height;

        // this projection matrix is applied to object coordinates
        // in the onDrawFrame() method
        Matrix.frustumM(mProjectionMatrix, 0, -ratio, ratio, -1, 1, 3, 7);

    }

    /**
     * Utility method for compiling a OpenGL shader.
     *
     * <p><strong>Note:</strong> When developing shaders, use the checkGlError()
     * method to debug shader coding errors.</p>
     *
     * @param type - Vertex or fragment shader type.
     * @param shaderCode - String containing the shader code.
     * @return - Returns an id for the shader.
     */
    public static int loadShader(int type, String shaderCode){

        // create a vertex shader type (GLES20.GL_VERTEX_SHADER)
        // or a fragment shader type (GLES20.GL_FRAGMENT_SHADER)
        int shader = GLES20.glCreateShader(type);

        // add the source code to the shader and compile it
        GLES20.glShaderSource(shader, shaderCode);
        GLES20.glCompileShader(shader);

        return shader;
    }

    /**
    * Utility method for debugging OpenGL calls. Provide the name of the call
    * just after making it:
    *
    * <pre>
    * mColorHandle = GLES20.glGetUniformLocation(mProgram, "vColor");
    * MyGLRenderer.checkGlError("glGetUniformLocation");</pre>
    *
    * If the operation is not successful, the check throws an error.
    *
    * @param glOperation - Name of the OpenGL call to check.
    */
    public static void checkGlError(String glOperation) {
        int error;
        while ((error = GLES20.glGetError()) != GLES20.GL_NO_ERROR) {
            Log.e(TAG, glOperation + ": glError " + error);
            throw new RuntimeException(glOperation + ": glError " + error);
        }
    }

    /**
     * Returns the rotation angle of the triangle shape (mTriangle).
     *
     * @return - A float representing the rotation angle.
     */
    public float getAngle() {
        return mAngle;
    }

    /**
     * Sets the rotation angle of the triangle shape (mTriangle).
     */
    public void setAngle(float angle) {
        mAngle = angle;
    }

}