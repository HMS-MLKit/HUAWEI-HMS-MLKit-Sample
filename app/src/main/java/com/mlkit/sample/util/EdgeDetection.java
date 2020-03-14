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

public class EdgeDetection {
    int label;
    int[][] array;
    boolean[][] isEdgeArray;

    public EdgeDetection() {
    }

    public EdgeDetection(int[][] array, boolean[][] isEdgeArray) {
        if (array != null) {
            this.array = array.clone();
        }
        if (isEdgeArray != null) {
            this.isEdgeArray = isEdgeArray.clone();
        }
    }

    public void setArray(int[][] array) {
        if (array != null) {
            this.array = array.clone();
        }
    }

    public int[][] getArray() {
        return this.array.clone();
    }

    public boolean[][] getIsEdgeArray() {
        return this.isEdgeArray.clone();
    }

    public void setIsEdgeArray(boolean[][] isEdgeArray) {
        if (isEdgeArray != null) {
            this.isEdgeArray = isEdgeArray.clone();
        }
    }

    public int getLabel() {
        return this.label;
    }

    public void setLabel(int label) {
        this.label = label;
    }

    /**
     * Detect all pixels and output the judgment result of each pixel.
     */
    public boolean[] detect() {
        for (int row = 0; row < this.array.length; row++) {
            for (int col = 0; col < this.array[0].length; col++) {
                this.isEdgeArray[row][col] = this.isEdge(row, col);
            }
        }
        return this.twoToOneArray();
    }

    /**
     * Whether it is the edge point of the specified segmentation type.
     * Condition 1: The current pixel is the category to be segmented.
     * Condition 2: There are pixels of other segmentation types around.
     */
    public boolean isEdge(int row, int col) {
        if (!this.isSelectedLabel(row, col)) {
            return false;
        }
        return this.isOthersSurrouding(row, col);
    }

    /**
     * Detection condition 1, detecting whether the current pixel is a segmentation type to be identified.
     */
    private boolean isSelectedLabel(int row, int col) {
        int currentLabel = this.array[row][col];
        return currentLabel == this.label;
    }

    /**
     * Detection condition 2, traverse the surrounding pixels to check if there are other segmentation types.
     */
    private boolean isOthersSurrouding(int row, int col) {
        int[] xInterval = {-1, -1, -1, 0, 0, 1, 1, 1};
        int[] yInterval = {-1, 0, 1, -1, 1, -1, 0, 1};
        for (int index = 0; index < xInterval.length; index++) {
            int currentX = row + xInterval[index];
            int currentY = col + yInterval[index];
            if (currentX >= 0 && currentX < this.array.length && currentY >= 0 && currentY < this.array[0].length) {
                if (!this.isSelectedLabel(currentX, currentY)) {
                    return true;
                }
            }
        }
        return false;
    }

    /**
     * Convert two-dimensional array to one-dimensional array.
     */
    private boolean[] twoToOneArray() {
        boolean[] result = new boolean[this.isEdgeArray.length * this.isEdgeArray[0].length];
        int index = 0;
        for (boolean[] tmp : this.isEdgeArray) {
            for (boolean value : tmp) {
                result[index++] = value;
            }
        }
        return result;
    }
}
