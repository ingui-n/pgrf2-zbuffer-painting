package render;

import model.Lerp;
import model.Vertex;
import raster.ZBuffer;
import sharers.Shader;

public class LineRasterizer {
    private final ZBuffer zBuffer;
    private final int width, height;
    private Shader shader;
    private final Lerp<Vertex> lerp = new Lerp<>();

    public LineRasterizer(ZBuffer zBuffer) {
        this.zBuffer = zBuffer;
        this.width = zBuffer.getWidth();
        this.height = zBuffer.getHeight();
    }

    public void rasterize(Vertex v1, Vertex v2) {
        Vertex a = v1.dehomog(v1).transformToWindow(width, height);
        Vertex b = v2.dehomog(v2).transformToWindow(width, height);

        double k = Math.abs(a.getY() - b.getY()) / Math.abs(a.getX() - b.getX());

        if (k <= -1 || k >= 1) {
            if (a.getY() > b.getY()) {
                Vertex _a = a;
                a = b;
                b = _a;
            }

            for (int y = Math.max((int) a.getY() + 1, 0); y < Math.min(b.getY(), zBuffer.getHeight() - 1); y++) {
                double t = (y - a.getY()) / (b.getY() - a.getY());
                Vertex v = lerp.lerp(a, b, t);

                zBuffer.drawWithZTest((int) v.getX(), y, v.getZ(), shader.shade(v));
            }
        } else {
            if (a.getX() > b.getX()) {
                Vertex _a = a;
                a = b;
                b = _a;
            }

            for (int x = Math.max((int) a.getX() + 1, 0); x < Math.min(b.getX(), zBuffer.getWidth() - 1); x++) {
                double t = (x - a.getX()) / (b.getX() - a.getX());
                Vertex v = lerp.lerp(a, b, t);

                zBuffer.drawWithZTest(x, (int) v.getY(), v.getZ(), shader.shade(v));
            }
        }
    }

    public void setShader(Shader shader) {
        this.shader = shader;
    }
}
