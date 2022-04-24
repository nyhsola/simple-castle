How to run
======
Use `gradlew :server:launcher:run` task to run application

How to pack into jar
=====
Use `gradlew :server:launcher:jar` task to pack application into jar

How to pack blender model into the game
======
### (No need to do this if you did not change model)
Use task `gradlew :asset:buildModel` to update model. It packs **.blend** files from step4 folder into the assets

### Notes:

* Before usage update `asset/paths.properties` according your Blender app location and fbx-conv.exe
* When saving project file in Blender, mark option File > External Data > Automatically Pack Into .blend.

Other:
======

### Controls:

ESC - Exit game  
Arrow keys/WASD - Move your camera  
Mouse wheel - Move the camera away

### Chat commands:

debug - Enable/Disable debug view in top right corner  

### Demo

https://user-images.githubusercontent.com/20153735/163736200-0d822612-a710-4bef-aaa7-f45bd3b45ad7.mp4
