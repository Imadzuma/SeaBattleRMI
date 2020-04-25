package shared.data;

import java.io.Serializable;

public class PlayerBoard implements Serializable {

    public FieldType[][] fields = new FieldType[10][10];

    public PlayerBoard() {
        for (int i = 0; i < 10; ++i) {
            for (int j = 0; j < 10; ++j)
                fields[i][j] = FieldType.EMPTY;
        }
    }
}
