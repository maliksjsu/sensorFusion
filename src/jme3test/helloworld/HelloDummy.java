

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
import java.lang.*;

public class HelloDummy extends SimpleApplication {

    private AnimControl control;
    private AnimControl suit;
    private float angle = 0;
    private float scale = 1;
    private float rate = 1;
    public static Float[][] CSVArray;
    public int counter = 1;
    public boolean start = false;
    public int row = 1;
    
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

        Node model = (Node) assetManager.loadModel("Models/Desktop_Body/Desktop_Body.mesh.j3o");
        //model.attachChild(assetManager.loadModel("Models/Desktop_malesuit01HRes/Desktop_malesuit01HRes.mesh.j3o"));
        model.attachChild(assetManager.loadModel("Models/Desktop_classicshoes_hres/Desktop_classicshoes_hres.mesh.j3o"));
        model.attachChild(assetManager.loadModel("Models/Desktop_HighPolyEyes/Desktop_HighPolyEyes.mesh.j3o"));
        model.attachChild(assetManager.loadModel("Models/Desktop_mhair01/Desktop_mhair01.mesh.j3o"));
        Node suit1 = (Node) assetManager.loadModel("Models/Desktop_malesuit01HRes/Desktop_malesuit01HRes.mesh.j3o");
        
        suit = suit1.getControl(AnimControl.class);
        control = model.getControl(AnimControl.class);

        //AnimChannel feet = control.createChannel();
        //AnimChannel leftHand = control.createChannel();
        //AnimChannel rightHand = control.createChannel();
        //flyCam.setEnabled(false); 
        //feet.addFromRootBone("Arm.Hand.IK_L");
        rootNode.attachChild(model);
        rootNode.attachChild(suit1);
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
              
                if( "Left".equals(name)){
                    CSV_Input FeedIn = new CSV_Input();         //Construct CSV_Input
                    CSVArray = new Float[2000][4];              //Set up local array
                    CSVArray = FeedIn.SetUpCSVArray();          //Get array from the CSV input function
                    start = true;

                }
                if( "Rotate".equals(name)){ //Feeds in from file if "J" is pressed
                
                    Quaternion FromFile = new Quaternion();                 //Quaternion initalized at (0,0,0,1) (x,y,z,w)
                    Bone b = control.getSkeleton().getBone("hand.L");   //Designate bone as left shoulder
                    //Bone b1 = suit.getSkeleton().getBone("lowerarm.R");
                    b.setUserControl(true);                                 //Allow user input
                    //b.
                    
                   
                   if (row<1997)                                            //Limit rows to available 
                   {
                        if (start == true)
                        {
                            System.out.println("Visualizing...");
                            FromFile.set(CSVArray[row][3],CSVArray[row][0], CSVArray[row][1], CSVArray[row][2]); //CSV Input is (w,x,y,z) must feed into JM3 as (x,y,z,w)
                            
                            b.setUserTransforms(Vector3f.ZERO , FromFile.opposite(), Vector3f.UNIT_XYZ ); //Set user input to FromFile Quaternion
                        
                            //pause 0.1 sec to mimic the 10 Hz sample rate
                            try{
                            Thread.sleep(10);                       //Currently sped up to show better demoing
                            } catch(InterruptedException ex){       //Interrupt exception caught
                                Thread.currentThread().interrupt();
                            }
                        
                            row++;                          //Iterate row
                            System.out.println("Row: "+row);
                        }
                        else
                        {
                            System.out.println("Must load data first.");    //Check to see if data loaded. Otherwise will return nullpointer exception
                        }
                   }
                   else
                   {
                       System.out.println("Done");
                   }
                      
                } 

            if( "Scroll".equals(name) ) {
                
                Quaternion rotate = new Quaternion();
                //rotate.fromAngleAxis(tpf, Vector3f.UNIT_X);
                angle += rate * speed;
  
                
                rotate.fromAngles(angle,0,angle);
                
                //rotation.mulltLocal(rotate);
                
                
                Bone b = control.getSkeleton().getBone("deltoid.L");
                Bone b1 = suit.getSkeleton().getBone("deltoid.L");
               
                //Bone b2 = control.getSkeleton().getBone("Arm.Lower_L");
                
                b.setUserControl(true);
                b1.setUserControl(true);
                b.setUserTransforms(Vector3f.ZERO, rotate, Vector3f.UNIT_XYZ);
                b1.setUserTransforms(Vector3f.ZERO, rotate, Vector3f.UNIT_XYZ);
 

            }else if ("Scroll_negative".equals(name)) {
                Quaternion rotate = new Quaternion();
                //rotate.fromAngleAxis(tpf, Vector3f.UNIT_X);
                angle += -(rate * speed );
                rotate.fromAngles(0,angle,0);
                
                //rotation.mulltLocal(rotate);
                
                
                Bone b = control.getSkeleton().getBone("deltoid.L");
                Bone b1 = suit.getSkeleton().getBone("deltoid.L");
                //Bone b2 = control.getSkeleton().getBone("Arm.Lower_L");
                    
                b.setUserControl(true);
                b1.setUserControl(true);
                b.setUserTransforms(Vector3f.ZERO, rotate, Vector3f.UNIT_XYZ);
                b1.setUserTransforms(Vector3f.ZERO, rotate, Vector3f.UNIT_XYZ);
                
                
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
    
   
