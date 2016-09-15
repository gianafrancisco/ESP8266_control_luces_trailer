package fransis.homecontrol;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Response;
import retrofit2.http.GET;

/**
 * Created by francisco on 14/09/2016.
 */
public interface ArduinoService {
    @GET("/on")
    Call<String> frenosOn();
    @GET("/off")
    Call<String> frenosOff();
    @GET("/balizas/on")
    Call<String> balizasOn();
    @GET("/balizas/off")
    Call<String> balizasOff();
}
