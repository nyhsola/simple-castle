package com.simple.castle.manager.empty;

import com.simple.castle.camera.CameraSettings;
import com.simple.castle.manager.GetCameraSettings;

public class EmptyGetCameraSettings implements GetCameraSettings {
    @Override
    public CameraSettings getSettings() {
        return new CameraSettings();
    }
}
