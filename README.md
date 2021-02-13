Build Model
=====
Use task `:blender:buildModel` to update model. It packs **map.blend** model into the game (`sc\android\assets\models`).

### Notes:

* Task works only on Windows
* Before usage update `sc\blender\paths.properties` according your Blender app location
* When saving project file in Blender, mark option File > External Data > Automatically Pack Into .blend.
* To make this work you'll need to install VC 2015 Redistributable Package (See https://github.com/libgdx/fbx-conv)

Launch
======
Use `:sc:server:server-core:run` task to run application  

### Keyboard:  
ESC - Exit game  
Arrow keys/WASD - Move your camera  
Mouse wheel - Move the camera away

### Chat commands:  
debug-physic - Enable\Disable physic shape borders
debug-ui - Enable\Disable ui borders

Screenshots
===========
![screenshot-example](/demo/screenshot-1.jpg)