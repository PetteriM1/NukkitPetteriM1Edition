package cn.nukkit.entity.route;

import cn.nukkit.math.Vector3;
import lombok.Getter;

import java.util.Objects;

/**
 * @author zzz1999 @ MobPlugin
 */
public class Node implements Comparable<Node> {

    @Getter
    private final Vector3 vector3;
    private Node parent;
    private int G;
    private int H;
    private int F;

    Node(Vector3 vector3, Node parent, int G, int H) {
        this.vector3 = vector3;
        this.parent = parent;
        this.G = G;
        this.H = H;
        this.F = G + H;
    }

    Node(Vector3 vector3) {
        this(vector3, null, 0, 0);
    }

    @Override
    public int compareTo(Node o) {
        Objects.requireNonNull(o);
        if (this.getF() != o.getF()) {
            return this.getF() - o.getF();
        }
        double breaking;
        if ((breaking = this.getG() + (this.getH() * 0.1) - (o.getG() + (this.getH() * 0.1))) > 0) {
            return 1;
        } else if (breaking < 0) {
            return -1;
        } else {
            return 0;
        }
    }

    int getF() {
        return F;
    }

    void setF(int f) {
        F = f;
    }

    int getG() {
        return G;
    }

    void setG(int g) {
        G = g;
    }

    int getH() {
        return H;
    }

    void setH(int h) {
        H = h;
    }

    Node getParent() {
        return parent;
    }

    void setParent(Node parent) {
        this.parent = parent;
    }
}
