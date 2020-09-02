import bpy
import sys

folder = ""

for arg in sys.argv:
    if "build-folder" in arg:
        folder = arg.split("=")[1]

bpy.ops.export_scene.fbx(filepath=folder + '/map.fbx', axis_forward='-Z', axis_up='Y', global_scale=0.01, bake_space_transform=True)
