package objects;

import model.Part;
import model.TopologyType;
import model.Vertex;

public class Arrow extends Solid {
    public Arrow() {
        vertexBuffer.add(new Vertex(0,0,0));
        vertexBuffer.add(new Vertex(.7,0,0));
        vertexBuffer.add(new Vertex(1,0,0));
        vertexBuffer.add(new Vertex(.7,.2,0));
        vertexBuffer.add(new Vertex(.7,-.2,0));

        indexBuffer.add(0);
        indexBuffer.add(1);

        indexBuffer.add(2);
        indexBuffer.add(3);
        indexBuffer.add(4);

        partBuffer.add(new Part(TopologyType.LINE, 0, 1));
        partBuffer.add(new Part(TopologyType.TRIANGLE, 2, 1));
    }
}
