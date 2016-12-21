package com.africastalking;


public final class AfricasTalking {

    private static final String BASE_PACKAGE = AfricasTalking.class.getPackage().getName();

    public static final String SERVICE_ACCOUNT = BASE_PACKAGE + ".AccountService";
    public static final String SERVICE_SMS = BASE_PACKAGE + ".SMSService";
    public static final String SERVICE_VOICE = BASE_PACKAGE + ".VoiceService";
    public static final String SERVICE_USSD = BASE_PACKAGE + ".USSDService";
    public static final String SERVICE_AIRTIME = BASE_PACKAGE + ".AirtimeService";
    public static final String SERVICE_PAYMENTS = BASE_PACKAGE + ".PaymentsService";


    private static String sUsername, sApiKey;
    private static Format sFormat;
    private static Boolean DEBUG;


    /**
     * Initialize the SDK
     * @param username
     * @param apiKey
     * @param format
     * @param debug
     */
    public static void initialize(String username, String apiKey, Format format, boolean debug) {

        destroyAllServices();

        // Init
        DEBUG = debug;
        sUsername = username;
        sApiKey = apiKey;
        sFormat = format;
    }

    /**
     *
     * @param username
     * @param apiKey
     * @param format
     */
    public static void initialize(String username, String apiKey, Format format) {
        initialize(username, apiKey, format, false);
    }

    /**
     *
     * @param username
     * @param apiKey
     */
    public static void initialize(String username, String apiKey) {
        initialize(username, apiKey, Format.XML, false);
    }


    /**
     * Get a service by class. e.g. SMService.class
     * @param classInfo
     * @param <T>
     * @return
     */
    public static <T extends Service> T getService(Class<T> classInfo) {
        try {
            T raw = classInfo.newInstance();

            if (sApiKey == null || sUsername == null) {
                return raw;
            }
            return (T)raw.getInstance(sUsername, sApiKey, sFormat, DEBUG);
        } catch (InstantiationException e) {
            e.printStackTrace();
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        }

        throw new RuntimeException("Failed to init service");
    }

    /**
     * Get a service by name
     * @param serviceName see AfricasTalking.SERVICES_*
     * @param <T>
     * @return
     */
    public static <T extends Service> T getService(String serviceName) {

        try {
            Class<T> tClass = (Class<T>)Class.forName(serviceName);
            return getService(tClass);
        } catch (ClassNotFoundException e) {
            //e.printStackTrace();
            return null;
        }
    }

    /**
     * Destroy all initialized services
     */
    private static void destroyAllServices() {
        Service services[] = new Service[] {
                getService(SERVICE_ACCOUNT),
                getService(SERVICE_VOICE),
                getService(SERVICE_SMS),
                getService(SERVICE_USSD),
                getService(SERVICE_AIRTIME),
                getService(SERVICE_PAYMENTS)
        };
        for (Service service:services) {
            if (service != null && service.isInitialized()) {
                service.destroyService();
            }
        }
    }

}