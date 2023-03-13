package raster;

import transforms.Col;

import java.awt.*;

public class ZBuffer {
    private final DepthBuffer depthBuffer;
    private final ImageBuffer imageBuffer;

    public ZBuffer(ImageBuffer imageBuffer) {
        this.imageBuffer = imageBuffer;
        this.depthBuffer = new DepthBuffer(imageBuffer.getWidth(), imageBuffer.getHeight());
    }

    public void drawWithZTest(int x, int y, Double z, Col color) {
        if (depthBuffer.isInside(x, y)) {
            if (depthBuffer.getValue(x, y) < z) {
                depthBuffer.setValue(x, y, z);
                imageBuffer.setValue(x, y, color);
            }
        }
    }

    public Graphics getGraphics() {
        return imageBuffer.getGraphics();
    }

    public int getWidth() {
        return imageBuffer.getWidth();
    }

    public int getHeight() {
        return imageBuffer.getHeight();
    }
}
