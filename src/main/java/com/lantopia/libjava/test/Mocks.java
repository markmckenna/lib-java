package com.lantopia.libjava.test;

import com.lantopia.libjava.patterns.Callback;
import org.mockito.ArgumentCaptor;
import org.mockito.invocation.InvocationOnMock;
import org.mockito.stubbing.Answer;

import javax.annotation.Nullable;

/**
 * @author Mark McKenna &lt;mark.denis.mckenna@gmail.com&gt;
 * @version 0.1
 * @since 08/08/2014
 */
@SuppressWarnings("ProhibitedExceptionDeclared")
public class Mocks {
    private Mocks() {}

    public static <T> Answer<Void> callbackAnswer(final ArgumentCaptor<Callback<T>> callback, final T callbackArgument) {
        return new CallbackAnswer<>(callbackArgument, callback);
    }

    /**
     * Build this with a captor for a callback and a (probably mocked) callback argument.
     * Passing an instance of this in as a response object allows mocking of a method
     * whose results are paid out with a callback instead of via a return value.
     * <p/>
     * Example:
     * <code>
     *
     * @Captor ArgumentCaptor<Callback<String>> captor;
     * ...
     * MockitoAnnotations.initMocks(this);
     * ...
     * doAnswer(new CallbackAnswer("testresponse", captor))
     * .when(mockObject).perform(captor.capture());
     * </code>
     * <p/>
     * The above defines a mock which, when the method 'perform' is called with a Callback instance,
     * responds by invoking the callback with the String "testresponse".
     */
    private static class CallbackAnswer<T> implements Answer<Void> {
        private final T callbackArgument;
        private final ArgumentCaptor<Callback<T>> callback;

        public CallbackAnswer(final T callbackArgument, final ArgumentCaptor<Callback<T>> callback) {
            this.callbackArgument = callbackArgument;
            this.callback = callback;
        }

        @Nullable @Override
        public Void answer(final InvocationOnMock invocation) throws Throwable {
            callback.getValue().execute(callbackArgument);
            return null;
        }
    }
}
