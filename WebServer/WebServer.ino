#include <ESP8266WiFi.h>
#include <ESP8266WebServer.h>

const char* ssid = "ESP_WIFI";
const char* password = "12345678";

const int PIN_RELE = 2;   //Es el puerto GPIO2
const int PIN_RELE_0 = 0;   //Es el puerto GPIO2
String ESTADO_RELE = "OFF";
String ESTADO_RELE_0 = "OFF";
bool ESTADO_BALIZAS = false;
bool BALIZAS_ESTADO_ACTUAL = false;

#define DUTY_CICLE 400

ESP8266WebServer server(80);


void info() {
  String info_mensaje = "<b>ESP8266</b><br>Comando <b>/on</b>: encender freno<br>Comando <b>/off</b>: apagar freno<br><br><b>/balizas/on</b>: encender balizas<br>Comando <b>/balizas/off</b>: apagar balizas<br><br>";
  String str_estado = ESTADO_RELE;
  info_mensaje = info_mensaje + "Estado freno:" + str_estado + "<br>Estado balizas: " + ESTADO_RELE_0;
  server.send(200, "text/html", info_mensaje);
}

void estado_rele() {
  server.send(200, "text/plain", ESTADO_RELE);
}

void no_encontrado() {
  server.send(404, "text/plain", "ERROR EN LA PETICION :( ");
}

void on_rele() {
  server.send(200, "text/plain", "OK ON");
  ESTADO_RELE = "ON";
  digitalWrite(PIN_RELE, LOW);
}

void off_rele() {
  server.send(200, "text/plain", "OK OFF");
  ESTADO_RELE = "OFF";
  digitalWrite(PIN_RELE, HIGH);
}

void on_balizas() {
  server.send(200, "text/plain", "OK ON");
  ESTADO_RELE_0 = "ON";
  ESTADO_BALIZAS = true;
}

void off_balizas() {
  server.send(200, "text/plain", "OK OFF");
  ESTADO_RELE_0 = "OFF";
  ESTADO_BALIZAS = false;
  digitalWrite(PIN_RELE_0, HIGH);
}


void setup(void) {
  pinMode(PIN_RELE, OUTPUT);
  pinMode(PIN_RELE_0, OUTPUT);
  digitalWrite(PIN_RELE, HIGH);
  digitalWrite(PIN_RELE_0, HIGH);

  Serial.begin(115200);
  WiFi.begin(ssid, password);
  Serial.println("");

  while (WiFi.status() != WL_CONNECTED) {
    delay(500);
    Serial.print(".");
  }
  Serial.println("");
  Serial.print("Conectedo a ");
  Serial.println(ssid);
  Serial.print("Dirección IP: ");
  Serial.println(WiFi.localIP());

  IPAddress sensor_ip = WiFi.localIP();
  
  server.on("/", info);
  server.on("/estado", estado_rele);

  server.on("/on", on_rele);
  server.on("/off", off_rele);

  server.on("/balizas/on", on_balizas);
  server.on("/balizas/off", off_balizas);

  server.onNotFound(no_encontrado);

  server.begin();
  Serial.println("Servidor HTTP activo");
  
}
unsigned long start = 0;
void loop(void) {
  server.handleClient();
  if(ESTADO_BALIZAS == true){
    if(int(millis() - start) > DUTY_CICLE){
      start = millis();
      if(BALIZAS_ESTADO_ACTUAL == false){
        BALIZAS_ESTADO_ACTUAL = true;
        digitalWrite(PIN_RELE_0, LOW);
      }else{
        BALIZAS_ESTADO_ACTUAL = false;
        digitalWrite(PIN_RELE_0, HIGH);
      }
     }
  }
}
