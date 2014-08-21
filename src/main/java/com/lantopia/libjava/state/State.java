package com.lantopia.libjava.state;

import com.google.common.base.Optional;
import com.lantopia.libjava.util.Markers;

import javax.annotation.Nonnull;
import javax.inject.Provider;
import java.util.*;

import static com.lantopia.libjava.util.BuilderTools.require;

/**
 * @author Mark McKenna %ltmark.denis.mckenna@gmail.com>
 * @version 0.1
 * @since 26/01/14
 */
@SuppressWarnings("UnusedDeclaration")
public interface State {
    @SuppressWarnings("PublicStaticCollectionField")
    Set<State> AnyPredecessor = Markers.markerSet("ANY-PREDECESSOR");

    boolean canTransitionFrom(State state);

    boolean canTransitionTo(State state);

    String getName();

    Set<State> getPredecessors();


    @SuppressWarnings("PackageVisibleField")
    class Builder implements Provider<State> {
        @Nonnull final List<State> mPredecessors = new LinkedList<>();
        @Nonnull Optional<String> mName = Optional.absent();

        Builder() { }

        public Builder named(final Optional<String> name) {
            this.mName = name;
            return this;
        }

        public Builder withPredecessor(final State predecessor) {
            mPredecessors.add(predecessor);
            return this;
        }

        public Builder withPredecessors(final Collection<State> predecessors) {
            this.mPredecessors.addAll(predecessors);
            return this;
        }

        public Builder withoutPredecessor(final State predecessor) {
            mPredecessors.remove(predecessor);
            return this;
        }

        public Builder withoutAllPredecessors() {
            mPredecessors.clear();
            return this;
        }

        @Override
        public State get() { return new StateImpl(this); }

        @Override
        public String toString() { return "State.Builder{" + "name='" + mName + '\'' + '}'; }
    }

    /**
     * @author Mark McKenna %ltmark.denis.mckenna@gmail.com>
     * @version 0.1
     * @since 23/01/14
     * <p/>
     * Abstractly represents a state in a state machine.  This type of state is suitable for machines with no special
     * information attached to each state; i.e. the state is itself enough to specify how the machine should behave.
     * <p/>
     * Another, more elaborate type of state machine would attach information or behaviour to a state, such that the
     * state machine invokes some or all methods <i>on the state instance</i>, instead of on the machine itself; this
     * expands the cost of each state object, but reduces the complexity of the state machine (in many cases it can
     * reduce machine complexity to the point where it is pretty much just boiler-plate code).  This state machine is
     * not supported here; (TODO: support it using mapped policies)
     */
    @SuppressWarnings({"UnusedDeclaration", "PublicConstructor", "MethodParameterOfConcreteClass"})
    class StateImpl implements State {
        private final String name;
        private final Set<State> allowedPredecessors;

        StateImpl(final Builder builder) {
            this.name = require(builder.mName, "name");
            this.allowedPredecessors = new TreeSet<>(builder.mPredecessors);
        }

        @Override public boolean canTransitionFrom(final State state) {
            return allowedPredecessors.contains(state);
        }

        @Override public boolean canTransitionTo(final State state) {
            return allowedPredecessors.contains(this);
        }

        @Override
        public String getName() { return name; }

        @SuppressWarnings("ReturnOfCollectionOrArrayField")
        @Override
        public Set<State> getPredecessors() {
            return allowedPredecessors;
        }

        @Override
        public String toString() {
            return "State{" + name + '}';
        }
    }
}
