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

import com.sun.jna.Pointer;
import name.audet.samuel.javacv.jna.cv;
import name.audet.samuel.javacv.jna.cxcore;
import name.audet.samuel.javacv.jna.cxcore.CvAttrList;
import name.audet.samuel.javacv.jna.cxcore.CvFileNode;
import name.audet.samuel.javacv.jna.cxcore.CvFileStorage;
import name.audet.samuel.javacv.jna.cxcore.CvMat;
import name.audet.samuel.javacv.jna.cxcore.CvScalar;
import name.audet.samuel.javacv.jna.cxcore.CvSeq;
import name.audet.samuel.javacv.jna.cxcore.IplImage;

/**
 *
 * @author Samuel Audet
 */
public class ProjectiveDevice {
    public ProjectiveDevice(String name, String filename) {
        settings = new Settings();
        settings.name = name;
        readParameters(filename);
    }
    public ProjectiveDevice(String name, CvFileStorage fs) {
        settings = new Settings();
        settings.name = name;
        readParameters(fs);
    }
    public ProjectiveDevice(Settings settings) {
        setSettings(settings);
    }

    public static class Settings extends BaseSettings {
        public Settings() { }
        public Settings(ProjectiveDevice.Settings settings) {
            this.name = settings.name;
            this.initAspectRatio = settings.initAspectRatio;
            this.flags = settings.flags;
        }
        String name = "";
        double initAspectRatio = 1.0;
        int flags = cv.CV_CALIB_FIX_K3 | cv.CV_CALIB_FIX_INTRINSIC;

        @Override public String getName() {
            return name;
        }
        public void setName(String name) {
            pcs.firePropertyChange("name", this.name, this.name = name);
        }

        public double getInitAspectRatio() {
            return initAspectRatio;
        }
        public void setInitAspectRatio(double initAspectRatio) {
            this.initAspectRatio = initAspectRatio;
        }

        public boolean isUseIntrinsicGuess() {
            return (flags & cv.CV_CALIB_USE_INTRINSIC_GUESS) != 0;
        }
        public void setUseIntrinsicGuess(boolean useIntrinsicGuess) {
            if (useIntrinsicGuess) {
                flags |= cv.CV_CALIB_USE_INTRINSIC_GUESS;
            } else {
                flags &= ~cv.CV_CALIB_USE_INTRINSIC_GUESS;
            }
        }

        public boolean isFixAspectRatio() {
            return (flags & cv.CV_CALIB_FIX_ASPECT_RATIO) != 0;
        }
        public void setFixAspectRatio(boolean fixAspectRatio) {
            if (fixAspectRatio) {
                flags |= cv.CV_CALIB_FIX_ASPECT_RATIO;
            } else {
                flags &= ~cv.CV_CALIB_FIX_ASPECT_RATIO;
            }
        }

        public boolean isFixPrincipalPoint() {
            return (flags & cv.CV_CALIB_FIX_PRINCIPAL_POINT) != 0;
        }
        public void setFixPrincipalPoint(boolean fixPrincipalPoint) {
            if (fixPrincipalPoint) {
                flags |= cv.CV_CALIB_FIX_PRINCIPAL_POINT;
            } else {
                flags &= ~cv.CV_CALIB_FIX_PRINCIPAL_POINT;
            }
        }

        public boolean isZeroTangentDist() {
            return (flags & cv.CV_CALIB_ZERO_TANGENT_DIST) != 0;
        }
        public void setZeroTangentDist(boolean zeroTangentDist) {
            if (zeroTangentDist) {
                flags |= cv.CV_CALIB_ZERO_TANGENT_DIST;
            } else {
                flags &= ~cv.CV_CALIB_ZERO_TANGENT_DIST;
            }
        }

        public boolean isFixLocalLength() {
            return (flags & cv.CV_CALIB_FIX_FOCAL_LENGTH) != 0;
        }
        public void setFixLocalLength(boolean fixLocalLength) {
            if (fixLocalLength) {
                flags |= cv.CV_CALIB_FIX_FOCAL_LENGTH;
            } else {
                flags &= ~cv.CV_CALIB_FIX_FOCAL_LENGTH;
            }
        }

        public boolean isFixK1() {
            return (flags & cv.CV_CALIB_FIX_K1) != 0;
        }
        public void setFixK1(boolean fixK1) {
            if (fixK1) {
                flags |= cv.CV_CALIB_FIX_K1;
            } else {
                flags &= ~cv.CV_CALIB_FIX_K1;
            }
        }

        public boolean isFixK2() {
            return (flags & cv.CV_CALIB_FIX_K2) != 0;
        }
        public void setFixK2(boolean fixK2) {
            if (fixK2) {
                flags |= cv.CV_CALIB_FIX_K2;
            } else {
                flags &= ~cv.CV_CALIB_FIX_K2;
            }
        }

        public boolean isFixK3() {
            return (flags & cv.CV_CALIB_FIX_K3) != 0;
        }
        public void setFixK3(boolean fixK3) {
            if (fixK3) {
                flags |= cv.CV_CALIB_FIX_K3;
            } else {
                flags &= ~cv.CV_CALIB_FIX_K3;
            }
        }

        public boolean isStereoFixIntrinsic() {
            return (flags & cv.CV_CALIB_FIX_INTRINSIC) != 0;
        }
        public void setStereoFixIntrinsic(boolean stereoFixIntrinsic) {
            if (stereoFixIntrinsic) {
                flags |= cv.CV_CALIB_FIX_INTRINSIC;
            } else {
                flags &= ~cv.CV_CALIB_FIX_INTRINSIC;
            }
        }

        public boolean isStereoSameFocalLength() {
            return (flags & cv.CV_CALIB_SAME_FOCAL_LENGTH) != 0;
        }
        public void setStereoSameFocalLength(boolean stereoSameFocalLength) {
            if (stereoSameFocalLength) {
                flags |= cv.CV_CALIB_SAME_FOCAL_LENGTH;
            } else {
                flags &= ~cv.CV_CALIB_SAME_FOCAL_LENGTH;
            }
        }

    }

    private Settings settings;
    public Settings getSettings() {
        return settings;
    }
    public void setSettings(Settings settings) {
        this.settings = settings;
        if (settings.isFixK3()) {
            distortionCoeffs = CvMat.create(1, 4);
        } else {
            distortionCoeffs = CvMat.create(1, 5);
        }
    }

    public int imageWidth = 0, imageHeight = 0;

    public CvMat cameraMatrix = null, distortionCoeffs = null,
                 extrParams = null, reprojErrs = null;
    public double avgReprojErr;
    public double nominalDistance = 0;

    public CvMat R = null, T = null, E = null, F = null;
    public double avgEpipolarErr;

    public CvMat colorMixingMatrix = null, ambientLight = null;

    public boolean isCalibrated() {
        return cameraMatrix != null;
    }

    public double getNominalDistance(int objectWidth, int objectHeight) {
        double f = (cameraMatrix.get(0)+cameraMatrix.get(4))/(2*cameraMatrix.get(8));
        double imageSize = Math.sqrt(imageWidth *imageWidth +
                                     imageHeight*imageHeight);
        double objectSize = Math.sqrt(objectWidth *objectWidth +
                                      objectHeight*objectHeight);
        return f*objectSize/imageSize;
    }
    public double getNominalDistance(MarkedPlane board) {
        return getNominalDistance(board.getWidth(), board.getHeight());
    }

    //Compensates for radial and tangential distortion. Model From Oulu university.
    //Code ported from Camera Calibration Toolbox for Matlab by Jean-Yves Bouguet
    //http://www.vision.caltech.edu/bouguetj/calib_doc/
    //function name: comp_distortion_oulu()
    //
    //INPUT: xd: distorted (normalized) point coordinates in the image plane (2xN matrix)
    //       k: Distortion coefficients (radial and tangential) (4x1 vector)
    //
    //OUTPUT: x: undistorted (normalized) point coordinates in the image plane (2xN matrix)
    //
    //Method: Iterative method for compensation.
    //
    //NOTE: This compensation has to be done after the subtraction
    //      of the principal point, and division by the focal length.
    public static double[] undistort(double[] xd, double[] k) {
        double k1 = k[0];
        double k2 = k[1];
        double k3 = k.length > 4 ? k[4] : 0;
        double p1 = k[2];
        double p2 = k[3];

        double[] xu = xd.clone(); // initial guess

        for (int i = 0; i < xd.length/2; i++) {
            double x  = xu[i*2], y  = xu[i*2 + 1];
            double xo = xd[i*2], yo = xd[i*2 + 1];
            for (int j = 0; j < 20; j++) {
                double r_2 = x*x + y*y;
                double k_radial = 1 + k1*r_2 + k2*r_2*r_2 + k3*r_2*r_2*r_2;
                double delta_x = 2*p1*x*y         + p2*(r_2 + 2*x*x);
                double delta_y = p1*(r_2 + 2*y*y) + 2*p2*x*y;
                x = (xo - delta_x)/k_radial;
                y = (yo - delta_y)/k_radial;
            }
            xu[i*2] = x; xu[i*2 + 1] = y;
        }
        return xu;
    }
    public double[] undistort(double[] x) {
        double[] xn = normalize(x, cameraMatrix);
        double[] xu = undistort(xn, distortionCoeffs.get());
        return unnormalize(xu, cameraMatrix);
    }

    public static double[] distort(double[] xu, double[] k) {
        double k1 = k[0];
        double k2 = k[1];
        double k3 = k.length > 4 ? k[4] : 0;
        double p1 = k[2];
        double p2 = k[3];

        double[] xd = xu.clone();

        for (int i = 0; i < xu.length/2; i++) {
            double x = xu[i*2    ],
                   y = xu[i*2 + 1];
            double r_2 = x*x + y*y;
            double k_radial = 1 + k1*r_2 + k2*r_2*r_2 + k3*r_2*r_2*r_2;
            double delta_x = 2*p1*x*y         + p2*(r_2 + 2*x*x);
            double delta_y = p1*(r_2 + 2*y*y) + 2*p2*x*y;
            xd[i*2    ] = x*k_radial + delta_x;
            xd[i*2 + 1] = y*k_radial + delta_y;
        }
        return xd;
    }
    public double[] distort(double[] x) {
        double[] xn = normalize(x, cameraMatrix);
        double[] xd = distort(xn, distortionCoeffs.get());
        return unnormalize(xd, cameraMatrix);
    }

    public static double[] normalize(double[] xu, CvMat K) {
        double[] xn = xu.clone();

        double fx = K.get(0)/K.get(8);
        double fy = K.get(4)/K.get(8);
        double dx = K.get(2)/K.get(8);
        double dy = K.get(5)/K.get(8);
        double s  = K.get(1)/K.get(8);
        for (int i = 0; i < xu.length/2; i++) {
            xn[i*2    ] = (xu[i*2    ] - dx)/fx - s*(xu[i*2 + 1] + dy)/(fx*fy);
            xn[i*2 + 1] = (xu[i*2 + 1] - dy)/fy;
        }
        return xn;
    }
    public static double[] unnormalize(double[] xn, CvMat K) {
        double[] xu = xn.clone();

        double fx = K.get(0)/K.get(8);
        double fy = K.get(4)/K.get(8);
        double dx = K.get(2)/K.get(8);
        double dy = K.get(5)/K.get(8);
        double s  = K.get(1)/K.get(8);
        for (int i = 0; i < xn.length/2; i++) {
            xu[i*2    ] = fx*xn[i*2    ] + dx + s*xn[i*2 + 1];
            xu[i*2 + 1] = fy*xn[i*2 + 1] + dy;
        }
        return xu;
    }

    private IplImage mapx = null, mapy = null;
    public void undistort(IplImage src, IplImage dst) {
        //cv.cvUndistort2(src, dst, cameraMatrix, distortionCoeffs);
        if (mapx == null || mapy == null) {
            mapx = IplImage.create(imageWidth, imageHeight, cxcore.IPL_DEPTH_32F, 1);
            mapy = IplImage.create(imageWidth, imageHeight, cxcore.IPL_DEPTH_32F, 1);
            cv.cvInitUndistortMap(cameraMatrix, distortionCoeffs, mapx, mapy);
        }
        if (src != null && dst != null) {
            cv.cvRemap(src, dst, mapx, mapy, cv.CV_INTER_LINEAR |
                    cv.CV_WARP_FILL_OUTLIERS, CvScalar.cvScalarAll(0));
        }
    }

    // B = [ (-R^T t)*plane^T - plane^T*(-R^T t) I ] [ (K R)^-1  ]
    //                                               [  0  0  0  ]
    // where plane = [ n | d ]
    public CvMat getBackProjectionMatrix(CvMat n, double d) {
        CvMat B = CvMat.create(4, 3);
        CvMat temp = CvMat.create(3, 3);

        temp.cols = 1; temp.step /= 3;
        B.rows = 3;
        cxcore.cvGEMM(R, T, -1,    null, 0,  temp,  cxcore.CV_GEMM_A_T);
        cxcore.cvGEMM(temp, n, 1,  null, 0,  B,     cxcore.CV_GEMM_B_T);
        double a = cxcore.cvDotProduct(n, temp) + d;
        B.put(0, B.get(0) - a);
        B.put(4, B.get(4) - a);
        B.put(8, B.get(8) - a);
        B.rows = 4;
        temp.cols = 3; temp.step *= 3;

        B.put(9, n.get());

        cxcore.cvGEMM(cameraMatrix, R, 1, null, 0, temp, 0);
        cxcore.cvInvert(temp, temp, cxcore.CV_LU);

        cxcore.cvGEMM(B, temp, 1, null, 0, B, 0);
        cxcore.cvConvertScale(B, B, 1/B.get(11), 0);

        return B;
    }

    public static ProjectiveDevice[] readDevices(String filename) {
        CvFileStorage fs = CvFileStorage.open(filename, null, cxcore.CV_STORAGE_READ);

        CvFileNode cameraNode = cxcore.cvGetFileNodeByName(fs, null, "Cameras");
        cameraNode.data.setType(CvSeq.ByReference.class);
        cameraNode.data.read();
        cameraNode.data.seq.read();
        int cameraCount = cameraNode.data.seq.total;

        CvFileNode projectorNode = cxcore.cvGetFileNodeByName(fs, null, "Projectors");
        projectorNode.data.setType(CvSeq.ByReference.class);
        projectorNode.data.read();
        projectorNode.data.seq.read();
        int projectorCount = projectorNode.data.seq.total;

        ProjectiveDevice[] devices = new ProjectiveDevice[cameraCount+projectorCount];
        for (int i = 0; i < cameraCount; i++) {
            Pointer p = cxcore.cvGetSeqElem(cameraNode.data.seq, i);
            if (p == null) continue;
            String name = cxcore.cvReadString(new CvFileNode(p), null);
            devices[i] = new CameraDevice(name, fs);
        }
        for (int i = 0; i < projectorCount; i++) {
            Pointer p = cxcore.cvGetSeqElem(projectorNode.data.seq, i);
            if (p == null) continue;
            String name = cxcore.cvReadString(new CvFileNode(p), null);
            devices[cameraCount+i] = new ProjectorDevice(name, fs);
        }
        fs.release();

        return devices;
    }

    public static void writeDevices(String filename, ProjectiveDevice[] devices) {
        CvFileStorage fs = CvFileStorage.open(filename, null, cxcore.CV_STORAGE_WRITE);
        CvAttrList.ByValue a = new CvAttrList().byValue();

        cxcore.cvStartWriteStruct(fs, "Cameras", cxcore.CV_NODE_SEQ, null, a);
        for (ProjectiveDevice d : devices) {
            if (d instanceof CameraDevice) {
                cxcore.cvWriteString(fs, null, d.getSettings().getName(), 0);
            }
        }
        cxcore.cvEndWriteStruct(fs);

        cxcore.cvStartWriteStruct(fs, "Projectors", cxcore.CV_NODE_SEQ, null, a);
        for (ProjectiveDevice d : devices) {
            if (d instanceof ProjectorDevice) {
                cxcore.cvWriteString(fs, null, d.getSettings().getName(), 0);
            }
        }
        cxcore.cvEndWriteStruct(fs);

        for (ProjectiveDevice d : devices) {
            d.writeParameters(fs);
        }
        fs.release();
    }

    public void writeParameters(String filename) {
        CvFileStorage fs = cxcore.cvOpenFileStorage(filename, null, cxcore.CV_STORAGE_WRITE);
        writeParameters(fs);
        fs.release();
    }
    public void writeParameters(CvFileStorage fs) {
        CvAttrList.ByValue a = new CvAttrList().byValue();

        cxcore.cvStartWriteStruct(fs, settings.name, cxcore.CV_NODE_MAP, null, a);

        cxcore.cvWriteInt(fs, "imageWidth", imageWidth);
        cxcore.cvWriteInt(fs, "imageHeight", imageHeight);
        cxcore.cvWriteReal(fs, "initAspectRatio", settings.initAspectRatio);
        cxcore.cvWriteInt(fs, "flags", settings.flags);
        if (cameraMatrix != null)
            cxcore.cvWrite(fs, "cameraMatrix", cameraMatrix, a);
        if (distortionCoeffs != null)
            cxcore.cvWrite(fs, "distortionCoeffs", distortionCoeffs, a);
        if (extrParams != null)
            cxcore.cvWrite(fs, "extrParams", extrParams, a);
        if (reprojErrs != null)
            cxcore.cvWrite(fs, "reprojErrs", reprojErrs, a);
        cxcore.cvWriteReal(fs, "avgReprojErr", avgReprojErr);
        cxcore.cvWriteReal(fs, "nominalDistance", nominalDistance);
        if (R != null)
            cxcore.cvWrite(fs, "R", R, a);
        if (T != null)
            cxcore.cvWrite(fs, "T", T, a);
        if (E != null)
            cxcore.cvWrite(fs, "E", E, a);
        if (F != null)
            cxcore.cvWrite(fs, "F", F, a);
        cxcore.cvWriteReal(fs, "avgEpipolarErr", avgEpipolarErr);

        if (colorMixingMatrix != null)
            cxcore.cvWrite(fs, "colorMixingMatrix", colorMixingMatrix, a);
        if (ambientLight != null)
            cxcore.cvWrite(fs, "ambientLight", ambientLight, a);

        cxcore.cvEndWriteStruct(fs);
    }

    public void readParameters(String filename) {
        CvFileStorage fs = cxcore.cvOpenFileStorage(filename, null, cxcore.CV_STORAGE_READ);
        readParameters(fs);
        fs.release();
    }
    public void readParameters(CvFileStorage fs) {
        CvAttrList.ByValue a = new CvAttrList().byValue();

        CvFileNode fn = cxcore.cvGetFileNodeByName(fs, null, settings.name);

        imageWidth = fs.cvReadIntByName(fn, "imageWidth", imageWidth);
        imageHeight = fs.cvReadIntByName(fn, "imageHeight", imageHeight);
        settings.initAspectRatio = fs.cvReadRealByName(fn, "initAspectRatio", settings.initAspectRatio);
        settings.flags = fs.cvReadIntByName(fn, "flags", settings.flags);
        Pointer p = fs.cvReadByName(fn, "cameraMatrix", a);
        cameraMatrix = p == null ? null : new CvMat(p);
        p = fs.cvReadByName(fn, "distortionCoeffs", a);
        distortionCoeffs = p == null ? null : new CvMat(p);
        p = fs.cvReadByName(fn, "extrParams", a);
        extrParams = p == null ? null : new CvMat(p);
        p = fs.cvReadByName(fn, "reprojErrs", a);
        reprojErrs = p == null ? null : new CvMat(p);
        avgReprojErr = fs.cvReadRealByName(fn, "avgReprojErr", avgReprojErr);
        nominalDistance = fs.cvReadRealByName(fn, "nominalDistance", nominalDistance);
        p = fs.cvReadByName(fn, "R", a);
        R = p == null ? null : new CvMat(p);
        p = fs.cvReadByName(fn, "T", a);
        T = p == null ? null : new CvMat(p);
        p = fs.cvReadByName(fn, "E", a);
        E = p == null ? null : new CvMat(p);
        p = fs.cvReadByName(fn, "F", a);
        F = p == null ? null : new CvMat(p);
        avgEpipolarErr = fs.cvReadRealByName(fn, "avgEpipolarErr", avgEpipolarErr);

        p = fs.cvReadByName(fn, "colorMixingMatrix", a);
        colorMixingMatrix = p == null ? null : new CvMat(p);
        p = fs.cvReadByName(fn, "ambientLight", a);
        ambientLight = p == null ? null : new CvMat(p);
    }

    @Override public String toString() {
        String s =
        settings.getName() + " (" + imageWidth + " x " + imageHeight + ")\n";
        for (int i = 0; i < settings.getName().length(); i++) {
            s += "=";
        }
        s += "\n" +
        "Intrinsics\n" +
        "----------\n" +
        "camera matrix = " + (cameraMatrix == null ? "null" : cameraMatrix.toString(16)) + "\n" +
        "distortion coefficients = " + (distortionCoeffs == null ? "null" : distortionCoeffs) + "\n" +
        "reprojection RMSE (pixels) = " + (float)avgReprojErr + "\n\n" +

        "Extrinsics\n" +
        "----------\n" +
        "rotation = " + (R == null ? "null" : R.toString(11)) + "\n" +
        "translation = " + (T == null ? "null" : T.toString(14)) + "\n" +
        "epipolar RMSE (pixels) = " + (float)avgEpipolarErr;

        return s;
    }

}
