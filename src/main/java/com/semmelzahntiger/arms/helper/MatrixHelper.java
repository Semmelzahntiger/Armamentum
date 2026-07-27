package com.semmelzahntiger.arms.helper;

import org.joml.Matrix3f;

public class MatrixHelper {

    public static Matrix3f getRotationMatrix(int xDegrees, int yDegrees, int zDegrees) {
        float xRad = (float) Math.toRadians(xDegrees);
        float yRad = (float) Math.toRadians(yDegrees);
        float zRad = (float) Math.toRadians(zDegrees);

        return new Matrix3f().rotationXYZ(xRad, yRad, zRad);
    }

}
