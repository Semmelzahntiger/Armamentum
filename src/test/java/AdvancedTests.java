import com.semmelzahntiger.arms.common.skeletons.TestSkeleton;
import com.semmelzahntiger.arms.core.obb.BoneId;
import com.semmelzahntiger.arms.core.obb.Pose;
import com.semmelzahntiger.arms.helper.MatrixHelper;
import org.joml.Matrix3f;
import org.junit.Test;
import net.minecraft.world.phys.Vec3;

import static org.junit.Assert.assertEquals;

public class AdvancedTests {

    private static final float EPS = 1e-4f;

    private static final BoneId MAIN      = new BoneId("TestMain");
    private static final BoneId CHILD     = new BoneId("Child");
    private static final BoneId CHILD_ROT = new BoneId("ChildRotated");


    private static void assertMatrixEquals(Matrix3f expected, Matrix3f actual, String msg) {
        for (int c = 0; c < 3; c++) {
            for (int r = 0; r < 3; r++) {
                assertEquals(msg + " mismatch at col " + c + " row " + r,
                        expected.get(c, r), actual.get(c, r), EPS);
            }
        }
    }

    private static void assertVecEquals(Vec3 expected, Vec3 actual, String msg) {
        assertEquals(msg + " x", expected.x, actual.x, EPS);
        assertEquals(msg + " y", expected.y, actual.y, EPS);
        assertEquals(msg + " z", expected.z, actual.z, EPS);
    }

    private static Pose freshPose() {
        return new Pose(TestSkeleton.TEST_SKELETON);
    }

    @Test
    public void yRotationPropagatesToDescendants() {
        Pose pose = freshPose();

        Matrix3f main30 = MatrixHelper.getRotationMatrix(0, 30, 0);
        pose.setRotation(MAIN, main30);
        pose.setRotation(CHILD, new Matrix3f());                              // identity
        pose.setRotation(CHILD_ROT, new Matrix3f()); // own bind 45°

        pose.verifyCache();

        // MAIN: exactly 30° about Y.
        assertMatrixEquals(main30, pose.getWorldRotationOf(MAIN),
                "MAIN world rotation");

        // CHILD: identity local rotation, inherits MAIN's 30°.
        assertMatrixEquals(main30, pose.getWorldRotationOf(CHILD),
                "CHILD should inherit MAIN's 30");

        // CHILD_ROT: 30° + 45° = 75° about Y (same-axis rotations add).
        Matrix3f expected75 = MatrixHelper.getRotationMatrix(0, 75, 0);
        assertMatrixEquals(expected75, pose.getWorldRotationOf(CHILD_ROT),
                "CHILD_ROT should be 30 + 45 = 75 about Y");
    }


    @Test
    public void parentRotationPivotsChildPosition() {
        Pose pose = freshPose();

        pose.setRotation(MAIN, new Matrix3f());
        pose.setRotation(CHILD, new Matrix3f());
        pose.setRotation(CHILD_ROT, MatrixHelper.getRotationMatrix(0, 45, 0));

        pose.verifyCache();
        assertVecEquals(new Vec3(0, 0, 0), pose.getWorldPositionOf(MAIN),      "MAIN bind pos");
        assertVecEquals(new Vec3(0, 2, 0), pose.getWorldPositionOf(CHILD),     "CHILD bind pos");
        assertVecEquals(new Vec3(0, 4, 0), pose.getWorldPositionOf(CHILD_ROT), "CHILD_ROT bind pos");

        // Rotate CHILD 90° about Y. CHILD sits on the Y axis, so it doesn't move,
        // and CHILD_ROT's (0,2,0) offset is parallel to Y, so it doesn't move either.
        pose.setRotation(CHILD, MatrixHelper.getRotationMatrix(0, 90, 0));
        pose.verifyCache();

        assertVecEquals(new Vec3(0, 2, 0), pose.getWorldPositionOf(CHILD),
                "CHILD stays put on the Y rotation axis");
        assertVecEquals(new Vec3(0, 4, 0), pose.getWorldPositionOf(CHILD_ROT),
                "CHILD_ROT offset is along Y, unaffected by Y rotation");
    }

    @Test
    public void parentRotationSwingsOffsetChild() {
        Pose pose = freshPose();
        pose.setRotation(MAIN, new Matrix3f());
        pose.setRotation(CHILD, new Matrix3f());
        pose.setRotation(CHILD_ROT, new Matrix3f());

        // Push CHILD off the Y axis so a Y rotation has something to swing.
        pose.setTranslation(CHILD, new Vec3(1, 0, 0)); // CHILD offset becomes (1,2,0)
        pose.setRotation(MAIN, MatrixHelper.getRotationMatrix(0, 90, 0));
        pose.verifyCache();

        // (1,2,0) rotated 90° about Y (right-handed): +X -> -Z, so -> (0,2,-1).
        assertVecEquals(new Vec3(0, 2, -1), pose.getWorldPositionOf(CHILD),
                "CHILD should swing from +X to -Z about MAIN's origin");
    }
}