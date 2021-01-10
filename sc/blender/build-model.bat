@echo off
call load-properties.bat
"%blender-path%\blender.exe" "blender-project\map.blend" --background --python "python\blender-export-fbx.py" -- build-folder="blender-project"
"fbx-converter/fbx-conv.exe" -o G3DJ "blender-project/map.fbx"
cd "blender-project"
copy /y "map.g3dj" "../android/assets/models/"
cd ..