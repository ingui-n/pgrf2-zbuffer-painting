package render;

import model.Lerp;
import model.Vertex;
import raster.ZBuffer;
import transforms.Col;

import java.awt.*;

public class TriangleRasterizer {
    private final ZBuffer zBuffer;
    private final Lerp<Vertex> lerp;
    private final int width;
    private final int height;

    public TriangleRasterizer(ZBuffer zBuffer) {
        this.zBuffer = zBuffer;
        this.width = zBuffer.getWidth();
        this.height = zBuffer.getHeight();
        this.lerp = new Lerp<>();
    }

    public void rasterize(Vertex v1, Vertex v2, Vertex v3) {
        Vertex a = v1.transformToWindow(width, height);
        Vertex b = v2.transformToWindow(width, height);
        Vertex c = v3.transformToWindow(width, height);

        Graphics g = zBuffer.getGraphics();
        g.setColor(new Color(0xff00ff));

        g.drawLine((int) a.getX(), (int) a.getY(), (int) b.getX(), (int) b.getY());
        g.drawLine((int) b.getX(), (int) b.getY(), (int) c.getX(), (int) c.getY());
        g.drawLine((int) c.getX(), (int) c.getY(), (int) a.getX(), (int) a.getY());

        if (a.getY() > b.getY()) {
            Vertex _a = a;
            a = b;
            b = _a;
        }

        if (a.getY() > c.getY()) {
            Vertex _a = a;
            a = c;
            c = _a;
        }

        if (b.getY() > c.getY()) {
            Vertex _b = b;
            b = c;
            c = _b;
        }

        int start = (int) a.getY();

        if (start < 0)
            start = 0;

        for (int y = start; y <= b.getY(); y++) {
            // interpolační koeficient AB
            double t1 = (y - a.getY()) / (b.getY() - a.getY());
            Vertex vab = lerp.lerp(a, b, t1);

            // interpolační koeficient AC
            double t2 = (y - a.getY()) / (c.getY() - a.getY());
            Vertex vac = lerp.lerp(a, c, t2);

            rasterizeLine(vab, vac, y);
        }

        start = (int) b.getY();

        if (start < 0)
            start = 0;

        for (int y = start; y <= c.getY(); y++) {
            // interpolační koeficient AC
            double t2 = (y - a.getY()) / (c.getY() - a.getY());
            Vertex vac = lerp.lerp(a, c, t2);

            // interpolační koeficient BC
            double t3 = (y - b.getY()) / (c.getY() - b.getY());
            Vertex vbc = lerp.lerp(b, c, t3);

            rasterizeLine(vac, vbc, y);
        }
    }

    private void rasterizeLine(Vertex v1, Vertex v2, int y) {
        if (v1.getX() > v2.getX()) {
            Vertex vertex_1 = v1;
            v1 = v2;
            v2 = vertex_1;
        }

        for (int x = (int) v1.getX(); x <= v2.getX(); x++) {
            double t = (x - v2.getX()) / (v1.getX() - v2.getX());
            Vertex v = lerp.lerp(v1, v2, t);

            zBuffer.drawWithZTest(x, y, v.getZ(), new Col(0xff00ff));
        }
    }
}
