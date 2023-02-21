package render;

import raster.ZBuffer;
import transforms.Col;
import transforms.Point3D;
import transforms.Vec3D;

import java.awt.*;

public class TriangleRasterizer {
    private final ZBuffer zBuffer;

    public TriangleRasterizer(ZBuffer zBuffer) {
        this.zBuffer = zBuffer;
    }

    public void rasterize(Point3D p1, Point3D p2, Point3D p3) {
        Vec3D a = transformToWindow(p1);
        Vec3D b = transformToWindow(p2);
        Vec3D c = transformToWindow(p3);

        Graphics g = zBuffer.getGraphics();
        g.setColor(new Color(0xff00ff));

        g.drawLine((int) a.x, (int) a.y, (int) b.x, (int) b.y);
        g.drawLine((int) b.x, (int) b.y, (int) c.x, (int) c.y);
        g.drawLine((int) c.x, (int) c.y, (int) a.x, (int) a.y);

        if (a.y > b.y) {
            Vec3D _a = a;
            a = b;
            b = _a;
        }

        if (a.y > c.y) {
            Vec3D _a = a;
            a = c;
            c = _a;
        }

        if (b.y > c.y) {
            Vec3D _b = b;
            b = c;
            c = _b;
        }

        for (int y = (int) a.y; y <= b.y; y++) {
            // interpolační koeficient AB
            double t1 = (y - a.y) / (b.y - a.y);
            int x1 = (int) ((1 - t1) * a.x + t1 * b.x);

            // interpolační koeficient AC
            double t2 = (y - a.y) / (c.y - a.y);
            int x2 = (int) ((1 - t2) * a.x + t2 * c.x);

            rasterizeLine(x1, x2, y);
        }

        for (int y = (int) b.y; y <= c.y; y++) {
            // interpolační koeficient AC
            double t2 = (y - a.y) / (c.y - a.y);
            int x2 = (int) ((1 - t2) * a.x + t2 * c.x);

            // interpolační koeficient BC
            double t3 = (y - b.y) / (c.y - b.y);
            int x3 = (int) ((1 - t3) * b.x + t3 * c.x);

            rasterizeLine(x2, x3, y);
        }
    }

    private void rasterizeLine(int x1, int x2, int y) {
        if (x1 > x2) {
            int _x1 = x1;
            x1 = x2;
            x2 = _x1;
        }

        for (int x = x1; x <= x2; x++) {
            zBuffer.drawWithZTest(x, y, .5, new Col(0xff00ff));
        }
    }

    private Vec3D transformToWindow(Point3D p) {
        return p.ignoreW()
                .mul(new Vec3D(1, -1, 1))
                .add(new Vec3D(1, 1, 0))
                .mul(new Vec3D((zBuffer.getWidth() - 1) / 2., (zBuffer.getHeight() - 1) / 2., 1));
    }
}
