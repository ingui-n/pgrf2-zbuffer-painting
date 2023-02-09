package raster;

import transforms.Col;

import java.awt.*;
import java.awt.image.BufferedImage;

public class ImageBuffer implements Raster<Col> {
    private final BufferedImage img;
    private Col color = new Col(0x000);

    public ImageBuffer(int width, int height) {
        img = new BufferedImage(width, height, BufferedImage.TYPE_INT_RGB);
    }

    public void repaint(Graphics graphics) {
        graphics.drawImage(img, 0, 0, null);
    }

    public void draw(ImageBuffer raster) {
        Graphics graphics = img.getGraphics();
        graphics.setColor(new Color(color.getRGB()));
        graphics.fillRect(0, 0, getWidth(), getHeight());
        graphics.drawImage(raster.img, 0, 0, null);
    }

    @Override
    public Col getElement(int x, int y) throws ArrayIndexOutOfBoundsException {
        if (isCoordinatesValid(x, y))
            return new Col(img.getRGB(x, y));
        return null;
    }

    @Override
    public void setElement(int x, int y, Col color) throws ArrayIndexOutOfBoundsException {
        if (isCoordinatesValid(x, y))
            img.setRGB(x, y, color.getRGB());
    }

    @Override
    public void clear() {
        Graphics g = img.getGraphics();
        g.setColor(new Color(color.getRGB()));
        g.clearRect(0, 0, img.getWidth(), img.getHeight());
    }

    @Override
    public void setClearElement(Col color) {
        this.color = color;
    }

    @Override
    public int getWidth() {
        return img.getWidth();
    }

    @Override
    public int getHeight() {
        return img.getHeight();
    }
    
    public boolean isCoordinatesValid(int x, int y) {//todo 
        if (x >= 0 && x < getWidth()) {
            return y >= 0 && y < getHeight();
        }

        return false;
    }
}

