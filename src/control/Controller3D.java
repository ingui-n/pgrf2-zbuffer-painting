package control;

import raster.ImageBuffer;
import raster.ZBuffer;
import transforms.Col;
import view.Panel;

import java.awt.event.*;

public class Controller3D implements Controller {
    private final Panel panel;
    private final ZBuffer zBuffer;

    public Controller3D(Panel panel) {
        this.panel = panel;
        this.zBuffer = new ZBuffer(panel.getRaster());
        
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
        
        //todo remove test
        zBuffer.drawWithZTest(10, 10, 0.1, new Col(0xffff00));
        zBuffer.drawWithZTest(10, 10, 0.5, new Col(0xff0000));
        
        panel.repaint();
    }
}
