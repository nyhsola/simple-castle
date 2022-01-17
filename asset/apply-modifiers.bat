@echo off
call load-properties.bat

cd "blender-project"
cd "step1"
copy /y "unit-s1.blend" "../step2/unit-s2.blend"

cd "../../"
"%blender-path%\blender.exe" "blender-project\step2\unit-s2.blend" --background --python "python\apply-modifiers.py" -- build-folder="blender-project\step3"
