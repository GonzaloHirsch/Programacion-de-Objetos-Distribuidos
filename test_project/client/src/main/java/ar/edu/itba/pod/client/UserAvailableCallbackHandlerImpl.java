package ar.edu.itba.pod.client;

import ar.edu.itba.pod.User;
import ar.edu.itba.pod.UserAvailableCallbackHandler;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class UserAvailableCallbackHandlerImpl implements UserAvailableCallbackHandler {
    private static Logger LOG = LoggerFactory.getLogger(UserAvailableCallbackHandlerImpl.class);

    @Override
    public void userAvailable(User user) {
        LOG.debug("Got user {}", user.getUsername());
    }
}
