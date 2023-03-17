package raster;

public class DepthBuffer implements Raster<Double> {
    private final int width, height;
    private double clearValue = 1;
    private final double[][] buffer;

    public DepthBuffer(int width, int height) {
        buffer = new double[width][height];
        this.width = width;
        this.height = height;
    }

    @Override
    public void clear() {
        for (int x = 0; x < width; x++) {
            for (int y = 0; y < height; y++) {
                buffer[x][y] = clearValue;
            }
        }
    }

    @Override
    public void setClearValue(Double value) {
        clearValue = value;
    }

    @Override
    public int getWidth() {
        return width;
    }

    @Override
    public int getHeight() {
        return height;
    }

    @Override
    public Double getValue(int x, int y) {
        if (isInside(x, y)) {
            return buffer[x][y];
        }
        return null;
    }

    @Override
    public void setValue(int x, int y, Double value) {
        if (isInside(x, y))
            buffer[x][y] = value;
    }
}
