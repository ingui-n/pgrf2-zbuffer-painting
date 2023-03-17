package render;

import model.Lerp;
import model.Part;
import model.Vertex;
import objects.Solid;
import transforms.Mat4;

import java.util.ArrayList;

public class Renderer {
    private final LineRasterizer lineRasterizer;
    private final TriangleRasterizer triangleRasterizer;
    private final Lerp<Vertex> lerp = new Lerp<>();
    private Mat4 view;
    private Mat4 proj;

    public Renderer(LineRasterizer lineRasterizer, TriangleRasterizer triangleRasterizer, Mat4 view, Mat4 proj) {
        this.lineRasterizer = lineRasterizer;
        this.triangleRasterizer = triangleRasterizer;
        this.view = view;
        this.proj = proj;
    }

    public void render(ArrayList<Solid> scene) {
        for (Solid solid : scene)
            render(solid);
    }

    public void render(Solid solid) {
        Mat4 mvp = solid.getModel().mul(solid.getTransl()).mul(view).mul(proj);

        int start;

        for (Part part : solid.getPartBuffer()) {
            switch (part.getType()) {
                case LINE -> {
                    start = part.getIndex();

                    for (int i = 0; i < part.getCount(); i++) {
                        int indexA = solid.getIndexBuffer().get(start);
                        int indexB = solid.getIndexBuffer().get(start + 1);

                        start += 2;

                        Vertex a = solid.getVertexBuffer().get(indexA).transform(mvp);
                        Vertex b = solid.getVertexBuffer().get(indexB).transform(mvp);
                        renderLine(a, b);
                    }
                }
                case LINE_STRIP -> {
                    start = part.getIndex();

                    for (int i = 0; i < part.getCount(); i++) {
                        int indexA = solid.getIndexBuffer().get(start);
                        int indexB = solid.getIndexBuffer().get(start + 1);

                        start++;

                        Vertex a = solid.getVertexBuffer().get(indexA).transform(mvp);
                        Vertex b = solid.getVertexBuffer().get(indexB).transform(mvp);

                        renderLine(a, b);
                    }
                }
                case TRIANGLE -> {
                    start = part.getIndex();

                    for (int i = 0; i < part.getCount(); i++) {
                        int indexA = solid.getIndexBuffer().get(start);
                        int indexB = solid.getIndexBuffer().get(start + 1);
                        int indexC = solid.getIndexBuffer().get(start + 2);

                        start += 3;

                        Vertex a = solid.getVertexBuffer().get(indexA).transform(mvp);
                        Vertex b = solid.getVertexBuffer().get(indexB).transform(mvp);
                        Vertex c = solid.getVertexBuffer().get(indexC).transform(mvp);

                        renderTriangle(a, b, c);
                    }
                }
                case TRIANGLE_STRIP -> {
                    start = part.getIndex();

                    for (int i = 0; i < part.getCount(); i++) {
                        int indexA = solid.getIndexBuffer().get(start);
                        int indexB = solid.getIndexBuffer().get(start + 1);
                        int indexC = solid.getIndexBuffer().get(start + 2);

                        start++;

                        Vertex a = solid.getVertexBuffer().get(indexA).transform(mvp);
                        Vertex b = solid.getVertexBuffer().get(indexB).transform(mvp);
                        Vertex c = solid.getVertexBuffer().get(indexC).transform(mvp);

                        renderTriangle(a, b, c);
                    }
                }
            }
        }
    }

    public void setProj(Mat4 proj) {
        this.proj = proj;
    }

    public void setView(Mat4 view) {
        this.view = view;
    }

    private void renderTriangle(Vertex a, Vertex b, Vertex c) {
        //ořez fast clip 99
        if (a.getX() > a.getW() && b.getX() > b.getW() && c.getX() > c.getW()) return;
        if (a.getX() < -a.getW() && b.getX() < -b.getW() && c.getX() < -c.getW()) return;
        if (a.getY() > a.getW() && b.getY() > b.getW() && c.getY() > c.getW()) return;
        if (a.getY() < -a.getW() && b.getY() < -b.getW() && c.getY() < -c.getW()) return;
        if (a.getZ() > a.getW() && b.getZ() > b.getW() && c.getZ() > c.getW()) return;
        if (a.getZ() < 0 && b.getZ() < 0 && c.getZ() < 0) return;

        //seřadit vrcholy podle z od největšího
        if (b.getZ() > a.getZ()) {
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

        if (a.getZ() <= zMin)
            return;

        if (b.getZ() <= zMin) {
            double t1 = (zMin - a.getZ()) / (b.getZ() - a.getZ());
            Vertex vab = lerp.lerp(a, b, t1);

            double t2 = (zMin - a.getZ()) / (c.getZ() - a.getZ());
            Vertex vac = lerp.lerp(a, c, t2);

            triangleRasterizer.rasterize(a, vab, vac);
            return;
        }

        if (c.getZ() <= zMin) {
            double t1 = (zMin - b.getZ()) / (c.getZ() - b.getZ());
            Vertex vbc = lerp.lerp(b, c, t1);

            double t2 = (zMin - a.getZ()) / (c.getZ() - a.getZ());
            Vertex vac = lerp.lerp(a, c, t2);

            triangleRasterizer.rasterize(a, b, vbc);
            triangleRasterizer.rasterize(a, vbc, vac);
            return;
        }

        triangleRasterizer.rasterize(a, b, c);
    }

    private void renderLine(Vertex a, Vertex b) {
        if (a.getX() > a.getW() && b.getX() > b.getW()) return;
        if (a.getX() < -a.getW() && b.getX() < -b.getW()) return;
        if (a.getY() > a.getW() && b.getY() > b.getW()) return;
        if (a.getY() < -a.getW() && b.getY() < -b.getW()) return;
        if (a.getZ() > a.getW() && b.getZ() > b.getW()) return;
        if (a.getZ() < 0 && b.getZ() < 0) return;

        if (b.getZ() > a.getZ()) {
            Vertex _a = a;
            a = b;
            b = _a;
        }

        double zMin = 0;

        if (a.getZ() <= zMin)
            return;

        if (b.getZ() <= zMin) {
            double t = (zMin - a.getZ()) / (b.getZ() - a.getZ());
            Vertex vab = lerp.lerp(a, b, t);

            lineRasterizer.rasterize(a, vab);
            return;
        }

        lineRasterizer.rasterize(a, b);
    }
}
