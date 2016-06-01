/*
 * Copyright (c) 2009-2012 jMonkeyEngine
 * All rights reserved.
 *
 * Redistribution and use in source and binary forms, with or without
 * modification, are permitted provided that the following conditions are
 * met:
 *
 * * Redistributions of source code must retain the above copyright
 *   notice, this list of conditions and the following disclaimer.
 *
 * * Redistributions in binary form must reproduce the above copyright
 *   notice, this list of conditions and the following disclaimer in the
 *   documentation and/or other materials provided with the distribution.
 *
 * * Neither the name of 'jMonkeyEngine' nor the names of its contributors
 *   may be used to endorse or promote products derived from this software
 *   without specific prior written permission.
 *
 * THIS SOFTWARE IS PROVIDED BY THE COPYRIGHT HOLDERS AND CONTRIBUTORS
 * "AS IS" AND ANY EXPRESS OR IMPLIED WARRANTIES, INCLUDING, BUT NOT LIMITED
 * TO, THE IMPLIED WARRANTIES OF MERCHANTABILITY AND FITNESS FOR A PARTICULAR
 * PURPOSE ARE DISCLAIMED. IN NO EVENT SHALL THE COPYRIGHT OWNER OR
 * CONTRIBUTORS BE LIABLE FOR ANY DIRECT, INDIRECT, INCIDENTAL, SPECIAL,
 * EXEMPLARY, OR CONSEQUENTIAL DAMAGES (INCLUDING, BUT NOT LIMITED TO,
 * PROCUREMENT OF SUBSTITUTE GOODS OR SERVICES; LOSS OF USE, DATA, OR
 * PROFITS; OR BUSINESS INTERRUPTION) HOWEVER CAUSED AND ON ANY THEORY OF
 * LIABILITY, WHETHER IN CONTRACT, STRICT LIABILITY, OR TORT (INCLUDING
 * NEGLIGENCE OR OTHERWISE) ARISING IN ANY WAY OUT OF THE USE OF THIS
 * SOFTWARE, EVEN IF ADVISED OF THE POSSIBILITY OF SUCH DAMAGE.
 */

package jme3test.helloworld;
import com.jme3.animation.AnimChannel;
import com.jme3.animation.AnimControl;
import com.jme3.animation.Bone;
import com.jme3.animation.LoopMode;
import com.jme3.app.SimpleApplication;
import com.jme3.input.KeyInput;
import com.jme3.input.MouseInput;
import com.jme3.input.controls.KeyTrigger;
import com.jme3.input.controls.MouseButtonTrigger;
import com.jme3.light.DirectionalLight;
import com.jme3.material.Material;
import com.jme3.math.ColorRGBA;
import com.jme3.math.FastMath;
import com.jme3.math.Quaternion;
import com.jme3.math.Vector3f;
import com.jme3.scene.Node;
import com.jme3.scene.debug.SkeletonDebugger;
import com.jme3.input.controls.ActionListener;
import com.jme3.input.controls.AnalogListener;
import com.jme3.input.controls.KeyTrigger;
import com.jme3.input.controls.MouseButtonTrigger;
import com.jme3.scene.Geometry;
import com.jme3.input.controls.MouseAxisTrigger;

public class HelloDummy extends SimpleApplication {

    private AnimControl control;
    private float angle = 0;
    private float scale = 1;
    private float rate = 1;
    
    
    public static void main(String[] args) {
        HelloDummy app = new HelloDummy();
        app.start();
    }
    protected Geometry model;
    Boolean isRunning=true;

    @Override
    public void simpleInitApp() {
        flyCam.setMoveSpeed(10f);
        cam.setLocation(new Vector3f(6.4013605f, 7.488437f, 12.843031f));
        cam.setRotation(new Quaternion(-0.060740203f, 0.93925786f, -0.2398315f, -0.2378785f));

        DirectionalLight dl = new DirectionalLight();
        dl.setDirection(new Vector3f(-0.1f, -0.7f, -1).normalizeLocal());
        dl.setColor(new ColorRGBA(1f, 1f, 1f, 1.0f));
        rootNode.addLight(dl);

        Node model = (Node) assetManager.loadModel("Models/Template/Template.mesh.xml");

        control = model.getControl(AnimControl.class);

        //AnimChannel feet = control.createChannel();
        //AnimChannel leftHand = control.createChannel();
        //AnimChannel rightHand = control.createChannel();
        //flyCam.setEnabled(false); 
        //feet.addFromRootBone("Arm.Hand.IK_L");
        rootNode.attachChild(model);
        initKeys();
    }
    
    private void initKeys() {
    // You can map one or several inputs to one named action
    inputManager.addMapping("Pause",  new KeyTrigger(KeyInput.KEY_P));
    inputManager.addMapping("Left",   new KeyTrigger(KeyInput.KEY_J));
    inputManager.addMapping("Right",  new KeyTrigger(KeyInput.KEY_K));
    inputManager.addMapping("Rotate", new KeyTrigger(KeyInput.KEY_SPACE),
                                      new MouseButtonTrigger(MouseInput.BUTTON_LEFT));
    
    inputManager.addMapping("Scroll_negative", new MouseAxisTrigger(MouseInput.AXIS_WHEEL, false));
    inputManager.addMapping("Scroll", new MouseAxisTrigger(MouseInput.AXIS_WHEEL, true));
    inputManager.addMapping("RotateX", new MouseAxisTrigger(MouseInput.AXIS_X, true));
    inputManager.addMapping("RotateX_negative", new MouseAxisTrigger(MouseInput.AXIS_X, false));
    
    
    // Add the names to the action listener.
    inputManager.addListener(actionListener,"Pause");
    inputManager.addListener(analogListener,"Left", "Right", "Rotate","RotateX","RotateX_negative","Scroll","Scroll_negative");
    
    
    
 
  }
    private ActionListener actionListener = new ActionListener() {
        public void onAction(String name, boolean keyPressed, float tpf) {
          if (name.equals("Pause") && !keyPressed) {
            isRunning = !isRunning;
          }
        }
      };
    
    private AnalogListener analogListener = new AnalogListener() {
        public void onAnalog(String name, float value, float tpf) {
          if (isRunning) {
              
            
            
            if( "Scroll".equals(name) ) {
                
                Quaternion rotate = new Quaternion();
                //rotate.fromAngleAxis(tpf, Vector3f.UNIT_X);
                angle += rate * speed;
  
                
                rotate.fromAngles(angle,0,angle);
                
                //rotation.mulltLocal(rotate);
                
                
                Bone b = control.getSkeleton().getBone("Shoulder_R");
               
                //Bone b2 = control.getSkeleton().getBone("Arm.Lower_L");
                
                b.setUserControl(true);
                b.setUserTransforms(Vector3f.ZERO, rotate, Vector3f.UNIT_XYZ);
 

            }else if ("Scroll_negative".equals(name)) {
                Quaternion rotate = new Quaternion();
                //rotate.fromAngleAxis(tpf, Vector3f.UNIT_X);
                angle += -(rate * speed );
                rotate.fromAngles(0,angle,0);
                
                //rotation.mulltLocal(rotate);
                
                
                Bone b = control.getSkeleton().getBone("Shoulder_R");
                //Bone b2 = control.getSkeleton().getBone("Arm.Lower_L");
                    
                b.setUserControl(true);
                b.setUserTransforms(Vector3f.ZERO, rotate, Vector3f.UNIT_XYZ);
                
                
            }
            /*
            if (name.equals("Right")) {
              //model.rotate(0, value*speed, 0);
                Bone b = control.getSkeleton().getBone("Spine");
                Bone b2 = control.getSkeleton().getBone("Arm.Lower_L");

                angle += tpf * rate;        
                if (angle > FastMath.HALF_PI / 2f){
                    angle = FastMath.HALF_PI / 2f;
                    rate = -1;
                }else if (angle < -FastMath.HALF_PI / 2f){
                    angle = -FastMath.HALF_PI / 2f;
                    rate = 1;
                }

                Quaternion q = new Quaternion();
                q.fromAngles(0, angle, 0);

                b.setUserControl(true);
                b.setUserTransforms(Vector3f.ZERO, q, Vector3f.UNIT_XYZ);

                b2.setUserControl(true);
                b2.setUserTransforms(Vector3f.ZERO, Quaternion.IDENTITY, new Vector3f(1+angle,1+ angle, 1+angle));
            }
            
            if (name.equals("Right")) {
              Vector3f v = model.getLocalTranslation();
              model.setLocalTranslation(v.x + value*speed, v.y, v.z);
            }
            if (name.equals("Left")) {
              Vector3f v = model.getLocalTranslation();
              model.setLocalTranslation(v.x - value*speed, v.y, v.z);
            }
            */
          } else {
            System.out.println("Press P to unpause.");
          }
        }
      };

    
   

}