package sharers;

import model.Vertex;
import transforms.Col;

public interface Shader {
    Col shade(Vertex vertex);
}
