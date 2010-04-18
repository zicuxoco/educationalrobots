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
 *
 *
 * This file is based on information found in highgui.h of 
 * OpenCV 1.1pre1, which are covered by the following copyright notice:
 *
 *                        Intel License Agreement
 *                For Open Source Computer Vision Library
 *
 * Copyright (C) 2000, Intel Corporation, all rights reserved.
 * Third party copyrights are property of their respective owners.
 *
 * Redistribution and use in source and binary forms, with or without modification,
 * are permitted provided that the following conditions are met:
 *
 *   * Redistribution's of source code must retain the above copyright notice,
 *     this list of conditions and the following disclaimer.
 *
 *   * Redistribution's in binary form must reproduce the above copyright notice,
 *     this list of conditions and the following disclaimer in the documentation
 *     and/or other materials provided with the distribution.
 *
 *   * The name of Intel Corporation may not be used to endorse or promote products
 *     derived from this software without specific prior written permission.
 *
 * This software is provided by the copyright holders and contributors "as is" and
 * any express or implied warranties, including, but not limited to, the implied
 * warranties of merchantability and fitness for a particular purpose are disclaimed.
 * In no event shall the Intel Corporation or contributors be liable for any direct,
 * indirect, incidental, special, exemplary, or consequential damages
 * (including, but not limited to, procurement of substitute goods or services;
 * loss of use, data, or profits; or business interruption) however caused
 * and on any theory of liability, whether in contract, strict liability,
 * or tort (including negligence or otherwise) arising in any way out of
 * the use of this software, even if advised of the possibility of such damage.
 *
 */

package name.audet.samuel.javacv.jna;

import com.sun.jna.Native;
import com.sun.jna.NativeLibrary;
import com.sun.jna.Pointer;
import com.sun.jna.PointerType;
import name.audet.samuel.javacv.jna.cxcore.CvArr;
import name.audet.samuel.javacv.jna.cxcore.CvMat;
import name.audet.samuel.javacv.jna.cxcore.CvSize;
import name.audet.samuel.javacv.jna.cxcore.IplImage;

/**
 *
 * @author Samuel Audet
 */
public class highgui {
    // OpenCV sometimes does not install itself in the PATH :(
    public static final String[] paths = { "C:/Program Files/OpenCV/bin/",
                                           "C:/Program Files (x86)/OpenCV/bin/",
                                           "/usr/local/lib/",
                                           "/usr/local/lib64/" };
    public static final String[] libnames = { "highgui110", "highgui110_64", "highgui" };
    public static final String libname;
    static {
        int i = 0;
        for (; i < libnames.length; i++) {
            for (int j = 0; j < paths.length; j++) {
                NativeLibrary.addSearchPath(libnames[i], paths[j]);
            }
            try {
                Native.register(libnames[i]);
                break;
            } catch (LinkageError e) {
                if (i >= libnames.length-1)
                    throw e;
            }
        }
        libname = libnames[i];
    }

    public static final int
            CV_LOAD_IMAGE_UNCHANGED  = -1,
            CV_LOAD_IMAGE_GRAYSCALE  = 0,
            CV_LOAD_IMAGE_COLOR      = 1,
            CV_LOAD_IMAGE_ANYDEPTH   = 2,
            CV_LOAD_IMAGE_ANYCOLOR   = 4;
    public static native IplImage cvLoadImage(String filename, int iscolor/*=CV_LOAD_IMAGE_COLOR*/);
    public static native CvMat cvLoadImageM(String filename, int iscolor/*=CV_LOAD_IMAGE_COLOR*/);
    public static native int cvSaveImage(String filename, CvArr image);


    public static class CvCapture extends PointerType {
        public CvCapture() { }
        public CvCapture(Pointer p) { super(p); }

        public static class PointerByReference extends com.sun.jna.ptr.PointerByReference {
            public PointerByReference() { }
            public PointerByReference(CvCapture p) {
                setStructure(p);
            }
            public CvCapture getStructure() {
                return new CvCapture(getValue());
            }
            public void getStructure(CvCapture p) {
                p.setPointer(getValue());
            }
            public void setStructure(CvCapture p) {
                setValue(p.getPointer());
            }
        }
        public PointerByReference pointerByReference() {
            return new PointerByReference(this);
        }
    }
    public static native CvCapture cvCreateFileCapture(String filename);

    public static final int
            CV_CAP_ANY     = 0,

            CV_CAP_MIL     = 100,

            CV_CAP_VFW     = 200,
            CV_CAP_V4L     = 200,
            CV_CAP_V4L2    = 200,

            CV_CAP_FIREWARE= 300,
            CV_CAP_FIREWIRE= 300,
            CV_CAP_IEEE1394= 300,
            CV_CAP_DC1394  = 300,
            CV_CAP_CMU1394 = 300,

            CV_CAP_STEREO  = 400,
            CV_CAP_TYZX    = 400,
            CV_TYZX_LEFT   = 400,
            CV_TYZX_RIGHT  = 401,
            CV_TYZX_COLOR  = 402,
            CV_TYZX_Z      = 403,

            CV_CAP_QT      = 500,

            CV_CAP_UNICAP  = 600,

            CV_CAP_DSHOW   = 700;
    public static native CvCapture cvCreateCameraCapture(int index);
    public static native void cvReleaseCapture(CvCapture.PointerByReference capture);
    public static native int cvGrabFrame(CvCapture capture);
    public static native IplImage cvRetrieveFrame(CvCapture capture);
    public static native IplImage cvQueryFrame(CvCapture capture);

    public static final int
            CV_CAP_PROP_POS_MSEC      =  0,
            CV_CAP_PROP_POS_FRAMES    =  1,
            CV_CAP_PROP_POS_AVI_RATIO =  2,
            CV_CAP_PROP_FRAME_WIDTH   =  3,
            CV_CAP_PROP_FRAME_HEIGHT  =  4,
            CV_CAP_PROP_FPS           =  5,
            CV_CAP_PROP_FOURCC        =  6,
            CV_CAP_PROP_FRAME_COUNT   =  7,
            CV_CAP_PROP_FORMAT        =  8,
            CV_CAP_PROP_MODE          =  9,
            CV_CAP_PROP_BRIGHTNESS    = 10,
            CV_CAP_PROP_CONTRAST      = 11,
            CV_CAP_PROP_SATURATION    = 12,
            CV_CAP_PROP_HUE           = 13,
            CV_CAP_PROP_GAIN          = 14,
            CV_CAP_PROP_CONVERT_RGB   = 15;
    public static native double cvGetCaptureProperty(CvCapture capture, int property_id);
    public static native int    cvSetCaptureProperty(CvCapture capture, int property_id, double value);


    public static class CvVideoWriter extends PointerType {
        public CvVideoWriter() { }
        public CvVideoWriter(Pointer p) { super(p); }

        public static int CV_FOURCC(byte c1, byte c2, byte c3, byte c4) {
            return (((c1)&255) + (((c2)&255)<<8) + (((c3)&255)<<16) + (((c4)&255)<<24));
        }

        public static class PointerByReference extends com.sun.jna.ptr.PointerByReference {
            public PointerByReference() { }
            public PointerByReference(CvVideoWriter p) {
                setStructure(p);
            }
            public CvVideoWriter getStructure() {
                return new CvVideoWriter(getValue());
            }
            public void getStructure(CvVideoWriter p) {
                p.setPointer(getValue());
            }
            public void setStructure(CvVideoWriter p) {
                setValue(p.getPointer());
            }
        }
        public PointerByReference pointerByReference() {
            return new PointerByReference(this);
        }
    }
    public static final int
            CV_FOURCC_PROMPT  = -1,
            CV_FOURCC_DEFAULT = -1;
    public static native CvVideoWriter cvCreateVideoWriter(String filename, int fourcc/*=-1*/, double fps,
            CvSize.ByValue frame_size, int is_color/*=CV_LOAD_IMAGE_UNCHANGED*/);
    public static native void cvReleaseVideoWriter(CvVideoWriter.PointerByReference writer);
    public static native int cvWriteFrame(CvVideoWriter writer, IplImage image);


    public static final int
            CV_CVTIMG_FLIP     = 1,
            CV_CVTIMG_SWAP_RB  = 2 ;
    public static native void cvConvertImage(CvArr src, CvArr dst, int flags/*=0*/);
}
