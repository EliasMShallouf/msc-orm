package com.eliasshallouf.msc.seminar2.service.utils.join;

import com.eliasshallouf.msc.seminar2.service.utils.helpers.JoinHelper;
import com.eliasshallouf.msc.seminar2.service.utils.query.Query;

public class Join<E1, E2> {
    private E1 left;
    private E2 right;
    private JoinQuery<E1, E2> on;

    public Join(E1 left, E2 right, JoinQuery<E1, E2> on) {
        this.left = left;
        this.right = right;
        this.on = on;
    }

    public E1 getLeft() {
        return left;
    }

    public E2 getRight() {
        return right;
    }

    public Query on() {
        return on.join(left, right);
    }

    public <E3> Join<Join<E1, E2>, E3> join(E3 e3, JoinQuery<Join<E1, E2>, E3> on) {
        return new Join<>(this, e3, on);
    }

    public String sql() {
        return JoinHelper.join(this);
    }
}
