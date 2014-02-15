package com.lantopia.libjava.state;

import org.jetbrains.annotations.NonNls;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.*;

/**
 * @author Mark McKenna %ltmark.denis.mckenna@gmail.com>
 * @version 0.1
 * @since 23/01/14
 *
 * Abstractly represents a state in a state machine.  This type of state is suitable for machines with no special
 * information attached to each state; i.e. the state is itself enough to specify how the machine should behave.
 *
 * Another, more elaborate type of state machine would attach information or behaviour to a state, such that the
 * state machine invokes some or all methods <i>on the state instance</i>, instead of on the machine itself; this
 * expands the cost of each state object, but reduces the complexity of the state machine (in many cases it can
 * reduce machine complexity to the point where it is pretty much just boiler-plate code).  This state machine is
 * not supported here; (TODO: support it using mapped policies)
 *
*/
@SuppressWarnings({"UnusedDeclaration", "PublicConstructor", "MethodParameterOfConcreteClass"})
public class StateImpl implements State {
    private final String name;
    private final Set<State> allowedPredecessors;

    StateImpl(@NotNull final State.Builder builder) {
        this.name = builder.name;
        this.allowedPredecessors = new TreeSet<>(builder.predecessors);
    }

    StateImpl(@NotNull final String name, @Nullable final State... predecessors) {
        this.name = name;
        allowedPredecessors = ((predecessors == null) || (predecessors.length == 0))
                ? AnyPredecessor
                : Collections.unmodifiableSet(new HashSet<>(Arrays.asList(predecessors)));
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

    @NonNls
    @Override
    public String toString() {
        return "State{" + name + '}';
    }
}
