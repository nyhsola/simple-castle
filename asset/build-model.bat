@echo off
call load-properties.bat

"%blender-path%\blender.exe" "blender-project\step4\map.blend" --background --python "python\blender-export-fbx.py" -- filename="blender-project\step4\map.fbx"
"fbx-converter/fbx-conv.exe" -f -o G3DJ "blender-project/step4/map.fbx"

"%blender-path%\blender.exe" "blender-project\step4\unit-warrior.blend" --background --python "python\blender-export-fbx.py" -- filename="blender-project\step4\unit-warrior.fbx"
"fbx-converter/fbx-conv.exe" -f -o G3DJ "blender-project/step4/unit-warrior.fbx"

"%blender-path%\blender.exe" "blender-project\step4\castle.blend" --background --python "python\blender-export-fbx.py" -- filename="blender-project\step4\castle.fbx"
"fbx-converter/fbx-conv.exe" -f -o G3DJ "blender-project/step4/castle.fbx"

cd "blender-project"
cd "step4"
move /y "map.g3dj" "../../../android/assets/assets3d/"
move /y "unit-warrior.g3dj" "../../../android/assets/assets3d/"
move /y "castle.g3dj" "../../../android/assets/assets3d/"
move /y "textures\*.*" "../../../android/assets/assets3d/"
cd ..