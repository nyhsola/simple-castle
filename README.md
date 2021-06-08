How to run
======
Use `gradlew :server:launcher:run` task to run application

How to pack blender model into the game
======
### (No need to do this if you did not change model)
Use task `gradlew :blender:buildModel` to update model. It packs **map.blend** model into the game.

### Notes:

* Task works only on Windows
* Before usage update `blender/paths.properties` according your Blender app location
* When saving project file in Blender, mark option File > External Data > Automatically Pack Into .blend.
* To make this work you'll need to install VC 2015 Redistributable Package (See https://github.com/libgdx/fbx-conv)

Other:
======

### Controls:

ESC - Exit game  
Arrow keys/WASD - Move your camera  
Mouse wheel - Move the camera away

### Chat commands:

debug-physic - Enable/Disable physic shape borders  
debug-ui - Enable/Disable ui borders

### Demo

![gif-example](/demo/gif-1.gif)