package objects;

import model.Part;
import model.TopologyType;
import model.Vertex;
import transforms.*;

public class BicubicWire extends Solid {
    public BicubicWire(Col color) {
        Point3D[] points = new Point3D[]{
                new Point3D(1, 6, -.5),
                new Point3D(-1, -.5, -.5),
                new Point3D(1, 0, .2),
                new Point3D(-1, .5, .3),
                new Point3D(.8, 1, .9),
                new Point3D(-.5, -1, -.3),
                new Point3D(-.5, 1, -.4),
                new Point3D(-.9, -1, .5),
                new Point3D(0, 6, .1),
                new Point3D(.5, -1, 0),
                new Point3D(.5, 1, .6),
                new Point3D(1, -1, -.8),
                new Point3D(.6, 2, -.6),
                new Point3D(.2, 0, -.4),
                new Point3D(.8, .5, -.3),
                new Point3D(1, 6, 0),
        };

        Bicubic bicubic = new Bicubic(Cubic.FERGUSON, points);

        for (int i = 1; i <= 30; i++) {
            double t = i / 30.;

            for (int j = 1; j <= 30; j++) {
                double u = j / 30.;

                vertexBuffer.add(new Vertex(bicubic.compute(u, t), color));

                addIndices(j + i * 30);
                addIndices(j + i * 30);
                addIndices(j + (i - 1) * 30);
                addIndices(j - 1 + i * 30);
            }
        }

        partBuffer.add(new Part(TopologyType.LINE, 0, 1738));
    }
}
