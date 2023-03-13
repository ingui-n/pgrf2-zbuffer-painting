package objects;

import model.Part;
import model.Vertex;

import java.util.ArrayList;

public abstract class Solid {
    protected ArrayList<Integer> indexBuffer = new ArrayList<>();
    protected ArrayList<Vertex> vertexBuffer = new ArrayList<>();
    protected ArrayList<Part> partBuffer = new ArrayList<>();

    public Solid() {

    }

    public ArrayList<Integer> getIndexBuffer() {
        return indexBuffer;
    }

    public ArrayList<Vertex> getVertexBuffer() {
        return vertexBuffer;
    }

    public ArrayList<Part> getPartBuffer() {
        return partBuffer;
    }
}
