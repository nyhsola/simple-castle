@echo off
call load-properties.bat

"%blender-path%\blender.exe" "blender-project\step4\map.blend" --background --python "python\blender-export-fbx.py" -- filename="blender-project\step4\map.fbx"
"fbx-converter/fbx-conv.exe" -f -o G3DJ "blender-project/step4/map.fbx"

"%blender-path%\blender.exe" "blender-project\step4\unit-warrior-n.blend" --background --python "python\blender-export-fbx.py" -- filename="blender-project\step4\unit-warrior-n.fbx"
"fbx-converter/fbx-conv.exe" -f -o G3DJ "blender-project/step4/unit-warrior-n.fbx"

"%blender-path%\blender.exe" "blender-project\step4\pick.blend" --background --python "python\blender-export-fbx.py" -- filename="blender-project\step4\pick.fbx"
"fbx-converter/fbx-conv.exe" -f -o G3DJ "blender-project/step4/pick.fbx"


cd "blender-project"
cd "step4"
move /y "map.g3dj" "../../../android/assets/assets3d/"
move /y "unit-warrior-n.g3dj" "../../../android/assets/assets3d/"
move /y "pick.g3dj" "../../../android/assets/assets3d/"
move /y "textures\*.*" "../../../android/assets/assets3d/"
cd ..