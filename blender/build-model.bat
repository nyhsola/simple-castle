@echo off
"fbx-converter/fbx-conv.exe" -o G3DJ "blender-project/map.fbx"
cd "blender-project"
copy /y "map.g3dj" "../../simple-castle-project/android/assets/models/"
cd ..