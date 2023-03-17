package objects;

import model.Part;
import model.TopologyType;
import model.Vertex;
import transforms.Col;

public class Cube extends Solid {
    public Cube(boolean useLines) {
        Col colorA = new Col(0xFF0000);
        Col colorB = new Col(0xFFA600);
        Col colorC = new Col(0xE8FF00);
        Col colorD = new Col(0x74FF00);
        Col colorF = new Col(0x00FF8B);
        Col colorG = new Col(0x00C5FF);
        Col colorH = new Col(0x0061FF);
        Col colorI = new Col(0xDC00FF);

        vertexBuffer.add(new Vertex(0, 0, 0, colorA));// 0
        vertexBuffer.add(new Vertex(0, 1, 0, colorI));// 1
        vertexBuffer.add(new Vertex(1, 0, 0, colorB));// 2
        vertexBuffer.add(new Vertex(1, 1, 0, colorH));// 3
        vertexBuffer.add(new Vertex(0, 0, 1, colorC));// 4
        vertexBuffer.add(new Vertex(0, 1, 1, colorG));// 5
        vertexBuffer.add(new Vertex(1, 0, 1, colorD));// 6
        vertexBuffer.add(new Vertex(1, 1, 1, colorF));// 7

        if (useLines) {
            addIndices(0, 1, 1, 3, 3, 2, 2, 0, 0, 4, 2, 6, 1, 5, 3, 7, 7, 6, 6, 4, 4, 5, 5, 7);
            partBuffer.add(new Part(TopologyType.LINE, 0, 12));
        } else {
            addIndices(
                    0, 1, 2, 1, 2, 3,
                    2, 3, 6, 3, 6, 7,
                    4, 5, 6, 5, 6, 7,
                    1, 4, 0, 1, 4, 5,
                    2, 4, 0, 2, 4, 6,
                    5, 3, 1, 5, 3, 7
            );
            partBuffer.add(new Part(TopologyType.TRIANGLE, 0, 12));
        }
    }
}
