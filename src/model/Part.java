package model;

public class Part {
    private final TopologyType type;
    private final int index;//prvni index
    private final int count; //pocet primitiv


    public Part(TopologyType type, int index, int count) {
        this.type = type;
        this.index = index;
        this.count = count;
    }

    public int getCount() {
        return count;
    }

    public int getIndex() {
        return index;
    }

    public TopologyType getType() {
        return type;
    }
}
