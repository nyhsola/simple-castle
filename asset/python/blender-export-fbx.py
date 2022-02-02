import bpy
import sys

filename = ""

for arg in sys.argv:
    if "filename" in arg:
        filename = arg.split("=")[1]

bpy.ops.export_scene.fbx(filepath=filename, axis_forward='-Z', axis_up='Y', global_scale=0.01, bake_space_transform=True, bake_anim_use_nla_strips=False)
bpy.ops.file.unpack_all(method='WRITE_LOCAL')