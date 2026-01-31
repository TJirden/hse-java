package hse.java.practice.task1;

import java.util.Arrays;

public class RubiksCube implements Cube {
    private final int[] state;
    private static final int EDGES_COUNT = 6;
    private final Edge[] edges = new Edge[EDGES_COUNT];

    /**
     * Создать валидный собранный кубик
     * грани разместить по ордеру в енуме цветов
     * грань 0 -> цвет 0
     * грань 1 -> цвет 1
     * и тд 3
     */
    public RubiksCube() {
        state = new int[49];
        for (int i = 1; i <= 48; i++) {
            state[i] = i;
        }
        CubeColor[] colors = CubeColor.values();
        for (int i = 0; i < 6; i++) {
            edges[i] = new Edge(colors[i]);
        }
    }

    @Override
    public void up(RotateDirection direction) {
        rotate(direction, RotationPermutations.U_CW, RotationPermutations.U_CCW);
    }

    @Override
    public void down(RotateDirection direction) {
        rotate(direction, RotationPermutations.D_CW, RotationPermutations.D_CCW);
    }

    @Override
    public void left(RotateDirection direction) {
        rotate(direction, RotationPermutations.L_CW, RotationPermutations.L_CCW);
    }

    @Override
    public void right(RotateDirection direction) {
        rotate(direction, RotationPermutations.R_CW, RotationPermutations.R_CCW);
    }

    @Override
    public void front(RotateDirection direction) {
        rotate(direction, RotationPermutations.F_CW, RotationPermutations.F_CCW);
    }

    @Override
    public void back(RotateDirection direction) {
        rotate(direction, RotationPermutations.B_CW, RotationPermutations.B_CCW);
    }

    private void rotate(RotateDirection direction, int[][] clockwiseCycles, int[][] counterClockwiseCycles) {
        int[][] cycles = direction == RotateDirection.CLOCKWISE ? clockwiseCycles : counterClockwiseCycles;
        applyCycles(cycles);
    }

    private void applyCycles(int[][] cycles) {
        for (int[] cycle : cycles) {
            rotateCycle(cycle);
        }
    }

    private void rotateCycle(int[] cycle) {
        int lastValue = state[cycle[cycle.length - 1]];

        for (int i = cycle.length - 1; i > 0; i--) {
            state[cycle[i]] = state[cycle[i - 1]];
        }

        state[cycle[0]] = lastValue;
    }


    public Edge[] getEdges() {
        CubeColor[] colors = CubeColor.values();

        for (int edgeIndex = 0; edgeIndex < EDGES_COUNT; edgeIndex++) {
            CubeColor[][] edgeParts = new CubeColor[3][3];
            fillEdgeParts(edgeIndex, edgeParts, colors);

            int hardwareEdgeIndex = RotationPermutations.fromWikiToHw.get(edgeIndex);
            edges[hardwareEdgeIndex].setParts(edgeParts);
        }

        return edges;
    }

    private void fillEdgeParts(int edgeIndex, CubeColor[][] edgeParts, CubeColor[] colors) {
        for (int row = 0; row < 3; row++) {
            for (int col = 0; col < 3; col++) {
                int position = col + row * 3 + 1;
                CubeColor color = determineColor(edgeIndex, position, colors);
                edgeParts[row][col] = color;
            }
        }
    }


    private CubeColor determineColor(int edgeIndex, int position, CubeColor[] colors) {
        if (position == 5) {
            int colorId = RotationPermutations.fromWikiToHw.get(edgeIndex);
            return colors[colorId];
        } else if (position > 5) {
            position--;
        }

        int stateIndex = edgeIndex * 8 + position;
        int mappedColorId = (state[stateIndex] - 1) / 8;
        int hardwareColorId = RotationPermutations.fromWikiToHw.get(mappedColorId);

        return colors[hardwareColorId];
    }

    @Override
    public String toString() {
        return Arrays.toString(edges);
    }
}