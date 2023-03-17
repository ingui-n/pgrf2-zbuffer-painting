package objects;

import model.Part;
import model.TopologyType;
import model.Vertex;
import transforms.Col;

public class Axis extends Solid {
    public Axis() {
        vertexBuffer.add(new Vertex(0, 0, 0));
        vertexBuffer.add(new Vertex(1, 0, 0, new Col(0xFF0000)));
        vertexBuffer.add(new Vertex(0, 1, 0, new Col(0x00FF00)));
        vertexBuffer.add(new Vertex(0, 0, 1, new Col(0x0000FF)));

        addIndices(0, 1, 0, 2, 0, 3);
        partBuffer.add(new Part(TopologyType.LINE, 0, 3));
    }
}
