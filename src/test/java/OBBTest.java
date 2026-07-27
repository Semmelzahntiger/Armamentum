import com.semmelzahntiger.arms.common.skeletons.TestSkeleton;
import com.semmelzahntiger.arms.core.obb.BoneId;
import com.semmelzahntiger.arms.core.obb.Pose;
import com.semmelzahntiger.arms.core.obb.Skeleton;
import com.semmelzahntiger.arms.helper.MatrixHelper;
import org.joml.Matrix3f;
import org.junit.Test;
import util.LogUtil;

public class OBBTest {

    @Test
    public void testRotation() {
        Skeleton skeleton = TestSkeleton.TEST_SKELETON;
        Pose pose = new Pose(skeleton);
        BoneId boneId = new BoneId("TestMain");
        Matrix3f rotation = pose.getWorldRotationOf(boneId);
        LogUtil.getLogger().info("Rotation:\n{}", rotation);
        pose.addRotation(boneId, MatrixHelper.getRotationMatrix(0,35,0));
        LogUtil.getLogger().info("Rotation:\n{}", pose.getWorldRotationOf(boneId));
    }
}
