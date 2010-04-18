/*
 * Copyright (C) 2009 Samuel Audet
 *
 * This file is part of JavaCV.
 *
 * JavaCV is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 2 of the License, or
 * (at your option) any later version.
 *
 * JavaCV is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with JavaCV.  If not, see <http://www.gnu.org/licenses/>.
 */

package name.audet.samuel.javacv;

import java.awt.DisplayMode;
import name.audet.samuel.javacv.jna.cxcore;
import name.audet.samuel.javacv.jna.cxcore.CvFileStorage;
import name.audet.samuel.javacv.jna.cxcore.CvMat;

/**
 *
 * @author Samuel Audet
 */
public class ProjectorDevice extends ProjectiveDevice {
    public ProjectorDevice(String name, String filename) {
        super(name, filename);
        this.settings = new Settings(super.getSettings());
        settings.setImageWidth(imageWidth);
        settings.setImageHeight(imageHeight);
    }
    public ProjectorDevice(String name, CvFileStorage fs) {
        super(name, fs);
        this.settings = new Settings(super.getSettings());
        settings.setImageWidth(imageWidth);
        settings.setImageHeight(imageHeight);
    }
    public ProjectorDevice(Settings settings) {
        super(settings);
        setSettings(settings);
    }

    public static class Settings extends ProjectiveDevice.Settings {
        public Settings() { }
        public Settings(ProjectiveDevice.Settings settings) {
            super(settings);
            if (settings instanceof Settings) {
                Settings s = (Settings)settings;
                this.screenNumber = s.screenNumber;
                this.latency = s.latency;
                this.brightnessBackground = s.brightnessBackground;
                this.brightnessForeground = s.brightnessForeground;
                this.imageWidth = s.imageWidth;
                this.imageHeight = s.imageHeight;
                this.bitDepth = s.bitDepth;
                this.refreshRate = s.refreshRate;
            }
        }

        int screenNumber = CanvasFrame.getScreenDevices().length > 1 ? 1 : 0;
        long latency = CanvasFrame.DEFAULT_LATENCY;
        double brightnessBackground = 0.25, brightnessForeground = 1.0;

        public int getScreenNumber() {
            return screenNumber;
        }
        public void setScreenNumber(int screenNumber) {
            DisplayMode d = CanvasFrame.getDisplayMode(screenNumber);
            String oldDescription = getDescription();
            pcs.firePropertyChange("screenNumber", this.screenNumber, this.screenNumber = screenNumber);
            pcs.firePropertyChange("description", getDescription(), oldDescription);
            pcs.firePropertyChange("imageWidth", imageWidth, imageWidth = d == null ? 0 : d.getWidth());
            pcs.firePropertyChange("imageHeight", imageHeight, imageHeight = d == null ? 0 : d.getHeight());
            pcs.firePropertyChange("bitDepth", bitDepth, bitDepth = d == null ? 0 : d.getBitDepth());
            pcs.firePropertyChange("refreshRate", refreshRate, refreshRate = d == null ? 0 : d.getRefreshRate());
        }

        public long getLatency() {
            return latency;
        }
        public void setLatency(long latency) {
            this.latency = latency;
        }

        public double getBrightnessBackground() {
            return brightnessBackground;
        }
        public void setBrightnessBackground(double brightnessBackground) {
            pcs.firePropertyChange("brightnessBackground", this.brightnessBackground,
                    this.brightnessBackground = brightnessBackground);
        }

        public double getBrightnessForeground() {
            return brightnessForeground;
        }
        public void setBrightnessForeground(double brightnessForeground) {
            pcs.firePropertyChange("brightnessForeground", this.brightnessForeground,
                    this.brightnessForeground = brightnessForeground);
        }

        public String getDescription() {
            String[] descriptions = null;
            descriptions = CanvasFrame.getScreenDescriptions();

            if (descriptions != null && screenNumber < descriptions.length) {
                return descriptions[screenNumber];
            } else {
                return "";
            }
        }

        int imageWidth = 0, imageHeight = 0, bitDepth = 0, refreshRate = 0;

        public int getImageWidth() {
            return imageWidth;
        }
        public void setImageWidth(int imageWidth) {
            pcs.firePropertyChange("imageWidth", this.imageWidth, this.imageWidth = imageWidth);
        }

        public int getImageHeight() {
            return imageHeight;
        }
        public void setImageHeight(int imageHeight) {
            pcs.firePropertyChange("imageHeight", this.imageHeight, this.imageHeight = imageHeight);
        }

        public int getBitDepth() {
            return bitDepth;
        }
        public void setBitDepth(int bitDepth) {
            this.bitDepth = bitDepth;
        }

        public int getRefreshRate() {
            return refreshRate;
        }
        public void setRefreshRate(int refreshRate) {
            this.refreshRate = refreshRate;
        }

    }

    private Settings settings;
    @Override public Settings getSettings() {
        return settings;
    }
    @Override public void setSettings(ProjectiveDevice.Settings settings) {
        super.setSettings(settings);
        this.settings = new Settings(settings);
        if (settings.name == null || settings.name.length() == 0) {
            settings.name = "Projector " + String.format("%2d", this.settings.screenNumber);
        }
    }

    public CanvasFrame createCanvasFrame() throws Exception {
        DisplayMode d = new DisplayMode(settings.imageWidth, settings.imageHeight,
                settings.bitDepth, settings.refreshRate);
        CanvasFrame c = new CanvasFrame(true, settings.name, settings.screenNumber, d);
        c.setLatency(settings.latency);
        return c;
    }

    public double getAttenuation(double x, double y, CvMat n, double d) {
        CvMat B = getBackProjectionMatrix(n, d);
        CvMat x2 = CvMat.create(3, 1);
        x2.put(x, y, 1);
        CvMat x3 = CvMat.create(4, 1);

        cxcore.cvGEMM(B, x2, 1, null, 0, x3, 0);

        // find the direction and the distance of that middle point to the
        // projector and use it to compute the expected overall attenuation
        //      cos(theta) * nominal_distance^2 / distance^2
        // we assume a perfectly Lambertian surface ... ugh ...
        // at a sufficient distance from the projector... ugh...
        cxcore.cvGEMM(R, T, -1, null, 0, x2, cxcore.CV_GEMM_A_T);
        x3.rows = 3;
        cxcore.cvAddWeighted(x3, 1/x3.get(3), x2, -1, 0, x2);
        double distance2 = cxcore.cvDotProduct(x2, x2);
        double distance = Math.sqrt(distance2);
        double cosangle = -Math.signum(d)*cxcore.cvDotProduct(x2, n)/
                (distance * Math.sqrt(cxcore.cvDotProduct(n, n)));
        double attenuation = cosangle/distance2;
//        System.out.println(distance + " " + cosangle + " " + attenuation);

        return attenuation;
    }

}
