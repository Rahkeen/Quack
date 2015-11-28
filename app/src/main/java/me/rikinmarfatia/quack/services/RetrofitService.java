package me.rikinmarfatia.quack.services;

import me.rikinmarfatia.quack.util.ServiceUrl;
import retrofit.GsonConverterFactory;
import retrofit.Retrofit;

/**
 * Generates API Service Classes using Retrofit
 *
 * @author Rikin (rikinm10@gmail.com)
 */
public final class RetrofitService {

    
    private RetrofitService() {
        // no need to instantiate
    }

    /**
     * Currently creates a DuckDuckGo service by default, if no URL provided
     */
    public static <S> S createService(Class<S> serviceClass) {
         return createService(serviceClass, ServiceUrl.DUCK_URL);
    }

    /**
     * Creates a service to a given REST API
     */
    public static <S> S createService(Class<S> serviceClass, String baseUrl) {
        Retrofit retrofit = new Retrofit.Builder()
                                    .baseUrl(baseUrl)
                                    .addConverterFactory(GsonConverterFactory.create())
                                    .build();

        return retrofit.create(serviceClass);
    }


}
