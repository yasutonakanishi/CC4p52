package net.unitedfield.cc;

import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.awt.image.BufferedImage;
import java.awt.image.DataBufferByte;
import java.nio.ByteBuffer;

import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.SwingUtilities;

import processing.core.PApplet;
import processing.video.Capture;

import com.jme3.math.ColorRGBA;
import com.jme3.math.Vector3f;
import com.jme3.post.SceneProcessor;
import com.jme3.renderer.Camera;
import com.jme3.renderer.RenderManager;
import com.jme3.renderer.Renderer;
import com.jme3.renderer.ViewPort;
import com.jme3.renderer.queue.RenderQueue;
import com.jme3.scene.Node;
import com.jme3.texture.FrameBuffer;
import com.jme3.texture.Image.Format;
import com.jme3.texture.Texture2D;
import com.jme3.util.BufferUtils;
import com.jme3.util.Screenshots;


public class CaptureJME3toP5 extends Capture implements SceneProcessor {
	/*
	 * based on TestRenderToMemory http://jmonkeyengine.googlecode.com/svn/trunk/engine/src/test/jme3test/post/TestRenderToMemory.java
	 */	
    private FrameBuffer offBuffer;
    private ViewPort offView;
    private Texture2D offTex;
    private Camera offCamera;
    
    private ImageDisplay display;
    private boolean showDisplay = false;
    //private static final int width = 400, height = 300;

    private final ByteBuffer cpuBuf = BufferUtils.createByteBuffer(width * height * 4);
    private final byte[] cpuArray = new byte[width * height * 4];
    private final BufferedImage image = new BufferedImage(width, height, BufferedImage.TYPE_4BYTE_ABGR);
	
    //
    private RenderManager renderManager;
    private Renderer renderer;
    private	Node rootNode;
		  
	public CaptureJME3toP5(PApplet parent, int requestWidth, int requestHeight, RenderManager renderManager, Renderer renderer, Node rootNode) {
		super(parent, requestWidth, requestHeight);
		//init(parent, requestWidth, requestHeight, null, 30); //	commented out for P5_2	
		
		this.parent = parent;
		this.renderManager = renderManager;
    	this.renderer = renderer;
    	this.rootNode = rootNode;
    	
    	setupOffscreenView();
    	if(showDisplay)
    		createDisplayFrame();    
    }
    
//	commented out for P5_2	
//	@Override
//	public void init(PApplet parent, int requestWidth, int requestHeight, String name, int frameRate){		
//		// intentionally override Capture.init() so that this class doesn't open QTSession.
//	 	super.init(requestWidth, requestHeight, ARGB);
//	}
	  protected void initGStreamer(PApplet parent, int rw, int rh, String src,
              String idName, Object idValue,
              String fps) {
		// intentionally override Capture.init() so that this class doesn't open GStreamer.  
		//do nothing because this class is Virtual Capture
		  ;		  
	  }
	 
    public void createDisplayFrame() {
        SwingUtilities.invokeLater(new Runnable(){
            public void run(){
                JFrame frame = new JFrame("CaptureJMEtoP5");
                display = new ImageDisplay();
                display.setPreferredSize(new Dimension(width, height));
                frame.getContentPane().add(display);
                frame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
                frame.addWindowListener(new WindowAdapter(){
                    public void windowClosed(WindowEvent e){
                        System.exit(0);
                    }
                });
                frame.pack();
                frame.setLocationRelativeTo(null);
                frame.setResizable(false);
                frame.setVisible(true);
                frame.setLocation(0,0);
            }
        });		
	}
    
    @Override
    public boolean available() {
    	return image != null;
    }
	@Override
    public void read(){
		int a,r,g,b;
		synchronized (image) {
			if(image == null){				
				for(int i=0; i<this.width;i++){
					for(int j=0; j<this.height; j++){
						a = 255;
						r = 0;
						b = 255;
						g = 0;						
						this.pixels[i*height+j] = (a<<24)|(r<<16)|(g<<8)|b;
					}
				}
			}else{//TYPE_4BYTE_ABGR to ARGB
		      int w = image.getWidth();
		      int h = image.getHeight();
		      int pixelsize = w*h;
		      
		      byte[] binary = ((DataBufferByte)(image.getRaster().getDataBuffer())).getData();
		      for(int i=0,j=0;i<pixelsize;i++) {
		          a = binary[j++]&0xff;
		          b = binary[j++]&0xff;
		          g = binary[j++]&0xff;
		          r = binary[j++]&0xff;
		          this.pixels[i] = (a<<24)|(r<<16)|(g<<8)|b;
		      }		      	
			}
		}
    }
	
	public void updateImageContents(){
        cpuBuf.clear();
        renderer.readFrameBuffer(offBuffer, cpuBuf);

        synchronized (image) {
            Screenshots.convertScreenShot(cpuBuf, image);    
        }
        if (display != null)
            display.repaint();
    }

    public void setupOffscreenView(){
        offCamera = new Camera(width, height);

        // create a pre-view. a view that is rendered before the main view
        offView = renderManager.createPreView("Offscreen View", offCamera);
        offView.setBackgroundColor(ColorRGBA.DarkGray);
        offView.setClearFlags(true, true, true);
        
        // this will let us know when the scene has been rendered to the 
        // frame buffer
        offView.addProcessor(this);

        // create offscreen framebuffer
        offBuffer = new FrameBuffer(width, height, 1);

        //setup framebuffer's cam
        //offCamera.setFrustumPerspective(45f, 1f, 1f, 1000f);
        //offCamera.setLocation(new Vector3f(0f, 0f, -5f));
        offCamera.setFrustumPerspective(45f, (float)width/height, 1f, 300f);
        offCamera.setLocation(new Vector3f(0f, 0f, 0f));
        offCamera.lookAt(new Vector3f(0f, 0f, 0f), Vector3f.UNIT_Y);

        //setup framebuffer's texture
//        offTex = new Texture2D(width, height, Format.RGBA8);

        //setup framebuffer to use renderbuffer
        // this is faster for gpu -> cpu copies
        offBuffer.setDepthBuffer(Format.Depth);
        offBuffer.setColorBuffer(Format.RGBA8);
//        offBuffer.setColorTexture(offTex);
        
        //set viewport to render to offscreen framebuffer
        offView.setOutputFrameBuffer(offBuffer);

        // setup framebuffer's scene
//        Box boxMesh = new Box(Vector3f.ZERO, 1,1,1);
//        Material material = assetManager.loadMaterial("Interface/Logo/Logo.j3m");
//        offBox = new Geometry("box", boxMesh);
//        offBox.setMaterial(material);

        // attach the scene to the viewport to be rendered
        //offView.attachScene(offBox);
        offView.attachScene(this.rootNode);        
    }

    
	@Override
	public void initialize(RenderManager rm, ViewPort vp) {
	}

	@Override
	public void reshape(ViewPort vp, int w, int h) {}

	@Override
	public boolean isInitialized() {
		return true;
	}

	@Override
	public void preFrame(float tpf) {}

	@Override
	public void postQueue(RenderQueue rq) {}

	/**
	 *  update the CPU image's contents after the scene has been rendered to the framebuffer.
	 */
	@Override
	public void postFrame(FrameBuffer out) {
		updateImageContents();		
	}

	@Override
	public void cleanup() {}

	public	Camera	getCamera(){
		return this.offCamera;
	}
	
	private class ImageDisplay extends JPanel {
        private long t;
        private long total;
        private int frames;
        private int fps;

        @Override
        public void paintComponent(Graphics gfx) {
            super.paintComponent(gfx);
            Graphics2D g2d = (Graphics2D) gfx;

            synchronized (image){
                g2d.drawImage(image, null, 0, 0);
            }
        }
    }
}
