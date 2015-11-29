package me.rikinmarfatia.quack.services;

import me.rikinmarfatia.quack.models.SearchResult;
import retrofit.Call;
import retrofit.http.GET;
import retrofit.http.Query;

/**
 * Used with Retrofit to connect to Duck Duck Go API
 *
 * @author Rikin (rikinm10@gmail.com)
 */
public interface DuckDuckGoService {

    @GET("/")
    Call<SearchResult> search (@Query("q") String query,
                               @Query("format") String format,
                               @Query("skip_disambig") int disambig);
}
