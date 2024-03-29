Simple castle
=====
[![Discord Chat](https://img.shields.io/discord/976981768387309638?logo=discord&style=flat-square)](https://discord.gg/4B9Gg5eUU9)

-----
### [:joystick: Game](#Game) · [:zap: FAQ](#FAQ) · [:star: Support](#Support)


:joystick: Game
======
### :keyboard: Controls
ESC - Toggle show menu  
Arrow keys/WASD - Move your camera  
Mouse wheel - Move the camera away  
Double space tap - Move camera to castle

### :keyboard: Chat commands
debug - Enable/Disable debug view in top right corner  

### :desktop_computer: Demo
https://user-images.githubusercontent.com/20153735/185769167-4c127bd9-2916-4611-b6c1-95d6be4d49b5.mp4

:zap: FAQ
=====
### How to run
Use `gradlew :server:launcher:run` task to run application

### How to pack into jar
Use `gradlew :server:launcher:jar` task to pack application into jar

### How to dist with JRE 
Use `gradlew :server:launcher:distpackr` task to distribute  
Before running, edit jdk.properties to use proper JDK

### How to pack blender model into the game
**(No need to do this if you did not change model)**  
Use task `gradlew :asset:buildModel` to update model. It packs **.blend** files from step4 folder into the assets  
* Before usage update `asset/paths.properties` according your Blender app location and fbx-conv.exe  
* When saving project file in Blender, mark option File > External Data > Automatically Pack Into .blend.

:star: Support
======
[![Google Pay](https://img.shields.io/badge/G%20pay-2875E3?logo=googlepay&style=flat-square)](https://send.monobank.ua/jar/8DWtnAx9m8)

USDT (TRC20): THacRXtAhTb1n9j6j6tkhFWYnRxoojyUMp  
BTC: 1EoK1EWtkwJFfw1bEHF9fKN4TEYhCFTnGm  
BNB (BEP20): 0x788f1fc89c4d818ddbe072dd8b1a02c9b2e84521  
ETH (ERC20): 0x788f1fc89c4d818ddbe072dd8b1a02c9b2e84521  
DOGE: D5Ao9BniCQUkxEASUoVmhmhCGZGrMv5BXx  
DASH: XsppQGWURyYz3VE1RcXf3gZUGUUxm8dTFC

## :handshake: Contribute