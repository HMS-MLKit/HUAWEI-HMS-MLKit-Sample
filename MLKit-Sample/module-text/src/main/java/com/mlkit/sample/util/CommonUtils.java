/**
 * Copyright 2020. Huawei Technologies Co., Ltd. All rights reserved.
 *
 *    Licensed under the Apache License, Version 2.0 (the "License");
 *    you may not use this file except in compliance with the License.
 *    You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 *    Unless required by applicable law or agreed to in writing, software
 *    distributed under the License is distributed on an "AS IS" BASIS,
 *    WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *    See the License for the specific language governing permissions and
 *    limitations under the License.
 */

package com.mlkit.sample.util;

import android.content.Context;
import android.util.Log;

import java.nio.ByteBuffer;

public class CommonUtils {
    private static final String TAG = "CommonUtils";
    /**
     * Handle ByteBuffer
     *
     * @param src High resolution ByteBuffer
     * @param dst Low resolution ByteBuffer
     * @param srcWidth High resolution wide
     * @param srcHeight High resolution height
     * @param dstWidth Low resolution wide
     * @param dstHeight Low resolution height
     */
    public static void handleByteBuffer(ByteBuffer src, ByteBuffer dst,
                                        int srcWidth, int srcHeight, int dstWidth, int dstHeight) {
        int sw = srcWidth;
        int sh = srcHeight;
        int dw = dstWidth;
        int dh = dstHeight;
        int y, x;
        int srcy, srcx, src_index;
        int xrIntFloat_16 = (sw << 16) / dw + 1;
        int yrIntFloat_16 = (sh << 16) / dh + 1;

        int dst_uv = dh * dw;
        int src_uv = sh * sw;
        int dst_uv_yScanline = 0;
        int src_uv_yScanline = 0;
        int dst_y_slice = 0;
        int src_y_slice;
        int sp;
        int dp;

        for (y = 0; y < (dh & ~7); ++y) {
            srcy = (y * yrIntFloat_16) >> 16;
            src_y_slice = srcy * sw;

            if ((y & 1) == 0) {
                dst_uv_yScanline = dst_uv + (y / 2) * dw;
                src_uv_yScanline = src_uv + (srcy / 2) * sw;
            }

            for (x = 0; x < (dw & ~7); ++x) {
                srcx = (x * xrIntFloat_16) >> 16;
                try {
                    dst.put((x + dst_y_slice), src.get(src_y_slice + srcx));
                } catch (Exception e) {
                    Log.d(TAG, "nv12_Resize Exception1" + e.getMessage());
                }

                if ((y & 1) == 0) {
                    if ((x & 1) == 0) {
                        src_index = (srcx / 2) * 2;
                        sp = dst_uv_yScanline + x;
                        dp = src_uv_yScanline + src_index;
                        try {
                            dst.put(sp, src.get(dp));
                        } catch (Exception e) {
                            Log.d(TAG, "nv12_Resize Exception2" + e.getMessage());
                        }
                        ++sp;
                        ++dp;
                        try {
                            dst.put(sp, src.get(dp));
                        } catch (Exception e) {
                            Log.d(TAG, "nv12_Resize Exception3" + e.getMessage());
                        }
                    }
                }
            }
            dst_y_slice += dw;
        }
    }

    public static float dp2px(Context context, float dipValue) {
        return dipValue * context.getResources().getDisplayMetrics().density + 0.5f;
    }
}
