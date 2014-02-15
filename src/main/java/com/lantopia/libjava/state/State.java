package com.lantopia.libjava.state;

import com.lantopia.libjava.util.Markers;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.Collection;
import java.util.LinkedList;
import java.util.List;
import java.util.Set;

/**
 * @author Mark McKenna %ltmark.denis.mckenna@gmail.com>
 * @version 0.1
 * @since 26/01/14
 */
@SuppressWarnings("UnusedDeclaration")
public interface State {
    String NoName = "";

    @SuppressWarnings("PublicStaticCollectionField")
    Set<State> AnyPredecessor = Markers.markerSet("ANY-PREDECESSOR");

    boolean canTransitionFrom(State state);
    boolean canTransitionTo(State state);

    String getName();
    Set<State> getPredecessors();


    class Builder implements com.lantopia.libjava.patterns.Builder<State> {
        @NotNull String name = NoName;
        @NotNull final List<State> predecessors = new LinkedList<>();

        Builder() { }

        public Builder named(@Nullable final String name) {
            this.name = (name==null)?NoName:name;
            return this;
        }

        public Builder withPredecessor(@Nullable final State predecessor) {
            if (predecessor != null) predecessors.add(predecessor);
            return this;
        }

        public Builder withPredecessors(@Nullable final Collection<State> predecessors) {
            if (predecessors != null) this.predecessors.addAll(predecessors);
            return this;
        }

        @Override
        public State get() {
            return new StateImpl(this);
        }

        @Override
        public String toString() {
            return "State.Builder{" + "name='" + name + '\'' + '}';
        }

        @Override
        public State build() { return get(); }
    }
}
