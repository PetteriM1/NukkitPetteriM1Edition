package cn.nukkit.entity.route;

import cn.nukkit.entity.EntityWalking;

/**
 * @author zzz1999 @ MobPlugin
 */
class SimpleRouteFinder extends RouteFinder {

    SimpleRouteFinder(EntityWalking entity) {
        super(entity);
    }

    @Override
    public void search() {
        this.resetNodes();
        this.addNode(new Node(this.destination));
    }
}