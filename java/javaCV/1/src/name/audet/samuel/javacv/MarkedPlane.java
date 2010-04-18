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

import java.awt.Dimension;
import java.nio.ByteBuffer;
import name.audet.samuel.javacv.jna.cv;
import name.audet.samuel.javacv.jna.cxcore;
import name.audet.samuel.javacv.jna.cxcore.CvMat;
import name.audet.samuel.javacv.jna.cxcore.CvPoint;
import name.audet.samuel.javacv.jna.cxcore.CvScalar;
import name.audet.samuel.javacv.jna.cxcore.IplImage;

/**
 *
 * @author Samuel Audet
 */
public class MarkedPlane {

    public MarkedPlane(Dimension size, Marker[] planeMarkers, double superScale) {
        this(size.width, size.height, planeMarkers, superScale);
    }
    public MarkedPlane(int width, int height, Marker[] planeMarkers, double superScale) {
        this(width, height, planeMarkers, false, CvScalar.BLACK, CvScalar.WHITE, superScale);
    }
    public MarkedPlane(Dimension size, Marker[] planeMarkers,
            boolean initPrewarp, CvScalar foregroundColor, CvScalar backgroundColor, double superScale) {
        this(size.width, size.height, planeMarkers, initPrewarp, foregroundColor, backgroundColor, superScale);
    }
    public MarkedPlane(int width, int height, Marker[] planeMarkers,
            boolean initPrewarp, CvScalar foregroundColor, CvScalar backgroundColor, double superScale) {
        this.planeMarkers = planeMarkers;
        this.foregroundColor = foregroundColor.byValue();
        this.backgroundColor = backgroundColor.byValue();

//        this.srcPts    = CvMat.create(planeMarkers.length*4, 2);
//        this.dstPts    = CvMat.create(planeMarkers.length*4, 2);

        this.prewarp = null;
//        this.totalWarp = CvMat.create(3, 3);
//        this.tempWarp = CvMat.create(3, 3);

        if (initPrewarp) {
            prewarp = CvMat.create(3, 3);
            double minx = Double.MAX_VALUE, miny = Double.MAX_VALUE,
                   maxx = Double.MIN_VALUE, maxy = Double.MIN_VALUE;
            for (Marker m : planeMarkers) {
                double[] c = m.corners;
                minx = Math.min(Math.min(Math.min(Math.min(minx, c[0]), c[2]), c[4]), c[6]);
                miny = Math.min(Math.min(Math.min(Math.min(miny, c[1]), c[3]), c[5]), c[7]);
                maxx = Math.max(Math.max(Math.max(Math.max(maxx, c[0]), c[2]), c[4]), c[6]);
                maxy = Math.max(Math.max(Math.max(Math.max(maxy, c[1]), c[3]), c[5]), c[7]);
            }
            double aspect = (maxx-minx)/(maxy-miny);
            if (aspect > (double)width/height) {
                double h = (double)width/aspect;
//                srcPtsBuf.position(0); srcPtsBuf.put(new double[] { minx, miny, maxx, miny, maxx, maxy, minx, maxy });
//                dstPtsBuf.position(0); dstPtsBuf.put(new double[] { 0, height-h, width, height-h, width, height, 0, height });
//                srcPts.height = dstPts.height = 4;
//                cv.cvFindHomography(srcPts, dstPts, preWarp);
                JavaCV.getPerspectiveTransform(
                        new double[] { minx, miny, maxx, miny, maxx, maxy, minx, maxy },
                        new double[] { 0, height-h, width, height-h, width, height, 0, height }, prewarp);
            } else {
                double w = height*aspect;
//                srcPtsBuf.position(0); srcPtsBuf.put(new double[] { minx, miny, maxx, miny, maxx, maxy, minx, maxy });
//                dstPtsBuf.position(0); dstPtsBuf.put(new double[] { 0, 0, w, 0, w, height, 0, height });
//                srcPts.height = dstPts.height = 4;
//                cv.cvFindHomography(srcPts, dstPts, preWarp);
                JavaCV.getPerspectiveTransform(
                        new double[] { minx, miny, maxx, miny, maxx, maxy, minx, maxy },
                        new double[] { 0, 0, w, 0, w, height, 0, height }, prewarp);
            }
        }

        if (width > 0 && height > 0) {
            planeImage = IplImage.create(width, height, cxcore.IPL_DEPTH_8U, 1);
            if (superScale == 1.0) {
                superPlaneImage = null;
            } else {
                superPlaneImage = IplImage.create((int)Math.ceil(width*superScale),
                        (int)Math.ceil(height*superScale), cxcore.IPL_DEPTH_8U, 1);
            }
            setPrewarp(prewarp);
        }
    }

    private Marker[] planeMarkers = null;
//    private CvPoint[] tempPts = CvPoint.createArray(4);
//    private CvMat srcPts, dstPts;
    private CvMat prewarp;//, totalWarp, tempWarp;

    private IplImage planeImage = null, superPlaneImage = null;
    private CvScalar.ByValue foregroundColor, backgroundColor;

    public CvScalar getForegroundColor() {
        return foregroundColor;
    }
    public void setForegroundColor(CvScalar foregroundColor) {
        this.foregroundColor = foregroundColor.byValue();
        setPrewarp(prewarp);
    }

    public CvScalar getBackgroundColor() {
        return backgroundColor;
    }
    public void setBackgroundColor(CvScalar backgroundColor) {
        this.backgroundColor = backgroundColor.byValue();
        setPrewarp(prewarp);
    }

    public Marker[] getPlaneMarkers() {
        return planeMarkers;
    }
    public void setColors(CvScalar foregroundColor, CvScalar backgroundColor) {
        this.foregroundColor = foregroundColor.byValue();
        this.backgroundColor = backgroundColor.byValue();
        setPrewarp(prewarp);
    }

    private void drawMarker(IplImage image, IplImage marker, CvMat H, double scale) {
        ByteBuffer mbuf = marker.getByteBuffer();
        CvMat srcPts    = CvMat.create(4, 1, cxcore.CV_64F, 2);
        CvMat dstPts    = CvMat.create(4, 1, cxcore.CV_64F, 2);
        CvPoint[] tempPts = CvPoint.createArray(4);

        for (int y = 0; y < marker.height; y++) {
            for (int x = 0; x < marker.width; x++) {
                if (mbuf.get(y*marker.width + x) == 0) {
                    srcPts.put(0, x  ); srcPts.put(1, y  );
                    srcPts.put(2, x+1); srcPts.put(3, y  );
                    srcPts.put(4, x+1); srcPts.put(5, y+1);
                    srcPts.put(6, x  ); srcPts.put(7, y+1);
                    //System.out.println("srcPts" + srcPts);
                    cxcore.cvPerspectiveTransform(srcPts, dstPts, H);
                    //System.out.println("dstPts" + dstPts);

                    double centerx = 0, centery = 0;
                    for (int i = 0; i < 4; i++) {
                      centerx += dstPts.get(i*2  );
                      centery += dstPts.get(i*2+1);
                    }
                    centerx /= 4;
                    centery /= 4;
                    for (int i = 0; i < 4; i++) {
                        double a = dstPts.get(i*2  );
                        double b = dstPts.get(i*2+1);
                        double dx = centerx - a;
                        double dy = centery - b;
                        dx = dx < 0 ? -1 : 0;
                        dy = dy < 0 ? -1 : 0;
                        tempPts[i].x = (int)Math.round((a*scale + dx) * (1<<16));
                        tempPts[i].y = (int)Math.round((b*scale + dy) * (1<<16));
                    }
                    cxcore.cvFillConvexPoly(image, tempPts, 4, foregroundColor, 8/*cxcore.CV_AA*/, 16);
                }
            }
        }
    }

    public CvMat getPrewarp() {
        return prewarp;
    }
    public void setPrewarp(CvMat prewarp) {
        this.prewarp = prewarp;
        CvMat tempWarp = CvMat.create(3, 3);
        if (superPlaneImage == null) {
            cxcore.cvSet(planeImage, backgroundColor);
        } else {
            cxcore.cvSet(superPlaneImage, backgroundColor);
        }
        double[] src = { 0, 0, 8, 0, 8, 8, 0, 8 };
        for (int i = 0; i < planeMarkers.length; i++) {
            JavaCV.getPerspectiveTransform(src, planeMarkers[i].corners, tempWarp);
            if (prewarp != null) {
                cxcore.cvGEMM(prewarp, tempWarp, 1, null, 0, tempWarp, 0);
            }
            if (superPlaneImage == null) {
                drawMarker(planeImage, planeMarkers[i].getImage(), tempWarp, 1.0);
            } else {
                drawMarker(superPlaneImage, planeMarkers[i].getImage(), tempWarp,
                        (double)superPlaneImage.width/planeImage.width);
            }
        }
        if (superPlaneImage != null) {
            cv.cvResize(superPlaneImage, planeImage, cv.CV_INTER_AREA);
        }
        //highgui.INSTANCE.cvSaveImage("planeImage.png", planeImage);
    }

    public IplImage getImage() {
        return planeImage;
    }
    public int getWidth() {
        return planeImage.width;
    }
    public int getHeight() {
        return planeImage.height;
    }

    public double getTotalWarp(Marker[] imagedMarkers, CvMat totalWarp) {
        double rmse = Double.POSITIVE_INFINITY;

        CvMat tempWarp  = CvMat.create(3, 3);
        CvMat srcPts    = CvMat.create(planeMarkers.length*4, 2);
        CvMat dstPts    = CvMat.create(planeMarkers.length*4, 2);

        int numPoints = 0;
        for (Marker m1 : planeMarkers) {
            for (Marker m2 : imagedMarkers) {
                if (m1.id == m2.id) {
                    // I got a SIGSEGV here.. why??
                    srcPts.put(numPoints*2, m1.corners);
                    dstPts.put(numPoints*2, m2.corners);
                    numPoints += 4;
                    break;
                }
            }
        }

        if (numPoints > 4) {
            // compute homography ... should we use a robust method?
            srcPts.rows = dstPts.rows = numPoints;
            cv.cvFindHomography(srcPts, dstPts, totalWarp);

            // compute transformed source<->dest RMSE
            srcPts.cols = 1; srcPts.setType(cxcore.CV_64F, 2);
            dstPts.cols = 1; dstPts.setType(cxcore.CV_64F, 2);
            cxcore.cvPerspectiveTransform(srcPts, srcPts, totalWarp);
            srcPts.cols = 2; srcPts.setType(cxcore.CV_64F, 1);
            dstPts.cols = 2; dstPts.setType(cxcore.CV_64F, 1);

            rmse = 0;
            for (int i = 0; i < numPoints; i++) {
                double dx = dstPts.get(i*2  )-srcPts.get(i*2  );
                double dy = dstPts.get(i*2+1)-srcPts.get(i*2+1);
                rmse += dx*dx+dy*dy;
            }
            rmse = Math.sqrt(rmse/numPoints);
//            System.out.println(rmse);

            if (prewarp != null) {
                // remove pre-warp from total warp
                cxcore.cvInvert(prewarp, tempWarp);
                cxcore.cvMatMul(totalWarp, tempWarp, totalWarp);
            }
//            System.out.println("totalWarp:\n" + totalWarp);
        }
        return rmse;
    }

}
