Setup
=====
Update `sc\blender\paths.properties` according your Blender app location. Use task `:blender:buildModel` to update
model. When saving project file, use File > External Data > Automatically Pack Into .blend option

Build
=====
Currently, `:blender:buildModel` task works only on Windows. To make this work you'll need to install VC 2015
Redistributable Package (For making fbx-conv work - https://github.com/libgdx/fbx-conv)

Launch
======
Use `:server:run` task to run application

ESC - Exit game  
F1 - Enable/Disable debug mode  
Arrow keys/WASD - Move your camera  
Mouse wheel - Move the camera away

Screenshots
===========
![screenshot-example](/demo/screenshot-1.png)