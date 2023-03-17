package render;

import model.Lerp;
import model.Vertex;
import raster.ZBuffer;
import sharers.Shader;

public class TriangleRasterizer {
    private final ZBuffer zBuffer;
    private final Lerp<Vertex> lerp;
    private final int width;
    private final int height;
    private Shader shader;

    public TriangleRasterizer(ZBuffer zBuffer) {
        this.zBuffer = zBuffer;
        this.width = zBuffer.getWidth();
        this.height = zBuffer.getHeight();
        this.lerp = new Lerp<>();
    }

    public void rasterize(Vertex v1, Vertex v2, Vertex v3) {
        Vertex a = v1.dehomog(v1).transformToWindow(width, height);
        Vertex b = v2.dehomog(v2).transformToWindow(width, height);
        Vertex c = v3.dehomog(v3).transformToWindow(width, height);

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

        for (int y = Math.max((int) a.getY() + 1, 0); y < Math.min(b.getY(), width - 1); y++) {
            // interpolační koeficient AB
            double t1 = (y - a.getY()) / (b.getY() - a.getY());
            Vertex vab = lerp.lerp(a, b, t1);

            // interpolační koeficient AC
            double t2 = (y - a.getY()) / (c.getY() - a.getY());
            Vertex vac = lerp.lerp(a, c, t2);

            rasterizeLine(vab, vac, y);
        }

        for (int y = Math.max((int) b.getY() + 1, 0); y <= Math.min(c.getY(), height - 1); y++) {
            // interpolační koeficient AC
            double t1 = (y - a.getY()) / (c.getY() - a.getY());
            Vertex vac = lerp.lerp(a, c, t1);

            // interpolační koeficient BC
            double t2 = (y - b.getY()) / (c.getY() - b.getY());
            Vertex vbc = lerp.lerp(b, c, t2);

            rasterizeLine(vac, vbc, y);
        }
    }

    public void setShader(Shader shader) {
        this.shader = shader;
    }

    private void rasterizeLine(Vertex v1, Vertex v2, int y) {
        if (v1.getX() > v2.getX()) {
            Vertex vertex_1 = v1;
            v1 = v2;
            v2 = vertex_1;
        }

        for (int x = Math.max((int) v1.getX(), 0); x <= Math.min(v2.getX(), width - 1); x++) {
            double t = (x - v1.getX()) / (v2.getX() - v1.getX());
            Vertex v = lerp.lerp(v1, v2, t);

            zBuffer.drawWithZTest(x, y, v.getZ(), shader.shade(v));
        }
    }
}
