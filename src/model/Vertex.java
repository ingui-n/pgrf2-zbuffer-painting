package model;

import transforms.Col;
import transforms.Mat4;
import transforms.Point3D;
import transforms.Vec3D;

public class Vertex implements Vectorizable<Vertex> {
    private final Point3D position;
    private Col color = new Col(0xffffff);
    private final double one;

    public Vertex(double x, double y, double z, Col color, double one) {
        this.position = new Point3D(x, y, z);
        this.color = color;
        this.one = one;
    }

    public Vertex(double x, double y, double z, Col color) {
        this.position = new Point3D(x, y, z);
        this.color = color;
        this.one = 1;
    }

    public Vertex(double x, double y, double z) {
        this.position = new Point3D(x, y, z);
        this.one = 1;
    }

    public Vertex(Point3D point, Col color, double one) {
        this.position = point;
        this.color = color;
        this.one = one;
    }

    public Vertex(Point3D point, Col color) {
        this.position = point;
        this.color = color;
        this.one = 1;
    }

    public double getOne() {
        return one;
    }

    public Point3D getPosition() {
        return position;
    }

    public double getX() {
        return position.getX();
    }

    public double getY() {
        return position.getY();
    }

    public double getZ() {
        return position.getZ();
    }

    public double getW() {
        return position.getW();
    }

    public Col getColor() {
        return color;
    }

    @Override
    public Vertex mul(double k) {
        return new Vertex(position.mul(k), color.mul(k), one * k);
    }

    @Override
    public Vertex add(Vertex v) {
        return new Vertex(position.add(v.getPosition()), color.add(v.color), one + v.getOne());
    }

    public Vertex dehomog(Vertex v) {
        return new Vertex(getX() / getW(), getY() / getW(), getZ() / getW(), v.getColor(), v.getOne());
    }

    public Vertex transform(Mat4 mat) {
        return new Vertex(position.mul(mat), color);
    }

    public Vertex transformToWindow(int width, int height) {
        Vec3D v = position.ignoreW()
                .mul(new Vec3D(1, -1, 1))
                .add(new Vec3D(1, 1, 0))
                .mul(new Vec3D((width - 1) / 2., (height - 1) / 2., 1));

        return new Vertex(v.getX(), v.getY(), v.getZ(), color, one);
    }
}
