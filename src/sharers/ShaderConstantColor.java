package sharers;

import model.Vertex;
import transforms.Col;

public class ShaderConstantColor implements Shader {
    private final Col color = new Col(0xFFFFFF);

    @Override
    public Col shade(Vertex vertex) {
        return color;
    }
}
