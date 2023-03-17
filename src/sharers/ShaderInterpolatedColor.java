package sharers;

import model.Vertex;
import transforms.Col;

public class ShaderInterpolatedColor implements Shader {
    @Override
    public Col shade(Vertex vertex) {
        return vertex.getColor().mul(1 / vertex.getOne());
    }
}
