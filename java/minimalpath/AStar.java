package minimalpath;

/**
 * 2,3강 탐색에서 배운 탐색중 대표적인 A* 알고리즘으로 3*3 사이즈의 숫자퍼즐을 풀수있게 구현해봤다.
 */

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.*;

public class AStar {
    //풀 수 있는 숫자퍼즐인지 계산하는 함수
    public boolean isSolved(int[] puzzle) {
        int count = 0;
        for (int i = 0; i < 9; i++) {
            for (int j = i + 1; j < 9; j++) {
                if (puzzle[i] > 0 && puzzle[j] > 0 && puzzle[i] > puzzle[j]) {
                    count++;
                }
            }
        }
        return count % 2 == 0 ? true : false;
    }

    //
    private boolean isClear(Node node) {
        if (node.puzzle[8] != 0) {
            return false;
        }
        for (int i = 0; i < 8; i++) {
            if (node.puzzle[i] != i + 1) {
                return false;
            }
        }
        return true;
    }

    public String aStar(int[][] puzzle) {

        Node start = Node.makeStart(puzzle);
        Node end = null;
        //해결 불가능한 퍼즐일시 -1 반환
        if (!isSolved(start.puzzle)) {
            return "-1";
        }

        //우선순위 큐를이용해서 계산된 Node의 value가 낮은 순으로 실행
        //value는 이동횟수+올바른자리에 있는 블럭수로 계산
        PriorityQueue<Node> queue = new PriorityQueue<>();
        queue.add(start);
        //현재 빈칸의 이동할수있는 다음 빈칸의 위치
        int[][] canVisit = {{1, 3}, {0, 4, 2}, {1, 5}, {0, 4, 6}, {1, 3, 5, 7}, {2, 4, 8}, {3, 7}, {4, 6, 8}, {7, 5}};
        while (!queue.isEmpty()) {
            Node cur = queue.poll();
            if (isClear(cur)) {
                end = cur;
                break;
            }
            for (int i = 0; i < canVisit[cur.blank].length; i++) {
                int nextBlank = canVisit[cur.blank][i];
                //이전 단계와 같은 위치로 이동할려는 경우는 추가하지 않는다.
                if (cur.parent.blank == nextBlank) {
                    continue;
                }
                int[] newArr = Arrays.copyOf(cur.puzzle, 9);

                int temp = newArr[cur.blank];
                newArr[cur.blank] = newArr[nextBlank];
                newArr[nextBlank] = temp;

                queue.add(new Node(newArr, nextBlank, cur, cur.deep + 1));
            }
        }
        return end.stepString();
    }

    static class Node implements Comparable<Node> {
        int[] puzzle;
        int deep;
        int blank;
        Node parent;
        int value;

        public static Node makeStart(int[][] puzzle) {
            int[] newPuzzle = new int[9];
            int blank = 0;
            for (int i = 0; i < 3; i++) {
                for (int j = 0; j < 3; j++) {
                    newPuzzle[j + 3 * i] = puzzle[i][j];
                    if (newPuzzle[j + 3 * i] == 0) {
                        blank = j + 3 * i;
                    }
                }
            }
            Node node = new Node(newPuzzle, blank, new Node(-1, -1), 0);
            System.out.println(node);
            return node;
        }
        private Node(int deep, int blank) {
            this.deep = deep;
            this.blank = blank;
        }

        public Node(int[] puzzle, int blank, Node parent, int deep) {
            this.puzzle = puzzle;
            this.deep = deep;
            this.blank = blank;
            this.parent = parent;
            this.value = g(puzzle)+deep;

        }
        //평가함수
        public int g(int [] puzzle){
            int value=0;
            for (int i = 0; i < 8; i++) {
                if (this.puzzle[i] != i + 1) {
                    this.value++;
                }
            }
            if (puzzle[8] != 0) {
                this.value++;
            }
            return value;
        }
        @Override
        public int compareTo(Node o) {
            return Integer.compare(this.value, o.value);
        }

        public String stepString() {
            LinkedList<Node> list = new LinkedList<>();
            Node current = this;
            while (current.parent.blank!=-1) {
                list.addFirst(current);
                current = current.parent;
            }
            int n = 0;
            StringBuffer step = new StringBuffer();
            for (Node node : list) {
                step.append("(" + n++ + ")\n");
                if (node.blank == -1) continue;
                step.append(node);
            }
            return step.toString();
        }
        @Override
        public String toString() {
            StringBuffer step=new StringBuffer();
//            String row = "|";
            String colum = "\n-------------\n";
            step.append(colum);
            for (int i = 0; i < 3; i++) {
                step.append("|");
                for (int j = 0; j < 3; j++) {
                    step.append(" ");
                    step.append(this.puzzle[i * 3 + j]);
                    step.append(" |");
                }
                step.append(colum);
            }
            return step.toString();
        }
    }
}
