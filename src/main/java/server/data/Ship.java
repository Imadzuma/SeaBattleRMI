package server.data;

import lombok.Data;

@Data
public class Ship {
    int type;
    int[] x_coords;
    int[] y_coords;
    boolean[] alive_coords;
    boolean alive;

    public boolean shot(int x, int y) {
        for (int i = 0; i < type; ++i) {
            if (x_coords[i] == x && y_coords[i] == y)
                alive_coords[i] = false;
        }
        for (int i = 0; i < type; ++i) {
            if (alive_coords[i])
                return alive;
        }
        alive = false;
        return false;
    }
}
