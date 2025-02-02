/*
 * This file is part of Bisq.
 *
 * Bisq is free software: you can redistribute it and/or modify it
 * under the terms of the GNU Affero General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or (at
 * your option) any later version.
 *
 * Bisq is distributed in the hope that it will be useful, but WITHOUT
 * ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or
 * FITNESS FOR A PARTICULAR PURPOSE. See the GNU Affero General Public
 * License for more details.
 *
 * You should have received a copy of the GNU Affero General Public License
 * along with Bisq. If not, see <http://www.gnu.org/licenses/>.
 */

package bisq.desktop.main.account.content.notifications.qr;

import org.apache.commons.io.IOUtils;

import java.nio.ByteBuffer;

import java.io.IOException;
import java.io.InputStream;

import org.jetbrains.annotations.NotNull;

import static org.bytedeco.opencv.global.opencv_imgcodecs.imdecode;
import static org.opencv.imgcodecs.Imgcodecs.IMREAD_ANYCOLOR;
import static org.opencv.imgcodecs.Imgcodecs.IMREAD_ANYDEPTH;



import org.bytedeco.javacpp.BytePointer;
import org.bytedeco.javacv.Frame;
import org.bytedeco.javacv.OpenCVFrameConverter;
import org.bytedeco.opencv.opencv_core.Mat;

/**
 * A utility for managing JavaCV {@link Frame}'s for testing purposes.
 */
public class FrameUtil {
    private FrameUtil() {
        throw new AssertionError("Utility class should not be instantiated");
    }

    public static Frame createRandomFrame(final int width, final int height, final int channels) {
        final byte[] imageData = new byte[width * height * channels];
        for (int i = 0; i < imageData.length; i++) {
            imageData[i] = (byte) (i % 255);
        }

        final Frame frame = new Frame(width, height, Frame.DEPTH_UBYTE, channels);
        frame.image[0] = ByteBuffer.wrap(imageData);
        return frame;
    }

    public static Frame createFrameFromImageResource(@NotNull final String imagePath) {
        try (
                InputStream is = FrameUtil.class.getClassLoader().getResourceAsStream(imagePath);
                OpenCVFrameConverter.ToMat converter = new OpenCVFrameConverter.ToMat()
        ) {
            if (is == null) {
                throw new IllegalArgumentException("Image could not be loaded: " + imagePath);
            }
            Mat imageMat = imdecode(new Mat(new BytePointer(IOUtils.toByteArray(is))), IMREAD_ANYDEPTH | IMREAD_ANYCOLOR);
            if (imageMat.empty()) {
                throw new IllegalArgumentException("Image could not be loaded: " + imagePath);
            }
            return converter.convert(imageMat);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}
