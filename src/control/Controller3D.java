package control;

import objects.*;
import raster.ZBuffer;
import render.LineRasterizer;
import render.Renderer;
import render.TriangleRasterizer;
import sharers.ShaderConstantColor;
import sharers.ShaderInterpolatedColor;
import transforms.*;
import view.Panel;

import javax.swing.*;
import java.awt.event.*;
import java.util.ArrayList;

public class Controller3D implements Controller {
    private final Panel panel;
    private ZBuffer zBuffer;
    private LineRasterizer lineRasterizer;
    private TriangleRasterizer triangleRasterizer;
    private Renderer renderer;
    private Camera camera;
    private final double cameraSpeed = .3;
    private boolean firstPerson = false;
    private int ox = 10, oy = 0;
    private Solid axis, cube, pyramid, curveWire;
    private Mat4 proj;
    private final int x = -10, y = 1, z = 5;
    private boolean shaderConstant = false;
    private ArrayList<Solid> solids = new ArrayList<>();

    public Controller3D(Panel panel) {
        this.panel = panel;
        this.zBuffer = new ZBuffer(panel.getRaster());
        this.triangleRasterizer = new TriangleRasterizer(zBuffer);
        this.lineRasterizer = new LineRasterizer(zBuffer);

        triangleRasterizer.setShader(new ShaderInterpolatedColor());
        lineRasterizer.setShader(new ShaderInterpolatedColor());

        initObjects();
        initListeners();
        initCamera();
        redraw();
    }

    public void initObjects() {
        axis = new Axis();
        cube = new Cube(false);
        pyramid = new Pyramid(false);
        curveWire = new BicubicWire(new Col(0x0000FF));

        new Timer(10, e -> {
            pyramid.setzRot(pyramid.getzRot() - .01);
            redraw();
        }).start();
    }

    @Override
    public void initListeners() {
        panel.addComponentListener(new ComponentAdapter() {
            @Override
            public void componentResized(ComponentEvent e) {
                panel.resize();
                zBuffer = new ZBuffer(panel.getRaster());
                triangleRasterizer = new TriangleRasterizer(zBuffer);
                lineRasterizer = new LineRasterizer(zBuffer);

                triangleRasterizer.setShader(new ShaderInterpolatedColor());
                lineRasterizer.setShader(new ShaderInterpolatedColor());

                initObjects();
                initCamera();
                redraw();
            }
        });

        panel.addKeyListener(new KeyAdapter() {
            @Override
            public void keyPressed(KeyEvent e) {
                switch (e.getKeyCode()) {
                    case KeyEvent.VK_W -> camera = camera.forward(cameraSpeed);
                    case KeyEvent.VK_A -> camera = camera.left(cameraSpeed);
                    case KeyEvent.VK_S -> camera = camera.backward(cameraSpeed);
                    case KeyEvent.VK_D -> camera = camera.right(cameraSpeed);
                    case KeyEvent.VK_SHIFT -> camera = camera.up(cameraSpeed);
                    case KeyEvent.VK_CONTROL -> camera = camera.down(cameraSpeed);
                    case KeyEvent.VK_PLUS, KeyEvent.VK_ADD -> changeView("scale+");
                    case KeyEvent.VK_MINUS, KeyEvent.VK_SUBTRACT -> changeView("scale-");
                    case KeyEvent.VK_F1 -> changeView("xRot-");
                    case KeyEvent.VK_F2 -> changeView("xRot+");
                    case KeyEvent.VK_F3 -> changeView("yRot+");
                    case KeyEvent.VK_F4 -> changeView("yRot-");
                    case KeyEvent.VK_F5 -> changeView("zRot-");
                    case KeyEvent.VK_F6 -> changeView("zRot+");
                    case KeyEvent.VK_F7 -> changeView("xTran-");
                    case KeyEvent.VK_F8 -> changeView("xTran+");
                    case KeyEvent.VK_F9 -> changeView("yTran-");
                    case KeyEvent.VK_F10 -> changeView("yTran+");
                    case KeyEvent.VK_F11 -> changeView("zTran-");
                    case KeyEvent.VK_F12 -> changeView("zTran+");
                    case KeyEvent.VK_M -> cube = cube.cloneSolid(new Cube(!cube.isSelected()));
                    case KeyEvent.VK_N -> pyramid = pyramid.cloneSolid(new Pyramid(!pyramid.isSelected()));
                    case KeyEvent.VK_B ->
                            curveWire = curveWire.cloneSolid(new BicubicWire(new Col((int) (Math.random() * 0x1000000))));
                    case KeyEvent.VK_P -> switchFirstPerson();
                    case KeyEvent.VK_ESCAPE -> System.exit(0);
                    case KeyEvent.VK_C -> {
                        if (shaderConstant) {
                            triangleRasterizer.setShader(new ShaderInterpolatedColor());
                            lineRasterizer.setShader(new ShaderInterpolatedColor());
                        } else {
                            triangleRasterizer.setShader(new ShaderConstantColor());
                            lineRasterizer.setShader(new ShaderConstantColor());
                        }

                        shaderConstant = !shaderConstant;
                    }
                }

                redraw();
            }
        });

        panel.addMouseMotionListener(new MouseAdapter() {
            @Override
            public void mouseDragged(MouseEvent e) {
                double azimuth = (e.getX() - ox) / (double) panel.getWidth();
                double zenith = (e.getY() - oy) / (double) panel.getHeight();

                camera = camera.addAzimuth(azimuth).addZenith(zenith);

                ox = e.getX();
                oy = e.getY();

                redraw();
            }
        });

        panel.addMouseListener(new MouseAdapter() {
            @Override
            public void mousePressed(MouseEvent e) {
                ox = e.getX();
                oy = e.getY();
            }
        });

        panel.addMouseWheelListener(new MouseAdapter() {
            @Override
            public void mouseWheelMoved(MouseWheelEvent e) {
                if (e.getWheelRotation() == 1) {
                    camera = camera.backward(cameraSpeed);
                } else {
                    camera = camera.forward(cameraSpeed);
                }

                redraw();
            }
        });
    }

    private void switchFirstPerson() {
        firstPerson = !firstPerson;
        initCamera();
        redraw();
    }

    private void initCamera() {
        ox = 0;
        oy = 0;

        camera = new Camera(new Vec3D(x, y, z), Math.toRadians(ox), Math.toRadians(oy), 1, false);

        if (firstPerson) {
            proj = new Mat4OrthoRH(15, (panel.getHeight() / (double) panel.getWidth()) * 15., 1, 20);
        } else {
            proj = new Mat4PerspRH(Math.toRadians(60), (float) zBuffer.getWidth() / zBuffer.getHeight(), 1, 20);
        }

        renderer = new Renderer(lineRasterizer, triangleRasterizer, camera.getViewMatrix(), proj);
    }

    private void changeView(String viewType) {
        for (Solid solid : solids) {
            if (solid.isSelected()) {
                switch (viewType) {
                    case "xRot+" -> solid.setxRot(solid.getxRot() + .01);
                    case "xRot-" -> solid.setxRot(solid.getxRot() - .01);
                    case "yRot+" -> solid.setyRot(solid.getyRot() + .01);
                    case "yRot-" -> solid.setyRot(solid.getyRot() - .01);
                    case "zRot+" -> solid.setzRot(solid.getzRot() + .01);
                    case "zRot-" -> solid.setzRot(solid.getzRot() - .01);
                    case "xTran+" -> solid.setxTran(solid.getxTran() + .01);
                    case "xTran-" -> solid.setxTran(solid.getxTran() - .01);
                    case "yTran+" -> solid.setyTran(solid.getyTran() + .01);
                    case "yTran-" -> solid.setyTran(solid.getyTran() - .01);
                    case "zTran+" -> solid.setzTran(solid.getzTran() + .01);
                    case "zTran-" -> solid.setzTran(solid.getzTran() - .01);
                    case "scale+" -> solid.setScale(solid.getScale() + .1);
                    case "scale-" -> solid.setScale(solid.getScale() - .1);
                }
            }
        }
    }

    private void redraw() {
        panel.clear();
        zBuffer.clear();

        renderer.setView(camera.getViewMatrix());

        cube.setTransl(new Mat4Transl(1, 1, 1));
        cube.setScale(2);
        pyramid.setTransl(new Mat4Transl(5, -2, 1));
        pyramid.setScale(3);
        curveWire.setTransl(new Mat4Transl(5, 0, 5));

        ArrayList<Solid> s = new ArrayList<>();

        s.add(axis);
        s.add(cube);
        s.add(pyramid);
        s.add(curveWire);

        for (Solid solid : s) {
            solid.setModel(
                    new Mat4RotXYZ(solid.getxRot(), solid.getyRot(), solid.getzRot())
                            .mul(new Mat4Transl(solid.getxTran(), solid.getyTran(), solid.getzTran()))
                            .mul(new Mat4Scale(solid.getScale(), solid.getScale(), solid.getScale()))
            );
        }

        solids = s;
        renderer.render(solids);
        panel.repaint();
    }
}
