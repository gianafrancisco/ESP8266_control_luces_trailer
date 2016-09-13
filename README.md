# Control remoto de luces con ESP8266
Sistema para controlar remotamente desde una APP en android un modulo ESP8266 con 2 salidas rele.

En el directorio WebServer se encuentra el codigo para grabar en el modulo ESP8266, el codigo levanta un web server y escucha las peticiones GET /on y /off para activar las luces de freno y /balizas/on y /balizas/off para activar las balizas del trailer.

Para que fucione es necesario que el telefono android de encuentre en modo AP generando una señal de WIFI con el SSID ESP_WIFI y password 12345678 de esta forma el modulo ESP va a conectarse a la señal compartida por el telefono celular.

