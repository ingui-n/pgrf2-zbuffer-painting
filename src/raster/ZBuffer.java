package raster;

import transforms.Col;

public class ZBuffer {
    private final DepthBuffer depthBuffer;
    private final ImageBuffer imageBuffer;

    public ZBuffer(ImageBuffer imageBuffer) {
        this.imageBuffer = imageBuffer;
        this.depthBuffer = new DepthBuffer(imageBuffer.getWidth(), imageBuffer.getHeight());
    }

    public void drawWithZTest(int x, int y, Double z, Col color) {
        if (depthBuffer.isCoordinatesValid(x, y)) {
            if (depthBuffer.getElement(x, y) < z) {
                depthBuffer.setElement(x, y, z);
                imageBuffer.setElement(x, y, color);
            }
        }
    }
}