@echo off
call load-properties.bat
"%blender-path%\blender.exe" "blender-project\map.blend" --background --python "python\blender-export-fbx.py" -- build-folder="blender-project"
"fbx-converter/fbx-conv.exe" -f -o G3DJ "blender-project/map.fbx"
cd "blender-project"
move /y "map.g3dj" "../../android/assets/models/"
move /y "textures\*.*" "../../android/assets/models/"
cd ..