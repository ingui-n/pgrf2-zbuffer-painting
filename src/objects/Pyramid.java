package objects;

import model.Part;
import model.TopologyType;
import model.Vertex;
import transforms.Col;

public class Pyramid extends Solid {
    public Pyramid(boolean useLines) {
        Col colorA = new Col(0xFF0000);
        Col colorB = new Col(0xFFA600);
        Col colorH = new Col(0x0061FF);
        Col colorI = new Col(0xDC00FF);

        vertexBuffer.add(new Vertex(0, 0, 0, colorA));// 0
        vertexBuffer.add(new Vertex(0, 0, 1, colorI));// 1
        vertexBuffer.add(new Vertex(1, 0, 0, colorI));// 2
        vertexBuffer.add(new Vertex(1, 0, 1, colorB));// 3
        vertexBuffer.add(new Vertex(.5, 1, .5, colorH));// 4

        if (useLines) {
            addIndices(0, 1, 1, 3, 0, 2, 3, 2, 0, 4, 1, 4, 2, 4, 3, 4);
            partBuffer.add(new Part(TopologyType.LINE, 0, 8));
        } else {
            addIndices(
                    0, 1, 2, 1, 2, 3,
                    0, 1, 4, 1, 3, 4,
                    2, 3, 4, 0, 2, 4
            );
            partBuffer.add(new Part(TopologyType.TRIANGLE, 0, 6));
        }
    }
}
