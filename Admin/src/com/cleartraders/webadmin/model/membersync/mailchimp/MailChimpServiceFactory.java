package com.cleartraders.webadmin.model.membersync.mailchimp;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.lang.reflect.Proxy;
import java.net.URL;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.xmlrpc.XmlRpcException;
import org.apache.xmlrpc.client.XmlRpcClient;
import org.apache.xmlrpc.client.XmlRpcClientConfigImpl;
import org.apache.xmlrpc.common.TypeConverter;
import org.apache.xmlrpc.common.TypeConverterFactory;
import org.apache.xmlrpc.common.TypeConverterFactoryImpl;
import org.apache.xmlrpc.common.XmlRpcInvocationException;

public class MailChimpServiceFactory
{
    private static final Log logger = LogFactory.getLog(MailChimpServiceFactory.class);

    public static IMailChimpServices getMailChimpServices()
    {
        IMailChimpServices result = null;
        try
        {
            final XmlRpcClientConfigImpl config = new XmlRpcClientConfigImpl();
            config.setEnabledForExtensions(true);
            config.setServerURL(new URL("http://api.mailchimp.com/1.2/"));
            final XmlRpcClient client = new XmlRpcClient();
            client.setConfig(config);
            final ClientFactory factory = new ClientFactory(client);

            result = (IMailChimpServices) factory.newInstance(IMailChimpServices.class.getClassLoader(),
                    IMailChimpServices.class);
        }
        catch (final Throwable t)
        {
            logger.error("Failed to connect to mailchimp services", t);
        }
        return result;
    }

    private static class ClientFactory
    {
        private final XmlRpcClient client;

        private final TypeConverterFactory typeConverterFactory;

        private boolean objectMethodLocal;

        /**
         * Creates a new instance.
         * 
         * @param pClient A fully configured XML-RPC client, which is used
         *            internally to perform XML-RPC calls.
         * @param pTypeConverterFactory Creates instances of
         *            {@link TypeConverterFactory}, which are used to transform
         *            the result object in its target representation.
         */
        public ClientFactory(final XmlRpcClient pClient, final TypeConverterFactory pTypeConverterFactory)
        {
            typeConverterFactory = pTypeConverterFactory;
            client = pClient;
        }

        /**
         * Creates a new instance. Shortcut for
         * 
         * <pre>
         * new ClientFactory(pClient, new TypeConverterFactoryImpl());
         * </pre>
         * 
         * @param pClient A fully configured XML-RPC client, which is used
         *            internally to perform XML-RPC calls.
         * @see TypeConverterFactoryImpl
         */
        public ClientFactory(final XmlRpcClient pClient)
        {
            this(pClient, new TypeConverterFactoryImpl());
        }

        /**
         * Returns the factories client.
         */
        public XmlRpcClient getClient()
        {
            return client;
        }

        /**
         * Returns, whether a method declared by the {@link Object Object class}
         * is performed by the local object, rather than by the server. Defaults
         * to true.
         */
        public boolean isObjectMethodLocal()
        {
            return objectMethodLocal;
        }

        /**
         * Sets, whether a method declared by the {@link Object Object class} is
         * performed by the local object, rather than by the server. Defaults to
         * true.
         */
        public void setObjectMethodLocal(final boolean pObjectMethodLocal)
        {
            objectMethodLocal = pObjectMethodLocal;
        }

        /**
         * Creates an object, which is implementing the given interface. The
         * objects methods are internally calling an XML-RPC server by using the
         * factories client; shortcut for
         * 
         * <pre>
         * newInstance(Thread.currentThread().getContextClassLoader(), pClass)
         * </pre>
         */
        public Object newInstance(final Class<?> pClass)
        {
            return newInstance(Thread.currentThread().getContextClassLoader(), pClass);
        }

        /**
         * Creates an object, which is implementing the given interface. The
         * objects methods are internally calling an XML-RPC server by using the
         * factories client; shortcut for
         * 
         * <pre>
         * newInstance(pClassLoader, pClass, pClass.getName())
         * </pre>
         */
        public Object newInstance(final ClassLoader pClassLoader, final Class<?> pClass)
        {
            return newInstance(pClassLoader, pClass, pClass.getName());
        }

        /**
         * Creates an object, which is implementing the given interface. The
         * objects methods are internally calling an XML-RPC server by using the
         * factories client.
         * 
         * @param pClassLoader The class loader, which is being used for loading
         *            classes, if required.
         * @param pClass Interface, which is being implemented.
         * @param pRemoteName Handler name, which is being used when calling the
         *            server. This is used for composing the method name. For
         *            example, if <code>pRemoteName</code> is "Foo" and you
         *            want to invoke the method "bar" in the handler, then the
         *            full method name would be "Foo.bar".
         */
        public Object newInstance(final ClassLoader pClassLoader, final Class<?> pClass, final String pRemoteName)
        {
            return Proxy.newProxyInstance(pClassLoader, new Class[] { pClass }, new InvocationHandler()
            {
                public Object invoke(final Object pProxy, final Method pMethod, final Object[] pArgs) throws Throwable
                {
                    if (isObjectMethodLocal() && pMethod.getDeclaringClass().equals(Object.class))
                    {
                        return pMethod.invoke(pProxy, pArgs);
                    }
                    final String methodName = pMethod.getName();
                    Object result;
                    try
                    {
                        result = client.execute(methodName, pArgs);
                    }
                    catch (final XmlRpcInvocationException e)
                    {
                        final Throwable t = e.linkedException;
                        if (t instanceof RuntimeException)
                        {
                            throw t;
                        }
                        final Class<?>[] exceptionTypes = pMethod.getExceptionTypes();
                        for (final Class<?> c : exceptionTypes)
                        {
                            if (c.isAssignableFrom(t.getClass()))
                            {
                                throw t;
                            }
                        }
                        String message = t.getMessage();
                        if (message == null)
                        {
                            message = "Unknown Error";
                        }
                        throw new MailChimpServiceException(message, e.code);
                    }
                    catch (final XmlRpcException e)
                    {
                        String message = e.getMessage();
                        if ((message == null) || (message.length() == 0))
                        {
                            message = "Unknown Error";
                        }
                        throw new MailChimpServiceException(message, e.code);
                    }
                    final TypeConverter typeConverter = typeConverterFactory.getTypeConverter(pMethod.getReturnType());
                    return typeConverter.convert(result);
                }
            });
        }
    }
}
