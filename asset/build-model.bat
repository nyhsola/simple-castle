@echo off
call load-properties.bat
"%blender-path%\blender.exe" "blender-project\step4\map.blend" --background --python "python\blender-export-fbx.py" -- build-folder="blender-project\step4"
"fbx-converter/fbx-conv.exe" -f -o G3DJ "blender-project/step4/map.fbx"
cd "blender-project"
cd "step4"
move /y "map.g3dj" "../../../android/assets/assets3d/"
move /y "textures\*.*" "../../../android/assets/assets3d/"
cd ..