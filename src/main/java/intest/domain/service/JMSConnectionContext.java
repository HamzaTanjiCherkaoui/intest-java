package intest.domain.service;

import javax.jms.Connection;
import javax.jms.JMSException;
import javax.naming.Context;
import javax.naming.NamingException;

public class JMSConnectionContext {
    
    private final Context context;
    private final Connection connection;

    public JMSConnectionContext(Context context, Connection connection) {
        this.context = context;
        this.connection = connection;
    }

    public Context getContext() {
        return context;
    }

    public Connection getConnection() {
        return connection;
    }

    public void startListeners() {
        try {
            connection.start();
        } catch (JMSException e) {
            throw new RuntimeException(e);
        }
    }

    public void stopConnection () {
        try {
            if(connection != null) {
                connection.close();
            }
            if(context != null) {
                context.close();
            }
        } catch (NamingException | JMSException e) {
            throw new RuntimeException(e);
        }
    }
}
