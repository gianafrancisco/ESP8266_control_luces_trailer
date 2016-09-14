package fransis.homecontrol;

import retrofit2.http.GET;

/**
 * Created by francisco on 14/09/2016.
 */
public interface ArduinoService {
    @GET("on")
    String frenosOn();
    @GET("off")
    String frenosOff();
    @GET("balizas/on")
    String balizasOn();
    @GET("balizas/off")
    String balizasOff();
}
