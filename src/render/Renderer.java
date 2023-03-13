package render;

import model.Lerp;
import model.Part;
import model.Vertex;
import objects.Solid;

import java.util.List;

public class Renderer {
    private final TriangleRasterizer triangleRasterizer;
    private final Lerp<Vertex> lerp = new Lerp<>();

    public Renderer(TriangleRasterizer triangleRasterizer) {
        this.triangleRasterizer = triangleRasterizer;
    }

    public void render(List<Solid> scene) {
        for (Solid solid : scene)
            render(solid);
    }

    public void render(Solid solid) {
        for (Part part : solid.getPartBuffer()) {
            switch (part.getType()) {
//                case POINT -> ;todo
//                case LINE ->
//                case LINE_STRIP ->
                case TRIANGLE -> renderTriangle(part, solid);
//                case TRIANGLE_STRIP ->
            }
        }
    }

    private void renderTriangle(Part part, Solid solid) {
        int start = part.getIndex();

        for (int i = 0; i < part.getCount(); i++) {
            int indexA = solid.getIndexBuffer().get(start);
            int indexB = solid.getIndexBuffer().get(start + 1);
            int indexC = solid.getIndexBuffer().get(start + 2);

            start += 3;

            Vertex a = solid.getVertexBuffer().get(indexA);
            Vertex b = solid.getVertexBuffer().get(indexB);
            Vertex c = solid.getVertexBuffer().get(indexC);

            //ořez fast clip 99 todo test
            if (a.getX() > a.getW() && b.getX() > b.getW() && c.getX() > c.getW()) return;
            if (a.getX() < -a.getW() && b.getX() < -b.getW() && c.getX() < -c.getW()) return;
            if (a.getY() > a.getW() && b.getY() > b.getW() && c.getY() > c.getW()) return;
            if (a.getY() < -a.getW() && b.getY() < -b.getW() && c.getY() < -c.getW()) return;
            if (a.getZ() > a.getW() && b.getZ() > b.getW() && c.getZ() > c.getW()) return;
            if (a.getZ() < 0 && b.getZ() < 0 && c.getZ() < 0) return;

            //seřadit vrcholy podle z od největšího
            if (b.getZ() > a.getZ()) {//todo test
                Vertex _a = a;
                a = b;
                b = _a;
            }

            if (c.getZ() > a.getZ()) {
                Vertex _a = a;
                a = c;
                c = _a;
            }

            if (c.getZ() > b.getZ()) {
                Vertex _b = b;
                b = c;
                c = _b;
            }

            //ořez podle Z 103
            double zMin = 0;

            if (a.getZ() < zMin)
                return;

            if (b.getZ() < zMin) {
                double t1 = (zMin - b.getZ()) / (a.getZ() - b.getZ());
                Vertex vab = lerp.lerp(b, a, t1);

                double t2 = (zMin - c.getZ()) / (a.getZ() - c.getZ());
                Vertex vac = lerp.lerp(c, a, t2);

                triangleRasterizer.rasterize(a, vab, vac);
                return;
            }

            if (c.getZ() < zMin) {
                double t1 = (zMin - b.getZ()) / (c.getZ() - b.getZ());
                Vertex vbc = lerp.lerp(b, a, t1);

                double t2 = (zMin - c.getZ()) / (a.getZ() - c.getZ());
                Vertex vac = lerp.lerp(c, a, t2);

                triangleRasterizer.rasterize(a, b, vbc);
                triangleRasterizer.rasterize(a, vbc, vac);//todo test
                return;
            }

            //dehomog
            a = a.dehomog();//todo placing
            b = b.dehomog();
            c = c.dehomog();

            triangleRasterizer.rasterize(a, b, c);
        }
    }
}
