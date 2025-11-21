package cn.nukkit.entity.route;

import cn.nukkit.block.Block;
import cn.nukkit.block.BlockFlowable;
import cn.nukkit.block.BlockSnowLayer;
import cn.nukkit.entity.EntityWalking;
import cn.nukkit.level.format.FullChunk;
import cn.nukkit.math.AxisAlignedBB;
import cn.nukkit.math.SimpleAxisAlignedBB;
import cn.nukkit.math.Vector3;

import java.util.ArrayList;
import java.util.Collections;
import java.util.PriorityQueue;

/**
 * @author zzz1999 @ MobPlugin
 */
public class WalkerRouteFinder extends SimpleRouteFinder {

    private final static int DIRECT_MOVE_COST = 10;
    private final static int OBLIQUE_MOVE_COST = 14;

    private static final int ACCORDING_X_OBTAIN_Y = 0;
    private static final int ACCORDING_Y_OBTAIN_X = 1;

    private final PriorityQueue<Node> openList = new PriorityQueue<>();
    private final ArrayList<Node> closeList = new ArrayList<>();

    private int searchLimit = 100;

    public WalkerRouteFinder(EntityWalking entity) {
        super(entity);
        this.level = entity.getLevel();
    }

    public WalkerRouteFinder(EntityWalking entity, Vector3 start) {
        super(entity);
        this.level = entity.getLevel();
        this.start = start;
    }

    public WalkerRouteFinder(EntityWalking entity, Vector3 start, Vector3 destination) {
        super(entity);
        this.level = entity.getLevel();
        this.start = start;
        this.destination = destination;
    }

    private ArrayList<Node> FloydSmooth(ArrayList<Node> array) {
        int current = 0;
        int total = 2;
        if (array.size() > 2) {
            while (total < array.size()) {
                if (!hasBarrier(array.get(current), array.get(total)) && total != array.size() - 1) {
                    total++;
                } else {
                    array.get(total - 1).setParent(array.get(current));
                    current = total - 1;
                    total++;
                }
            }

            Node temp = array.get(array.size() - 1);
            ArrayList<Node> tempL = new ArrayList<>();
            tempL.add(temp);
            while (temp.getParent() != null) {
                tempL.add((temp = temp.getParent()));
            }
            Collections.reverse(tempL);
            return tempL;
        }
        return array;
    }

    private int calHeuristic(Vector3 pos1, Vector3 pos2) {
        return 10 * (Math.abs(pos1.getFloorX() - pos2.getFloorX()) + Math.abs(pos1.getFloorZ() - pos2.getFloorZ())) +
                11 * Math.abs(pos1.getFloorY() - pos2.getFloorY());
    }

    private static double calLinearFunction(Vector3 pos1, Vector3 pos2, double element, int type) {
        if (pos1.getFloorY() != pos2.getFloorY()) return Double.MAX_VALUE;
        if (pos1.getX() == pos2.getX()) {
            if (type == ACCORDING_Y_OBTAIN_X) return pos1.getX();
            else return Double.MAX_VALUE;
        } else if (pos1.getZ() == pos2.getZ()) {
            if (type == ACCORDING_X_OBTAIN_Y) return pos1.getZ();
            else return Double.MAX_VALUE;
        } else {
            if (type == ACCORDING_X_OBTAIN_Y) {
                return (element - pos1.getX()) * (pos1.getZ() - pos2.getZ()) / (pos1.getX() - pos2.getX()) + pos1.getZ();
            } else {
                return (element - pos1.getZ()) * (pos1.getX() - pos2.getX()) / (pos1.getZ() - pos2.getZ()) + pos1.getX();
            }
        }
    }

    private Block getHighestUnder(Vector3 pos) {
        int x = pos.getFloorX();
        int z = pos.getFloorZ();

        FullChunk chunk = level.getChunkIfLoaded(x >> 4, z >> 4);

        if (chunk != null) {
            int minY = pos.getFloorY() - 4;

            for (int y = pos.getFloorY(); y >= minY; y--) {
                Block block = level.getBlock(chunk, x, y, z, false);
                Block above;
                if (isWalkable(block) && ((above = level.getBlock(chunk, x, y + 1, z, false)).getId() == Block.AIR || above instanceof BlockFlowable || (above instanceof BlockSnowLayer && above.isTransparent()))) {
                    return block;
                }
            }
        }

        return null;
    }

    private Node getNodeInCloseByVector2(Vector3 vector) {
        for (Node node : this.closeList) {
            if (vector.equals(node.getVector3())) {
                return node;
            }
        }
        return null;
    }

    private Node getNodeInOpenByVector2(Vector3 vector2) {
        for (Node node : this.openList) {
            if (vector2.equals(node.getVector3())) {
                return node;
            }
        }

        return null;
    }

    private ArrayList<Node> getPathRoute() {
        ArrayList<Node> nodes = new ArrayList<>();
        Node temp = closeList.get(closeList.size() - 1);
        nodes.add(temp);
        while (!temp.getParent().getVector3().equals(start)) {
            nodes.add(temp = temp.getParent());
        }
        nodes.add(temp.getParent());
        Collections.reverse(nodes);
        return nodes;
    }

    private int getWalkableHorizontalOffset(Vector3 vector3) {
        if (level.getProvider() == null) {
            return -256;
        }
        Block block = getHighestUnder(vector3);
        if (block != null) {
            return ((int) block.getY() - vector3.getFloorY()) + 1;
        }
        return -256;
    }

    private boolean hasBarrier(Node node1, Node node2) {
        return hasBarrier(node1.getVector3(), node2.getVector3());
    }

    private boolean hasBarrier(Vector3 pos1, Vector3 pos2) {
        if (pos1.equals(pos2)) return false;
        if (pos1.getFloorY() != pos2.getFloorY()) return true;
        boolean traverseDirection = Math.abs(pos1.getX() - pos2.getX()) > Math.abs(pos1.getZ() - pos2.getZ());
        double loopStart;
        double loopEnd;
        ArrayList<Vector3> list = new ArrayList<>();
        if (traverseDirection) {
            loopStart = Math.min(pos1.getX(), pos2.getX());
            loopEnd = Math.max(pos1.getX(), pos2.getX());
            for (double i = Math.ceil(loopStart); i <= Math.floor(loopEnd); i += 1.0) {
                double result;
                if ((result = calLinearFunction(pos1, pos2, i, ACCORDING_X_OBTAIN_Y)) != Double.MAX_VALUE)
                    list.add(new Vector3(i, pos1.getY(), result));
            }
        } else {
            loopStart = Math.min(pos1.getZ(), pos2.getZ());
            loopEnd = Math.max(pos1.getZ(), pos2.getZ());
            for (double i = Math.ceil(loopStart); i <= Math.floor(loopEnd); i += 1.0) {
                double result;
                if ((result = calLinearFunction(pos1, pos2, i, ACCORDING_Y_OBTAIN_X)) != Double.MAX_VALUE)
                    list.add(new Vector3(result, pos1.getY(), i));
            }

        }
        return hasBlocksAround(list);
    }

    private boolean hasBlocksAround(ArrayList<Vector3> list) {
        double radius = (this.entity.getWidth() * this.entity.getScale()) / 2 + 0.1;
        double height = this.entity.getHeight() * this.entity.getScale();
        for (Vector3 vector3 : list) {
            AxisAlignedBB bb = new SimpleAxisAlignedBB(vector3.getX() - radius, vector3.getY(), vector3.getZ() - radius, vector3.getX() + radius, vector3.getY() + height, vector3.getZ() + radius);
            if (level.hasCollision(this.entity, bb, false)) return true;

            boolean xIsInt = vector3.getX() % 1 == 0;
            boolean zIsInt = vector3.getZ() % 1 == 0;
            Vector3 floor = vector3.floor();
            if (xIsInt && zIsInt) {
                if (!isWalkable(level.getBlock(this.entity.chunk, (int) floor.getX(), (int) (floor.getY() - 1), (int) floor.getZ(), false)) ||
                        !isWalkable(level.getBlock(this.entity.chunk, (int) (floor.getX() - 1), (int) (floor.getY() - 1), (int) floor.getZ(), false)) ||
                        !isWalkable(level.getBlock(this.entity.chunk, (int) (floor.getX() - 1), (int) (floor.getY() - 1), (int) (floor.getZ() - 1), false)) ||
                        !isWalkable(level.getBlock(this.entity.chunk, (int) floor.getX(), (int) (floor.getY() - 1), (int) (floor.getZ() - 1), false)))
                    return true;
            } else if (xIsInt) {
                if (!isWalkable(level.getBlock(this.entity.chunk, (int) floor.getX(), (int) (floor.getY() - 1), (int) floor.getZ(), false)) ||
                        !isWalkable(level.getBlock(this.entity.chunk, (int) (floor.getX() - 1), (int) (floor.getY() - 1), (int) floor.getZ(), false)))
                    return true;
            } else if (zIsInt) {
                if (!isWalkable(level.getBlock(this.entity.chunk, (int) floor.getX(), (int) (floor.getY() - 1), (int) floor.getZ(), false)) ||
                        !isWalkable(level.getBlock(this.entity.chunk, (int) floor.getX(), (int) (floor.getY() - 1), (int) (floor.getZ() - 1), false)))
                    return true;
            } else {
                if (!isWalkable(level.getBlock(this.entity.chunk, (int) floor.getX(), (int) (floor.getY() - 1), (int) floor.getZ(), false)))
                    return true;
            }
        }
        return false;
    }

    private boolean isPassable(Vector3 vector3) {
        double radius = (this.entity.getWidth() * this.entity.getScale()) / 2;
        float height = this.entity.getHeight() * this.entity.getScale();
        AxisAlignedBB bb = new SimpleAxisAlignedBB(vector3.getX() - radius, vector3.getY(), vector3.getZ() - radius, vector3.getX() + radius, vector3.getY() + height, vector3.getZ() + radius);

        return !level.hasCollision(this.entity, bb, false);
    }

    private boolean isPositionOverlap(Vector3 vector1, Vector3 vector2) {
        return (int) vector1.x == (int) vector2.x
                && (int) vector1.z == (int) vector2.z
                && (int) vector1.y == (int) vector2.y;
    }

    private boolean isWalkable(Block block) {
        return !block.canPassThrough() && !(block.getId() == Block.LAVA || block.getId() == Block.STILL_LAVA || block.getId() == Block.CACTUS);
    }

    private boolean noContainsInClose(Vector3 vector) {
        return getNodeInCloseByVector2(vector) == null;
    }

    private void putNeighborNodeIntoOpen(Node node) {
        boolean N, E, S, W;

        Vector3 vector3 = new Vector3(node.getVector3().getFloorX() + 0.5, node.getVector3().getY(), node.getVector3().getFloorZ() + 0.5);

        double y;

        if (E = ((y = getWalkableHorizontalOffset(vector3.add(1, 0, 0))) != -256)) {
            Vector3 vec = vector3.add(1, y, 0);
            if (noContainsInClose(vec) && isPassable(vec)) {
                Node nodeNear = getNodeInOpenByVector2(vec);
                if (nodeNear == null) {
                    this.openList.offer(new Node(vec, node, DIRECT_MOVE_COST + node.getG(), calHeuristic(vec, destination)));
                } else {
                    if (node.getG() + DIRECT_MOVE_COST < nodeNear.getG()) {
                        nodeNear.setParent(node);
                        nodeNear.setG(node.getG() + DIRECT_MOVE_COST);
                        nodeNear.setF(nodeNear.getG() + nodeNear.getH());
                    }
                }
            }
        }

        if (S = ((y = getWalkableHorizontalOffset(vector3.add(0, 0, 1))) != -256)) {
            Vector3 vec = vector3.add(0, y, 1);
            if (noContainsInClose(vec) && isPassable(vec)) {
                Node nodeNear = getNodeInOpenByVector2(vec);
                if (nodeNear == null) {
                    this.openList.offer(new Node(vec, node, DIRECT_MOVE_COST + node.getG(), calHeuristic(vec, destination)));
                } else {
                    if (node.getG() + DIRECT_MOVE_COST < nodeNear.getG()) {
                        nodeNear.setParent(node);
                        nodeNear.setG(node.getG() + DIRECT_MOVE_COST);
                        nodeNear.setF(nodeNear.getG() + nodeNear.getH());
                    }
                }
            }
        }

        if (W = ((y = getWalkableHorizontalOffset(vector3.add(-1, 0, 0))) != -256)) {
            Vector3 vec = vector3.add(-1, y, 0);
            if (noContainsInClose(vec) && isPassable(vec)) {
                Node nodeNear = getNodeInOpenByVector2(vec);
                if (nodeNear == null) {
                    this.openList.offer(new Node(vec, node, DIRECT_MOVE_COST + node.getG(), calHeuristic(vec, destination)));
                } else {
                    if (node.getG() + DIRECT_MOVE_COST < nodeNear.getG()) {
                        nodeNear.setParent(node);
                        nodeNear.setG(node.getG() + DIRECT_MOVE_COST);
                        nodeNear.setF(nodeNear.getG() + nodeNear.getH());
                    }
                }
            }
        }

        if (N = ((y = getWalkableHorizontalOffset(vector3.add(0, 0, -1))) != -256)) {
            Vector3 vec = vector3.add(0, y, -1);
            if (noContainsInClose(vec) && isPassable(vec)) {
                Node nodeNear = getNodeInOpenByVector2(vec);
                if (nodeNear == null) {
                    this.openList.offer(new Node(vec, node, DIRECT_MOVE_COST + node.getG(), calHeuristic(vec, destination)));
                } else {
                    if (node.getG() + DIRECT_MOVE_COST < nodeNear.getG()) {
                        nodeNear.setParent(node);
                        nodeNear.setG(node.getG() + DIRECT_MOVE_COST);
                        nodeNear.setF(nodeNear.getG() + nodeNear.getH());
                    }
                }
            }
        }

        if (N && E && ((y = getWalkableHorizontalOffset(vector3.add(1, 0, -1))) != -256)) {
            Vector3 vec = vector3.add(1, y, -1);
            if (noContainsInClose(vec) && isPassable(vec)) {
                Node nodeNear = getNodeInOpenByVector2(vec);
                if (nodeNear == null) {
                    this.openList.offer(new Node(vec, node, OBLIQUE_MOVE_COST + node.getG(), calHeuristic(vec, destination)));
                } else {
                    if (node.getG() + OBLIQUE_MOVE_COST < nodeNear.getG()) {
                        nodeNear.setParent(node);
                        nodeNear.setG(node.getG() + OBLIQUE_MOVE_COST);
                        nodeNear.setF(nodeNear.getG() + nodeNear.getH());
                    }
                }
            }
        }

        if (E && S && ((y = getWalkableHorizontalOffset(vector3.add(1, 0, 1))) != -256)) {
            Vector3 vec = vector3.add(1, y, 1);
            if (noContainsInClose(vec) && isPassable(vec)) {
                Node nodeNear = getNodeInOpenByVector2(vec);
                if (nodeNear == null) {
                    this.openList.offer(new Node(vec, node, OBLIQUE_MOVE_COST + node.getG(), calHeuristic(vec, destination)));
                } else {
                    if (node.getG() + OBLIQUE_MOVE_COST < nodeNear.getG()) {
                        nodeNear.setParent(node);
                        nodeNear.setG(node.getG() + OBLIQUE_MOVE_COST);
                        nodeNear.setF(nodeNear.getG() + nodeNear.getH());
                    }
                }
            }
        }

        if (W && S && ((y = getWalkableHorizontalOffset(vector3.add(-1, 0, 1))) != -256)) {
            Vector3 vec = vector3.add(-1, y, 1);
            if (noContainsInClose(vec) && isPassable(vec)) {
                Node nodeNear = getNodeInOpenByVector2(vec);
                if (nodeNear == null) {
                    this.openList.offer(new Node(vec, node, OBLIQUE_MOVE_COST + node.getG(), calHeuristic(vec, destination)));
                } else {
                    if (node.getG() + OBLIQUE_MOVE_COST < nodeNear.getG()) {
                        nodeNear.setParent(node);
                        nodeNear.setG(node.getG() + OBLIQUE_MOVE_COST);
                        nodeNear.setF(nodeNear.getG() + nodeNear.getH());
                    }
                }
            }
        }

        if (W && N && ((y = getWalkableHorizontalOffset(vector3.add(-1, 0, -1))) != -256)) {
            Vector3 vec = vector3.add(-1, y, -1);
            if (noContainsInClose(vec) && isPassable(vec)) {
                Node nodeNear = getNodeInOpenByVector2(vec);
                if (nodeNear == null) {
                    this.openList.offer(new Node(vec, node, OBLIQUE_MOVE_COST + node.getG(), calHeuristic(vec, destination)));
                } else {
                    if (node.getG() + OBLIQUE_MOVE_COST < nodeNear.getG()) {
                        nodeNear.setParent(node);
                        nodeNear.setG(node.getG() + OBLIQUE_MOVE_COST);
                        nodeNear.setF(nodeNear.getG() + nodeNear.getH());
                    }
                }
            }
        }
    }

    private void resetTemporary() {
        this.openList.clear();
        this.closeList.clear();
        this.searchLimit = 100;
    }

    @Override
    public void search() {
        this.finished = false;
        this.searching = true;

        if (this.start == null) {
            this.start = this.entity;
        }

        if (this.destination == null) {
            Vector3 vec = entity.getTarget();
            if (vec != null) {
                this.destination = vec;
            } else {
                this.searching = false;
                this.finished = true;
                return;
            }
        }

        this.resetTemporary();

        Node presentNode = new Node(start);
        closeList.add(new Node(start));


        while (!isPositionOverlap(presentNode.getVector3(), destination)) {
            if (this.isInterrupted()) {
                searchLimit = 0;
                this.searching = false;
                this.finished = true;
                return;
            }
            putNeighborNodeIntoOpen(presentNode);
            if (openList.peek() != null && searchLimit-- > 0) {
                closeList.add(presentNode = openList.poll());

            } else {
                this.searching = false;
                this.finished = true;
                this.reachable = false;
                this.addNode(new Node(destination));
                return;
            }
        }

        if (!presentNode.getVector3().equals(destination)) {
            closeList.add(new Node(destination, presentNode, 0, 0));
        }
        ArrayList<Node> findingPath = getPathRoute();
        findingPath = FloydSmooth(findingPath);

        this.resetNodes();

        this.addNode(findingPath);
        this.finished = true;
        this.searching = false;
    }
}
