package control;

import model.Vertex;
import objects.Arrow;
import objects.Solid;
import raster.ImageBuffer;
import raster.ZBuffer;
import render.Renderer;
import render.TriangleRasterizer;
import transforms.Col;
import view.Panel;

import java.awt.event.*;

public class Controller3D implements Controller {
    private final Panel panel;
    private final ZBuffer zBuffer;
    private final TriangleRasterizer triangleRasterizer;
    private final Renderer renderer;

    public Controller3D(Panel panel) {
        this.panel = panel;
        this.zBuffer = new ZBuffer(panel.getRaster());
        this.triangleRasterizer = new TriangleRasterizer(zBuffer);
        this.renderer = new Renderer(triangleRasterizer);

        initObjects(panel.getRaster());
        initListeners();
        redraw();
    }

    public void initObjects(ImageBuffer raster) {
        raster.setClearValue(new Col(0x101010));
    }

    @Override
    public void initListeners() {
        panel.addComponentListener(new ComponentAdapter() {
            @Override
            public void componentResized(ComponentEvent e) {
                panel.resize();
                initObjects(panel.getRaster());
            }
        });
    }

    private void redraw() {
        panel.clear();

        triangleRasterizer.rasterize(
                new Vertex(1, 1, .5),
                new Vertex(-1, 0, .5),
                new Vertex(0, -1, .5)
        );


        Solid arrow = new Arrow();
        renderer.render(arrow);

        panel.repaint();

    }
}
