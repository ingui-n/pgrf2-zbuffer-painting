package objects;

import model.Part;
import model.Vertex;
import transforms.Mat4;
import transforms.Mat4Identity;
import transforms.Mat4Transl;

import java.util.ArrayList;
import java.util.Arrays;

public abstract class Solid {
    protected ArrayList<Integer> indexBuffer = new ArrayList<>();
    protected ArrayList<Vertex> vertexBuffer = new ArrayList<>();
    protected ArrayList<Part> partBuffer = new ArrayList<>();

    protected Mat4 model = new Mat4Identity();
    protected Mat4 transl = new Mat4Transl(0, 0, 0);
    protected boolean isSelected = false;
    private double xRot = 0, yRot = 0, zRot = 0;
    private double xTran = 0, yTran = 0, zTran = 0, scale = 1;

    public ArrayList<Integer> getIndexBuffer() {
        return indexBuffer;
    }

    public ArrayList<Vertex> getVertexBuffer() {
        return vertexBuffer;
    }

    public ArrayList<Part> getPartBuffer() {
        return partBuffer;
    }

    public Mat4 getModel() {
        return model;
    }

    public Mat4 getTransl() {
        return transl;
    }

    public void setTransl(Mat4 transl) {
        this.transl = transl;
    }

    public void setModel(Mat4 model) {
        this.model = model;
    }

    protected void addIndices(Integer... indices) {
        indexBuffer.addAll(Arrays.asList(indices));
    }

    public boolean isSelected() {
        return isSelected;
    }

    public Solid cloneSolid(Solid solid) {
        solid.setSelected(!isSelected());
        solid.setxRot(getxRot());
        solid.setyRot(getyRot());
        solid.setzRot(getzRot());
        solid.setxTran(getxTran());
        solid.setyTran(getyTran());
        solid.setzTran(getzTran());
        solid.setScale(getScale());

        return solid;
    }

    public void setSelected(boolean selected) {
        isSelected = selected;
    }

    public double getxRot() {
        return xRot;
    }

    public double getyRot() {
        return yRot;
    }

    public double getzRot() {
        return zRot;
    }

    public double getxTran() {
        return xTran;
    }

    public double getyTran() {
        return yTran;
    }

    public double getzTran() {
        return zTran;
    }

    public void setxRot(double xRot) {
        this.xRot = xRot;
    }

    public void setyRot(double yRot) {
        this.yRot = yRot;
    }

    public void setzRot(double zRot) {
        this.zRot = zRot;
    }

    public void setxTran(double xTran) {
        this.xTran = xTran;
    }

    public void setyTran(double yTran) {
        this.yTran = yTran;
    }

    public void setzTran(double zTran) {
        this.zTran = zTran;
    }

    public double getScale() {
        return scale;
    }

    public void setScale(double scale) {
        this.scale = scale;
    }
}
