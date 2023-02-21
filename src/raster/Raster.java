package raster;

public interface Raster<E> {
    void clear();

    void setClearElement(E element);

    int getWidth();

    int getHeight();

    E getElement(int x, int y);

    void setElement(int x, int y, E element);
    
    default boolean isInside(int x, int y) {
        if (x >= 0 && x < getWidth()) {
            return y >= 0 && y < getHeight();
        }
        
        return false;
    }
}
