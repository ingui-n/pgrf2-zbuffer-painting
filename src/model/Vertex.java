package model;

import transforms.Col;
import transforms.Mat4;
import transforms.Point3D;
import transforms.Vec3D;

public class Vertex implements Vectorizable<Vertex> {
    private final Point3D position;
    private final Col color;
    //todo normala, souradnice do textury

    public Vertex(double x, double y, double z, Col color) {
        this.position = new Point3D(x, y, z);
        this.color = color;
    }

    public Vertex(double x, double y, double z) {
        this.position = new Point3D(x, y, z);
        this.color = new Col(0xff0000);
    }

    public Vertex(Point3D p, Col color) {
        this.position = new Point3D(p.getX(), p.getY(), p.getZ(), p.getW());
        this.color = color;
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
        return new Vertex(position.mul(k), color.mul(k));
    }

    @Override
    public Vertex add(Vertex v) {
        return new Vertex(position.add(v.getPosition()), color.add(v.color));
    }

    public Vertex dehomog() {
        return new Vertex(new Point3D(getX() / getW(), getY() / getW(), getZ() / getW(), 1), color);
    }


    //todo společný interface pro metody:


    public Vertex transform(Mat4 mat) {
        return new Vertex(position.mul(mat), color);
    }

    public Vertex transformToWindow(int width, int height) {
        Vec3D v = position.ignoreW()
                .mul(new Vec3D(1, -1, 1))
                .add(new Vec3D(1, 1, 0))
                .mul(new Vec3D((width - 1) / 2., (height - 1) / 2., 1));

        return new Vertex(v.getX(), v.getY(), v.getZ(), color);
    }
}
