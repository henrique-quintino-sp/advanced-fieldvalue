package dao;

import sailpoint.api.SailPointContext;
import sailpoint.api.SailPointFactory;
import sailpoint.spring.SpringStarter;
import sailpoint.tools.GeneralException;

public class IIQInstance {
    private boolean connected = false;
    private final SpringStarter ss = new SpringStarter("iiqBeans",null);
    private volatile SailPointContext context;
    private static volatile IIQInstance iiqinstance;
    // Private constructor to prevent instantiation
    private IIQInstance() {
    }
    /**
     * Get the current instance of the class
     *
     * @return IIQInstance - the current instance of this class
     */
    public final static IIQInstance getIIQInstance() {
        if (iiqinstance == null) {
            synchronized (IIQInstance.class) {
                if (iiqinstance == null) {
                    iiqinstance = new IIQInstance();
                }
            }
        }
        return iiqinstance;
    }
    /**
     * Connects to IIQ and returns a context
     *
     * @return SailPointContext
     * @throws GeneralException
     */
    public final static SailPointContext instantContext()
            throws GeneralException {
        iiqinstance.connect();
        return iiqinstance.getContext();
    }
    /**
     * Connects to the IIQ database
     */
    public void connect() {
        if (!connected) {
            // 6.1 version
            // Suppress the schedulers
            // SpringStarter.setSuppressTaskScheduler(true);
            // SpringStarter.setSuppressRequestScheduler(true);
            // 6.2 version
            final String[] suppressedServices = {"Request", "Task", "FullText", "SMListener"};
            SpringStarter.setSuppressedServices(suppressedServices);
            SpringStarter.suppressSchedulers();
            // Create the SpringStarter and connect

            String configFile = ss.getConfigFile();

            ss.start();
            connected = true;
        }
    }
    /**
     * Closes the IIQ connection
     *
     * @throws GeneralException
     */
    public void close() throws GeneralException {
        if (connected) {
            ss.close();
        }
        if (context != null) {
            context.close();
        }
    }
    /**
     * @return connected
     */
    public final boolean isConnected() {
        return connected;
    }
    /**
     * @return the SpringStarter
     */
    public final SpringStarter getSpringStarter() {
        return ss;
    }
    /**
     * @return the context
     * @throws GeneralException
     */
    public final SailPointContext getContext() throws GeneralException {
        if (context == null) {
            synchronized (IIQInstance.class) {
                if (context == null) {
                    context = SailPointFactory.createContext();
                }
            }
        }
        return context;
    }
}