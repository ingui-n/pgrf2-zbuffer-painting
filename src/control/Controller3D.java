package control;

import raster.ImageBuffer;
import raster.ZBuffer;
import render.TriangleRasterizer;
import transforms.Col;
import transforms.Point3D;
import view.Panel;

import java.awt.event.*;

public class Controller3D implements Controller {
    private final Panel panel;
    private final ZBuffer zBuffer;
    private final TriangleRasterizer triangleRasterizer;

    public Controller3D(Panel panel) {
        this.panel = panel;
        this.zBuffer = new ZBuffer(panel.getRaster());
        this.triangleRasterizer = new TriangleRasterizer(zBuffer);

        initObjects(panel.getRaster());
        initListeners();
        redraw();
    }

    public void initObjects(ImageBuffer raster) {
        raster.setClearElement(new Col(0x101010));
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
                new Point3D(1, 1, .5),
                new Point3D(-1, 0, .5),
                new Point3D(0, -1, .5)
        );

        //todo remove test
//        zBuffer.drawWithZTest(10, 10, 0.1, new Col(0xffff00));
//        zBuffer.drawWithZTest(10, 10, 0.5, new Col(0xff0000));

        panel.repaint();
    }
}
