package civ.core;

import org.lwjgl.*;
import org.lwjgl.glfw.*;
import org.lwjgl.opengl.*;
import org.lwjgl.system.*;
import civ.core.timing.Timer;
import java.nio.*;

import static org.lwjgl.glfw.Callbacks.*;
import static org.lwjgl.glfw.GLFW.*;
import static org.lwjgl.opengl.GL11.*;
import static org.lwjgl.system.MemoryStack.*;
import static org.lwjgl.system.MemoryUtil.*;

public class Civilization {
  private final String TITLE = "Civilization";
  private final double TARGET_UPS = 30D;
  
  private long window; // The window handle
  boolean resizable = true; // Whether or not the window is resizable

  public void run() {
    System.out.println("LWJGL " + Version.getVersion() + "");

    init();
    loop();

    // Free the window callbacks and destroy the window
    glfwFreeCallbacks(window);
    glfwDestroyWindow(window);

    // Terminate GLFW and free the error callback
    glfwTerminate();
    glfwSetErrorCallback(null).free();
  }

  private void init() {
    // Setup an error callback. The default implementation
    // will print the error message in System.err.
    GLFWErrorCallback.createPrint(System.err).set();

    // Initialize GLFW. Most GLFW functions will not work before doing this.
    if (!glfwInit())
      throw new IllegalStateException("Unable to initialize GLFW");

    // Configure GLFW
    glfwDefaultWindowHints(); // optional, the current window hints are already the default
    
    //When we are running on macOS, we need specific settings
    String lcOSName = System.getProperty("os.name");
    boolean IS_MAC = lcOSName.contains("OS X");
    if (IS_MAC) {
      glfwWindowHint(GLFW_CONTEXT_VERSION_MAJOR, 3);
      glfwWindowHint(GLFW_CONTEXT_VERSION_MINOR, 2);
      glfwWindowHint(GLFW_OPENGL_PROFILE, GLFW_OPENGL_CORE_PROFILE);
      glfwWindowHint(GLFW_OPENGL_FORWARD_COMPAT, GL_TRUE);
    }
    
    glfwWindowHint(GLFW_VISIBLE, GLFW_FALSE); // the window will stay hidden after creation
    glfwWindowHint(GLFW_RESIZABLE, resizable ? GLFW_TRUE : GLFW_FALSE); // the window will be resizable

    // Create the window
    window = glfwCreateWindow(300, 300, TITLE, NULL, NULL);
    if (window == NULL)
      throw new RuntimeException("Failed to create the GLFW window");


    // Setup a key callback. It will be called every time a key is pressed, repeated or released.
    glfwSetKeyCallback(window, (window, key, scancode, action, mods) -> {
      if (key == GLFW_KEY_ESCAPE && action == GLFW_RELEASE)
        glfwSetWindowShouldClose(window, true); // We will detect this in the rendering loop
    });

    // Get the thread stack and push a new frame
    try (MemoryStack stack = stackPush()) {
      IntBuffer pWidth = stack.mallocInt(1); // int*
      IntBuffer pHeight = stack.mallocInt(1); // int*

      // Get the window size passed to glfwCreateWindow
      glfwGetWindowSize(window, pWidth, pHeight);

      // Get the resolution of the primary monitor
      GLFWVidMode vidmode = glfwGetVideoMode(glfwGetPrimaryMonitor());

      // Center the window
      glfwSetWindowPos(window, (vidmode.width() - pWidth.get(0)) / 2,
          (vidmode.height() - pHeight.get(0)) / 2);
    } // the stack frame is popped automatically

    // Make the OpenGL context current
    glfwMakeContextCurrent(window);
    // Enable v-sync
    glfwSwapInterval(1);

    // Make the window visible
    glfwShowWindow(window);
    
    // This line is critical for LWJGL's interoperation with GLFW's
    // OpenGL context, or any context that is managed externally.
    // LWJGL detects the context that is current in the current thread,
    // creates the GLCapabilities instance and makes the OpenGL
    // bindings available for use.
    GL.createCapabilities();

    // Set the clear color to light gray
    glClearColor(0.25f, 0.25f, 0.25f, 0.0f);
  }

  private void loop() {    
    Timer t = new Timer();
    t.init();
    
    double delta;
    double accumulator = 0D;
    double interval = 1D / TARGET_UPS;
    
    // Run the rendering loop until the user has attempted to close
    // the window or has pressed the ESCAPE key.
    while (!glfwWindowShouldClose(window)) {
      // Poll for window events. The key callback above will only be
      // invoked during this call.
      glfwPollEvents();
      
      delta = t.getDelta();
      accumulator += delta;
      
      //input();
      
      while (accumulator >= interval) {
        update();
        t.incrementUPS();
        accumulator -= interval;
      }
      
      render();
      t.incrementFPS();
      t.update();
     
      glfwSwapBuffers(window); // swap the color buffers
    }
  }
  
  private void render() {
    glClear(GL_COLOR_BUFFER_BIT | GL_DEPTH_BUFFER_BIT); // clear the framebuffer
    
    //glBegin(GL_POLYGON);
    //for(int i = 0; i < 6; ++i) {
    //    glVertex2d(Math.sin(i/6.0*2*Math.PI),
    //               Math.cos(i/6.0*2*Math.PI));
   // }
    //glEnd();
    
  }
  
  private void update() {
    
  }

  public static void main(String[] args) {
    new Civilization().run();
  }

}
