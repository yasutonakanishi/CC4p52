**Readme for CityCompiler** 

>[Processing][processing] is an open source programming language and environment for people who want to create images, animations, and interactions.  

and,

>[jMonkeyEngine3 (jme3)][jmonkeyengine] is an open source game engine, written entirely in Java. 

CityCompiler contains classes that bridge [Processing][processing] and [jMonkeyEngine3 (jme3)][jmonkeyengine].

With using CityCompiler, jMonkeyEngine3 simulates how an interactive system developed using Processing and modeled using Google SketchUp or Blender for augmented spaces runs in a 3D virtual space.

Virtual I/O for Processing available in CityCompiler are as follows.
+ virtual display (flat, cylinder and sphere)
+ virtual projector (with shadow or without shadow)
+ virtual camera 
+ virtual sensor that wraps Arduino

src directory in this project contains following packages.
+ cc.arduino : Arduino/firmata library for Processing, which was modified for Processing2.0bX. http://firmata.org/
+ com.jme3.post : texture projection for jme3
+ net.unitedfield.cc : virtual I/O that bridge Processing and jMonkeyEngine3 (jme3)
+ net.unitedfield.cc.map : GoogleMap in virtual space
+ net.unitedfield.cc.util : utility classes
+ simulation.cc : basic examples of virtual simulation using virtual I/O and Processing
+ simulation.p5 : Processing codes for the virtual simulation
+ simulation.workshop : more examples for our workshop
+ test.cc : test and sample codes for virtual I/O
+ test.jme : sample codes of jme3
+ test.jmetest.bullet : sample codes of Bullet, a physics engine, in jme3
+ test.p5 : Processing codes for the test and sample codes.

CityCompiler_doc.pdf is the document of CityCompiler. It introduces how to install and run sample codes. However it is only available in Japanese, we hope that images would be helpful.

The current target version of Processing is 2.0b8 on 24/Feb/2013.

Copyright &copy; 2012 Yasuto Nakanishi
Licensed under the [BSD License][BSD]

[processing]: http://processing.org 
[jmonkeyEngine]: http://jmonkeyengine.com
[BSD]: http://opensource.org/licenses/bsd-license.php